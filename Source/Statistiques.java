package Source;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import static java.lang.Double.parseDouble;


public class Statistiques {
    final static int TYPE_EMPLOYE_0 = 0;
    final static int TYPE_EMPLOYE_1 = 1;
    final static int TYPE_EMPLOYE_2 = 2;
    final static double ETAT_PAR_CLIENT_1000 = 1000.00;
    final static double ETAT_PAR_CLIENT_10000 = 10000.00;

    Scanner scanner = new Scanner(System.in);
    public static void afficherStatistiques(JSONObject statistiques, boolean fichierVide, String nomFichier,
                                            JSONArray interventions,String json) {

        System.out.println("Statistiques :");

        System.out.println("-------------------------");
        calculerHeureMaxPourIntervention(json,statistiques);
        calculerInterventionsParTypeEmploye(json,statistiques);
    }

    public static void ecrireStatisques(JSONObject statistiques, String nomFichier, String option) {

        if(!estFichierVide(nomFichier) && option.equals("-S"))
            statistiques = sauvegarderStatistiques(statistiques,nomFichier);

        if(option.equals("-S"))
            try {
                FileUtils.writeStringToFile(new File(nomFichier), statistiques.toString(2), "UTF-8");// le 2 dans tostring sert a ecrire le json d'une facon indente
                affichageStatistique(statistiques);
            } catch (IOException e) {
            System.out.println("Une erreur est survenue : " + e.getMessage());
        }

    }

    private static void affichageStatistique(JSONObject statistiques) {
        JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(statistiques);

        Map<String, Object> jsonMap = new LinkedHashMap<String, Object>();
        for (Iterator<?> keys = jsonObject.keys(); keys.hasNext();) {
            String key = (String) keys.next();
            jsonMap.put(key, jsonObject.get(key));
        }

        for (Map.Entry<String, Object> entry : jsonMap.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }

    }


    public static void reinitialiserStatistiques(JSONObject statistiques, String nomFichier) throws IOException {

        if (confirmerReinitialisation()) {
            String contenuJson = lireContenuFichier(nomFichier);
            statistiques = JSONObject.fromObject(contenuJson);

            reinitialiserValeurs(statistiques);
            System.out.println("Statistiques après réinitialisation :");
            System.out.println("Statistiques :");
            System.out.println("-------------------------");
            affichageStatistique(statistiques);
            sauvegarderStatistiquesSous(statistiques, nomFichier);
        } else {
            System.out.println("Opération annulée. Les statistiques n'ont pas été réinitialisées.");
        }
    }

    private static boolean confirmerReinitialisation() {

        System.out.println("Voulez-vous vraiment réinitialiser les statistiques ? (Oui/Non)");
        Scanner scanner = new Scanner(System.in);
        String reponse = scanner.nextLine().toLowerCase();
        return reponse.equals("oui");
    }

    private static String lireContenuFichier(String nomFichier) throws IOException {

        return new String(Files.readAllBytes(Paths.get(nomFichier)), StandardCharsets.UTF_8);
    }

    private static void reinitialiserValeurs(JSONObject statistiques) {

        Iterator<String> keysIterator = statistiques.keys();
        while (keysIterator.hasNext()) {
            String key = keysIterator.next();
            statistiques.put(key, 0);
        }
    }

    private static void sauvegarderStatistiquesSous(JSONObject statistiques, String nomFichier) {

        try {
            FileUtils.writeStringToFile(new File(nomFichier), statistiques.toString(2), "UTF-8");
            System.out.println("Statistiques réinitialisées.");
        } catch (IOException e) {
            System.out.println("Une erreur est survenue : " + e.getMessage());
        }
    }


    public static void mettreAJourNombreTotalInterventions(JSONObject statistiques, int compte) {

        String nomFichier = "statistiques.json";
        chargerStatistiques(statistiques, nomFichier);
        int nombreTotalInterventions = statistiques.optInt("interventions", 0);

        nombreTotalInterventions += compte;

        statistiques.put("interventions", nombreTotalInterventions);
    }


