package fi.nordicwatt.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fi.nordicwatt.model.data.ChartRequest;
import fi.nordicwatt.model.datamodel.SettingsData;
import fi.nordicwatt.model.service.DataManager;
import fi.nordicwatt.model.session.SessionChangeData;
import fi.nordicwatt.types.AxisType;
import fi.nordicwatt.types.ChartType;
import fi.nordicwatt.types.DataType;
import fi.nordicwatt.types.RelativeTimePeriod;
import fi.nordicwatt.types.Scenes;
import fi.nordicwatt.utils.Logger;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.chart.Chart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 * Controller for mainworkspace(.fxml). Handle only the user inputs from UI elements. Listens to
 * SaveSettingsController and LoadSettingsController.
 * 
 * @author Antti Hakkarainen
 */
public class RequestController
        implements SaveSettingsControllerListener, LoadSettingsControllerListener {

    private static final RequestDispatcher requestDispatcher = RequestDispatcher.getInstance();
    private static final SessionController sessionController = SessionController.getInstance();
    private static SaveSettingsController saveSettingsController;
    private static LoadSettingsController loadSettingsController;
    private static final ArrayList<RequestControllerListener> listeners = new ArrayList<>();
    private static final DataManager dataManager = DataManager.getInstance();

    private ChartType selectedChartType = null;

    @FXML
    private TabPane mainTabPane;

    @FXML
    private ChoiceBox<ChartType> chartTypeChoiceBox;

    @FXML
    private ChoiceBox<DataType> xAxisChoiceBox;

    @FXML
    private ChoiceBox<DataType> yAxisChoiceBox;

    @FXML
    private DatePicker fromDatePicker;

    @FXML
    private DatePicker toDatePicker;

    @FXML
    private ChoiceBox<RelativeTimePeriod> relativeTimeChoiceBox;

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
    private Button regenDiagramButton;

    @FXML
    private Button apiSettingsButton;

    @FXML
    private Button savePresetButton;

    @FXML
    private Button loadPresetButton;

    @FXML
    private Label xSourceLabel;

    @FXML
    private Label ySourceLabel;

    @FXML
    private Label xUnitLabel;

    @FXML
    private Label yUnitLabel;

    @FXML
    private TextArea xDescriptionTextArea;

    @FXML
    private TextArea yDescriptionTextArea;

    @FXML
    private ChoiceBox<String> locationChoiceBox;

    /**
     * Populate choicebox values and select defaults
     */
    @FXML
    public void initialize() {
        observeForTabChanges();
        initializeChartTypeChoiceBox();
        initializeXAxisChoiceBox();
        initializeYAxisChoiceBox();
        initializeTimeBoxes();
        initializeRelativeTimeToggle();
        initializeDateBoxes();
        initLoadPresetButton();
        savePresetButton();
        initializeLocationChoiceBox();
    }


    /**
     * Observes changes in mainTabPane and updates the tab id in SessionController, if tab changes.
     */
    private void observeForTabChanges() {
        mainTabPane.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldTab, newTab) -> {
                    if (newTab != null) {
                        String selectedTabId = newTab.getId();
                        sessionController.setCurrentTabId(selectedTabId);
                    }
                });

        regenDiagramButton.setDisable(true); // initially disable regen button, fix if session
                                             // restore is implemented
        mainTabPane.getTabs().addListener((ListChangeListener.Change<? extends Tab> c) -> {
            if (mainTabPane.getTabs().isEmpty()) {
                regenDiagramButton.setDisable(true);
                regenDiagramButton.setStyle("-fx-opacity: 0.4;");
            } else if (regenDiagramButton.isDisabled()) {
                regenDiagramButton.setDisable(false);
                regenDiagramButton.setStyle("-fx-opacity: 1.0;");
            }
        });
    }

    public void setGenerateButtonsDisabled(boolean state) {
        createDiagramButton.setDisable(state);
        regenDiagramButton.setDisable(state);;
    }

    public void restoreGenerateButtons() {
        setGenerateButtonsDisabled(false);
        createDiagramButton.setGraphic(null);
        createDiagramButton.setText("Generate");
        regenDiagramButton.setGraphic(null);
        regenDiagramButton.setText("Regenerate");
    }

    /**
     * 
     * @param toNewTab replace current chart or add a new chart to new tab?
     */
    private void sendChartRequest(boolean toNewTab) {
        Map<AxisType, DataType> axisMap = Map.of(AxisType.X_AXIS, xAxisChoiceBox.getValue(),
                AxisType.Y_AXIS, yAxisChoiceBox.getValue());

        ChartRequest chartRequest = new ChartRequest(chartTypeChoiceBox.getValue(), axisMap, null,
                locationChoiceBox.getValue(), fromDatePicker.getValue().atStartOfDay(),
                toDatePicker.getValue().atTime(23, 59, 59));

        if (requestDispatcher.validateAddChartRequest(chartRequest)) {
            Logger.log("request validated, sending to sessioncontroller");
            requestDispatcher.dispatchRequest(chartRequest, toNewTab);
        } else {
            restoreGenerateButtons();
        }
    }

    /**
     * Called when user clicks the "Generate" button. Creates a ChartRequest based on user's
     * selections in UI, and sends it with more user choices to RequestDispatcher for validation. If
     * validation is successful, RequestDispatcher will send a DataRequest to DataManager.
     */
    @FXML
    public void createDiagramButtonAction() {
        setGenerateButtonsDisabled(true);
        displayProgressIndicator(createDiagramButton);
        sendChartRequest(true);
    }


    @FXML
    public void regenDiagramButtonAction() {
        setGenerateButtonsDisabled(true);
        displayProgressIndicator(regenDiagramButton);
        sendChartRequest(false);

    }

    @FXML
    public void apiSettingsButtonAction() throws IOException, IllegalStateException {
        PrimaryController pc = PrimaryController.getInstance();
        pc.displayApiSettingsWindow();
    }

    /**
     * Bound to a tab's contextmenu, this action is called from the tab's contextmenu when user
     * wants to close a tab. We forward the info to SessionManager which decides what to do.
     * 
     * @param tabId String representation UUID of the tab to be removed
     */
    private void removeTabAction(String tabId) {
        sessionController.removeTab(tabId);
    }


    /**
     * PrimaryController calls this method to remove a tab from UI. Tab is found with the unique id.
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

    public void removeChartFromUI(SessionChangeData data) {
        String tabId = data.getTabId();
        String chartId = data.getChartId();

        for (Tab tab : mainTabPane.getTabs()) {
            if (tabId.equals(tab.getId())) {
                Platform.runLater(() -> {
                    sessionController.removeChart(chartId);
                    tab.setContent(null);
                });
                break;
            }
        }
    }


    /**
     * Adds a new JavaFX Tab element to TabPane mainTabPane. Adds a provided unique ID for the Tab,
     * so it can be identified later. Adds a right click context menu with "Close tab" selection to
     * each Tab.
     * 
     * @param data object containing tab's id and title
     */
    public void addNewTabToUI(SessionChangeData data) {
        Tab newTab = new Tab(data.getTitle());
        newTab.setId(data.getTabId().toString());

        // Contex menu for closing the tab
        ContextMenu contextMenu = new ContextMenu();
        MenuItem closeItem = new MenuItem("Close tab");
        closeItem.setOnAction(event -> {
            removeTabAction(newTab.getId());

        });
        contextMenu.getItems().add(closeItem);

        newTab.setContextMenu(contextMenu);
        mainTabPane.getTabs().add(newTab);
        mainTabPane.getSelectionModel().select(newTab);
    }


    /**
     * After a long round trip around the galaxy and half of the program, PrimaryController finally
     * calls this method to display a generated Chart on a tab.
     * 
     * @param chart Chart a pie/xychart ready to be displayed
     */
    public void addChartToTab(String tabId, Chart chart) {
        for (Tab tab : mainTabPane.getTabs()) {
            if (tabId.equals(tab.getId())) {
                tab.setContent(chart);
                mainTabPane.getSelectionModel().select(tab);
                return;
            }
        }
    }


    /**
     * Displays a spinning ProgressIndicator in a tab to indicate that a chart is being generated.
     * 
     * @param data object containing tabid and chartid where to place the spinner
     */
    private void displayProgressIndicator(Button button) {
        ProgressIndicator progressIndicator = new ProgressIndicator();

        progressIndicator.setMinSize(25, 25);
        progressIndicator.setMaxSize(25, 25);
        progressIndicator.setStyle("-fx-opacity: 1.0; -fx-progress-color: red;");

        button.setStyle("-fx-opacity: 1.0;");
        button.setText("");
        button.setGraphic(progressIndicator);
    }


    /**
     * Triggered every time chart type is changed. Updates the x and y axis choice boxes.
     */
    @FXML
    private void chartTypeChoiceBoxAction() {
        if (this.selectedChartType != null
                && this.selectedChartType != chartTypeChoiceBox.getValue()) {
            updateXYAxisChoiceBoxes();
        }
        this.selectedChartType = chartTypeChoiceBox.getValue();
    }


    /**
     * Who writes these uncommented methods >:( ? -ah
     * 
     */
    private List<DataType> getFilteredDataTypeList(ChartType selectedChart, AxisType axisType) {
        List<DataType> dataTypeList = new ArrayList<>();
        for (DataType dataType : DataType.values()) {
            if (dataType.isAxisAllowed(axisType) && dataType.isChartAllowed(selectedChart)) {
                dataTypeList.add(dataType);
            }
        }
        return dataTypeList;
    }


    /**
     * Who writes these uncommented methods >:( ? -ah
     * 
     */
    private void updateXYAxisChoiceBoxes() {

        DataType xSelection = xAxisChoiceBox.getValue();
        DataType ySelection = yAxisChoiceBox.getValue();

        List<DataType> xData = xAxisChoiceBox.getItems();
        List<DataType> yData = yAxisChoiceBox.getItems();
        xData.clear();
        yData.clear();

        ChartType selectedChart = chartTypeChoiceBox.getValue();
        List<DataType> newXData = getFilteredDataTypeList(selectedChart, AxisType.X_AXIS);
        List<DataType> newYData = getFilteredDataTypeList(selectedChart, AxisType.Y_AXIS);
        xData.addAll(newXData);
        yData.addAll(newYData);
        if (xData.contains(xSelection)) {
            xAxisChoiceBox.setValue(xSelection);
        } else {
            xAxisChoiceBox.setValue(xData.get(0));
        }

        if (yData.contains(ySelection)) {
            yAxisChoiceBox.setValue(ySelection);
        } else {
            yAxisChoiceBox.setValue(yData.get(0));
        }

        updateXAxisLabels();
        updateYAxisLabels();
    }

    /**
     * Triggered every time x label is updated. Updates the x API info in the UI.
     */
    private void updateXAxisLabels() {
        DataType dataType = xAxisChoiceBox.getValue();

        xSourceLabel.setText(dataType.getAPI().toString());
        xUnitLabel.setText(dataType.getUnit().toString());
        xDescriptionTextArea.setText(dataType.getDescription());
    }


    /**
     * Triggered every time y label is updated. Updates the y API info in the UI.
     */
    private void updateYAxisLabels() {
        DataType dataType = yAxisChoiceBox.getValue();

        ySourceLabel.setText(dataType.getAPI().toString());
        yUnitLabel.setText(dataType.getUnit().toString());
        yDescriptionTextArea.setText(dataType.getDescription());
    }


    /**
     * Triggered every time x or y axis is changed. Updates the location choice box visibility.
     * Depends on if the selected DataType enum requires a location or not.
     */
    private void setLocationChoiceBoxDisabled() {
        // These boxes should always have a value, but in some cases they don't.. weird. -ah
        if (xAxisChoiceBox.getValue() == null || yAxisChoiceBox.getValue() == null) {
            return;
        }

        boolean locationRequired = xAxisChoiceBox.getValue().isLocationRequired()
                || yAxisChoiceBox.getValue().isLocationRequired();

        locationChoiceBox.setDisable(!locationRequired);
    }


    /**
     * Most likely loads the settings from a file. (someone implemented this without documenting it
     * -ah)
     */
    private void initLoadPresetButton() {
        loadPresetButton.setOnAction((event) -> {
            loadSettingsController = LoadSettingsController.getInstance();
            try {
                loadSettingsController.addListener(this);
                Stage stage = new Stage();
                stage.setTitle("NordicWatt - Load preset");
                stage.getIcons().add(new Image("file:doc/logo_small.png"));
                for (RequestControllerListener l : listeners) {
                    l.LoadScene(Scenes.LoadSettingsWindow.toString(), stage, 400, 200);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    /**
     * Fills the choice box with values.
     */
    private void initializeLocationChoiceBox() {
        if (dataManager.loadLocations() == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Location initialization error");
            alert.setContentText("File locations.conf not found.");
            alert.showAndWait();
            Stage stage = (Stage) locationChoiceBox.getScene().getWindow();
            stage.close();
        }
        locationChoiceBox.getItems().addAll(dataManager.loadLocations());
        locationChoiceBox.setValue("Tampere");
        locationChoiceBox.setDisable(true);
    }


    /**
     * Populates the relativeTimeChoiceBox with String values and sets the default value to "Last 24
     * hours". Also sets the default values for the datepickers. (which are hidden by default).
     */
    private void initializeTimeBoxes() {
        relativeTimeChoiceBox.getItems().addAll(RelativeTimePeriod.values());
        relativeTimeChoiceBox.setValue(RelativeTimePeriod.TODAY);

        fromDatePicker.setValue(RelativeTimePeriod.TODAY.getFromDate());
        toDatePicker.setValue(LocalDate.now());

        relativeTimeChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            fromDatePicker.setValue(newValue.getFromDate());
            toDatePicker.setValue(LocalDate.now());
        });
    }


    /**
     * Sets the default values for the datepickers. (which are hidden by default).
     */
    private void initializeDateBoxes() {
        fromDatePicker.setValue(LocalDate.now());
        toDatePicker.setValue(LocalDate.now());
    }


    /**
     * Initializes the relativeTimeToggle and binds it to the relativeTimeChoiceBox. When the toggle
     * is selected, the choicebox is hidden and vice versa.
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
                .then("Relative time").otherwise("Real time"));

        // Instantly trigger this event to set the default state
        relativeTimeToggle.setSelected(!relativeTimeToggle.isSelected());
    }


    /**
     * Populates the xAxisChoiceBox with String values from DataTypes enum, and sets the default
     * value to "Time".
     */
    private void initializeXAxisChoiceBox() {

        ChartType selectedChart = chartTypeChoiceBox.getValue();
        xAxisChoiceBox.setConverter(new StringConverter<DataType>() {
            public String toString(DataType type) {
                return (type == null) ? "" : type.toString();
            }

            public DataType fromString(String label) {
                return null;
            }
        });

        // Filter the DataType values to only include those with X_AXIS in their allowedAxes
        List<DataType> xDataTypeList = getFilteredDataTypeList(selectedChart, AxisType.X_AXIS);

        xAxisChoiceBox.getItems().addAll(xDataTypeList);
        xAxisChoiceBox.setValue(DataType.TIME);
        xAxisChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && oldValue != newValue) {
                updateXAxisLabels();
                setLocationChoiceBoxDisabled();
            }

        });

        updateXAxisLabels();
    }


    /**
     * Populates the yAxisChoiceBox with DataType enums and sets the default value to CONSUMPTION.
     */
    private void initializeYAxisChoiceBox() {

        ChartType selectedChart = chartTypeChoiceBox.getValue();
        yAxisChoiceBox.setConverter(new StringConverter<DataType>() {
            public String toString(DataType type) {
                return (type == null) ? "" : type.toString();
            }

            public DataType fromString(String label) {
                return null;
            }
        });

        // Filter the DataType values to only include those with Y_AXIS in their allowedAxes
        List<DataType> yDataTypeList = getFilteredDataTypeList(selectedChart, AxisType.Y_AXIS);

        yAxisChoiceBox.getItems().addAll(yDataTypeList);
        yAxisChoiceBox.setValue(yDataTypeList.get(0));
        yAxisChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && oldValue != newValue) {
                updateYAxisLabels();
                setLocationChoiceBoxDisabled();
            }
        });
        updateYAxisLabels();
    }


    /**
     * Populates the chartTypeChoiceBox with ChartType enums and sets the default value to
     * LINE_CHART.
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
     * Most likely saves the current settings to a file. (someone implemented this without
     * documenting it -ah)
     */
    private void savePresetButton() {
        savePresetButton.setOnAction((event) -> {
            try {
                saveSettingsController = SaveSettingsController.getInstance();
                saveSettingsController.addListener(this);
                Stage stage = new Stage();
                stage.setTitle("NordicWatt - Save preset");
                stage.getIcons().add(new Image("file:doc/logo_small.png"));
                for (RequestControllerListener l : listeners) {
                    l.LoadScene(Scenes.SaveSettingsWindow.toString(), stage, 400, 200);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    @Override
    public void saveSettings(String id) {
        try {
            SettingsData settingsData =
                    new SettingsData(chartTypeChoiceBox.getValue(), xAxisChoiceBox.getValue(),
                            yAxisChoiceBox.getValue(), relativeTimeToggle.isSelected(),
                            relativeTimeChoiceBox.getValue(), fromDatePicker.getValue(),
                            toDatePicker.getValue(), locationChoiceBox.getValue());
            dataManager.savePreset(id, settingsData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadSettings(SettingsData settingsData) {
        chartTypeChoiceBox.setValue(settingsData.getChartType());
        xAxisChoiceBox.setValue(settingsData.getXAxis());
        yAxisChoiceBox.setValue(settingsData.getYAxis());
        relativeTimeToggle.setSelected(settingsData.isRelativeTime());
        relativeTimeChoiceBox.setValue(settingsData.getRelativeTimePeriod());
        fromDatePicker.setValue(settingsData.getStarttime());
        toDatePicker.setValue(settingsData.getEndtime());
        locationChoiceBox.setValue(settingsData.getLocation());
    }


    public void addListener(RequestControllerListener listener) {
        listeners.add(listener);
    }

    public void removeListener(RequestControllerListener listener) {
        listeners.remove(listener);
    }
}
