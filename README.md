# brain
#URl
http://54.81.76.23:8080/api/stats
http://54.81.76.23:8080/api/mutant
#instrucciones:
para consumir el servicio /mutant colocar en el body de la peticion:

{
    "dna":["ATGCGA", "CAGTGC", "TTATTT", "AGACGG", "GCGTCA", "TCACGT"]
}

comrpobando el funcionamiento de la misma:

el servicio esta desplegado en AWS en el servicio ECS con fargate.

#herramientas utilizadas
-Docker
-springboot
-mysql
