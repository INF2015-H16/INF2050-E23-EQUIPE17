package Source;
import java.io.*;


public class Main {
    public static void main(String[] args) {
        String[][] donnees;
        String argument = "test.json",argument2 = "sortie.json",json = "",buffer = "";
        json = lecteurFichier(argument, json,buffer);
        donnees = GestionJson.lireFichierEntreeJson(json);        // Conversion du contenu JSON en tableau de données
        try {
            GestionProgramme.executer(donnees, argument2);
        } catch (JsonException e) {
            System.out.println("Il y'a un probleme dans le fichier JSON");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static String lecteurFichier(String argument, String json, String buffer) {
        try (BufferedReader reader = new BufferedReader(new FileReader(argument))) {
            while ((buffer = reader.readLine()) != null) {
                if (buffer != null)
                    json += buffer;
                json += "\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    //TODO: FORMAT A respecter 0.00$

    //TODO: rendre la methode executer plus courte en creant des sous methodes

    //TODO: ajouter une methode qui check l'occurence
}