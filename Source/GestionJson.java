package Source;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;

import java.io.*;

public class GestionJson {

    public static int calculInterventions(String json) {
        int nombreinterventions = 0;
        try {
            nombreinterventions = JSONObject.fromObject(json).getJSONArray("interventions").size();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return nombreinterventions;
    }

    /**
     * Convertit une chaîne JSON en un tableau à deux dimensions contenant les données extraites.
     *
     * @param json La chaîne JSON à convertir.
     * @return Un tableau à deux dimensions contenant les données extraites du JSON.
     */
    public static String[][] lireFichierEntreeJson(String json) {               // le 5 c'est pour le type, marticule et les taux
        String[][] attributsJson = new String[5+calculInterventions(json)][5]; //La methode calculInterventions permet de savoir combien on va resever d'espace pour les interventions
        JSONObject employee = JSONObject.fromObject(json);
        attributsJson = recuperationInfo(attributsJson, employee);
        JSONArray interventions = employee.getJSONArray("interventions");
        attributsJson = recuperationInfo2(attributsJson, interventions);
        attributsJson[attributsJson.length-1][0] = String.valueOf(interventions.size());
        return attributsJson;
    }

    private static String[][] recuperationInfo2(String[][] attributsJson, JSONArray interventions) {
        for (int compteurInterventions = 0; compteurInterventions < interventions.size(); compteurInterventions++) {
            JSONObject intervention = interventions.getJSONObject(compteurInterventions);
            attributsJson[4 + compteurInterventions][0] = intervention.optString("code_client"); // Utilisation de optString au lieu de getString
            attributsJson[4 + compteurInterventions][1] = intervention.getString("distance_deplacement");
            attributsJson[4 + compteurInterventions][2] = intervention.getString("overtime");
            attributsJson[4 + compteurInterventions][3] = intervention.getString("nombre_heures");
            attributsJson[4 + compteurInterventions][4] = intervention.optString("date_intervention");
        }
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
        employee.accumulate("etat_compte", etat_compte + "$");
        employee.accumulate("cout_fixe", cout_fixe + "$");
        employee.accumulate("cout_variable", cout_variable + "$");
        return employee;
    }

    private static JSONArray preparationJson(String[] code, double[] etat_par_client, int j, int[] nbrs, JSONObject employee, CalculEmploye calculEmploye, JSONArray clients, JSONObject client) {
        for(int i = 0; i< j; i++) {
            if(GestionProgramme.verification(nbrs,i)) {
                client.accumulate("code_client", code[i]);
                client.accumulate("etat_par_client", etat_par_client[i] + "$");
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
}