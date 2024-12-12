package org.example;

public class Gause {
    public double[] coefficients = new double[4];
    public double freeMember;

   public void clear() {
       for (int i = 0; i < coefficients.length; i++) {
           coefficients[i] = 0;
       }
       freeMember = 0;
   }

   public void firstEquationZeroDerivative (double[] dx, double S) {
       coefficients[2] = dx[0];
       coefficients[3] = dx[2];
       freeMember = S;
   }
    public void firstEquationFirstDerivative (double[] dx, double S) {
        coefficients[1] = 1;
        coefficients[2] = 3 * dx[1];
        coefficients[3] = -1;
        freeMember = S;
    }

    public void firstEquationSecondDerivative (double[] dx, double S) {
        coefficients[1] = 6 * dx[0];
        coefficients[3] = -2;
        freeMember = S;
    }

    public void zeroDerivative (double[] dx, double S) {
        coefficients[1] = dx[0];
        coefficients[2] = dx[1];
        coefficients[3] = dx[2];
        freeMember = S;
    }
    public void firstDerivative (double[] dx) {
        coefficients[0] = 1;
        coefficients[1] = 2 * dx[0];
        coefficients[2] = 3 * dx[1];
        coefficients[3] = -1;
    }

    public void secondDerivative (double[] dx) {
        coefficients[0] = 2;
        coefficients[1] = 6 * dx[0];
        coefficients[3] = -2;
    }

    public void boundaryCondition (double[] dx, double S) {
        coefficients[1] = 2;
        coefficients[2] = 6 * dx[0];
        freeMember = S;
    }

    public void reductionCentral() {
       if (coefficients[2] != 1) {
           freeMember /= coefficients[2];
           coefficients[3] /= coefficients[2];
       }
    }

    public void reductionPrevious(int i) {
        if (coefficients[i] != 1) {
            freeMember /= coefficients[i];
            for (int k = 3; k > i; k--) {
                coefficients[k] /= coefficients[i];
            }
        }
    }

    public void subBottomLines(Gause first, Gause second) {
       if (coefficients[2] != 1) {
           reductionCentral();
       }

       if (first.coefficients[1] != 0) {
           first.reductionPrevious(1);
           subFirstBottomLine(first);
       }

       if (second.coefficients[0] != 0) {
           second.reductionPrevious(0);
           subSecondBottomLine(second);
       }
    }

    public void subFirstBottomLine(Gause line) {
       line.coefficients[1] -= coefficients[2];
        line.coefficients[2] -= coefficients[3];
        line.freeMember -= freeMember;
    }

    public void subSecondBottomLine(Gause line) {
        line.coefficients[0] -= coefficients[2];
        line.coefficients[1] -= coefficients[3];
        line.freeMember -= freeMember;
    }

    public void subTopLine(Gause line) {
       line.freeMember -= freeMember * line.coefficients[3];
    }
}
