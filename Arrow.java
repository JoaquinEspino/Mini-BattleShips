import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
public class Arrow extends MovingGraphics implements ActionListener{
    private Timer clock;
    public Arrow(double x, double y, double s, Color c){
        super(x, y, s, c);
        clock = new Timer( 1000, this );
    }
	public void draw(Graphics2D g2d){
            clock.start();
		Rectangle2D.Double body = new Rectangle2D.Double(x, y, size, size);
                g2d.setColor(color);
                g2d.fill(body);
		Path2D.Double head = new Path2D.Double();
		head.moveTo(x-(size*1/2), y+size);
		head.lineTo(x+(size*1/2), y+size+(size+1/2));
		head.lineTo(x+size+(size*1/2), y+size);
		head.lineTo(x-(size*1/2), y+size);
		g2d.setColor(color);
                g2d.fill(head);
	}
        public void actionPerformed( ActionEvent ae ){
            upY();
            //downY();
        }
}