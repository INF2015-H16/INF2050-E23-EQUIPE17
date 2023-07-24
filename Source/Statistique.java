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
import java.util.Scanner;

import static java.lang.Double.parseDouble;


public class Statistique {
    final static int TYPE_EMPLOYE_0 = 0;
    final static int TYPE_EMPLOYE_1 = 1;
    final static int TYPE_EMPLOYE_2 = 2;
    final static double ETAT_PAR_CLIENT_1000 = 1000.00;
    final static double ETAT_PAR_CLIENT_10000 = 10000.00;

    Scanner scanner = new Scanner(System.in);
    private static int nombreTotalInterventions = 0;
    private static JSONObject occurrencesEtatClient = new JSONObject();
    public static void afficherStatistiques(JSONObject statistiques, boolean fichierVide, String nomFichier, JSONArray interventions,String json) {
        System.out.println("Statistiques :");
        System.out.println("-------------");
        System.out.println("Nombre total d'interventions : " + nombreTotalInterventions);
        System.out.println("Occurrences par état par client :");
        calculerHeureMaxPourIntervention(json,statistiques);
        calculerInterventionsParTypeEmploye(json,statistiques);
    }

    public static void ecrireStatisques(JSONObject statistiques, String nomFichier, String option) {
        if(!estFichierVide(nomFichier) && option.equals("-S"))
            statistiques = sauvegarderStatistiques(statistiques,nomFichier);

        if(option.equals("-S"))
        try {
            FileUtils.writeStringToFile(new File(nomFichier), statistiques.toString(2), "UTF-8");// le 2 dans tostring sert a ecrire le json d'une facon indente
        } catch (IOException e) {
            System.out.println("Une erreur est survenue : " + e.getMessage());
        }
    }


