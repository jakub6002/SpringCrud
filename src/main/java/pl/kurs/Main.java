package pl.kurs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.kurs.controller.CarController;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);

//        try (BufferedWriter out = new BufferedWriter(new FileWriter("books.csv"))) {
//            for (int i = 0; i < 20_000_000; i++) {
//                int randomAuthro = (int) (Math.random() * 2) + 1;
//                out.write("title_" + i + ",ROMANCE," + randomAuthro);
//                out.newLine();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }
}