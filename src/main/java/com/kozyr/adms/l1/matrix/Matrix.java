package com.kozyr.adms.l1.matrix;

import com.kozyr.adms.l1.utils.Logger;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Matrix {
    private final int rowCount;
    private final int columnCount;

    private final List<List<Double>> rows;

    protected Matrix(int rowCount, int columnCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;

        rows = new ArrayList<>(rowCount);
        for (int i = 0; i < rowCount; i++) {
            rows.add(new ArrayList<>(Collections.nCopies(columnCount, null)));
        }
    }

    public static Matrix create(int rowCount, int columnCount) {
        if (rowCount <= 0 || columnCount <= 0)
            throw new IllegalArgumentException("Row and column count must be greater than zero.");
        return new Matrix(rowCount, columnCount);
    }

    public void fill(Double value) {
        for (var row : rows) {
            Collections.fill(row, value);
        }
    }

    public void fill(Matrix source) {
        Objects.requireNonNull(source);
        fill(source.rows);
    }

    public void fill(List<List<Double>> source) {
        Objects.requireNonNull(source);
        if (source.size() != rowCount)
            throw new IllegalArgumentException("The number of rows in the source must be the same as in the target.");
        int expectedColumnCount = source.get(0).size();
        if (expectedColumnCount != columnCount)
            throw new IllegalArgumentException("The number of columns in the source must be the same as in the target.");
        if (source.stream().anyMatch(row -> row.size() != expectedColumnCount))
            throw new IllegalArgumentException("All rows in the source must have the same number of entries.");
        copyRows(source);
    }

    private void copyRows(List<List<Double>> rows) {
        this.rows.clear();
        for (var row : rows) {
            this.rows.add(new ArrayList<>(row));
        }
    }

    public List<List<Double>> export() {
        return rows.stream()
                .map(ArrayList::new)
                .collect(Collectors.toList());
    }

    public final int getRowCount() {
        return rowCount;
    }

    public final int getColumnCount() {
        return columnCount;
    }

    public final Double getEntry(int row, int column) {
        Objects.checkIndex(row, rowCount);
        Objects.checkIndex(column, columnCount);
        return rows.get(row).get(column);
    }

    public final void setEntry(int row, int column, Double value) {
        Objects.checkIndex(row, rowCount);
        Objects.checkIndex(column, columnCount);
        rows.get(row).set(column, value);
    }

    public Matrix gaussianElimination(int pivotRow, int pivotColumn) {
        var matrix = new Matrix(getRowCount(), getColumnCount());
        matrix.fill(this);

        matrix.setEntry(pivotRow, pivotColumn, 1.0);

        var pivot = getEntry(pivotRow, pivotColumn);

        for (int i = 0; i < matrix.getColumnCount(); i++) {
            if (i == pivotColumn) continue;
            var entry = matrix.getEntry(pivotRow, i);
            matrix.setEntry(pivotRow, i, entry * -1);
        }

        for (int i = 0; i < matrix.getRowCount(); i++) {
            for (int j = 0; j < matrix.getColumnCount(); j++) {
                if (i == pivotRow || j == pivotColumn) continue;
                matrix.setEntry(i, j, getEntry(i, j) * pivot - getEntry(i, pivotColumn) * getEntry(pivotRow, j));
            }
        }

        for (int i = 0; i < matrix.getRowCount(); i++) {
            for (int j = 0; j < matrix.getColumnCount(); j++) {
                var entry = matrix.getEntry(i, j);
                matrix.setEntry(i, j, entry / pivot);
            }
        }

        return matrix;
    }

    public String toText(Function<Double, String> valueToText) {
        return export().stream()
                .map(row -> row.stream().map(valueToText)
                        .collect(Collectors.joining(" ")))
                .collect(Collectors.joining("\n"));
    }

    public static Matrix fromText(String text, Function<String, Double> textToValue) {
        var rows = text.lines()
                .map(line -> Arrays.stream(line.split(" "))
                        .map(textToValue)
                        .toList())
                .toList();
        var matrix = create(rows.size(), rows.get(0).size());
        matrix.fill(rows);
        return matrix;
    }

    @Override
    public String toString() {
        return rows.toString();
    }
}