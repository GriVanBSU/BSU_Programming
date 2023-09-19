import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Main {
    public static void main(String[] args) {

        File inputFile = new File("input.txt");
        File outputFile = new File("output.txt");
        String zipFilePath = "info.rar";
        String destDirPath = ".";

        try{
            UnzipFile.unzip(zipFilePath, destDirPath);

            File unzippedFile = new File(destDirPath + "/input.txt");
            Scanner inputScanner = new Scanner(unzippedFile);
            String expression = inputScanner.nextLine();
            inputScanner.close();

            ArithmeticSolver solver = new ArithmeticSolver();
            double result = solver.solve(expression);

            FileWriter outputWriter = new FileWriter(outputFile);
            outputWriter.write(Double.toString(result));
            outputWriter.close();

        }catch(IOException e){
            System.out.println("Error with files : " + e.getMessage());
        }
    }
}