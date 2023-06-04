package Tests;

import Source.Main;
import org.junit.Assert;
import org.junit.Test;

public class TestCalculerEtatParClient {

    @Test
    public void testCalculerEtatParClient() {
        // Test cas 1
        int typeEmploye1 = 0;
        double nombreHeures1 = 40.0;
        double tauxHoraireMin1 = 15.0;
        double tauxHoraireMax1 = 25.0;
        double distanceDeplacement1 = 10.0;
        double overtime1 = 5.0;
        double montantRegulier1 = Main.calculerMontantRegulier(typeEmploye1, nombreHeures1,
                tauxHoraireMin1, tauxHoraireMax1);
        double expectedMontantTotal1 = 500.0;
        double actualMontantTotal1 = Main.calculerEtatParClient(typeEmploye1, nombreHeures1,
                tauxHoraireMin1, tauxHoraireMax1, distanceDeplacement1, overtime1, montantRegulier1);
        Assert.assertEquals(expectedMontantTotal1, actualMontantTotal1, 0.0);

        // Test cas 2
        int typeEmploye2 = 1;
        double nombreHeures2 = 50.0;
        double tauxHoraireMin2 = 20.0;
        double tauxHoraireMax2 = 30.0;
        double distanceDeplacement2 = 5.0;
        double overtime2 = 10.0;
        double montantRegulier2 = Main.calculerMontantRegulier(typeEmploye2, nombreHeures2,
                tauxHoraireMin2, tauxHoraireMax2);
        double expectedMontantTotal2 = 2558.77;
        double actualMontantTotal2 = Main.calculerEtatParClient(typeEmploye2, nombreHeures2,
                tauxHoraireMin2, tauxHoraireMax2, distanceDeplacement2, overtime2, montantRegulier2);
        Assert.assertEquals(expectedMontantTotal2, actualMontantTotal2, 0.0);

        // Test cas 3
        int typeEmploye3 = 2;
        double nombreHeures3 = 35.0;
        double tauxHoraireMin3 = 18.0;
        double tauxHoraireMax3 = 28.0;
        double distanceDeplacement3 = 8.0;
        double overtime3 = 3.0;
        double montantRegulier3 = Main.calculerMontantRegulier(typeEmploye3, nombreHeures3,
                tauxHoraireMin3, tauxHoraireMax3);
        double expectedMontantTotal3 = 229.0;
        double actualMontantTotal3 = Main.calculerEtatParClient(typeEmploye3, nombreHeures3,
                tauxHoraireMin3, tauxHoraireMax3, distanceDeplacement3, overtime3, montantRegulier3);
        Assert.assertEquals(expectedMontantTotal3, actualMontantTotal3, 0.0);
    }
}
