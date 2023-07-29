package Tests;

import Source.Main;
import org.junit.Assert;
import org.junit.Test;

public class TestCalculerMontantHeuresSupplementaires {

    @Test
    public void testCalculerMontantHeuresSupplementaires() {
        // Test case 1 - Supervisor (typeEmploye = 0)
        double overtime1 = 5.0;
        double expectedMontantHeuresSupplementaires1 = 0.0;
        double actualMontantHeuresSupplementaires1 = Main.calculerMontantHeuresSupplementaires(0, overtime1, 0);
        Assert.assertEquals(expectedMontantHeuresSupplementaires1, actualMontantHeuresSupplementaires1, 0.0);

        // Test case 2 - Permanent (typeEmploye = 1)
        double overtime2 = 6.0;
        double nombreHeures2 = 10.0;
        double expectedMontantHeuresSupplementaires2 = 600.0;
        double actualMontantHeuresSupplementaires2 = Main.calculerMontantHeuresSupplementaires(1, overtime2, nombreHeures2);
        Assert.assertEquals(expectedMontantHeuresSupplementaires2, actualMontantHeuresSupplementaires2, 0.0);

        // Test case 3 - Contractual (typeEmploye = 2)
        double overtime3 = 3.0;
        double nombreHeures3 = 5.0;
        double expectedMontantHeuresSupplementaires3 = 225.0;
        double actualMontantHeuresSupplementaires3 = Main.calculerMontantHeuresSupplementaires(2, overtime3, nombreHeures3);
        Assert.assertEquals(expectedMontantHeuresSupplementaires3, actualMontantHeuresSupplementaires3, 0.0);
    }
}
