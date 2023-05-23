import net.sf.json.JSONArray;
import java.io.*;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.*;

public class GestionJson {
    public static String[][] lecture(String json)
    {
        int j;
        String [][] tableau = new String[9][1];
        String matricule ,type ,horaire_min ,horaire_max;

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
            intervention[4][j] = Intervention.optString("date_intervention", Intervention.optString("date_affectation"));
        }

        tableau[4] = intervention[0];
        tableau[5] = intervention[1];
        tableau[6] = intervention[2];
        tableau[7] = intervention[3];
        tableau[8] = intervention[4];


        return tableau;
    }
    
}