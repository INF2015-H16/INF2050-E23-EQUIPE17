package Tests;

import Source.GestionJson;
import net.sf.json.test.JSONAssert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class TestEcriture {
    @Test
    public void testEcriture() throws IOException {
        int matricule_employe = 123456789;
        double etat_compte = 58004.20;
        double cout_fixe = 696.05;
        double cout_variable = 1450.10;
        String[] code = {"C123", "C456", "C789"};
        double[] etat_par_client = {1266.45, 9000.00, 47004.00};
        int j = 3;
        String arg = "test.json";
        int[] nbrs = new int[30];
        Arrays.fill(nbrs, -1);

        GestionJson.ecrireFichierSortieJson(matricule_employe, etat_compte, cout_fixe, cout_variable, code, etat_par_client, j,arg, nbrs);


        String expectedOutput = "{\n" +
                "    \"matricule_employe\":123456789,\n" +
                "    \"etat_compte\":\"58004.20 $\",\n" +
                "    \"cout_fixe\":\"696.05 $\",\n" +
                "    \"cout_variable\":\"1450.10 $\",\n" +
                "    \"clients\":[\n" +
                "    {\n" +
                "    \"code_client\":\"C123\",\n" +
                "    \"etat_par_client\":\"1266.45 $\"\n" +
                "    },\n" +
                "    {\n" +
                "    \"code_client\":\"C456\",\n" +
                "    \"etat_par_client\":\"9000.00 $\"\n" +
                "    },\n" +
                "    {\n" +
                "    \"code_client\":\"C789\",\n" +
                "    \"etat_par_client\":\"47004.00 $\"\n" +
                "    }\n" +
                "    ]\n" +
                "}\n";
        String content = null;
        try {
            content = TestEcriture.readFileToString("path/to/your/file.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONAssert.assertEquals(expectedOutput, content, true);
    }

    public static String readFileToString(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader("JsonEcriture"));
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        return content.toString();
    }
}
