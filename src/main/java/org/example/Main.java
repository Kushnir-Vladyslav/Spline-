package org.example;

import java.time.Duration;
import java.time.Instant;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

public class Main {

    public static int size = 1000;

    public static float[] x = new float[size];
    public static float[] y = new float[size];


    public static void main(String[] args) {

        Instant startI = Instant.now();

        for (int i = 0; i < size; i++) {
            x[i] = i;
            y[i] = (float) Math.sin((double) i);

//            System.out.println(x[i] + " " + y[i]);
        }


        ForkJoinPool pool = new ForkJoinPool();

        pool.invoke(new Spline(x, y));


//        for (Result result: GlobalState.result) {
//            System.out.println(result.a + " " + result.b + " " + result.c + " " + result.d);
//        }
        Instant endI = Instant.now();
        printExecutionTime(startI, endI);

        JavaFXran.main(args);


    }

    private static void printExecutionTime(Instant start, Instant end) {
        Duration duration = Duration.between(start, end);
        System.out.println("Час виконання: " + duration.toSeconds() + " sec");
        System.out.println("Час виконання: " + duration.toMillis() + " millisec");
    }
}


