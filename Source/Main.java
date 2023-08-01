package Source;

import net.sf.json.JSONArray;


import java.io.*;

public class Main {
    public static void main(String[] args) throws Exception {

        String[][] donnees;
        JSONArray observations = new JSONArray(),interventions = null;
        String argument = args[0],argument2 = args[1],json = "",buffer = "",argument3 = args[2];

        try {
            json = lecteurFichier(argument, json,buffer);
            donnees = GestionJson.lireFichierEntreeJson(GestionJson.convertirMajusculesEnMinuscules(json),
                    argument2,observations,interventions );  // Conversion du contenu JSON en tableau de données
            GestionProgramme.executerRecuperationInterventions(donnees, argument2, json,observations,
                    argument3, interventions);
        } catch (JsonException e) {
            GestionProgramme.ajouterMessage(e.getMessage(),argument2);
        }
    }

    public static String lecteurFichier(String argument, String json, String buffer) throws JsonException {

        try (BufferedReader reader = new BufferedReader(new FileReader(argument))) {

            while ((buffer = reader.readLine()) != null) {

                if (buffer != null)
                    json += buffer;
                json += "\n";
            }
        } catch (IOException e) {
            throw new JsonException("Fichier Introuvable.");
        }
        return json;
    }
}