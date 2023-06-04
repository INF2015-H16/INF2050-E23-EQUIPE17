package Tests;

import Source.Main;
import org.junit.Assert;
import org.junit.Test;

public class TestCalculerEtatCompteTotal {

    @Test
    public void testCalculerEtatCompteTotal() {
        // Test case 1
        double[] etatsParClient1 = { 100.0, 200.0, 300.0 };
        double expectedEtatCompteTotal1 = 600.0;
        double actualEtatCompteTotal1 = Main.calculerEtatCompteTotal(etatsParClient1);
        Assert.assertEquals(expectedEtatCompteTotal1, actualEtatCompteTotal1, 0.0);

        // Test case 2
        double[] etatsParClient2 = { 50.0, 75.0, 100.0, 125.0 };
        double expectedEtatCompteTotal2 = 350.0;
        double actualEtatCompteTotal2 = Main.calculerEtatCompteTotal(etatsParClient2);
        Assert.assertEquals(expectedEtatCompteTotal2, actualEtatCompteTotal2, 0.0);

        // Test case 3
        double[] etatsParClient3 = { 0.0 };
        double expectedEtatCompteTotal3 = 0.0;
        double actualEtatCompteTotal3 = Main.calculerEtatCompteTotal(etatsParClient3);
        Assert.assertEquals(expectedEtatCompteTotal3, actualEtatCompteTotal3, 0.0);
    }
}
