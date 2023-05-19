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



}
