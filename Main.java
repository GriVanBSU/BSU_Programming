package org.example;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.GCMParameterSpec;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the name of the archive file (without extension): ");
        String archiveFileName = scanner.nextLine();
        System.out.println("Enter the extension of the archive file: ");
        String archiveFileExtension = scanner.nextLine();
        String archiveFilePath = archiveFileName + "." + archiveFileExtension;

        System.out.println("Enter the name of the text file (without extension): ");
        String textFileName = scanner.nextLine();
        System.out.println("Enter the extension of the text file: ");
        String textFileExtension = scanner.nextLine();
        String textFilePath = textFileName + "." + textFileExtension;

        System.out.println("Enter the method to use for solving the arithmetic expression:");
        System.out.println("regular - Solution via regular expressions.");
        System.out.println("line - Solution via line reading.");
        System.out.println("library - Решение через библиотеку  exp4j");
        String choice = scanner.nextLine();

        try {
            String key = "mysecretKeyiSkek";
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
            byte[] iv = new byte[12];
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv);

            File archiveFile = new File(archiveFilePath);
            FileInputStream archiveInput = new FileInputStream(archiveFile);
            byte[] archiveBytes = new byte[(int) archiveFile.length()];
            archiveInput.read(archiveBytes);
            archiveInput.close();

            File textFile = UnzipFile.unzip(archiveFilePath, textFilePath);

            FileInputStream textInput = new FileInputStream("D:\\JavaLab1\\input.txt\\input.txt");
            byte[] textBytes =  textInput.readAllBytes();
            textInput.close();

//            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
//            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, gcmParameterSpec);
//            byte[] encryptedBytes = cipher.doFinal(textBytes);
//
//            cipher = Cipher.getInstance("AES/GCM/NoPadding");
//            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, gcmParameterSpec);
//            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
//            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);


            Files.write(Paths.get("myfile.txt"), textBytes);
            BufferedReader reader = new BufferedReader(new FileReader("myfile.txt"));
            String expression = reader.readLine();
            double result = 0;
            FileWriter outputWriter = new FileWriter("output.txt");

            while(expression != null){
                if (choice.equals("regular")) {
                    result = ArithmeticSolver.regexSolve(expression);
                } else if (choice.equals("line")) {
                    ArithmeticSolver solver = new ArithmeticSolver();
                    result = solver.solve(expression);
                } else if(choice.equals("library")){
                    ArithmeticSolver solver = new ArithmeticSolver();
                    result = solver.solveByLibrary(expression);
                }
                else {
                    System.out.println("Invalid input....");
                    return;
                }

                outputWriter.write(Double.toString(result) + "\n");
                expression = reader.readLine();
            }

            reader.close();
            outputWriter.close();
        } catch (Exception e) {
            System.out.println("Error with files: " + e.getMessage());
        }
    }
}