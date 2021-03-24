/**
	GameCanvas is a class that is responsible for drawing the game visuals. It contains the elements needed to create the grid for the game.

	@Alysha Canonigo Columbres 186050 and Joaquin Espino 181877
	@May 17, 2019
**/

/*
I have not discussed the Java language code in my program with anyone other than my instructor or the teaching assistants assigned to this course.
I have not used Java language code obtained from another student, or any other unauthorized source, either modified or unmodified.
If any Java language code or documentation used in my program was obtained from another source, such as a textbook or course notes, that has been clearly noted with a proper citation in the comments of my program.
*/

import javax.swing.*; // "Java's framework for programming lightweight GUI components" ; to be able to use JFrame and swing components
import java.awt.*; // "Java's framework for programming GUIs" (and graphics) ; to be able to use AWT components
import java.awt.geom.*; // for drawing 2D shapes
import java.util.*;
import javax.swing.Timer;
import java.awt.event.*;

public class GameCanvas extends JComponent
{
	// instance fields are declared
	private int width, height;
	private Color backgroundColor;
	private Font boxNum;
        private Arrow arrow;
        private double x, y, blinkCounter;
        private boolean b1,b2,b3,b4;
        private boolean changeDir = true;
        private boolean changeBlink = true;
        private boolean blink1, blink2, blink3, blink4;
        private Blinker blinker;
        private int blinkValue;

	public GameCanvas( int w, int h ) // handles initialization
	{
            width = w;
            height = h;
                
            b1 = false; // boolean values for the drawing of the arrow
            b2 = false;
            b3 = false;
            b4 = false;
            blink1 = false;
            blink2 = false;
            blink3 = false;
            blink4 = false;
            x = 0;
            y = 0;
            blinkCounter = 0;
            blinkValue = 0;
            
            ActionListener animate = new ActionListener(){
              public void actionPerformed(ActionEvent ae){
              
              revalidate();
              repaint();    
              }  
            };
            Timer timer = new Timer(50, animate);
            timer.start();

            setPreferredSize( new Dimension( width, height ) ); // sets the preferred size of the canvas
	}

