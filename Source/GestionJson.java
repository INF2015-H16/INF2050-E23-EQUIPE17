package Source;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;

import java.io.*;

public class GestionJson {


    /**
     * Convertit une chaîne JSON en un tableau à deux dimensions contenant les données extraites.
     *
     * @param json La chaîne JSON à convertir.
     * @return Un tableau à deux dimensions contenant les données extraites du JSON.
     */
    public static String[][] lireFichierEntreeJson(String json) {
        int j;
        String[][] tableau = new String[9][5];

        JSONObject employee = JSONObject.fromObject(json);

        // Extraction des données de l'employé
        tableau[0][0] = employee.getString("matricule_employe");
        tableau[1][0] = employee.getString("type_employe");
        tableau[2][0] = employee.getString("taux_horaire_min");
        tableau[3][0] = employee.getString("taux_horaire_max");

        JSONArray interventions = employee.getJSONArray("interventions");

        // Extraction des données des interventions
        for (j = 0; j < interventions.size(); j++) {
            JSONObject intervention = interventions.getJSONObject(j);

            tableau[4 + j][0] = intervention.optString("code_client"); // Utilisation de optString au lieu de getString
            tableau[4 + j][1] = intervention.getString("distance_deplacement");
            tableau[4 + j][2] = intervention.getString("overtime");
            tableau[4 + j][3] = intervention.getString("nombre_heures");
            tableau[4 + j][4] = intervention.optString("date_intervention");
        }

        tableau[8][0] = String.valueOf(interventions.size());

        return tableau;
    }

    /**
     * Écrit les données fournies dans un fichier JSON.
     *
     * @param matricule_employe   Le matricule de l'employé.
     * @param etat_compte         L'état du compte de l'employé.
     * @param cout_fixe           Le coût fixe de l'employé.
     * @param cout_variable       Le coût variable de l'employé.
     * @param code                Les codes des clients.
     * @param etat_par_client     Les états par client.
     * @param j                   Le nombre de clients.
     * @param arg                 Le nom du fichier de sortie.
     */
    public static void ecrireFichierSortieJson(int matricule_employe, double etat_compte, double cout_fixe, double cout_variable,
                                               String[] code, double[] etat_par_client, int j, String arg, int[] nbrs) {
        JSONObject employee = new JSONObject();
        CalculEmploye calculEmploye = new CalculEmploye();

        employee.accumulate("matricule_employe", matricule_employe);
        employee.accumulate("etat_compte", etat_compte + "$");
        employee.accumulate("cout_fixe", cout_fixe + "$");
        employee.accumulate("cout_variable", cout_variable + "$");

        JSONArray clients = new JSONArray();
        JSONObject client = new JSONObject();

        // Ajout des données de chaque client
        for(int i=0; i<j ; i++) {
            if(calculEmploye.verification(nbrs,i))
            {
                client.accumulate("code_client", code[i]);
                client.accumulate("etat_par_client", etat_par_client[i] + "$");
                clients.add(client);
                client.clear();
            }
        }

        employee.accumulate("clients", clients);

        try {
            // Écriture du contenu JSON dans le fichier spécifié
            FileUtils.writeStringToFile(new File(arg), employee.toString(2), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void validerInterventions(JSONObject objetJSON) { //TODO faire la validation s'il y a des lettres dans les objets
        // Validation du type d'employé
        int typeEmploye = objetJSON.getInt("typeEmploye");
        if (typeEmploye != 0 && typeEmploye != 1 && typeEmploye != 2) {
            System.out.println("Erreur: le type d'employé n'est pas valide.");
        }

        // Validation du nombre distance_deplacement
        if (!objetJSON.has("distanceDeplacement")) {
            System.out.println("Erreur: la propriété distanceDeplacement est manquante dans l'objet JSON.");
        } else {
            int distanceDeplacement = objetJSON.getInt("distanceDeplacement");
            if (distanceDeplacement < 0) {
                System.out.println("Erreur: le nombre de distanceDeplacement ne peut pas être négatif.");
            }
        }

        // Validation des overtimes
        if (!objetJSON.has("overtime")) {
            System.out.println("Erreur: la propriété overtime est manquante dans l'objet JSON.");
        } else {
            int overtime = objetJSON.getInt("overtime");
            if (overtime < 0) {
                System.out.println("Erreur: le nombre de overtime ne peut pas être négatif.");
            }
        }

        // Validation du nombre d'heures
        if (!objetJSON.has("nombreHeures")) {
            System.out.println("Erreur: la propriété nombreHeures est manquante dans l'objet JSON.");
        } else {
            int nombreHeures = objetJSON.getInt("nombreHeures");
            if (nombreHeures <= 0) {
                System.out.println("Erreur: Le nombre d'heures doit être supérieur à zéro.");
            }
        }

        // Validation du nombre d'interventions
        if (!objetJSON.has("interventions")) {
            System.out.println("Erreur: la propriété interventions est manquante dans l'objet JSON.");
        } else {
            String interventions = objetJSON.getString("interventions");
            int nombreInterventions = 0;
            for (int i = 0; i < interventions.length(); i++) {
                if (Character.isLetter(interventions.charAt(i))) {
                    nombreInterventions++;
                }
            }

            if (nombreInterventions == 0) {
                System.out.println("Erreur : Aucune intervention valide n'a été détectée.");
            }
        }
    }

    public static double convertirMontantEnDecimal(String montant){
        if (montant.contains(",")){
            montant.replace(",",".");
        }
        return Double.parseDouble(montant);
    }

    public static void modifierProprietesJSONEnMinuscule(JSONObject objetJSON) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(String.valueOf(objetJSON)));
        String valeurJson = reader.readLine();
        while (valeurJson != null) {
            valeurJson.toLowerCase();
            if (valeurJson.contains(" ")){
                valeurJson.replace(" ","");
            }
        }

    }


}