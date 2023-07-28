package com.techelevator;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    File file = new File("log.txt");

    public Logger(File file){
        this.file = file;
    }

    public void write(String message){

        LocalDateTime timestamp = LocalDateTime.now();

        DateTimeFormatter targetFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a");
//        timestamp.format(targetFormat);

        try{
            PrintWriter writer = null;

            if (file.exists()){

                writer = new PrintWriter(new FileOutputStream(file, true));
            } else{
                writer = new PrintWriter(file);
            }

            writer.append(timestamp.format(targetFormat) + " " + message + "\n");
            writer.flush();
            writer.close();

        }catch (FileNotFoundException e){
            System.out.println("File can't be accessed");
        }
    }
}
