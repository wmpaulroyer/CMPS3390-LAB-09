import java.util.Random;
import java.awt.*;
import javax.swing.*;

public class StockTicker{
    public static void main(String[] args){
        JFrame frame = new JFrame("Stock Ticker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new DrawBars());
        frame.setSize(800, 700);
        frame.setVisible(true);
    }
}

//sets up the drawing area and an array of 50 stocks to animate
class DrawBars extends JPanel{
    Stock[] stocks;
    int count = 50;

    public DrawBars(){
        this.stocks = new Stock[count];

        for(int i=0; i<count; i++){
            this.stocks[i] = new Stock();
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        int height = getHeight();
        int spacing = height/count;

        Font font = new Font("Arial", Font.BOLD, 10);
        g.setFont(font);
        for(int i=0; i<count; i++){
            int position = (i+1) * spacing;
            g.setColor(new Color(stocks[i].rColor, stocks[i].gColor, stocks[i].bColor));
            g.fillRect(0, position, stocks[i].value, 5);
            g.drawString(stocks[i].name + " ("+stocks[i].value+")", stocks[i].value+5, position+6);
        }
        update();
        repaint();
    }

    public void update() {
        try{
            Thread.sleep(500);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        for (Stock stock : stocks) {
            stock.updateValue();
        }
    }
}

//Stock class
class Stock{
    Random rand = new Random();
    String name = "";
    int rColor, gColor, bColor;
    int value;

    //Constructor
    public Stock(){
        int length = rand.nextInt(3)+2;

        for(int i=0; i<length; i++){
            this.name += (char)(rand.nextInt(26) + 'A');
        }

        this.rColor = rand.nextInt(200);
        this.gColor = rand.nextInt(200);
        this.bColor = rand.nextInt(200);

        this.value = rand.nextInt(290) + 10;
    }

    //Updates the Stock value
    public void updateValue(){
        value += rand.nextInt(21) - 10;
        if(value < 0) value = 0;
    }
}
