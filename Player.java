/**
	Player is a class wherein the functionality of the game is programmed. It establishes a network connection with the GameServer in order to execute the game.

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
import java.awt.event.*; // for "events fired by Swing components"
import java.io.*; // responsible for "system input and output through data streams, serialization and the file system"
import java.net.*; // "classes for implementing networking applications" (http://www.dcs.gla.ac.uk/~pd/JavaRefCard/private/packagelist.pdf)

public class Player extends JFrame
{
	// instance fields are declared
	private int width, height, playerID, otherPlayer, round, triggerInt, counter, p1Points, p2Points;
	private Container contentPane;

	private GameCanvas gameCanvas;

	private JButton playGame;
	private JLayeredPane layeredPane;
	private JPanel panel1, panel2, panel3, panel4;
	private JLabel gameMessage, imageLabel;

	private boolean chooseMode;
        private ImageIcon image;

    private ClientSideConnection csc;

	public Player( int w, int h ) // handles initialization
	{
                
		width = w;
		height = h;

		contentPane = this.getContentPane();

		p1Points = 0;
		p2Points = 0;
        round = 1;
                
	}

	public void setUpFrame()
	{
		this.setPreferredSize( new Dimension( width, height ) );
		this.setTitle( "Player #" + playerID );

		contentPane.setLayout( new BorderLayout() ); // opening screen is in BorderLayout format

        image = new ImageIcon( getClass().getResource( "title card.jpg" ) ); // displays the title card of the game
        imageLabel = new JLabel( image );
        contentPane.add( imageLabel, "Center" );

		playGame = new JButton( "Start" ); // instantiates the button that will start the game
		contentPane.add( playGame, "South" ); // adds button to bottom of screen
		playGame.addActionListener( new PlayGameButtonListener() ); // adds the PlayGameButtonListener to the playGame button

		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		pack();
		setVisible( true );
	}

	class PlayGameButtonListener implements ActionListener
	/*
		This inner class implements the ActionListener interface. It sends the player from the title screen to the main grid playing screen.
	*/
	{
		public void actionPerformed( ActionEvent ae )
		{
			contentPane.removeAll();

			layeredPane = new JLayeredPane(); // "base" layer of game screen
			contentPane.add( layeredPane, "Center" );

			gameCanvas = new GameCanvas( 500, 500 ); // first layer of the game screen, on which the mouse panels will be placed
			Dimension gameCanvasSize = gameCanvas.getPreferredSize();
			gameCanvas.setBounds( 0, 0, 500, 500 );

			layeredPane.add( gameCanvas, 0, 0 );

			gameMessage = new JLabel();
			gameMessage.setBounds( 30, 395, 500, 100 );
                        
			if( playerID == 1 )
			{
				gameMessage.setText( "You are player #1. Please select a box." ); // game message at the bottom of the screen
				otherPlayer = 2;
                System.out.println( "Player #" + playerID + " will start the game." );     
                Thread pt = new Thread( new Runnable()
                {
                	public void run()
                	{
                		updatePoints();
                		System.out.println("Player #1 = " + p1Points + "Player # 2 = " + p2Points);

                	}
                } );
                pt.start();               
			}

			else
			{
				gameMessage.setText( "You are player #2. Wait for your turn." );
				otherPlayer = 1;
                System.out.println( "Player #" + playerID + " waits for Player #" + otherPlayer + " to start the game." );
                
                Thread th = new Thread( new Runnable() // runs the updateText() method of CCS class so player #2 will receive the trigger number that will change the gameMessage on their window when player #1 clicks a box
                {
                    public void run()
                    {
                    	updateText(); // player #2's gameMessage will be updated after player #1 clicks a box
                    }
                                        
                } );

                th.start(); // starts the thread
			}

			layeredPane.add( gameMessage, 1, 0 );

			panel1 = new JPanel(); // for the MouseListener at panel 1
			panel1.setBackground( new Color( 0, 0, 0, 0 ) ); // SET OPACITY : https://stackoverflow.com/questions/3562225/java-set-opacity-in-jpanel
			Dimension panel1Size = panel1.getPreferredSize();
			panel1.setBounds( 10, 10, 245, 200 );

			layeredPane.add( panel1, 1, 0 );

			panel1.addMouseListener( panel1Action );

			panel2 = new JPanel(); // for the MouseListener at panel 2
			panel2.setBackground( new Color( 0, 0, 0, 0 ) );
			Dimension panel2Size = panel2.getPreferredSize();
			panel2.setBounds( 255, 10, 235, 200 );

			layeredPane.add( panel2, 1, 0 );

			panel2.addMouseListener( panel2Action );

			panel3 = new JPanel(); // for the MouseListener at panel 3
			panel3.setBackground( new Color( 0, 0, 0, 0 ) );
			Dimension panel3Size = panel3.getPreferredSize();
			panel3.setBounds( 10, 210, 245, 200 );

			layeredPane.add( panel3, 1, 0 );

			panel3.addMouseListener( panel3Action );

			panel4 = new JPanel(); // for the MouseListener at panel 4
			panel4.setBackground( new Color( 0, 0, 0, 0 ) );
			Dimension panel4Size = panel4.getPreferredSize();
			panel4.setBounds( 255, 210, 235, 200 );

			layeredPane.add( panel4, 1, 0 );

			panel4.addMouseListener( panel4Action );

			contentPane.revalidate();
			contentPane.repaint();
		}
	}

	MouseListener panel1Action = new MouseListener() {
	/*
		This anonymous MouseListener class is responsible for handling events when box 1 in the game is clicked. It executes different processes depending on the round number and the player.
	*/
                
		public void mouseClicked( MouseEvent me )
		{
                    gameCanvas.blinkTo1();
			if( round == 1 || round == 3 ) // rounds wherein player #1 chooses a box and player #2 waits
			{	
                triggerInt = 0; // trigger number that will cause a player's gameMessage to change

				if( playerID == 1 )
				{
					int boxClickedBy1 = 1; // field that assigns the box its number

					gameMessage.setText( "You clicked box #1! Wait for player #" + otherPlayer + " to choose a box." );

					csc.sendInt( triggerInt ); // sends trigger number to GameServer
                    csc.sendInt( boxClickedBy1 ); // sends box number to GameServer

                    System.out.println( "You clicked " + boxClickedBy1 + "!" );

                    Thread th = new Thread( new Runnable()
                    {
                        public void run()
                        {
                            updateText();
                        }
                    } );
                    
                    th.start();
                }

				else
				{
					int boxChosenBy2 = 1;
					int boxNumFrom1 = csc.receiveInt();
					System.out.println( "Received box number from GameServer: " + boxNumFrom1 ); // REMOVE
                    
                    if( boxNumFrom1 == boxChosenBy2 )
                    {
                        p2Points += 1;
                        System.out.println( "------ROUND UPDATE------" );
						System.out.println( "Player #1 points: " + p1Points + " || Player #2 points: " + p2Points );
						
                        csc.sendPoints( p1Points );
                        csc.sendPoints( p2Points );
                        /*
                        p1Points = csc.receivePoints();
                        p2Points = csc.receivePoints();
						*/
                        gameMessage.setText( "You guessed right! You now have " + p2Points + " point(s). Your turn to choose." );
                    }

                    else
                    {
                        p1Points += 1;
                        System.out.println( "------ROUND UPDATE------" );
						System.out.println( "Player #1 points: " + p1Points + " || Player #2 points: " + p2Points );
						
                        csc.sendPoints( p1Points );
                        csc.sendPoints( p2Points );
						/*
                        p1Points = csc.receivePoints();
                        p2Points = csc.receivePoints();
						*/
                        gameMessage.setText( "You guessed wrong! Player #1 now has " + p1Points + " point(s). Your turn to choose." );
                    }
                }
			}

            else if ( round == 2 || round == 4 )
			{
				if( playerID == 1 )
				{
					int boxChosenBy1 = 1;
                    int boxNumFrom2 = csc.receiveInt();
                    System.out.println( "Received box number from GameServer: " + boxNumFrom2 ); // REMOVE

                    gameMessage.setText( "Player #2 has chosen a box! Try to guess which one it is." );

                    if( boxNumFrom2 == boxChosenBy1 )
                    {
                        p1Points += 1;
                        System.out.println( "------ROUND UPDATE------" );
						System.out.println( "Player #1 points: " + p1Points + " || Player #2 points: " + p2Points );
						
                        csc.sendPoints( p1Points );
                        csc.sendPoints( p2Points );
                        /*
                        p1Points = csc.receivePoints();
                        p2Points = csc.receivePoints();
						*/
                        gameMessage.setText( "You guessed right! You now have "+ p1Points + " point(s). Your turn to choose." );

                        if( round == 4 )
                        {
                            checkWinner();
                        }
                    }

                    else
                    {
                        p2Points += 1;
                        System.out.println( "------ROUND UPDATE------" );
						System.out.println( "Player #1 points: " + p1Points + " || Player #2 points: " + p2Points );
						
                        csc.sendPoints( p1Points );
                        csc.sendPoints( p2Points );
                        /*
                        p1Points = csc.receivePoints();
                        p2Points = csc.receivePoints();
						*/
                        gameMessage.setText( "You guessed wrong! Player 1 now has "+ p2Points + " point(s). Your turn to choose." );

                        if( round == 4 )
                        {
                            checkWinner();
                        }
                    }
                }

				else
				{
					int boxClickedBy2 = 1;

                    gameMessage.setText( "You clicked box #1! Wait for player #" + otherPlayer + " to choose a box." );

                    csc.sendInt( triggerInt );
                    csc.sendInt( boxClickedBy2 );

                    System.out.println( "You clicked " + boxClickedBy2 + "!" );
                                    
                    Thread th = new Thread( new Runnable()
                    {
                        public void run()
                        {
                            updateText();
                        }
                                        
                    } );
                    
                    th.start();
                    
                    if( round == 4 )
                    {
                        gameMessage.setText( "You clicked box #1! Check player #" + otherPlayer + "'s window for results." );
                        csc.sendPoints( p2Points );
                    }
				}
            }

			round++;
			System.out.println( "Now going into round " + round + "!" );
		}
                

		public void mouseExited( MouseEvent me ) {} // these methods are only here because MouseListener is an interface and all its methods must be defined
		public void mousePressed( MouseEvent me ) {}
        public void mouseEntered( MouseEvent me ) {
            gameCanvas.goTo1();
        }
        public void mouseReleased( MouseEvent me ) {}
	};

	MouseListener panel2Action = new MouseListener() {
	/*
		This anonymous MouseListener class is responsible for handling events when box 2 in the game is clicked. It executes different processes depending on the round number and the player.
	*/
		public void mouseClicked( MouseEvent me )
		{
                    gameCanvas.blinkTo2();
            if( round == 1 || round == 3 ) // rounds wherein player #1 chooses a box and player #2 waits
			{	
                triggerInt = 0; // trigger number that will cause a player's gameMessage to change

				if( playerID == 1 )
				{
					int boxClickedBy1 = 2; // field that assigns the box its number

					gameMessage.setText( "You clicked box #1! Wait for player #" + otherPlayer + " to choose a box." );

					csc.sendInt( triggerInt ); // sends trigger number to GameServer
                    csc.sendInt( boxClickedBy1 ); // sends box number to GameServer

                    System.out.println( "You clicked " + boxClickedBy1 + "!" );

                    Thread th = new Thread( new Runnable()
                    {
                        public void run()
                        {
                            updateText();
                        }
                    } );
                    
                    th.start();
                }

				else
				{
					int boxChosenBy2 = 2;
					int boxNumFrom1 = csc.receiveInt();
					System.out.println( "Received box number from GameServer: " + boxNumFrom1 ); // REMOVE
                    
                    if( boxNumFrom1 == boxChosenBy2 )
                    {
                        p2Points += 1;
                        System.out.println( "------ROUND UPDATE------" );
						System.out.println( "Player #1 points: " + p1Points + " || Player #2 points: " + p2Points );
						
                        csc.sendPoints( p1Points );
                        csc.sendPoints( p2Points );
                        /*
                        p1Points = csc.receivePoints();
                        p2Points = csc.receivePoints();
						*/
                        gameMessage.setText( "You guessed right! You now have " + p2Points + " point(s). Your turn to choose." );
                    }

                    else
                    {
                        p1Points += 1;
                        System.out.println( "------ROUND UPDATE------" );
						System.out.println( "Player #1 points: " + p1Points + " || Player #2 points: " + p2Points );
						
                        csc.sendPoints( p1Points );
                        csc.sendPoints( p2Points );
						/*
                        p1Points = csc.receivePoints();
                        p2Points = csc.receivePoints();
						*/
                        gameMessage.setText( "You guessed wrong! Player #1 now has " + p1Points + " point(s). Your turn to choose." );
                    }
                }
			}

            else if ( round == 2 || round == 4 )
			{
				if( playerID == 1 )
				{
					int boxChosenBy1 = 2;
                    int boxNumFrom2 = csc.receiveInt();
                    System.out.println( "Received box number from GameServer: " + boxNumFrom2 ); // REMOVE

                    gameMessage.setText( "Player #2 has chosen a box! Try to guess which one it is." );

                    if( boxNumFrom2 == boxChosenBy1 )
                    {
                        p1Points += 1;
                        System.out.println( "------ROUND UPDATE------" );
						System.out.println( "Player #1 points: " + p1Points + " || Player #2 points: " + p2Points );
						
                        csc.sendPoints( p1Points );
                        csc.sendPoints( p2Points );
                        /*
                        p1Points = csc.receivePoints();
                        p2Points = csc.receivePoints();
						*/
                        gameMessage.setText( "You guessed right! You now have "+ p1Points + " point(s). Your turn to choose." );

                        if( round == 4 )
                        {
                            checkWinner();
                        }
                    }

                    else
                    {
                        p2Points += 1;
                        System.out.println( "------ROUND UPDATE------" );
						System.out.println( "Player #1 points: " + p1Points + " || Player #2 points: " + p2Points );
						
                        csc.sendPoints( p1Points );
                        csc.sendPoints( p2Points );
                        /*
                        p1Points = csc.receivePoints();
                        p2Points = csc.receivePoints();
						*/
                        gameMessage.setText( "You guessed wrong! Player 1 now has "+ p2Points + " point(s). Your turn to choose." );

                        if( round == 4 )
                        {
                            checkWinner();
                        }
                    }
                }

				else
				{
					int boxClickedBy2 = 2;

                    gameMessage.setText( "You clicked box #1! Wait for player #" + otherPlayer + " to choose a box." );

                    csc.sendInt( triggerInt );
                    csc.sendInt( boxClickedBy2 );

                    System.out.println( "You clicked " + boxClickedBy2 + "!" );
                                    
                    Thread th = new Thread( new Runnable()
                    {
                        public void run()
                        {
                            updateText();
                        }
                                        
                    } );
                    
                    th.start();
                    
                    if( round == 4 )
                    {
                        gameMessage.setText( "You clicked box #1! Check player #" + otherPlayer + "'s window for results." );
                        csc.sendPoints( p2Points );
                    }
				}
            }

			round++;
			System.out.println( "Now going into round " + round + "!" );
		}

		public void mouseExited( MouseEvent me ) {}
		public void mousePressed( MouseEvent me ) {}
        public void mouseEntered( MouseEvent me ) {
            gameCanvas.goTo2();
        }
        public void mouseReleased( MouseEvent me ) {}
	};

	MouseListener panel3Action = new MouseListener() {
	/*
		This anonymous MouseListener class is responsible for handling events when box 3 in the game is clicked. It executes different processes depending on the round number and the player.
	*/
		public void mouseClicked( MouseEvent me )
		{
                    gameCanvas.blinkTo3();
            if( round == 1 || round == 3 ) // rounds wherein player #1 chooses a box and player #2 waits
			{	
                triggerInt = 0; // trigger number that will cause a player's gameMessage to change

				if( playerID == 1 )
				{
					int boxClickedBy1 = 3; // field that assigns the box its number

					gameMessage.setText( "You clicked box #1! Wait for player #" + otherPlayer + " to choose a box." );

					csc.sendInt( triggerInt ); // sends trigger number to GameServer
                    csc.sendInt( boxClickedBy1 ); // sends box number to GameServer

                    System.out.println( "You clicked " + boxClickedBy1 + "!" );

                    Thread th = new Thread( new Runnable()
                    {
                        public void run()
                        {
                            updateText();
                        }
                    } );
                    
                    th.start();
                }

				else
				{
					int boxChosenBy2 = 3;
					int boxNumFrom1 = csc.receiveInt();
					System.out.println( "Received box number from GameServer: " + boxNumFrom1 ); // REMOVE
                    
                    if( boxNumFrom1 == boxChosenBy2 )
                    {
                        p2Points += 1;
                        System.out.println( "------ROUND UPDATE------" );
						System.out.println( "Player #1 points: " + p1Points + " || Player #2 points: " + p2Points );
						
                        csc.sendPoints( p1Points );
                        csc.sendPoints( p2Points );
                        /*
                        p1Points = csc.receivePoints();
                        p2Points = csc.receivePoints();
						*/
                        gameMessage.setText( "You guessed right! You now have " + p2Points + " point(s). Your turn to choose." );
                    }

                    else
                    {
                        p1Points += 1;
                        System.out.println( "------ROUND UPDATE------" );
						System.out.println( "Player #1 points: " + p1Points + " || Player #2 points: " + p2Points );
						
                        csc.sendPoints( p1Points );
                        csc.sendPoints( p2Points );
                        /*
                        p1Points = csc.receivePoints();
                        p2Points = csc.receivePoints();
						*/
                        gameMessage.setText( "You guessed wrong! Player #1 now has " + p1Points + " point(s). Your turn to choose." );
                    }
                }
			}

            else if ( round == 2 || round == 4 )
			{
				if( playerID == 1 )
				{
					int boxChosenBy1 = 3;
                    int boxNumFrom2 = csc.receiveInt();
                    System.out.println( "Received box number from GameServer: " + boxNumFrom2 ); // REMOVE

                    gameMessage.setText( "Player #2 has chosen a box! Try to guess which one it is." );

                    if( boxNumFrom2 == boxChosenBy1 )
                    {
                        p1Points += 1;
                        System.out.println( "------ROUND UPDATE------" );
						System.out.println( "Player #1 points: " + p1Points + " || Player #2 points: " + p2Points );
						
                        csc.sendPoints( p1Points );
                        csc.sendPoints( p2Points );
                        /*
                        p1Points = csc.receivePoints();
                        p2Points = csc.receivePoints();
						*/
                        gameMessage.setText( "You guessed right! You now have "+ p1Points + " point(s). Your turn to choose." );

                        if( round == 4 )
                        {
                            checkWinner();
                        }
                    }

                    else
                    {
                        p2Points += 1;
                        System.out.println( "------ROUND UPDATE------" );
						System.out.println( "Player #1 points: " + p1Points + " || Player #2 points: " + p2Points );
						
                        csc.sendPoints( p1Points );
                        csc.sendPoints( p2Points );
						/*
                        p1Points = csc.receivePoints();
                        p2Points = csc.receivePoints();
						*/
                        gameMessage.setText( "You guessed wrong! Player 1 now has "+ p2Points + " point(s). Your turn to choose." );

                        if( round == 4 )
                        {
                            checkWinner();
                        }
                    }
                }

				else
				{
					int boxClickedBy2 = 3;

                    gameMessage.setText( "You clicked box #1! Wait for player #" + otherPlayer + " to choose a box." );

                    csc.sendInt( triggerInt );
                    csc.sendInt( boxClickedBy2 );

                    System.out.println( "You clicked " + boxClickedBy2 + "!" );
                                    
                    Thread th = new Thread( new Runnable()
                    {
                        public void run()
                        {
                            updateText();
                        }
                                        
                    } );
                    
                    th.start();
                    
                    if( round == 4 )
                    {
                        gameMessage.setText( "You clicked box #1! Check player #" + otherPlayer + "'s window for results." );
                        csc.sendPoints( p2Points );
                    }
				}
            }

			round++;
			System.out.println( "Now going into round " + round + "!" );
		}

		public void mouseExited( MouseEvent me ) {}
		public void mousePressed( MouseEvent me ) {}
        public void mouseEntered( MouseEvent me ) {
            gameCanvas.goTo3();
        }
        public void mouseReleased( MouseEvent me ) {}
	};

	MouseListener panel4Action = new MouseListener() {
	/*
		This anonymous MouseListener class is responsible for handling events when box 4 in the game is clicked. It executes different processes depending on the round number and the player.
	*/
		public void mouseClicked( MouseEvent me )
		{
                    gameCanvas.blinkTo4();
            if( round == 1 || round == 3 ) // rounds wherein player #1 chooses a box and player #2 waits
			{	
                triggerInt = 0; // trigger number that will cause a player's gameMessage to change

				if( playerID == 1 )
				{
					int boxClickedBy1 = 4; // field that assigns the box its number

					gameMessage.setText( "You clicked box #1! Wait for player #" + otherPlayer + " to choose a box." );

					csc.sendInt( triggerInt ); // sends trigger number to GameServer
                    csc.sendInt( boxClickedBy1 ); // sends box number to GameServer

                    System.out.println( "You clicked " + boxClickedBy1 + "!" );

                    Thread th = new Thread( new Runnable()
                    {
                        public void run()
                        {
                            updateText();

                        }
                    } );
                    
                    th.start();
                }

				else
				{
					int boxChosenBy2 = 4;
					int boxNumFrom1 = csc.receiveInt();
					System.out.println( "Received box number from GameServer: " + boxNumFrom1 ); // REMOVE
                    
                    if( boxNumFrom1 == boxChosenBy2 )
                    {
                        p2Points += 1;
                        System.out.println( "------ROUND UPDATE------" );
						System.out.println( "Player #1 points: " + p1Points + " || Player #2 points: " + p2Points );
						
                        csc.sendPoints( p1Points );
                        csc.sendPoints( p2Points );
                        /*
                        p1Points = csc.receivePoints();
                        p2Points = csc.receivePoints();
						*/
                        gameMessage.setText( "You guessed right! You now have " + p2Points + " point(s). Your turn to choose." );
                    }

                    else
                    {
                        p1Points += 1;
                        System.out.println( "------ROUND UPDATE------" );
						System.out.println( "Player #1 points: " + p1Points + " || Player #2 points: " + p2Points );
						
                        csc.sendPoints( p1Points );
                        csc.sendPoints( p2Points );
                        /*
                        p1Points = csc.receivePoints();
                        p2Points = csc.receivePoints();
						*/
                        gameMessage.setText( "You guessed wrong! Player #1 now has " + p1Points + " point(s). Your turn to choose." );
                    }
                }
			}

            else if ( round == 2 || round == 4 )
			{
				if( playerID == 1 )
				{
					int boxChosenBy1 = 4;
                    int boxNumFrom2 = csc.receiveInt();
                    System.out.println( "Received box number from GameServer: " + boxNumFrom2 ); // REMOVE

                    gameMessage.setText( "Player #2 has chosen a box! Try to guess which one it is." );

                    if( boxNumFrom2 == boxChosenBy1 )
                    {
                        p1Points += 1;
                        System.out.println( "------ROUND UPDATE------" );
						System.out.println( "Player #1 points: " + p1Points + " || Player #2 points: " + p2Points );
						
                        csc.sendPoints( p1Points );
                        csc.sendPoints( p2Points );
                        /*
                        p1Points = csc.receivePoints();
                        p2Points = csc.receivePoints();
						*/
                        gameMessage.setText( "You guessed right! You now have "+ p1Points + " point(s). Your turn to choose." );

                        if( round == 4 )
                        {
                            checkWinner();
                        }
                    }

                    else
                    {
                        p2Points += 1;
                        System.out.println( "------ROUND UPDATE------" );
						System.out.println( "Player #1 points: " + p1Points + " || Player #2 points: " + p2Points );
						
                        csc.sendPoints( p1Points );
                        csc.sendPoints( p2Points );
                        /*
                        p1Points = csc.receivePoints();
                        p2Points = csc.receivePoints();
						*/
                        gameMessage.setText( "You guessed wrong! Player 1 now has "+ p2Points + " point(s). Your turn to choose." );

                        if( round == 4 )
                        {
                            checkWinner();
                        }
                    }
                }

				else
				{
					int boxClickedBy2 = 4;

                    gameMessage.setText( "You clicked box #1! Wait for player #" + otherPlayer + " to choose a box." );

                    csc.sendInt( triggerInt );
                    csc.sendInt( boxClickedBy2 );

                    System.out.println( "You clicked " + boxClickedBy2 + "!" );
                                    
                    Thread th = new Thread( new Runnable()
                    {
                        public void run()
                        {
                            updateText();
                        }
                                        
                    } );
                    
                    th.start();
                    
                    if( round == 4 )
                    {
                        gameMessage.setText( "You clicked box #1! Check player #" + otherPlayer + "'s window for results." );
                        csc.sendPoints( p2Points );
                    }
				}
            }

			round++;
			System.out.println( "Now going into round " + round + "!" );
		}

		public void mouseExited( MouseEvent me ) {}
		public void mousePressed( MouseEvent me ) {}
        public void mouseEntered( MouseEvent me ) {
            gameCanvas.goTo4();
        }
        public void mouseReleased( MouseEvent me ) {}
	};
        
    private class ClientSideConnection
    /*
    	This class establishes a connection to the GameServer.
    */
    {
    	private Socket socket;
        private DataInputStream dataIn;
        private DataOutputStream dataOut;
        
        public ClientSideConnection()
        {
            System.out.println( "---Client---") ;

            try {
                socket = new Socket( "localhost", 51734 );

                dataIn = new DataInputStream( socket.getInputStream() );
                dataOut = new DataOutputStream( socket.getOutputStream() );

                playerID = dataIn.readInt(); // reads playerID sent by GameServer
				System.out.println( "Connected to server as Player #" + playerID + "." );

            } catch ( IOException ex ) {
                System.out.println( "IOException from CSC constructor." );
            }
        }
        public void sendInt(int n){
        try{
                dataOut.writeInt(n);
                dataOut.flush();
            } catch(IOException ex){
                System.out.println("IOException from sendInt() CSC");
            }
        }
        public int receiveInt(){
            int n = -1;
            try{
                n = dataIn.readInt();
            }catch(IOException ex){
                System.out.println("IOException from receiveInt() CSC");
            }
            
            return n;
        }
        public String receiveGameMessege(){
            String message = "blank";
            try{
                message = dataIn.readUTF();
                System.out.println(message);
            }catch(IOException ex){
                System.out.println("IOException from receiveGameMessege() CSC");
            }
            
            return message;
        }
        public void sendPoints(int n){
            try{
                dataOut.writeInt(n);
                dataOut.flush();
            } catch(IOException ex){
                System.out.println("IOException from sendPoints() CSC");
            }
        }
        public int receivePoints(){
           int n = -1;
            try{
                n = dataIn.readInt();
                
            }catch(IOException ex){
                System.out.println("IOException from receivePoints() CSC");
            }
            
            return n; 
        }
        
        public void closeConnection(){
            try{
                socket.close();
                System.out.println("----CONNECTION CLOSED----");
            }catch(IOException ex){
                System.out.println("IOException on closedConnection CSS");
            }
        }
        
    }

    public void connectToServer() // method that connects to server
    {
  		csc = new ClientSideConnection();
    }
    

    public void updateText(){
        int n = csc.receiveInt();
        gameMessage.setText("Player #" + otherPlayer + " clicked a box. Guess what it is.");
    }
    public void updatePoints(){
    	p1Points = csc.receiveInt();
    	p2Points = csc.receiveInt();
    	
    }
    
    public void checkWinner()
    {
        if(p1Points > p2Points){
            gameMessage.setText("You WON with " + p1Points + " points! The enemy has " + p2Points + "." );
        }else if(p1Points < p2Points){
            gameMessage.setText("You LOST with " + p1Points + " points! The enemy has " + p2Points + "." );
        }else{
            gameMessage.setText("It's a tie! You both got " + p1Points + " points.");
        }
        
        csc.closeConnection();
    }
    
}
