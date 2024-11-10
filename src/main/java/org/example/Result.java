package org.example;

import static java.lang.Math.pow;

public class Result {
    float a;
    float b;
    float c;
    float d;

    public float getY(float x0, float x) {
        return (float) (a + b * (x - x0) + c * pow(x - x0, 2) + d * pow(x - x0, 3));
    }
}
