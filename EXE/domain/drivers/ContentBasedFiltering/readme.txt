- OBJECTE DE LA PROVA

Comprovar el funcionament de l'algorisme de Content Based Filtering

- ALTRES ELEMENTS INTEGRATS A LA PROVA

Classes Pair, Item i TipusItem

- FITXERS DE DADES NECESSARIS

DATA/items-driver.csv
DATA/ratings.db-driver.csv

- EFECTES ESTUDIATS

Es fa la càrrega del dataset d'items i de valoracions correctament
S'aplica l'algorisme a un usuari en concret:
- A partir de les seves valoracions (indicant una puntuacio minima, en aquest cas 3.5), s'agafa el set d'items valorats
- El set d'items del conjunt seran tots els items carregats (250 items)
- S'indica el valor de k
- Per cada item de valoracions, es calcula la distancia entre aquest i tots els items del conjunt (exclou els items que estiguin dins dels valorats), guarda els k items mes propers a una llista de prioritat
- Finalment, d'aquesta llista s'obtenen els k primers i es guarden en una llista ordenada (importa l'ordre de rellevancia).

