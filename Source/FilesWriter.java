package Source;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FilesWriter {
    public static void saveStringIntoFile(String filePath, String contentToSave)
            throws FileNotFoundException, IOException {
        File f = new File(filePath);
        FileUtils.writeStringToFile(f, contentToSave, "UTF-8");
    }
}