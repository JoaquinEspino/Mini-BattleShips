/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ASUS
 */
import java.awt.*;
import java.awt.geom.*;
public class Blinker extends MovingGraphics{
    private int j = 255;
    public Blinker(double x, double y, double s, Color c){
        super(x, y, s, c);
    }   
        public void draw(Graphics2D g2d){
		Ellipse2D.Double blinker = new Ellipse2D.Double(x, y, size, size);
                g2d.setColor( color );
                g2d.fill(blinker);
	}
        
        public void makeTransparent(){
            j = 0;
        }
        public void makeVisible(){
            j = 255;
        }
            
}
