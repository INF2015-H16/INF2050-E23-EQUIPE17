package Tests;

import Source.Main;
import org.junit.Test;

public class TestVerifierFichierEntree {

    @Test

    public void testVerifierFichierEntree(){

        boolean fichierExiste;
        String argument = "test.json";
        fichierExiste = Main.verifierFichierEntree(argument);
        //S'assurer d'avoir le fichier d'entree ¨entree.json¨ dans le repertoire du projet pour verifier si le fichier
        //existe.
        if(fichierExiste){
            System.out.println("Le fichier d'entree existe.");
        }
        //S'assurer de ne pas avoir le fichier d'entree ¨entree.json¨ dans le repertoire du projet pour verifier si le
        //fichier n'existe pas.
        else{
            System.out.println("Le fichier d'entree n'existe pas.");
        }
    }
}
