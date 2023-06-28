package Source;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class JsonException extends Exception{

    public JsonException(String message)
    {
        super(message);
    }

    public static int checkDistance(int nbr) throws JsonException
    {
        if (nbr < 0.0 || nbr > 100.0) {
            throw new JsonException("Distance deplacement invalide");
        }
        return nbr;
    }

    public static int checkOvertime(int nbr) throws JsonException
    {
        if (nbr < 0.0 || nbr > 4.0) {
            throw new JsonException("Overtime invalide");
        }

        return nbr;
    }

    public static int checkNombreHeures(int nbr) throws JsonException
    {
        if (nbr < 0.0  || nbr > 8.0) {
            throw new JsonException("Nombre d'heures invalide");
        }

        return nbr;
    }

    public static double checkerTaux(String[][] data, int i) throws JsonException
    {
        double taux_horaire;
        try {
            taux_horaire = Double.parseDouble(data[i][0].substring(0, 5));
        }
        catch (Exception e)
        {
            taux_horaire = Double.parseDouble(data[i][0].replace(",", ".").replace("$", ""));// Sa c'est pour gerer le virgule
        }

        if(taux_horaire < 0)
            throw new JsonException("Taux horaire invalide");
        return taux_horaire;
    }

    public static int checkerTypeEmploye(String[][] data) throws JsonException {
        int type = Integer.parseInt(data[1][0]);
        if (type < 0 || type > 2)
            throw new JsonException("Type d'employé invalide");

        else
            return type;
    }

    public static void validerNombreInterventions(int nombre) throws JsonException {
        if (nombre > 10) {
            throw new JsonException("Le nombre d’interventions est supérieur à 10.");
        }
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

        //TODO: enlever le return -1 et la remplacer par un try catch statement
    }

    public static boolean validerFormatDate(String dateStr) {
        try {
            LocalDate.parse(dateStr);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
