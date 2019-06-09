import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.nio.file.Path;
import java.io.IOException;

/**
 * @author jordan vanevery
 * SnakeFrame manages both the display and control aspects of the application.
 * For this reason the class extends JFrame. 
 */


//TODO CONCURRENCYYYYYY

public class SnakeFrame extends JFrame{


    /** Game manager for managing internal model */
    private GameManager manager;
    
    /** Timer delay in ms */
    private static final int TIME_DELAY = 200;

    /** Paused application flag */
    private boolean isPaused = true;

    /** Game over flag */
    private boolean gameOver = false;

    /** Score keeper */
    int score = 0;

    /** Inner class that groups all Snake Components in a Panel */
    private class SnakePanel extends JPanel implements ActionListener{

        /** Custom component for drawing a representation of the game */
        private SnakeDisplay display;

        /** Score for this instance of the application */
        private JLabel scoreLabel;

        //Pause button
        private JButton pauseButton;

        //Timer for game updates
        private Timer timer;

        //Last input direction from keyboard when game not paused
        private Directions nextDir;
    
        public SnakePanel(){
            //Set panel layout
            setLayout( new BorderLayout() );

            //Component that handles the display of the game
            display = new SnakeDisplay(manager);

            //TODO Make nice score label
            //TODO Score label needs to listen to updates from game manager

            //Pause, score, and other components
            scoreLabel = new JLabel("0000000");
            pauseButton = new JButton("Play Snake");
            ActionListener pauseListener = new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent ev){
                    if(isPaused){ 
                        isPaused=false; 
                        pauseButton.setText("Pause");
                        display.requestFocusInWindow();
                        timer.start();
                    }
                    else{ 
                        isPaused = true; 
                        pauseButton.setText("Continue");          
                        timer.stop();
                    }
                }
            };
            pauseButton.addActionListener(pauseListener);

            //Confiugre control panel and add components
            JPanel controlPanel = new JPanel();
            controlPanel.setLayout(new BoxLayout(controlPanel, 
                                   BoxLayout.X_AXIS));
            controlPanel.add(scoreLabel);
            controlPanel.add(Box.createHorizontalGlue());
            controlPanel.add(pauseButton);

            //Add controlPanel and display to SnakePanel
            add(display, BorderLayout.CENTER);
            add(controlPanel, BorderLayout.SOUTH);

            //Initialize timer
            timer = new Timer(TIME_DELAY, this);
            timer.setInitialDelay(0);

            //Initialize nextDir
            nextDir = manager.getSnakeDirection();

            //Key listener
            KeyAdapter keyListener = new KeyAdapter(){
                public void keyPressed(KeyEvent keyEv){
                    if(!isPaused){
                        switch(keyEv.getKeyCode()){
                            case KeyEvent.VK_UP:
                                nextDir = Directions.DOWN; //TODO FIX
                                break;
                            case KeyEvent.VK_DOWN:
                                nextDir = Directions.UP; //TODO FIX
                                break;
                            case KeyEvent.VK_RIGHT:
                                nextDir = Directions.RIGHT;
                                break;
                            case KeyEvent.VK_LEFT:
                                nextDir = Directions.LEFT;
                                break;
                        }
                    }
                }
            };
            display.addKeyListener( keyListener );

        }

        /**
         * On every tick of the timer the game manager is updated and the
         * display is repainted.
         * @param timerEv timer event that triggerd actionPerformed
         */
        public void actionPerformed(ActionEvent timerEv){
            //Update the snake direction
            if( Directions.isValidTurn(manager.getSnakeDirection(),
                    nextDir) ){
                manager.setSnakeDirection(nextDir);
            }
            //Update
            TurnSummary thisTurn = manager.update();
            switch( thisTurn.status ){
                case EMPTY:
                    break;
                case WALL_COLLISION:
                    gameOver = true;
                    break;
                case SNAKE_COLLISION:
                    gameOver = true;
                    break;
                case FOOD_COLLISION:
                    //System.out.println("Snake got some food, " + thisTurn.scoreGain + " points!");
                    score += thisTurn.scoreGain;
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            scoreLabel.setText( Integer.toString(score) );
                        }
                    });
            }
            repaint();
            //TODO Create Option pane for playing again or quitting
            if(gameOver){ 
                timer.stop();
                endGame(); 
            }
        }

    }

    /**
     * Constructor for SnakeFrame, which serves as the main frame of the Snake
     * GUI
     * @param userMap A path to a file that will be parsed according to the 
     * Snake map format.
     *
     */
    //TODO Have a more general config data type that we can pass as argument
    public SnakeFrame(Path userMap){
        super("Snake");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Initialize GameManager 
        if(userMap==null){
            manager = new GameManager();
        }
        else{
            try{
                manager = new GameManager(userMap);
            }catch(IOException ioErr){
                manager = new GameManager();
                System.out.println("Path to user file was bad, loaded default");
            }
        }

        //Add SnakePanel
        SnakePanel gamePanel = new SnakePanel();
        add(gamePanel);        

        //Pack and display
        pack();
        setVisible(true);
    }

    //Display end game message and quit when user selects OK
    private void endGame(){
        JOptionPane.showMessageDialog(this, "Game Over! You scored " 
                                            + score + " points.");
        this.dispatchEvent(new WindowEvent(this, 
                           WindowEvent.WINDOW_CLOSING));
    }

}
