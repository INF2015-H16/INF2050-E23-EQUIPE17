import net.sf.json.JSONArray;
import java.io.*;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.*;

public class GestionJson {
    public static int lecture(String json)
    {
        int j;

        int matricule = 0, type, distance = 0, overtime = 0,hours = 0;
        String horaire_min, horaire_max;
        String code = null, date;

        JSONObject employee;
        
        employee = JSONObject.fromObject(json);

        matricule = employee.getInt("matricule_employe");
        type = employee.getInt("type_employe");
        horaire_min = employee.getString("taux_horaire_min");
        horaire_max = employee.getString("taux_horaire_max");
        JSONArray interventions = employee.getJSONArray("interventions");
        for(j=0; j<interventions.size() ; j++)
        {
            JSONObject intervention = interventions.getJSONObject(j);

            try
            {
                code = intervention.getString("code_client");
            }

            catch(Exception e)
            {
                code = intervention.getString("code_projet");
            }
            distance = intervention.getInt("distance_deplacement");
            overtime = intervention.getInt("overtime");
            hours = intervention.getInt("nombre_heures");
            date = intervention.optString("date_intervention", intervention.optString("date_affectation"));
        }


        System.out.println(matricule + " " + type + " ");
        for(int i=0; i< interventions.size() ;i++)
        {
            System.out.println(distance +" " +overtime +" "+ hours +" "+ code);
        }

        return j;
    }
}

