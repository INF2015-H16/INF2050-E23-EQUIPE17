package Source;
import java.io.*;


public class Main {

    public static void main(String[] args) {

        String[][] donnees;
        String argument = "test.json";
        String argument2 = "sortie.json";
        String json = "";

        String buffer;

        try (BufferedReader reader = new BufferedReader(new FileReader(argument))) {
            // Lecture du contenu du fichier JSON ligne par ligne
            while ((buffer = reader.readLine()) != null) {
                if (buffer != null)
                    json += buffer;
                json += "\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Conversion du contenu JSON en tableau de données
        donnees = GestionJson.lireFichierEntreeJson(json);

        try {
            GestionProgramme.executer(donnees, argument2);
        } catch (JsonException e) {
            System.out.println("Il y'a un probleme dans le fichier JSON");
            e.printStackTrace();
            System.exit(1);
        }
    }

    //TODO: FORMAT A respecter 0.00$

    //TODO: rendre la methode executer plus courte en creant des sous methodes


    //TODO: ajouter une methode qui check l'occurence


}