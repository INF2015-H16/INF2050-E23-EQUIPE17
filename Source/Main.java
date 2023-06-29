package Source;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws Exception {
        String[][] donnees;
        String argument = "test.json",argument2 = "sortie.json",json = "",buffer = "";
        try {
            json = lecteurFichier(argument, json,buffer);
            donnees = GestionJson.lireFichierEntreeJson(json);        // Conversion du contenu JSON en tableau de données
            GestionProgramme.executer(donnees, argument2, json);
            JsonException.validationDate(donnees,json);
        } catch (JsonException e) {
            JsonException.erreurJson(e.getMessage(),argument2);
        }
    }

    private static String lecteurFichier(String argument, String json, String buffer) throws JsonException {
        try (BufferedReader reader = new BufferedReader(new FileReader(argument))) {
            while ((buffer = reader.readLine()) != null) {
        //
                if (buffer != null)
                    json += buffer;
                json += "\n";
            }
        } catch (IOException e) {
            throw new JsonException("Fichier Introuvable.");
        }
        return json;
    }

    public static boolean validerFichierEntreeDisponible(String nomFichierEntree) {
        File fichier = new File(nomFichierEntree);
        return fichier.exists() && fichier.isFile();
    }

    public static void validerFichierSortieDispo(String cheminFichierSortie) {
        File fichierSortie = new File(cheminFichierSortie);

        try {
            if (!fichierSortie.exists()) {
                boolean fichierCree = fichierSortie.createNewFile();
                if (!fichierCree) {
                    System.out.println("Impossible de créer le fichier de sortie.");
                    System.out.println("Veuillez vérifier les autorisations d'écriture et le chemin spécifié.");
                }
            }
        } catch (IOException e) {
            System.out.println("Une erreur s'est produite lors de la création du fichier de sortie.");
            System.out.println("Veuillez vérifier les autorisations d'écriture et le chemin spécifié.");
        }
    }


    public static void validerProprietesJsonPresentes(JSONObject jsonObject) {
        boolean attributsManquants = false;

        if (!jsonObject.has("matricule_employe")) {
            ajouterMessage(jsonObject, "Attribut 'matricule_employe' manquant");
            attributsManquants = true;
        }

        if (!jsonObject.has("type_employe")) {
            ajouterMessage(jsonObject, "Attribut 'type_employe' manquant");
            attributsManquants = true;
        }

        if (!jsonObject.has("taux_horaire_min")) {
            ajouterMessage(jsonObject, "Attribut 'taux_horaire_min' manquant");
            attributsManquants = true;
        }

        if (!jsonObject.has("taux_horaire_max")) {
            ajouterMessage(jsonObject, "Attribut 'taux_horaire_max' manquant");
            attributsManquants = true;
        }

        if (!jsonObject.has("interventions")) {
            ajouterMessage(jsonObject, "Attribut 'interventions' manquant");
            attributsManquants = true;
        }

    }

    private static void ajouterMessage(JSONObject jsonObject, String message) {
        JSONObject messageObjet = new JSONObject();
        messageObjet.accumulate("message", message);

        jsonObject.accumulate("message", messageObjet);
    }

    public static boolean validerInterventionsNonVide(JSONObject jsonObject, JSONObject jsonObjectSortie) {
        JSONArray interventionsArray = jsonObject.getJSONArray("interventions");
        if (interventionsArray.size() == 0) {
            JSONObject messageObj = new JSONObject();
            messageObj.accumulate("message", "Aucune intervention trouvée dans le fichier JSON");
            jsonObjectSortie.put("message", messageObj);
            return false;
        }
        return true;
    }

    public static boolean validerComboCodeClientDateIntervention(JSONObject jsonObject, String cheminJson)
            throws IOException {

        JSONArray interventionsArray = jsonObject.getJSONArray("interventions");

        Set<String> codeClients = new HashSet<>();
        Set<String> dates = new HashSet<>();
        for (int i = 0; i < interventionsArray.size(); i++) {
            JSONObject intervention = interventionsArray.getJSONObject(i);
            String codeClient = intervention.getString("code_client");
            String dateIntervention = intervention.getString("date_intervention");
            if (codeClients.contains(codeClient) && dates.contains(dateIntervention)) {
                JSONObject messageObj = new JSONObject();
                messageObj.accumulate("message", "Intervention non valide : même code client et même date");
                FilesWriter.saveStringIntoFile(cheminJson, messageObj.toString());
                return false;
            }
            codeClients.add(codeClient);
            dates.add(dateIntervention);
        }
        return true;
    }

}