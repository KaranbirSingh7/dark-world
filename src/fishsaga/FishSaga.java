package fishsaga;

import java.io.*;
import java.util.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/*
 *Name - Karanbir Singh
 */
public class FishSaga extends Application
{    
    private Stage window;
    private Scene scene1,scene2;

    private Pane gameplayPane,homePane;

    int score = 0;
    int lives = 3;
    int prevScore;

    private File file = new File("c:\\game_score.txt");
    private Rectangle live1,live2,live3;

    Label lblHighscore = new Label();
    Label lblyourScore = new Label();
    Label lblName =  new Label("Karanbir Singh");
    Label lblScore = new Label("Score : " +score);
    Label lblLives = new Label("Lives : "+lives);
    Label lblInstructions = new Label("" +
            "1. Move fish with mouse to eat food.\n" +
            "2. You Have Total 3 LIVES.\n"+
            "3. Pineapple will increment score by 10.\n" +
            "4. Raspberry is worth 50 points.\n"+
            "5. BE CAREFUL from falling enemies.\n\n" +
            "   CLICK BUTTON TO START GAME");

    private AnimationTimer timer;
    private Node fish;
    private List<Node> enemy = new ArrayList<>();
    private List<Node> pineapple = new ArrayList<>();
    private List<Node> raspberry = new ArrayList<>();
    private Button btnPlay = new Button();

    Image enemyImage = new Image("images/octopus.gif");
    Image planeImage = new Image("images/fish.gif");
    Image background = new Image("images/water.gif");
    Image pineappleImage = new Image("images/pineapple.png");
    Image raspberryImage = new Image("images/raspberry.png");
    Image liveImage = new Image("images/lives.png");
    Image liveOffImage = new Image("images/livesOff.png");
    Image trophy = new Image("images/trophy.png");

    ImageView bg = new ImageView(background);

    private Pane gameOver()
    {
        Label lblgameOver = new Label("GAME OVER ");
        lblgameOver.setId("labelGameOver");
        labelPosition(lblgameOver,265,325);
        gameplayPane.getChildren().add(lblgameOver);
        return gameplayPane;
    }

