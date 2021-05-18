package red_miror;

import java.awt.event.*;					//Importe les libraries nécessaires au fonctionnement du logiciel
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ImageIcon;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import java.util.Calendar;

public class Interface extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;								
	private int selection = -1;													//Variable afin de savoir quelle type d'objet on veut poser sur la map
	private JTable table_1;
	private boolean obsOK = false;								//Savoir si le laser est déjà posé
	private boolean slash = false;								//Type de miroir entre '/'=true et '\'=false
	private int direction=2;									//Direction empruntée par le laser 1=E 2=N 3=O 4=S
	private int choixPrecedent=0, nouveauChoix=0, numeroDeplacement=1, ii=0, jj=0, i=0, j=0, compensation=0;
	private Calendar calDebut;
	private	Calendar calFin;
	private Pile <Deplacement> P = new Pile<Deplacement>();		//Creation de la pile de deplacement
	private Deplacement deplacementPrecedent;
	private TableColumn col = new TableColumn();
	
	public Interface(int taille, int Xresolution, int Yresolution, Map map) {												//Constructeur de notre interface
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);							//Création de la fenêtre de base
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(1,2));
		
		ImageIcon vide = new ImageIcon(Interface.class.getResource("/red_miror/white_solid.png"));			//Création des objets pour l'affichage dans le tableau
		ImageIcon obstacle = new ImageIcon(Interface.class.getResource("/red_miror/grey_solid.png"));
		ImageIcon laser = new ImageIcon(Interface.class.getResource("/red_miror/red_solid.png"));
		
		Object[][] donnees = new Object[taille][taille];													//Création du tableau vide
		String[] entetes = new String[taille];
		for(i=0;i<taille;i++) {
			for(j=0;j<taille;j++) {
				donnees[i][j]=vide;
			}
			entetes[i]="0";
		}
 
        DefaultTableModel modele = new DefaultTableModel(donnees, entetes);
		table_1 = new JTable(modele)
		{          
			private static final long serialVersionUID = -8913506703926579066L;

			/*détection automatique des types de données             
             de toutes les colonnes    
             */
            @SuppressWarnings({ "unchecked", "rawtypes" })
			public Class getColumnClass(int colonne)
            {
                return getValueAt(0, colonne).getClass();
            }
            
           public boolean isCellEditable(int x, int y) {
            	return false;
            	}
        };
        table_1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table_1.setRowHeight(Yresolution/taille);
        for (i=0;i<taille;i++) {
        	col = table_1.getColumnModel().getColumn(i);
            col.setWidth(Xresolution/taille);
        }
        col = table_1.getColumnModel().getColumn(0);
        contentPane.add(table_1);
				
		JPanel panel_1 = new JPanel();											//Création de l'emplacement de nos boutons
		panel_1.setAlignmentX(Component.RIGHT_ALIGNMENT);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		JButton btnStartSimulation = new JButton("Start Simulation");			//Création du bouton pour lancer la simulation
		btnStartSimulation.setBounds(Xresolution-680, Yresolution-76, 149, 25);
		panel_1.add(btnStartSimulation);
		
		JButton btnReset = new JButton("Reset");                               //Création du bouton Reset
        btnReset.setBounds(Xresolution-880, Yresolution-76, 149, 25);
        panel_1.add(btnReset);
		
		JRadioButton rdbtnObstacle = new JRadioButton("Obstacle");				//Création de la sélection 1 : l'obstacle
		rdbtnObstacle.setBounds(Xresolution-603, Yresolution-554, 88, 23);
		panel_1.add(rdbtnObstacle);
	
		JRadioButton rdbtnLaser = new JRadioButton("Laser");  					//Création de la sélection 2 : le laser
		rdbtnLaser.setBounds(Xresolution-580, Yresolution-527, 65, 23);
		panel_1.add(rdbtnLaser);
		
		JRadioButton rdbtnDelete = new JRadioButton("Delete");  					//Création de la sélection 3 : supprimer
		rdbtnDelete.setBounds(Xresolution-588, Yresolution-500, 80, 23);
		panel_1.add(rdbtnDelete);
		
		JLabel lblAdd = new JLabel("Add :");									//Création de l'indication de sélection de l'objet à ajouter
		lblAdd.setBounds(Xresolution-555, 0, 42, 15);
		panel_1.add(lblAdd);
		
		JLabel lblLog = new JLabel("Log :");									//Création de l'indication de log
		lblLog.setBounds(5, 180, 42, 15);
		panel_1.add(lblLog);
		
		JTextArea textLog = new JTextArea();									//Création de la fenêtre de log
		textLog.setBounds(5, 200, 500, 280);
		panel_1.add(textLog);
		textLog.setEditable(false);
		
		JLabel lblObstacle = new JLabel("");									//Création de l'image de l'obstacle
		lblObstacle.setEnabled(false);
		lblObstacle.setVisible(false);
		lblObstacle.setIcon(obstacle);
		lblObstacle.setBounds(0, 0, col.getWidth(), table_1.getRowHeight(0));
		table_1.add(lblObstacle);
		
		JLabel lblLaser = new JLabel("");										//Création de l'image du laser
		lblLaser.setEnabled(false);
		lblLaser.setVisible(false);
		lblLaser.setIcon(laser);
		lblLaser.setBounds(0, 0, col.getWidth(), table_1.getRowHeight(0));
		table_1.add(lblLaser);
		
				
		rdbtnObstacle.addItemListener(new ItemListener() {				//Bloc permettant de suivre les actions de la sélection Obstacle
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					selection = 1;
					rdbtnLaser.setSelected(false);
					rdbtnDelete.setSelected(false);					
					textLog.append("Box Obstacle selected\n");
				}
				if (e.getStateChange() == ItemEvent.DESELECTED && selection == 1) {
					rdbtnObstacle.setSelected(true);
				}
			}
		
		});
		
		rdbtnLaser.addItemListener(new ItemListener() {					//Bloc permettant de suivre les actions de la sélection Laser
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					selection = 2;
					rdbtnObstacle.setSelected(false);
					rdbtnDelete.setSelected(false);					
					textLog.append("Box Laser selected\n");
				}
				if (e.getStateChange() == ItemEvent.DESELECTED && selection == 2) {
					rdbtnLaser.setSelected(true);
				}
			}
		
		});
		
		rdbtnDelete.addItemListener(new ItemListener() {					//Bloc permettant de suivre les actions de la sélection Delete
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					selection = 0;
					rdbtnObstacle.setSelected(false);
					rdbtnLaser.setSelected(false);
					textLog.append("Box Delete selected\n");
				}
				if (e.getStateChange() == ItemEvent.DESELECTED && selection == 0) {
					rdbtnDelete.setSelected(true);
				}
			}
		
		});
		
		btnReset.addActionListener(new ActionListener() { 		//Bloc permettant de suivre les actions du bouton Reset
			  int i = 0,j = 0;
			  public void actionPerformed(ActionEvent e) { 
				  textLog.append("Reset\n");
				  map.resetAll();
				  for(i=0; i<taille; i++) {
				  	for(j=0; j<taille; j++) {
				  		modele.setValueAt(vide, i, j);
					  	}
				  }
				  obsOK=false;
				  modele.fireTableDataChanged();
			  } 
		});
		
		btnStartSimulation.addActionListener(new ActionListener() { 		//Bloc permettant de suivre les actions du bouton Start Simulation
			JOptionPane box;  
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent e) { 
				  if (obsOK==true) {
					  textLog.append("Start Simulation\n");
					  selection=-1;
					  rdbtnObstacle.setSelected(false);
					  rdbtnLaser.setSelected(false);
					  rdbtnDelete.setSelected(false);
					  lblLaser.setEnabled(false);
					  lblLaser.setVisible(false);
					  lblObstacle.setEnabled(false);
					  lblObstacle.setVisible(false);
					  modele.fireTableDataChanged();
                  	
					  //Depart de la simulation
					  map.tracer(modele); System.out.println("");									//Monitoring
					  //System.out.println("Appuyez sur une touche pour lancer la r�solution.");	//Monitoring
					  //sc.nextLine();																//Monitoring
					  calDebut=Calendar.getInstance();
					  map.majSurfaceRealisable(textLog);				//Mise a jour de la surface realisable (voir map.java)
					  if(!map.laserBloque()) {
						  do {
							  slash=false;
						  	direction=2;
						  	choixPrecedent=-2;
						  	nouveauChoix=0;
						  	numeroDeplacement=1;
						  	ii=0; jj=0;
						  	int i=map.getPositionLaserX();
						  	int j=map.getPositionLaserY();
						  	map.resetBeam();
						  
						  	//Si la pile n'est pas vide, on retire toutes les assiettes
						  	while(P.Taille()!=0) {
						  		P.Depile();
						  	}
						  	while(!map.plein(P.Taille()+compensation)) {       	//Tant que la map n'est pas pleine
							  	nouveauChoix=map.choixSuivant(i,j,choixPrecedent,direction);
								if(nouveauChoix!=-1) {		//Si le nouveau choix n est pas une impasse
								    P.Empile(new Deplacement(i,j,nouveauChoix,direction));		//ajout d une assiette a la pile
								    ii=i; jj=j;
								    if(nouveauChoix==1) {		//Si tout droit
								        if(direction==1){i++;}
								        if(direction==2){j--;}
								        if(direction==3){i--;}
								        if(direction==4){j++;}
								    }
					                if(nouveauChoix==2) {		//Si droite via miroir
					                    if(direction==1){j++; slash=false;}
					                    if(direction==2){i++; slash=true;}
					                    if(direction==3){j--; slash=false;}
					                    if(direction==4){i--; slash=true;}
					                    direction--;			//Changement de direction
					                    if(direction==0) {direction=4;}		//modulo 
					                }
					                if(nouveauChoix==3) {		//Si gauche via miroir
					                	if(direction==1){j--; slash=true;}
					                    if(direction==2){i--; slash=false;}
					                    if(direction==3){j++; slash=true;}
					                    if(direction==4){i++; slash=false;}
					                    direction++;			//Changement de direction
					                    if(direction==5) {direction=1;}		//modulo
					                }
					                numeroDeplacement++;
					                
					                //Place sur la map le deplacement laser effectu�
					                if(nouveauChoix==1) {
					                	map.set(i,j,slash,nouveauChoix);
					                } else {
					                	map.set(ii,jj,slash,nouveauChoix);
					                	map.set(i,j,slash,1);
					                }	
					                choixPrecedent=0;
					            }
					            else if(numeroDeplacement!=1) {				//Impasse donc retour arriere
					            	deplacementPrecedent=P.SommetPile();	//Recuperation du dernier deplacement au sommet de la pile
					            	P.Depile();								//Retire la derniere assiette
					            	map.reset(i,j);							//Retire le deplacement laser
					            	i=deplacementPrecedent.posPrecedentX;
					            	j=deplacementPrecedent.posPrecedentY;
					            	direction=deplacementPrecedent.directionPrecedente;
					            	choixPrecedent=deplacementPrecedent.choixPrecedent;
					            	numeroDeplacement--;
					            }
					            else {				//pas de solution
					            	break;
					            }
					            //sc.nextLine();		//Monitoring: une �tape � chaque touche
					            //map.tracer();			//Monitoring
					        }//fin while
						  	if(nouveauChoix==-1 &&  numeroDeplacement==1)
						  	compensation++;
						  	System.out.println("Compensation="+compensation);				//Monitoring
					  	}while(nouveauChoix==-1 && numeroDeplacement==1);//fin do while
					} else {
						box = new JOptionPane();
						box.showMessageDialog(null, "Laser obstructed, make it free", "Warning", JOptionPane.WARNING_MESSAGE);
					}
			        map.tracer(modele);
			        calFin = Calendar.getInstance();
			        float tempsCalcul;
			        tempsCalcul = (calFin.get(Calendar.HOUR_OF_DAY)*60*60*1000+calFin.get(Calendar.MINUTE)*60*1000+calFin.get(Calendar.SECOND)*1000+calFin.get(Calendar.MILLISECOND))-(calDebut.get(Calendar.HOUR_OF_DAY)*60*60*1000+calDebut.get(Calendar.MINUTE)*60*1000+calDebut.get(Calendar.SECOND)*1000+calDebut.get(Calendar.MILLISECOND));
			        tempsCalcul=tempsCalcul/1000f;
			        textLog.append("Solution found in "+tempsCalcul+" secondes\n");
			  } 
				  else if(obsOK==false) {
					  box = new JOptionPane();
					  box.showMessageDialog(null, "You have to put a laser on the map.", "Warning", JOptionPane.WARNING_MESSAGE); 
				  }
			}
		});

		
		table_1.addMouseMotionListener(new MouseMotionListener() {				//Bloc permettant de suivre les déplacements de la souris
			
			public void mouseDragged(MouseEvent arg0) {							//Mouvement + clic
				System.out.println("Log : Mouse dragged");
			}
			
			public void mouseMoved(MouseEvent arg0) {							//Gestion dynamique de l'affichage de l'objet sur le pointeur
				System.out.println("Log : Mouse moved : X = "+arg0.getX()+" ; Y = "+arg0.getY());
				lblLaser.setBounds(arg0.getX()-20, arg0.getY()-10, 33, 33);			
				lblObstacle.setBounds(arg0.getX()-20, arg0.getY()-10, 33, 33);		
				modele.fireTableDataChanged();
				
				if (arg0.getX()<504 && selection==1) {								
					lblObstacle.setEnabled(true);
					lblObstacle.setVisible(true);
					lblLaser.setEnabled(false);
					lblLaser.setVisible(false);
				}
				else if (arg0.getX()<504 && selection==2){							
					lblObstacle.setEnabled(false);
					lblObstacle.setVisible(false);
					lblLaser.setEnabled(true);
					lblLaser.setVisible(true);
				}
				else if (arg0.getX()>=504 || selection==0){
					lblObstacle.setEnabled(false);
					lblObstacle.setVisible(false);
					lblLaser.setEnabled(false);
					lblLaser.setVisible(false);
				}
			}
			
				
		});
		
		
		table_1.addMouseListener(new MouseAdapter() {	//Ajout d'un obstacle dans la case correspondante du tableau
			@SuppressWarnings("static-access")
			public void mouseClicked(MouseEvent e) {
				
				JOptionPane box;
				
				if (selection == 0) {							//On supprime ce qui était placé
					try{
						map.addObject(selection, this.positionTableY(lblObstacle.getY()), this.positionTableX(lblObstacle.getX()));
						if (modele.getValueAt(this.positionTableY(lblObstacle.getY()), this.positionTableX(lblObstacle.getX())) == laser){
							obsOK = false;
						}
						modele.setValueAt(vide, this.positionTableY(lblObstacle.getY()), this.positionTableX(lblObstacle.getX()));
					}
					catch(Exception j) {
						box = new JOptionPane();
						box.showMessageDialog(null, "You clicked outside of the table !", "Warning", JOptionPane.WARNING_MESSAGE);
					}
						modele.fireTableDataChanged();
				}
				
				if (obsOK == true && selection == 2) {								//Impossible de placer deux lasers
					box = new JOptionPane();
					box.showMessageDialog(null, "You already have a Laser, delete it first.", "Information", JOptionPane.INFORMATION_MESSAGE);
				}
				
				if (selection == 2 && obsOK == false){								//On place un laser
					try{
						map.addObject(selection, this.positionTableY(lblLaser.getY()), this.positionTableX(lblLaser.getX()));
						modele.setValueAt(laser, this.positionTableY(lblLaser.getY()), this.positionTableX(lblLaser.getX()));
						obsOK = true;
					}
					catch(Exception j) {
						box = new JOptionPane();
						box.showMessageDialog(null, "You clicked outside of the table !", "Warning", JOptionPane.WARNING_MESSAGE);
					}
					modele.fireTableDataChanged();
				}
				
				if (selection == 1){																							//On place un obstacle
					try{
						if (modele.getValueAt(this.positionTableY(lblObstacle.getY()), this.positionTableX(lblObstacle.getX())) == laser){
							obsOK = false;
						}
						map.addObject(selection, this.positionTableY(lblObstacle.getY()), this.positionTableX(lblObstacle.getX()));
						modele.setValueAt(obstacle, this.positionTableY(lblObstacle.getY()), this.positionTableX(lblObstacle.getX()));
					}
					catch (Exception j) {
						box = new JOptionPane();
						box.showMessageDialog(null, "You clicked outside of the table !", "Warning", JOptionPane.WARNING_MESSAGE);
					}
					modele.fireTableDataChanged();
					}
				}

			private int positionTableY(int Y) {										//Permet de placer l'objet en fonction de la position du curseur
  				for(i=0;i<=taille;i++) {
					if (table_1.getRowHeight()*i <= Y && Y < table_1.getRowHeight()*(i+1)) {
						break;
					}
				}
				return i;
			}
			private int positionTableX(int X) {										//Permet de placer l'objet en fonction de la position du curseur
				for(i=0;i<=taille;i++) {
					if (col.getWidth()*i <= X && X < col.getWidth()*(i+1)) {
						break;
					}
				}
			return i;
			}
		});
	}
}