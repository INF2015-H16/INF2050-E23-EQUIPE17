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

    public static void calculerHeureMaxPourIntervention(String entreeJson, JSONArray tableauInterventions,
                                                        String arg3) throws IOException {

        JSONObject employe = JSONObject.fromObject(entreeJson);
        tableauInterventions = employe.getJSONArray("interventions");
        int heureMax = 0;
        for (Object interventionObj : tableauInterventions) {
            JSONObject interventionJson = (JSONObject) interventionObj;
            int nombreHeures = interventionJson.getInt("nombre_heures");
            heureMax = Math.max(heureMax, nombreHeures);
        }
        GestionProgramme.ajouterMessage("Le nombre d'heures maximal pour une intervention est de" +" "+ heureMax
                ,arg3);

    }

    public static void calculerEtatParClientMax(String sortieJson, JSONArray clients,
                                                String arg3 ) throws IOException {

        JSONObject employe = JSONObject.fromObject(sortieJson);
        clients = employe.getJSONArray("clients");
        double etatParClientMax = 0.0;

        for (Object clientObj : clients) {
            JSONObject clientJson = (JSONObject) clientObj;
            double etatParClient = clientJson.getDouble("etat_par_client");
            etatParClientMax = Math.max(etatParClientMax, etatParClient);
        }

        GestionProgramme.ajouterMessage("L'etat par client maximal est de" +" "+ etatParClientMax
                ,arg3);

        
    }
}