    private Rectangle deployPineapple()
    {
        Rectangle rect = new Rectangle(25,35);
        rect.setFill(new ImagePattern(pineappleImage,0,0,1,1,true));
        rect.setTranslateX((int)(Math.random()*14)*40);
        gameplayPane.getChildren().add(rect);
        return rect;
    }
    private Rectangle deployRaspberry()
    {
        Rectangle rect = new Rectangle(30,40);
        rect.setFill(new ImagePattern(raspberryImage,0,0,1,1,true));
        rect.setTranslateX((int)(Math.random()*14)*40);
        gameplayPane.getChildren().add(rect);
        return rect;
    }
    private Rectangle deployLivesBar()
    {
        Rectangle rect = new Rectangle(25,25);
        rect.setFill(new ImagePattern(liveImage,0,0,1,1,true));
        return rect;
    }
    private Rectangle deployFish()
    {
        Rectangle rect = new Rectangle(50,50,Color.GREEN);
        rect.setFill(new ImagePattern(planeImage,0,0,1,1,true));
        rect.setTranslateY(600-39);
        return rect;
    }
    private Rectangle deployEnemy()
    {
        Rectangle rect = new Rectangle(45,45,Color.RED);
        rect.setFill(new ImagePattern(enemyImage,0,0,1,1,true));
        rect.setTranslateX((int)(Math.random()*14)*40);
        gameplayPane.getChildren().add(rect);
        return rect;
    }
    private void checkState()
    {
        for (Node en : enemy) 
        {
            if (en.getBoundsInParent().intersects(fish.getBoundsInParent())) 
            {
                if (lives <= 1)
                {
                    live3.setFill(new ImagePattern(liveOffImage, 0, 0, 1, 1, true));
                    gameplayPane.getChildren().removeAll(fish);
                    try 
                    {
                        Scanner read = new Scanner(file);
                        read.useDelimiter(System.getProperty("line.separator"));
                        prevScore = read.nextInt();
                        System.out.println(prevScore);

                        if (prevScore < score) 
                        {
                            try 
                            {
                                PrintWriter output = new PrintWriter(file);
                                output.print(score);
                                output.close();
                                prevScore = score;
                                highScore();
                                lblyourScore.setText("\tCongratulations!\n" +
                                        "Your New High Score : " + score);
                                labelPosition(lblyourScore, 250, 250);
                                gameplayPane.getChildren().add(lblyourScore);

                            }
                            catch (Exception ex) 
                            {
                                System.out.println(ex);
                            }
                        } 
                        else 
                        {
                            lblyourScore.setText("   Your Final Score : " + score);
                            lblHighscore.setText("   Highest Score : " + String.valueOf(prevScore));

                            labelPosition(lblyourScore, 265, 295);
                            labelPosition(lblHighscore, 265, 250);
                            gameplayPane.getChildren().addAll(lblHighscore, lblyourScore);
                        }
                        lblLives.setText("Lives : 0");
                        scene2.setCursor(Cursor.DEFAULT);
                        timer.stop();
                        gameOver();
                        return;
                    }
                    catch (Exception e) 
                    {
                        System.out.println(e);
                    }
                }
                else
                    {
                        en.relocate(-800, -800);
                        if (lives == 3)
                        {
                            live1.setFill(new ImagePattern(liveOffImage,0,0,1,1,true));
                        }
                        else if (lives == 2)
                        {
                            live2.setFill(new ImagePattern(liveOffImage,0,0,1,1,true));
                        }
                        lives--;
                        lblLives.setText("Lives : " + lives);
                        return;
                    }
                }
            }
        }
    private void checkScore()
    {
        for (Node f : pineapple)
        {
            if (f.getBoundsInParent().intersects(fish.getBoundsInParent()))
            {
                f.relocate(-800,-800);
                score = score + 10;
                lblScore.setText("Score : "+score);
                return;
            }
        }
        for (Node r : raspberry)
        {
            if (r.getBoundsInParent().intersects(fish.getBoundsInParent()))
            {
                r.relocate(-800,-800);
                score = score + 50;
                lblScore.setText("Score : "+score);
                return;
            }
        }
    }
    private Rectangle highScore()
    {
        Rectangle r = new Rectangle(100,100);
        r.setFill(new ImagePattern(trophy,0,0,1,1,true));
        imagePosition(r,350,150);
        gameplayPane.getChildren().add(r);
        return  r;
    }
    private void onUpdate()
    {
        for (Node en : enemy )
            en.setTranslateY(en.getTranslateY() + Math.random()*5);
        for (Node f : pineapple)
            f.setTranslateY(f.getTranslateY() +  Math.random()*5);
        for (Node r : raspberry)
            r.setTranslateY(r.getTranslateY() + Math.random()*5);
        if (Math.random() < 0.0275)
        {
            enemy.add(deployEnemy());
        }
        if (Math.random() < 0.0375)
        {
            pineapple.add(deployPineapple());
        }
        if (Math.random() < 0.0015)
        {
            raspberry.add(deployRaspberry());
        }
        checkState();
        checkScore();
    }
    private Rectangle imagePosition(Rectangle r,int x, int y)
    {
        Rectangle a = r;
        a.setTranslateY(y);
        a.setTranslateX(x);
        return a;
    }
    private Label labelPosition(Label l,int x,int y)
    {
        Label la = l;
        la.setTranslateY(y);
        la.setTranslateX(x);
        return la;
    }
    private Button buttonPosition(Button l,int x,int y)
    {
        Button bt = l;
        bt.setTranslateY(y);
        bt.setTranslateX(x);
        return bt;
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        
        //If files doesn't exists create new one
        //Default Score will be initailized to 0
        if(!file.exists())
        {
            file.createNewFile();
            FileWriter fw = new FileWriter(file);
            fw.write("0");
            fw.close();
        }
        window=primaryStage;

        // PANES
        homePane = new Pane();
        gameplayPane = new Pane();
        // SCENES
        scene1 = new Scene(homePane,800,600);
        //BG-COLOR SCENE 1
        Rectangle bgColorScene1 = new Rectangle(0,0,800,600);
        bgColorScene1.setFill(Color.LIMEGREEN);

        scene1.getStylesheets().add("fishsaga/style.css");
        scene2 = new Scene(gameplayPane,800,600);
        scene2.getStylesheets().add("fishsaga/style.css");

        labelPosition(lblScore,640,50);//SCORE POSITION
        lblInstructions.setId("labelInstructions");
        lblName.setId("labelName");


        //Deploying Lives Images
        live1 = deployLivesBar();
        imagePosition(live1,665,15);

        live2 = deployLivesBar();
        imagePosition(live2,695,15);

        live3 = deployLivesBar();
        imagePosition(live3,725,15);


        // ADD START GAME BUTTON
        btnPlay.setText("START GAME");

        labelPosition(lblName,300,1);
        buttonPosition(btnPlay,300,350);
        labelPosition(lblInstructions,200,100);


        fish = deployFish(); // ADD FISH TO SCENE
        homePane.getChildren().addAll(bgColorScene1,lblName,lblInstructions,btnPlay);
        gameplayPane.getChildren().addAll(bg,fish,lblScore,live1,live2,live3);

        //BACKGROUND
        bg.setFitHeight(800);
        bg.setFitWidth(800);

        //START TIMER
        btnPlay.setOnAction(e ->
        {
            window.setScene(scene2);
            timer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    onUpdate();
                }
            };
            timer.start();
        });

        scene2.setCursor(Cursor.NONE);
        scene2.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event)
            {
                fish.setTranslateX(event.getX());
                fish.setTranslateY(event.getY());
            }
        });
        window.setTitle("Just a game - Karanbir Singh");
        window.setScene(scene1);
        window.show();
    }
    public static void main(String[] args)
    {
        launch(args);
    }
    
}
