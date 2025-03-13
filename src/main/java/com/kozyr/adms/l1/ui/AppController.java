package com.kozyr.adms.l1.ui;

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
    private CheckBox screenProtocolCheckbox;

    @FXML
    private CheckBox fileProtocolCheckbox;

    @FXML
    private Spinner<Integer> variableCountSpinner;

    @FXML
    private RadioButton minRadioButton;

    @FXML
    private RadioButton maxRadioButton;

    @FXML
    private TextField matrixTargetFunctionTextField;

    @FXML
    private TextField xTextField;

    @FXML
    private TextField zTextField;

    @FXML
    private TextArea limitationsTextArea;

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeDimensionSpinners();
        initializeMinMax();
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            var alert = new Alert(Alert.AlertType.ERROR, e.getCause().getCause().getMessage(), ButtonType.OK);
            alert.show();
        });
    }

    private void initializeDimensionSpinners() {
        var rowCountSpinnerFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1);
        variableCountSpinner.setValueFactory(rowCountSpinnerFactory);
    }

    private void initializeMinMax() {
        ToggleGroup toggleGroup = new ToggleGroup();
        minRadioButton.setToggleGroup(toggleGroup);
        maxRadioButton.setToggleGroup(toggleGroup);
        toggleGroup.selectToggle(minRadioButton);
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
    private void onExampleButtonAction(ActionEvent ignoredEvent) {

    }

    @FXML
    private void onFindOptimalSolutionAction(ActionEvent ignoredEvent) {

    }
}
