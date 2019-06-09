import java.util.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
/* ****************************************************************************
 * @author jordan vanevery
 *
 * GameManagerTester is the testing class for GameManager, and by extension all
 * of Classes GameManager uses 
 * ***************************************************************************/

public class GameManagerTester{

    //Enum for legal moves Snake can make relative to itself
    private static enum Moves{ FORWARD, TURN_RIGHT, TURN_LEFT; }

    public static void main(String[] args){

        //Demo snake eating food and colliding with itself
        GameManager test = new GameManager();
        System.out.println( test.toString() );
        List<Moves> someMoves = new ArrayList<>();
        someMoves.add( Moves.FORWARD );
        someMoves.add( Moves.FORWARD );
        someMoves.add( Moves.TURN_RIGHT );
        someMoves.add( Moves.TURN_LEFT );
        someMoves.add( Moves.FORWARD );
        someMoves.add( Moves.FORWARD );
        someMoves.add( Moves.TURN_RIGHT );
        someMoves.add( Moves.FORWARD );
        someMoves.add( Moves.TURN_RIGHT );
        someMoves.add( Moves.TURN_RIGHT );
        someMoves.add( Moves.FORWARD );
        someMoves.add( Moves.TURN_RIGHT );
        System.out.println("**TEST ONE: EATING FOOD AND SELF COLLISION**\n");
        runMoveSequence( test, someMoves ); 

        //Demo loading a map file, and self collision (maze-simple.txt)
        try{
            Path testPath = Paths.get("maze-simple.txt");
            GameManager testMap = new GameManager( testPath );
            System.out.println( testMap.toString() );
            List<Moves> moreMoves = new ArrayList<>();
            moreMoves.add( Moves.FORWARD );
            moreMoves.add( Moves.TURN_LEFT );
            moreMoves.add( Moves.TURN_RIGHT );
            moreMoves.add( Moves.FORWARD );
            System.out.println("**TEST TWO: LOAD MAP FROM FILE, "
                              +"WALL COLLISION**\n");
            runMoveSequence( testMap, moreMoves );
        }catch(IOException ioErr){
            System.err.format("IOException: %s%n", ioErr);
        }
        
        //Showing that I can operate multiple game managers independently
        GameManager testOne = new GameManager();
        GameManager testTwo = new GameManager();
        System.out.println("**TEST THREE: TWO MANAGERS INDEPENDENTLY**\n");
        System.out.println( "Manager one: " );
        System.out.println( testOne.toString() );
        System.out.println( "Manager two: " );
        System.out.println( testTwo.toString() );
        System.out.println( "Manager one: " );
        moveAndPrint(testOne, Moves.FORWARD);
        System.out.println( "Manager two: " );
        moveAndPrint(testTwo, Moves.TURN_RIGHT);
        for(int i = 0; i<2; ++i){
            System.out.println( "Manager one: " );
            moveAndPrint(testOne, Moves.FORWARD);
            System.out.println( "Manager two: " );
            moveAndPrint(testTwo, Moves.TURN_LEFT);
        }
    }

    //Return false if the game not over, true otherwise
    private static boolean moveAndPrint(GameManager manager, Moves move){
        boolean gameOver = false;
        Directions newDir = manager.getSnakeDirection();
        switch( move ){
            case TURN_LEFT:
                newDir = Directions.turnLeft( newDir );
                break;
            case TURN_RIGHT:
                newDir = Directions.turnRight( newDir );
                break;
            case FORWARD:
                break;
        }
        manager.setSnakeDirection( newDir );
        TurnSummary thisTurn = manager.update();
        System.out.println( manager.toString() );
        switch( thisTurn.status ){
            case EMPTY:
                System.out.println("Snake moves");
                break;
            case WALL_COLLISION:
                System.out.println("Snake ran into wall");
                gameOver = true;
                break;
            case SNAKE_COLLISION:
                System.out.println("Snake ran into itself :(");
                gameOver = true;
                break;
            case FOOD_COLLISION:
                System.out.println("Snake got some food");

        }
        System.out.println();
        return gameOver;
        
    }

    private static void runMoveSequence(GameManager manager, List<Moves> moves){
        for(Moves obj : moves){
            if( GameManagerTester.moveAndPrint( manager, obj ) ){ break; }
        }
    }

}
