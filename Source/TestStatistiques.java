package Source;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;


public class TestStatistiques {

        @Test
        public void testCalculerHeureMaxPourIntervention() {
            String json = "{\"interventions\":[" +
                    "{\"nombre_heures\": 3}," +
                    "{\"nombre_heures\": 5}," +
                    "{\"nombre_heures\": 7}," +
                    "{\"nombre_heures\": 4}" +
                    "]}";

            JSONObject statistiques = new JSONObject();
            Statistiques.calculerHeureMaxPourIntervention(json, statistiques);

            int expectedHeureMax = 7;
            int actualHeureMax = statistiques.optInt("Heure maximal soumis pour une intervention: ", 0);
            Assertions.assertEquals(expectedHeureMax, actualHeureMax);
        }

        @Test
        public void testCalculerEtatParClientMax() {
            String json = "{\"clients\":[" +
                    "{\"etat_par_client\": \"200.00$\"}," +
                    "{\"etat_par_client\": \"500.00$\"}," +
                    "{\"etat_par_client\": \"1000.00$\"}," +
                    "{\"etat_par_client\": \"750.00$\"}" +
                    "]}";

            JSONObject statistiques = new JSONObject();
            JSONObject employe = JSONObject.fromObject(json);
            Statistiques.calculerEtatParClientMax(employe, statistiques);

            double expectedEtatParClientMax = 1000.0;
            double actualEtatParClientMax = statistiques.optDouble("l’état par client maximal retourné pour un client: ", 0.0);
            Assertions.assertEquals(expectedEtatParClientMax, actualEtatParClientMax, 0.001);
        }

        @Test
        public void testCalculerOccurrencesEtatParClient() {
            String json = "{\"clients\":[" +
                    "{\"etat_par_client\": \"200.00$\"}," +
                    "{\"etat_par_client\": \"500.00$\"}," +
                    "{\"etat_par_client\": \"1000.00$\"}," +
                    "{\"etat_par_client\": \"750.00$\"}," +
                    "{\"etat_par_client\": \"8000.00$\"}," +
                    "{\"etat_par_client\": \"15000.00$\"}" +
                    "]}";

            JSONObject statistiques = new JSONObject();
            JSONObject employe = JSONObject.fromObject(json);
            Statistiques.calculerOccurrencesEtatParClient(employe, statistiques);

            int expectedEtatInf1000 = 2;
            int expectedEtatEntreMinMax = 3;
            int expectedEtatSup10000 = 1;

            int actualEtatInf1000 = statistiques.optInt("Le nombre d'etats par client moins que 1000 est de : ", 0);
            int actualEtatEntreMinMax = statistiques.optInt("Le nombre d'etats par client entre 1000 et 10000 est de : ", 0);
            int actualEtatSup10000 = statistiques.optInt("Le nombre d'etats par client superieur a 10000 est de : ", 0);

            Assertions.assertEquals(expectedEtatInf1000, actualEtatInf1000);
            Assertions.assertEquals(expectedEtatEntreMinMax, actualEtatEntreMinMax);
            Assertions.assertEquals(expectedEtatSup10000, actualEtatSup10000);
        }

        @Test
        public void testCalculerTotalInterventions() {
            String json = "{\"clients\":[" +
                    "{\"code_client\": \"A\"}," +
                    "{\"code_client\": \"B\"}," +
                    "{\"code_client\": \"C\"}," +
                    "{\"code_client\": \"D\"}" +
                    "]}";

            JSONObject statistiques = new JSONObject();
            JSONObject employe = JSONObject.fromObject(json);
            Statistiques.calculerTotalInterventions(employe, statistiques);

            int expectedTotalInterventions = 4;
            int actualTotalInterventions = statistiques.optInt("Le total d'interventions dans le fichier JSON est : ", 0);
            Assertions.assertEquals(expectedTotalInterventions, actualTotalInterventions);
        }

        @Test
        public void testCalculerInterventionsParTypeEmploye_Type0() {
            String json = "{\"type_employe\": 0, \"interventions\":[" +
                    "{\"nombre_heures\": 3}," +
                    "{\"nombre_heures\": 5}," +
                    "{\"nombre_heures\": 7}," +
                    "{\"nombre_heures\": 4}" +
                    "]}";

            JSONObject statistiques = new JSONObject();
            Statistiques.calculerInterventionsParTypeEmploye(json, statistiques);

            int expectedInterventionsType0 = 4;
            int actualInterventionsType0 = statistiques.optInt("Le nombre d'interventions pour les employes de type 0 est de :", 0);
            Assertions.assertEquals(expectedInterventionsType0, actualInterventionsType0);
        }

