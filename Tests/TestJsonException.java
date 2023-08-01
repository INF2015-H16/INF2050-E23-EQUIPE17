package Tests;

import Source.JsonException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestJsonException {
    @Test
    public void testValiderDistance_Valid() {
        Assertions.assertDoesNotThrow(() -> JsonException.validerDistance(50));
    }

}
