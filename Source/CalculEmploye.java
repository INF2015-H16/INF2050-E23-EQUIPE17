package Source;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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
        return etatCompteTotal * 1.2/100;
    }

    /**
     * Cette méthode calculera l'état par client en utilisant les méthodes de calcul ci-dessous.
     * @param typeEmploye
     * @param nombreHeures
     * @param tauxHoraireMin
     * @param tauxHoraireMax
     * @param distanceDeplacement
     * @param overtime
     * @return
     * @throws JsonException
     */
    public static double calculerEtatParClient(int typeEmploye, double nombreHeures, double tauxHoraireMin, double tauxHoraireMax, double distanceDeplacement, double overtime) throws JsonException {
        // Calcul des montants réguliers, de déplacement et des heures supplémentaires
        double montantRegulier = nombreHeures * ((tauxHoraireMin + tauxHoraireMax) / 2);
        double montantDeplacement = (200 - (distanceDeplacement * (0.10 * montantRegulier)));
        double montantHeuresSupp = (nombreHeures > 40) ? (nombreHeures - 40) * 100.0 * overtime : 0;

        // Obtention du montant total en appelant la sous-méthode
        double montantTotal = calculMontantTotal(montantRegulier, montantDeplacement, montantHeuresSupp);

        if (montantTotal < 0) {
            throw new JsonException("Le montant total ne peut pas être négatif");
        }

        return montantTotal;
    }

    private static double calculMontantTotal(double montantRegulier, double montantDeplacement, double montantHeuresSupp) {
        // Calcul du montant total
        double montantTotal = montantRegulier + montantDeplacement + montantHeuresSupp;

        // Arrondi du montant total avec la méthode arrondirMontant
        double montantTotalArrondi = arrondirMontant(montantTotal);

        return montantTotalArrondi;
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
    public static double calculerMontantRegulier(int typeEmploye, double nombreHeures, double tauxHoraireMin,
                                                 double tauxHoraireMax) {

        double tauxHoraire = 0;

        if (typeEmploye == 0){
            tauxHoraire = tauxHoraireMin;
        } else if (typeEmploye == 1) {
            tauxHoraire = nombreHeures * (tauxHoraireMin + tauxHoraireMax)/2;
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
    public static double calculerMontantDeplacement(int typeEmploye, double distanceDeplacement,
                                                    double montantRegulier) throws JsonException {

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

    public static double calculerMontantHeuresSupplementairesType1(double overtime) {

        double montantHeuresSupplementaires = 0.0;

        if (overtime > 4 && overtime <= 8) {
            montantHeuresSupplementaires = 50.0 * overtime;
        } else if (overtime > 8) {
            montantHeuresSupplementaires = 100.0 * overtime;
        }

        return montantHeuresSupplementaires;
    }


    public static double calculerMontantHeuresSupplementairesType2(double overtime) {

        double montantHeuresSupplementaires = 0.0;

        if (overtime <= 4) {
            montantHeuresSupplementaires = 75.0 * overtime;
        } else if (overtime > 4) {
            montantHeuresSupplementaires = 150.0 * overtime;
        }

        return montantHeuresSupplementaires;
    }

    public static double arrondirMontant(double montant) {
        double arrondi = Math.round(montant * 20) / 20.0; // Arrondi à 2 décimales
        double difference = arrondi - Math.floor(arrondi); // Partie décimale

        if (difference < 0.025 || difference > 0.975) {
            // Arrondi au multiple de 0.05 le plus proche
            int multiplicateur = (int) Math.round(arrondi * 20) % 5;
            if (multiplicateur == 0 || multiplicateur == 1) {
                return Math.floor(arrondi * 20) / 20.0;
            } else if (multiplicateur == 4) {
                return Math.ceil(arrondi * 20) / 20.0;
            }
        }

        return arrondi;
    }

    public static void calculEtatClient(String argument2, int itterations, int[] nbrs, int[] distance_deplacement,
                                        int[] overtime, int[] nombre_heures, String[] code, double taux_horaire_min,
                                        double taux_horaire_max, double[] etatParClient, int type_employe,
                                        int matricule_employe, JSONArray observations, String option,
                                        JSONArray interventions, String json, String fichierEntree) throws JsonException {

        for (int i = 0; i < itterations; i++) {

            if(calculerEtatParClient(type_employe, nombre_heures[i], taux_horaire_min,
                    taux_horaire_max, distance_deplacement[i], overtime[i]) > 200){

                etatParClient[i] = calculerEtatParClient(type_employe, nombre_heures[i],
                        taux_horaire_min, taux_horaire_max, distance_deplacement[i], overtime[i]);
            }
        }
        calculCouts(argument2, itterations, nbrs, code, etatParClient, matricule_employe,observations,option,
                interventions,json, fichierEntree);
    }

    public static void calculCouts(String argument2, int itterations, int[] nbrs, String[] code,
                                   double[] etatParClient, int matricule_employe, JSONArray observations,
                                   String option, JSONArray interventions, String json, String fichierEntree) {

        JSONObject statistiques = new JSONObject();
        double etatCompteTotal = calculerEtatCompteTotal(etatParClient);
        double coutVariable = calculerCoutVariable(etatCompteTotal);
        double coutFixe = calculerCoutFixe(etatCompteTotal);
        JsonException.validerFichierSortieDispo(argument2);
        Statistiques.gestionStatistiques(option, interventions,json,statistiques);

        GestionJson.formattageFichierSortieJson(matricule_employe, arrondirMontant(etatCompteTotal),
                arrondirMontant(coutFixe),
                arrondirMontant(coutVariable), code, etatParClient, itterations, argument2,
                nbrs,observations, statistiques, option, fichierEntree);
    }
}
