package Tests;

import Source.Main;
import org.junit.Assert;
import org.junit.Test;

public class TestCalculerCoutVariable {

    @Test
    public void testCalculerCoutVariable() {
        // Test case 1
        double etatCompteTotal1 = 1000.0;
        double expectedCoutVariable1 = 25.0;
        double actualCoutVariable1 = Main.calculerCoutVariable(etatCompteTotal1);
        Assert.assertEquals(expectedCoutVariable1, actualCoutVariable1, 0.0);

        // Test case 2
        double etatCompteTotal2 = 500.0;
        double expectedCoutVariable2 = 12.5;
        double actualCoutVariable2 = Main.calculerCoutVariable(etatCompteTotal2);
        Assert.assertEquals(expectedCoutVariable2, actualCoutVariable2, 0.0);

        // Test case 3
        double etatCompteTotal3 = 1500.0;
        double expectedCoutVariable3 = 37.5;
        double actualCoutVariable3 = Main.calculerCoutVariable(etatCompteTotal3);
        Assert.assertEquals(expectedCoutVariable3, actualCoutVariable3, 0.0);
    }
}
