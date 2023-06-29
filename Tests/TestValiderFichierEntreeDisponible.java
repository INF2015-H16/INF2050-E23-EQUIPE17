package Tests;

import Source.Main;
import org.junit.Test;

public class TestValiderFichierEntreeDisponible {

    @Test

    public void testValiderFichierEntreeDisponible(){

        boolean fichierExiste;
        //TODO renommer "test.json" en "entree.json" partout ou il figure avant la remise du projet
        String argument = "test.json";
        fichierExiste = Main.validerFichierEntreeDisponible(argument);
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
