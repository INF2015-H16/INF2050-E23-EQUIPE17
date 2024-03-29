package Source;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class GestionProgramme {

    public static boolean verificationCodeClient(int[] nbrs, int codeClient) {

        for (int nbr : nbrs) {
            if (nbr == codeClient)
                return false;
        }
        return true;
    }

    public static void executerRecuperationInterventions(String[][] donnees, String argument2, String json,
                                                         JSONArray observations, String option, JSONArray interventions)
            throws JsonException {

        int itterations = Integer.parseInt(donnees[donnees.length - 1][0]);
        int[] nbrs = new int[30], distance_deplacement = new int[Integer.parseInt(donnees[donnees.length - 1][0])],
                overtime = new int[Integer.parseInt(donnees[donnees.length - 1][0])],
                nombre_heures = new int[Integer.parseInt(donnees[donnees.length - 1][0])];
        String[] code = new String[Integer.parseInt(donnees[donnees.length - 1][0])];
        double tauxHoraireMin = 0, taux_horaire_max = 0, montantRegulier = 0;
        double[] etatParClient = new double[code.length];
        Arrays.fill(nbrs, -1);

        recuperationInterventions(donnees, argument2, itterations, nbrs, distance_deplacement, overtime, nombre_heures,
                code, tauxHoraireMin, taux_horaire_max, montantRegulier, etatParClient,observations, option,
                interventions,json); // Traitement des données
    }

    private static void recuperationInterventions(String[][] donnees, String argument2, int itterations, int[] nbrs,
                                                  int[] distance_deplacement, int[] overtime, int[] nombre_heures,
                                                  String[] code, double taux_horaire_min, double taux_horaire_max,
                                                  double montantRegulier, double[] EtatParClient,
                                                  JSONArray observations, String option, JSONArray interventions,
                                                  String json) throws JsonException {
        int  status;

        for(int i = 0, j = 4; j< donnees.length-1; j++, i++)  {

            status = JsonException.validation(donnees,j);

            if(status != -1 && !donnees[j][0].equals("#"))
                sousMethodeInterventions2(donnees, distance_deplacement, overtime, nombre_heures, code, status, i, j,
                        observations);

            else if(!(donnees[j][0].equals("#")) && !donnees[j][0].equals(""))
            {
                sousMethodeInterventions1(distance_deplacement, overtime, nombre_heures, code, i, donnees[j],
                        observations);
            }
        }
        recuperationInfoEmploye(donnees, argument2, itterations, nbrs, distance_deplacement, overtime, nombre_heures,
                code, taux_horaire_min, taux_horaire_max, montantRegulier, EtatParClient,observations, option,
                interventions,json, json);
    }

    private static void sousMethodeInterventions1(int[] distance_deplacement, int[] overtime, int[] nombre_heures,
                                                  String[] code, int i, String[] donnees,JSONArray observations)
            throws JsonException {

        code[i] = donnees[0];
        distance_deplacement[i] = JsonException.validerDistance(Integer.parseInt(donnees[1]));
        overtime[i] = JsonException.validerOvertime(Integer.parseInt(donnees[2]));
        nombre_heures[i] = JsonException.validerNombreHeures(Integer.parseInt(donnees[3]));
        Observations.employeeObservation(code[i] ,overtime[i],distance_deplacement[i],observations);
    }


    private static void sousMethodeInterventions2(String[][] donnees, int[] distance_deplacement, int[] overtime,
                                                  int[] nombre_heures, String[] code, int status, int i, int j,
                                                  JSONArray observations) throws JsonException {
        code[i] = donnees[j][0];
        distance_deplacement[i] = JsonException.validerDistance(Integer.parseInt(donnees[j][1])) +
                JsonException.validerDistance(Integer.parseInt( donnees[status][1]));

        overtime[i] = JsonException.validerOvertime(Integer.parseInt(donnees[j][2])) +
                JsonException.validerOvertime(Integer.parseInt(donnees[status][2]));

        nombre_heures[i] = JsonException.validerNombreHeures(Integer.parseInt(donnees[j][3])) +
                JsonException.validerNombreHeures(Integer.parseInt(donnees[status][3]));

        donnees[status][0] = "#";
        donnees[status][1] = "-200";

        Observations.employeeObservation(code[i] ,overtime[i],distance_deplacement[i],observations);
    }

    private static void recuperationInfoEmploye(String[][] donnees, String argument2, int itterations, int[] nbrs,
                                                int[] distance_deplacement, int[] overtime, int[] nombre_heures,
                                                String[] code, double taux_horaire_min, double taux_horaire_max,
                                                double montantRegulier, double[] EtatParClient, JSONArray observations,
                                                String option, JSONArray interventions, String json, String fichierEntree)
            throws JsonException {

        int type_employe, matricule_employe;
        matricule_employe = Integer.parseInt(donnees[0][0]);        // Récupération des données d'entrée
        type_employe = JsonException.validerTypeEmploye(donnees);
        taux_horaire_min = JsonException.validerTaux(donnees, 2);
        taux_horaire_max = JsonException.validerTaux(donnees, 3);
        Observations.observationTaux(taux_horaire_max,taux_horaire_min,observations);

        CalculEmploye.calculEtatClient(argument2, itterations, nbrs, distance_deplacement, overtime, nombre_heures, code,
                taux_horaire_min, taux_horaire_max, EtatParClient, type_employe, matricule_employe,observations,
                option,interventions,json, fichierEntree);
    }


    public static void ajouterMessage(String message, String arg) throws IOException {
        JSONObject messageObjet = new JSONObject();
        messageObjet.accumulate("message", message);
        FileUtils.writeStringToFile(new File(arg), messageObjet.toString(), "UTF-8");
        System.exit(0);
    }
}