    public static void reinitialiserStatistiques(JSONObject statistiques, String nomFichier) throws IOException {
        if (confirmerReinitialisation()) {
            String jsonContent = lireContenuFichier(nomFichier);
            statistiques = JSONObject.fromObject(jsonContent);

            reinitialiserValeurs(statistiques);

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


    /**
     * Met à jour le nombre total d'interventions dans les statistiques et sauvegarde les statistiques dans un fichier.
     *
     * @param statistiques   L'objet JSON contenant les statistiques à mettre à jour.
     * @param count          Le nombre d'interventions à ajouter au nombre total.
     */
    public static void mettreAJourNombreTotalInterventions(JSONObject statistiques, int count) {
        String nomFichier = "statistiques.json"; // Le nom du fichier où les statistiques sont sauvegardées
        chargerStatistiques(statistiques, nomFichier); // Charge les statistiques à partir du fichier
        int nombreTotalInterventions = statistiques.optInt("interventions", 0); // Obtient le nombre total d'interventions actuel
        nombreTotalInterventions += count; // Met à jour le nombre total d'interventions en ajoutant le count
        statistiques.put("interventions", nombreTotalInterventions); // Met à jour le nombre total d'interventions dans l'objet JSON
    }

    /**
     * Met à jour le nombre d'occurrences avec un état par client dans la plage spécifiée et sauvegarde les statistiques dans un fichier.
     *
     * @param statistiques   L'objet JSON contenant les statistiques à mettre à jour.
     * @param plage          La plage d'état par client (ex: "moins_de_1000", "entre_1000_et_10000", "plus_de_10000").
     * @param count          Le nombre d'occurrences à ajouter à la plage spécifiée.
     */
    public static void mettreAJourOccurrencesEtatClient(JSONObject statistiques, String plage, int count) {
        String nomFichier = "statistiques.json"; // Le nom du fichier où les statistiques sont sauvegardées
        chargerStatistiques(statistiques, nomFichier); // Charge les statistiques à partir du fichier
        JSONObject occurrencesEtatClient = statistiques.optJSONObject("etat_par_client"); // Obtient l'objet JSON "etat_par_client" s'il existe
        if (occurrencesEtatClient == null) {
            occurrencesEtatClient = new JSONObject(); // Crée un nouvel objet JSON s'il n'existe pas encore
            statistiques.put("etat_par_client", occurrencesEtatClient); // Ajoute l'objet "etat_par_client" aux statistiques
        }
        int nombreOccurrences = occurrencesEtatClient.optInt(plage, 0); // Obtient le nombre d'occurrences actuel pour la plage spécifiée
        int nombreOccurrencesMaj = nombreOccurrences + count; // Met à jour le nombre d'occurrences en ajoutant le count
        occurrencesEtatClient.put(plage, nombreOccurrencesMaj); // Met à jour le nombre d'occurrences pour la plage spécifiée dans l'objet JSON
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


    private static JSONObject sauvegarderStatistiques(JSONObject statistiques, String nomFichier) {
        JSONObject jsonObject = null;
        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get(nomFichier)), StandardCharsets.UTF_8);
            jsonObject = (JSONObject) JSONSerializer.toJSON(jsonContent);

            Iterator<String> keysIterator = statistiques.keys();
            while (keysIterator.hasNext()) {
                String key = keysIterator.next();
                int value1 = statistiques.getInt(key);
                int value2 = jsonObject.getInt(key);
                jsonObject.put(key, value1 + value2);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonObject;
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
        statistique.put("Heure maximal soumis pour une intervention: ",heureMax);
    }


    public static void calculerEtatParClientMax(JSONObject employe, JSONObject statistique) {
        JSONArray clients = employe.getJSONArray("clients");

        double etatParClientMax = 0.0;


        for (int i = 0; i < clients.size(); i++) {
            JSONObject jsonObject = clients.getJSONObject(i);
            String etatParClient = jsonObject.getString("etat_par_client");
            etatParClientMax = Math.max(etatParClientMax, parseDouble(etatParClient.substring(0,etatParClient.length()-1)));
        }

        System.out.println(etatParClientMax);
        statistique.put("l’état par client maximal retourné pour un client: ", etatParClientMax);
    }

    public static void calculerOccurrencesEtatParClient(JSONObject employe, JSONObject statistique) {
        int nbrEtatInf1000 = 0;
        int nbrEtatEntreMinMax = 0;
        int nbrEtatSup10000 = 0;

        JSONArray clients = employe.getJSONArray("clients");

        for (int i = 0; i < clients.size(); i++) {
            JSONObject jsonObject = clients.getJSONObject(i);
            String etatParClient = jsonObject.getString("etat_par_client");
            double etatClient = parseDouble(etatParClient.substring(0, etatParClient.length() - 1));

            // Update occurrence counters based on the value of etatClient
            updateOccurrenceCounters(etatClient, nbrEtatInf1000, nbrEtatEntreMinMax, nbrEtatSup10000);
        }

        // Update the statistics JSONObject with the calculated occurrences
        updateStatistics(statistique, nbrEtatInf1000, nbrEtatEntreMinMax, nbrEtatSup10000);
    }

    private static void updateOccurrenceCounters(double etatClient, int nbrEtatInf1000, int nbrEtatEntreMinMax, int nbrEtatSup10000) {
        if (etatClient < ETAT_PAR_CLIENT_1000) {
            nbrEtatInf1000++;
        } else if (etatClient > ETAT_PAR_CLIENT_1000 && etatClient < ETAT_PAR_CLIENT_10000) {
            nbrEtatEntreMinMax++;
        } else if (etatClient > ETAT_PAR_CLIENT_10000) {
            nbrEtatSup10000++;
        }
    }

    private static void updateStatistics(JSONObject statistique, int nbrEtatInf1000, int nbrEtatEntreMinMax, int nbrEtatSup10000) {
        statistique.put("Le nombre d'etats par client moins que 1000 est de : ", nbrEtatInf1000);
        statistique.put("Le nombre d'etats par client entre 1000 et 10000 est de : ", nbrEtatEntreMinMax);
        statistique.put("Le nombre d'etats par client superieur a 10000 est de : ", nbrEtatSup10000);
    }


    public static void calculerTotalInterventions(JSONObject employe, JSONObject statistique) {


        int totalInterventions = 0;
        JSONArray listeJson = employe.getJSONArray("clients");

        System.out.println(listeJson);

        for (Object obj : listeJson) {
            JSONObject objetJson = (JSONObject) obj;
            if (objetJson.containsKey("code_client")) {
                totalInterventions++;
            }
        }

        statistique.put("Le total d'interventions dans le fichier JSON est : " ,totalInterventions);

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


        statistique.put("Le nombre d'interventions pour les employes de type 0 est de :" ,typeEmploye0);
        statistique.put("Le nombre d'interventions pour les employes de type 1 est de :" ,typeEmploye1);
        statistique.put("Le nombre d'interventions pour les employes de type 2 est de :" , typeEmploye2);

    }


    public static void gestionStatistiques(String option, JSONArray interventions, String json,JSONObject statistiques) {
        System.out.println("Here");
        String nomFichier = "Statistique.json";

        boolean fichierVide = estFichierVide(nomFichier);


        try {
            if (option.equals("-SR"))
                reinitialiserStatistiques(statistiques, nomFichier);
            else if (option.equals("-S"))
                afficherStatistiques(statistiques, fichierVide, nomFichier, interventions, json);
        }catch (IOException e)
        {
            e.getMessage();
        }
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

    static JSONArray interventionsArray(String json) {
        JSONArray interventions = new JSONArray();
        String extracted = "";
        boolean flag = false;
        char ch = 0;

        int i=0;
        while(i < json.length() )
        {
            ch = json.charAt(i);
            if(ch == '[')
                flag = true;

            else if (ch == ']'){
                flag = false;
                extracted += "]";
            }

            if(flag)
                extracted += ch;

            i++;
        }


        interventions = JSONArray.fromObject(extracted.toString());

        return interventions;
    }
}
