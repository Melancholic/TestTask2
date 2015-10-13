package com.anagorny;


import com.anagorny.Polynomial.Polynomial;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by Andrey Nagorny on 10.10.15.
 *
 * @author Andrey Nagorny
 * Точка входа в программу и демонстрация использования полинома.
 */
public class Main {
    /**
     * Производит считывание исходных данных и передает их в {@link com.anagorny.Polynomial.Polynomial}
     * При считывании данных из файла, первым аргументом программы должен буть путь к файлу,
     * относительно текущей рабочей директории.
     * В противном случае, если имя файла не указано, ввод данных будет совершен в консоли,
     * посредством системного ввода.
     * При вводе данных с клавиатуры максмиальное число полиномов за 1 прогон программы равно 1.
     * @see com.anagorny.Polynomial.Polynomial
     * @param args Аргументы приложения (через пробел).
     *             Первый аргумент - путь к файла с исходными данными (относительно текущего расположения)
     */
    public static void main(String[] args) {
        String data;
        if (args.length == 1) {
            try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
                while ((data = br.readLine()) != null) {
                    System.out.println("Cannonical form: " + Polynomial.parse(data));
                }

            } catch (FileNotFoundException e) {
                System.err.println("Error: File not found: " + args[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.print("Read from console > ");
            Scanner scanIn = new Scanner(System.in);
            data = scanIn.nextLine();
            scanIn.close();
            System.out.println("Cannonical form: " + Polynomial.parse(data));
        }
    }
}
