package org.example;

import static java.lang.Math.pow;

public class Result {
    double a;
    double b;
    double c;
    double d;

    public double getY(double x0, double x) {
        return (double) (a + b * (x - x0) + c * pow(x - x0, 2) + d * pow(x - x0, 3));
    }

    public double getYfd(double x0, double x) {
        return (double) (b + 2 * c * (x - x0) + 3 *d * pow(x - x0, 2));
    }

    public double getYsd(double x0, double x) {
        return (double) (2 * c  + 6 * d * (x - x0));
    }
}
