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
                {"C456", "1", "0", "1"},
                {"C789", "1", "1", "1"},
                {"C123", "9", "3", "3"},
                {"C789","3","1","2"}

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
        public void testAjouterMessage() throws IOException {
            String message = "This is a test message.";
            String arg = "output.txt";

            // Call the method and assert that the file is created without any exceptions
            assertDoesNotThrow(() -> GestionProgramme.ajouterMessage(message, arg));
            // Add more test cases as needed
        }
}
