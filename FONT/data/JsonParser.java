package data;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.*;

/**
 * Parsejador generic de la representacio d'objectes del dominini en fitxers en
 * format JSON.
 *
 * @author Roberto Navarro Morales
 *
 */
public class JsonParser {

    /**
     * Instancia unica del parsejador
     */
    private static JsonParser singletonObject;

    public static JsonParser getInstance() {
        if (singletonObject == null) {
            singletonObject = new JsonParser() {
            };
        }
        return singletonObject;
    }

    private JsonParser() {
    }

    /**
     * Carregar objectes d'un tipus deternimat
     *
     * @param objectType tipus de l'objecte
     * @param filename fitxer d'on es carrega, pot ser path relatiu o absolut (.json)
     * @param arrayName nom de l'array que conte els objectes
     * @return els objectes llegits (tots del mateix tipus)
     * @throws Exception
     */
    public List<Object> loadObjectsFullPath(Class<?> objectType, String filename, String arrayName) throws Exception {
        File f = new File(filename);

        List<Object> l = new ArrayList<Object>();

        if (f.exists()) {
            String content = new String(Files.readAllBytes(Paths.get(f.toURI())));

            JSONObject json = new JSONObject(content);
            JSONArray arr = json.getJSONArray(arrayName);

            for (int i = 0; i < arr.length(); i++) {
                JSONObject iobj = arr.getJSONObject(i);
                l.add(loadObject(iobj, objectType));
            }
        } else {
            throw new Exception("File " + filename + " not found");
        }

        return l;
    }
    
    /**
     * Carregar objectes d'un tipus deternimat
     *
     * @param objectType tipus de l'objecte
     * @param filename fitxer d'on es carrega (carpeta DATA) (.json)
     * @param arrayName nom de l'array que conte els objectes
     * @return els objectes llegits (tots del mateix tipus)
     * @throws Exception
     */
    public List<Object> loadObjects(Class<?> objectType, String filename, String arrayName) throws Exception {
        return loadObjectsFullPath(objectType, "./DATA/" + filename, arrayName);
    }

    /**
     * Parsejar un objecte de json
     *
     * @param json l'objecte json
     * @param objectType el tipus de l'objecte que es vol parsejar
     * @return l'objecte parsejat
     * @throws Exception
     */
    private Object loadObject(JSONObject json, Class<?> objectType) throws Exception {
        ArrayList<Class<?>> nonDirConstructible = new ArrayList<Class<?>>();
        nonDirConstructible.add(Set.class);
        nonDirConstructible.add(Map.class);
        nonDirConstructible.add(List.class);

        // Trobar el tipus de la classe derivada
        if (Modifier.isAbstract(objectType.getModifiers())) {
            objectType = deduceDerived(objectType, json);
        }

        Constructor<?>[] constructors = objectType.getDeclaredConstructors();

        if (constructors.length != 1) {
            throw new Exception("No es pot determinar el constructor de la classe a fer servir");
        }

        // Obtenir els parametres del constructor
        Constructor<?> ctor = constructors[0];
        Class<?>[] parameters = ctor.getParameterTypes();

        // Obtenir les propietats
        List<Field> fields = getAllFields(new ArrayList<Field>(), objectType, true);

        LinkedList<Field> toResolve = new LinkedList<Field>();
        ArrayList<Field> resolved = new ArrayList<Field>();

        int ctorTFields = 0;
        for (int i = 0; i < fields.size(); i++) {
            boolean nonDir = false;
            for (Class<?> clazz : nonDirConstructible) {
                if (clazz.isAssignableFrom(fields.get(i).getType())) {
                    nonDir = true;
                    break;
                }
            }

            if (!nonDir) {
                ctorTFields++;
                resolved.add(fields.get(i));
            } else {
                toResolve.add(fields.get(i));
            }
        }

        // Comparar si els parametres i les propietats coincideixen exactament
        if (parameters.length != ctorTFields) {
            throw new Exception("La quantitat de parametres que rep el constructor i les propietats de la classe difereixen");
        }

        Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Field field = resolved.get(i);
            if (field.getType() != parameters[i]) {
                throw new Exception("El tipus de la propietat " + fields.get(i).getName() + " no es pot relacionar amb el ctor");
            }

            args[i] = parseBuiltinField(field.getType(), field.getName(), json);
            if (args[i] == null) {
                args[i] = loadObject(json.getJSONObject(field.getName()), field.getType());
            }
        }

