package Source;
import java.io.*;


public class Main {

    public static void main(String[] args) {
        CalculEmploye calculEmploye = new CalculEmploye();

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
            CalculEmploye.executer(donnees, argument2);
        } catch (JsonException e) {
            System.out.println("Il y'a un probleme dans le fichier JSON");
            e.printStackTrace();
            System.exit(1);
        }
    }
}