    public static void mettreAJourOccurrencesEtatClient(JSONObject statistiques, String plage, int compte) {

        String nomFichier = "Statistique.json";
        chargerStatistiques(statistiques, nomFichier);
        JSONObject occurrencesEtatClient = statistiques.optJSONObject("etat_par_client");

        if (occurrencesEtatClient == null) {
            occurrencesEtatClient = new JSONObject();
            statistiques.put("etat_par_client", occurrencesEtatClient);
        }
        int nombreOccurrences = occurrencesEtatClient.optInt(plage, 0);
        int nombreOccurrencesMaj = nombreOccurrences + compte;

        occurrencesEtatClient.put(plage, nombreOccurrencesMaj);
    }

    private static void chargerStatistiques(JSONObject statistiques,String nomFichier) {

        boolean fichierVide = estFichierVide(nomFichier);

        if (!fichierVide) {
            try (Scanner scanner = new Scanner(new File(nomFichier))) {
                StringBuilder constructeurString = new StringBuilder();
                while (scanner.hasNextLine()) {
                    constructeurString.append(scanner.nextLine());
                }
                statistiques = JSONObject.fromObject(constructeurString.toString());
            } catch (IOException e) {
                System.out.println("Erreur lors de la lecture des statistiques : " + e.getMessage());
            }
        }
    }


