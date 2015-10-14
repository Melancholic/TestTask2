package com.anagorny;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by Andrey Nagorny on 13.10.15.
 *
 * @author Andrey Nagorny
 *         Точка входа в программу и демонстрация использования вычисляющего метода.
 */
public class Main {
    /**
     * Производит считывание исходных данных и передает их в {@link #eval(long, long)}
     * При считывании данных из файла, первым аргументом программы должен буть путь к файлу,
     * относительно текущей рабочей директории.
     * В противном случае, если имя файла не указано, ввод данных будет совершен в консоли,
     * посредством системного ввода.
     * При вводе данных с клавиатуры максмиальное число полиномов за 1 прогон программы равно 1.
     *
     * @param args Аргументы приложения (через пробел).
     *             Первый аргумент - путь к файла с исходными данными (относительно текущего расположения)
     */
    public static void main(String[] args) {
        long n;
        long m;
        String data;
        if (args.length == 1) {
            try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
                while ((data = br.readLine()) != null) {
                    Scanner scanData = new Scanner(data);
                    n = scanData.nextLong();
                    m = scanData.nextLong();
                    System.out.println(n + " -> " + m + " with " + eval(n, m) + " subsitutions");
                }

            } catch (FileNotFoundException e) {
                System.err.println("Error: File not found: " + args[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.print("Read from console > ");
            Scanner scanIn = new Scanner(System.in);
            n = scanIn.nextLong();
            m = scanIn.nextLong();
            scanIn.close();
            System.out.println(n + " -> " + m + " with " + eval(n, m) + " subsitutions");
        }
    }

    /**
     * Функция вычисляет минимальное число подстановок, необходимое для перевода первого
     * числа во второе.
     *
     * @param n Первое число
     * @param m Второе число
     * @return Минимальное число подстановок
     */
    public static long eval(long n, long m) {
        if (m < n) {
            throw new IllegalArgumentException("Error: The second argument can not be " +
                    "less than the first: " + n + ", " + m + "!");
        }
        long count = 0;
        while (m / 2 >= n && m>1 && m-n>2) {
            if (m - 2 == n + 2 && m / 2 != n) {
                break; // Если 2 сложения выгоднее чем деление, перейти к сложению
            }
            if (m % 2 != 0) {
                m--;
                count++;
            }
            m = m / 2;
            count++;
        }
        while (m - n >= 2) {
            m -= 2;
            count++;
        }
        if (m != n) {
            m -= 1;
            count++;
        }
        return count;
    }
}
