- OBJECTE DE LA PROVA

Provar la funcionalitat de l'avaluacio de les recomanacions


- FITXERS ADDICIONALS

Els fitxers per la prova de l'avaluacio correcta son:
* DATA/driver-avaluacio/avaluacio.correcte.true.csv
* DATA/driver-avaluacio/avaluacio.correcte.true.csv

Els fitxers per la prova de l'avaluacio incorrecta son:
* DATA/driver-avaluacio/avaluacio.incorrecte.true.csv
* DATA/driver-avaluacio/avaluacio.incorrecte.true.csv


- VALORS ESTUDIATS

Es volen provar dues avalucions, una on les recomanacions segueixen un ordre proper a l'esperat i una
altra on els ordres són prou diferents.

Per ambdues proves les valoracions esperades són les següents (format: item, valoracio)
1 5
2 3
3 2
4 4
5 1

Lo que donaria el següent ordre: 1, 4, 2, 3, 5

En la primera prova es tenen les següents prediccions
1 5
2 3
3 4
4 4

Lo que donaria el següent ordre: 1, [3, 4], 2

Com és molt proper a l'ordre òptim s'obté una valoració propera a 1.

En la segona prova es tenen les següents prediccions
1 1
2 5
3 3
4 2

Lo que donaria el següent ordre: 2, 3, 4, 1

Com és força llunyà a l'òptim s'obté una valoració poc propera a 1.

