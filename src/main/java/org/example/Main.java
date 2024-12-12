package org.example;

import java.time.Duration;
import java.time.Instant;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

public class Main {

    public static int size = 25;

    public static double[] x = new double[size];
    public static double[] y = new double[size];

    static double indentation = 2.5;
    static double step = 0.0001;

    public static void main(String[] args) {

        Instant startI = Instant.now();

        for (int i = 0; i < size; i++) {
            x[i] = i * step + indentation;
            y[i] = Math.sin(x[i]);
            y[i] = x[i] * x[i];

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


