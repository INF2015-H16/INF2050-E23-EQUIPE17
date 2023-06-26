package Tests;

import Source.GestionJson;
import org.junit.Assert;
import org.junit.Test;

public class TestLecture {

    @Test
    public void testLecture() {
        String json = "{\"matricule_employe\": 123456789, \"type_employe\": 2, \"taux_horaire_min\": \"35.45 $\", \"taux_horaire_max\": \"72.00 $\", \"interventions\": [{\"code_client\": \"C123\", \"distance_deplacement\": 4, \"overtime\": 0, \"nombre_heures\": 4, \"date_intervention\": \"2023-04-14\"}, {\"code_client\": \"C456\", \"distance_deplacement\": 2, \"overtime\": 1, \"nombre_heures\": 8, \"date_intervention\": \"2023-03-15\"}, {\"code_client\": \"C789\", \"distance_deplacement\": 3, \"overtime\": 3, \"nombre_heures\": 7, \"date_intervention\": \"2023-03-20\"}, {\"code_client\": \"C123\", \"distance_deplacement\": 8, \"overtime\": 3, \"nombre_heures\": 3, \"date_intervention\": \"2023-04-20\"}]}";

        String[][] expectedTableau = new String[9][5];
        expectedTableau[0][0] = "123456789";
        expectedTableau[1][0] = "2";
        expectedTableau[2][0] = "35.45 $";
        expectedTableau[3][0] = "72.00 $";
        expectedTableau[4][0] = "C123";
        expectedTableau[4][1] = "4";
        expectedTableau[4][2] = "0";
        expectedTableau[4][3] = "4";
        expectedTableau[4][4] = "2023-04-14";
        expectedTableau[5][0] = "C456";
        expectedTableau[5][1] = "2";
        expectedTableau[5][2] = "1";
        expectedTableau[5][3] = "8";
        expectedTableau[5][4] = "2023-03-15";
        expectedTableau[6][0] = "C789";
        expectedTableau[6][1] = "3";
        expectedTableau[6][2] = "3";
        expectedTableau[6][3] = "7";
        expectedTableau[6][4] = "2023-03-20";
        expectedTableau[7][0] = "C123";
        expectedTableau[7][1] = "8";
        expectedTableau[7][2] = "3";
        expectedTableau[7][3] = "3";
        expectedTableau[7][4] = "2023-04-20";
        expectedTableau[8][0] = "4";

        String[][] result = GestionJson.lireFichierEntreeJson(json);

        // Assert that the two-dimensional arrays are equal
        Assert.assertArrayEquals(expectedTableau, result);
    }


}
