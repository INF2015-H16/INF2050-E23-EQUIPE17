package Source;

import java.util.Arrays;

public class CalculEmploye {
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
