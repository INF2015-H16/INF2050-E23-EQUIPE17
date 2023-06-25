package Source;

import java.io.*;

public class Main {

    public static void main(String[] args) {
        CalculEmploye calculEmploye = new CalculEmploye();

        String[][] donnees; // Déclaration d'une variable pour stocker les données
        String argument = args[0]; // Chemin vers le fichier d'entrée
        String argument2 = args[1]; // Nom du fichier de sortie
        String json = ""; // Variable pour stocker le contenu du fichier JSON
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

        // Appel de la méthode "executer" pour traiter les données et générer les résultats
        calculEmploye.executer(donnees, argument2);
    }



}