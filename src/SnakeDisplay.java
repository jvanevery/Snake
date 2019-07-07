import java.util.List;
import java.lang.Math;
import java.awt.*;
import javax.swing.*;

/**
 * @author jordan vanevery
 * Component of Snake GUI that handles the drawing of the representation of 
 * the game.  
 */

public class SnakeDisplay extends JPanel{

    /** Tile draw width and height */
    private static final int TILE_DRAW_LENGTH = 30;

    /** Draw width */
    private final int drawWidth;

    /** Draw height */
    private final int drawHeight;

    /** Tile width of a square in the drawing of the game board */
    //private final int tileWidth;

    /** Tile width of a square in the drawing of the game board */
    //private final int tileHeight;

    /** Reference to GameManager used in SnakeFrame */
    private GameManager manager;

    /** Sprite Manager */
    private SpriteManager spriteManager;

    /** Number of food images */
    public static final int NUM_FOOD_TYPES = 4;

    /** Number of wall images */
    public static final int NUM_WALL_IMAGES = 3;

    /**
     * Create SnakeDisplay and associate it with game manager
     * @param manager GameManager that the new SnakeDisplay component
     * will be associated with. All drawing will be based off the model
     * stored in this manager
     */
    public SnakeDisplay(GameManager manager){
        this.manager = manager;
        spriteManager = new SpriteManager();
        drawWidth = TILE_DRAW_LENGTH*manager.getGridWidth();
        drawHeight = TILE_DRAW_LENGTH*manager.getGridHeight();
        setPreferredSize(new Dimension(drawWidth, drawHeight));
        setBackground(Color.BLACK);
    }

    @Override
    public void paintComponent(Graphics g){
        //TODO Make it so we don't have black space on edges
        //TODO Make this less ugly
        //TODO Find a graceful way to deal with rectangular grids
        super.paintComponent(g);

        Color wallColor = new Color(119,136,153);
        Color snakeColor = new Color(0,139,69);
        Color snakeHeadColor = new Color(0,250,154);
        Color foodColor = new Color(178,34,34);

        List<GameObject> model = manager.getModel();

        for(GameObject obj : model){
            int x0 = 0;
            int y0 = 0;
            for(Coordinate coorObj : obj.getCoordinates()){
                String coorType = coorObj.toString();
                x0 = TILE_DRAW_LENGTH*((int)coorObj.getX());
                y0 = TILE_DRAW_LENGTH*((int)coorObj.getY());
                if(!coorType.equals("f")){
                    switch(coorType){
                        case "S":
                            g.setColor(snakeHeadColor);
                            g.fillRect(x0,y0,TILE_DRAW_LENGTH,TILE_DRAW_LENGTH);
                            break;
                        case "s":
                            g.setColor(snakeColor);
                            g.drawRect(x0,y0,TILE_DRAW_LENGTH,TILE_DRAW_LENGTH);
                            break;
                    }                            
                }

                if(coorType.equals("f")){
                    int foodIndex = ((Food)obj).getID();
                    g.drawImage(spriteManager.getFoodSprite(foodIndex), x0, y0, TILE_DRAW_LENGTH,
                                TILE_DRAW_LENGTH , null);
                }
                else if(coorType.equals("X")){
                    //TODO make reliable way to get same images for walls
                    int wallIndex = (int)(coorObj.getX() + coorObj.getY())%NUM_WALL_IMAGES;
                    g.drawImage(spriteManager.getWallSprite(wallIndex), x0, y0, TILE_DRAW_LENGTH,
                            TILE_DRAW_LENGTH, null);
                }
            }
        }
    }
}
