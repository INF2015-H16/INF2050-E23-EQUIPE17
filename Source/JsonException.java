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
            taux_horaire = Double.parseDouble(data[i][0].replace(",", ".").replace("$", ""));// Sa c'est pour gerer la virgule
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

    public static boolean validerFormatDate(String dateStr) {
        try {
            LocalDate.parse(dateStr);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static int validation(String[][] donnees, int i)
    {
        for(int j=i+1; j<donnees.length-1 ; j++) {
            if(!donnees[j][0].equals("")) {
                if (donnees[j][0].equals(donnees[i][0])) {
                    donnees[j][0] = "";
                    return j;
                }
            }
        }

        return -1;
    }

    public static void validationDate(String [][] donnees) throws JsonException{
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
            existFichier(fichierSortie);
        } catch (IOException e) {
            System.out.println("Une erreur s'est produite lors de la création du fichier de sortie. Veuillez vérifier les autorisations d'écriture et le chemin spécifié.");
        }
    }

    private static void existFichier(File fichierSortie) throws IOException {
        if (!fichierSortie.exists()) {
            boolean fichierCree = fichierSortie.createNewFile();
            if (!fichierCree)
                System.out.println("Impossible de créer le fichier de sortie. Veuillez vérifier les autorisations d'écriture et le chemin spécifié.");
        }
    }

    public static void validerProprietesJsonPresentes(String jsonObject, String arg2) throws IOException {
        codeClientVerification(jsonObject, arg2);
        matriculeVerification(jsonObject, arg2);
        typeVerification(jsonObject, arg2);
        taux_horaireVerification(jsonObject, arg2);
        interventionsVerification(jsonObject, arg2);
        distanceDeplacement(jsonObject, arg2);
        overtimeVerification(jsonObject, arg2);
        nombreHeuresVerification(jsonObject, arg2);
        dateVerification(jsonObject, arg2);
    }

    private static void dateVerification(String jsonObject, String arg2) throws IOException {
        if (jsonObject.contains("date_intervention"))
            GestionProgramme.ajouterMessage("Attribut 'date_intervention' manquant", arg2);
    }

    private static void nombreHeuresVerification(String jsonObject, String arg2) throws IOException {
        if (jsonObject.contains("nombre_heures"))
            GestionProgramme.ajouterMessage("Attribut 'nombre_heures' manquant", arg2);
    }

    private static void overtimeVerification(String jsonObject, String arg2) throws IOException {
        if (jsonObject.contains("overtime"))
            GestionProgramme.ajouterMessage("Attribut 'overtime' manquant", arg2);
    }

    private static void distanceDeplacement(String jsonObject, String arg2) throws IOException {
        if (jsonObject.contains("distance_deplacement"))
            GestionProgramme.ajouterMessage("Attribut 'distance_deplacement' manquant", arg2);
    }

    private static void interventionsVerification(String jsonObject, String arg2) throws IOException {
        if (jsonObject.contains("interventions"))
            GestionProgramme.ajouterMessage("Attribut 'interventions' manquant", arg2);
    }

    private static void taux_horaireVerification(String jsonObject, String arg2) throws IOException {
        if (jsonObject.contains("taux_horaire_max") || jsonObject.contains("taux_horaire_min") )
            GestionProgramme.ajouterMessage("Attribut 'taux_horaire' manquant", arg2);
    }

    private static void typeVerification(String jsonObject, String arg2) throws IOException {
        if (jsonObject.contains("type_employe"))
            GestionProgramme.ajouterMessage("Attribut 'type_employe' manquant", arg2);
    }

    private static void matriculeVerification(String jsonObject, String arg2) throws IOException {
        if (jsonObject.contains("matricule_employe"))
            GestionProgramme.ajouterMessage("Attribut 'matricule_employe' manquant", arg2);
    }

    private static void codeClientVerification(String jsonObject, String arg2) throws IOException {
        if (jsonObject.contains("code_client"))
            GestionProgramme.ajouterMessage("Attribut 'code_client' manquant", arg2);
    }

    public static void validerInterventionsNonVide(JSONArray interventions, String arg) throws IOException {
        if (interventions.size() == 0) {
            GestionProgramme.ajouterMessage("Aucune intervention trouvée dans le fichier JSON",arg);
        }
    }

    public static void validerComboCodeClientDateIntervention(JSONArray interventionsArray, String cheminJson,JSONArray observations) throws IOException, JsonException {
        Set<String> codeClients = new HashSet<>();
        Set<String> dates = new HashSet<>();
        for (int i = 0; i < interventionsArray.size(); i++) {
            try {
                verificationInterventions(interventionsArray, cheminJson, codeClients, dates, i,observations);
            }catch (Exception e){
                validerProprietesJsonPresentes(e.getMessage(),cheminJson);
            }
        }
        validerChampVide(interventionsArray,cheminJson);
    }

    private static void verificationInterventions(JSONArray interventionsArray, String cheminJson, Set<String> codeClients, Set<String> dates, int i,JSONArray observations) throws IOException,Exception {
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


