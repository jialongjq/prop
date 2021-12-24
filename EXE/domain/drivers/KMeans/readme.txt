- OBJECTE DE LA PROVA

Provar la implementacio de l'algorisme KMeans.

- FITXERS ADDICIONALS

* DATA/driver-kmeans/data.csv: conte uns punts en un espai de dues dimensions.
* DATA/driver-kmeans/centroides.csv: conte uns baricentres assignats per a la prova amb assignacio fixada.


- VALORS ESTUDIATS

Es passa una matriu de 6x2 a l'algorisme, és a dir, es tenen 6 punts en un espai de dues dimensions:

	1	2
1	5	4	
2	4	5
3	3	3
4	3	2.5
5	1	0
6	1	3.5

A simple vista es pot observar que els punts tenen una afinitat 2 a 2 amb la distància euclidiana.

(1-2) : 1.41
(3-4) : 0.5
(5-6) : 3.5

És fan dues proves, una amb els centroides triats de manera aleatòria i una altra fixant-los.
Es farà amb una k = 3.

S'espera que la classificació sigui semblant a la següent:
- Cluster 1: 1,2
- Cluster 2: 3,4
- Cluster 3: 5,6

Amb l'assignació aleatòria s'obté a següent classificació:
- Cluster 1: 1,3,4 // D(1,3): 2.23 / D(1,4): 2.5 / D(3,4): 0.5
- Cluster 2: 5 
- Cluster 3: 2,6 // D(2,6): 3.35

La classificació amb un conjunt reduït de dades queda clarament condicionada pels centres escollits de
manera aleatòria.

Triant com a centroides inicials els punts (4.5, 4.5), (3, 2.75), (1, 2.5) si que surt la
classificació esperada.
