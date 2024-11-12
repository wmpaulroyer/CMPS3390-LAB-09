import java.util.Random;
import java.awt.*;
import javax.swing.*;

class OilMonitor{
    public static void main(String[] args){
        JFrame frame = new JFrame("Oil Monitor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new DrawOilPumps());
        frame.setSize(800, 800);
        frame.setVisible(true);
    }
}

class DrawOilPumps extends JPanel{
    OilPump[][] pumps;

    //constructor sets up 2d array of OilPumps
    public DrawOilPumps(){
        this.pumps = new OilPump[5][4];
        Random rand = new Random();
        int pumpCount = 1;

        for(int i=0; i<pumps.length; i++){
            for(int j=0; j<pumps[i].length; j++){
                String label = String.format("Pump %d", pumpCount++);
                int x = (i+1)*135;
                int y = (j+1)*135;
                Color color = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
                int speed = rand.nextInt(50) + 50;
                pumps[i][j] = new OilPump(label,x,y,color,speed);
            }
        }
    }

    //Calls the update() function for each pump
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        for (OilPump[] pumpRow : pumps) {
            for (OilPump oilPump : pumpRow) {
                oilPump.update(g);
            }
        }
    }

    //Threaded OilPump class
    class OilPump implements Runnable{
        String label;
        int x, y;
        Color color;
        int value, speed, direction, total;

        //OilPump constructor
        OilPump(String l, int x, int y, Color c, int s){
            this.label = l;
            this.x = x;
            this.y = y;
            this.color = c;
            this.speed = s;
            this.value = 0;
            this.direction = 1;
            this.total = 0;

            new Thread(this).start();
        }

        //updates the Graphics area with current data when called
        public void update(Graphics g){
            Font font = new Font("Arial", Font.BOLD, 12);
            g.setFont(font);
            g.setColor(color);
            int size = value/2;

            g.fillOval(x-size,y-size,value,value);
            g.setColor(Color.black);
            g.drawString(label, x-50, y+65);
            g.drawString(""+value, x-7, y+5);
            g.drawString("TOTAL: "+total, x-50, y+80);
        }

        //Updates value of current OilPump & repaints
        public void run(){
            while(true){
                value += (direction);
                total += value;
                if(value <= 0) direction = 1;
                if(value >= 100) direction = -1;

                repaint();
                try{
                    Thread.sleep(speed);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
