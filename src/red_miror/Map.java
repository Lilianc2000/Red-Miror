package red_miror;

import javax.swing.ImageIcon;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

public class Map {
	
	/*
	 * Note importante:
	 * Pour eviter des erreurs de manipulations du tableau (out of bounds, ...)
	 * Veuillez renseigner des coordonnees allant de 1 a ptaille. Les indices
	 * a zero sont les murs de la piece, comme ceux a ptaille+1. Donc le
	 * premier element du tableau se situe aux coordonnees (1,1) et le dernier
	 * element se situe au coordonnees (ptaille,ptaille)
	 * Pour ajouter des elements dans le tableau, uniquement passer par
	 * addObject(...)
	 */
	
	private int [][] piece;
	private int taille;
	private int positionLaserX=-1;
	private int positionLaserY=-1;
	private int surfaceMax;
	private int surfaceRealisable;
	private int nbrCroisement;
	private ImageIcon Miror_r = new ImageIcon(Interface.class.getResource("/red_miror/Miror_r.png"));
	private ImageIcon Miror_l = new ImageIcon(Interface.class.getResource("/red_miror/Miror_l.png"));
	private ImageIcon faisceau = new ImageIcon(Interface.class.getResource("/red_miror/faisceau.png"));
	
	Map(int ptaille) {
		int i=0,j=0;
		taille=ptaille+2;
		piece = new int [taille][taille];
		for(j=0;j<taille;j++) {
			for(i=0;i<taille;i++) {
				if(j==0||j==(taille-1)||i==0||i==(taille-1)) {
					piece[i][j]=1;
				} else {
					piece [i][j]=0;
				}
				
			}
		}
		surfaceMax=ptaille*ptaille;
		surfaceRealisable=surfaceMax;
		nbrCroisement=0;
	}//fin constructeur
	
	//Obtenir la positionX du laser
	int getPositionLaserX() {
		return positionLaserX;
	}
	
	//Obtenir la positionY du laser
	int getPositionLaserY() {
		return positionLaserY;
	}
	
	//Definir la position du laser
	private void setPositionLaser(int x, int y) {
		positionLaserX=x;
		positionLaserY=y;
		piece[x][y]=2;
	}
	
	public boolean laserBloque() {
		return piece[positionLaserX][positionLaserY-1]==1;
	}
	
	//Determination du choix suivant en fonction du choix precedent, de la position actuelle et de la direction
	int choixSuivant(int i, int j, int choixPrecedent, int direction) {
		//0 2
		//Monitoring
		//this.tracer();
		//System.out.println("\n");
		if(choixPrecedent==-2) {		//Premier deplacement
			//Tout droit ?
			if(direction==1) {if(piece[i+1][j]==0) return 1; if(piece[i+1][j]==3) {if(piece[i+2][j]==0) return 1;}}
			if(direction==2) {if(piece[i][j-1]==0) return 1; if(piece[i][j-1]==3) {if(piece[i][j-2]==0) return 1;}}
			if(direction==3) {if(piece[i-1][j]==0) return 1; if(piece[i-1][j]==3) {if(piece[i-2][j]==0) return 1;}}
			if(direction==4) {if(piece[i][j+1]==0) return 1; if(piece[i][j+1]==3) {if(piece[i][j-2]==0) return 1;}}
		}
		if(choixPrecedent==0) {
			//Tout droit ?
			if(direction==1) {if(piece[i+1][j]==0) return 1; if(piece[i+1][j]==3) {if(piece[i+2][j]==0) return 1;}}
			if(direction==2) {if(piece[i][j-1]==0) return 1; if(piece[i][j-1]==3) {if(piece[i][j-2]==0) return 1;}}
			if(direction==3) {if(piece[i-1][j]==0) return 1; if(piece[i-1][j]==3) {if(piece[i-2][j]==0) return 1;}}
			if(direction==4) {if(piece[i][j+1]==0) return 1; if(piece[i][j+1]==3) {if(piece[i][j-2]==0) return 1;}}
			//Droite ?
			if(direction==1) {if(piece[i][j+1]==0) return 2; if(piece[i][j+1]==3 ) {if(piece[i][j+2]==0) return 2;}}
			if(direction==2) {if(piece[i+1][j]==0) return 2; if(piece[i+1][j]==3 ) {if(piece[i+2][j]==0) return 2;}}
			if(direction==3) {if(piece[i][j-1]==0) return 2; if(piece[i][j-1]==3 ) {if(piece[i][j-2]==0) return 2;}}
			if(direction==4) {if(piece[i-1][j]==0) return 2; if(piece[i-1][j]==3 ) {if(piece[i-2][j]==0) return 2;}}
			//Gauche ?
			if(direction==1) {if(piece[i][j-1]==0) return 3; if(piece[i][j-1]==3 ) {if(piece[i][j-2]==0) return 3;}}
			if(direction==2) {if(piece[i-1][j]==0) return 3; if(piece[i-1][j]==3 ) {if(piece[i-2][j]==0) return 3;}}
			if(direction==3) {if(piece[i][j+1]==0) return 3; if(piece[i][j+1]==3 ) {if(piece[i][j+2]==0) return 3;}}
			if(direction==4) {if(piece[i+1][j]==0) return 3; if(piece[i+1][j]==3 ) {if(piece[i+2][j]==0) return 3;}}
		}
		if(choixPrecedent==1) {
			//Droite ?
			if(direction==1) {if(piece[i][j+1]==0) return 2; if(piece[i][j+1]==3 ) {if(piece[i][j+2]==0) return 2;}}
			if(direction==2) {if(piece[i+1][j]==0) return 2; if(piece[i+1][j]==3 ) {if(piece[i+2][j]==0) return 2;}}
			if(direction==3) {if(piece[i][j-1]==0) return 2; if(piece[i][j-1]==3 ) {if(piece[i][j-2]==0) return 2;}}
			if(direction==4) {if(piece[i-1][j]==0) return 2; if(piece[i-1][j]==3 ) {if(piece[i-2][j]==0) return 2;}}
			//Gauche ?
			if(direction==1) {if(piece[i][j-1]==0) return 3; if(piece[i][j-1]==3 ) {if(piece[i][j-2]==0) return 3;}}
			if(direction==2) {if(piece[i-1][j]==0) return 3; if(piece[i-1][j]==3 ) {if(piece[i-2][j]==0) return 3;}}
			if(direction==3) {if(piece[i][j+1]==0) return 3; if(piece[i][j+1]==3 ) {if(piece[i][j+2]==0) return 3;}}
			if(direction==4) {if(piece[i+1][j]==0) return 3; if(piece[i+1][j]==3 ) {if(piece[i+2][j]==0) return 3;}}
		}
		if(choixPrecedent==2) {
			//Gauche ?
			if(direction==1) {if(piece[i][j-1]==0) return 3; if(piece[i][j-1]==3 ) {if(piece[i][j-2]==0) return 3;}}
			if(direction==2) {if(piece[i-1][j]==0) return 3; if(piece[i-1][j]==3 ) {if(piece[i-2][j]==0) return 3;}}
			if(direction==3) {if(piece[i][j+1]==0) return 3; if(piece[i][j+1]==3 ) {if(piece[i][j+2]==0) return 3;}}
			if(direction==4) {if(piece[i+1][j]==0) return 3; if(piece[i+1][j]==3 ) {if(piece[i+2][j]==0) return 3;}}
		}
		if(choixPrecedent==3) return -1;
		return -1;
	}
	
