# Logiciel de Calcul d'État de Compte d'Employé

## Description

Ce logiciel a été développé pour calculer l'état de compte d'un employé en fonction de ses interventions chez les clients. L'état de compte est utilisé comme base de facturation pour les clients et englobe une partie importante du coût du service offert, ainsi que des coûts fixes et variables de l'entreprise. Ce logiciel est conçu pour être invoqué à partir d'une application web et ne possède pas d'interface utilisateur propre.

## Fonctionnalités

- Prend en charge un fichier d'entrée JSON spécifié en ligne de commande lors de l'exécution du logiciel en ligne de commande.
- Demande également de spécifier le fichier de sortie en ligne de commande (par exemple : java -jar Projet.jar entree.json sortie.json).
- Calcule l'état de compte par client pour toutes les interventions soumises dans le fichier d'entrée.
- Fournit un état de compte global suggéré au gestionnaire pour la facturation de l'entreprise.

## Installation

Pour installer le logiciel, suivez les étapes suivantes :
Clonez le dépôt GIT fourni : https://github.com/INF2015-H16/INF2050-E23-EQUIPE17.git
Assurez-vous d'avoir JDK-8 et les librairies installé sur votre machine.

## Utilisation

Pour utiliser le logiciel, suivez les étapes suivantes :
   1- Placez votre fichier d'entrée JSON contenant les interventions de l'employé dans le répertoire spécifié.
   2- Ouvrez une ligne de commande et naviguez vers le répertoire contenant le logiciel.
   3- Exécutez la commande suivante : commande_d'exécution, en spécifiant le fichier d'entrée et le fichier de sortie souhaités.
   4- Le logiciel traitera les données d'entrée et générera un fichier de sortie contenant les états de compte par client et l'état de compte total suggéré pour la facturation.

## Date limite

La date limite pour livrer les fonctionnalités demandées est le 5 juin 2023 à 23h55.

