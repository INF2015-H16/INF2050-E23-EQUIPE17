package Tests;

import Source.Statistiques;
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

            int heureMaxAttendue = 7;
            int heureMaxActuelle = statistiques.optInt("Heure maximal soumis pour une intervention: ", 0);
            Assertions.assertEquals(heureMaxAttendue, heureMaxActuelle);
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

            double etatParClientMaxAttendu = 0.0;
            double etatParClientMaxActuel = statistiques.optDouble("l’état par client maximal retourné pour un client: ", 0.0);
            Assertions.assertEquals(etatParClientMaxAttendu, etatParClientMaxActuel, 0.001);
        }

        @Test
        public void testCalculerOccurrencesEtatParClient() {
            String json = "{\n" +
                    "  \"matricule_employe\": 123456789,\n" +
                    "  \"etat_compte\": \"1349.40$\",\n" +
                    "  \"cout_fixe\": \"16.20$\",\n" +
                    "  \"cout_variable\": \"33.75$\",\n" +
                    "  \"clients\":   [\n" +
                    "        {\n" +
                    "      \"code_client\": \"C456\",\n" +
                    "      \"etat_par_client\": \"100000$\"\n" +
                    "    },\n" +
                    "        {\n" +
                    "      \"code_client\": \"C789\",\n" +
                    "      \"etat_par_client\": \"209.40$\"\n" +
                    "    },\n" +
                    "        {\n" +
                    "      \"code_client\": \"C123\",\n" +
                    "      \"etat_par_client\": \"1500$\"\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"observations\": [\"L’écart maximal entre les dates d’intervention (date_intervention) du " +
                    "client C789 d’un même employé doit être de moins de 6 mois.\"]\n" +
                    "}";

            JSONObject employe = JSONObject.fromObject(json);
            JSONObject statistiques = new JSONObject();
            Statistiques.calculerOccurrencesEtatParClient(employe, statistiques);

            int etatInf1000Attendu = 1;
            int etatEntreMinMaxAttendu = 1 ;
            int etatSup10000Attendu = 1;

            int etatInf1000Actuel = statistiques.optInt("Le nombre d'etats par client moins que 1000 est de : ");
            int etatEntreMinMaxActuel = statistiques.optInt("Le nombre d'etats par client entre 1000 et 10000 est de : ");
            int etatSup10000Actuel = statistiques.optInt("Le nombre d'etats par client superieur a 10000 est de : ");

            Assertions.assertEquals(etatInf1000Attendu, etatInf1000Actuel);
            Assertions.assertEquals(etatEntreMinMaxAttendu, etatEntreMinMaxActuel);
            Assertions.assertEquals(etatSup10000Attendu, etatSup10000Actuel);
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

            int totalInterventionsAttendu = 4;

            int totalInterventionsActuel = Statistiques.calculerTotalInterventions(employe, statistiques);

            Assertions.assertEquals(totalInterventionsAttendu, totalInterventionsActuel);
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

            int interventionsType0Attendues = 4;
            int interventionsType0Actuelles = statistiques.optInt("Le nombre d'interventions pour les employes de type 0 est de :", 0);
            Assertions.assertEquals(interventionsType0Attendues, interventionsType0Actuelles);
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

            int interventionsType1Attendues = 4;
            int interventionsType1Actuelles = statistiques.optInt("Le nombre d'interventions pour les employes de type 1 est de :", 0);
            Assertions.assertEquals(interventionsType1Attendues, interventionsType1Actuelles);
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

            int interventionsType2Attendues = 4;
            int interventionsType2Actuelles = statistiques.optInt("Le nombre d'interventions pour les employes de type 2 est de :", 0);
            Assertions.assertEquals(interventionsType2Attendues, interventionsType2Actuelles);
        }

        @Test
        public void testEstFichierVide_FileExistsNotEmpty() {
            String nomFichier = "test_file_not_empty.txt";
            try {
                FileUtils.writeStringToFile(new File(nomFichier), "Not empty content", "UTF-8");
                boolean estFichierVide = Statistiques.estFichierVide(nomFichier);
                Assertions.assertFalse(estFichierVide);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Test
        public void testEstFichierVide_FileExistsEmpty() {
            String nomFichier = "test_file_empty.txt";
            try {
                FileUtils.writeStringToFile(new File(nomFichier), "", "UTF-8");
                boolean estFichiervide = Statistiques.estFichierVide(nomFichier);
                Assertions.assertTrue(estFichiervide);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Test
        public void testEstFichierVide_FileDoesNotExist() {
            String nomFichier = "non_existent_file.txt";
            boolean estFichierVide = Statistiques.estFichierVide(nomFichier);
            Assertions.assertFalse(estFichierVide);
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
            int totalInterventionsAttendu = 15;
            int totalInterventionsActuel = statistiques.optInt("interventions", 0);
            Assertions.assertEquals(totalInterventionsAttendu, totalInterventionsActuel);
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

            int occurrences1000Attendu = 2;
            int occurrences5000Attendu = 5;
            int occurrences10000Attendu = 0;

            int occurrencesActuelles1000 = occurrencesEtatClient.optInt("1000", 0);
            int occurrencesActuelles5000 = occurrencesEtatClient.optInt("5000", 0);
            int occurrencesActuelles10000 = occurrencesEtatClient.optInt("10000", 0);

            Assertions.assertEquals(occurrences1000Attendu, occurrencesActuelles1000);
            Assertions.assertEquals(occurrences5000Attendu, occurrencesActuelles5000);
            Assertions.assertEquals(occurrences10000Attendu, occurrencesActuelles10000);
        }


}
