package com.kozyr.adms.l1.ui;

import com.kozyr.adms.l1.matrix.Matrix;
import com.kozyr.adms.l1.matrix.RectangularMatrix;
import com.kozyr.adms.l1.matrix.SquareMatrix;
import com.kozyr.adms.l1.utils.DoubleUtils;
import com.kozyr.adms.l1.utils.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AppController implements Initializable {
    @FXML
    private Spinner<Integer> rowCountSpinner;

    @FXML
    private Spinner<Integer> columnCountSpinner;

    @FXML
    private CheckBox generateACheckBox;

    @FXML
    private CheckBox generateBCheckBox;

    @FXML
    private CheckBox screenProtocolCheckbox;

    @FXML
    private CheckBox fileProtocolCheckbox;

    @FXML
    private TextArea squareMatrixTextArea;

    @FXML
    private TextArea rectangularMatrixTextArea;

    @FXML
    private TextArea inverseMatrixTextArea;

    @FXML
    private TextField matrixRankTextField;

    @FXML
    private TextArea soleSolutionSetTextArea;

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeDimensionSpinners();
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            var alert = new Alert(Alert.AlertType.ERROR, e.getCause().getCause().getMessage(), ButtonType.OK);
            alert.show();
        });
    }

    private void initializeDimensionSpinners() {
        var rowCountSpinnerFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1);
        rowCountSpinner.setValueFactory(rowCountSpinnerFactory);

        var columnCountSpinnerFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 1);
        columnCountSpinner.setValueFactory(columnCountSpinnerFactory);
    }

    @FXML
    private void onGenerateMatricesButtonAction(ActionEvent ignoredEvent) {
        if (generateACheckBox.isSelected()) {
            generateRandomSquareMatrix(rowCountSpinner.getValue());
        }

        if (generateBCheckBox.isSelected()) {
            generateRandomRectangularMatrix(rowCountSpinner.getValue(), columnCountSpinner.getValue());
        }
    }

    @FXML
    private void onGenerateProtocolButtonAction(ActionEvent ignoredEvent) {
        var log = Logger.getLog();
        if (fileProtocolCheckbox.isSelected()) {
            saveProtocolToFile(log);
        }
        if (screenProtocolCheckbox.isSelected()) {
            showPopupProtocol(log);
        }
    }

    public static void saveProtocolToFile(String log) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("protocol.txt"))) {
            writer.write(log);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void showPopupProtocol(String log) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Протокол");

        TextArea textArea = new TextArea();
        textArea.setWrapText(true);
        textArea.setPrefSize(400, 300);

        textArea.setText(log);

        ScrollPane scrollPane = new ScrollPane(textArea);
        scrollPane.setFitToWidth(true);

        VBox layout = new VBox(10, scrollPane);
        layout.setStyle("-fx-padding: 10;");

        Scene popupScene = new Scene(layout, 450, 350);
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }

    @FXML
    private void onFindInverseMatrixButtonAction(ActionEvent ignoredEvent) {
        var matrix = Matrix.fromText(squareMatrixTextArea.getText(), DoubleUtils::stringToDouble);
        var squareMatrix = SquareMatrix.create(matrix.getRowCount());
        squareMatrix.fill(matrix);
        var inverseMatrix = squareMatrix.findInverse();
        inverseMatrixTextArea.setText(inverseMatrix.toText(value -> String.format("%.2f", value)));
    }

    @FXML
    private void onCalculateRankButtonAction(ActionEvent ignoredEvent) {
        var matrix = Matrix.fromText(rectangularMatrixTextArea.getText(), DoubleUtils::stringToDouble);
        var rectangularMatrix = RectangularMatrix.create(matrix.getRowCount(), matrix.getColumnCount());
        rectangularMatrix.fill(matrix);
        var rank = rectangularMatrix.rank();
        matrixRankTextField.setText(String.valueOf(rank));
    }

    @FXML
    private void onCalculateSoleButtonAction(ActionEvent ignoredEvent) {
        var matrix = Matrix.fromText(squareMatrixTextArea.getText(), DoubleUtils::stringToDouble);
        var squareMatrix = SquareMatrix.create(matrix.getRowCount());
        squareMatrix.fill(matrix);

        matrix = Matrix.fromText(rectangularMatrixTextArea.getText(), DoubleUtils::stringToDouble);
        var rectangularMatrix = RectangularMatrix.create(matrix.getRowCount(), matrix.getColumnCount());
        rectangularMatrix.fill(matrix);

        StringBuilder solutionSetText = new StringBuilder();
        for (var value : squareMatrix.sole(rectangularMatrix)) {
            solutionSetText.append(String.format("%.2f", value)).append("\n");
        }
        soleSolutionSetTextArea.setText(solutionSetText.toString());
    }

    private void generateRandomSquareMatrix(int dimension) {
        var matrix = SquareMatrix.create(dimension);
        for (int rowIndex = 0; rowIndex < dimension; rowIndex++) {
            for (int colIndex = 0; colIndex < dimension; colIndex++) {
                matrix.setEntry(rowIndex, colIndex, Math.random() * 89 + 10);
            }
        }
        squareMatrixTextArea.setText(matrix.toText(value -> String.format("%.2f", value)));
    }

    private void generateRandomRectangularMatrix(int rowCount, int columnCount) {
        var matrix = RectangularMatrix.create(rowCount, columnCount);
        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            for (int colIndex = 0; colIndex < columnCount; colIndex++) {
                matrix.setEntry(rowIndex, colIndex, Math.random() * 89 + 10);
            }
        }
        rectangularMatrixTextArea.setText(matrix.toText(value -> String.format("%.2f", value)));
    }
}
