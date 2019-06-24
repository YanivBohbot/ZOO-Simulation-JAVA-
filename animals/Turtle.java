package animals;

import diet.Herbivore;
import graphics.ZooPanel;
import mobility.Point;

/**
 * @author Sergey Tregulov
 *
 */
public class Turtle extends Animal {

	public Turtle(int s,int x, int y, int h, int v, String c, ZooPanel p) {
		 super("Turtle",s/2,s/2,h,v,c,p);
		 setLocation(new Point(x,y));
		 setDiet(new Herbivore());
		 loadImages("trt");
		 cor_x1 = size/4;
		 cor_x2 = (int) (-size/4);
		 cor_x3 = (int) (-size*0.25);
		 cor_x4 = (int) (size*0.25);
		 cor_y1 = (int) (-30-size*0.125);
		 cor_y3 = size/8;
		 cor_x5 = -size;
		 cor_y5 = cor_y6 = -size/4;
		 cor_h = (int)(size*0.68);
	 }
}
