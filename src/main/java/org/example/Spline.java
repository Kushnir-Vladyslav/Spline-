package org.example;

import java.text.DecimalFormat;
import java.util.concurrent.RecursiveAction;

public class Spline extends RecursiveAction {
    double[] x;
    double[] y;

    Result[] results;
    Gause[] gauses;

    Spline (double[] x, double[] y) {
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

        double[] dx = new double[3];
        dx[0] = x[1] - x[0];
        dx[1] = dx[0] * dx[0];
        dx[2] = dx[1] * dx[0];

        results[0].c = (2 * y[3] - 5 * y[2] + 4 * y[1] - y[0]) / (dx[1] * 2);
        double rightBoundary = (2 * y[x.length - 1] - 5 * y[x.length - 2] + 4 * y[x.length - 3] - y[x.length - 4]) / dx[1] ;

        gauses = new Gause[(x.length - 1) * 3 - 1];

        for (int i = 0; i < gauses.length; i++) {
            gauses[i] = new Gause();
            gauses[i].clear();
        }

        //заповнення масиву
        {
            gauses[0].firstEquationZeroDerivative(dx, y[1] - y[0] - results[0].c * dx[1]);
            gauses[1].firstEquationFirstDerivative(dx, -2 * results[0].c * dx[0]);
            gauses[2].firstEquationSecondDerivative(dx, -2 * results[0].c);

            for (int i = 3; i < gauses.length - 2; i += 3) {
                gauses[i].zeroDerivative(dx, y[i / 3 + 1] - y[i / 3]);
                gauses[i + 1].firstDerivative(dx);
                gauses[i + 2].secondDerivative(dx);
            }

            gauses[gauses.length - 2].zeroDerivative(dx, y[y.length - 1] - y[y.length - 2]);
            gauses[gauses.length - 1].boundaryCondition(dx, rightBoundary);
        }

        //пряме проходження
        {
            for (int i = 0; i < gauses.length - 2; i++) {
                gauses[i].subBottomLines(gauses[i + 1], gauses[i + 2]);
            }

            if (gauses[gauses.length - 2].coefficients[2] != 1) {
                gauses[gauses.length - 2].reductionCentral();
            }
            if (gauses[gauses.length - 1].coefficients[1] != 0) {
                gauses[gauses.length - 1].reductionPrevious(1);
                gauses[gauses.length - 2].subFirstBottomLine(gauses[gauses.length - 1]);
            }
        }

        //зворотне пргоходження
        {
            gauses[gauses.length - 1].freeMember /= gauses[gauses.length - 1].coefficients[2];

            for (int i = gauses.length - 1; i > 0; i--) {
                gauses[i].subTopLine(gauses[i - 1]);
            }
        }

        //отримання результатів
        {
            results[0].b = gauses[0].freeMember;
            results[0].d = gauses[1].freeMember;

            for (int i = 1; i < results.length; i++) {
                results[i].b = gauses[i * 3 - 1].freeMember;
                results[i].c = gauses[i * 3].freeMember;
                results[i].d = gauses[i * 3 + 1].freeMember;
            }
        }

        GlobalState.result = results;
    }
}


