import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import java.awt.event.*;
import java.sql.Time;
import java.util.Random;
import java.awt.event.ActionListener;
public class GamePanel extends JPanel implements ActionListener{
    
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY = 80;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 6;
    int foodEnten;
    int foodX;
    int foodY;
    char direction = 'R';     // User input direction R:Right, L:Left, U:Up, D:Down
    boolean running = false;
    Timer timer;
    Random random;   // For food to appear on screen randomly

    // ImageIcon snakeHead = new ImageIcon(this.getClass().getResource("data/head.png"));
    // ImageIcon snakeBody = new ImageIcon(this.getClass().getResource("data/body.png")); 

    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        StartGame();
    }
    public void StartGame(){
        newFood();
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        if(running){
            // for (int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++){
            //     g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
            //     g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);            
            // }
            g.setColor(Color.white);
            g.fillOval(foodX, foodY, UNIT_SIZE, UNIT_SIZE);
                
            for (int i = 0; i < bodyParts; i++) {
                if(i == 0){
                    BufferedImage snakeHead;
                    try {
                        snakeHead = ImageIO.read(new File("data/head.png"));
                        g.drawImage(snakeHead, x[i], y[i], UNIT_SIZE, UNIT_SIZE, null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
        
                    // g.setColor(Color.yellow);
                    // g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                else {
                    BufferedImage snakeBody;
                    try {
                        snakeBody = ImageIO.read(new File("data/body.png"));
                        g.drawImage(snakeBody, x[i], y[i], UNIT_SIZE, UNIT_SIZE, null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // g.setColor(Color.white);
                    // g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }           
            }

            g.setColor(Color.red);
            g.setFont(new Font("SansSerif", Font.BOLD, 35));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: "+foodEnten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+foodEnten))/2, g.getFont().getSize());
        }
        else {
            gameOver(g);
        }
    }
    public void newFood(){
        foodX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        foodY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }
    public void move(){
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];            
        }
        switch(direction){
            case 'U':
            y[0] = y[0] - UNIT_SIZE;
            break;
            case 'D':
            y[0] = y[0] + UNIT_SIZE;
            break;
            case 'L':
            x[0] = x[0] - UNIT_SIZE;
            break;
            case 'R':
            x[0] = x[0] + UNIT_SIZE;
            break;
        }

    }
    public void checkFood(){
        if((x[0] == foodX) && (y[0] == foodY)){
            bodyParts++;
            foodEnten++;
            newFood();
        }
    }
    public void checkCollision(){
        //checks if the head collides with the body
        for (int i = bodyParts; i > 0; i--) {
            if((x[0] == x[i]) && (y[0] == y[i])){
                running = false;
            }          
        }
        //checks if head touches left border
        if(x[0] < 0){
             running = false;   
        }
        //checks if head touches right border
        if(x[0] > SCREEN_WIDTH){
            running = false; 
        }
        //checks if head touches top border
        if(y[0] < 0){
            running = false; 
        }
        //checks if head touches bottom border
        if(y[0] > SCREEN_HEIGHT){
            running = false; 
        }

        if(!running){
            timer.stop();
        }
    }
    public void gameOver(Graphics g){
        //Setting up game over text
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 70));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics1.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);

        g.setColor(Color.red);
        g.setFont(new Font("SansSerif", Font.BOLD, 35));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Score: "+foodEnten, (SCREEN_WIDTH - metrics2.stringWidth("Score: "+foodEnten))/2, (SCREEN_HEIGHT/2) + 50);
    }
    @Override
    public void actionPerformed(ActionEvent e){
        if(running){
            move();
            checkFood();
            checkCollision();
        }
        repaint();
    }
    public class MyKeyAdapter extends KeyAdapter{
        // This part of code is done by Farzana Anam Dristy and Salma Jannat
        @Override
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()){
                case KeyEvent.VK_A:
                    if(direction != 'R'){
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_D:
                    if(direction != 'L'){
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_W:
                    if(direction != 'D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_S:
                    if(direction != 'U'){
                        direction = 'D';
                    }
                    break;
            }
            
        }
    }
}

//Completed