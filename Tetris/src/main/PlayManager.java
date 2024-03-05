package main;

import mino.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class PlayManager {
    //main play area
    final int WIDTH=360;
    final int HEIGHT=600;
    public static int left_x;
    public static int right_x;
    public static int top_y;
    public static int bottom_y;

    //MINO
    Mino currentMino;
final int MINO_START_X;
    final int MINO_START_Y;
    Mino nextMino;
    final int NEXTMINO_X;
    final int NEXTMINO_Y;
    public static ArrayList<Block>staticBlocks= new ArrayList<>();
    //others
    public static int dropInterval=60; //mino drops

    //effect
    boolean effectCounterOn;
    int effectCounter;
    ArrayList<Integer>effectY=new ArrayList<>();
    boolean gameOver;


    //SCORE
    int level=1;
    int lines;
    int score;

    public PlayManager(){
        // main play area frame
        left_x=(GamePanel.WIDTH/2)-(WIDTH/2); //1280/2-360/2;
        right_x=left_x +WIDTH;
        top_y=50;
        bottom_y=top_y+HEIGHT;

        MINO_START_X= left_x+(WIDTH/2)- Block.SIZE;
        MINO_START_Y= top_y+ Block.SIZE;

        NEXTMINO_X=right_x+175;
        NEXTMINO_Y=top_y+500;

        //st the starting mino
        currentMino=pickMino();
        currentMino.setXY(MINO_START_X,MINO_START_Y);

        nextMino=pickMino();
        nextMino.setXY(NEXTMINO_X,NEXTMINO_Y);
    }
    private Mino pickMino(){
        // pick a random mino
        Mino mino=null;
        int i =new Random().nextInt(7);

        switch (i){
            case 0:mino =new Mino_L1();break;
            case 1:mino =new Mino_L2();break;
            case 2:mino =new Mino_Square();break;
            case 3:mino =new Mino_Bar();break;
            case 4:mino =new Mino_T();break;
            case 5:mino =new Mino_Z1();break;
            case 6:mino =new Mino_Z2();break;
        }
        return mino;
    }
    public void update(){
if (currentMino.active==false){
    staticBlocks.add(currentMino.b[0]);
    staticBlocks.add(currentMino.b[1]);
    staticBlocks.add(currentMino.b[2]);
    staticBlocks.add(currentMino.b[3]);
    //game over
    if (currentMino.b[0].x==MINO_START_X && currentMino.b[0].y==MINO_START_Y){
        gameOver=true;
    }

    currentMino.deactivating=false;



//replacte the crrent mnio next mnio
    currentMino=nextMino;
    currentMino.setXY(MINO_START_X,MINO_START_Y);
    nextMino=pickMino();
    nextMino.setXY(MINO_START_X,MINO_START_Y);


    checkDelete();
}
else {
    currentMino.update();
}
    }
    public void checkDelete() {
        int x = left_x;
        int y = top_y;
        int blockCount = 0;
        int lineCount=0;


        while (x < right_x && y < bottom_y) {
            for (int i = 0; i < staticBlocks.size(); i++) {
                if (staticBlocks.get(i).x == x && staticBlocks.get(i).y == y) {
                    blockCount++;
                }
            }
            x += Block.SIZE;

            if (x == right_x) {
                if (blockCount == 12) {

                    effectCounterOn=true;
                    effectY.add(y);


                    // Remove blocks in the current row
                    for (int i = staticBlocks.size() - 1; i >= 0; i--) {
                        if (staticBlocks.get(i).y == y) {
                            staticBlocks.remove(i);
                        }
                    }

                    lineCount++;
                    lines++;

                    //drop speed if the line score ,increasing the drop speed

                    if (lines%10==0&&dropInterval>1){
                        level++;
                        if (dropInterval>10){
                            dropInterval-=10;
                        }else {
                            dropInterval-=1;
                        }
                    }

                    // Shift blocks above the removed row down
                    for (int i = 0; i < staticBlocks.size(); i++) {
                        if (staticBlocks.get(i).y < y) {
                            staticBlocks.get(i).y += Block.SIZE;
                        }
                    }
                }

                blockCount = 0;
                x = left_x;
                y += Block.SIZE;
            }
        }
        if (lineCount>0){
            int singleLineScore=10*level;
            score+=singleLineScore*lineCount;
        }
    }


    public void draw(Graphics2D g2){

        //draw play area
        g2.setColor(Color.WHITE);
        g2.setStroke( new BasicStroke(4f));
        g2.drawRect(left_x-4,top_y-4,WIDTH+8,HEIGHT+8);

        // DRAW NEXT MINI FRAME
        int x=right_x+100;
        int y=bottom_y-200;

        g2.drawRect(x,y,200,200);
        g2.setFont(new Font("Arial",Font.PLAIN,30));
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.drawString("NEXT",x+60,y+60);

        // draw next mino inside the next mini frame
        nextMino.setXY(x + 85, y + 100); // adjust the coordinates as needed
        nextMino.draw(g2);

        //draw score frame
        g2.drawRect(x,top_y,250,300);
        x+=40;
        y=top_y+90;
        g2.drawString("LEVEL:"+level,x,y); y+=70;
        g2.drawString("LINES:"+lines,x,y); y+=70;
        g2.drawString("SCORE:"+score,x,y);



        // draw the current mino
        if (currentMino != null){
            currentMino.draw(g2);
        }

        // DRAW STATIC BLOCKS
        for (int i = 0; i < staticBlocks.size(); i++){
            staticBlocks.get(i).draw(g2);
        }
        //draw effect
        if (effectCounterOn){
            effectCounter++;

            g2.setColor(Color.red);
            for (int i =0; i<effectY.size();i++){
                g2.fillRect(left_x,effectY.get(i),WIDTH,Block.SIZE);
            }
            if (effectCounter==10){
                effectCounterOn=false;
                effectCounter=0;
                effectY.clear();
            }
        }
        // draw pause
        g2.setColor(Color.RED);
        g2.setFont(g2.getFont().deriveFont(50f));
        if (gameOver){
            x=left_x+25;
            y=top_y+320;
            g2.drawString("GAME OVER",x,y);

        }
        if (KeyHandler.pausePressed){
            x=left_x+70;
            y=top_y+320;
            g2.drawString("PAUSED",x,y);
        }
        x=35;
        y=top_y+320;
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Times New Roman",Font.ITALIC,60));
        g2.drawString("TETRIS",x+80,y);
    }


}
