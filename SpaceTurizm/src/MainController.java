import javafx.fxml.FXML;
import javafx.scene.control.*;

public class MainController {

    @FXML private RadioButton passengerRadio;
    @FXML private RadioButton cargoRadio;
    @FXML private ComboBox<String> fromPlanetCombo;
    @FXML private ComboBox<String> toPlanetCombo;
    @FXML private TextField nameField;
    @FXML private TextField weightField;
    @FXML private Label resultLabel;

    public void initialize() {
        ToggleGroup group = new ToggleGroup();
        passengerRadio.setToggleGroup(group);
        cargoRadio.setToggleGroup(group);

        fromPlanetCombo.getItems().addAll("Mars", "Venus", "Jupiter");
        toPlanetCombo.getItems().addAll("Mars", "Venus", "Jupiter");
    }

    @FXML
    public void handleBuyTicket() {
        String from = fromPlanetCombo.getValue();
        String to = toPlanetCombo.getValue();

        if (passengerRadio.isSelected()) {
            String name = nameField.getText();
            resultLabel.setText("Yolcu Bileti: " + name + " | " + from + " -> " + to);
        } else if (cargoRadio.isSelected()) {
            String weight = weightField.getText();
            resultLabel.setText("Kargo Bileti: " + weight + " kg | " + from + " -> " + to);
        } else {
            resultLabel.setText("Lütfen yolcu ya da kargo seçin.");
        }
    }
}
