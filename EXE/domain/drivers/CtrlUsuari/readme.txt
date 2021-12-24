- OBJECTE DE LA PROVA

Comprovar el funcionament del controlador d'usuaris

- FITXER DE PROVES OPCIONAL

DATA/ctrlusuari.txt

- ALTRES ELEMENTS INTEGRATS A LA PROVA

Classes Usuari i CtrlDomini

- FITXERS DE DADES NECESSARIS

DATA/usuaris.json
DATA/usuaris-driver.json

- EFECTES ESTUDIATS

Neteja del fitxer DATA/usuaris-driver.json
Carrega d'usuaris del fitxer DATA/usuaris.json
Registrar un usuari nou
Registrar un usuari pero amb un nom que no esta disponible
Registrar un usuari pero amb contrasenyes que no coincideixen
Registrar un usuari amb un id especific
Registrar un usuari amb un id especific pero amb un nom que no esta disponible
Registrar un usuari amb un id especific pero les contrasenyes no coincideixen
Registrar un usuari amb un id especific pero l'id no esta disponible
Registrar un usuari sense especificar id, despres d'haver registrat un amb un id que no segueix la sequencia continua
Login de l'usuari
Logout de l'usuari actiu
Login de l'usuari, pero la contrasenya es incorrecta
Login d'un usuari que no esta registrat
Logout de l'usuari actiu pero no hi ha cap usuari que hagi iniciat sessio
Canvi de contrasenya pero no hi ha cap usuari que hagi iniciat sessio
Canvi de nom pero no hi ha cap usuari que hagi iniciat sessio
Canvi de contrasenya (amb login previ), pero les contrasenyes no coincideixen
Canvi de contrasenya, pero es la que ja tenia
Canvi de contrasenya de manera correcte
Es vol canviar el nom, pero el nom indicat es el que ja te l'usuari actiu
Es vol canviar el nom, pero el nom indicat no esta disponible
S'aconsegueix efectuar un canvi de nom
Comprovacions d'existencia d'usuaris
Guarda la llista d'usuaris carregats al controlador, amb els canvis efectuats, al fitxer DATA/usuaris-driver.json
