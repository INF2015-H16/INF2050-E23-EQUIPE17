package Tests;
import Source.Main;
import org.junit.Assert;
import org.junit.Test;

public class TestCalculerCoutFixe {

    @Test
    public void testCalculerCoutFixe() {
        // Test cas 1: etatCompteTotal >= 1000.0
        double etatCompteTotal1 = 1500.0;
        double expectedCoutFixe1 = 18.0;
        double actualCoutFixe1 = Main.calculerCoutFixe(etatCompteTotal1);
        Assert.assertEquals(expectedCoutFixe1, actualCoutFixe1, 0.0);

        // Test cas 2: 500.0 <= etatCompteTotal < 1000.0
        double etatCompteTotal2 = 700.0;
        double expectedCoutFixe2 = 5.65;
        double actualCoutFixe2 = Main.calculerCoutFixe(etatCompteTotal2);
        Assert.assertEquals(expectedCoutFixe2, actualCoutFixe2, 0.0);

        // Test cas 3: etatCompteTotal < 500.0
        double etatCompteTotal3 = 300.0;
        double expectedCoutFixe3 = 1.2;
        double actualCoutFixe3 = Main.calculerCoutFixe(etatCompteTotal3);
        Assert.assertEquals(expectedCoutFixe3, actualCoutFixe3, 0.0);
    }
}
