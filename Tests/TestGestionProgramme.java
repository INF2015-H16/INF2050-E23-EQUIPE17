package Tests;

import Source.GestionProgramme;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.sf.json.JSONArray;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
public class TestGestionProgramme {
    private String[][] donnees;

    @BeforeEach
    public void setUp() {
        // Set up the test data for each test case
        donnees = new String[][]{
                {"123", "A", "10.00", "20.00"},
                {"456", "B", "15.00", "25.00"},
                {"#", "", "-200", "0"},
                {"789", "C", "12.00", "22.00"},
                {"#", "", "10", "20"},
                {"321", "A", "8.00", "18.00"},
                {"654", "B", "13.00", "23.00"},
                {"#", "", "15", "25"},
                {"987", "C", "14.00", "24.00"},
                {"#", "", "5", "15"},
                {"246", "A", "9.00", "19.00"},
                {"579", "B", "20.00", "30.00"},
                {"#", "", "-200", "0"},
                {"000", "C", "11.00", "21.00"},
                {"#", "", "-200", "0"},
                {"135", "A", "7.00", "17.00"},
                {"468", "B", "16.00", "26.00"},
                {"#", "", "12", "22"},
                {"975", "C", "13.00", "23.00"},
                {"#", "", "-200", "0"},
                {"791", "A", "11.00", "21.00"},
                {"132", "B", "14.00", "24.00"},
                {"#", "", "-200", "0"},
                {"657", "C", "18.00", "28.00"},
                {"#", "", "-200", "0"},
                {"952", "A", "10.00", "20.00"},
                {"713", "B", "19.00", "29.00"},
                {"#", "", "-200", "0"},
                {"314", "C", "9.00", "19.00"},
                {"#", "", "-200", "0"},
                {"924", "A", "8.00", "18.00"},
                {"168", "B", "12.00", "22.00"},
                {"#", "", "-200", "0"},
                {"547", "C", "17.00", "27.00"},
                {"#", "", "-200", "0"},
                {"348", "A", "6.00", "16.00"},
                {"768", "B", "11.00", "21.00"},
                {"#", "", "-200", "0"},
                {"925", "C", "15.00", "25.00"},
                {"#", "", "30", "40"},
                {"239", "A", "8.00", "18.00"},
                {"641", "B", "14.00", "24.00"},
                {"#", "", "10", "20"},
                {"826", "C", "13.00", "23.00"},
                {"#", "", "15", "25"},
                {"493", "A", "7.00", "17.00"},
                {"653", "B", "16.00", "26.00"},
                {"#", "", "-200", "0"},
                {"321", "C", "14.00", "24.00"},
                {"#", "", "-200", "0"},
                {"846", "A", "11.00", "21.00"},
                {"457", "B", "19.00", "29.00"},
                {"#", "", "-200", "0"},
                {"985", "C", "15.00", "25.00"},
                {"#", "", "-200", "0"},
                {"357", "A", "9.00", "19.00"},
                {"823", "B", "12.00", "22.00"},
                {"#", "", "-200", "0"},
                {"648", "C", "17.00", "27.00"},
                {"#", "", "-200", "0"},
                {"245", "A", "10.00", "20.00"},
                {"236", "B", "13.00", "23.00"},
                {"#", "", "-200", "0"},
                {"123", "C", "16.00", "26.00"},
                {"#", "", "-200", "0"},
                {"934", "A", "8.00", "18.00"},
                {"346", "B", "15.00", "25.00"},
                {"#", "", "10", "20"},
                {"654", "C", "14.00", "24.00"},
                {"#", "", "15", "25"},
                {"347", "A", "7.00", "17.00"},
                {"348", "B", "12.00", "22.00"},
                {"#", "", "-200", "0"},
                {"943", "C", "13.00", "23.00"},
                {"#", "", "-200", "0"},
                {"928", "A", "11.00", "21.00"},
                {"623", "B", "19.00", "29.00"},
                {"#", "", "-200", "0"},
                {"237", "C", "15.00", "25.00"},
                {"#", "", "-200", "0"},
                {"347", "A", "9.00", "19.00"},
                {"523", "B", "12.00", "22.00"},
                {"#", "", "-200", "0"},
                {"123", "C", "17.00", "27.00"},
                {"#", "", "-200", "0"},
                {"946", "A", "8.00", "18.00"},
                {"946", "B", "15.00", "25.00"},
                {"#", "", "10", "20"},
                {"863", "C", "14.00", "24.00"},
                {"#", "", "15", "25"},
                {"467", "A", "7.00", "17.00"},
                {"135", "B", "16.00", "26.00"},
                {"#", "", "-200", "0"},
                {"897", "C", "13.00", "23.00"},
                {"#", "", "-200", "0"},
                {"732", "A", "11.00", "21.00"},
                {"784", "B", "19.00", "29.00"},
        };
    }
        @Test
        public void testVerificationCodeClient() {
            int[] nbrs = {123, 456, 789};
            int codeClient1 = 999;
            int codeClient2 = 123;

            Assertions.assertTrue(GestionProgramme.verificationCodeClient(nbrs, codeClient1));
            assertFalse(GestionProgramme.verificationCodeClient(nbrs, codeClient2));
        }

        @Test
        public void testExecuterRecuperationInterventions() {
            String argument2 = "output.json";
            String json = "data.json";
            JSONArray observations = new JSONArray();
            String option = "option1";
            JSONArray interventions = new JSONArray();

            // Call the method and assert the expected output
            assertDoesNotThrow(() -> GestionProgramme.executerRecuperationInterventions(donnees, argument2, json, observations, option, interventions));
        }

        @Test
        public void testAjouterMessage() throws IOException {
            String message = "This is a test message.";
            String arg = "output.txt";

            // Call the method and assert that the file is created without any exceptions
            assertDoesNotThrow(() -> GestionProgramme.ajouterMessage(message, arg));
            // Add more test cases as needed
        }
}
