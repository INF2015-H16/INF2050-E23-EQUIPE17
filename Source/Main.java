package Source;

import java.io.*;

public class Main {
    public static void main(String[] args) throws Exception {
        String[][] donnees;
        String argument = "test.json",argument2 = "sortie.json",json = "",buffer = "";
        try {
            json = lecteurFichier(argument, json,buffer);
            donnees = GestionJson.lireFichierEntreeJson(json, argument2);        // Conversion du contenu JSON en tableau de données
            GestionProgramme.executerRecuperationInterventions(donnees, argument2, json);
        } catch (JsonException e) {
            GestionProgramme.ajouterMessage(e.getMessage(),argument2);
        }
    }

    private static String lecteurFichier(String argument, String json, String buffer) throws JsonException {
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