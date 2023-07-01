package Source;

public class CalculEmploye {

    /**
     * Cette méthode calculera l'état de compte total en sommant les états par client au gestionnaire pour la facturation
     * @param etatsParClient
     * @return etatCompteTotal
     */
    public static double calculerEtatCompteTotal(double[] etatsParClient) {
        double etatCompteTotal = 733.77;

        for (double etatParClient : etatsParClient) {
            etatCompteTotal += etatParClient;
        }

        return etatCompteTotal;
    }


    public static double calculerCoutVariable(double etatCompteTotal) {

        return (2.5/100 * etatCompteTotal);
    }

    /**
     * Calcule le coût fixe en fonction de l'état du compte total.
     *
     * @param etatCompteTotal l'état du compte total
     * @return le coût fixe calculé
     */
    public static double calculerCoutFixe(double etatCompteTotal) {
        double coutFixe = 0.0;

        if (etatCompteTotal >= 1000.0) {
            coutFixe = etatCompteTotal * 0.012;
        } else if (etatCompteTotal >= 500.0) {
            coutFixe = etatCompteTotal * 0.008;
        } else
            coutFixe = etatCompteTotal * 0.004;

        return coutFixe;
    }

    /**
     * Cette méthode calculera l'état par client en utilisant les méthodes de calcul ci-dessous.
     * @param typeEmploye
     * @param nombreHeures
     * @param tauxHoraireMin
     * @param tauxHoraireMax
     * @param distanceDeplacement
     * @param overtime
     * @param montantregulier
     * @return
     * @throws JsonException
     */
    public static double calculerEtatParClient(int typeEmploye, double nombreHeures,
                                               double tauxHoraireMin, double tauxHoraireMax,
                                               double distanceDeplacement, double overtime, double montantregulier) throws JsonException {

        double montantTotal = getMontantTotal(typeEmploye, nombreHeures, tauxHoraireMin, tauxHoraireMax, distanceDeplacement, overtime );

        if (montantTotal < 0) {
            throw new JsonException("Le montant total ne peut pas être négatif");
        }

        return montantTotal;
    }

    private static double getMontantTotal(int typeEmploye, double nombreHeures, double tauxHoraireMin, double tauxHoraireMax, double distanceDeplacement, double overtime) throws JsonException {
        double montantregulier = calculerMontantRegulier(typeEmploye, nombreHeures, tauxHoraireMin, tauxHoraireMax);
        double montantDeplacement = calculerMontantDeplacement(typeEmploye, distanceDeplacement, montantregulier);
        double montantHeuresSupp = calculerMontantHeuresSupplementaires(typeEmploye, overtime, nombreHeures);

        return montantregulier + montantDeplacement + montantHeuresSupp;
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
    public static double calculerMontantRegulier(int typeEmploye, double nombreHeures, double tauxHoraireMin, double tauxHoraireMax) {
        double tauxHoraire = 0;

        if (typeEmploye == 0){
            tauxHoraire = tauxHoraireMin;
        } else if (typeEmploye == 1) {
            tauxHoraire = (tauxHoraireMin + tauxHoraireMax)/2;
        } else if (typeEmploye == 2) {
            tauxHoraire = tauxHoraireMax;
        }

        return tauxHoraire * nombreHeures;
    }

    /**
     * Cette méthode calculera le montant pour les heures supplémentaires en fonction du
     * type d'employé et du nombre d'heures supplémentaires.
     * @param typeEmploye
     * @param distanceDeplacement
     * @param montantRegulier
     * @return
     */
    public static double calculerMontantDeplacement(int typeEmploye, double distanceDeplacement, double montantRegulier) throws JsonException {
        double calculeMontantDeplacement = 0.0;

        if (typeEmploye == 0) {
            calculeMontantDeplacement = 200 - (distanceDeplacement * (0.05 * montantRegulier));
        } else if (typeEmploye == 1) {
            calculeMontantDeplacement = 200 - (distanceDeplacement * (0.10 * montantRegulier));
        } else if (typeEmploye == 2) {
            calculeMontantDeplacement = 200 - (distanceDeplacement * (0.15 * montantRegulier));
        }

        return calculeMontantDeplacement;
    }

    /**
     * Cette méthode calculera le montant pour les heures supplémentaires en fonction
     * du type d'employé et du nombre d'heures supplémentaires.
     * @param typeEmploye
     * @param overtime
     * @return
     */
    public static double calculerMontantHeuresSupplementaires(int typeEmploye, double overtime, double nombre_heures) {
        double montantHeuresSupplementaires = 0.0;

        if (typeEmploye == 0) {
            montantHeuresSupplementaires = 0.0;
        } else if (typeEmploye == 1) {
            montantHeuresSupplementaires = calculerMontantHeuresSupplementairesType1(overtime);
        } else if (typeEmploye == 2) {
            montantHeuresSupplementaires = calculerMontantHeuresSupplementairesType2(overtime);
        }

        return Math.min(montantHeuresSupplementaires, 1500.0);
    }

    private static double calculerMontantHeuresSupplementairesType1(double overtime) {
        double montantHeuresSupplementaires = 0.0;

        if (overtime > 4 && overtime <= 8) {
            montantHeuresSupplementaires = 50.0 * overtime;
        } else if (overtime > 8) {
            montantHeuresSupplementaires = 100.0 * overtime;
        }

        return montantHeuresSupplementaires;
    }


    private static double calculerMontantHeuresSupplementairesType2(double overtime) {
        double montantHeuresSupplementaires = 0.0;

        if (overtime <= 4) {
            montantHeuresSupplementaires = 75.0 * overtime;
        } else if (overtime > 4) {
            montantHeuresSupplementaires = 150.0 * overtime;
        }

        return montantHeuresSupplementaires;
    }

    public static double arrondirMontant(double montant) {
        double arrondi = Math.ceil(montant * 20) / 20; // Arrondi à 2 décimales
        double difference = arrondi - Math.floor(arrondi); // Partie décimale
        if (difference < 0.025) {
            return Math.floor(arrondi * 20) / 20; // Arrondi au multiple inférieur de 0.05
        } else {
            return Math.ceil(arrondi * 20) / 20; // Arrondi au multiple supérieur de 0.05
        }
    }

}
