import net.sf.json.JSONArray;
import java.io.*;

import net.sf.json.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.*;
import org.apache.commons.io.FileUtils;

public class GestionJson {
    public static String[][] lecture(String json)
    {
        int j;
        String [][] tableau = new String[10][1];

        JSONObject employee;
        
        employee = JSONObject.fromObject(json);

        tableau[0][0] = employee.getString("matricule_employe");
        tableau[1][0] = employee.getString("type_employe");
        tableau[2][0] = employee.getString("taux_horaire_min");
        tableau[3][0] = employee.getString("taux_horaire_max");
        JSONArray interventions = employee.getJSONArray("interventions");
        String [][] intervention = new String [5][interventions.size()];

        for(j=0; j<interventions.size() ; j++)
        {
            JSONObject Intervention = interventions.getJSONObject(j);

            try
            {
                intervention[0][j] = Intervention.getString("code_client");
            }

            catch(Exception e)
            {
                intervention[0][j] = Intervention.getString("code_projet");
            }
            intervention[1][j] = Intervention.getString("distance_deplacement");
            intervention[2][j] = Intervention.getString("overtime");
            intervention[3][j] = Intervention.getString("nombre_heures");
            intervention[4][j] = Intervention.optString("date_intervention",
                    Intervention.optString("date_affectation"));
        }

        tableau[4] = intervention[0];
        tableau[5] = intervention[1];
        tableau[6] = intervention[2];
        tableau[7] = intervention[3];
        tableau[8] = intervention[4];
        tableau[9][0] = String.valueOf(j);

        return tableau;
    }
    public static void ecriture(int matricule_employe, double etat_compte,
                                double cout_fixe,double cout_variable,String[] code
            ,int[] etat_par_client)
    {
        JSONObject employee = new JSONObject();

        employee.accumulate("matricule_employe",matricule_employe);
        employee.accumulate("etat_compte",etat_compte);
        employee.accumulate("cout_fixe",cout_fixe);
        employee.accumulate("cout_variable",cout_variable);

        JSONArray clients = new JSONArray();
        JSONObject client = new JSONObject();

        client.accumulate("code_client",code[0]);
        client.accumulate("etat_par_client",etat_par_client[0]);
        clients.add(client);
        client.clear();

        client.accumulate("code_client",code[1]);
        client.accumulate("etat_par_client",etat_par_client[1]);
        clients.add(client);
        client.clear();

        client.accumulate("code_client",code[2]);
        client.accumulate("etat_par_client",etat_par_client[2]);
        clients.add(client);
        client.clear();

        employee.accumulate("clients", clients);
        try {
            FileUtils.writeStringToFile(new File("Sortie"),
                    employee.toString(2), /*le 2 dans la methode toString est pour le formattage*/"UTF-8");
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}