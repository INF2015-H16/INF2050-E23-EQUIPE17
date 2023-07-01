package Source;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.File;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Set;

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



    public static void validationDate(String [][] donnees,int tailleInterventions) throws JsonException{
            for (int i = 4; i <  + 4; i++) { //+4 pour que je puisse itterer vers tous les itterations
                if (!(validerFormatDate(donnees[i][4]))) {
                    i = i - 3; // pour que j'ai le nombre correct de l'intervention
                    throw new JsonException("Format de date invalide dans l'intervention " + i + ".");
                }
            }
    }

    public static void validerFichierSortieDispo(String cheminFichierSortie) {
        File fichierSortie = new File(cheminFichierSortie);
        try {
            if (!fichierSortie.exists()) {
                boolean fichierCree = fichierSortie.createNewFile();
                if (!fichierCree) {
                    System.out.println("Impossible de créer le fichier de sortie. Veuillez vérifier les autorisations d'écriture et le chemin spécifié.");
                }
            }
        } catch (IOException e) {
            System.out.println("Une erreur s'est produite lors de la création du fichier de sortie. Veuillez vérifier les autorisations d'écriture et le chemin spécifié.");
        }
    }

    public static void validerProprietesJsonPresentes(JSONObject jsonObject, String arg2) throws IOException {

        if (!jsonObject.has("matricule_employe"))
            GestionProgramme.ajouterMessage("Attribut 'matricule_employe' manquant", arg2);

        if (!jsonObject.has("type_employe"))
            GestionProgramme.ajouterMessage("Attribut 'type_employe' manquant", arg2);

        if (!jsonObject.has("taux_horaire_min"))
            GestionProgramme.ajouterMessage("Attribut 'taux_horaire_min' manquant", arg2);

        if (!jsonObject.has("taux_horaire_max"))
            GestionProgramme.ajouterMessage("Attribut 'taux_horaire_max' manquant", arg2);

        if (!jsonObject.has("interventions"))
            GestionProgramme.ajouterMessage("Attribut 'interventions' manquant", arg2);
    }

    public static void validerInterventionsNonVide(JSONArray interventions, String arg) throws IOException {
        if (interventions.size() == 0) {
            GestionProgramme.ajouterMessage("Aucune intervention trouvée dans le fichier JSON",arg);
        }
    }

    public static void validerComboCodeClientDateIntervention(JSONArray interventionsArray, String cheminJson) throws IOException {
        Set<String> codeClients = new HashSet<>();
        Set<String> dates = new HashSet<>();
        for (int i = 0; i < interventionsArray.size(); i++) {
            verificationInterventions(interventionsArray, cheminJson, codeClients, dates, i);
        }
        validerChampVide(interventionsArray,cheminJson);
    }

    private static void verificationInterventions(JSONArray interventionsArray, String cheminJson, Set<String> codeClients, Set<String> dates, int i) throws IOException {
        JSONObject intervention = interventionsArray.getJSONObject(i);
        String codeClient = intervention.getString("code_client");
        String dateIntervention = intervention.getString("date_intervention");
        if (codeClients.contains(codeClient) && dates.contains(dateIntervention))
            GestionProgramme.ajouterMessage("Intervention non valide : même code client et même date", cheminJson);
        codeClients.add(codeClient);
        dates.add(dateIntervention);
    }

    private static void validerChampVide(JSONArray interventions, String cheminJson) throws IOException {
        for (int i = 0; i < interventions.size(); i++) {
            JSONObject intervention = interventions.getJSONObject(i);
            if (intervention.getString("code_client").equals("") || intervention.getString("code_client") == null) {
                GestionProgramme.ajouterMessage("Un champ de code client est vide", cheminJson);
            }
            if (intervention.getString("date_intervention").equals("") || intervention.getString("date_intervention") == null) {
                GestionProgramme.ajouterMessage("Un champ de date est vide", cheminJson);
            }
        }
    }
}


