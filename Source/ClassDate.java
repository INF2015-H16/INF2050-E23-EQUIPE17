package Source;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ClassDate {
    public static void ecrireDateFichier(String date) {
        String filePath = "./dates.txt";

        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write(date + "\n");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }

    public static String readLineFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("./dates.txt"))) {
            String line = reader.readLine();
            return line;
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file: " + e.getMessage());
        }
        return null;
    }

    public static String readLineFromFile1() {
        try (BufferedReader reader = new BufferedReader(new FileReader("./dates.txt"))) {
            String line = reader.readLine();
            String line1 = reader.readLine();
            return line1;
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file: " + e.getMessage());
        }
        return null;
    }

    public static void clearFile() {
        try (FileWriter writer = new FileWriter("./dates.txt")) {
            writer.write("");
        } catch (IOException e) {
            System.out.println("An error occurred while clearing the file: " + e.getMessage());
        }
    }
}
