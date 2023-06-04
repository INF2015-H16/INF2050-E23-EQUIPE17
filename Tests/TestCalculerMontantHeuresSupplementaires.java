package Tests;

import Source.Main;
import org.junit.Assert;
import org.junit.Test;

public class TestCalculerMontantHeuresSupplementaires {

    @Test
    public void testCalculerMontantHeuresSupplementaires() {
        // Test cas 1: typeEmploye = 0 (Superviseur)
        int typeEmploye1 = 0;
        double overtime1 = 6.0;
        double expectedMontantHeuresSupplementaires1 = 0.0;
        double actualMontantHeuresSupplementaires1 = Main.calculerMontantHeuresSupplementaires(typeEmploye1, overtime1);
        Assert.assertEquals(expectedMontantHeuresSupplementaires1, actualMontantHeuresSupplementaires1, 0.0);

        // Test cas 2: typeEmploye = 1 (Permanent)
        int typeEmploye2 = 1;
        double overtime2 = 5.0;
        double expectedMontantHeuresSupplementaires2 = 250.0;
        double actualMontantHeuresSupplementaires2 = Main.calculerMontantHeuresSupplementaires(typeEmploye2, overtime2);
        Assert.assertEquals(expectedMontantHeuresSupplementaires2, actualMontantHeuresSupplementaires2, 0.0);

        // Test cas 3: typeEmploye = 2 (Contractuel)
        int typeEmploye3 = 2;
        double overtime3 = 10.0;
        double expectedMontantHeuresSupplementaires3 = 1500.0;
        double actualMontantHeuresSupplementaires3 = Main.calculerMontantHeuresSupplementaires(typeEmploye3, overtime3);
        Assert.assertEquals(expectedMontantHeuresSupplementaires3, actualMontantHeuresSupplementaires3, 0.0);
    }
}
