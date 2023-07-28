package Source;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestCalculEmploye {

    @Test
    public void testCalculerEtatCompteTotal() {
        double[] etatsParClient = {100.0, 200.0, 300.0};
        double expectedEtatCompteTotal = 733.77 + 100.0 + 200.0 + 300.0;
        double actualEtatCompteTotal = CalculEmploye.calculerEtatCompteTotal(etatsParClient);
        Assertions.assertEquals(expectedEtatCompteTotal, actualEtatCompteTotal, 0.001);
    }

    @Test
    public void testCalculerCoutVariable() {
        double etatCompteTotal = 1000.0;
        double expectedCoutVariable = (2.5 / 100) * etatCompteTotal;
        double actualCoutVariable = CalculEmploye.calculerCoutVariable(etatCompteTotal);
        Assertions.assertEquals(expectedCoutVariable, actualCoutVariable, 0.001);
    }

    @Test
    public void testCalculerCoutFixe() {
        double etatCompteTotal = 2000.0;
        double expectedCoutFixe = etatCompteTotal * (1.2 / 100);
        double actualCoutFixe = CalculEmploye.calculerCoutFixe(etatCompteTotal);
        Assertions.assertEquals(expectedCoutFixe, actualCoutFixe, 0.001);
    }

    @Test
    public void testCalculerEtatParClient_Type0() throws JsonException {
        int typeEmploye = 0;
        double nombreHeures = 40.0;
        double tauxHoraireMin = 10.0;
        double tauxHoraireMax = 15.0;
        double distanceDeplacement = 50.0;
        double overtime = 0.0;

        double expectedEtatParClient = 400.0; // 40 * 10 (tauxHoraireMin)
        double actualEtatParClient = CalculEmploye.calculerEtatParClient(typeEmploye, nombreHeures, tauxHoraireMin, tauxHoraireMax, distanceDeplacement, overtime);
        Assertions.assertEquals(expectedEtatParClient, actualEtatParClient, 0.001);
    }

    @Test
    public void testCalculerEtatParClient_Type1() throws JsonException {
        int typeEmploye = 1;
        double nombreHeures = 50.0;
        double tauxHoraireMin = 10.0;
        double tauxHoraireMax = 15.0;
        double distanceDeplacement = 70.0;
        double overtime = 6.0;

        // Expected Etat par client = montantRegulier + montantDeplacement + montantHeuresSupp
        double expectedEtatParClient = 50 * ((tauxHoraireMin + tauxHoraireMax) / 2) + (200 - (70 * (0.10 * (50 * ((tauxHoraireMin + tauxHoraireMax) / 2))))) + (50 * 6 * 100.0);
        double actualEtatParClient = CalculEmploye.calculerEtatParClient(typeEmploye, nombreHeures, tauxHoraireMin, tauxHoraireMax, distanceDeplacement, overtime);
        Assertions.assertEquals(expectedEtatParClient, actualEtatParClient, 0.001);
    }

    @Test
    public void testCalculerMontantRegulier_Type2() {
        int typeEmploye = 2;
        double nombreHeures = 35.0;
        double tauxHoraireMin = 20.0;
        double tauxHoraireMax = 30.0;

        // Expected montantRegulier = tauxHoraireMax * nombreHeures
        double expectedMontantRegulier = tauxHoraireMax * nombreHeures;
        double actualMontantRegulier = CalculEmploye.calculerMontantRegulier(typeEmploye, nombreHeures, tauxHoraireMin, tauxHoraireMax);
        Assertions.assertEquals(expectedMontantRegulier, actualMontantRegulier, 0.001);
    }

    @Test
    public void testCalculerMontantDeplacement_Type1() throws JsonException {
        int typeEmploye = 1;
        double distanceDeplacement = 80.0;
        double montantRegulier = 1000.0;

        // Expected montantDeplacement = 200 - (distanceDeplacement * (0.10 * montantRegulier))
        double expectedMontantDeplacement = 200 - (distanceDeplacement * (0.10 * montantRegulier));
        double actualMontantDeplacement = CalculEmploye.calculerMontantDeplacement(typeEmploye, distanceDeplacement, montantRegulier);
        Assertions.assertEquals(expectedMontantDeplacement, actualMontantDeplacement, 0.001);
    }

    @Test
    public void testCalculerMontantHeuresSupplementairesType1() {
        double overtime = 5.0;

        // Expected montantHeuresSupplementaires = 50.0 * overtime
        double expectedMontantHeuresSupplementaires = 50.0 * overtime;
        double actualMontantHeuresSupplementaires = CalculEmploye.calculerMontantHeuresSupplementairesType1(overtime);
        Assertions.assertEquals(expectedMontantHeuresSupplementaires, actualMontantHeuresSupplementaires, 0.001);
    }

    @Test
    public void testCalculerMontantHeuresSupplementairesType2() {
        double overtime = 3.5;

        // Expected montantHeuresSupplementaires = 75.0 * overtime
        double expectedMontantHeuresSupplementaires = 75.0 * overtime;
        double actualMontantHeuresSupplementaires = CalculEmploye.calculerMontantHeuresSupplementairesType2(overtime);
        Assertions.assertEquals(expectedMontantHeuresSupplementaires, actualMontantHeuresSupplementaires, 0.001);
    }

    @Test
    public void testArrondirMontant() {
        double montant = 456.78;

        // Expected arrondi = 456.80 (rounded to the nearest 0.05)
        double expectedArrondi = 456.80;
        double actualArrondi = CalculEmploye.arrondirMontant(montant);
        Assertions.assertEquals(expectedArrondi, actualArrondi, 0.001);
    }

}
