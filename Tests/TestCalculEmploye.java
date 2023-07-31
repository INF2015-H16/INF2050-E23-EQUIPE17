package Tests;

import Source.CalculEmploye;
import Source.JsonException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestCalculEmploye {

    @Test
    public void testCalculerEtatCompteTotal() {

        double[] etatsParClient = {100.0, 200.0, 300.0};
        double etatCompteTotalAttendu = 733.77 + 100.0 + 200.0 + 300.0;
        double etatCompteTotalActuel = CalculEmploye.calculerEtatCompteTotal(etatsParClient);

        Assertions.assertEquals(etatCompteTotalAttendu, etatCompteTotalActuel, 0.001);
    }

    @Test
    public void testCalculerCoutVariable() {

        double etatCompteTotal = 1000.0;
        double coutVariableAttendu = (2.5 / 100) * etatCompteTotal;
        double coutVariableActuel = CalculEmploye.calculerCoutVariable(etatCompteTotal);

        Assertions.assertEquals(coutVariableAttendu, coutVariableActuel, 0.001);
    }

    @Test
    public void testCalculerCoutFixe() {

        double etatCompteTotal = 2000.0;
        double coutFixeAttendu = etatCompteTotal * (1.2 / 100);
        double coutFixeActuel = CalculEmploye.calculerCoutFixe(etatCompteTotal);

        Assertions.assertEquals(coutFixeAttendu, coutFixeActuel, 0.001);
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

        double etatParClientAttendu =  montantRegulier + montantDeplacement + montantHeuresSupp;
        double etatParClientActuel = CalculEmploye.calculerEtatParClient(typeEmploye, nombreHeures, tauxHoraireMin,
                tauxHoraireMax, distanceDeplacement, overtime);
        System.out.print(etatParClientActuel);

        Assertions.assertEquals(etatParClientAttendu, etatParClientActuel, 0.001);

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

        double etatParClientAttendu =  montantRegulier + montantDeplacement + montantHeuresSupp;
        double etatParClientActuel = CalculEmploye.calculerEtatParClient(typeEmploye, nombreHeures, tauxHoraireMin,
                tauxHoraireMax, distanceDeplacement, overtime);

        Assertions.assertEquals(etatParClientAttendu, etatParClientActuel, 0.001);
    }

    @Test
    public void testCalculerMontantRegulier_Type2() {

        int typeEmploye = 2;
        double nombreHeures = 35.0;
        double tauxHoraireMin = 20.0;
        double tauxHoraireMax = 30.0;

        double montantRegulierAttendu = tauxHoraireMax * nombreHeures;
        double montantRegulierActuel = CalculEmploye.calculerMontantRegulier(typeEmploye, nombreHeures, tauxHoraireMin,
                tauxHoraireMax);

        Assertions.assertEquals(montantRegulierAttendu, montantRegulierActuel, 0.001);
    }

    @Test
    public void testCalculerMontantDeplacement_Type1() throws JsonException {

        int typeEmploye = 1;
        double distanceDeplacement = 80.0;
        double montantRegulier = 1000.0;

        double montantDeplacementAttendu = 200 - (distanceDeplacement * (0.10 * montantRegulier));
        double montantDeplacementActuel = CalculEmploye.calculerMontantDeplacement(typeEmploye, distanceDeplacement,
                montantRegulier);

        Assertions.assertEquals(montantDeplacementAttendu, montantDeplacementActuel, 0.001);
    }

    @Test
    public void testCalculerMontantHeuresSupplementairesType1() {

        double overtime = 5.0;
        double montantHrsSupAttendu = 50.0 * overtime;
        double montantHrsSupActuel = CalculEmploye.calculerMontantHeuresSupplementairesType1(overtime);

        Assertions.assertEquals(montantHrsSupAttendu, montantHrsSupActuel, 0.001);
    }

    @Test
    public void testCalculerMontantHeuresSupplementairesType2() {

        double overtime = 3.5;
        double montantHrsSupAttendu = 75.0 * overtime;
        double montantHrsSupActuel = CalculEmploye.calculerMontantHeuresSupplementairesType2(overtime);

        Assertions.assertEquals(montantHrsSupAttendu, montantHrsSupActuel, 0.001);
    }

    @Test
    public void testArrondirMontant() {

        double montant = 456.78;
        double arrondissementAttendu = 456.80;
        double arrondissementActuel = CalculEmploye.arrondirMontant(montant);

        Assertions.assertEquals(arrondissementAttendu, arrondissementActuel, 0.001);
    }

}
