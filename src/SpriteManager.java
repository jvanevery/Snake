import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/** A class that will handle the loading of sprites from our sprite sheet */
public class SpriteManager {

    /** Sprite sheet path */
    private static final String SPRITE_SHEET_PATH_NAME = "sprite_assets.png";

    /** Map from a food id to buffered image */
    private HashMap<Integer, BufferedImage> foodSpriteMap;

    /** Map from a int id for wall to buffered image */
    private HashMap<Integer, BufferedImage> wallSpriteMap;

    /** Get food sprite */
    public BufferedImage getFoodSprite(int id){
        return foodSpriteMap.get(id);
    }

    /** Get wall sprite */
    public BufferedImage getWallSprite(int id){
        return wallSpriteMap.get(id);
    }

    public SpriteManager(){
        BufferedImage spriteSheet = null;
        ClassLoader cl = getClass().getClassLoader();
        InputStream in = cl.getResourceAsStream(SPRITE_SHEET_PATH_NAME);
        try {
            spriteSheet = ImageIO.read(in);
        }catch (IOException ex) {
            System.err.println("Error loading: " + SPRITE_SHEET_PATH_NAME);
        }

        //TODO: Make a sprite atlas in a .txt file and a system for reading/parsing it
        /** Food images */
        foodSpriteMap = new HashMap<>();
        foodSpriteMap.put(0, spriteSheet.getSubimage(33, 17, 16, 16)); //eggs
        foodSpriteMap.put(1, spriteSheet.getSubimage(33, 0, 16, 16));  //beer
        foodSpriteMap.put(2, spriteSheet.getSubimage(50, 0, 16, 16));  //pepper
        foodSpriteMap.put(3, spriteSheet.getSubimage(33, 34, 16, 16)); //steak

        wallSpriteMap = new HashMap<>();
        wallSpriteMap.put(0, spriteSheet.getSubimage(0, 0, 32, 32));
        wallSpriteMap.put(1, spriteSheet.getSubimage(0, 32, 32, 32));
        wallSpriteMap.put(2, spriteSheet.getSubimage(0, 64, 32, 32));
    }
}
