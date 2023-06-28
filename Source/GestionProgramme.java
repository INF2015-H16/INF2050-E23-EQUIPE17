package Source;

import java.util.Arrays;

public class GestionProgramme {
    /**
     * Vérifie si un entier `j` est présent dans le tableau `nbrs`.
     *
     * @param nbrs Le tableau d'entiers à vérifier.
     * @param codeClient    L'entier à rechercher.
     * @return `true` si l'entier `j` n'est pas présent dans le tableau `nbrs`, `false` sinon.
     */
    public static boolean verification(int[] nbrs, int codeClient) { //l'argument "j" refere au code client dans le fichier JSON
        for (int i = 0; i < nbrs.length; i++) {
            if (nbrs[i] == codeClient)
                return false;
        }
        return true;
    }

    public static void executer(String[][] data, String argument2) throws JsonException {
        int status, matricule_employe, type_employe, itterations = Integer.parseInt(data[data.length - 1][0]);
        int[] nbrs = new int[30], distance_deplacement = new int[Integer.parseInt(data[data.length - 1][0])],overtime = new int[Integer.parseInt(data[data.length - 1][0])],nombre_heures = new int[Integer.parseInt(data[data.length - 1][0])];
        String[] code = new String[Integer.parseInt(data[data.length - 1][0])];
        Arrays.fill(nbrs, -1);

        // Traitement des données
        for(int i=0,j=4; j< data.length-1;j++, i++)  {
            status = JsonException.checker(data,j,nbrs);
            if(status != -1 && status != -2) {
                code[i] = data[j][0];
                distance_deplacement[i] = JsonException.checkDistance(Integer.parseInt(data[j][1])) + JsonException.checkDistance(Integer.parseInt( data[status][1]));
                overtime[i] = JsonException.checkOvertime(Integer.parseInt(data[j][2])) + JsonException.checkOvertime(Integer.parseInt(data[status][2]));
                nombre_heures[i] = JsonException.checkNombreHeures(Integer.parseInt(data[j][3])) + JsonException.checkNombreHeures(Integer.parseInt(data[status][3]));
            }
            else if(status == -1) {
                code[i] = data[j][0];
                distance_deplacement[i] = JsonException.checkDistance(Integer.parseInt(data[j][1]));
                overtime[i] = JsonException.checkOvertime(Integer.parseInt(data[j][2]));;
                nombre_heures[i] = JsonException.checkNombreHeures(Integer.parseInt(data[j][3]));
            }
        }

        double taux_horaire_min = 0, taux_horaire_max = 0, montantDeplacement = 0, coutVariable, montantRegulier = 0,
                montantHeureSupplementaire = 0;
        double[] EtatParClient = new double[code.length];

        // Récupération des données d'entrée
        matricule_employe = Integer.parseInt(data[0][0]);

        type_employe = JsonException.checkerTypeEmploye(data);
        JsonException.checkerTaux(data, 2);
        JsonException.checkerTaux(data, 3);


        for(int i=0 ; i<itterations; i++)
        {
            if(verification(nbrs,i))
                EtatParClient[i] = CalculEmploye.calculerEtatParClient(type_employe,nombre_heures[i],taux_horaire_min,taux_horaire_max,distance_deplacement[i],overtime[i],montantRegulier);
        }

        for (int i = 0; i < itterations; i++) {
            EtatParClient[i] = CalculEmploye.calculerEtatParClient(type_employe, nombre_heures[i],
                    taux_horaire_min, taux_horaire_max, distance_deplacement[i], overtime[i], montantRegulier);
        }

        double etatCompteTotal = CalculEmploye.calculerEtatCompteTotal(EtatParClient);
        coutVariable = CalculEmploye.calculerCoutVariable(etatCompteTotal);
        double coutFixe = CalculEmploye.calculerCoutFixe(etatCompteTotal);

        GestionJson.formattageFichierSortieJson(matricule_employe, CalculEmploye.arrondirMontant(etatCompteTotal), CalculEmploye.arrondirMontant(coutFixe),
                CalculEmploye.arrondirMontant(coutVariable),code,EtatParClient,itterations, argument2, nbrs);
    }
}
