package Source;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;


//TODO Faire une methode pour calculer le nombre total d’interventions

public class Statistique {
    final static int TYPE_EMPLOYE_0 = 0;
    final static int TYPE_EMPLOYE_1 = 1;
    final static int TYPE_EMPLOYE_2 = 2;
    final static double ETAT_PAR_CLIENT_1000 = 1000.00;
    final static double ETAT_PAR_CLIENT_10000 = 10000.00;

    Scanner scanner = new Scanner(System.in);
    private static int nombreTotalInterventions = 0;
    private static JSONObject occurrencesEtatClient = new JSONObject();
    public static void afficherStatistiques(JSONObject statistiques, boolean fichierVide, String nomFichier) {
        System.out.println("Statistiques :");
        System.out.println("-------------");
        System.out.println("Nombre total d'interventions : " + nombreTotalInterventions);
        System.out.println("Occurrences par état par client :");
        for (Object plage : occurrencesEtatClient.keySet()) {
            int count = occurrencesEtatClient.getInt(plage.toString());
            System.out.println("- " + plage + " : " + count);
        }
    }
    
    public static void reinitialiserStatistiques(JSONObject statistiques, String nomFichier) {
        System.out.println("Voulez-vous vraiment reinitialiser les statistiques ? (Oui/Non)");
        Scanner scanner = new Scanner(System.in);
        String reponse = scanner.nextLine();
        reponse = reponse.toLowerCase();

        if(reponse.equals("oui"))
        {
            statistiques.put("nombre_total_interventions", 0);

            JSONObject etatParClient = new JSONObject();
            etatParClient.put("moins_de_1000", 0);
            etatParClient.put("entre_1000_et_10000", 0);
            etatParClient.put("plus_de_10000", 0);
            statistiques.put("etat_par_client", 0);

            JSONObject interventionsParEmploye = new JSONObject();
            interventionsParEmploye.put("Nombre d'interventions pour type employe 0", 0);
            interventionsParEmploye.put("Nombre d'interventions pour type employe 1", 0);
            interventionsParEmploye.put("Nombre d'interventions pour type employe 2", 0);
            statistiques.put("interventions_par_employe", interventionsParEmploye);
            statistiques.put("nombre heures maximal", 0);
            statistiques.put("etat maximal par client", 0);

            try {
                FileUtils.writeStringToFile(new File(nomFichier), statistiques.toString(2), "UTF-8");// le 2 dans tostring sert a ecrire le json d'une facon indente
                System.out.println("Statistique reinitialiser.");
            } catch (IOException e) {
                System.out.println("Une erreur est survenue : " + e.getMessage());
            }
        }

    }

    public static void mettreAJourNombreTotalInterventions(JSONObject statistiques,int count) {
        String nomFichier = "";
        chargerStatistiques(statistiques,nomFichier);
        int nombreTotalInterventions = statistiques.optInt("interventions", 0);
        nombreTotalInterventions += count;
        statistiques.put("interventions", nombreTotalInterventions);
        sauvegarderStatistiques(nomFichier,statistiques);
    }
    public static void mettreAJourOccurrencesEtatClient(JSONObject statistiques,String plage, int count) {
        String nomFichier = "";
        chargerStatistiques(statistiques,nomFichier);
        JSONObject occurrencesEtatClient = statistiques.optJSONObject("etat_par_client");
        if (occurrencesEtatClient == null) {
            occurrencesEtatClient = new JSONObject();
            statistiques.put("etat_par_client", occurrencesEtatClient);
        }
        int nombreOccurrences = occurrencesEtatClient.optInt(plage, 0);
        int nombreOccurrencesMaj = nombreOccurrences + count;
        occurrencesEtatClient.put(plage, nombreOccurrencesMaj);
        sauvegarderStatistiques(nomFichier,statistiques);
    }
    private static void chargerStatistiques(JSONObject statistiques,String nomFichier) {
        boolean fichierVide = estFichierVide(nomFichier);
        if (!fichierVide) {
            try (Scanner scanner = new Scanner(new File(nomFichier))) {
                StringBuilder stringBuilder = new StringBuilder();
                while (scanner.hasNextLine()) {
                    stringBuilder.append(scanner.nextLine());
                }
                statistiques = JSONObject.fromObject(stringBuilder.toString());
            } catch (IOException e) {
                System.out.println("Erreur lors de la lecture des statistiques : " + e.getMessage());
            }
        }
    }
    private static void sauvegarderStatistiques(String nomFichier, JSONObject statistiques) {
        try {
            FileUtils.writeStringToFile(new File(nomFichier), statistiques.toString(), "UTF-8");
        } catch (IOException e) {
            System.out.println("Erreur lors de la sauvegarde des statistiques : " + e.getMessage());
        }
    }

