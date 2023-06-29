package Source;

import java.util.Arrays;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class CalculEmploye {

//TODO verifier que les methodes suivent les conventions et normes

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
        double tauxHoraire = 0;

        if (typeEmploye == 0){
            tauxHoraire = tauxHoraireMin;
        } else if (typeEmploye == 1) {
            tauxHoraire = (tauxHoraireMin + tauxHoraireMax)/2;
        } else if (typeEmploye == 2) {
            tauxHoraire = tauxHoraireMax;
        }
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
                                                    double montantRegulier) throws JsonException {
        double CalculemontantDeplacement = 0;

        double montantDeplacement = 0.0;

        if (typeEmploye == 0) {
            CalculemontantDeplacement = 200 - (distanceDeplacement * (0.05 * montantRegulier));
        } else if (typeEmploye == 1) {
            CalculemontantDeplacement = 200 - (distanceDeplacement * (0.10 * montantRegulier));
        } else if (typeEmploye == 2) {
            CalculemontantDeplacement = 200 - (distanceDeplacement * (0.15 * montantRegulier));
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
    public static double calculerMontantHeuresSupplementaires(int typeEmploye, double overtime, double nombre_heures) {
        double montantHeuresSupplementaires = 0.0;

        if (typeEmploye == 0) {
            montantHeuresSupplementaires = 0.0;
        } else if (typeEmploye == 1) {
            montantHeuresSupplementaires = calculerMontantHeuresSupplementairesType1(overtime);
        } else if (typeEmploye == 2) {
            montantHeuresSupplementaires = calculerMontantHeuresSupplementairesType2(overtime);
        }

        montantHeuresSupplementaires = Math.min(montantHeuresSupplementaires, 1500.0);

        return montantHeuresSupplementaires;
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
        double arrondi = Math.ceil(montant * 20) / 20; // Arrondi à 2 décimales
        double difference = arrondi - Math.floor(arrondi); // Partie décimale
        if (difference < 0.025) {
            return Math.floor(arrondi * 20) / 20; // Arrondi au multiple inférieur de 0.05
        } else {
            return Math.ceil(arrondi * 20) / 20; // Arrondi au multiple supérieur de 0.05
        }
    }

    public static double calculerEtatParClient(int typeEmploye, double nombreHeures,
                                               double tauxHoraireMin, double tauxHoraireMax,
                                               double distanceDeplacement, double overtime, double montantregulier) throws JsonException {
        double montantTotal = 0.0;

        double montantHeuresTravaillees = calculerMontantRegulier(typeEmploye, nombreHeures,
                tauxHoraireMin, tauxHoraireMax);

        montantTotal += montantHeuresTravaillees;

        double montantHeuresSupplementaires = calculerMontantHeuresSupplementaires(typeEmploye, overtime, nombreHeures);
        montantTotal += montantHeuresSupplementaires;

        double montantDeplacement = calculerMontantDeplacement(typeEmploye, distanceDeplacement, montantregulier);
        montantTotal += montantDeplacement;

        montantTotal = arrondirMontant(montantTotal);

        if (montantTotal < 0) {
            throw new JsonException ("Le montant total ne peut pas être négatif");
        }

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
        double coutFixe = 0.0;

        if (etatCompteTotal >= 1000.0)
            coutFixe = etatCompteTotal * 0.012;
        else if (etatCompteTotal >= 500.0)
            coutFixe = etatCompteTotal * 0.008;
        else
            coutFixe = etatCompteTotal * 0.004;

        return arrondirMontant(coutFixe);
    }
}
