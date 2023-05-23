import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.*;
public class Main {
    public static void main(String[] args)
    {

        String arugument = "test.json";
        String json="",buffer;


        try (BufferedReader reader = new BufferedReader(new FileReader(arugument))) {

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

        GestionJson.lecture(json);
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
}
