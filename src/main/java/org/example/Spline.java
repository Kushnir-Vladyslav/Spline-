package org.example;

import java.util.concurrent.RecursiveAction;

public class Spline extends RecursiveAction {
    float[] x;
    float[] y;

    Result[] results;
    Gause[] gauses;

    Spline (float[] x, float[] y) {
        this.x = x;
        this.y = y;
    }

    @Override
    protected void compute() {
        results = new Result[x.length - 1];

        for (int i = 0; i < results.length; i++) {
            results[i] = new Result();
            results[i].a = y[i];
        }

        results[0].c = 0;
        float dx = x[1] - x[0];
        float dx2 = dx * dx;
        float dx3 = dx2 * dx;

        gauses = new Gause[(x.length - 1) * 3 - 1];

        for (int i = 0; i < gauses.length; i++) {
            gauses[i] = new Gause();
        }

        //заповнення масиву
        {
            gauses[0].b = dx;
            gauses[0].d = dx3;
            gauses[0].s = y[1] - y[0];
            gauses[1].d = 3 * dx2;
            gauses[1].bb = -1;
            gauses[2].d = 3 * dx;
            gauses[2].cc = -1;

            for (int i = 3; i < gauses.length - 2; i += 3) {
                gauses[i].b = dx;
                gauses[i].c = dx2;
                gauses[i].d = dx3;
                gauses[i].s = y[i / 3 + 1] - y[i / 3];
                gauses[i + 1].c = 2 * dx;
                gauses[i + 1].d = 3 * dx2;
                gauses[i + 1].bb = -1;
                gauses[i + 2].d = 3 * dx;
                gauses[i + 2].cc = -1;

            }
            gauses[gauses.length - 2].b = dx;
            gauses[gauses.length - 2].c = dx2;
            gauses[gauses.length - 2].d = dx3;
            gauses[gauses.length - 2].s = y[y.length - 1] - y[y.length - 2];
            gauses[gauses.length - 1].d = 3 * dx;
        }

        //пряме проходження
        {
            gauses[0].s /= dx;
            gauses[0].d = dx2;

            gauses[1].s -= gauses[0].s / 2 * dx2;
            gauses[1].bb /= 2 * dx2;

            gauses[2].s -= gauses[1].s;
            gauses[2].cc /= gauses[2].d;
            gauses[2].bb -= gauses[1].bb;

            for (int i = 2; i < gauses.length - 3; i += 3) {
                gauses[i].s /= gauses[i].bb;
                gauses[i].cc /= gauses[i].bb;

                gauses[i + 1].s /= gauses[i + 1].b;
                gauses[i + 1].d /= gauses[i + 1].b;
                gauses[i + 1].s /= gauses[i + 1].b;

                gauses[i + 1].s -= gauses[i].s;
                gauses[i + 1].c -= gauses[i].cc;

                gauses[i + 2].s -= gauses[i].s;
                gauses[i + 2].c -= gauses[i].cc;


                gauses[i + 1].s /= gauses[i + 1].c;
                gauses[i + 1].d /= gauses[i + 1].c;

                gauses[i + 2].s /= gauses[i + 2].c;
                gauses[i + 2].bb /= gauses[i + 2].c;
                gauses[i + 2].d /= gauses[i + 2].c;

                gauses[i + 2].s -= gauses[i + 1].s;
                gauses[i + 2].d -= gauses[i + 1].d;

                gauses[i + 3].s -= gauses[i + 1].s;
                gauses[i + 3].d -= gauses[i + 1].d;


                gauses[i + 2].s /= gauses[i + 2].d;
                gauses[i + 2].bb /= gauses[i + 2].d;

                gauses[i + 3].s /= gauses[i + 3].d;
                gauses[i + 3].cc /= gauses[i + 3].d;

                gauses[i + 3].s -= gauses[i + 2].s;
                gauses[i + 3].bb -= gauses[i + 2].bb;
            }

            gauses[gauses.length - 3].s /= gauses[gauses.length - 3].bb;
            gauses[gauses.length - 3].cc /= gauses[gauses.length - 3].bb;

            gauses[gauses.length - 2].s /= gauses[gauses.length - 2].b;
            gauses[gauses.length - 2].d /= gauses[gauses.length - 2].b;
            gauses[gauses.length - 2].c /= gauses[gauses.length - 2].b;

            gauses[gauses.length - 2].s -= gauses[gauses.length - 3].s;
            gauses[gauses.length - 2].c -= gauses[gauses.length - 3].cc;


            gauses[gauses.length - 2].s /= gauses[gauses.length - 2].c;
            gauses[gauses.length - 2].d /= gauses[gauses.length - 2].c;

            gauses[gauses.length - 1].s -= gauses[gauses.length - 2].s;
            gauses[gauses.length - 1].d -= gauses[gauses.length - 2].d;


            gauses[gauses.length - 1].s /= gauses[gauses.length - 1].d;
        }

        //зворотне пргоходження
        {
            results[results.length - 1].d = gauses[gauses.length - 1].s;
            results[results.length - 1].c = gauses[gauses.length - 2].s - results[results.length - 1].d * gauses[gauses.length - 2].d;
            results[results.length - 1].b = gauses[gauses.length - 3].s - results[results.length - 1].c * gauses[gauses.length - 3].cc;

            for (int i = results.length - 2; i > 0; i--) {
                results[i].d = gauses[i * 3  + 1].s - results[i + 1].b * gauses[i * 3  + 1].bb;
                results[i].c = gauses[i * 3].s - results[i].d * gauses[i * 3].d;
                results[i].b = gauses[i * 3  - 1].s - results[i].c * gauses[i * 3 - 1].cc;
            }

            results[0].d = gauses[1].s - results[1].b * gauses[1].bb;
            results[0].b = gauses[0].s - results[0].d * gauses[0].d;
        }

        GlobalState.result = results;
    }
}


