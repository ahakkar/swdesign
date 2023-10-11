package org.example.controller;

import java.time.LocalDate;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;
import javafx.scene.chart.Chart;
import javafx.scene.chart.XYChart;

import org.example.model.data.ChartRequest;
import org.example.model.data.DataResult;
import org.example.model.session.SessionChangeData;
import org.example.types.DataType;
import org.example.types.AxisType;
import org.example.types.ChartType;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import javafx.scene.control.CheckBox;

/**
 * Controller for mainworkspace(.fxml). Handle only the user inputs from UI elements.
 * 
 * @author Antti Hakkarainen
 */
public class RequestController {

    private static final RequestDispatcher requestDispatcher = RequestDispatcher.getInstance();
    private static final SessionController sessionManager = SessionController.getInstance();

    @FXML
    private TabPane mainTabPane;

    @FXML
    private ChoiceBox<ChartType> chartTypeChoiceBox;

    @FXML
    private ChoiceBox<String> xAxisChoiceBox;

    @FXML
    private ChoiceBox<DataType> yAxisChoiceBox;

    @FXML
    private DatePicker fromDatePicker;

    @FXML
    private DatePicker toDatePicker;

    @FXML
    private ChoiceBox<String> relativeTimeChoiceBox;

    @FXML
    private ToggleButton relativeTimeToggle;

    @FXML
    private Label relativeTimeLabel;

    @FXML
    private Label startTimeLabel;

    @FXML
    private Label endTimeLabel;

    @FXML
    private Button removeCurrentChartButton;

    @FXML
    private Button exportCurrentButton;

    @FXML
    private CheckBox showYAverageCheckBox;

    @FXML
    private CheckBox showYQ;

    @FXML
    private Button createDiagramButton;

    @FXML
    private Button savePresetButton;

    @FXML
    private Button loadPresetButton;

    @FXML
    private Button addNewTabButton;

    /**
     * Populate choicebox values and select defaults
     */
    @FXML
    public void initialize() {
        initializeChartTypeChoiceBox();
        initializeXAxisChoiceBox();
        initializeYAxisChoiceBox();
        initializeRelativeTimeChoiceBox();
        initializeRelativeTimeToggle();
        initRemoveCurrentChartButton();
        exportCurrentButton();
        showYAverageCheckBoxClick();
        showYQClick();
        //generateNewDiagramButton();
        savePresetButton();
        initLoadPresetButton();
        initDateBoxes();

    }

    /**
     * Decides what happens when user clicks the Create Diagram button. Probably
     * one of the most important methods in this class.
     */
    @FXML
    public void createDiagramButtonAction() {
        Map<AxisType, DataType> axisMap = Map.of(
            AxisType.X_AXIS, DataType.TIME,
            AxisType.Y_AXIS, yAxisChoiceBox.getValue()
        );

        // TODO display a spinning widget while chart is being generated
        ChartRequest chartRequest = new ChartRequest(
            yAxisChoiceBox.getValue().getAPIType(),
            chartTypeChoiceBox.getValue(),
            axisMap,
            null            
        );

        requestDispatcher.validateAddChartRequest(
            chartRequest,
            yAxisChoiceBox.getValue(),
            fromDatePicker.getValue().atStartOfDay(),
            toDatePicker.getValue().atStartOfDay(),
            null,
            mainTabPane.getSelectionModel().getSelectedItem().getId()
        );
    }

    @FXML 
    public void addNewTabAction() {        
        sessionManager.addTab();
    }

    /**
     * Bound to a tab's contextmenu, this action is called from the tab's 
     * contextmenu when user wants to close a tab. We forward the info to
     * SessionManager which decides what to do.
     * 
     * @param tabId
     */
    private void removeTabAction(String tabId) {
        sessionManager.removeTab(tabId);
    }

    /**
     * Adds a new JavaFX Tab element to TabPane mainTabPane
     */
    public void addNewTabToUI(SessionChangeData data) {
        Tab newTab = new Tab(data.getTitle());
        newTab.setId(data.getId().toString());

        // Contex menu for closing the tab
        ContextMenu contextMenu = new ContextMenu();
        MenuItem closeItem = new MenuItem("Close tab");
        closeItem.setOnAction(event -> {
            removeTabAction(newTab.getId());
            
        });
        contextMenu.getItems().add(closeItem);

        newTab.setContextMenu(contextMenu);
        mainTabPane.getTabs().add(newTab);
    }

    /**
     * PrimaryController calls this method to remove a tab from UI. Tab is
     * found with the unique id.
     * 
     * @param tabId String representation UUID of the tab to be removed
     */
    public void removeTabFromUI(String tabId) {
        for (Tab tab : mainTabPane.getTabs()) {
            if (tabId.equals(tab.getId())) {
                mainTabPane.getTabs().remove(tab);
                break;
            }
        }
    }

    /**
     * PrimaryController calls this method to display a generated chart on a tab
     * @param chart Chart a pie/xychart ready to be displayed
     */
    public void addChartToTab(String tabId, Chart chart) {
        // TODO actually place the chart into a tab with specified id
        // TODO and probably switch the active tab to id as well
    }


    @FXML
    private void chartTypeChoiceBoxAction() {
        // System.out.println("Chart type Choice Box: Value " +
        // chartTypeChoiceBox.getValue() + " was selected");
    }

    @FXML
    private void testSomething() {
        
    }

