package Source;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class Validations {

    public static boolean validerComboCodeClientDateIntervention(JSONObject jsonObject, String cheminJson) throws IOException {

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
                FileWriter.saveStringIntoFile(cheminJson, messageObj.toString());
                return false;
            }

            codeClients.add(codeClient);
            dates.add(dateIntervention);
        }


        return true;
    }

            //public static void main(String[] args) {
                // Méthode pour lire le fichier JSON en entrée et obtenir l'objet JSONObject correspondant
              //  JSONObject jsonObjectEntree = lireFichierJsonEntree("chemin/vers/fichier_entree.json");

                // Validation des interventions
                //boolean interventionsValides = validerComboCodeClientDateIntervention(jsonObjectEntree);

                //if (!interventionsValides) {
                    // Méthode pour écrire l'objet JSONObject dans un fichier JSON de sortie
                  //  ecrireFichierJsonSortie("chemin/vers/fichier_sortie.json", jsonObjectEntree);
                //}
            //}

            // Méthode pour lire le fichier JSON d'entrée et retourner l'objet JSONObject correspondant

}
