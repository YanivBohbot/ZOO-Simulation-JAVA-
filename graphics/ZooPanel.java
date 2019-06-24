package graphics;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.imageio.ImageIO;
import javax.swing.*;

import animals.Animal;
import animals.Bear;
import animals.Elephant;
import animals.Giraffe;
import animals.Lion;
import animals.Turtle;
import food.EFoodType;
import plants.Cabbage;
import plants.Lettuce;
import plants.Plant;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class ZooPanel extends JPanel implements ActionListener, Runnable
{
   private static final long serialVersionUID = 1L;
   private static final int MAX_ANIMAL_NUMBER  = 10;
   private final String BACKGROUND_PATH = Animal.PICTURE_PATH+"savanna.jpg";
   private final String MEAT_PATH = Animal.PICTURE_PATH+"meat.gif";
   private final int RESOLUTION = 25; 
   private ZooFrame frame;
   private EFoodType Food;
   private JPanel p1;
   private JButton[] b_num;
   private String[] names = {"Add Animal","Sleep","Wake up","Clear","Food","Info","Exit"};
   private ArrayList<Animal> animals;
   private Plant forFood = null;
   private JScrollPane scrollPane;
   private boolean isTableVisible;
   private int totalCount;
   private BufferedImage img, img_m;
   private boolean bgr;
   private Thread controller;
   
   public ZooPanel(ZooFrame f)
   {
	    frame = f;
	    Food = EFoodType.NOTFOOD;
	    totalCount = 0;
	    isTableVisible = false;
	    
	    animals = new ArrayList<Animal>();

	    controller = new Thread(this);
	    controller.start();	    
	   
	    setBackground(new Color(255,255,255));
	    
	    p1=new JPanel();
		p1.setLayout(new GridLayout(1,7,0,0));
		p1.setBackground(new Color(0,150,255));

		b_num=new JButton[names.length];
		for(int i=0;i<names.length;i++)
		{
		    b_num[i]=new JButton(names[i]);
		    b_num[i].addActionListener(this);
		    b_num[i].setBackground(Color.lightGray);
		    p1.add(b_num[i]);		
		}

		setLayout(new BorderLayout());
		add("South", p1);
		
		img = img_m = null;
		bgr = false;
		try { img = ImageIO.read(new File(BACKGROUND_PATH)); } 
		catch (IOException e) { System.out.println("Cannot load background"); }
		try { img_m = ImageIO.read(new File(MEAT_PATH)); } 
		catch (IOException e) { System.out.println("Cannot load meat"); }
   }		

   public void paintComponent(Graphics g)
   {
	   	super.paintComponent(g);	
	   	
	   	if(bgr && (img!=null))
            g.drawImage(img, 0, 0, getWidth(), getHeight(), this);

	   	if(Food == EFoodType.MEAT)
	   		g.drawImage(img_m, getWidth()/2-20, getHeight()/2-20, 40, 40, this);
	    
	   	if((Food == EFoodType.VEGETABLE) && (forFood != null))
	   		forFood.drawObject(g);

	   	synchronized(this) {
		   	for(Animal an : animals)
		    	an.drawObject(g);
	   	}
   }
   
   public void setBackgr(int num) {
	   switch(num) {
	   case 0:
		   setBackground(new Color(255,255,255));
		   bgr = false; 
		   break;
	   case 1:
		   setBackground(new Color(0,155,0));
		   bgr = false; 
		   break;
	   default:
			bgr = true;   
	   }
	   repaint();
   }
   
   synchronized public EFoodType checkFood()
   {
	   return Food;
   }

   /**
    * CallBack function 
    * @param f
    */
   synchronized public void eatFood(Animal an)
   {
	   if(Food != EFoodType.NOTFOOD)
	   {
		    if(Food == EFoodType.VEGETABLE)
		    	forFood = null;
		   	Food = EFoodType.NOTFOOD;
	   		an.eatInc();
	   		totalCount++;
	   		System.out.println("The "+an.getName()+" with "+an.getColor()+" color and size "+an.getSize()+" ate food.");
	   }
	   else
	   {
		   System.out.println("The "+an.getName()+" with "+an.getColor()+" color and size "+an.getSize()+" missed food.");
	   }
   }

   public void addDialog()
   {
	   if(animals.size()==MAX_ANIMAL_NUMBER) {
		   JOptionPane.showMessageDialog(this, "You cannot add more than "+MAX_ANIMAL_NUMBER+" animals");
	   }
	   else {
		   AddAnimalDialog dial = new AddAnimalDialog(this,"Add an animal to aquarium");
		   dial.setVisible(true);
	   }
   }
   
   public void addAnimal(String animal, int sz, int hor, int ver, String c)
   {
	   Animal an = null;
	   if(animal.equals("Elephant"))
		   an = new Elephant(sz,0,0,hor,ver,c,this);
	   else if (animal.equals("Lion"))
		   an = new Lion(sz,0,0,hor,ver,c,this);
	   else if (animal.equals("Turtle")) 
		   an = new Turtle(sz,0,0,hor,ver,c,this);
	   else if (animal.equals("Bear"))
		   an = new Bear(sz,0,0,hor,ver,c,this);
	   else 
		   an = new Giraffe(sz,0,0,hor,ver,c,this);
	   animals.add(an);
	   an.start();
   }

	public void start() {
	    for(Animal an : animals)
	    	an.setResume();
   }

 	public void stop() {
	    for(Animal an : animals)
	    	an.setSuspend();
   }

   synchronized public void clear()
   {
	   for(Animal an : animals)
	    an.interrupt();
	   animals.clear();
	   Food = EFoodType.NOTFOOD;
	   forFood = null;
	   totalCount = 0;
	   repaint();
   }

   synchronized public void preyEating(Animal predator, Animal prey)
   {
	   predator.eatInc();
	   totalCount -= (prey.getEatCount()-1);
   }

   synchronized public void addFood()
   {
	   if(Food == EFoodType.NOTFOOD){
		   Object[] options = {"Meat", "Cabbage", "Lettuce"}; 
		   int n = JOptionPane.showOptionDialog(frame, 
		   		"Please choose food", "Food for animals", 
		   		JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, 
		   		null, options, options[2]);
		   switch(n) {
		   case 0: // Meat
			   Food = EFoodType.MEAT;
			   break;
		   case 1: // Cabbage
			   Food = EFoodType.VEGETABLE;
			   forFood = new Cabbage(this);
			   break;
		   default: // Lettuce
			   Food = EFoodType.VEGETABLE;
			   forFood = new Lettuce(this);
			   break;
		   }
	   }
	   else {
		   Food = EFoodType.NOTFOOD;
		   forFood = null;
	   }
	   repaint();
  }
   
   public void info()
   {  	 
	   if(isTableVisible == false)
	   {
		  int i=0;
		  int sz = animals.size();

		  String[] columnNames = {"Animal","Color","Weight","Hor. speed","Ver. speed","Eat counter"};
	      String [][] data = new String[sz+1][columnNames.length];
		  for(Animal an : animals)
	      {
	    	  data[i][0] = an.getName();
	    	  data[i][1] = an.getColor();
	    	  data[i][2] = new Integer((int)(an.getWeight())).toString();
		      data[i][3] = new Integer(an.getHorSpeed()).toString();
		      data[i][4] = new Integer(an.getVerSpeed()).toString();
	    	  data[i][5] = new Integer(an.getEatCount()).toString();
	    	  i++;
	      }
	      data[i][0] = "Total";
	      data[i][5] = new Integer(totalCount).toString();
	      
	      JTable table = new JTable(data, columnNames);
	      scrollPane = new JScrollPane(table);
	      scrollPane.setSize(450,table.getRowHeight()*(sz+1)+24);
	      add( scrollPane, BorderLayout.CENTER );
	      isTableVisible = true;
	   }
	   else
	   {
		   isTableVisible = false;
	   }
	   scrollPane.setVisible(isTableVisible);
       repaint();
   }

   public void destroy()
   { 
	  for(Animal an : animals)
		  an.interrupt();
	  controller.interrupt();
      System.exit(0);
   }
   
   public void actionPerformed(ActionEvent e)
   {
	if(e.getSource() == b_num[0]) // "Add Animal"
		addDialog();
	else if(e.getSource() == b_num[1]) // "Sleep"
		stop();
	else if(e.getSource() == b_num[2]) // "Wake up"
		start();
	else if(e.getSource() == b_num[3]) // "Clear"
		clear();
	else if(e.getSource() == b_num[4]) // "Food"
		addFood();
	else if(e.getSource() == b_num[5]) // "Info"
		info();
	else if(e.getSource() == b_num[6]) // "Exit"
		destroy();
   }

	public void run() {
		while(true) {
			if(isChange())
				repaint();
			
			boolean prey_eaten = false;
			synchronized(this) {
				for(Animal predator : animals) {
					for(Animal prey : animals) {
						if(predator != prey && predator.getDiet().canEat(prey) && predator.getWeight()/prey.getWeight() >= 2 &&
						   (Math.abs(predator.getLocation().getX() - prey.getLocation().getX()) < prey.getSize()) &&
						   (Math.abs(predator.getLocation().getY() - prey.getLocation().getY()) < prey.getSize())) {
								preyEating(predator,prey);
								System.out.print("The "+predator+" cought up the "+prey+" ==> ");
								prey.interrupt();
								animals.remove(prey);
								repaint();
								//JOptionPane.showMessageDialog(frame, ""+prey+" killed by "+predator);
								prey_eaten = true;
								break;
						}
					}
					if(prey_eaten)
						break;
				}
			}
			try {
				Thread.sleep(1000/RESOLUTION);
			} catch (InterruptedException e) {
				return;
			}
		}
	}

	public boolean isChange() {
		boolean rc = false;
		for(Animal an : animals) {
		    if(an.getChanges()){
		    	rc = true;
		    	an.setChanges(false);
			}
	    }
		return rc;
	}
}