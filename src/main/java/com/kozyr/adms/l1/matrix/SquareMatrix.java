package com.kozyr.adms.l1.matrix;

import com.kozyr.adms.l1.utils.Logger;

import java.util.Arrays;
import java.util.List;

public class SquareMatrix extends RectangularMatrix {
    public final int dimension;

    protected SquareMatrix(int dimension) {
        super(dimension, dimension);
        this.dimension = dimension;
    }

    public static SquareMatrix create(int dimension) {
        if (dimension <= 0)
            throw new IllegalArgumentException("Dimension must be greater than zero.");
        return new SquareMatrix(dimension);
    }

    @Override
    public void fill(Matrix source) {
        if (source.getRowCount() != source.getColumnCount())
            throw new IllegalArgumentException("The source matrix must be square.");
        super.fill(source);
    }

    public SquareMatrix findInverse() {
        Logger.log("Протокол обчислення:\n");
        var matrix = Matrix.create(dimension, dimension);
        matrix.fill(this);
        for (int di = 0; di < dimension; di++) {
            var pivot = matrix.getEntry(di, di);
            Logger.log("Розв'язувальний елемент: A[" + (di + 1) + "][" + (di + 1) + "] = " + String.format("%.2f", pivot));

            matrix = matrix.gaussianElimination(di, di);
            Logger.log("Крок " + (di + 1) + ":");
            Logger.log(matrix.toText(value -> String.format("%.2f", value)) + "\n\n");
        }
        var inverseMatrix = SquareMatrix.create(dimension);
        inverseMatrix.fill(matrix);

        Logger.log("Обернена матриця:\n");
        Logger.log(inverseMatrix.toText(value -> String.format("%.2f", value)) + "\n");
        return inverseMatrix;
    }

    public List<Double> sole(RectangularMatrix matrix) {
        if (matrix.getRowCount() != dimension)
            throw new IllegalArgumentException("The source matrix must have the same number of rows as the target's dimension.");

        Logger.log("Вхідна матриця A:\n");
        Logger.log(toText(value -> String.format("%.2f", value)) + "\n");

        var inverseMatrix = findInverse();
        Logger.log("Обернена матриця:\n");
        Logger.log(inverseMatrix.toText(value -> String.format("%.2f", value)) + "\n");

        Logger.log("Вхідна матриця B:\n");
        Logger.log(matrix.toText(value -> String.format("%.2f", value)) + "\n");

        Logger.log("Обчислення розв'язків:\n");
        var solutionSet = new double[dimension];

        for (int i = 0; i < dimension; i++) {
            double rowResult = 0.0;
            StringBuilder calculationLog = new StringBuilder("X[" + (i + 1) + "] = ");
            for (int j = 0; j < dimension; j++) {
                double a = matrix.getEntry(j, 0);
                double b = inverseMatrix.getEntry(i, j);
                double product = a * b;
                rowResult += product;

                calculationLog.append(String.format("%.2f * %.2f", a, b));
                if (j < dimension - 1) {
                    calculationLog.append(" + ");
                }
            }

            calculationLog.append(" = ").append(String.format("%.2f", rowResult)).append("\n");
            Logger.log(calculationLog.toString());
            solutionSet[i] = rowResult;
        }

        return Arrays.stream(solutionSet).boxed().toList();
    }
}
