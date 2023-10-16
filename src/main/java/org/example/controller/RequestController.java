package org.example.controller;

import java.time.LocalDate;
import java.util.Map;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.util.StringConverter;
import javafx.scene.chart.Chart;

import org.example.model.data.ChartRequest;
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
    private static final SessionController sessionController = SessionController.getInstance();

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
    public void initialize()
    {
        observeForTabChanges();
        initializeChartTypeChoiceBox();
        initializeXAxisChoiceBox();
        initializeYAxisChoiceBox();
        initializeRelativeTimeChoiceBox();
        initializeRelativeTimeToggle();
        initializeDateBoxes();
        initRemoveCurrentChartButton();
        initLoadPresetButton();
        exportCurrentButton();
        showYAverageCheckBoxClick();
        showYQClick();
        savePresetButton();   
    }


    /**
     * Observes changes in mainTabPane and updates the tab id in
     * SessionController, if tab changes.
     */
    private void observeForTabChanges()
    {
        mainTabPane.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldTab, newTab) -> {
                if (newTab != null) {
                    String selectedTabId = newTab.getId();
                    sessionController.setCurrentTabId(selectedTabId);
                }
            }
        );
    }


    /**
     * Called when user clicks the "Generate" button. Creates a ChartRequest
     * based on user's selections in UI, and sends it with more user choices
     * to RequestDispatcher for validation. If validation is successful,
     * RequestDispatcher will send a DataRequest to DataManager.
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
            null         // Constructed in RequestDispatcher after validation         
        );

        requestDispatcher.validateAddChartRequest(
            chartRequest,
            yAxisChoiceBox.getValue(),
            fromDatePicker.getValue().atStartOfDay(),
            toDatePicker.getValue().atStartOfDay(),
            "tampere", // TODO remove placeholder hardcoded location!!
            sessionController.getTabIdForChart()
        );
    }

    @FXML 
    public void addNewTabAction() {        
        sessionController.addTab();
    }

    /**
     * Bound to a tab's contextmenu, this action is called from the tab's 
     * contextmenu when user wants to close a tab. We forward the info to
     * SessionManager which decides what to do.
     * 
     * @param tabId String representation UUID of the tab to be removed
     */
    private void removeTabAction(String tabId) {
        sessionController.removeTab(tabId);
    }

    /**
     * Adds a new JavaFX Tab element to TabPane mainTabPane. Adds a provided
     * unique ID for the Tab, so it can be identified later. Adds a right 
     * click context menu with "Close tab" selection to each Tab.
     * 
     * @param data object containing tab's id and title
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
     * After a long round trip around the galaxy and half of the program,
     * PrimaryController finally calls this method to display a generated Chart 
     * on a tab.
     * 
     * @param chart Chart a pie/xychart ready to be displayed
     */
    public void addChartToTab(String tabId, Chart chart) {
        for (Tab tab : mainTabPane.getTabs()) {
            if (tabId.equals(tab.getId())) {
                tab.setContent(chart);
                mainTabPane.getSelectionModel().select(tab);

                break;
            }
        }
    }


    /** TODO what is this method used for? -ah */
    @FXML
    private void chartTypeChoiceBoxAction() {
        // System.out.println("Chart type Choice Box: Value " +
        // chartTypeChoiceBox.getValue() + " was selected");
    }

    /** TODO what is this method used for? -ah */
    @FXML
    private void testSomething() {  }


    /**
     * Populates the chartTypeChoiceBox with ChartType enums and sets the default
     * value to LINE_CHART.
     */
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


    /**
     * Populates the xAxisChoiceBox with String values and sets the default
     * value to "Time".
     * 
     * TODO replace hardcoded value with actual options
     */
    private void initializeXAxisChoiceBox() {
        xAxisChoiceBox.getItems().add("Time");
        xAxisChoiceBox.setValue("Time");
    }


    /**
     * Populates the yAxisChoiceBox with DataType enums and sets the default
     * value to CONSUMPTION.
     */
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


    /**
     * Populates the relativeTimeChoiceBox with String values and sets the default
     * value to "Last 24 hours".
     * 
     * TODO remove hard-coded values and move them over to an enum or something
     */
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
     * TODO adjust default values
     */
    private void initializeDateBoxes() {
        fromDatePicker.setValue(LocalDate.now().minusDays(1));
        toDatePicker.setValue(LocalDate.now());
    }


    /**
     * Initializes the relativeTimeToggle and binds it to the relativeTimeChoiceBox.
     * When the toggle is selected, the choicebox is hidden and vice versa.
     */
    private void initializeRelativeTimeToggle() {
        relativeTimeChoiceBox.visibleProperty().bind(relativeTimeToggle.selectedProperty());
        relativeTimeLabel.visibleProperty().bind(relativeTimeToggle.selectedProperty());
        fromDatePicker.visibleProperty().bind(relativeTimeToggle.selectedProperty().not());
        toDatePicker.visibleProperty().bind(relativeTimeToggle.selectedProperty().not());
        startTimeLabel.visibleProperty().bind(relativeTimeToggle.selectedProperty().not());
        endTimeLabel.visibleProperty().bind(relativeTimeToggle.selectedProperty().not());

        // Update the Button based on the toggle selection
        relativeTimeToggle.textProperty().bind(Bindings.when(relativeTimeToggle.selectedProperty())
            .then("Relative time")
            .otherwise("Real time"));

        // Instantly trigger this event to set the default state
        relativeTimeToggle.setSelected(!relativeTimeToggle.isSelected());
    }


    /**
     * initiates the button to open up a dialog. Currently only sets
     * a placeholder text in dialog and does nothing. Dialog should be able
     * to close on ok and cancel buttons.
     * 
     * TODO implement some actual functionality to removeCurrentChartButton
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


    /**
     * initiates the button to open up a dialog. Currently only sets
     * a placeholder text in dialog and does nothing. Dialog should be able
     * to close on ok and cancel buttons.
     * 
     * TODO implement some actual functionality to exportCurrentButton
     */
    private void exportCurrentButton() {
        exportCurrentButton.setOnAction((event) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("This is placeholder");
            alert.setHeaderText(null); // No header
            alert.setContentText("This action would export the chart we see in the view as image or pdf.");
            alert.showAndWait();
        });
    }


    /**
     * initiates the button to open up a dialog. Currently only sets
     * a placeholder text in dialog and does nothing. Dialog should be able
     * to close on ok and cancel buttons.
     * 
     * TODO implement some actual functionality to showYAverageCheckBoxClick
     */
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


    /**
     * initiates the button to open up a dialog. Currently only sets
     * a placeholder text in dialog and does nothing. Dialog should be able
     * to close on ok and cancel buttons.
     * 
     * TODO implement some actual functionality to showYQClick
     */
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


    /**
     * initiates the button to open up a dialog. Currently only sets
     * a placeholder text in dialog and does nothing. Dialog should be able
     * to close on ok and cancel buttons.
     * 
     * TODO implement some actual functionality to savePresetButton
     */
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


    /**
     * initiates the button to open up a dialog. Currently only sets
     * a placeholder text in dialog and does nothing. Dialog should be able
     * to close on ok and cancel buttons.
     * 
     * TODO implement some actual functionality to initLoadPresetButton
     */
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
}
