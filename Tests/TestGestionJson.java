package Tests;

import Source.GestionJson;
import Source.JsonException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
public class TestGestionJson {
    @Test
    public void testConvertirMajusculesEnMinuscules() {
        String entree = "Hello World";
        String sortieAttendue = "helloworld";
        String sortieActuelle = GestionJson.convertirMajusculesEnMinuscules(entree);
        Assertions.assertEquals(sortieAttendue, sortieActuelle);
    }

    @Test
    public void testLireFichierEntreeJson() {

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

            String matriculeEmployeAttendu = "123";
            String matriculeEmployeActuel = attributsJson[0][0];
            Assertions.assertEquals(matriculeEmployeAttendu, matriculeEmployeActuel);

            String typeEmployeAttendu = "TYPE_1";
            String typeEmployeActuel = attributsJson[1][0];
            Assertions.assertEquals(typeEmployeAttendu, typeEmployeActuel);

            String tauxHoraierMinAttendu = "20.0";
            String tauxHoraireMinActuel = attributsJson[2][0];
            Assertions.assertEquals(tauxHoraierMinAttendu, tauxHoraireMinActuel);

            String tauxHoraieMaxAttendu = "30.0";
            String tauxHoraireMaxActuel = attributsJson[3][0];
            Assertions.assertEquals(tauxHoraieMaxAttendu, tauxHoraireMaxActuel);

            String codeClient1Attendu = "C1001";
            String codeClient1Actuel = attributsJson[4][0];
            Assertions.assertEquals(codeClient1Attendu, codeClient1Actuel);

            String distanceDeplacement1Attendu = "50.00%";
            String distanceDeplacement1Actuel = attributsJson[4][1];
            Assertions.assertEquals(distanceDeplacement1Attendu, distanceDeplacement1Actuel);


        } catch (JsonException | IOException e) {
            Assertions.fail("Exception ne devrait pas etre lancee durant les tests: " + e.getMessage());
        }
    }
    @Test
    public void testEmployeeInfo() {

        int matriculeEmploye = 123;
        double etatCompte = 50000.00;
        double coutFixe = 1700.00;
        double coutVariable = 3200.00;
        JSONArray observations = new JSONArray();

        JSONObject employee = new JSONObject();
        employee = GestionJson.employeeInfo(matriculeEmploye, etatCompte, coutFixe, coutVariable, employee, observations);

        String matriculeEmployeAttendu = "123";
        String matriculeEmployeActuel = employee.getString("matricule_employe");
        Assertions.assertEquals(matriculeEmployeAttendu, matriculeEmployeActuel);

        String etatCompteAttendu = "50000.00$";
        String etatCompteActuel = employee.getString("etat_compte");
        Assertions.assertEquals(etatCompteAttendu, etatCompteActuel);

        String coutFixeAttendu = "1700.00$";
        String coutFixeActuel = employee.getString("cout_fixe");
        Assertions.assertEquals(coutFixeAttendu, coutFixeActuel);

        String coutVariableAttendu = "3200.00$";
        String coutVariableActuel = employee.getString("cout_variable");
        Assertions.assertEquals(coutVariableAttendu, coutVariableActuel);

        // Ensure that the observations are added correctly to the employee
        JSONArray observationsAttendues = new JSONArray();
        observationsAttendues.add("Le cout variable payable nécessite des ajustements");
        observationsAttendues.add("L’état de compte total ne doit pas dépasser 30000.00 $.");
        observationsAttendues.add("Le cout fixe payable ne doit pas dépasser 1500.00 $.");

        int tailleObservationsAttendues = observationsAttendues.size();
        int tailleObservationsActuelles = observations.size();
        Assertions.assertEquals(tailleObservationsAttendues, tailleObservationsActuelles, "Observations list size mismatch");

        for (int i = 0; i < tailleObservationsAttendues; i++) {
            Object elementAttendu = observationsAttendues.get(i);
            Object elementActuel = observations.get(i);
            Assertions.assertEquals(elementAttendu, elementActuel, "Observation mismatch at index " + i);
        }
    }
}
