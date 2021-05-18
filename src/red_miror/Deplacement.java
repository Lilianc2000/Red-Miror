package red_miror;

public class Deplacement {
	
	int choixPrecedent;
	int posPrecedentX;
	int posPrecedentY;
	int directionPrecedente;
	
	Deplacement(int x, int y, int choix, int direction) {
		choixPrecedent=choix;
		posPrecedentX=x;
		posPrecedentY=y;
		directionPrecedente=direction;
	}
}
