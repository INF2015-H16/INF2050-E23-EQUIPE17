package Tests;

import Source.FileReaders;
import Source.GestionJson;
import Source.Validations;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

public class TestValiderComboCodeClientDateIntervention {

    @Test

    public void testValiderComboCodeCLientDateIntervention_InterventionsValides() throws IOException {

        int matricule_Employe = 123456789;
        double etat_compte = 58004.20;
        double cout_fixe = 696.05;
        double cout_variable = 1450.10;
        String[] code = {"C123", "C456", "C789"};
        double[] etat_par_client = {1266.45, 9000.00, 47004.00};
        int j = 3;
        String arg = "sortie.json";
        int[] nbrs = new int[30];
        Arrays.fill(nbrs, -1);

        String myJson = FileReaders.loadFileIntoString("C:\\Users\\steve\\IdeaProjects\\INF2050-E23-EQUIPE17" +
                        "\\test.json",
                "UTF-8");

        JSONObject contenuJson = JSONObject.fromObject(myJson);
        int matricule_employe = contenuJson.getInt("matricule_employe");
        int type_employe = contenuJson.getInt("type_employe");
        String taux_horaire_min = contenuJson.getString("taux_horaire_min");
        String taux_horaire_max = contenuJson.getString("taux_horaire_max");
        JSONArray listeInterventions = contenuJson.getJSONArray("interventions");
        JSONObject intervention;
        for (int i = 0; i < listeInterventions.size(); i++) {
            String codeClient;
            int distDeplacement;
            int overtime;
            int nbrHeures;
            String dateIntervention;

            intervention = listeInterventions.getJSONObject(i);
            codeClient = intervention.getString("code_client");
            distDeplacement = intervention.getInt("distance_deplacement");
            overtime = intervention.getInt("overtime");
            nbrHeures = intervention.getInt("nombre_heures");
            dateIntervention = intervention.getString("date_intervention");

            System.out.println("\t" + codeClient + "," + distDeplacement + "," + overtime + "," + nbrHeures + "," +
                    dateIntervention);

            boolean interventionsValides = Validations.validerComboCodeClientDateIntervention(contenuJson,
                    "C:\\Users\\steve\\IdeaProjects\\INF2050-E23-EQUIPE17\\sortie.json");
            if (interventionsValides) {
                GestionJson.ecrireFichierSortieJson(matricule_employe, etat_compte, cout_fixe, cout_variable, code,
                        etat_par_client, j, arg, nbrs);
            }
        }

        Assert.assertTrue(interventionsValides);
    }

    @Test

    public void testValiderComboCodeCLientDateIntervention_InterventionsNonValides() throws IOException {

        int matricule_Employe = 123456789;
        double etat_compte = 58004.20;
        double cout_fixe = 696.05;
        double cout_variable = 1450.10;
        String[] code = {"C123", "C456", "C789"};
        double[] etat_par_client = {1266.45, 9000.00, 47004.00};
        int j = 3;
        String arg = "sortie2.json";
        int[] nbrs = new int[30];
        Arrays.fill(nbrs, -1);

        String myJson = FileReaders.loadFileIntoString("C:\\Users\\steve\\IdeaProjects\\INF2050-E23-EQUIPE17" +
                        "\\test2.json",
                "UTF-8");

        JSONObject contenuJson = JSONObject.fromObject(myJson);
        int matricule_employe = contenuJson.getInt("matricule_employe");
        int type_employe = contenuJson.getInt("type_employe");
        String taux_horaire_min = contenuJson.getString("taux_horaire_min");
        String taux_horaire_max = contenuJson.getString("taux_horaire_max");
        JSONArray listeInterventions = contenuJson.getJSONArray("interventions");
        JSONObject intervention;
        for (int i = 0; i < listeInterventions.size(); i++) {
            String codeClient;
            int distDeplacement;
            int overtime;
            int nbrHeures;
            String dateIntervention;

            intervention = listeInterventions.getJSONObject(i);
            codeClient = intervention.getString("code_client");
            distDeplacement = intervention.getInt("distance_deplacement");
            overtime = intervention.getInt("overtime");
            nbrHeures = intervention.getInt("nombre_heures");
            dateIntervention = intervention.getString("date_intervention");

            System.out.println("\t" + codeClient + "," + distDeplacement + "," + overtime + "," + nbrHeures + "," +
                    dateIntervention);

            boolean interventionsValides = Validations.validerComboCodeClientDateIntervention(contenuJson,
                    "C:\\Users\\steve\\IdeaProjects\\INF2050-E23-EQUIPE17\\sortie2.json");
            if (interventionsValides) {
                GestionJson.ecrireFichierSortieJson(matricule_employe, etat_compte, cout_fixe, cout_variable, code,
                        etat_par_client, j, arg, nbrs);
            }

            Assert.assertFalse(interventionsValides);
        }
    }
}