	@Override
	protected void paintComponent( Graphics g )
	{
            Graphics2D g2d = ( Graphics2D ) g;

            RenderingHints rh = new RenderingHints( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
            g2d.setRenderingHints( rh );

            Rectangle2D.Double grid = new Rectangle2D.Double( 10, 10, 480, 400 ); // draws the outer grid borders
            g2d.setColor( Color.BLACK );
            g2d.setStroke( new BasicStroke( 2 ) ); // https://stackoverflow.com/questions/2839508/java2d-increase-the-line-width
            g2d.draw( grid );

            Line2D.Double vertLine = new Line2D.Double( 255, 10, 255, 410 ); // draws the vertical inner grid line
            g2d.setColor( Color.BLACK );
            g2d.setStroke( new BasicStroke( 2 ) );
            g2d.draw( vertLine );

            Line2D.Double horiLine = new Line2D.Double( 10, 210, 490, 210 ); // draws the horizontal inner grid line
            g2d.setColor( Color.BLACK );
            g2d.setStroke( new BasicStroke( 2 ) );
            g2d.draw( horiLine );

            boxNum = new Font( "Serif", Font.BOLD, 40 ); // sets the box letters for each box
            g2d.setFont( boxNum );
            g2d.drawString( "1", 115, 115 );
            g2d.drawString( "2", 365, 115 );
            g2d.drawString( "3", 115, 325 );
            g2d.drawString( "4", 365, 325 );
            if (blink1 == true){
                blinker = new Blinker(35, 80, 20, new Color(255, 0, 0, blinkValue));
                blinker.draw( g2d );
                if(changeBlink == true){
                    blinkCounter++;
                    blinkValue = 0;
                    if(blinkCounter == 5){
                        changeBlink = false;    
                    }
                }else{
                    blinkValue = 255;
                    if (blinkCounter == 0){
                        changeBlink = true;
                    }
                    blinkCounter--;
                }
                
            }
            if (blink2 == true){
                blinker = new Blinker(275, 80, 20, new Color(255, 0, 0, blinkValue));
                blinker.draw( g2d );
                if(changeBlink == true){
                    blinkCounter++;
                    blinkValue = 0;
                    if(blinkCounter == 5){
                        changeBlink = false;    
                    }
                }else{
                    blinkValue = 255;
                    if (blinkCounter == 0){
                        changeBlink = true;
                    }
                    blinkCounter--;
                }
                
            }
            if (blink3 == true){
                blinker = new Blinker(35, 310, 20, new Color(255, 0, 0, blinkValue));
                blinker.draw( g2d );
                if(changeBlink == true){
                    blinkCounter++;
                    blinkValue = 0;
                    if(blinkCounter == 5){
                        changeBlink = false;    
                    }
                }else{
                    blinkValue = 255;
                    if (blinkCounter == 0){
                        changeBlink = true;
                    }
                    blinkCounter--;
                }
                
            }
            if (blink4 == true){
                blinker = new Blinker(275, 310, 20, new Color(255, 0, 0, blinkValue));
                blinker.draw( g2d );
                if(changeBlink == true){
                    blinkCounter++;
                    blinkValue = 0;
                    if(blinkCounter == 5){
                        changeBlink = false;    
                    }
                }else{
                    blinkValue = 255;
                    if (blinkCounter == 0){
                        changeBlink = true;
                    }
                    blinkCounter--;
                }
                
            }
            if( b1 == true )
            {
                arrow = new Arrow( x+30, y+20, 30, Color.RED );
                arrow.draw( g2d );
                if (changeDir == true){
                    y = arrow.upY()-20;
                    if(y == 5){
                    changeDir = false;
                    }
                }
                else{
                    y = arrow.downY()-20;;
                    if(y == 0 ){
                        changeDir = true;
                    }
                }
            }
                
            if( b2 == true )
            {
                arrow = new Arrow( x+270, y+20, 30, Color.RED );
                arrow.draw( g2d );
                if (changeDir == true){
                    y = arrow.upY()-20;
                    if(y == 5){
                    changeDir = false;
                    }
                }
                else{
                    y = arrow.downY()-20;
                    if(y == 0 ){
                        changeDir = true;
                    }
                }
                
                
            }
                
            if( b3 == true )
            {
                arrow = new Arrow( x+30, y+230, 30, Color.RED );
                arrow.draw( g2d );
                if (changeDir == true){
                    y = arrow.upY()-230;
                    if(y == 5){
                    changeDir = false;
                    }
                }
                else{
                    y = arrow.downY()-230;
                    if(y == 0 ){
                        changeDir = true;
                    }
                }
            }
                
            if( b4 == true )
            {
                arrow = new Arrow( x+270, y+230, 30, Color.RED );
                arrow.draw( g2d );
                if (changeDir == true){
                    y = arrow.upY()-230;
                    if(y == 5){
                    changeDir = false;
                    }
                }
                else{
                    y = arrow.downY()-230;
                    if(y == 0 ){
                        changeDir = true;
                    }
                }
            }
                
               
	}
        
        public void goTo1(){
            b1 = true;
            b2 = false;
            b3 = false;
            b4 = false;
        }
        public void goTo2(){
            b1 = false;
            b2 = true;
            b3 = false;
            b4 = false;
        }
        public void goTo3(){
            b1 = false;
            b2 = false;
            b3 = true;
            b4 = false;
        }
        public void goTo4(){
            b1 = false;
            b2 = false;
            b3 = false;
            b4 = true;
        }
        public void blinkTo1(){
            blink1 = true;
            blink2 = false;
            blink3 = false;
            blink4 = false;
        }
        public void blinkTo2(){
            blink1 = false;
            blink2 = true;
            blink3 = false;
            blink4 = false;
        }
        public void blinkTo3(){
            blink1 = false;
            blink2 = false;
            blink3 = true;
            blink4 = false;
        }
        public void blinkTo4(){
            blink1 = false;
            blink2 = false;
            blink3 = false;
            blink4 = true;
        }
        
        
}