    private void initializeChartTypeChoiceBox() {
        // allows storing ChartType enums directly as values of the ChoiceBox
        chartTypeChoiceBox.setConverter(new StringConverter<ChartType>() {
            public String toString(ChartType type) {
                return (type == null) ? "" : type.toString();
            }

            public ChartType fromString(String label) {
                return null;
            }
        });

        chartTypeChoiceBox.getItems().addAll(ChartType.values());
        chartTypeChoiceBox.setValue(ChartType.LINE_CHART);
    }

    private void initializeXAxisChoiceBox() {
        xAxisChoiceBox.getItems().add("Time");
        xAxisChoiceBox.setValue("Time");
    }

    private void initializeYAxisChoiceBox() {
        yAxisChoiceBox.setConverter(new StringConverter<DataType>() {
            public String toString(DataType type) {
                return (type == null) ? "" : type.toString();  
            }

            public DataType fromString(String label) {
                return null; 
            }            
        });

        yAxisChoiceBox.getItems().addAll(DataType.values());
        yAxisChoiceBox.setValue(DataType.CONSUMPTION);
    }

    private void initializeRelativeTimeChoiceBox() {

        relativeTimeChoiceBox.getItems().addAll(
                "Last 24 hours",
                "Last 3 days",
                "Last 7 days",
                "Last month",
                "Last year");
        relativeTimeChoiceBox.setValue("Last 24 hours");
    }

    /**
     * Adds relativeTimeToggle logic
     * When toggle is selected:
     * - relativeTimeChoiceBox is shown
     * - fromDatePicker and toDatePicker are hidden
     * - shows and hides their labels accordingly
     * When toggle is not selected:
     * - relativeTimeChoiceBox is hidden
     * - fromDatePicker and toDatePicker are shown
     * - shows and hides their labels accordingly
     */
    private void initializeRelativeTimeToggle() {
        relativeTimeToggle.setOnAction((event) -> {
            if (relativeTimeToggle.isSelected()) {
                relativeTimeToggle.setText("Relative time");
                relativeTimeChoiceBox.setVisible(true);
                fromDatePicker.setVisible(false);
                toDatePicker.setVisible(false);
                relativeTimeLabel.setVisible(true);
                startTimeLabel.setVisible(false);
                endTimeLabel.setVisible(false);
            } else {
                relativeTimeToggle.setText("Real time");
                relativeTimeChoiceBox.setVisible(false);
                fromDatePicker.setVisible(true);
                toDatePicker.setVisible(true);
                relativeTimeLabel.setVisible(false);
                startTimeLabel.setVisible(true);
                endTimeLabel.setVisible(true);
            }
        });
        // Instantly trigger this event to set the default state
        relativeTimeToggle.fire();
    }

    /**
     * initiates the button to open up a dialog. Currently only sets
     * a placeholder text in dialog and does nothing. Dialog should be able
     * to close on ok and cancel buttons.
     */
    private void initRemoveCurrentChartButton() {
        removeCurrentChartButton.setOnAction((event) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("This is placeholder");
            alert.setHeaderText(null); // No header
            alert.setContentText("This action would remove the currently displayed chart tab.");
            alert.showAndWait();
        });
    }

    private void exportCurrentButton() {
        exportCurrentButton.setOnAction((event) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("This is placeholder");
            alert.setHeaderText(null); // No header
            alert.setContentText("This action would export the chart we see in the view as image or pdf.");
            alert.showAndWait();
        });
    }

    private void showYAverageCheckBoxClick() {
        showYAverageCheckBox.setOnAction((event) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("This is placeholder");
            alert.setHeaderText(null); // No header
            alert.setContentText(
                    "This action would draw a vertical line on the chart where the average of Y value is.");
            alert.showAndWait();
        });
    }

    private void showYQClick() {
        showYQ.setOnAction((event) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("This is placeholder");
            alert.setHeaderText(null); // No header
            alert.setContentText(
                    "This action would draw two vertical lines on the chart of the upper and lower quartile of Y value.");
            alert.showAndWait();
        });
    }

/*     private void generateNewDiagramButton() {
        generateNewDiagramButton.setOnAction((event) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("This is placeholder");
            alert.setHeaderText(null); // No header
            alert.setContentText(
                    "This action works the same as regenerate, but it would open a new tab for the new diagram. That is we will not lose the old diagram.");
            alert.showAndWait();
        });
    } */

    private void savePresetButton() {
        savePresetButton.setOnAction((event) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("This is placeholder");
            alert.setHeaderText(null); // No header
            alert.setContentText(
                    "This action would save the current diagram search terms as a preset. One could then load the preset to get the same diagram by selecting the preset from the general tab.");
            alert.showAndWait();
        });
    }

    private void initLoadPresetButton() {
        loadPresetButton.setOnAction((event) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("This is placeholder");
            alert.setHeaderText(null); // No header
            alert.setContentText(
                    "This action would load a preset and set the search terms to to predifend values. \nThis could be imlpelemented by opening a dialog where one could select the preset from a list.\nOther option would be to have a dropdown menu in the tab you just pressed.");
            alert.showAndWait();
        });
    }

    /**
     * TODO adjust default values
     */
    private void initDateBoxes() {
        fromDatePicker.setValue(LocalDate.now().minusDays(1));
        toDatePicker.setValue(LocalDate.now());
    }

    private void replacePlaceholderChart(XYChart<?, ?> chart) {
        // chartPlaceholder.getChildren().clear();

        // Glue added the chart to anchorpane's borders
        AnchorPane.setTopAnchor(chart, 0.0);
        AnchorPane.setRightAnchor(chart, 0.0);
        AnchorPane.setBottomAnchor(chart, 0.0);
        AnchorPane.setLeftAnchor(chart, 0.0);

        // chartPlaceholder.getChildren().add(chart);
    }


}