        @Test
        public void testCalculerInterventionsParTypeEmploye_Type1() {
            String json = "{\"type_employe\": 1, \"interventions\":[" +
                    "{\"nombre_heures\": 3}," +
                    "{\"nombre_heures\": 5}," +
                    "{\"nombre_heures\": 7}," +
                    "{\"nombre_heures\": 4}" +
                    "]}";

            JSONObject statistiques = new JSONObject();
            Statistiques.calculerInterventionsParTypeEmploye(json, statistiques);

            int expectedInterventionsType1 = 4;
            int actualInterventionsType1 = statistiques.optInt("Le nombre d'interventions pour les employes de type 1 est de :", 0);
            Assertions.assertEquals(expectedInterventionsType1, actualInterventionsType1);
        }

        @Test
        public void testCalculerInterventionsParTypeEmploye_Type2() {
            String json = "{\"type_employe\": 2, \"interventions\":[" +
                    "{\"nombre_heures\": 3}," +
                    "{\"nombre_heures\": 5}," +
                    "{\"nombre_heures\": 7}," +
                    "{\"nombre_heures\": 4}" +
                    "]}";

            JSONObject statistiques = new JSONObject();
            Statistiques.calculerInterventionsParTypeEmploye(json, statistiques);

            int expectedInterventionsType2 = 4;
            int actualInterventionsType2 = statistiques.optInt("Le nombre d'interventions pour les employes de type 2 est de :", 0);
            Assertions.assertEquals(expectedInterventionsType2, actualInterventionsType2);
        }

        @Test
        public void testEstFichierVide_FileExistsNotEmpty() {
            String nomFichier = "test_file_not_empty.txt";
            try {
                FileUtils.writeStringToFile(new File(nomFichier), "Not empty content", "UTF-8");
                boolean isFileEmpty = Statistiques.estFichierVide(nomFichier);
                Assertions.assertFalse(isFileEmpty);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Test
        public void testEstFichierVide_FileExistsEmpty() {
            String nomFichier = "test_file_empty.txt";
            try {
                FileUtils.writeStringToFile(new File(nomFichier), "", "UTF-8");
                boolean isFileEmpty = Statistiques.estFichierVide(nomFichier);
                Assertions.assertTrue(isFileEmpty);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Test
        public void testEstFichierVide_FileDoesNotExist() {
            String nomFichier = "non_existent_file.txt";
            boolean isFileEmpty = Statistiques.estFichierVide(nomFichier);
            Assertions.assertFalse(isFileEmpty);
        }

        @Test
        public void testListeInterventions() {
            String json = "{\"interventions\":[" +
                    "{\"nombre_heures\": 3}," +
                    "{\"nombre_heures\": 5}," +
                    "{\"nombre_heures\": 7}," +
                    "{\"nombre_heures\": 4}" +
                    "]}";

            JSONArray interventions = Statistiques.listeInterventions(json);
            Assertions.assertEquals(4, interventions.size());
        }
        @Test
        public void testMettreAJourNombreTotalInterventions() {
            JSONObject statistiques = new JSONObject();
            statistiques.put("interventions", 10);

            Statistiques.mettreAJourNombreTotalInterventions(statistiques, 5);
            int expectedTotalInterventions = 15;
            int actualTotalInterventions = statistiques.optInt("interventions", 0);
            Assertions.assertEquals(expectedTotalInterventions, actualTotalInterventions);
        }

        @Test
        public void testMettreAJourOccurrencesEtatClient() {
            JSONObject statistiques = new JSONObject();
            JSONObject occurrencesEtatClient = new JSONObject();
            occurrencesEtatClient.put("1000", 2);
            occurrencesEtatClient.put("5000", 5);
            statistiques.put("etat_par_client", occurrencesEtatClient);

            Statistiques.mettreAJourOccurrencesEtatClient(statistiques, "1000", 3);
            Statistiques.mettreAJourOccurrencesEtatClient(statistiques, "5000", 2);
            Statistiques.mettreAJourOccurrencesEtatClient(statistiques, "10000", 1);

            int expectedOccurrences1000 = 5;
            int expectedOccurrences5000 = 7;
            int expectedOccurrences10000 = 1;

            int actualOccurrences1000 = occurrencesEtatClient.optInt("1000", 0);
            int actualOccurrences5000 = occurrencesEtatClient.optInt("5000", 0);
            int actualOccurrences10000 = occurrencesEtatClient.optInt("10000", 0);

            Assertions.assertEquals(expectedOccurrences1000, actualOccurrences1000);
            Assertions.assertEquals(expectedOccurrences5000, actualOccurrences5000);
            Assertions.assertEquals(expectedOccurrences10000, actualOccurrences10000);
        }


}
