package Tests;

import Source.Main;
import org.junit.Assert;
import org.junit.Test;

public class TestCalculerMontantRegulier {

    @Test
    public void testCalculerMontantRegulier() {
        // Test cas 1: typeEmploye = 0 (minimum hourly rate)
        int typeEmploye1 = 0;
        double nombreHeures1 = 40.0;
        double tauxHoraireMin1 = 10.0;
        double tauxHoraireMax1 = 20.0;
        double expectedMontantRegulier1 = 400.0;
        double actualMontantRegulier1 = Main.calculerMontantRegulier(typeEmploye1, nombreHeures1, tauxHoraireMin1, tauxHoraireMax1);
        Assert.assertEquals(expectedMontantRegulier1, actualMontantRegulier1, 0.0);

        // Test cas 2: typeEmploye = 1 (average hourly rate)
        int typeEmploye2 = 1;
        double nombreHeures2 = 35.0;
        double tauxHoraireMin2 = 10.0;
        double tauxHoraireMax2 = 20.0;
        double expectedMontantRegulier2 = 525.0;
        double actualMontantRegulier2 = Main.calculerMontantRegulier(typeEmploye2, nombreHeures2, tauxHoraireMin2, tauxHoraireMax2);
        Assert.assertEquals(expectedMontantRegulier2, actualMontantRegulier2, 0.0);

        // Test cas 3: typeEmploye = 2 (maximum hourly rate)
        int typeEmploye3 = 2;
        double nombreHeures3 = 45.0;
        double tauxHoraireMin3 = 10.0;
        double tauxHoraireMax3 = 20.0;
        double expectedMontantRegulier3 = 900.0;
        double actualMontantRegulier3 = Main.calculerMontantRegulier(typeEmploye3, nombreHeures3, tauxHoraireMin3, tauxHoraireMax3);
        Assert.assertEquals(expectedMontantRegulier3, actualMontantRegulier3, 0.0);
    }
}
