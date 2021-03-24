import java.awt.*;
public class MovingGraphics {
	public double x;
	public double y;
	public double size;
	public Color color; 

	public MovingGraphics (double x, double y, double s, Color c){
		this.x = x;
		this.y = y;
		size = s;
		color = c;

	}

	public void leftX(){
		x += 1;
	}
	public void rightX(){
		x -= 1;
	}
	public double upY(){
                
		y += 1;
                return y;
	}
	public double downY(){
		y -= 1;
                return y;
	}
        public void setX(int n){
            x = n;
        }
        public void setY(int n){
            y = n;
        }
}