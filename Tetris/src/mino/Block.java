package mino;

import java.awt.*;

public class Block extends Rectangle {
    public int x,y;
    public static final int SIZE=30;
    public Color c;

    public Block(Color c){
        this.c=c;

    }
    public void draw(Graphics2D g2){
        g2.setColor(c);
        int margine=2;
        g2.fillRect(x+margine,y+margine,SIZE-(margine*2),SIZE-(margine*2));
    }
}