	//Ajoute le type d'un objet dans le tableau: 0=air 1=obstacle 2=laser
	public void addObject(int type, int positionY, int positionX) {
		positionY++; positionX++;					//Correction du d�calage du tableau dans interface.java
		if(positionX<taille && positionY<taille && positionX>0 && positionY>0) {		//Verification out of bounds
			System.out.println(type + " en X="+positionX+":Y="+positionY);								//Monitoring
			piece[positionX][positionY]=type;
			if(type==2) {
				this.setPositionLaser(positionX, positionY);
			}
		}
		this.majSurfaceMax();
	}
	
	//Est ce que le tableau est plein ?
	public boolean plein(int taillePile) {
		//System.out.println(taillePile-nbrCroisement);				//Monitoring
		return (taillePile-nbrCroisement)==surfaceRealisable;
	}
	
	//Place un objet a une position donnee
	public void set(int i, int j, boolean pSlash, int pNouveauChoix) {
		if(pNouveauChoix==1) {		//Si tout droit
			if(piece[i][j]==3) {	//Si il y a deja un faisceau alors croisement
				piece[i][j]=6;
				nbrCroisement++;
			} else {
				piece[i][j]=3;
			}
		} else {
			if(pSlash==true) {		//Si miroir en '/'
				piece[i][j]=4;
			}
			if(pSlash==false) {		//Si miroir en '\'
				piece[i][j]=5;
			}
		}
	}
	
	//Place de l'air a une position donnee
	public void reset(int i, int j) {
		if(piece[i][j]==6) {			//Si retour sur croisement
			piece[i][j]=3;				//On replace un faisceau
			nbrCroisement--;
		} else {
			piece[i][j]=0;
		}
		piece[positionLaserX][positionLaserY]=2;
	}
	
	//Affiche dans la console le tableau de la map
	public void tracer(DefaultTableModel modele) {
		int i=0, j=0;
		for(j=1;j<taille-1;j++) {
			for(i=1;i<taille-1;i++) {
				System.out.print(piece[i][j]);			//Monitoring
				if (piece[i][j]==3 || piece[i][j]==6) {
					modele.setValueAt(faisceau, j-1, i-1);
				}
				if (piece[i][j]==4) {
					modele.setValueAt(Miror_r, j-1, i-1);
				}
				if (piece[i][j]==5) {
					modele.setValueAt(Miror_l, j-1, i-1);
				}
				modele.fireTableDataChanged();
			}
			System.out.println("");			//Monitoring
		}
	}
	
