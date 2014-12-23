package erlyberly;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.HBox;
import de.jensd.fx.fontawesome.AwesomeIcon;
import de.jensd.fx.fontawesome.Icon;
import floatyfield.FloatyFieldView;

public class TopBarView implements Initializable {
	private static final KeyCodeCombination TOGGLE_HIDE_PROCESSES_SHORTCUT = new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN);

	private static final KeyCodeCombination TOGGLE_HIDE_MODULES_SHORTCUT = new KeyCodeCombination(KeyCode.M, KeyCombination.CONTROL_DOWN);
	
	@FXML
	private ToggleButton hideProcessesButton;
	@FXML
	private ToggleButton hideFunctionsButton;
  	@FXML
	private HBox topBox;
	
	@Override
	public void initialize(URL url, ResourceBundle r) {
		hideProcessesButton.setGraphic(Icon.create().icon(AwesomeIcon.RANDOM));
		hideProcessesButton.setContentDisplay(ContentDisplay.TOP);
		hideProcessesButton.setGraphicTextGap(0d);
		hideProcessesButton.setTooltip(new Tooltip("Show/Hide the Processes"));

		hideFunctionsButton.setGraphic(Icon.create().icon(AwesomeIcon.CUBE));
		hideFunctionsButton.setContentDisplay(ContentDisplay.TOP);
		hideFunctionsButton.setGraphicTextGap(0d);
		hideFunctionsButton.setTooltip(new Tooltip("Show/Hide the Modules"));
		
		hideProcsProperty().addListener((Observable o) -> { toggleHideProcsText(); });
		hideFunctionsProperty().addListener((Observable o) -> { toggleHideFuncsText(); });
		
		// these have to be run after initialisation is complete or an exception occurs
		Platform.runLater(() -> {
			accelerators().put(TOGGLE_HIDE_PROCESSES_SHORTCUT, () -> { invertSelection(hideProcessesButton); });
		});
		Platform.runLater(() -> {
			accelerators().put(TOGGLE_HIDE_MODULES_SHORTCUT, () -> { invertSelection(hideFunctionsButton); });
		});
		
		FxmlLoadable loader = new FxmlLoadable("/floatyfield/floaty-field.fxml");
		
		loader.load();
		
		Parent fxmlNode;
		
		fxmlNode = loader.fxmlNode;
		fxmlNode.getStyleClass().add("floaty-label");

		FloatyFieldView ffView;
		
		ffView = (FloatyFieldView) loader.controller;
		ffView.promptTextProperty().set("Processes");
		ffView.textProperty().set("3434");
		ffView.disableProperty().set(true);
		
		topBox.getChildren().add(new Separator(Orientation.VERTICAL));
		topBox.getChildren().add(loader.fxmlNode);
		
		toggleHideProcsText();
		toggleHideFuncsText();
	}

	private ObservableMap<KeyCombination, Runnable> accelerators() {
		Scene scene = hideProcessesButton.getScene();
		
		assert scene != null : "button not added to scene";
		
		return scene.getAccelerators();
	}
	
	private void invertSelection(ToggleButton toggleButton) {
		toggleButton.setSelected(!toggleButton.isSelected());
	}
	
	public BooleanProperty hideProcsProperty() {
		return hideProcessesButton.selectedProperty();
	}
	
	public BooleanProperty hideFunctionsProperty() {
		return hideFunctionsButton.selectedProperty();
	}
	
	private void toggleHideProcsText() {
		String buttonText = "";
		
		if(hideProcessesButton.isSelected())
			buttonText = "Show Processes";
		else
			buttonText = "Hide Processes";
		
		hideProcessesButton.setText(buttonText);
	}
	
	private void toggleHideFuncsText() {
		String buttonText = "";
		
		if(hideFunctionsButton.isSelected())
			buttonText = "Show Modules";
		else
			buttonText = "Hide Modules";
		
		hideFunctionsButton.setText(buttonText);
	}
}
