package Source;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestObservations {

    @Test
    public void testObservationTauxWithinLimits() {
        JSONArray observations = new JSONArray();
        Observations.observationTaux(2.0, 2.45, observations);
        assertEquals(0, observations.size());
    }

    @Test
    public void testObservationTauxExceedsLimit() {
        JSONArray observations = new JSONArray();
        Observations.observationTaux(2.1, 2.45, observations);
        assertEquals(1, observations.size());
        assertEquals("Le taux horaire maximum ne peut pas dépasser deux fois le taux horaire minimum.", observations.get(0));
    }

    @Test
    public void testEmployeeObservationDistanceWithinLimit() {
        JSONArray observations = new JSONArray();
        Observations.employeeObservation("C123", 0, 49, observations);
        assertEquals(0, observations.size());
    }

    @Test
    public void testEmployeeObservationDistanceExceedsLimit() {
        JSONArray observations = new JSONArray();
        Observations.employeeObservation("C789", 0, 51, observations);
        assertEquals(1, observations.size());
        assertEquals("Le client C789 a une distance de deplacement plus que 50 km", observations.get(0));
    }

    @Test
    public void testObservationDates() {
        JSONArray observations = new JSONArray();
        String[][] attributsJson = {
                {"C123", "...", "2023-07-14"},
                {"C456", "...", "2023-03-15"},
                {"C789", "...", "2023-03-20"},
                {"C123", "...", "2023-01-20"},
                {"C789", "...", "2023-12-10"}
        };
        Observations.observationDates(attributsJson, observations);
        assertEquals(1, observations.size());
        assertEquals("L’écart maximal entre les dates d’intervention (date_intervention) du client C789 d’un même employé doit être de moins de 6 mois.", observations.get(0));
    }

    @Test
    public void testEmployeeObservationCoutWithinLimits() {
        JSONArray observations = new JSONArray();
        JSONObject employee = new JSONObject();
        Observations.employeeObservation(employee, 0.0, 0.0, 0.0, observations);
        assertEquals(0, observations.size());
    }

    @Test
    public void testEmployeeObservationCoutExceedsLimits() {
        JSONArray observations = new JSONArray();
        JSONObject employee = new JSONObject();
        Observations.employeeObservation(employee, 3500.0, 35000.0, 1600.0, observations);
        assertEquals(3, observations.size());
        assertEquals("Le cout variable payable nécessite des ajustements", observations.get(0));
        assertEquals("L’état de compte total ne doit pas dépasser 30000.00 $.", observations.get(1));
        assertEquals("Le cout fixe payable ne doit pas dépasser 1500.00 $.", observations.get(2));
    }

    @Test
    public void testEtatClientObservationWithinLimit() {
        JSONArray observations = new JSONArray();
        Observations.etatClientObservation(observations, new JSONObject(), 12000.0, "ABC789");
        assertEquals(0, observations.size());
    }

    @Test
    public void testEtatClientObservationExceedsLimit() {
        JSONArray observations = new JSONArray();
        Observations.etatClientObservation(observations, new JSONObject(), 18000.0, "XYZ321");
        assertEquals(1, observations.size());
        assertEquals("L’état par client du client XYZ321 est trop dispendieuse.", observations.get(0));
    }

}
