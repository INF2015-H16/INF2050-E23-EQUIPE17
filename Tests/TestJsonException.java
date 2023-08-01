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

}
