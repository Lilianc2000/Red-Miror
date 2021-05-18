package red_miror;

import java.lang.System;
//import java.util.Scanner;
//import java.util.Calendar;

public class Main {

    public static void main(String[] args) {
    	
    	//Initialisation de la carte
    	//int taille = 10;									//Pour les tests
    	int taille = -1;
    	try{
    		taille = Integer.parseInt(args[0]);				//Taille de la carte n*n
      	}
    	catch(Exception e) {
    		System.out.println("Please, type an integer.");
    		System.exit(-1);
    	}
        Map map = new Map(taille);							//Instanciation de la carte
        	
        
        //Scanner sc = new Scanner(System.in);				//Pour utiliser le touche par touche
        
        
        //Partie graphique
        int Xresolution = 1024, Yresolution = 576;			//Résolution de la fenetre
        Interface frame = new Interface(taille, Xresolution, Yresolution, map);
        frame.setSize(Xresolution, Yresolution);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setTitle("Red Miror");
     }
}
