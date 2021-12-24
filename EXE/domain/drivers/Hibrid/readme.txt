- OBJECTE DE LA PROVA

Comprovar el funcionament de l'algorisme Hibrid amb un dataset real

- FITXERS DE DADES NECESSARIS

DATA/driver-recomanacio/items.csv
DATA/driver-recomanacio/ratings.test.known.csv
DATA/driver-recomanacio/ratings.test.unknown.csv

- EFECTES ESTUDIATS

Es fa la càrrega del dataset i la classificacio.
Posteriorment, es deixa triar sobre quin usuari es volen fer les
recomanacions i posterior avaluacio de la qualitat de les mateixes.

Es generen 2 fitxers:
* DATA/avaluacio.csv -> indica la qualitat de les recomanacions.
* DATA/prediccions.csv -> indica els scores assignats per a cada item.