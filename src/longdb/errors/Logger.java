/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package longdb.errors;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author ACER
 */
public class Logger implements Serializable {

    public List<String> readFile(String filename) {
        File file = null;
        FileReader fr = null;
        BufferedReader br = null;
        List<String> list = new ArrayList<>();
        try {

            file = new File(filename);
            fr = new FileReader(file);
            br = new BufferedReader(fr);

            String line = "";
            try {
                while ((line = br.readLine()) != null) {
                    list.add(line);
                }
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;

    }

    public void writeFile(List<String> exception) {
        File file = null;
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {

            file = new File("logFile.txt");
            fw = new FileWriter(file, true);
            bw = new BufferedWriter(fw);

            List<String> listError = readFile("logFile.txt");

            if (exception != null) {
                for (String line : exception) {
                    bw.write("\n");
                    bw.write(line);
                    bw.write("\n");
                }
            }
            bw.close();
            fw.close();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

//    public static void main(String[] args) {
//        Logger obj = new Logger();
////        List<String> listException = obj.readFile("logFile.txt");
////        System.out.println(listException.size());
//        List<String> exception = new ArrayList<>();
//        exception.add("error1");
//        exception.add("error3");
//        obj.writeFile(exception);
//
//    }
}
