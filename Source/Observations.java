package Source;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Observations {

   public static void observationTaux(double tauxHoraireMax, double tauxHoraireMin, JSONArray observations) {
        if(tauxHoraireMax > 2 * tauxHoraireMin) {
            observations.add("Le taux horaire maximum ne peut pas dépasser deux fois le taux horaire minimum.");
        }
    }

    public static void employeeObservation(String code, int overtime,int distance_deplacement,JSONArray observations) {
        if(distance_deplacement > 50)
            observations.add("Le client " + code + " a une distance de deplacement plus que 50 km");
    }

    static void observationDates(String[][] attributsJson, JSONArray observations) {
        String date1,date2,codeClient;
        int mois1,annee1,annee2,mois2,monthsApart;

        int i=4;
        while(attributsJson[i][4] != null){
            codeClient = attributsJson[i][0];
            date1 = attributsJson[i][4];
            for(int j=i+1; j<attributsJson.length-1 ; j++)
            {
                if(attributsJson[j][0].equals(codeClient))
                {
                    date2 = attributsJson[j][4];
                    mois1 = Integer.parseInt(date1.substring(5, 7));
                    annee1 = Integer.parseInt(date1.substring(0, 4));

                    mois2 = Integer.parseInt(date2.substring(5, 7));
                    annee2 = Integer.parseInt(date2.substring(0, 4));

                    if(annee2 > annee1 && mois2 > mois1)
                        monthsApart = (annee2 - annee1) * 12 + (mois2 - mois1);
                    else if(annee2 > annee1 && mois2 < mois1)
                        monthsApart = (annee2 - annee1) * 12 + (mois1 - mois2);
                    else if(annee2 < annee1 && mois2 > mois1)
                        monthsApart = (annee1 - annee2) * 12 + (mois2 - mois1);
                    else
                        monthsApart = (annee1 - annee2) * 12 + (mois1 - mois2);

                    if(monthsApart >= 6)
                        observations.add("L’écart maximal entre les dates d’intervention (date_intervention) du client " + codeClient +
                                " d’un même employé doit être de moins de 6 mois.");
                }

            }
            i++;
        }
    }

    static JSONObject employeeObservation(JSONObject employee, double cout_variable, double etat_compte,
                                          double cout_fixe, JSONArray observations){
        if(cout_variable > 3000)
            observations.add("Le cout variable payable nécessite des ajustements");
        if(etat_compte > 30000)
            observations.add("L’état de compte total ne doit pas dépasser 30000.00 $.");
        if(cout_fixe > 1500)
            observations.add("Le cout fixe payable ne doit pas dépasser 1500.00 $.");
        return employee;
    }

    static void etatClientObservation(JSONArray observation, JSONObject employee, double etat_par_client, String code) {
        if(etat_par_client > 15000)
            observation.add("L’état par client du client " + code + " est trop dispendieuse.");
    }
}