    public static void mettreAJourOccurrencesEtatClient(String plage, int count) {
        int nombreOccurrences = occurrencesEtatClient.optInt(plage, 0);
        int nombreOccurrencesMaj = nombreOccurrences + count;
        occurrencesEtatClient.put(plage, nombreOccurrencesMaj);
    }
    public static void calculerHeureMaxPourIntervention(String entreeJson, JSONArray tableauInterventions,
                                                        String arg3) throws IOException {

        JSONObject employe = JSONObject.fromObject(entreeJson);
        tableauInterventions = employe.getJSONArray("interventions");
        int heureMax = 0;
        for (Object interventionObj : tableauInterventions) {
            JSONObject interventionJson = (JSONObject) interventionObj;
            int nombreHeures = interventionJson.getInt("nombre_heures");
            heureMax = Math.max(heureMax, nombreHeures);
        }
        GestionProgramme.ajouterMessage("Le nombre d'heures maximal pour une intervention est de" +" "+ heureMax
                ,arg3);

    }

    public static void calculerEtatParClientMax(String sortieJson, JSONArray clients,
                                                String arg3 ) throws IOException {

        JSONObject employe = JSONObject.fromObject(sortieJson);
        clients = employe.getJSONArray("clients");
        double etatParClientMax = 0.0;

        for (Object clientObj : clients) {
            JSONObject clientJson = (JSONObject) clientObj;
            double etatParClient = clientJson.getDouble("etat_par_client");
            etatParClientMax = Math.max(etatParClientMax, etatParClient);
        }

        GestionProgramme.ajouterMessage("L'etat par client maximal est de" +" "+ etatParClientMax
                ,arg3);

    }

    public static void calculerOccurrencesEtatParClient(String sortieJson, String arg3) throws IOException {

        int nbrEtatInf1000 = 0;
        int nbrEtatEntreMinMax = 0;
        int nbrEtatSup10000 = 0;

        JSONArray jsonArray = (JSONArray) JSONSerializer.toJSON(sortieJson);

        for (Object obj : jsonArray) {
            JSONObject jsonObject = (JSONObject) obj;
                double etatParClient = jsonObject.getDouble("etat_par_client");
                if (etatParClient < ETAT_PAR_CLIENT_1000) {
                    nbrEtatInf1000++;

                } else if(etatParClient > ETAT_PAR_CLIENT_1000 && etatParClient < ETAT_PAR_CLIENT_10000){
                    nbrEtatEntreMinMax++;

            } else if(etatParClient > ETAT_PAR_CLIENT_10000){
                    nbrEtatSup10000++;
                }

            }

        GestionProgramme.ajouterMessage("Le nombre d'etats par client < 1000 est de :" + nbrEtatInf1000 +
                        "\nLe nombre d'etats par client entre 1000 et 10000 est de :" + nbrEtatEntreMinMax +
                        "\nLe nombre d'etats par client superieur a 10000 est de :" + nbrEtatSup10000, arg3);

    }


    public static void gestionStatistiques(String option) {
        JSONObject statistiques = new JSONObject();

        String nomFichier = "Statistique.json";

        boolean fichierVide = estFichierVide(nomFichier);


        if(option.equals("-SR"))
            reinitialiserStatistiques(statistiques,nomFichier);
        else if(option.equals("-S"))
            afficherStatistiques(statistiques,fichierVide,nomFichier);
    }
    private static boolean estFichierVide(String nomFichier) {
        File fichier = new File(nomFichier);
        if (!fichier.exists()) {
            System.out.println("Fichier introuvable : " + nomFichier);
            return false;
        }
        try (Scanner scanner = new Scanner(fichier)) {
            return !scanner.hasNextLine();
        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture du fichier : " + e.getMessage());
            return false;
        }
    }
    private static void calculStatistiques(JSONObject statistiques, boolean fichierVide) {
    }
}
