package Source;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class GestionJson {


    /**
     * Convertit une chaîne JSON en un tableau à deux dimensions contenant les données extraites.
     *
     * @param json La chaîne JSON à convertir.
     * @return Un tableau à deux dimensions contenant les données extraites du JSON.
     */
    public static String[][] lecture(String json) {
        int j;
        String[][] tableau = new String[9][5];

        JSONObject employee;

        employee = JSONObject.fromObject(json);

        // Extraction des données de l'employé
        tableau[0][0] = employee.getString("matricule_employe");
        tableau[1][0] = employee.getString("type_employe");
        tableau[2][0] = employee.getString("taux_horaire_min");
        tableau[3][0] = employee.getString("taux_horaire_max");

        JSONArray interventions = employee.getJSONArray("interventions");
        String[][] intervention = new String[interventions.size()][5];

        // Extraction des données des interventions
        for (j = 0; j < interventions.size(); j++) {
            JSONObject Intervention = interventions.getJSONObject(j);

            intervention[j][0] = Intervention.getString("code_client");
            intervention[j][1] = Intervention.getString("distance_deplacement");
            intervention[j][2] = Intervention.getString("overtime");
            intervention[j][3] = Intervention.getString("nombre_heures");
            intervention[j][4] = Intervention.optString("date_intervention");
        }

        // Copie des données d'intervention dans le tableau principal
        tableau[4] = intervention[0];
        tableau[5] = intervention[1];
        tableau[6] = intervention[2];
        tableau[7] = intervention[3];
        tableau[8][0] = String.valueOf(j);

        return tableau;
    }



    public static void ecriture(int matricule_employe, double etat_compte,
                                double cout_fixe,double cout_variable,String[] code
            ,double[] etat_par_client,int j,String arg)
    {

        JSONObject employee = new JSONObject();

        employee.accumulate("matricule_employe",matricule_employe);
        employee.accumulate("etat_compte",etat_compte + "$");
        employee.accumulate("cout_fixe",cout_fixe + "$");
        employee.accumulate("cout_variable",cout_variable + "$");

        JSONArray clients = new JSONArray();
        JSONObject client = new JSONObject();

        for(int i=0; i<j ; i++) {
            client.accumulate("code_client", code[i]);
            client.accumulate("etat_par_client", etat_par_client[i] + "$");
            clients.add(client);
            client.clear();
        }

        employee.accumulate("clients", clients);
        try {
            FileUtils.writeStringToFile(new File(arg),
                    employee.toString(2), /*le 2 dans la methode toString est pour le formattage*/"UTF-8");
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}