package Source;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class JsonException extends Exception{

    public JsonException(String message)
    {
        super(message);
    }

    public static int validerDistance(int nbr) throws JsonException
    {
        if (nbr < 0.0 || nbr > 100.0) {
            throw new JsonException("Distance deplacement invalide");
        }
        return nbr;
    }

    public static int validerOvertime(int nbr) throws JsonException
    {
        if (nbr < 0.0 || nbr > 4.0) {
            throw new JsonException("Overtime invalide");
        }

        return nbr;
    }

    public static int validerNombreHeures(int nbr) throws JsonException
    {
        if (nbr < 0.0  || nbr > 8.0) {
            throw new JsonException("Nombre d'heures invalide");
        }

        return nbr;
    }

    public static double validerTaux(String[][] data, int i) throws JsonException
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

    public static int validerTypeEmploye(String[][] data) throws JsonException {
        int type = Integer.parseInt(data[1][0]);
        if (type < 0 || type > 2)
            throw new JsonException("Type d'employé invalide");

        else
            return type;
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
    public static int validation(String[][] array, int z, int[] nbr) {
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

    public static void erreurJson(String erreur,String arg) throws Exception{
        File file = new File(arg);
        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write("{\"message\": "+"\"" + erreur +"\""+ "}");
        bufferedWriter.close();
    }

    public static void validationDate(String [][] donnees,String json) throws JsonException{
        for(int i=4; i< GestionJson.calculInterventions(json) + 4; i++) { //+4 pour que je puisse itterer vers tous les itterations
            if(!(validerFormatDate(donnees[i][4]))) {
                i = i-3; // pour que j'ai le nombre correct de l'intervention
                throw new JsonException("Format de date invalide dans l'intervention " + i+ ".");
            }
        }
    }
}
