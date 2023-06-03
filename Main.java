import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.*;
import java.util.Arrays;

public class Main {
    public static void main(String[] args)
    {
        String[][] donnees;
        String argument = "test.json";
        String json="",buffer;

        try (BufferedReader reader = new BufferedReader(new FileReader(argument))) {

            while ((buffer =reader.readLine()) != null)
            {
                if(buffer != null)
                    json += buffer;
                json += "\n";

            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        donnees = GestionJson.lecture(json);

        executer(donnees);
    }

    public static void executer(String[][] data)
    {
        int status,matricule_employe,type_employe,itterations=Integer.parseInt(data[data.length-1][0]);
        int [] distance_deplacement = new int[Integer.parseInt(data[data.length-1][0])],
                overtime = new int[Integer.parseInt(data[data.length-1][0])],
                nombre_heures = new int[Integer.parseInt(data[data.length-1][0])];
        double taux_horaire_min,taux_horaire_max, montantDeplacement = 0, coutVariable, montantRegulier = 0, montantHeureSupplementaire = 0;
        String[] code = new String[Integer.parseInt(data[data.length-1][0])];
        double[] EtatParClient = new double[code.length];

        matricule_employe = Integer.parseInt(data[0][0]);
        type_employe = Integer.parseInt(data[1][0]);
        taux_horaire_min = Double.parseDouble(data[2][0].substring(0,5));
        taux_horaire_max = Double.parseDouble(data[3][0].substring(0,5));

        for(int i=0,j=4; j< data.length-1;j++, i++)
        {
            status = checker(data,j);
            if(status != -1)
            {
                System.out.println(data[j][0] + " " + i);
                code[i] = data[j][0];
                distance_deplacement[i] = Integer.parseInt(data[j][1] + data[status][1]);
                overtime[i] = Integer.parseInt(data[j][2] + data[status][2]);
                nombre_heures[i] = Integer.parseInt(data[j][3] + data[status][3]);// i am not sure of this
                itterations--;
            }
            else //if(data[j][0] != null)
            {
                System.out.println(data[j][0] +" " + i);
                code[i] = data[j][0];
                distance_deplacement[i] = Integer.parseInt(data[j][1]);
                overtime[i] = Integer.parseInt(data[j][2]);
                nombre_heures[i] = Integer.parseInt(data[j][3]);
            }
        }


        for(int i=0 ; i<code.length ; i++)
        {
            montantRegulier += calculerMontantRegulier(type_employe,nombre_heures[i],taux_horaire_min,taux_horaire_max);
            montantDeplacement += calculerMontantDeplacement(type_employe,distance_deplacement[i], montantRegulier);
            montantHeureSupplementaire += calculerMontantHeuresSupplementaires(type_employe,overtime[i]);
            EtatParClient[i] = calculerEtatParClient(type_employe,nombre_heures[i],taux_horaire_min,taux_horaire_max,distance_deplacement[i],overtime[i],montantRegulier);
        }

        double etatCompteTotal = calculerEtatCompteTotal(EtatParClient);
        coutVariable = calculerCoutVariable(etatCompteTotal);
        double coutFixe = calculerCoutFixe(etatCompteTotal);


        for(int i=0 ; i<EtatParClient.length ; i++)
            EtatParClient[i] = arrondirMontant(EtatParClient[i]);

        GestionJson.ecriture(matricule_employe,arrondirMontant(etatCompteTotal),arrondirMontant(coutFixe),arrondirMontant(coutVariable),code,EtatParClient,itterations);

    }



    public static int checker(String[][] array,int z)
    {
        for (int i = z+1; i < array.length - 1; i++)
        {
            System.out.println(i + " " + z);
                if (array[z][0].equals( array[i][0])) ///wait to be continued
                {
                    return i;
                }
        }
        return -1;
    }

    /**
     * 4. Cette méthode calculera le montant régulier en fonction du type d'employé, du nombre d'heures travaillées et des taux horaires min et max.
     *
     * @params typeEmploye,
     * @params nombreHeures
     * @params tauxHoraireMin
     * @params tauxHoraireMax
     */
    public static double calculerMontantRegulier(int typeEmploye, double nombreHeures, double tauxHoraireMin, double tauxHoraireMax) {
        double tauxHoraire = 0;

        //Selectionner type d'employer
        if (typeEmploye == 0){
            tauxHoraire = tauxHoraireMin;
        } else if (typeEmploye == 1) {
            tauxHoraire = (tauxHoraireMin + tauxHoraireMax)/2;
        } else if (typeEmploye == 2) {
            tauxHoraire = tauxHoraireMax;
        }

        // Calculer le montant régulier en multipliant le taux horaire par le nombre d'heures
        double montantRegulier = tauxHoraire * nombreHeures;

        return montantRegulier;
    }

    /**
     * 5. Cette méthode calculera le montant pour les heures supplémentaires en fonction du type d'employé et du nombre d'heures supplémentaires.
     * @param typeEmploye
     * @param distanceDeplacement
     * @param montantRegulier
     * @return
     */
    public static double calculerMontantDeplacement(int typeEmploye, double distanceDeplacement, double montantRegulier){
        double montantDeplacement = 0;

        // Calculer le montant de déplacement en fonction du type d'employé
        if (typeEmploye == 0){
            montantDeplacement = 200 - (distanceDeplacement * (0.05 * montantRegulier));
        } else if (typeEmploye == 1) {
            montantDeplacement = 200 - (distanceDeplacement * (0.10 * montantRegulier));
        } else if (typeEmploye == 2) {
            montantDeplacement = 200 - (distanceDeplacement * (0.15 * montantRegulier));
        }

        return montantDeplacement;
    }

    /**
     * 6. Cette méthode calculera le montant pour les heures supplémentaires en fonction du type d'employé et du nombre d'heures supplémentaires.
     * @param typeEmploye
     * @param overtime
     * @return
     */
    public static double calculerMontantHeuresSupplementaires(int typeEmploye, double overtime) {
        double montantHeuresSupplementaires = 0.0;

        if (typeEmploye == 0) {
            // Superviseur : Pas de montant pour les heures supplémentaires
            montantHeuresSupplementaires = 0.0;
        } else if (typeEmploye == 1) {
            // Permanent
            if (overtime > 4 && overtime <= 8) {
                montantHeuresSupplementaires = 50.0 * overtime;
            } else if (overtime > 8) {
                montantHeuresSupplementaires = 100.0 * overtime;
            }
        } else if (typeEmploye == 2) {
            // Contractuel
            if (overtime <= 4) {
                montantHeuresSupplementaires = 75.0 * overtime;
            } else if (overtime > 4) {
                montantHeuresSupplementaires = 150.0 * overtime;
            }
        }

        // Limiter le montant des heures supplémentaires à 1500.00
        montantHeuresSupplementaires = Math.min(montantHeuresSupplementaires, 1500.0);

        return montantHeuresSupplementaires;
    }


    /**
     * 7. Cette méthode arrondira un montant au 5 sous supérieur.
     * @param montant
     * @return
     */
    public static double arrondirMontant(double montant) {
        // On multiplie par 20 pour déplacer la décimale au deuxième chiffre après la virgule
        return Math.ceil(montant * 20) / 20;
    }

    /**
     * 8. Cette méthode calculera l'état par client en utilisant les méthodes de calcul précédentes.
     * @param typeEmploye
     * @param nombreHeures
     * @param tauxHoraireMin
     * @param tauxHoraireMax
     * @param distanceDeplacement
     * @param overtime
     * @param montantregulier
     * @return
     */
    public static double calculerEtatParClient(int typeEmploye, double nombreHeures,
                                               double tauxHoraireMin, double tauxHoraireMax,
                                               double distanceDeplacement, double overtime, double montantregulier) {
        double montantTotal = 0.0;

        // Calcul du montant des heures travaillées
        double montantHeuresTravaillees = calculerMontantRegulier(typeEmploye, nombreHeures,
                tauxHoraireMin, tauxHoraireMax);
        montantTotal += montantHeuresTravaillees;

        // Calcul du montant des heures supplémentaires
        double montantHeuresSupplementaires = calculerMontantHeuresSupplementaires(typeEmploye, overtime);
        montantTotal += montantHeuresSupplementaires;

        // Calcul du montant de déplacement
        double montantDeplacement = calculerMontantDeplacement(typeEmploye, distanceDeplacement, montantregulier);
        montantTotal += montantDeplacement;

        // Calcul du montant total en fonction du type d'employé
        if (typeEmploye == 1) {
            // Type d'employé 1 - Ajouter un montant fixe
            montantTotal += 733.77;
        } else if (typeEmploye == 2) {
            // Type d'employé 2 - Pas de modification du montant total
        }
        return montantTotal;
    }

    /**
     * 9. Cette méthode calculera l'état de compte total en sommant les états par client.
     * @param etatsParClient
     * @return
     */
    public static double calculerEtatCompteTotal(double[] etatsParClient) {
        double etatCompteTotal = 0.0;

        for (double etatParClient : etatsParClient) {
            etatCompteTotal += etatParClient;
        }

        return etatCompteTotal;
    }

    /**
     * 10. Cette méthode calculera le coût fixe en fonction de l'état de compte total.
     * @param etatCompteTotal
     * @return
     */
    public static double calculerCoutFixe(double etatCompteTotal) {
        double coutFixe;

        // Calcul du coût fixe en fonction de l'État de compte total
        if (etatCompteTotal >= 1000.0) {
            coutFixe = etatCompteTotal * 0.012;
        } else if (etatCompteTotal >= 500.0) {
            coutFixe = etatCompteTotal * 0.008;
        } else {
            coutFixe = etatCompteTotal * 0.004;
        }

        // Arrondir le coût fixe au 5 sous supérieur
        coutFixe = arrondirMontant(coutFixe);

        return coutFixe;
    }

    /**
     * 11. Calcule le coût variable en fonction de l'état total du compte.
     *
     * @param etatCompteTotal   L'état total du compte pour lequel le coût variable doit être calculé.
     * @return                  Le coût variable calculé.
     */
    public static double calculerCoutVariable(double etatCompteTotal) {
        double coutVar = (2.5/100 * etatCompteTotal);
        return Math.ceil(coutVar * 20) / 20;
    }

}



    