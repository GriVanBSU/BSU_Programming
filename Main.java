import java.io.IOException;
import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.*;

public class Main {
    public static void main(String[] args) {

        input();
        output();
    }

    public static void input(){
        try{
            File file = new File("input.txt");
            if(!file.exists()){
                file.createNewFile();
            }

            PrintWriter pw = new PrintWriter(file);
            pw.println("5 + 3 - 7");
            pw.close();

        }catch(IOException e){
            System.out.println("Error : " + e);
        }
    }

    public static void output(){
        try{
            BufferedReader br = null;
            br = new BufferedReader(new FileReader("input.txt"));
            String line;

            while((line = br.readLine()) != null){
                System.out.println(line);
            }

            br.close();
        }
        catch(IOException exe) {
            System.out.println("Error : " + exe);
        }
    }
}