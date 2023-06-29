package Source;
<<<<<<< HEAD

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Main {

    public static void main(String[] args) throws IOException {
        CalculEmploye calculEmploye = new CalculEmploye();

        String[][] donnees;
        //TODO renommer "test.json" en "entree.json" partout ou il figure avant la remise du projet
        String argument = "test.json";
        String argument2 = "sortie.json";
        String json = "";

        String buffer;

        boolean fichierExiste = validerFichierEntreeDisponible(argument);

        if(!fichierExiste){
            System.out.println("Le fichier d'entree n'existe pas.");
            System.exit(0);
        }

=======
import java.io.*;


public class Main {
    public static void main(String[] args) throws Exception {
        String[][] donnees;
        String argument = "test-.json",argument2 = "sortie.json",json = "",buffer = "";
        try {
            json = lecteurFichier(argument, json,buffer);
            donnees = GestionJson.lireFichierEntreeJson(json);        // Conversion du contenu JSON en tableau de données
            GestionProgramme.executer(donnees, argument2);
        } catch (JsonException e) {
            JsonException.erreurJson(e.getMessage(),argument2);
        }
    }

    private static String lecteurFichier(String argument, String json, String buffer) throws JsonException {
>>>>>>> 393e922146a6793f5382238bcb3a5299c9b1fd6a
        try (BufferedReader reader = new BufferedReader(new FileReader(argument))) {
            while ((buffer = reader.readLine()) != null) {
        //
                if (buffer != null)
                    json += buffer;
                json += "\n";
            }
        } catch (IOException e) {
<<<<<<< HEAD
            e.printStackTrace();
        }

        // Conversion du contenu JSON en tableau de données
        donnees = GestionJson.lireFichierEntreeJson(json);
        try {
            executer(donnees, argument2);
        } catch (JsonException e) {
            throw new RuntimeException(e);
=======
            throw new JsonException("Fichier Introuvable.");
>>>>>>> 393e922146a6793f5382238bcb3a5299c9b1fd6a
        }
        return json;
    }

<<<<<<< HEAD
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
                    System.exit(0);
                }
            }
        } catch (IOException e) {
            System.out.println("Une erreur s'est produite lors de la création du fichier de sortie.");
            System.out.println("Veuillez vérifier les autorisations d'écriture et le chemin spécifié.");
            System.exit(0);
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

        if (attributsManquants) {
            // Méthode pour écrire l'objet JSONObject dans un fichier JSON de sortie
            ecrireFichierJsonSortie("C:\\Users\\steve\\IdeaProjects\\INF2050-E23-EQUIPE17" +
                    "\\sortie.json", jsonObject);
        }
    }

    private static void ajouterMessage(JSONObject jsonObject, String message) {
        JSONObject messageObjet = new JSONObject();
        messageObjet.accumulate("message", message);

        jsonObject.accumulate("message", messageObjet);
    }

    private static void ecrireFichierJsonSortie(String cheminFichierSortie, JSONObject jsonObjectSortie) {
        try (FileWriter fileWriter = new FileWriter(cheminFichierSortie)) {
            fileWriter.write(jsonObjectSortie.toString());
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
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



    /**
     * Vérifie si un entier `j` est présent dans le tableau `nbrs`.
     *
     * @param nbrs Le tableau d'entiers à vérifier.
     * @param j    L'entier à rechercher.
     * @return `true` si l'entier `j` n'est pas présent dans le tableau `nbrs`, `false` sinon.
     */
    public static boolean verification(int[] nbrs, int j) { //l'argument "j" refere au code client dans le fichier JSON
        for (int i = 0; i < nbrs.length; i++) {
            if (nbrs[i] == j)
                return false;
        }
        return true;
    }


    public static int checkDistance(int nbr) throws JsonException
    {
        if (nbr < 0 || nbr > 100) {
            throw new JsonException("Distance deplacement invalide");
        }
        return nbr;
    }

    public static int checkOvertime(int nbr) throws JsonException
    {
        if (nbr < 0 || nbr > 4) {
            throw new JsonException("Overtime invalide");
        }

        return nbr;
    }

    public static int checkNombreHeures(int nbr) throws JsonException
    {
        if (nbr < 0  || nbr > 8) {
            throw new JsonException("Nombre d'heures invalide");
        }

        return nbr;
    }

    public static double checkerTaux(String[][] data,int i) throws JsonException
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

    public static void checkerTypeEmploye(int type) throws JsonException {
        if (type < 0 || type > 2) {
            throw new JsonException("Type d'employé invalide");
        }
    }

    public static void validerNombreInterventions(int nombre) throws JsonException {
        if (nombre > 10) {
            throw new JsonException("Nombre d'interventions invalide");
        }
    }
=======
    //TODO: FORMAT A respecter 0.00$
>>>>>>> 393e922146a6793f5382238bcb3a5299c9b1fd6a

    //TODO: rendre la methode executer plus courte en creant des sous methodes

    //TODO: ajouter une methode qui check l'occurence
}