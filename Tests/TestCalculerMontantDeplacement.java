package Tests;

import Source.Main;
import org.junit.Assert;
import org.junit.Test;

public class TestCalculerMontantDeplacement {

    @Test
    public void testCalculerMontantDeplacement() {
        // Test cas 1: typeEmploye = 0
        int typeEmploye1 = 0;
        double distanceDeplacement1 = 10.0;
        double montantRegulier1 = 500.0;
        double expectedMontantDeplacement1 = -50;
        double actualMontantDeplacement1 = Main.calculerMontantDeplacement(typeEmploye1, distanceDeplacement1, montantRegulier1);
        Assert.assertEquals(expectedMontantDeplacement1, actualMontantDeplacement1, 0.0);

        // Test cas 2: typeEmploye = 1
        int typeEmploye2 = 1;
        double distanceDeplacement2 = 5.0;
        double montantRegulier2 = 700.0;
        double expectedMontantDeplacement2 = -150.0;
        double actualMontantDeplacement2 = Main.calculerMontantDeplacement(typeEmploye2, distanceDeplacement2, montantRegulier2);
        Assert.assertEquals(expectedMontantDeplacement2, actualMontantDeplacement2, 0.0);

        // Test cas 3: typeEmploye = 2
        int typeEmploye3 = 2;
        double distanceDeplacement3 = 8.0;
        double montantRegulier3 = 1000.0;
        double expectedMontantDeplacement3 = -1000.0;
        double actualMontantDeplacement3 = Main.calculerMontantDeplacement(typeEmploye3, distanceDeplacement3, montantRegulier3);
        Assert.assertEquals(expectedMontantDeplacement3, actualMontantDeplacement3, 0.0);
    }
}