    private static JSONObject sauvegarderStatistiques(JSONObject statistiques, String nomFichier) {

        JSONObject objetJson = null;
        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get(nomFichier)), StandardCharsets.UTF_8);
            objetJson = (JSONObject) JSONSerializer.toJSON(jsonContent);

            Iterator<String> keysIterator = statistiques.keys();
            while (keysIterator.hasNext()) {
                String cle = keysIterator.next();
                int valeur1 = statistiques.getInt(cle);
                int valeur2 = objetJson.getInt(cle);
                objetJson.put(cle, valeur1 + valeur2);
            }

        } catch (Exception e) {
            System.out.println("Il y'a une erreur dans le fichier statistique.json");
            System.exit(0);
        }

        return objetJson;
    }


    public static void calculerHeureMaxPourIntervention(String entreeJson, JSONObject statistique) {

        JSONArray tableauInterventions;
        JSONObject employe = JSONObject.fromObject(entreeJson);
        tableauInterventions = employe.getJSONArray("interventions");

        int heureMax = 0;

        for (Object interventionObj : tableauInterventions) {
            JSONObject interventionJson = (JSONObject) interventionObj;
            int nombreHeures = interventionJson.getInt("nombre_heures");
            heureMax = Math.max(heureMax, nombreHeures);
        }
        statistique.put("Heure maximal soumis pour une intervention: ", heureMax);
    }


    public static void calculerEtatParClientMax(JSONObject employe, JSONObject statistique) {

        JSONArray clients = employe.getJSONArray("clients");

        double etatParClientMax = 0.0;


        for (int i = 0; i < clients.size(); i++) {

            JSONObject objetJson = clients.getJSONObject(i);
            String etatParClient = objetJson.getString("etat_par_client");
            etatParClientMax = Math.max(etatParClientMax,
                    parseDouble(etatParClient.substring(0,etatParClient.length()-1)));
        }

        statistique.put("L’état par client maximal retourné pour un client: ", etatParClientMax);
    }

    public static void calculerOccurrencesEtatParClient(JSONObject employe, JSONObject statistique) {

        int nbrEtatInf1000 = 0;
        int nbrEtatEntreMinMax = 0;
        int nbrEtatSup10000 = 0;
        int tableauDonnees[] =new int[3] ;

        JSONArray clients = employe.getJSONArray("clients");

        System.out.println(employe.toString(2));

        for (int i = 0; i < clients.size(); i++) {
            JSONObject jsonObject = clients.getJSONObject(i);
            String etatParClient = jsonObject.getString("etat_par_client");
            double etatClient = parseDouble(etatParClient.substring(0, etatParClient.length() - 1));

            mettreCompteOccurrencesAJour(tableauDonnees,etatClient, nbrEtatInf1000, nbrEtatEntreMinMax, nbrEtatSup10000);
        }

        mettreStatistiquesAJour(tableauDonnees,statistique);
    }

    private static void mettreCompteOccurrencesAJour(int[] tableau , double etatClient, int nbrEtatInf1000, int nbrEtatEntreMinMax,
                                                     int nbrEtatSup10000) {
        if (etatClient < ETAT_PAR_CLIENT_1000) {
            nbrEtatInf1000++;
            tableau[0] = nbrEtatInf1000;
        } else if (etatClient > ETAT_PAR_CLIENT_1000 && etatClient < ETAT_PAR_CLIENT_10000) {
            nbrEtatEntreMinMax++;
            tableau[1] = nbrEtatEntreMinMax;
        } else if (etatClient > ETAT_PAR_CLIENT_10000) {
            nbrEtatSup10000++;
            tableau[2] = nbrEtatSup10000;
        }
    }

    private static void mettreStatistiquesAJour(int[] tableau, JSONObject statistique) {
        statistique.put("Le nombre d'etats par client moins que 1000 est de : ", tableau[0]);
        statistique.put("Le nombre d'etats par client entre 1000 et 10000 est de : ", tableau[1]);
        statistique.put("Le nombre d'etats par client superieur a 10000 est de : ", tableau[2]);
    }


    public static int calculerTotalInterventions(JSONObject employe, JSONObject statistique) {

        int totalInterventions = 0;
        JSONArray listeJson = employe.getJSONArray("clients");

        for (Object obj : listeJson) {
            JSONObject objetJson = (JSONObject) obj;
            if (objetJson.containsKey("code_client")) {
                totalInterventions++;
            }
        }

        statistique.put("Le total d'interventions soumises tous employés confondus est : ", totalInterventions);

        return totalInterventions;
    }


    public static void calculerInterventionsParTypeEmploye(String entreeJson, JSONObject statistique) {

        int typeEmploye0 = 0;
        int typeEmploye1 = 0;
        int typeEmploye2 = 0;

        JSONObject objetJson =  JSONObject.fromObject(entreeJson);

        int typeEmploye = objetJson.getInt("type_employe");

        if (typeEmploye == TYPE_EMPLOYE_0) {
            JSONArray interventions = objetJson.getJSONArray("interventions");
            typeEmploye0 = interventions.size();

        } else if(typeEmploye == TYPE_EMPLOYE_1){
            JSONArray interventions = objetJson.getJSONArray("interventions");
            typeEmploye1 = interventions.size();

        } else if(typeEmploye == TYPE_EMPLOYE_2){
            JSONArray interventions = objetJson.getJSONArray("interventions");
            typeEmploye2 = interventions.size();
        }


        statistique.put("Le nombre d'interventions pour les employes de type 0 est de :" , typeEmploye0);
        statistique.put("Le nombre d'interventions pour les employes de type 1 est de :" , typeEmploye1);
        statistique.put("Le nombre d'interventions pour les employes de type 2 est de :" , typeEmploye2);

    }


    public static void gestionStatistiques(String option, JSONArray interventions, String json,
                                           JSONObject statistiques) {

        String nomFichier = "Statistique.json";

        boolean fichierVide = estFichierVide(nomFichier);

        try {
            if (option.equals("-SR")) {
                reinitialiserStatistiques(statistiques, nomFichier);
            }
            else if (option.equals("-S")) {
                afficherStatistiques(statistiques, fichierVide, nomFichier, interventions, json);
            }
        }catch (IOException e)
        {
            e.getMessage();
        }
    }
    public static boolean estFichierVide(String nomFichier) {

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

    public static JSONArray listeInterventions(String json) {

        JSONArray interventions = new JSONArray();
        String extrait = "";
        boolean drapeau = false;
        char ch = 0;

        int i = 0;

        while(i < json.length()) {

            ch = json.charAt(i);

            if(ch == '[')
                drapeau = true;

            else if (ch == ']'){
                drapeau = false;
                extrait += "]";
            }

            if(drapeau)
                extrait += ch;

            i++;
        }

        interventions = JSONArray.fromObject(extrait.toString());

        return interventions;
    }
}
