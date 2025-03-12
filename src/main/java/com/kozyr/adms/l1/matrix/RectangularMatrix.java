package com.kozyr.adms.l1.matrix;

import com.kozyr.adms.l1.utils.Logger;

public class RectangularMatrix extends Matrix {

    protected RectangularMatrix(int rowCount, int columnCount) {
        super(rowCount, columnCount);
    }

    public static RectangularMatrix create(int rowCount, int columnCount) {
        return new RectangularMatrix(rowCount, columnCount);
    }

    @Override
    public void fill(Matrix source) {
        super.fill(source);
    }

    public int rank() {
        Logger.log("Вхідна матриця:\n");
        Logger.log(toText(value -> String.format("%.2f", value)) + "\n");

        int r = 0;
        var matrix = Matrix.create(getRowCount(), getColumnCount());
        matrix.fill(this);
        for (int i = 0; i < Math.min(getRowCount(), getColumnCount()); i++) {
            var entry = matrix.getEntry(i, i);
            if (entry == 0) continue;
            matrix = matrix.gaussianElimination(i, i);
            Logger.log("Крок " + (r + 1) + ":\n");
            Logger.log(matrix.toText(value -> String.format("%.2f", value)) + "\n\n");
            r++;
        }

        Logger.log("Ранг матриці: " + r + "\n");
        return r;
    }
}
