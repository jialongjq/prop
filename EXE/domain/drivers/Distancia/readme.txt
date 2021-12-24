- OBJECTE DE LA PROVA

Comprovar el funcionament de la classe distancia.

NOTA: La distancia entre items sempre es calcula respecte un conjunt total d'items.
Per calcular la distancia entre dos items, tenim en compte una distancia maxima. Aquesta distancia maxima es calcula com el sumatori de:
- Numero d'atributs descriptius
- Numero d'atributs numerics
- Numero d'atributs booleans
- En el cas dels atributs categorics, per cada atribut categoric es considera la quantitat mes gran de categories que hi ha dins un conjunt d'items
Si per exemple tenim un conjunt de dos items (han de ser del mateix tipus), amb un atribut de cada tipus (1 descriptiu, 1 numeric, 1 boolea, 1 categoric), i l'item 1 te 5 categories al conjunt de categories i l'item 2 te 3, la distancia maxima sera 1+1+1+max(5,3)=8.
La distancia sera, entre cada item del conjunt:
[distancia(item1, item2)] = [distancia maxima(conjunt)] - [grau de coincidencia total(item1, item2)]
Aquest grau de coincidencies es calcula comparant un per un els atributs del mateix tipus (es a dir, amb el mateix nom d'atribut), per aixo es important que siguin del mateix tipus d'item:
- Si un dels dos items no tenen un atribut determinat (null), no incrementa el grau de coincidencia
- Atributs descriptius: si les dues descripcions son iguals, el grau de coincidencia incrementa 1
- Atributs numerics: el grau de coincidencia s'incrementa 1-diferencia de valors normalitzat respecte el valor maxim que hi ha d'aquest atribut dins el conjunt d'items
- Atributs booleans: si els dos booleans tenen el mateix valor, el grau de coincidencia incrementa 1
- Atributs categorics: incrementa 1 per cada coincidencia en categories

- ALTRES ELEMENTS INTEGRATS A LA PROVA

S'utilitzen les classes TipusItem (per a la creacio d'items) i Item (per crear els items i calcular les distancies).

- VALORS ESTUDIATS

Es planteja la distancia entre dos items definits de la seguent manera:

Item [id=0, tipus=movies]
nomAtribut=genres, valor=adventure;comedy;action
nomAtribut=title, valor=Hora de Aventuras
nomAtribut=adult, valor=false
nomAtribut=budget, valor=1000.0

Item [id=1, tipus=movies]
nomAtribut=genres, valor=adventure;action
nomAtribut=title, valor=Indiana Jones
nomAtribut=adult, valor=false
nomAtribut=budget, valor=500.0

Per tal de calcular la distancia entre aquests dos items (exclusivament), es crea un conjunt d'items format unicament per aquests dos.
Abans de procedir, es necessari fer un calcul de la distancia maxima i determinar els valors maxims numerics i categorics:
- L'atribut numeric "budget" te un maxim de 1000.0, ja que entre els atributs "budget" dels dos items, max(1000.0, 500.0)=1000.0
- L'atribut categoric "genres" te un maxim de 3 categories, ja que entre els atributs "genres", max(3 categories, 2 categories)=3 categories
- Per calcular la distancia maxima tenim: 3+1+1+1=6 -> El 3 representa el maxim de categories de l'atribut categoric "genres"
						      -> Els 1 representen els valors descriptiu, boolea i numeric
						      -> Per tant, la suma total es 6.
Amb la distancia maxima calculada i el valor maxim per cada atribut numeric calculat, ja es pot aplicar el calcul de distancia entre els dos items:
- Per l'atribut "genres", hi ha un grau de coincidencia de 2 (coincideixen les categories "adventure" i "action")
- Per l'atribut "title", hi ha un grau de coincidencia de 0 (son diferents, "Hora de Aventuras"!="Indiana Jones")
- Per l'atribut "adult", hi ha un grau de coincidencia de 1 (son iguals, false==false)
- Per l'atribut "budget", hi ha un grau de coincidencia de (1 - |a1/max - a2/max|), on el max es el maxim numeric de l'atribut "budget" i a1 i a2 son els valors de l'item 1 i 2, respectivament.
  D'aquesta manera, tenim que a1/max i a2/max es normalitzen en un interval de [0..1] respecte el maxim. El calcul sera doncs: (1 - |0.5 - 1|) = 0.5
- Finalment, distancia(item1,item2) = distanciaMaxima - grauCoincidencia = 6.0 - (2 + 0 + 1 + 0.5) = 2.5

Es pot observar facilment que si el conjunt estigues format unicament per l'item amb id=0, i es compara la distancia entre aquest mateix item, el resultat sera 0 perque el grau de coincidencia anula totalment la distancia maxima.

- EFECTES ESTUDIATS

Es calcula correctament el valor maxim dels atributs numerics i categorics, la distancia maxima i la distancia entre dos items, tot respecte a un conjunt d'items determinat.

