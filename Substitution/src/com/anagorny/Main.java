package com.anagorny;

public class Main {

    public static void main(String[] args) {

        final long a = 5;//new Scanner(System.in).nextInt();

        final long b = (long) 13 ;//10e15;//new Scanner(System.in).nextInt();
        long i=b;
        int count = 0;
        while(i/2>=a){
            if(i%2!=0){
                i--;
                count++;
            }
                i = i >> 1;
                count++;
        }
        while(i-a>=2){
            i-=2;
            count++;
        }
        if(i!=a){
            i-=1;
            count++;
        }

        System.err.println(a + " " + b + ": " + count);

    }

}
