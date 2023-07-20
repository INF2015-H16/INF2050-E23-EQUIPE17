package Source;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class GestionProgramme {
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
    public static void mettreAJourNombreTotalInterventions(int count) {
        nombreTotalInterventions += count;
    }
    public static void mettreAJourOccurrencesEtatClient(String plage, int count) {
        int nombreOccurrences = occurrencesEtatClient.optInt(plage, 0);
        int nombreOccurrencesMaj = nombreOccurrences + count;
        occurrencesEtatClient.put(plage, nombreOccurrencesMaj);
    }
    /**
     * Vérifie si un codeClient est présent dans le tableau `nbrs`.
     *
     * @param nbrs Le tableau d'entiers à vérifier.
     * @param codeClient    L'entier à rechercher.
     * @return `true` si le codeClient n'est pas présent dans le tableau `nbrs`, `false` sinon.
     */


    public static boolean verificationCodeClient(int[] nbrs, int codeClient) {
        for (int nbr : nbrs) {
            if (nbr == codeClient)
                return false;
        }
        return true;
    }


    /**
     * Exécute la récupération des interventions à partir des données fournies.
     *
     * @param donnees   Les données d'entrée sous forme de tableau 2D.
     * @param argument2 L'argument 2 spécifique à la récupération des interventions.
     * @param json      Le fichier JSON contenant les informations nécessaires.
     * @param option
     * @throws JsonException Si une exception JSON se produit lors de la récupération des données.
     */
    public static void executerRecuperationInterventions(String[][] donnees, String argument2, String json, JSONArray observations, String option) throws JsonException {
        int itterations = Integer.parseInt(donnees[donnees.length - 1][0]);
        int[] nbrs = new int[30], distance_deplacement = new int[Integer.parseInt(donnees[donnees.length - 1][0])],overtime = new int[Integer.parseInt(donnees[donnees.length - 1][0])],nombre_heures = new int[Integer.parseInt(donnees[donnees.length - 1][0])];
        String[] code = new String[Integer.parseInt(donnees[donnees.length - 1][0])];
        double tauxHoraireMin = 0, taux_horaire_max = 0, montantRegulier = 0;
        double[] etatParClient = new double[code.length];
        Arrays.fill(nbrs, -1);

        recuperationInterventions(donnees, argument2, itterations, nbrs, distance_deplacement, overtime, nombre_heures, code, tauxHoraireMin, taux_horaire_max, montantRegulier, etatParClient,observations, option); // Traitement des données
    }

    private static void recuperationInterventions(String[][] donnees, String argument2, int itterations, int[] nbrs, int[] distance_deplacement, int[] overtime, int[] nombre_heures, String[] code, double taux_horaire_min, double taux_horaire_max, double montantRegulier, double[] EtatParClient, JSONArray observations, String option) throws JsonException {
        int  status;
        for(int i = 0, j = 4; j< donnees.length-1; j++, i++)  {
            status = JsonException.validation(donnees,j);
            if(status != -1 && !donnees[j][0].equals("#"))
                sousMethodeInterventions2(donnees, distance_deplacement, overtime, nombre_heures, code, status, i, j,observations);
            else if(!(donnees[j][0].equals("#")) && !donnees[j][0].equals(""))
            {
                sousMethodeInterventions1(distance_deplacement, overtime, nombre_heures, code, i, donnees[j],observations);
            }
        }
        recuperationInfoEmploye(donnees, argument2, itterations, nbrs, distance_deplacement, overtime, nombre_heures, code, taux_horaire_min, taux_horaire_max, montantRegulier, EtatParClient,observations, option);
    }

    private static void sousMethodeInterventions1(int[] distance_deplacement, int[] overtime, int[] nombre_heures, String[] code, int i, String[] donnees,JSONArray observations) throws JsonException {
        code[i] = donnees[0];
        distance_deplacement[i] = JsonException.validerDistance(Integer.parseInt(donnees[1]));
        overtime[i] = JsonException.validerOvertime(Integer.parseInt(donnees[2]));
        nombre_heures[i] = JsonException.validerNombreHeures(Integer.parseInt(donnees[3]));
        employeeObservation(code[i] ,overtime[i],distance_deplacement[i],observations);
    }

    public static void employeeObservation(String code, int overtime,int distance_deplacement,JSONArray observations) {
        if(distance_deplacement > 50)
            observations.add("Le client " + code + " a une distance de deplacement plus que 50 km");
    }


    private static void sousMethodeInterventions2(String[][] donnees, int[] distance_deplacement, int[] overtime, int[] nombre_heures, String[] code, int status, int i, int j,JSONArray observations) throws JsonException {
        code[i] = donnees[j][0];
        distance_deplacement[i] = JsonException.validerDistance(Integer.parseInt(donnees[j][1])) + JsonException.validerDistance(Integer.parseInt( donnees[status][1]));
        overtime[i] = JsonException.validerOvertime(Integer.parseInt(donnees[j][2])) + JsonException.validerOvertime(Integer.parseInt(donnees[status][2]));
        nombre_heures[i] = JsonException.validerNombreHeures(Integer.parseInt(donnees[j][3])) + JsonException.validerNombreHeures(Integer.parseInt(donnees[status][3]));
        donnees[status][0] = "#";
        donnees[status][1] = "-200";
        employeeObservation(code[i] ,overtime[i],distance_deplacement[i],observations);
    }

    private static void recuperationInfoEmploye(String[][] donnees, String argument2, int itterations, int[] nbrs, int[] distance_deplacement, int[] overtime, int[] nombre_heures, String[] code, double taux_horaire_min, double taux_horaire_max, double montantRegulier, double[] EtatParClient, JSONArray observations, String option) throws JsonException {
        int type_employe, matricule_employe;
        matricule_employe = Integer.parseInt(donnees[0][0]);        // Récupération des données d'entrée
        type_employe = JsonException.validerTypeEmploye(donnees);
        taux_horaire_min = JsonException.validerTaux(donnees, 2);
        taux_horaire_max = JsonException.validerTaux(donnees, 3);
        observationTaux(taux_horaire_max,taux_horaire_min,observations);
        calculEtatClient(argument2, itterations, nbrs, distance_deplacement, overtime, nombre_heures, code, taux_horaire_min, taux_horaire_max, EtatParClient, type_employe, matricule_employe,observations, option);
    }

    private static void observationTaux(double tauxHoraireMax, double tauxHoraireMin, JSONArray observations) {
        if(tauxHoraireMax > 2 * tauxHoraireMin) {
            observations.add("Le taux horaire maximum ne peut pas dépasser deux fois le taux horaire minimum.");
        }
    }

    private static void calculEtatClient(String argument2, int itterations, int[] nbrs, int[] distance_deplacement, int[] overtime, int[] nombre_heures, String[] code, double taux_horaire_min, double taux_horaire_max, double[] etatParClient, int type_employe, int matricule_employe, JSONArray observations, String option) throws JsonException {
        for (int i = 0; i < itterations; i++) {
            if(CalculEmploye.calculerEtatParClient(type_employe, nombre_heures[i], taux_horaire_min, taux_horaire_max, distance_deplacement[i], overtime[i]) > 200){
                etatParClient[i] = CalculEmploye.calculerEtatParClient(type_employe, nombre_heures[i],
                    taux_horaire_min, taux_horaire_max, distance_deplacement[i], overtime[i]);
        }
        }
        calculCouts(argument2, itterations, nbrs, code, etatParClient, matricule_employe,observations,option);
    }

    private static void calculCouts(String argument2, int itterations, int[] nbrs, String[] code, double[] etatParClient, int matricule_employe, JSONArray observations, String option) {
        double etatCompteTotal = CalculEmploye.calculerEtatCompteTotal(etatParClient);
        double coutVariable = CalculEmploye.calculerCoutVariable(etatCompteTotal);
        double coutFixe = CalculEmploye.calculerCoutFixe(etatCompteTotal);
        JsonException.validerFichierSortieDispo(argument2);
        gestionStatistiques(option);
        GestionJson.formattageFichierSortieJson(matricule_employe, CalculEmploye.arrondirMontant(etatCompteTotal), CalculEmploye.arrondirMontant(coutFixe),
                CalculEmploye.arrondirMontant(coutVariable), code, etatParClient, itterations, argument2, nbrs,observations);
    }

    public static boolean isFileEmpty(String fileName) {
        File file = new File(fileName);

        if (!file.exists()) {
            System.out.println("File not found: " + fileName);
            return false;
        }

        try (Scanner scanner = new Scanner(file)) {
            return !scanner.hasNextLine();
        } catch (FileNotFoundException e) {
            System.out.println("Error while reading the file: " + e.getMessage());
            return false;
        }
    }
    private static void gestionStatistiques(String option) {
        JSONObject statistiques = new JSONObject();

        String nomFichier = "Statistique.json";

        boolean fichierVide = isFileEmpty(nomFichier);


        if(option.equals("-SR"))
            reinitialiserStatistiques(statistiques,nomFichier);
        else if(option.equals("-S"))
            afficherStatistiques(statistiques,fichierVide,nomFichier);
    }

    private static void calculStatistiques(JSONObject statistiques, boolean fichierVide) {
    }

    public static void ajouterMessage(String message, String arg) throws IOException {
        JSONObject messageObjet = new JSONObject();
        messageObjet.accumulate("message", message);
        FileUtils.writeStringToFile(new File(arg), messageObjet.toString(), "UTF-8");
        System.exit(0);
    }
}
