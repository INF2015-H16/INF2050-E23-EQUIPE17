package Source;

import java.util.Arrays;

public class CalculEmploye {
    /**
     * Vérifie si un entier `j` est présent dans le tableau `nbrs`.
     *
     * @param nbrs Le tableau d'entiers à vérifier.
     * @param j    L'entier à rechercher.
     * @return `true` si l'entier `j` n'est pas présent dans le tableau `nbrs`, `false` sinon.
     */
    public static boolean verification(int[] nbrs, int j) {
        for (int i = 0; i < nbrs.length; i++) {
            if (nbrs[i] == j)
                return false;
        }
        return true;
    }



    /**
     * Effectue le traitement des données et génère les résultats correspondants.
     *
     * @param data       Les données d'entrée sous forme de tableau à deux dimensions.
     * @param argument2  Le fichier sortie.json a remplir.
     */
    public static void executer(String[][] data, String argument2) {
        int[] nbrs = new int[30];
        Arrays.fill(nbrs, -1);
        int status, matricule_employe, type_employe, itterations = Integer.parseInt(data[data.length - 1][0]);
        int[] distance_deplacement = new int[Integer.parseInt(data[data.length - 1][0])];
        int[] overtime = new int[Integer.parseInt(data[data.length - 1][0])];
        int[] nombre_heures = new int[Integer.parseInt(data[data.length - 1][0])];
        double taux_horaire_min, taux_horaire_max, montantDeplacement = 0, coutVariable, montantRegulier = 0,
                montantHeureSupplementaire = 0;
        String[] code = new String[Integer.parseInt(data[data.length - 1][0])];
        double[] EtatParClient = new double[code.length];

        // Récupération des données d'entrée
        matricule_employe = Integer.parseInt(data[0][0]);
        type_employe = Integer.parseInt(data[1][0]);
        taux_horaire_min = Double.parseDouble(data[2][0].substring(0, 5));
        taux_horaire_max = Double.parseDouble(data[3][0].substring(0, 5));

        // Traitement des données
        for(int i=0,j=4; j< data.length-1;j++, i++)
        {
            status = checker(data,j,nbrs);
            if(status != -1 && status != -2)
            {
                code[i] = data[j][0];
                distance_deplacement[i] = Integer.parseInt(data[j][1]) +Integer.parseInt( data[status][1]);
                overtime[i] = Integer.parseInt(data[j][2])  + Integer.parseInt(data[status][2]);
                nombre_heures[i] = Integer.parseInt(data[j][3]) + Integer.parseInt(data[status][3]);// i am not sure of this
                //itterations--;
            }
            else if(status == -1)
            {
                code[i] = data[j][0];
                distance_deplacement[i] = Integer.parseInt(data[j][1]);
                overtime[i] = Integer.parseInt(data[j][2]);
                nombre_heures[i] = Integer.parseInt(data[j][3]);
            }
        }


        for(int i=0 ; i<itterations; i++)
        {
            if(verification(nbrs,i))
                EtatParClient[i] = calculerEtatParClient(type_employe,nombre_heures[i],taux_horaire_min,taux_horaire_max,distance_deplacement[i],overtime[i],montantRegulier);
        }

        // Calcul de l'état par client pour chaque itération
        for (int i = 0; i < itterations; i++) {
            EtatParClient[i] = calculerEtatParClient(type_employe, nombre_heures[i],
                    taux_horaire_min, taux_horaire_max, distance_deplacement[i], overtime[i], montantRegulier);
        }

        // Calcul de l'état de compte total et des coûts
        double etatCompteTotal = calculerEtatCompteTotal(EtatParClient);
        coutVariable = calculerCoutVariable(etatCompteTotal);
        double coutFixe = calculerCoutFixe(etatCompteTotal);

        // Écriture des résultats dans un fichier JSON
        GestionJson.ecrireFichierSortieJson(matricule_employe, arrondirMontant(etatCompteTotal), arrondirMontant(coutFixe),
                arrondirMontant(coutVariable),code,EtatParClient,itterations, argument2, nbrs);
    }


