package Source;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;

import java.io.*;

public class GestionJson {
    public static int calculInterventions(String json) throws JsonException,IOException {
        int nombreinterventions;
        JSONObject jsonObject = JSONObject.fromObject(json);
        JsonException.validerProprietesJsonPresentes(jsonObject, json);
        JSONArray interventions = jsonObject.getJSONArray("interventions");
        nombreinterventions = interventions.size();
        if(nombreinterventions > 10)
            throw new JsonException("Le nombre d’interventions est supérieur à 10.");
        return nombreinterventions;
    }

    /**
     * Convertit une chaîne JSON en un tableau à deux dimensions contenant les données extraites.
     *
     * @param json      La chaîne JSON à convertir.
     * @param cheminJson
     * @return Un tableau à deux dimensions contenant les données extraites du JSON.
     */
    public static String[][] lireFichierEntreeJson(String json, String cheminJson) throws JsonException, IOException{
        JSONObject employee = JSONObject.fromObject(json);
        JSONArray interventions = employee.getJSONArray("interventions");
        String[][] attributsJson = new String[5+interventions.size()][5]; //La methode calculInterventions permet de savoir combien on va resever d'espace pour les interventions
        attributsJson = recuperationInfo(attributsJson, employee);// le 5 c'est pour le type, marticule et les taux
        JsonException.validerInterventionsNonVide(interventions,cheminJson);
        attributsJson = recuperationInfo(attributsJson, interventions);
        attributsJson[attributsJson.length-1][0] = String.valueOf(interventions.size());
        JsonException.validerComboCodeClientDateIntervention(interventions,cheminJson);
        return attributsJson;
    }

    private static String[][] recuperationInfo(String[][] attributsJson, JSONArray interventions) throws JsonException {
        for (int compteurInterventions = 0; compteurInterventions < interventions.size(); compteurInterventions++) {
            JSONObject intervention = interventions.getJSONObject(compteurInterventions);
            attributsJson[4 + compteurInterventions][0] = intervention.optString("code_client"); // Utilisation de optString au lieu de getString
            attributsJson[4 + compteurInterventions][1] = intervention.getString("distance_deplacement");
            attributsJson[4 + compteurInterventions][2] = intervention.getString("overtime");
            attributsJson[4 + compteurInterventions][3] = intervention.getString("nombre_heures");
            attributsJson[4 + compteurInterventions][4] = intervention.optString("date_intervention");
        }
        JsonException.validationDate(attributsJson,interventions.size());
        return attributsJson;
    }

    private static String[][] recuperationInfo(String[][] attributsJson, JSONObject employee) {
        attributsJson[0][0] = employee.getString("matricule_employe");
        attributsJson[1][0] = employee.getString("type_employe");
        attributsJson[2][0] = employee.getString("taux_horaire_min");
        attributsJson[3][0] = employee.getString("taux_horaire_max");
        return attributsJson;
    }


    public static void formattageFichierSortieJson(int matricule_employe, double etat_compte, double cout_fixe, double cout_variable, String[] code, double[] etat_par_client, int j, String arg, int[] nbrs) {
        JSONObject employee = new JSONObject();
        CalculEmploye calculEmploye = new CalculEmploye();
        employee = employeeInfo(matricule_employe, etat_compte, cout_fixe, cout_variable, employee);
        JSONArray clients = new JSONArray();
        JSONObject client = new JSONObject();
        employee.accumulate("clients", preparationJson(code, etat_par_client, j, nbrs, employee, calculEmploye, clients, client));
        ecritureFichierSortieJson(arg,employee);
    }

    private static JSONObject employeeInfo(int matricule_employe, double etat_compte, double cout_fixe, double cout_variable, JSONObject employee) {
        employee.accumulate("matricule_employe", matricule_employe);
        employee.accumulate("etat_compte", String.format("%.2f$", etat_compte));
        employee.accumulate("cout_fixe", String.format("%.2f$", cout_fixe));
        employee.accumulate("cout_variable", String.format("%.2f$", cout_variable));
        return employee;
    }


    private static JSONArray preparationJson(String[] code, double[] etat_par_client, int j, int[] nbrs, JSONObject employee, CalculEmploye calculEmploye, JSONArray clients, JSONObject client) {
        for(int i = 0; i < j; i++) {
            if(GestionProgramme.verificationCodeClient(nbrs, i)) {
                client.accumulate("code_client", code[i]);
                client.accumulate("etat_par_client", String.format("%.2f$", etat_par_client[i]));
                clients.add(client);
                client.clear();
            }
        }
        return clients;
    }


    private static void ecritureFichierSortieJson(String arg,JSONObject employee) {
        try {
            FileUtils.writeStringToFile(new File(arg), employee.toString(2), "UTF-8");// le 2 dans tostring sert a ecrire le json d'une facon indente
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Modifie les propriétés d'un objet JSON en convertissant les majuscules en minuscules et en remplaçant les "." par ",".
     * Les montants sont également convertis en décimaux.
     *
     * @param objetJSON L'objet JSON à modifier.
     * @throws IOException Si une erreur de lecture du fichier JSON se produit.
     */
    public static void modifierProprietesJSON(JSONObject objetJSON) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(String.valueOf(objetJSON)));
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            line = convertirMajusculesEnMinuscules(line);
            jsonBuilder.append(line).append(System.lineSeparator());
        }
        reader.close();
    }

    public static String convertirMajusculesEnMinuscules(String valeur) {
        if (valeur != null) {
            valeur = valeur.toLowerCase();
            if (valeur.contains(" ")) {
                valeur = valeur.replace(" ", "");
            }
        }
        return valeur;
    }
}