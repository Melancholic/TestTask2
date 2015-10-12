package com.company;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        int a= 2;//new Scanner(System.in).nextInt();

        final int b= 12;//new Scanner(System.in).nextInt();
        int i=b;
        int count=0;
        int iPred=0;
        while(Integer.toBinaryString(i).length()>Integer.toBinaryString(a).length()){
           iPred=i;
           i = i >> 1;
           i+=(iPred-(i<<1));
           count++;
        }
        System.err.println(a + " " + i+": "+count);
        while(i!=a){
            if(b>a){
                i--;
                count++;
            }else{
                i++;
                count++;
            }
        }
        System.err.println(a + " " + b+": "+count);

    }

}
