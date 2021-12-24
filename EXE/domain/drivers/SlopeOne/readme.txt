- OBJECTE DE LA PROVA

Provar la implementacio de l'algorisme Slope One.


- FITXERS ADDICIONALS

* DATA/driver-slopeone/clusters.csv : classificacions imposades.
* DATA/driver-slopeone/valoracions.csv : valoracions dels usuaris als diferents items.


- ALTRES ELEMENTS

Es farà servir la classe Classificacio.


- STUBS

S'imposa una classificació inicial per a no haver de fer la classificacio.


- VALORS ESTUDIATS

Es fan servir 4 usuaris i 4 items. Les seves valoracions son les següents (les files són els usuaris i les columnes els items)

	1 2 3 4
1 | 4 3 1 1
2 | 5 3 - 2
3 | 2 2 4 5
4 | 1 2 5 -

S'imposa una classificació en 2 grups: un amb l'usuari 1 i 2 i l'altre amb el 3 i el 4.

Per l'usuari 2 s'espera una valoracio de l'ítem 3 entre 1 i 2 d'acord amb les valoracions d'ambdós usuaris.
Per l'usuari 4 s'espera una valoracio de l'ítem 4 propera al 5 d'acord amb les valoracions d'ambdós usuaris.

Pel primer cas s'obté un 1.5 i pel segon un 5.