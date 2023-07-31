package Tests;

import Source.CalculEmploye;
import Source.JsonException;

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

        int typeEmploye =0;
        double nombreHeures = 50.0;
        double tauxHoraireMin = 10.0;
        double tauxHoraireMax = 15.0;
        double distanceDeplacement = 70.0;
        double overtime = 6.0;
        double montantRegulier,montantDeplacement,montantHeuresSupp;

        montantRegulier = 50 * ((tauxHoraireMin + tauxHoraireMax) / 2);
        montantDeplacement = (200 - (70 * (0.10 * montantRegulier)));;
        montantHeuresSupp = (nombreHeures > 40) ? (nombreHeures - 40) * 100.0 * overtime : 0;
        // Expected Etat par client = montantRegulier + montantDeplacement + montantHeuresSupp
        double expectedEtatParClient =  montantRegulier + montantDeplacement + montantHeuresSupp;
        double actualEtatParClient = CalculEmploye.calculerEtatParClient(typeEmploye, nombreHeures, tauxHoraireMin, tauxHoraireMax, distanceDeplacement, overtime);
        System.out.print(actualEtatParClient);
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
        double montantRegulier,montantDeplacement,montantHeuresSupp;

        montantRegulier = 50 * ((tauxHoraireMin + tauxHoraireMax) / 2);
        montantDeplacement = (200 - (70 * (0.10 * montantRegulier)));;
        montantHeuresSupp = (nombreHeures > 40) ? (nombreHeures - 40) * 100.0 * overtime : 0;

        double expectedEtatParClient =  montantRegulier + montantDeplacement + montantHeuresSupp;
        double actualEtatParClient = CalculEmploye.calculerEtatParClient(typeEmploye, nombreHeures, tauxHoraireMin, tauxHoraireMax, distanceDeplacement, overtime);
        Assertions.assertEquals(expectedEtatParClient, actualEtatParClient, 0.001);
    }

    @Test
    public void testCalculerMontantRegulier_Type2() {
        int typeEmploye = 2;
        double nombreHeures = 35.0;
        double tauxHoraireMin = 20.0;
        double tauxHoraireMax = 30.0;

        double expectedMontantRegulier = tauxHoraireMax * nombreHeures;
        double actualMontantRegulier = CalculEmploye.calculerMontantRegulier(typeEmploye, nombreHeures, tauxHoraireMin, tauxHoraireMax);
        Assertions.assertEquals(expectedMontantRegulier, actualMontantRegulier, 0.001);
    }

    @Test
    public void testCalculerMontantDeplacement_Type1() throws JsonException {
        int typeEmploye = 1;
        double distanceDeplacement = 80.0;
        double montantRegulier = 1000.0;

        double expectedMontantDeplacement = 200 - (distanceDeplacement * (0.10 * montantRegulier));
        double actualMontantDeplacement = CalculEmploye.calculerMontantDeplacement(typeEmploye, distanceDeplacement, montantRegulier);
        Assertions.assertEquals(expectedMontantDeplacement, actualMontantDeplacement, 0.001);
    }

    @Test
    public void testCalculerMontantHeuresSupplementairesType1() {
        double overtime = 5.0;

        double expectedMontantHeuresSupplementaires = 50.0 * overtime;
        double actualMontantHeuresSupplementaires = CalculEmploye.calculerMontantHeuresSupplementairesType1(overtime);
        Assertions.assertEquals(expectedMontantHeuresSupplementaires, actualMontantHeuresSupplementaires, 0.001);
    }

    @Test
    public void testCalculerMontantHeuresSupplementairesType2() {
        double overtime = 3.5;

        double expectedMontantHeuresSupplementaires = 75.0 * overtime;
        double actualMontantHeuresSupplementaires = CalculEmploye.calculerMontantHeuresSupplementairesType2(overtime);
        Assertions.assertEquals(expectedMontantHeuresSupplementaires, actualMontantHeuresSupplementaires, 0.001);
    }

    @Test
    public void testArrondirMontant() {
        double montant = 456.78;

        double expectedArrondi = 456.80;
        double actualArrondi = CalculEmploye.arrondirMontant(montant);
        Assertions.assertEquals(expectedArrondi, actualArrondi, 0.001);
    }

}
