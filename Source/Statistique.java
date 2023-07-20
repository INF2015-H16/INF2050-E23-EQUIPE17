package Source;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;

public class Statistique {
    final static int TYPE_EMPLOYE_0 = 0;
    final static int TYPE_EMPLOYE_1 = 1;
    final static int TYPE_EMPLOYE_2 = 2;
    final static int ETAT_PAR_CLIENT_1000 = 1000;
    final static int ETAT_PAR_CLIENT_10000 = 10000;

    public static int calculeHeureMaxIntervention(String jsonObject, JSONArray tableauInterventions,
                                                  String arg3) throws IOException {

        JSONObject employe = JSONObject.fromObject(jsonObject);
        tableauInterventions = employe.getJSONArray("interventions");
        int heureMax = 0;
        for (Object interventionObj : tableauInterventions) {
            JSONObject interventionJson = (JSONObject) interventionObj;
            int nombreHeures = interventionJson.getInt("nombre_heures");
            heureMax = Math.max(heureMax, nombreHeures);
        }
        GestionProgramme.ajouterMessage("Le nombre d'heures maximal pour une intervention est de" +" "+ heureMax
                ,arg3);

        return heureMax;
    }
}
