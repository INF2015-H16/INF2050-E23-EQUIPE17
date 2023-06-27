package Source;

import java.io.*;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        CalculEmploye calculEmploye = new CalculEmploye();

        String[][] donnees;
        String argument = "test.json";
        String argument2 = "sortie.json";
        String json = "";

        String buffer;

        try (BufferedReader reader = new BufferedReader(new FileReader(argument))) {
            // Lecture du contenu du fichier JSON ligne par ligne
            while ((buffer = reader.readLine()) != null) {
                if (buffer != null)
                    json += buffer;
                json += "\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Conversion du contenu JSON en tableau de données
        donnees = GestionJson.lireFichierEntreeJson(json);

        try {
            executer(donnees, argument2);
        } catch (JsonException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Vérifie si un entier `j` est présent dans le tableau `nbrs`.
     *
     * @param nbrs Le tableau d'entiers à vérifier.
     * @param j    L'entier à rechercher.
     * @return `true` si l'entier `j` n'est pas présent dans le tableau `nbrs`, `false` sinon.
     */
    public static boolean verification(int[] nbrs, int j) { //l'argument "j" refere au code client dans le fichier JSON
        for (int i = 0; i < nbrs.length; i++) {
            if (nbrs[i] == j)
                return false;
        }
        return true;
    }


    public static int checkDistance(int nbr) throws JsonException
    {
        if (nbr < 0 || nbr > 100) {
            throw new JsonException("Distance deplacement invalide");
        }
        return nbr;
    }

    public static int checkOvertime(int nbr) throws JsonException
    {
        if (nbr < 0 || nbr > 4) {
            throw new JsonException("Overtime invalide");
        }

        return nbr;
    }

    public static int checkNombreHeures(int nbr) throws JsonException
    {
        if (nbr < 0  || nbr > 8) {
            throw new JsonException("Nombre d'heures invalide");
        }

        return nbr;
    }

    public static double checkerTaux(String[][] data,int i) throws JsonException
    {
        double taux_horaire;
        try {
            taux_horaire = Double.parseDouble(data[i][0].substring(0, 5));
        }
        catch (Exception e)
        {
            taux_horaire = Double.parseDouble(data[i][0].replace(",", ".").replace("$", ""));// Sa c'est pour gerer le virgule
        }

        if(taux_horaire < 0)
            throw new JsonException("Taux horaire invalide");

        return taux_horaire;
    }

    public static void checkerTypeEmploye(int type) throws JsonException {
        if (type < 0 || type > 2) {
            throw new JsonException("Type d'employé invalide");
        }
    }

    public static void validerNombreInterventions(int nombre) throws JsonException {
        if (nombre > 10) {
            throw new JsonException("Nombre d'interventions invalide");
        }
    }

    //TODO: rendre la methode executer plus courte en creant des sous methodes

    public static void executer(String[][] data, String argument2) throws JsonException {
        int[] nbrs = new int[30];
        Arrays.fill(nbrs, -1);
        int status, matricule_employe, type_employe, itterations = Integer.parseInt(data[data.length - 1][0]);
        int[] distance_deplacement = new int[Integer.parseInt(data[data.length - 1][0])];
        int[] overtime = new int[Integer.parseInt(data[data.length - 1][0])];
        int[] nombre_heures = new int[Integer.parseInt(data[data.length - 1][0])];
        String[] code = new String[Integer.parseInt(data[data.length - 1][0])];



        // Traitement des données
        for(int i=0,j=4; j< data.length-1;j++, i++)
        {
            status = checker(data,j,nbrs);
            if(status != -1 && status != -2)
            {
                code[i] = data[j][0];
                try
                {
                    checkDistance(Integer.parseInt(data[j][1]));
                    checkOvertime(Integer.parseInt(data[j][2]));
                    checkNombreHeures(Integer.parseInt(data[j][3]));
                }
                catch (JsonException e)
                {
                    System.out.println(e.getMessage());
                    System.exit(1);
                }

                distance_deplacement[i] = Integer.parseInt(data[j][1]) +Integer.parseInt( data[status][1]);
                overtime[i] = Integer.parseInt(data[j][2])  + Integer.parseInt(data[status][2]);
                nombre_heures[i] = Integer.parseInt(data[j][3]) + Integer.parseInt(data[status][3]);// i am not sure of this
            }
            else if(status == -1)
            {
                code[i] = data[j][0];
                try
                {
                    checkDistance(Integer.parseInt(data[j][1]));
                    checkOvertime(Integer.parseInt(data[j][2]));
                    checkNombreHeures(Integer.parseInt(data[j][3]));
                }
                catch (JsonException e)
                {
                    System.out.println(e.getMessage());
                    System.exit(1);
                }
                distance_deplacement[i] = Integer.parseInt(data[j][1]);
                overtime[i] = Integer.parseInt(data[j][2]);
                nombre_heures[i] = Integer.parseInt(data[j][3]);
            }
        }

        double taux_horaire_min = 0, taux_horaire_max = 0, montantDeplacement = 0, coutVariable, montantRegulier = 0,
                montantHeureSupplementaire = 0;
        double[] EtatParClient = new double[code.length];

        // Récupération des données d'entrée
        matricule_employe = Integer.parseInt(data[0][0]);
        type_employe = Integer.parseInt(data[1][0]);

        try {
            checkerTaux(data, 2);
        }
        catch (JsonException e)
        {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        try {
            checkerTaux(data, 3);
        }
        catch (JsonException e)
        {
            System.out.println(e.getMessage());
            System.exit(1);
        }


        for(int i=0 ; i<itterations; i++)
        {
            if(verification(nbrs,i))
                EtatParClient[i] = calculerEtatParClient(type_employe,nombre_heures[i],taux_horaire_min,taux_horaire_max,distance_deplacement[i],overtime[i],montantRegulier);
        }

        for (int i = 0; i < itterations; i++) {
            EtatParClient[i] = calculerEtatParClient(type_employe, nombre_heures[i],
                    taux_horaire_min, taux_horaire_max, distance_deplacement[i], overtime[i], montantRegulier);
        }

        double etatCompteTotal = calculerEtatCompteTotal(EtatParClient);
        coutVariable = calculerCoutVariable(etatCompteTotal);
        double coutFixe = calculerCoutFixe(etatCompteTotal);

        GestionJson.ecrireFichierSortieJson(matricule_employe, arrondirMontant(etatCompteTotal), arrondirMontant(coutFixe),
                arrondirMontant(coutVariable),code,EtatParClient,itterations, argument2, nbrs);
    }


    /**
     * Vérifie s'il existe une occurrence précédente du code client dans le tableau à deux dimensions.
     * Si une occurrence est trouvée, elle met à jour le tableau `nbr` avec l'index de l'occurrence précédente.
     *
     * @param array Le tableau à deux dimensions contenant les données.
     * @param z     L'index actuel dans le tableau.
     * @param nbr   Le tableau de numéros de référence pour les occurrences précédentes.
     * @return L'index de l'occurrence précédente si elle existe, -1 si aucune occurrence précédente n'est trouvée,
     * -2 si une occurrence est trouvée mais son index est inférieur à l'index actuel.
     */
    public static int checker(String[][] array, int z, int[] nbr) {
        for (int i = 0; i < array.length - 1; i++) {
            if (i == z)
                i++;

            // Vérifie si le code client à l'index z est égal au code client à l'index i
            if (array[z][0].equals(array[i][0])) {
                if (i < z)
                    return -2; // Une occurrence précédente a été trouvée, mais son index est inférieur à l'index actuel
                else {
                    nbr[z] = i - 4; // Met à jour le tableau de numéros de référence avec l'index de l'occurrence précédente
                    return i; // Retourne l'index de l'occurrence précédente
                }
            }
        }
        return -1; // Aucune occurrence précédente n'a été trouvée

        //TODO: enlever le return -1 et la remplacer par un try catch statement
    }



    //TODO: ajouter une methode qui check l'occurence



    /**
     * Cette méthode calculera le montant régulier en fonction du type d'employé,
     * du nombre d'heures travaillées et des taux horaires min et max.
     *
     * @params typeEmploye,
     * @params nombreHeures
     * @params tauxHoraireMin
     * @params tauxHoraireMax
     */
    public static double calculerMontantRegulier(int typeEmploye, double nombreHeures,
                                                 double tauxHoraireMin, double tauxHoraireMax) {


        double tauxHoraire = 0;


        if (typeEmploye == 0){
            tauxHoraire = tauxHoraireMin;
        } else if (typeEmploye == 1) {
            tauxHoraire = (tauxHoraireMin + tauxHoraireMax)/2;
        } else if (typeEmploye == 2) {
            tauxHoraire = tauxHoraireMax;
        }

        double montantRegulier = tauxHoraire * nombreHeures;

        return montantRegulier;
    }

    /**
     * Cette méthode calculera le montant pour les heures supplémentaires en fonction du
     * type d'employé et du nombre d'heures supplémentaires.
     * @param typeEmploye
     * @param distanceDeplacement
     * @param montantRegulier
     * @return
     */
    public static double calculerMontantDeplacement(int typeEmploye, double distanceDeplacement,
                                                    double montantRegulier){
        if (montantRegulier < 0){
            throw new IllegalArgumentException("La valeur n'est pas valide.");
        }       //come  back here

        double montantDeplacement = 0;

        if (typeEmploye == 0){
            montantDeplacement = 200 - (distanceDeplacement * (0.05 * montantRegulier)) ;
        } else if (typeEmploye == 1) {
            montantDeplacement = 200 - (distanceDeplacement * (0.10 * montantRegulier));
        } else if (typeEmploye == 2) {
            montantDeplacement = 200 - (distanceDeplacement * (0.15 * montantRegulier));
        } else {
            throw new IllegalArgumentException("La valeur n'est pas valide.");
        }

        return montantDeplacement;
    }

    /**
     * Cette méthode calculera le montant pour les heures supplémentaires en fonction
     * du type d'employé et du nombre d'heures supplémentaires.
     * @param typeEmploye
     * @param overtime
     * @return
     */
    public static double calculerMontantHeuresSupplementaires(int typeEmploye, double overtime,double nombre_heures) {
        double montantHeuresSupplementaires = 0.0;

        if (typeEmploye == 0) {
            montantHeuresSupplementaires = 0.0;
        } else if (typeEmploye == 1) {
            if (nombre_heures > 4 && nombre_heures <= 8) {
                montantHeuresSupplementaires = 50.0 * overtime;
            } else if (nombre_heures > 8) {
                montantHeuresSupplementaires = 100.0 * overtime;
            }
        } else if (typeEmploye == 2) {
            if (overtime <= 4)
            {
                montantHeuresSupplementaires = 75.0 * overtime;
            }

            else if (overtime > 4)
            {
                montantHeuresSupplementaires = 150.0 * overtime;
            }
        } else {
            throw new IllegalArgumentException("Type d'employer n'est pas valide.");
        }

        montantHeuresSupplementaires = Math.min(montantHeuresSupplementaires, 1500.0);

        return montantHeuresSupplementaires;
    }

    /**
     * Calcule le coût variable en fonction de l'état total du compte.
     *
     * @param etatCompteTotal   L'état total du compte pour lequel le coût variable doit être calculé.
     * @return                  Le coût variable calculé.
     */
    public static double calculerCoutVariable(double etatCompteTotal) {
        double coutVar = (2.5/100 * etatCompteTotal);
        coutVar = arrondirMontant(coutVar);
        return coutVar;
    }

    public static double arrondirMontant(double montant) {
        double arrondi = Math.ceil(montant * 20) / 20; // Arrondi à 2 décimales
        double difference = arrondi - Math.floor(arrondi); // Partie décimale
        if (difference < 0.05) {
            return Math.floor(arrondi * 20) / 20; // Arrondi au multiple inférieur de 0,05
        } else {
            return Math.ceil(arrondi * 20) / 20; // Arrondi au multiple supérieur de 0,05
        }
    }

    public static double calculerEtatParClient(int typeEmploye, double nombreHeures,
                                               double tauxHoraireMin, double tauxHoraireMax,
                                               double distanceDeplacement, double overtime, double montantregulier) {
        double montantTotal = 0.0;

        double montantHeuresTravaillees = calculerMontantRegulier(typeEmploye, nombreHeures,
                tauxHoraireMin, tauxHoraireMax);

        montantTotal += montantHeuresTravaillees;

        double montantHeuresSupplementaires = calculerMontantHeuresSupplementaires(typeEmploye, overtime, nombreHeures);
        montantTotal += montantHeuresSupplementaires;

        double montantDeplacement = calculerMontantDeplacement(typeEmploye, distanceDeplacement, montantregulier);
        montantTotal += montantDeplacement;

        montantTotal = arrondirMontant(montantTotal);

        return montantTotal;
    }
    public static double calculerEtatCompteTotal(double[] etatsParClient) {
        double etatCompteTotal = 0.0;

        for (double etatParClient : etatsParClient) {
            etatCompteTotal += etatParClient;
        }

        return etatCompteTotal;
    }

    /**
     * Calcule le coût fixe en fonction de l'état du compte total.
     *
     * @param etatCompteTotal l'état du compte total
     * @return le coût fixe calculé
     */
    public static double calculerCoutFixe(double etatCompteTotal) {
        double coutFixe;

        if (etatCompteTotal >= 1000.0) {
            coutFixe = etatCompteTotal * 0.012;
        }
        else if (etatCompteTotal >= 500.0) {
            coutFixe = etatCompteTotal * 0.008;
        }
        else {
            coutFixe = etatCompteTotal * 0.004;
        }

        coutFixe = arrondirMontant(coutFixe);

        return coutFixe;
    }
}