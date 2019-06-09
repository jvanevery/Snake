import java.util.*;
import java.awt.Point;
import java.nio.file.Path;
import java.io.IOException;

/* ****************************************************************************
 * @author jordan vanevery
 *
 * GameManager manages the internal game model and logic. This includes but is
 * not limited to initializing the game model, updating the game model, and 
 * determining if end game conditions have been met 
 * ***************************************************************************/

class GameManager{

    /**Default width*/
    private static final int DEFAULT_WIDTH = 20;

    /**Default height*/
    private static final int DEFAULT_HEIGHT = 20;

    /** Width of game grid */
    private final int gridWidth;

    /** Height of game grid */
    private final int gridHeight;
    
    /** Model of game is a list of GameObjects */
    private List<GameObject> model;

    /** Snake object that moves around the game model */
    private Snake snake;
    
    /** List of walls in the game model */
    private List<Wall> walls;

    /** List of Food objects in the game model */
    private List<Food> foodItems;


    /** 
     * Most general constructor 
     * @param gridWidth Width of game grid
     * @param gridHeight Height of game grid
     * */
    public GameManager(int gridWidth, int gridHeight){
        Random randGen = new Random();

        //Initialize dimensions
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;

        //Initialize model of game
        model = new ArrayList<GameObject>();

        //Add walls
        walls = new ArrayList<Wall>();
        addWalls( makeBorderWalls() );

        //Add food
        foodItems = new ArrayList<Food>();
        Food someFood = new Food( newOpenCoordinate(randGen.nextInt()) );
        addFood( someFood );

        //Add snake
        //TODO Set snake somewhere where player won't immediately hit wall
        snake = new Snake( newOpenCoordinate(randGen.nextInt()), 
                           Directions.UP, 3 );
        model.add(snake);
    }
    /** 
     * Default constructor makes a map with border walls on a grid with
     * dimensions width = 20, height = 10
     * */
    public GameManager(){
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /** 
     * Special constructor that reads map files
     * @param mapPath Path to map file
     * @throws IOException
     * */ 
    public GameManager(Path mapPath) throws IOException{
        //Get MapWord from Path and extract data
        MapWord mapData = SnakeIO.readMap( mapPath );
        this.gridWidth = mapData.mapWidth;
        this.gridHeight = mapData.mapHeight;
        model = new ArrayList<GameObject>();
        walls = new ArrayList<Wall>();
        addWalls( mapData.walls );

        //TODO get rid of shameless copy paste code

        //Add border walls if they aren't already there 
        addWalls( makeBorderWalls() );

        //Add food
        foodItems = new ArrayList<Food>();
        Food someFood = new Food(newOpenCoordinate((long)(Math.random()*100)));
        addFood(someFood);

        //Add snake
        //TODO Set snake somewhere where player won't immediately hit wall
        snake = new Snake( newOpenCoordinate(3), Directions.UP, 3 );
        model.add(snake);
    }

    /** add border walls to outside of map */
    private List<Wall> makeBorderWalls(){
        List<Wall> temp = new ArrayList<Wall>();
        temp.add( makeStraightWall( new Coordinate(0,0), Directions.UP, 
                                    gridHeight) ); 
        temp.add( makeStraightWall( new Coordinate(1,0), Directions.RIGHT, 
                                    gridWidth-2 ) );
        temp.add( makeStraightWall( new Coordinate(1, gridHeight-1), 
                                    Directions.RIGHT, gridWidth-2 ) );
        temp.add( makeStraightWall( new Coordinate(gridWidth-1, 0), 
                                    Directions.UP, gridHeight ) );
        return temp;
    }

    /**
     * Return a wall with specified start point, direction and length
     * @param startPoint Coordinate at which we start building wall
     * @param dir Direction in which wall pieces are added
     * @param length Length of wall to be constructed
     * */
    public static Wall makeStraightWall( Coordinate startPoint, Directions dir,
                                   int length ){
        //TODO Detect out of bounds walls
        List<Coordinate> wallCoordinates = new ArrayList<>();
        int currX = (int) startPoint.getX();
        int currY = (int) startPoint.getY();
        for(int i=0; i<length; ++i){
            wallCoordinates.add( new Coordinate(currX, currY) );
            currX += dir.getXStep();
            currY += dir.getYStep();
        }
        return new Wall( wallCoordinates );
    }

    /** Add walls to model */
    private void addWalls( List<Wall> walls ){
        for(Wall obj : walls){
            this.walls.add(obj);
            model.add(obj);
        }
    }

    /** Add food to model */
    private void addFood( Food food ){
        foodItems.add(food);
        model.add(food);
    }

    @Override
    /** Display representation of map as 2d grid of characters */
    public String toString(){
        
        String[][] stringRep = new String[gridHeight][gridWidth];
        for(GameObject gameObj : model){ 
            List<Coordinate> temp = gameObj.getCoordinates();
            for(Coordinate coorObj : temp){
                stringRep[(int)coorObj.getY()][(int)coorObj.getX()] = 
                                                  coorObj.toString();
            }
        }
        
        //Fill remaining slots
        String finalString = "";
        for(int i=gridHeight-1; i>=0; --i){
            for(int j=0; j<gridWidth; ++j){ 
                if( stringRep[i][j]==null ){ finalString += "-"; }
                else{ finalString += stringRep[i][j]; }
            }
            finalString += "\n";
        }

        return finalString;

    }

    /** 
     * Set the head direction of the snake 
     * @param dir Direction value to be assigned to snake head
     * */
    public void setSnakeDirection( Directions dir ){
        snake.setDirection( dir );
    }

    /** 
     * Get the head direction of the snake 
     * @return Current Direction of snake head
     * */
    public Directions getSnakeDirection(){
        return snake.getDirection();
    }

    /** 
     * Move snake, check for wall/snake/food collisions, act accordingly
     * @return TurnOutcome that specifies the event that took place in
     * the model update.
     */
    public TurnSummary update(){

        TurnOutcome outcome = TurnOutcome.EMPTY;
        int lengthGain = 0;
        int scoreGain = 0;

        snake.move();
        Coordinate head = snake.getSnakeHeadLocation();
        Food hitFood = null;
        if( isWallCollision(head) ){
            outcome = TurnOutcome.WALL_COLLISION;
        }
        else if( isSnakeCollision(head) ){
            outcome = TurnOutcome.SNAKE_COLLISION;
        }
        else if( (hitFood = isFoodCollision(head))!=null ){
            //TODO make growthFactor a field of Food
            outcome = TurnOutcome.FOOD_COLLISION;
            lengthGain = hitFood.getValue();
            scoreGain = 100*lengthGain;
            snake.addSnakeLength( lengthGain );
            model.remove(hitFood);
            foodItems.remove(hitFood);
            addFood( new Food(newOpenCoordinate(5)) );
        }
        return new TurnSummary(outcome, lengthGain, scoreGain);
    }

    /** Check for snake collision with wall */
    private boolean isWallCollision(Coordinate pos){
        //TODO Consider making wall blocks held in a map since they don't move
        boolean collisionDetected = false;
        for(Wall wallObj: walls){
            List<Coordinate> wallBlocks = wallObj.getCoordinates();
            for(Coordinate obj : wallBlocks){
                if( pos.equals(obj) ){ 
                    collisionDetected = true;
                    break;
                }
            }
        }
        return collisionDetected;
    }

    /** Check for snake collision with itself */
    private boolean isSnakeCollision(Coordinate pos){
        //TODO Optimize somehow, can't really use map bc changes too often
        boolean collisionDetected = false;
        List<Coordinate> snakeBlocks = snake.getCoordinates();
        //Start at one to avoid clashing with head
        for(int i=1; i<snake.getLength(); ++i){
            if( snakeBlocks.get(i).equals(pos) ){
                collisionDetected = true;
                break;
            }
        }
        return collisionDetected;

    }

    /** 
     * Check for snake collision with food 
     * @param pos Coordinate that is checked for matching position with all
     * the Coordinates of all current Food objects.
     * @return If it exists, the Food object that was found to contain pos,
     * otherwise null 
     * */
    private Food isFoodCollision(Coordinate pos){
        Food hitFood = null; 
        for( Food foodObj: foodItems ){
            List<Coordinate> foodBlocks = foodObj.getCoordinates();
            for( Coordinate food : foodBlocks ){
                if( food.equals(pos) ){ 
                    hitFood = foodObj;
                    break;
                }
            }
        }
        
        return hitFood;
    }

    /** 
     * Find an unnocupied legal Coordinate not yet in the model
     * @param seed Seed for random number generator that looks for spaces
     * at random.
     * @return Open, legal Coordinate
     */
    private Coordinate newOpenCoordinate(long seed){

        Random rng = new Random(seed);
        Coordinate randCoor = new Coordinate( rng.nextInt( gridWidth ), 
                                              rng.nextInt( gridHeight ) );
        boolean foundCoor = false;
        while( !foundCoor ){
            randCoor.setLocation( rng.nextInt( gridWidth ), 
                                  rng.nextInt( gridHeight ) );
            boolean duplicate = false;
            for( GameObject gameObj : model ){
                List<Coordinate> coors = gameObj.getCoordinates();
                if(coors.contains(randCoor)){
                    duplicate = true;
                    break;
                }
            }

            if(!duplicate){
                foundCoor = true;
            }
        }
        return randCoor;
    }

    /**
     * Get the model 
     * @return List of all GameObjects in the game. 
     * This includes walls, snake body and head, food.
     */
    public List<GameObject> getModel(){ return model; }

    /**
     * Get list of walls
     * @return a list of all the walls in the model
     */
    public List<Wall> getWalls(){ return walls; }

    /**
     * Get grid width
     * @return gridWidth
     */
    public int getGridWidth(){ return gridWidth; }

    /**
     * Get grid height
     * @return gridHeight
     */
    public int getGridHeight(){ return gridHeight; }
} 
