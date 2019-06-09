import java.util.List;
import java.lang.Math;
import java.awt.*;
import javax.swing.*;
import java.io.IOException;
import java.io.*;
import java.awt.image.*;
import javax.imageio.*;

/**
 * @author jordan vanevery
 * Component of Snake GUI that handles the drawing of the representation of 
 * the game.  
 */

public class SnakeDisplay extends JPanel{

    /** Sprite sheet path */
    private static final String SPRITE_SHEET_PATH_NAME = "sprite_assets.png";
    /** Draw width */
    private static final int DRAW_WIDTH = 600;

    /** Draw height */
    private static final int DRAW_HEIGHT = 600;

    /** Tile width of a square in the drawing of the game board */
    private final int tileWidth;

    /** Tile width of a square in the drawing of the game board */
    private final int tileHeight;

    /** Reference to GameManager used in SnakeFrame */
    private GameManager manager;

    
    /** Number of food images */
    public static final int NUM_FOOD_TYPES = 4;

    /** Number of wall images */
    public static final int NUM_WALL_IMAGES = 3;

    /** Load sprite sheet */
    private BufferedImage imageSheet = getImage( SPRITE_SHEET_PATH_NAME );

    /** Food images */
    private BufferedImage[] foodImages = {
        imageSheet.getSubimage(33, 17, 16, 16), //eggs
        imageSheet.getSubimage(33, 0, 16, 16), //beer
        imageSheet.getSubimage(50, 0, 16, 16), //pepper
        imageSheet.getSubimage(33, 34, 16, 16)  //steak
    };

    /** Wall images */
    private BufferedImage[] wallImages = {
        imageSheet.getSubimage(0, 0, 32, 32),
        imageSheet.getSubimage(0, 0, 32, 64),
        imageSheet.getSubimage(0, 0, 32, 96)
    };



    /**
     * Create SnakeDisplay and associate it with game manager
     * @param manager GameManager that the new SnakeDisplay component
     * will be associated with. All drawing will be based off the model
     * stored in this manager
     */
    public SnakeDisplay(GameManager manager){
        this.manager = manager;
        setPreferredSize(new Dimension(DRAW_WIDTH, DRAW_HEIGHT));
        setBackground(Color.BLACK);
        this.tileWidth = DRAW_WIDTH/manager.getGridWidth();
        this.tileHeight = DRAW_HEIGHT/manager.getGridHeight();
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
                x0 = tileWidth*((int)coorObj.getX());
                y0 = tileHeight*((int)coorObj.getY());
                if(!coorType.equals("f")){
                    switch(coorType){
                        case "S":
                            g.setColor(snakeHeadColor);
                            g.fillRect(x0,y0,tileWidth,tileHeight);
                            break;
                        case "s":
                            g.setColor(snakeColor);
                            g.drawRect(x0,y0,tileWidth,tileHeight);
                            break;
                    }                            
                }

                if(coorType.equals("f")){
                    int foodIndex = ((Food)obj).getID();
                    g.drawImage(foodImages[foodIndex], x0, y0, tileWidth, 
                                tileHeight , null);
                }
                else if(coorType.equals("X")){
                    //TODO make reliable way to get same images for walls
                    int wallIndex = (int)(coorObj.getX() + coorObj.getY())%NUM_WALL_IMAGES;
                    g.drawImage(wallImages[wallIndex], x0, y0, tileWidth, tileHeight, null);
                }
            }
        }
    }

    public BufferedImage getImage(String imageName){
        BufferedImage image = null;
        ClassLoader cl = getClass().getClassLoader();
        InputStream in = cl.getResourceAsStream(imageName);
        try {
            image = ImageIO.read(in);
            setPreferredSize(new Dimension(image.getWidth(null),
                                           image.getHeight(null)));
        }catch (IOException ex) {
            System.err.println("Error loading: " + imageName);
        }
        return image;
    }

}
