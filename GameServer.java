/**
    GameServer is a class wherein two instances of the Player class are able to interact with each other. It handles the exchange of information between players and allows the game to progress that way.

    @Alysha Canonigo Columbres 186050 and Joaquin Espino 181877
    @May 17, 2019
**/

/*
I have not discussed the Java language code in my program with anyone other than my instructor or the teaching assistants assigned to this course.
I have not used Java language code obtained from another student, or any other unauthorized source, either modified or unmodified.
If any Java language code or documentation used in my program was obtained from another source, such as a textbook or course notes, that has been clearly noted with a proper citation in the comments of my program.
*/

import java.io.*; // responsible for "system input and output through data streams, serialization and the file system"
import java.net.*; // "classes for implementing networking applications" (http://www.dcs.gla.ac.uk/~pd/JavaRefCard/private/packagelist.pdf)

public class GameServer
{
    // instance fields are declared
    private ServerSocket ss;
    private int numPlayers;
    private ServerSideConnection player1;
    private ServerSideConnection player2;
    private int player1BoxNum, player2BoxNum, player1TriggerNumber, player2TriggerNumber;
    
    public GameServer() // opens connections for players
    {
        System.out.println( "------Game Server------" );

        numPlayers = 0;

        try {
            ss = new ServerSocket( 51734 ); // creates a socket when a player connects
        } catch( IOException ex ) {
            System.out.println( "IOException from GameServer Constructor." );
        }
    }
    
    public void acceptConnections()
    {
        try {
            System.out.println( "Waiting for connections..." );

            while( numPlayers < 2 )
            {
                Socket s = ss.accept(); // accepts player connections and creates a socket object in response that is connected to the player

                numPlayers++; // increments every time a player connects (only two players maximum)

                System.out.println( "Player #" + numPlayers + " has connected." );

                ServerSideConnection ssc = new ServerSideConnection( s, numPlayers ); // an inner class object that allows SSC processes to be run on two separate threads for both players

                if( numPlayers == 1 ) // assigns ssc to both players
                {
                    player1 = ssc;
                }

                else
                {
                    player2 = ssc;
                }

                Thread t = new Thread( ssc ); // creates the threads for both players
                t.start(); // starts the thread
            }

            System.out.println( "We now have 2 players. No longer accepting connections." );
        } catch ( IOException ex ) {
            System.out.println( "IOException from acceptConnections()." );
        }
    }
    
    private class ServerSideConnection implements Runnable
    /*
        This inner class implements the Runnable interface. This is where the GameServer is able to send and receive information to and from both players.
    */
    {
        // instance fields are declared
        private Socket socket;
        private DataInputStream dataIn;
        private DataOutputStream dataOut;
        private int playerID, p1Points, p2Points;
        
        public ServerSideConnection( Socket s, int id ) // constructor takes the socket object created by the ServerSocket after a player connects and the player ID number
        {
            socket = s;
            playerID = id;
            
            try {
                dataIn = new DataInputStream( socket.getInputStream() ); // reads information sent out by player
                dataOut = new DataOutputStream( socket.getOutputStream() ); // writes information to be sent out to players
            } catch( IOException ex ) {
                System.out.println( "IOException from SSC constructor." );
            }
        }
        public void run()
        {
            try {

                int counter = 1;

                dataOut.writeInt( playerID ); // sends out player ID number to players

                while( true )
                {
                    if( playerID == 1 )
                    {   if(counter == 1){


                        player1TriggerNumber = dataIn.readInt();
                        player1BoxNum = dataIn.readInt();

                        System.out.println( "Player #" + playerID + " clicked box " + player1BoxNum + "." );

                        player2.sendInt( player1TriggerNumber );
                        player2.sendInt( player1BoxNum );
                        /*
                        p1Points = dataIn.readInt();
                        p2Points = dataIn.readInt();

                        player2.sendInt( p1Points );
                        player2.sendInt( p2Points );
                        */
                        
                        }
                        counter++;
                    }

                    else
                    {   if(counter == 1){
                        player2TriggerNumber = dataIn.readInt();
                        player2BoxNum = dataIn.readInt();

                        System.out.println( "Player #" + playerID + " clicked box " + player2BoxNum + "." );

                        player1.sendInt( player2TriggerNumber );
                        player1.sendInt( player2BoxNum );
                        /*
                        p1Points = dataIn.readInt();
                        p2Points = dataIn.readInt();

                        player1.sendInt( p1Points );
                        player1.sendInt( p2Points );
                        */
                        }
                        if(counter == 2){
                        p1Points = dataIn.readInt();
                        p2Points = dataIn.readInt();

                        player1.sendInt( p1Points );
                        player1.sendInt( p2Points );
                        }
                        counter++;

                    }
                    
                }
            } catch( IOException ex ) {
                System.out.println( "IOException from run() SSC constructor." );
            }
        }
        public void sendInt( int n )
        {
            try {
                dataOut.writeInt( n );
                dataOut.flush();
            }catch( IOException ex ) {
                System.out.println( "IOException from sendInt() SSC method." );
            }
        }
    }
    
    public static void main( String[] args )
    {
        GameServer gs = new GameServer();
        gs.acceptConnections();
    }
}
