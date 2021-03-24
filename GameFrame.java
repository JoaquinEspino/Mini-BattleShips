/**
	GameFrame is a class wherein the Player class is instantiated. It sets up the frame for the Player class.

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

public class GameFrame extends JFrame
{
	// instance fields are declared
	private int width, height;
	
	private Player player;

	public GameFrame( int w, int h ) // handles initialization
	{
		width = w;
		height = h;

		player = new Player( 500, 500 ); // instantiates the Player class
		player.connectToServer(); // establishes the connection to the GameServer
		player.setUpFrame();
	}
}
