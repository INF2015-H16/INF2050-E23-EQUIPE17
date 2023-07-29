package Tests;

import Source.JsonException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;

public class TestJsonException {
    @Test
    public void testValiderDistance_Valid() {
        Assertions.assertDoesNotThrow(() -> JsonException.validerDistance(50));
    }

    @Test
    public void testValiderDistance_Invalid() {
        assertThrows(JsonException.class, () -> JsonException.validerDistance(-10));
        assertThrows(JsonException.class, () -> JsonException.validerDistance(101));
    }

    @Test
    public void testValiderOvertime_Valid() {
        assertDoesNotThrow(() -> JsonException.validerOvertime(2));
    }

    @Test
    public void testValiderOvertime_Invalid() {
        assertThrows(JsonException.class, () -> JsonException.validerOvertime(-1));
        assertThrows(JsonException.class, () -> JsonException.validerOvertime(5));
    }

    @Test
    public void testValiderNombreHeures_Valid() {
        assertDoesNotThrow(() -> JsonException.validerNombreHeures(4));
    }

    @Test
    public void testValiderNombreHeures_Invalid() {
        assertThrows(JsonException.class, () -> JsonException.validerNombreHeures(-1));
        assertThrows(JsonException.class, () -> JsonException.validerNombreHeures(9));
    }

    @Test
    public void testValiderTaux_Valid() {
        String[][] data = {{"12.34"}};
        assertDoesNotThrow(() -> JsonException.validerTaux(data, 0));
    }

    @Test
    public void testValiderTaux_Invalid() {
        String[][] data = {{"-1.0"}};
        assertThrows(JsonException.class, () -> JsonException.validerTaux(data, 0));
    }

    @Test
    public void testValiderTypeEmploye_Valid() {
        String[][] data = {{"1"}};
        assertDoesNotThrow(() -> JsonException.validerTypeEmploye(data));
    }

    @Test
    public void testValiderTypeEmploye_Invalid() {
        String[][] data = {{"3"}};
        assertThrows(JsonException.class, () -> JsonException.validerTypeEmploye(data));
    }

    @Test
    public void testValiderFormatDate_Valid() {
        assertTrue(JsonException.validerFormatDate("2023-07-28"));
    }

    @Test
    public void testValiderFormatDate_Invalid() {
        assertFalse(JsonException.validerFormatDate("2023/07/28"));
        assertFalse(JsonException.validerFormatDate("2023-13-28"));
        assertFalse(JsonException.validerFormatDate("2023-07-32"));
    }

    @Test
    public void testValidation_Valid() {
        String[][] donnees = {
                {"123", "A", "10.00", "20.00"},
                {"456", "B", "15.00", "25.00"},
                {"#", "", "-200", "0"},
                {"789", "C", "12.00", "22.00"},
                // ... Add more data rows ...
        };

        assertEquals(-1, JsonException.validation(donnees, 0));
        assertEquals(2, JsonException.validation(donnees, 1));
    }

    @Test
    public void testValidation_Invalid() {
        String[][] donnees = {
                {"123", "A", "10.00", "20.00"},
                {"#", "", "-200", "0"},
                {"#", "", "-200", "0"},
                {"789", "C", "12.00", "22.00"},
                // ... Add more data rows ...
        };

        assertEquals(1, JsonException.validation(donnees, 0));
    }

    @Test
    public void testValidationDate_Valid() {
        String[][] donnees = {
                {"123", "A", "10.00", "20.00", "2023-07-28"},
                {"456", "B", "15.00", "25.00", "2023-07-29"},
                {"#", "", "-200", "0", "2023-07-30"},
                {"789", "C", "12.00", "22.00", "2023-07-31"},
                // ... Add more data rows ...
        };

        assertDoesNotThrow(() -> JsonException.validationDate(donnees));
    }

    @Test
    public void testValidationDate_Invalid() {
        String[][] donnees = {
                {"123", "A", "10.00", "20.00", "2023-07-28"},
                {"456", "B", "15.00", "25.00", "2023-07-29"},
                {"#", "", "-200", "0", "2023-07-300"}, // Invalid date format
                {"789", "C", "12.00", "22.00", "2023-07-31"},
                // ... Add more data rows ...
        };

        assertThrows(JsonException.class, () -> JsonException.validationDate(donnees));
    }

    @Test
    public void testValiderFichierSortieDispo_FileNotExists() {
        String filePath = "output.json";
        assertDoesNotThrow(() -> JsonException.validerFichierSortieDispo(filePath));
    }

    @Test
    public void testValiderFichierSortieDispo_FileExists() throws IOException {
        String filePath = "output.json";
        File file = new File(filePath);
        file.createNewFile();
        assertDoesNotThrow(() -> JsonException.validerFichierSortieDispo(filePath));
        file.delete();
    }

    @Test
    public void testValiderProprietesJsonPresentes_AllPropertiesPresent() {
        String jsonObject = "{\"code_client\":\"123\",\"date_intervention\":\"2023-07-28\"," +
                "\"nombre_heures\":5,\"overtime\":1.5,\"distance_deplacement\":40," +
                "\"taux_horaire_max\":25.0,\"taux_horaire_min\":15.0,\"type_employe\":1}";
        String arg2 = "data.json";
        assertDoesNotThrow(() -> JsonException.validerProprietesJsonPresentes(jsonObject, arg2));
    }

    @Test
    public void testValiderProprietesJsonPresentes_SomePropertiesMissing() {
        String jsonObject = "{\"code_client\":\"123\",\"date_intervention\":\"2023-07-28\"," +
                "\"nombre_heures\":5,\"overtime\":1.5,\"distance_deplacement\":40}";
        String arg2 = "data.json";
        assertThrows(IOException.class, () -> JsonException.validerProprietesJsonPresentes(jsonObject, arg2));
    }

}
