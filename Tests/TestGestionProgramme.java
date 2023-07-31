package Tests;

import Source.GestionProgramme;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class TestGestionProgramme {
    private String[][] donnees;

    @BeforeEach
    public void configurations() {

        // Mets en place les donnees de test pour chaque cas de test.
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

}