    /**
     * Vérifie s'il existe une occurrence précédente du code client dans le tableau à deux dimensions.
     * Si une occurrence est trouvée, elle met à jour le tableau `nbr` avec l'index de l'occurrence précédente.
     *
     * @param array Le tableau à deux dimensions contenant les données.
     * @param z     L'index actuel dans le tableau.
     * @param nbr   Le tableau de numéros de référence pour les occurrences précédentes.
     * @return L'index de l'occurrence précédente si elle existe, -1 si aucune occurrence précédente n'est trouvée,
     * -2 si une occurrence est trouvée mais son index est inférieur à l'index actuel.
     */
    public static int checker(String[][] array, int z, int[] nbr) {
        for (int i = 0; i < array.length - 1; i++) {
            if (i == z)
                i++;

            // Vérifie si le code client à l'index z est égal au code client à l'index i
            if (array[z][0].equals(array[i][0])) {
                if (i < z)
                    return -2; // Une occurrence précédente a été trouvée, mais son index est inférieur à l'index actuel
                else {
                    nbr[z] = i - 4; // Met à jour le tableau de numéros de référence avec l'index de l'occurrence précédente
                    return i; // Retourne l'index de l'occurrence précédente
                }
            }
        }
        return -1; // Aucune occurrence précédente n'a été trouvée
    }



    /**
     * Cette méthode calculera le montant régulier en fonction du type d'employé,
     * du nombre d'heures travaillées et des taux horaires min et max.
     *
     * @params typeEmploye,
     * @params nombreHeures
     * @params tauxHoraireMin
     * @params tauxHoraireMax
     */
    public static double calculerMontantRegulier(int typeEmploye, double nombreHeures,
                                                 double tauxHoraireMin, double tauxHoraireMax) {
        if (tauxHoraireMin < 0 || tauxHoraireMax < 0 || nombreHeures < 0){
            throw new IllegalArgumentException("Le taux horaire ou le nombre d'heure n'est pas valide.");
        }

        double tauxHoraire = 0;


        //Selectionner type d'employe
        if (typeEmploye == 0){
            tauxHoraire = tauxHoraireMin;
        } else if (typeEmploye == 1) {
            tauxHoraire = (tauxHoraireMin + tauxHoraireMax)/2;
        } else if (typeEmploye == 2) {
            tauxHoraire = tauxHoraireMax;
        } else {
            throw new IllegalArgumentException("La valeur n'est pas valide. La valeur ne peut pas etre plus petite que 0 et plus grande que 2.");
        }

        // Calculer le montant régulier en multipliant le taux horaire par le nombre d'heures
        double montantRegulier = tauxHoraire * nombreHeures;

        return montantRegulier;
    }

    /**
     * Cette méthode calculera le montant pour les heures supplémentaires en fonction du
     * type d'employé et du nombre d'heures supplémentaires.
     * @param typeEmploye
     * @param distanceDeplacement
     * @param montantRegulier
     * @return
     */
    public static double calculerMontantDeplacement(int typeEmploye, double distanceDeplacement,
                                                    double montantRegulier){
        if (distanceDeplacement < 0 || montantRegulier < 0){
            throw new IllegalArgumentException("La valeur n'est pas valide.");
        }

        double montantDeplacement = 0;

        // Calculer le montant de déplacement en fonction du type d'employé
        if (typeEmploye == 0){
            montantDeplacement = 200 - (distanceDeplacement * (0.05 * montantRegulier)) ;
        } else if (typeEmploye == 1) {
            montantDeplacement = 200 - (distanceDeplacement * (0.10 * montantRegulier));
        } else if (typeEmploye == 2) {
            montantDeplacement = 200 - (distanceDeplacement * (0.15 * montantRegulier));
        } else {
            throw new IllegalArgumentException("La valeur n'est pas valide.");
        }

        return montantDeplacement;
    }

