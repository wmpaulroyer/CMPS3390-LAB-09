import java.util.*;
import java.io.*;

public class NameGenerator{
    public static void main(String[] args){
        if(args.length < 1){
            System.out.print("Usage: java NameGenerator howMany\n");
            System.exit(0);
        }

        int howMany = Integer.parseInt(args[0]);
        String[] names = new String[howMany];

        GenerateNamesThread generateNames = new GenerateNamesThread(names, howMany);
        generateNames.start();

        try{
            generateNames.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }

        for (String name : names) {
            System.out.println(name);
        }
    }
}

//sets up threads for first and last name
class GenerateNamesThread extends Thread{
    String[] first, last, names;

    public GenerateNamesThread(String [] n, int count){
        first = new String[count];
        last = new String[count];
        names = n;
    }

    public void run(){
        GenFirst th1 = new GenFirst(first);
        GenLast th2 = new GenLast(last);
        th1.start();
        th2.start();

        MergeNamesThread mergeNames = new MergeNamesThread(th1, th2, first, last, names);
        mergeNames.start();

        try{
            mergeNames.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}

//generates first name arraylist
class GenFirst extends Thread{
    String[] firstNames;

    public GenFirst(String[] f){
        firstNames = f;
    }

    public void run(){
        ArrayList<String> nameList = new ArrayList<String>();
        Random rand = new Random();

        try{
            Scanner scan = new Scanner(new File("resources/first.txt"));
            while(scan.hasNextLine()){
                nameList.add(scan.nextLine());
            }

            for(int i=0; i<firstNames.length; i++){
                firstNames[i] = nameList.get(rand.nextInt(nameList.size()));
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }
}

//generates last name arraylist
class GenLast extends Thread{
    String[] lastNames;

    public GenLast(String[] l){
        lastNames = l;
    }

    public void run(){
        ArrayList<String> nameList = new ArrayList<String>();
        Random rand = new Random();

        try{
            Scanner scan = new Scanner(new File("resources/last.txt"));
            while(scan.hasNextLine()){
                nameList.add(scan.nextLine());
            }

            for(int i=0; i<lastNames.length; i++){
                lastNames[i] = nameList.get(rand.nextInt(nameList.size()));
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }
}

//merges first name arraylist and last name arraylist
class MergeNamesThread extends Thread{
    Thread thd1 = null, thd2 = null;
    String[] first, last, names;

    public MergeNamesThread(Thread t1, Thread t2, String[] f, String[] l, String[] n){
        thd1 = t1; thd2 = t2;
        first = f; last = l; names = n;
    }

    public void run(){
        try{
            if(thd1 != null) thd1.join();
            if(thd2 != null) thd2.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        for(int i=0; i<names.length; i++){
            names[i] = first[i] + " " + last[i];
        }
    }
}