	//Met � jour la valeur de surface libre maximum sur la map
	//Le laser n est pas compte comme surface libre
	private void majSurfaceMax() {
		surfaceMax=(taille-2)*(taille-2);
		int i=0, j=0;
		for(j=1;j<(taille-1);j++) {
			for(i=1;(i<taille-1);i++) {
				if(piece[i][j]!=0) {
					surfaceMax--;
				}
			}
		}
		surfaceRealisable=surfaceMax;
	}
	
	//Met a jour la valeur de surface realisable au maximum par le laser
	// => elimination des cases isolees du laser (inatteignable)
	// => elimination des cases cul-de-sac sauf le + grand qui sera la fin
	//du parcours du faisceau
	public void majSurfaceRealisable(JTextArea textLog) {
		int i=0, j=0, ii=0, jj=0, k=-1, l=0;
		int dir=0, dirSuivante=0;
		int longueur=0, tailleMax=0, casesSept=1;
		//cases a 7: cases atteignables par le laser, non isolees, comptees par la variable casesSept
		
		//Recherche de cases isolees
		if(piece[this.getPositionLaserX()][this.getPositionLaserY()-1]==0) {		//Si le debut du faisceau est bien de l air
			piece[this.getPositionLaserX()][this.getPositionLaserY()-1]=7;
			while(k!=0) {		//Tant qu il y a de nouvelles cases atteignables de decouvertes
				k=0;
				for(j=1;j<taille-1;j++) {
					for(i=1;i<taille-1;i++) {
						if(piece[i][j]==0 && piece[i+1][j]==7) {piece[i][j]=7; k++; casesSept++;}
						if(piece[i][j]==0 && piece[i][j-1]==7) {piece[i][j]=7; k++; casesSept++;}
						if(piece[i][j]==0 && piece[i-1][j]==7) {piece[i][j]=7; k++; casesSept++;}
						if(piece[i][j]==0 && piece[i][j+1]==7) {piece[i][j]=7; k++; casesSept++;}
					}//fin for i
				}//fin for j
			}//fin while
			surfaceRealisable=casesSept;
		}//fin if
		textLog.append("Achievable boxes: "+casesSept+"\n");
		
		//Recherche des culs-de-sac
		for(j=1;j<taille-1;j++) {
			for(i=1;i<taille-1;i++) {
				k=0; dir=0; dirSuivante=0;
				//Comptage des obstacles autour de (i,j)
				if(piece[i+1][j]==1) {k++;} else {dir=1;}
				if(piece[i][j-1]==1) {k++;} else {dir=2;}
				if(piece[i-1][j]==1) {k++;} else {dir=3;}
				if(piece[i][j+1]==1) {k++;} else {dir=4;}
				//Si 3 obstacles alors cul-de-sac
				if(k==3 && piece[i][j]==7) {
					//System.out.println("cul-de-sac:"+i+","+j);			//Monitoring
					ii=i; jj=j;
					longueur=0;
					do {
						longueur++;
						surfaceRealisable--;
						if(dir==1) ii++;
						if(dir==2) jj--;
						if(dir==3) ii--;
						if(dir==4) jj++;
						l=0;
						//Comptage des obstacles autour de (ii,jj)
						if(piece[ii+1][jj]==1) l++; else if(dir!=3) dirSuivante=1;
						if(piece[ii][jj-1]==1) l++; else if(dir!=4) dirSuivante=2;
						if(piece[ii-1][jj]==1) l++; else if(dir!=1) dirSuivante=3;
						if(piece[ii][jj+1]==1) l++; else if(dir!=2) dirSuivante=4;
						if(l==2) {
							dir=dirSuivante;
						}
					}while(l==2);		//Tant que dans le cul-de-sac
					//Enregistrement du record de taille de cul-de-sac
					if(longueur>tailleMax) tailleMax=longueur;
				}
			}//fin for i
		}//fin for j
		//On remet les cases a 7 a 0
		for(j=1;j<taille-1;j++) {
			for(i=1;i<taille-1;i++) {
				if(piece[i][j]==7) {
					piece[i][j]=0;
				}
			}
		}
		surfaceRealisable=surfaceRealisable+tailleMax;
		textLog.append("Free area : "+surfaceRealisable+" boxes\n");			//Monitoring
	}
	
	//Retire les faisceaux laser et les miroirs
	public void resetBeam() {
		int i=0,j=0;
		for(j=1;j<taille-1;j++) {
			for(i=1;i<taille-1;i++) {
				if(piece[i][j]==3 || piece[i][j]==4 || piece[i][j]==5 || piece[i][j]==6) {
					piece[i][j]=0;
				}
			}
		}
	}
	
	//Retire tous les objets de la map, r�initialise toutes les valeurs de simulation:
	//positionLaserX, positionLaserY, surfaceMax, surfaceRealisable, nbrCroisement
	public void resetAll() {
		positionLaserX=-1;
		positionLaserY=-1;
		surfaceMax=(taille-2)*(taille-2);
		surfaceRealisable=surfaceMax;
		nbrCroisement=0;
		int i=0,j=0;
		for(j=1;j<taille-1;j++) {
			for(i=1;i<taille-1;i++) {
				piece[i][j]=0;
			}
		}
	}
}