    /**
     * Cette méthode calculera le montant pour les heures supplémentaires en fonction
     * du type d'employé et du nombre d'heures supplémentaires.
     * @param typeEmploye
     * @param overtime
     * @return
     */
    public static double calculerMontantHeuresSupplementaires(int typeEmploye, double overtime,double nombre_heures) {
        double montantHeuresSupplementaires = 0.0;

        if (overtime < 0 || nombre_heures < 0){
            throw new IllegalArgumentException("Overtime ou nombre d'heure n'est pas valide.");
        }

        if (typeEmploye == 0) {
            // Superviseur : Pas de montant pour les heures supplémentaires
            montantHeuresSupplementaires = 0.0;
        } else if (typeEmploye == 1) {
            // Permanent
            if (nombre_heures > 4 && nombre_heures <= 8) {
                montantHeuresSupplementaires = 50.0 * overtime;
            } else if (nombre_heures > 8) {
                montantHeuresSupplementaires = 100.0 * overtime;
            }
        } else if (typeEmploye == 2) {
            // Contractuel
            if (overtime <= 4)
            {
                montantHeuresSupplementaires = 75.0 * overtime;
            }

            else if (overtime > 4)
            {
                montantHeuresSupplementaires = 150.0 * overtime;
            }
        } else {
            throw new IllegalArgumentException("Type d'employer n'est pas valide.");
        }

        // Limiter le montant des heures supplémentaires à 1500.00
        montantHeuresSupplementaires = Math.min(montantHeuresSupplementaires, 1500.0);

        return montantHeuresSupplementaires;
    }

    /**
     * Calcule le coût variable en fonction de l'état total du compte.
     *
     * @param etatCompteTotal   L'état total du compte pour lequel le coût variable doit être calculé.
     * @return                  Le coût variable calculé.
     */
    public static double calculerCoutVariable(double etatCompteTotal) {
        double coutVar = (2.5/100 * etatCompteTotal);
        coutVar = arrondirMontant(coutVar);
        return coutVar;
    }

    public static double arrondirMontant(double montant) {
        // On multiplie par 20 pour déplacer la décimale au deuxième chiffre après la virgule
        return Math.ceil(montant * 20) / 20;
    }

    public static double calculerEtatParClient(int typeEmploye, double nombreHeures,
                                               double tauxHoraireMin, double tauxHoraireMax,
                                               double distanceDeplacement, double overtime, double montantregulier) {
        double montantTotal = 0.0;

        // Calcul du montant des heures travaillées
        double montantHeuresTravaillees = calculerMontantRegulier(typeEmploye, nombreHeures,
                tauxHoraireMin, tauxHoraireMax);

        montantTotal += montantHeuresTravaillees;

        // Calcul du montant des heures supplémentaires
        double montantHeuresSupplementaires = calculerMontantHeuresSupplementaires(typeEmploye, overtime, nombreHeures);
        montantTotal += montantHeuresSupplementaires;

        // Calcul du montant de déplacement
        double montantDeplacement = calculerMontantDeplacement(typeEmploye, distanceDeplacement, montantregulier);
        montantTotal += montantDeplacement;

        montantTotal = arrondirMontant(montantTotal);

        return montantTotal;
    }
    public static double calculerEtatCompteTotal(double[] etatsParClient) {
        double etatCompteTotal = 0.0;

        for (double etatParClient : etatsParClient) {
            etatCompteTotal += etatParClient;
        }

        return etatCompteTotal;
    }

    /**
     * Calcule le coût fixe en fonction de l'état du compte total.
     *
     * @param etatCompteTotal l'état du compte total
     * @return le coût fixe calculé
     */
    public static double calculerCoutFixe(double etatCompteTotal) {
        double coutFixe;

        // Calcul du coût fixe en fonction de l'État de compte total

        // Si l'état de compte total est supérieur ou égal à 1000.0
        if (etatCompteTotal >= 1000.0) {
            coutFixe = etatCompteTotal * 0.012;
        }
        // Si l'état de compte total est supérieur ou égal à 500.0 mais inférieur à 1000.0
        else if (etatCompteTotal >= 500.0) {
            coutFixe = etatCompteTotal * 0.008;
        }
        // Si l'état de compte total est inférieur à 500.0
        else {
            coutFixe = etatCompteTotal * 0.004;
        }

        // Arrondir le coût fixe au 5 sous supérieur
        coutFixe = arrondirMontant(coutFixe);

        return coutFixe;
    }

}
