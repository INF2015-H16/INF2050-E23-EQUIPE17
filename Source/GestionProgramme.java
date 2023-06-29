package Source;

import java.util.Arrays;

public class GestionProgramme {

    /**
     * Vérifie si un codeClient est présent dans le tableau `nbrs`.
     *
     * @param nbrs Le tableau d'entiers à vérifier.
     * @param codeClient    L'entier à rechercher.
     * @return `true` si le codeClient n'est pas présent dans le tableau `nbrs`, `false` sinon.
     */
    public static boolean verification(int[] nbrs, int codeClient) {
        for (int nbr : nbrs) {
            if (nbr == codeClient)
                return false;
        }
        return true;
    }

    public static void executer(String[][] donnees, String argument2, String json) throws JsonException {
        int itterations = Integer.parseInt(donnees[donnees.length - 1][0]);
        int[] nbrs = new int[30], distance_deplacement = new int[Integer.parseInt(donnees[donnees.length - 1][0])],overtime = new int[Integer.parseInt(donnees[donnees.length - 1][0])],nombre_heures = new int[Integer.parseInt(donnees[donnees.length - 1][0])];
        String[] code = new String[Integer.parseInt(donnees[donnees.length - 1][0])];
        double taux_horaire_min = 0, taux_horaire_max = 0, montantRegulier = 0;
        double[] EtatParClient = new double[code.length];
        Arrays.fill(nbrs, -1);
        recuperationInterventions(donnees, argument2, itterations, nbrs, distance_deplacement, overtime, nombre_heures, code, taux_horaire_min, taux_horaire_max, montantRegulier, EtatParClient); // Traitement des données
    }

    private static void recuperationInterventions(String[][] donnees, String argument2, int itterations, int[] nbrs, int[] distance_deplacement, int[] overtime, int[] nombre_heures, String[] code, double taux_horaire_min, double taux_horaire_max, double montantRegulier, double[] EtatParClient) throws JsonException {
        int  status;
        for(int i = 0, j = 4; j< donnees.length-1; j++, i++)  {
            status = JsonException.checker(donnees,j, nbrs);
            if(status != -1 && status != -2)
                sousMethodeInterventions2(donnees, distance_deplacement, overtime, nombre_heures, code, status, i, j);
            else if(status == -1)
                sousMethodeInterventions1(distance_deplacement, overtime, nombre_heures, code, i, donnees[j]);
        }
        recuperationInfoEmploye(donnees, argument2, itterations, nbrs, distance_deplacement, overtime, nombre_heures, code, taux_horaire_min, taux_horaire_max, montantRegulier, EtatParClient);
    }

    private static void sousMethodeInterventions1(int[] distance_deplacement, int[] overtime, int[] nombre_heures, String[] code, int i, String[] donnees) throws JsonException {
        code[i] = donnees[0];
        distance_deplacement[i] = JsonException.checkDistance(Integer.parseInt(donnees[1]));
        overtime[i] = JsonException.checkOvertime(Integer.parseInt(donnees[2]));
        nombre_heures[i] = JsonException.checkNombreHeures(Integer.parseInt(donnees[3]));
    }

    private static void sousMethodeInterventions2(String[][] donnees, int[] distance_deplacement, int[] overtime, int[] nombre_heures, String[] code, int status, int i, int j) throws JsonException {
        code[i] = donnees[j][0];
        distance_deplacement[i] = JsonException.checkDistance(Integer.parseInt(donnees[j][1])) + JsonException.checkDistance(Integer.parseInt( donnees[status][1]));
        overtime[i] = JsonException.checkOvertime(Integer.parseInt(donnees[j][2])) + JsonException.checkOvertime(Integer.parseInt(donnees[status][2]));
        nombre_heures[i] = JsonException.checkNombreHeures(Integer.parseInt(donnees[j][3])) + JsonException.checkNombreHeures(Integer.parseInt(donnees[status][3]));
    }

    private static void recuperationInfoEmploye(String[][] donnees, String argument2, int itterations, int[] nbrs, int[] distance_deplacement, int[] overtime, int[] nombre_heures, String[] code, double taux_horaire_min, double taux_horaire_max, double montantRegulier, double[] EtatParClient) throws JsonException {
        int type_employe, matricule_employe;
        matricule_employe = Integer.parseInt(donnees[0][0]);        // Récupération des données d'entrée
        type_employe = JsonException.checkerTypeEmploye(donnees);
        JsonException.checkerTaux(donnees, 2);
        JsonException.checkerTaux(donnees, 3);
        calculEtatClient(argument2, itterations, nbrs, distance_deplacement, overtime, nombre_heures, code, taux_horaire_min, taux_horaire_max, montantRegulier, EtatParClient, type_employe, matricule_employe);
    }

    private static void calculEtatClient(String argument2, int itterations, int[] nbrs, int[] distance_deplacement, int[] overtime, int[] nombre_heures, String[] code, double taux_horaire_min, double taux_horaire_max, double montantRegulier, double[] EtatParClient, int type_employe, int matricule_employe) throws JsonException {
        for(int i = 0; i< itterations; i++) {
            if(verification(nbrs,i))
                EtatParClient[i] = CalculEmploye.calculerEtatParClient(type_employe, nombre_heures[i], taux_horaire_min, taux_horaire_max, distance_deplacement[i], overtime[i], montantRegulier);
        }
        for (int i = 0; i < itterations; i++) {
            EtatParClient[i] = CalculEmploye.calculerEtatParClient(type_employe, nombre_heures[i],
                    taux_horaire_min, taux_horaire_max, distance_deplacement[i], overtime[i], montantRegulier);
        }
        calculCouts(argument2, itterations, nbrs, code, EtatParClient, matricule_employe);
    }

    private static void calculCouts(String argument2, int itterations, int[] nbrs, String[] code, double[] EtatParClient, int matricule_employe) {
        double etatCompteTotal = CalculEmploye.calculerEtatCompteTotal(EtatParClient);
        double coutVariable = CalculEmploye.calculerCoutVariable(etatCompteTotal);
        double coutFixe = CalculEmploye.calculerCoutFixe(etatCompteTotal);
        GestionJson.formattageFichierSortieJson(matricule_employe, CalculEmploye.arrondirMontant(etatCompteTotal), CalculEmploye.arrondirMontant(coutFixe),
                CalculEmploye.arrondirMontant(coutVariable), code, EtatParClient, itterations, argument2, nbrs);
    }
}