        Object obj = ctor.newInstance(args);

        while (!toResolve.isEmpty()) {
            Field field = toResolve.getFirst();
            if (Map.class.isAssignableFrom(field.getType())) {
                loadMap(field, obj, objectType, json.getJSONArray(field.getName()));
            } else if (List.class.isAssignableFrom(field.getType())
                    || Set.class.isAssignableFrom(field.getType())) {
                loadList(field, obj, objectType, json.getJSONArray(field.getName()));
            }

            toResolve.removeFirst();
        }

        return obj;
    }

    private Class<?> deduceDerived(Class<?> base, JSONObject json) throws Exception {
        // Obtenir el powertype
        List<Field> fields = getAllFields(new ArrayList<Field>(), base, false);

        Object powerType = null;
        for (Field field : fields) {
            if (field.getAnnotation(JsonBaseDeduce.class) != null) {
                powerType = parseBuiltinField(field.getType(), field.getName(), json);
                break;
            }
        }

        if (powerType == null) {
            throw new Exception("No s'ha pogut obtenir el powertype per " + base.toString());
        }

        Map<Object, Class<?>> m = (Map<Object, Class<?>>) base.getField("baseDeduce").get(null);

        return m.get(powerType);
    }

    /**
     * Carregar un camp de tipus map d'una classe
     *
     * @param field la propietat del tipus map
     * @param object l'objecte de la classe que s'esta tractant
     * @param parentType el tipus de l'objecte que conte el map
     * @param array l'array json que conte la informacio per carregar el map
     * @throws Exception
     */
    private void loadMap(Field field, Object object, Class<?> parentType, JSONArray array) throws Exception {
        ParameterizedType type = (ParameterizedType) field.getGenericType();
        Class<?> v = (Class<?>) type.getActualTypeArguments()[1];

        addToObject(field, object, v, parentType, array);
    }

    /**
     * Carregar un camp d'un tipus llista d'una classe
     *
     * @param field la propietat del tipus llista
     * @param object l'objecte de la classe que s'esta tractant
     * @param parentType el tipus de l'objecte que conte la llista
     * @param array l'array json que conte la informacio per carregar la llista
     * @throws Exception
     */
    private void loadList(Field field, Object object, Class<?> parentType, JSONArray array) throws Exception {
        ParameterizedType type = (ParameterizedType) field.getGenericType();
        Class<?> v = (Class<?>) type.getActualTypeArguments()[0];

        addToObject(field, object, v, parentType, array);
    }

    /**
     * Invocar el metode per afegir un objecte a l'objecte que el conte
     * (composicio)
     *
     * @param field camp que conte els objectes
     * @param object l'objecte pare
     * @param childType el tipus de l'objecte fill
     * @param parentType el tipus de l'objecte pare
     * @param array l'array que conte els objectes fills
     * @throws Exception
     */
    private void addToObject(Field field, Object object, Class<?> childType, Class<?> parentType, JSONArray array) throws Exception {
        boolean keyValLike = field.getAnnotation(JsonKeyValue.class) != null;

        String funcName = field.getAnnotation(JsonAdd.class).value();
        Method[] methods = parentType.getDeclaredMethods();

        Method method = null;
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().compareTo(funcName) == 0) {
                method = methods[i];
                break;
            }
        }

        if (method == null) {
            throw new Exception("No es pot trobar una funcio per afegir el fill");
        }

        List<Field> fields = getAllFields(new ArrayList<Field>(), childType, true);
        Class<?>[] parameters = method.getParameterTypes();

        for (int i = 0; i < array.length(); i++) {
            JSONObject json = array.optJSONObject(i);

            Object[] args = null;

            // Builtin array
            if (json == null) {
                args = new Object[1];
                args[0] = parseBuiltinField(parameters[0], array, i);

                if (args[0] == null) {
                    throw new Exception("S'esperava un tipus basic a l'array");
                }
            } else {
                if (keyValLike) {
                    if (parameters.length != 2) {
                        throw new Exception("El constructor no te la quantitat d'arguments requerida");
                    }

                    args = new Object[2];

                    args[0] = json.get("key");
                    args[1] = parseBuiltinField(parameters[1], "value", json);
                    if (args[1] == null) {
                        args[1] = loadObject(json.getJSONObject("value"), parameters[1]);
                    }
                } else {
                    for (int j = 0; j < fields.size(); j++) {
                        Field f = fields.get(j);
                        args = new Object[fields.size()];

                        if (f.getType() != parameters[j]) {
                            throw new Exception("El tipus de la propietat " + f.getName() + " no es pot relacionar amb el metode");
                        }

                        args[j] = parseBuiltinField(f.getType(), f.getName(), json);
                        if (args[j] == null) {
                            args[1] = loadObject(json, f.getType());
                        }
                    }
                }
            }

            method.invoke(object, args);
        }
    }

    /**
     * Llegir un valor del json com un tipus basic (int, double, boolean, enum)
     *
     * @param clazz el tipus de l'objecte
     * @param key la clau a l'objecte json
     * @param json l'objecte json
     * @return el valor del json convertit, null s'hi no s'ha pogut covertir
     */
    public Object parseBuiltinField(Class<?> clazz, String key, JSONObject json) {
        if (clazz == Integer.TYPE || clazz == Integer.class) {
            return json.getInt(key);
        } else if (clazz == Double.TYPE || clazz == Double.class) {
            return json.getDouble(key);
        } else if (clazz == Boolean.TYPE || clazz == Boolean.class) {
            return json.getBoolean(key);
        } else if (clazz.isEnum()) {
            return Enum.valueOf((Class<? extends Enum>) clazz, json.getString(key));
        } else if (clazz == String.class) {
            return json.getString(key);
        }

        return null;
    }

    public Object parseBuiltinField(Class<?> clazz, JSONArray array, int i) {
        if (clazz == Integer.TYPE || clazz == Integer.class) {
            return array.getInt(i);
        } else if (clazz == Double.TYPE || clazz == Double.class) {
            return array.getDouble(i);
        } else if (clazz == Boolean.TYPE || clazz == Boolean.class) {
            return array.getBoolean(i);
        } else if (clazz.isEnum()) {
            return Enum.valueOf((Class<? extends Enum>) clazz, array.getString(i));
        } else if (clazz == String.class) {
            return array.getString(i);
        }

        return null;
    }
    
    /**
     * Guardar un conjunt d'objects d'un mateix tipus en un fitxer json 
     *
     * @param <T> el tipus dels objectes
     * @param objects el llistat d'objectes
     * @param objectType la classe dels objectes
     * @param filename l'arxiu on s'han de guardar, pot portar path relatiu o absolut (extensio .json)
     * @param arrayName el nom de l'array que contindra els objectes al fitxer
     * @throws Exception
     */
    public <T> void saveObjectsFullPath(List<T> objects, Class<T> objectType, String filename, String arrayName) throws Exception {
        FileWriter file = new FileWriter(filename);

        JSONObject json_root = new JSONObject();
        JSONArray arr = new JSONArray();

        for (T obj : objects) {
            JSONObject json_obj = new JSONObject();
            saveObject(objectType, obj, json_obj);

            arr.put(json_obj);
        }

        json_root.put(arrayName, arr);
        file.write(json_root.toString(2));
        file.close();
    }

    /**
     * Guardar un conjunt d'objects d'un mateix tipus en un fitxer json al directori DATA
     *
     * @param <T> el tipus dels objectes
     * @param objects el llistat d'objectes
     * @param objectType la classe dels objectes
     * @param filename l'arxiu on s'han de guardar, sense path (extensio .json)
     * @param arrayName el nom de l'array que contindra els objectes al fitxer
     * @throws Exception
     */
    public <T> void saveObjects(List<T> objects, Class<T> objectType, String filename, String arrayName) throws Exception {
        saveObjectsFullPath(objects, objectType, "./DATA/" + filename, arrayName);
    }

    /**
     * Convertir un objecte d'una classe del domini a un objecte json
     *
     * @param objectType el tipus de l'objecte
     * @param object l'objecte a convertir
     * @param json l'objecte json on s'ha d'afegir
     * @throws Exception
     */
    private void saveObject(Class<?> objectType, Object object, JSONObject json) throws Exception {
        // Obtenir totes les propietats de l'objecte
        List<Field> fields = getAllFields(new ArrayList<Field>(), objectType, false);

        for (Field field : fields) {
            // Obtenir el getter de la propietat
            String fieldName = field.getName();
            String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

            if (!existsMethod(objectType, getterName)) {
                getterName = "is" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            }

            Method getter = objectType.getMethod(getterName);

            Object prop = getter.invoke(object);

            boolean saved = saveBuiltinIntoObj(field.getType(), field.getName(), prop, json);

            if (!saved) {
                if (Map.class.isAssignableFrom(field.getType())) {
                    JSONArray array = new JSONArray();

                    saveMap(field, prop, array);
                    json.put(field.getName(), array);
                } else if (List.class.isAssignableFrom(field.getType())
                        || Set.class.isAssignableFrom(field.getType())) {
                    JSONArray array = new JSONArray();

                    saveList(field, prop, array);
                    json.put(field.getName(), array);
                } else {
                    JSONObject innerObj = new JSONObject();
                    saveObject(prop.getClass(), prop, innerObj);

                    json.put(field.getName(), innerObj);
                }
            }
        }
    }

    private boolean saveBuiltinIntoObj(Class<?> clazz, String key, Object prop, JSONObject json) {
        if (clazz == Integer.TYPE || clazz == Integer.class) {
            json.put(key, (int) prop);
            return true;
        } else if (clazz == Double.TYPE || clazz == Double.class) {
            json.put(key, (double) prop);
            return true;
        } else if (clazz == Boolean.TYPE || clazz == Boolean.class) {
            json.put(key, (boolean) prop);
            return true;
        } else if (clazz == String.class || clazz.isEnum()) {
            json.put(key, prop.toString());
            return true;
        }

        return false;
    }

    private boolean saveBuiltinIntoArr(Class<?> clazz, Object prop, JSONArray array) {
        if (clazz == Integer.TYPE || clazz == Integer.class) {
            array.put((int) prop);
            return true;
        } else if (clazz == Double.TYPE || clazz == Double.class) {
            array.put((double) prop);
            return true;
        } else if (clazz == Boolean.TYPE || clazz == Boolean.class) {
            array.put((boolean) prop);
            return true;
        } else if (clazz == String.class || clazz.isEnum()) {
            array.put(prop.toString());
            return true;
        }

        return false;
    }

    /**
     * Guardar un map en un array json
     *
     * @param field camp del tipus map
     * @param prop l'objecte que conte el map
     * @param array l'array del json on s'han d'afegir els objectes
     * @throws Exception
     */
    private void saveMap(Field field, Object prop, JSONArray array) throws Exception {
        ParameterizedType type = (ParameterizedType) field.getGenericType();
        boolean keyVal = field.getAnnotation(JsonKeyValue.class) != null;

        saveMapObject(type, prop, array, keyVal);
    }

    private void saveMapObject(ParameterizedType type, Object object, JSONArray array, boolean keyVal) throws Exception {
        Map<Object, Object> m = (Map<Object, Object>) object;

        for (Map.Entry<Object, Object> kv : m.entrySet()) {
            JSONObject json_obj = new JSONObject();

            if (Map.class.isAssignableFrom(kv.getValue().getClass())) {
                if (!keyVal) {
                    throw new Exception("Nomes s'accepta el format clau-valor");
                }

                JSONArray inArray = new JSONArray();

                saveMapObject(
                        (ParameterizedType) type.getActualTypeArguments()[1],
                        kv.getValue(),
                        inArray,
                        keyVal
                );

                json_obj.put("key", kv.getKey());
                json_obj.put("value", inArray);

                array.put(json_obj);
            } //else if (Set.class.isAssignableFrom(kv.getValue().getClass()) || )
            //	List.class
            else {
                Class<?> v = (Class<?>) type.getActualTypeArguments()[1];

                if (keyVal) {
                    json_obj.put("key", kv.getKey());

                    // Intentar guardar-lo directament primer
                    boolean saved = saveBuiltinIntoObj(v, "value", kv.getValue(), json_obj);
                    if (!saved) {
                        JSONObject inner_obj = new JSONObject();
                        saveObject(kv.getValue().getClass(), kv.getValue(), inner_obj);

                        json_obj.put("value", inner_obj);
                    }

                    array.put(json_obj);
                } else {
                    // Intentar guardar-lo directament primer
                    boolean saved = saveBuiltinIntoArr(v, kv.getValue(), array);

                    if (!saved) {
                        saveObject(kv.getValue().getClass(), kv.getValue(), json_obj);
                        array.put(json_obj);
                    }
                }
            }
        }
    }

    /**
     * Guardar una list en un array json
     *
     * @param field camp del tipus list
     * @param prop l'objecte que conte la list
     * @param array l'array del json on s'han d'afegir els objectes
     * @throws Exception
     */
    private void saveList(Field field, Object prop, JSONArray array) throws Exception {
        ParameterizedType type = (ParameterizedType) field.getGenericType();
        saveListObject(type, prop, array);
    }

    private void saveListObject(ParameterizedType type, Object object, JSONArray array) throws Exception {
        Iterable<Object> l = (Iterable<Object>) object;
        for (Object o : l) {
            if (Map.class.isAssignableFrom(o.getClass())) {
                JSONArray inArray = new JSONArray();

                saveMapObject(
                        (ParameterizedType) type.getActualTypeArguments()[0],
                        o,
                        inArray,
                        true
                );

                array.put(inArray);
            } else if (Set.class.isAssignableFrom(o.getClass()) || List.class.isAssignableFrom(o.getClass())) {
                JSONArray inArray = new JSONArray();

                saveListObject(
                        (ParameterizedType) type.getActualTypeArguments()[0],
                        o,
                        inArray
                );

                array.put(inArray);
            } else {
                // Intentar guardar-lo directament primer
                Class<?> v = (Class<?>) type.getActualTypeArguments()[0];
                boolean saved = saveBuiltinIntoArr(v, o, array);

                if (!saved) {
                    JSONObject json_obj = new JSONObject();
                    saveObject(o.getClass(), o, json_obj);

                    array.put(json_obj);
                }
            }
        }
    }

    /**
     * Retorna els camps declarats inclosos el de la superclasse
     *
     * @param fields els camps de la crida anteriod
     * @param type tipus
     * @return l'agregacio dels camps
     */
    private List<Field> getAllFields(List<Field> fields, Class<?> type, boolean ignorePowerType) {
        Field[] _fields = type.getDeclaredFields();
        for (int i = 0; i < _fields.length; i++) {
            if (_fields[i].getAnnotation(JsonBaseDeduce.class) != null) {
                if (!ignorePowerType) {
                    fields.add(_fields[i]);
                }
            } else {
                if (_fields[i].getAnnotation(JsonIgnore.class) == null) {
                    fields.add(_fields[i]);
                }
            }
        }

        if (type.getSuperclass() != null) {
            getAllFields(fields, type.getSuperclass(), ignorePowerType);
        }

        return fields;
    }

    private boolean existsMethod(Class<?> clazz, String name) {
        Method[] methods = clazz.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().compareTo(name) == 0) {
                return true;
            }
        }

        return false;
    }
}
