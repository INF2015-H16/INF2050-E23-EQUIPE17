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
    public static void ecriture(int matricule_employe, double etat_compte, double cout_fixe, double cout_variable,
                                String[] code, double[] etat_par_client, int j, String arg, int[] nbrs) {
        JSONObject employee = new JSONObject();

        employee.accumulate("matricule_employe", matricule_employe);
        employee.accumulate("etat_compte", etat_compte + "$");
        employee.accumulate("cout_fixe", cout_fixe + "$");
        employee.accumulate("cout_variable", cout_variable + "$");

        JSONArray clients = new JSONArray();
        JSONObject client = new JSONObject();

        // Ajout des données de chaque client
        for(int i=0; i<j ; i++) {
            if(Main.verification(nbrs,i))
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


}