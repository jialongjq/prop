- OBJECTE DE LA PROVA

Comprovar el funcionament del control·lador de recomanacions així com
el funcionament dels algorismes amb datasets més grans.

- FITXERS DE DADES NECESSARIS

DATA/driver-recomanacio/items.csv
DATA/driver-recomanacio/ratings.test.known.csv
DATA/driver-recomanacio/ratings.test.unknown.csv

- EFECTES ESTUDIATS

Es fa la càrrega del dataset i la classificacio.
Posteriorment es dona l'opcio de fer una de les seguents operacions:
1- Recomanar: fer un conjunt de k recomanacions sobre l'usuari escollit.
2- Avaluar les recomanacions: s'avaluan totes les recomanacions per a tots els usuaris i el
resultat es guarda al fitxer DATA/avaluacio.csv
3- Guardar en disc: es crearan 2 fitxers, DATA/classificacio.json que conte tot el necessari
per a poder recuperar una classificacio i DATA/recomanacions.json que conte tot el necessari
per a recuperar les recomanacions fetes fins el moment.