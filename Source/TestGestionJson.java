package Source;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Locale;
public class TestGestionJson {
    @Test
    public void testConvertirMajusculesEnMinuscules() {
        String input = "Hello World";
        String expectedOutput = "helloworld";
        String actualOutput = GestionJson.convertirMajusculesEnMinuscules(input);
        Assertions.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testLireFichierEntreeJson() {
        // Sample JSON data for testing
        String json = "{\"matricule_employe\": 123," +
                "\"type_employe\": \"TYPE_1\"," +
                "\"taux_horaire_min\": 20.00," +
                "\"taux_horaire_max\": 30.00," +
                "\"interventions\": [" +
                "{\"code_client\": \"C1001\", \"distance_deplacement\": \"50.00%\", \"overtime\": \"1.25%\", \"nombre_heures\": \"3\", \"date_intervention\": \"2023-07-01\"}," +
                "{\"code_client\": \"C1002\", \"distance_deplacement\": \"25.00%\", \"overtime\": \"1.10%\", \"nombre_heures\": \"5\", \"date_intervention\": \"2023-07-10\"}" +
                "]}";

        JSONArray observations = new JSONArray();
        JSONArray interventions = new JSONArray();

        try {
            String[][] attributsJson = GestionJson.lireFichierEntreeJson(json, "chemin_json", observations, interventions);

            String expectedMatriculeEmploye = "123";
            String actualMatriculeEmploye = attributsJson[0][0];
            Assertions.assertEquals(expectedMatriculeEmploye, actualMatriculeEmploye);

            String expectedTypeEmploye = "TYPE_1";
            String actualTypeEmploye = attributsJson[1][0];
            Assertions.assertEquals(expectedTypeEmploye, actualTypeEmploye);

            String expectedTauxHoraireMin = "20.0";
            String actualTauxHoraireMin = attributsJson[2][0];
            Assertions.assertEquals(expectedTauxHoraireMin, actualTauxHoraireMin);

            String expectedTauxHoraireMax = "30.0";
            String actualTauxHoraireMax = attributsJson[3][0];
            Assertions.assertEquals(expectedTauxHoraireMax, actualTauxHoraireMax);

            String expectedCodeClient1 = "C1001";
            String actualCodeClient1 = attributsJson[4][0];
            Assertions.assertEquals(expectedCodeClient1, actualCodeClient1);

            String expectedDistanceDeplacement1 = "50.00%";
            String actualDistanceDeplacement1 = attributsJson[4][1];
            Assertions.assertEquals(expectedDistanceDeplacement1, actualDistanceDeplacement1);

            // Add more assertions for other attributes and interventions as needed

        } catch (JsonException | IOException e) {
            Assertions.fail("Exception should not be thrown during test: " + e.getMessage());
        }
    }
    @Test
    public void testEmployeeInfo() {
        // Sample data for testing
        int matriculeEmploye = 123;
        double etatCompte = 50000.00;
        double coutFixe = 1200.00;
        double coutVariable = 3200.00;
        JSONArray observations = new JSONArray();

        JSONObject employee = new JSONObject();
        employee = GestionJson.employeeInfo(matriculeEmploye, etatCompte, coutFixe, coutVariable, employee, observations);

        String expectedMatriculeEmploye = "123";
        String actualMatriculeEmploye = employee.getString("matricule_employe");
        Assertions.assertEquals(expectedMatriculeEmploye, actualMatriculeEmploye);

        String expectedEtatCompte = "50000.00$";
        String actualEtatCompte = employee.getString("etat_compte");
        Assertions.assertEquals(expectedEtatCompte, actualEtatCompte);

        String expectedCoutFixe = "1200.00$";
        String actualCoutFixe = employee.getString("cout_fixe");
        Assertions.assertEquals(expectedCoutFixe, actualCoutFixe);

        String expectedCoutVariable = "3200.00$";
        String actualCoutVariable = employee.getString("cout_variable");
        Assertions.assertEquals(expectedCoutVariable, actualCoutVariable);

        // Ensure that the observations are added correctly to the employee
        JSONArray expectedObservations = new JSONArray();
        expectedObservations.add("Le cout variable payable nécessite des ajustements");
        expectedObservations.add("L’état de compte total ne doit pas dépasser 30000.00 $.");
        expectedObservations.add("Le cout fixe payable ne doit pas dépasser 1500.00 $.");
        Assertions.assertEquals(expectedObservations, observations);
    }

    @Test
    public void testPreparationJson() {
        // Sample data for testing
        String[] code = {"C1001", "C1002", null, "C1003"};
        double[] etatParClient = {15000.00, 18000.00, 12000.00, 20000.00};
        int j = 4;
        int[] nbrs = {1, 1, 0, 1};
        JSONArray clients = new JSONArray();
        JSONObject client = new JSONObject();
        JSONObject employee = new JSONObject();
        JSONArray observations = new JSONArray();

        JSONArray actualClients = GestionJson.preparationJson(code, etatParClient, j, nbrs, clients, client, employee, observations);

        // Ensure that only non-null code clients are added to the clients array
        JSONArray expectedClients = new JSONArray();
        JSONObject expectedClient1 = new JSONObject();
        expectedClient1.accumulate("code_client", "C1001");
        expectedClient1.accumulate("etat_par_client", "15000.00$");
        JSONObject expectedClient2 = new JSONObject();
        expectedClient2.accumulate("code_client", "C1002");
        expectedClient2.accumulate("etat_par_client", "18000.00$");
        JSONObject expectedClient3 = new JSONObject();
        expectedClient3.accumulate("code_client", "C1003");
        expectedClient3.accumulate("etat_par_client", "20000.00$");
        expectedClients.add(expectedClient1);
        expectedClients.add(expectedClient2);
        expectedClients.add(expectedClient3);

        Assertions.assertEquals(expectedClients, actualClients);

        // Ensure that the observations are added correctly to the employee
        JSONArray expectedObservations = new JSONArray();
        expectedObservations.add("L’état par client du client C1002 est trop dispendieuse.");
        Assertions.assertEquals(expectedObservations, observations);
    }
}
