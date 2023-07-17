package Source;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.Locale;

public class GestionJson {

    public static String convertirMajusculesEnMinuscules(String valeur) {
        if (valeur != null) {
            valeur = valeur.toLowerCase();
            if (valeur.contains(" ")) {
                valeur = valeur.replace(" ", "");
            }
        }
        return valeur;
    }

    /**
     * Convertit une chaîne JSON en un tableau à deux dimensions contenant les données extraites.
     *
     * @param json       La chaîne JSON à convertir.
     * @param cheminJson
     * @return Un tableau à deux dimensions contenant les données extraites du JSON.
     */
    public static String[][] lireFichierEntreeJson(String json, String cheminJson,JSONArray observations) throws JsonException, IOException{
        JSONObject employee = JSONObject.fromObject(json);
        JSONArray interventions = employee.getJSONArray("interventions");
        String[][] attributsJson = new String[5+interventions.size()][5]; //La methode calculInterventions permet de savoir combien on va resever d'espace pour les interventions
        recuperationInfos(employee, interventions, attributsJson, cheminJson);
        JsonException.validerInterventionsNonVide(interventions,cheminJson);
        attributsJson[attributsJson.length-1][0] = String.valueOf(interventions.size());
        JsonException.validerComboCodeClientDateIntervention(interventions,cheminJson,observations);
        observationDates(attributsJson,observations);
        return attributsJson;
    }

    private static void observationDates(String[][] attributsJson, JSONArray observations) {
        String date1,date2,codeClient;
        int mois1,annee1,annee2,mois2;

        int i=4;
        while(attributsJson[i][4] != null){
            codeClient = attributsJson[i][0];
            date1 = attributsJson[i][4];
            for(int j=i+1; j<attributsJson.length-1 ; j++)
            {
                if(attributsJson[j][0].equals(codeClient))
                {
                    date2 = attributsJson[j][4];
                    mois1 = Integer.parseInt(date1.substring(5, 7));
                    annee1 = Integer.parseInt(date1.substring(0, 4));

                    mois2 = Integer.parseInt(date2.substring(5, 7));
                    annee2 = Integer.parseInt(date2.substring(0, 4));

                    int monthsApart = (annee2 - annee1) * 12 + (mois2 - mois1);

                    if(monthsApart >= 6)
                        observations.add("L’écart maximal entre les dates d’intervention (date_intervention) du client " + codeClient +
                                " d’un même employé doit être de moins de 6 mois.");                }

            }
            i++;
        }
    }

    private static void recuperationInfos(JSONObject employee, JSONArray interventions, String[][] attributsJson, String cheminJson) throws JsonException, IOException {
        recuperationInfo(attributsJson, employee);
        recuperationInfo(attributsJson, interventions, cheminJson);
    }

    private static void recuperationInfo(String[][] attributsJson, JSONArray interventions, String cheminJson) throws JsonException, IOException {
        try {
            for (int compteurInterventions = 0; compteurInterventions < interventions.size(); compteurInterventions++) {
                reccuperationAttributs(attributsJson, interventions, compteurInterventions);
            }
        }
        catch (Exception e) {
            JsonException.validerProprietesJsonPresentes(e.getMessage(),cheminJson);
        }
        JsonException.validationDate(attributsJson,interventions.size());
    }

    private static void reccuperationAttributs(String[][] attributsJson, JSONArray interventions, int compteurInterventions) {
        JSONObject intervention = interventions.getJSONObject(compteurInterventions);
        attributsJson[4 + compteurInterventions][0] = intervention.optString("code_client").toUpperCase(); // Utilisation de optString au lieu de getString
        attributsJson[4 + compteurInterventions][1] = intervention.getString("distance_deplacement");
        attributsJson[4 + compteurInterventions][2] = intervention.getString("overtime");
        attributsJson[4 + compteurInterventions][3] = intervention.getString("nombre_heures");
        attributsJson[4 + compteurInterventions][4] = intervention.optString("date_intervention");
    }

    private static void recuperationInfo(String[][] attributsJson, JSONObject employee) {
        attributsJson[0][0] = employee.getString("matricule_employe");
        attributsJson[1][0] = employee.getString("type_employe");
        attributsJson[2][0] = employee.getString("taux_horaire_min");
        attributsJson[3][0] = employee.getString("taux_horaire_max");
    }


    public static void formattageFichierSortieJson(int matricule_employe, double etat_compte, double cout_fixe, double cout_variable, String[] code, double[] etat_par_client, int j, String arg, int[] nbrs,JSONArray observation) {
        JSONObject employee = new JSONObject();
        employee = employeeInfo(matricule_employe, etat_compte, cout_fixe, cout_variable, employee,observation);
        JSONArray clients = new JSONArray();
        JSONObject client = new JSONObject();
        employee.accumulate("clients", preparationJson(code, etat_par_client, j, nbrs, clients, client, employee,observation));
        employee.accumulate("observations",observation);
        ecritureFichierSortieJson(arg,employee);
    }

    private static JSONObject employeeInfo(int matricule_employe, double etat_compte, double cout_fixe, double cout_variable, JSONObject employee,JSONArray observations) {
        employee.accumulate("matricule_employe", matricule_employe);
        employee.accumulate("etat_compte", String.format(Locale.CANADA,"%.2f$", etat_compte));
        employee.accumulate("cout_fixe", String.format(Locale.CANADA,"%.2f$", cout_fixe));
        employee.accumulate("cout_variable", String.format(Locale.CANADA,"%.2f$", cout_variable));
        employeeObservation(employee,cout_variable,etat_compte,cout_fixe,observations);
        return employee;
    }

    private static JSONObject employeeObservation(JSONObject employee,double cout_variable,double etat_compte, double cout_fixe,JSONArray observations){
        if(cout_variable > 3000)
            observations.add("Le cout variable payable nécessite des ajustements");
        if(etat_compte > 30000)
            observations.add("L’état de compte total ne doit pas dépasser 30000.00 $.");
        if(cout_fixe > 1500)
            observations.add("Le cout fixe payable ne doit pas dépasser 1500.00 $.");
        return employee;
    }

    private static JSONArray preparationJson(String[] code, double[] etat_par_client, int j, int[] nbrs, JSONArray clients, JSONObject client, JSONObject employee, JSONArray observation) {
        for(int i = 0; i < j; i++) {
            if(GestionProgramme.verificationCodeClient(nbrs, i) && code[i] != null) {
                client.accumulate("code_client", code[i]);
                client.accumulate("etat_par_client", String.format(Locale.CANADA,"%.2f$", etat_par_client[i]));
                clients.add(client);
                client.clear();
                etatClientObservation(observation,employee,etat_par_client[i],code[i]);
            }
        }
        return clients;
    }

    private static void etatClientObservation(JSONArray observation, JSONObject employee, double etat_par_client,String code) {
        if(etat_par_client > 15000)
            observation.add("L’état par client du client " + code + " est trop dispendieuse.");
    }


    private static void ecritureFichierSortieJson(String arg,JSONObject employee) {
        try {
            FileUtils.writeStringToFile(new File(arg), employee.toString(2), "UTF-8");// le 2 dans tostring sert a ecrire le json d'une facon indente
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}