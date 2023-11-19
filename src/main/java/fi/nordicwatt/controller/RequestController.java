package fi.nordicwatt.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fi.nordicwatt.App;
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
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 * Controller for mainworkspace(.fxml). Handle only the user inputs from UI elements. Listens to SaveSettingsController and LoadSettingsController.
 * 
 * @author Antti Hakkarainen
 */
public class RequestController implements SaveSettingsControllerListener, LoadSettingsControllerListener {

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
    private Button addNewTabButton;

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
    public void initialize()
    {
        observeForTabChanges();
        initializeChartTypeChoiceBox();
        initializeXAxisChoiceBox();
        initializeYAxisChoiceBox();
        initializeTimeBoxes();
        initializeRelativeTimeToggle();
        initializeDateBoxes();
        initRemoveCurrentChartButton();
        initLoadPresetButton();
        exportCurrentButton();
        showYAverageCheckBoxClick();
        showYQClick();
        savePresetButton();
        initializeLocationChoiceBox();
    }

    public void addListener(RequestControllerListener listener)
    {
        listeners.add(listener);
    }

    public void removeListener(RequestControllerListener listener)
    {
        listeners.remove(listener);
    }
    
    /**
     * Fills the choice box with values.
     */
    private void initializeLocationChoiceBox()
    {
        if (dataManager.loadLocations() == null)
        {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Location initialization error");
            alert.setContentText("File locations.conf not found.");
            alert.showAndWait();
            Stage stage = (Stage) locationChoiceBox.getScene().getWindow();
            stage.close(); 
        }
        locationChoiceBox.getItems().addAll(dataManager.loadLocations());
        locationChoiceBox.setValue("Tampere");
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

        regenDiagramButton.setDisable(true); // initially disable regen button, fix if session restore is implemented
        mainTabPane.getTabs().addListener((ListChangeListener.Change<? extends Tab> c) -> {
            if (mainTabPane.getTabs().isEmpty()) {
                System.out.println("No tabs... disabling regen button");                
                regenDiagramButton.setDisable(true);
                regenDiagramButton.setStyle("-fx-opacity: 0.4;");
            } else if(regenDiagramButton.isDisabled()) {
                System.out.println("At least one tab.. enabling regen button");
                regenDiagramButton.setDisable(false);
                regenDiagramButton.setStyle("-fx-opacity: 1.0;");
            }
        });
    }

    public void setGenerateButtonsDisabled(boolean state) {
        createDiagramButton.setDisable(state);
        regenDiagramButton.setDisable(state);;
    }

    public void restoreGenerateButtons() 
    {
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
        // figure out tabid

        // either add the chart to new tab or replace an existing tab's content

        // Disable buttons and re-enable them only after failure or success
        // TODO keep buttons always enabled after implementing frontend support for it        
        //setGenerateButtonDisabled(true);

        Logger.log("createDiagramButton pressed"); // writes to log.log

        Map<AxisType, DataType> axisMap = Map.of(
            AxisType.X_AXIS, xAxisChoiceBox.getValue(),
            AxisType.Y_AXIS, yAxisChoiceBox.getValue()
        );

        ChartRequest chartRequest = new ChartRequest(
            chartTypeChoiceBox.getValue(),
            axisMap,
            null,
            locationChoiceBox.getValue(),
            fromDatePicker.getValue().atStartOfDay(),
            toDatePicker.getValue().atTime(23, 59, 59)
        );

        if (requestDispatcher.validateAddChartRequest(chartRequest)) {            
            Logger.log("request validated, sending to sessioncontroller");
            requestDispatcher.dispatchRequest(chartRequest, toNewTab);
        }
        else {
            restoreGenerateButtons();
        }
    }

    /**
     * Called when user clicks the "Generate" button. Creates a ChartRequest
     * based on user's selections in UI, and sends it with more user choices
     * to RequestDispatcher for validation. If validation is successful,
     * RequestDispatcher will send a DataRequest to DataManager.
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
    public void addNewTabAction() {        
        sessionController.addTab();
    }

    @FXML
    public void apiSettingsButtonAction() throws IOException, IllegalStateException {
        try {
            Stage stage = new Stage();
            stage.setTitle("NordicWatt - Api options");
            stage.getIcons().add(new Image("file:doc/logo_small.png"));
            for ( RequestControllerListener l : listeners )
            {
                l.LoadScene(Scenes.ApiOptions.toString(),stage, 800, 300);
            }
        } 
        catch (IOException e) {
            System.err.println("[RequestController]: IOException while loading apioptions.fxml");
            e.printStackTrace();
        }
        catch (IllegalStateException e) {
            System.err.println("[RequestController]: IllegalStateException while loading apioptions.fxml");
            e.printStackTrace();
        }
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

    public void removeChartFromUI(SessionChangeData data) {
        String tabId = data.getTabId();
        String chartId = data.getChartId();

        for (Tab tab : mainTabPane.getTabs()) {
            if (tabId.equals(tab.getId())) {
                // TODO need to change this to support a specific chart in 1-4 chart mode
                Platform.runLater(() -> {
                    sessionController.removeChart(chartId);
                    tab.setContent(null);
                });
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
                return;
            } 
        }
    }


    /**
     * Displays a spinning ProgressIndicator in a tab to indicate that a chart
     * is being generated.
     * 
     * @param data object containing tabid and chartid where to place the spinner
     */
    private void displayProgressIndicator(Button button)
    {   
        ProgressIndicator progressIndicator = new ProgressIndicator();

        progressIndicator.setMinSize(25, 25);
        progressIndicator.setMaxSize(25, 25); 
        progressIndicator.setStyle("-fx-opacity: 1.0; -fx-progress-color: red;");

        button.setStyle("-fx-opacity: 1.0;");        
        button.setText("");       
        button.setGraphic(progressIndicator); 
    }


    /** TODO what is this method used for? -ah */
    @FXML
    private void chartTypeChoiceBoxAction() {
         System.out.println("Chart type Choice Box: Value " +
        chartTypeChoiceBox.getValue() + " was selected");

         if (this.selectedChartType != null && this.selectedChartType != chartTypeChoiceBox.getValue()) {
             System.out.println("UPDATE XY AXIS BOXES");
             updateXYAxisChoiceBoxes();
         }
        this.selectedChartType = chartTypeChoiceBox.getValue();
    }


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

        ChartType selectedChart = chartTypeChoiceBox.getValue();
        System.out.println("InitilaizeXAxisChoiceBox");
        System.out.println("selected chart: " + selectedChart.toString());
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
            }

        });
        updateXAxisLabels();
    }

    private List<DataType> getFilteredDataTypeList(ChartType selectedChart, AxisType axisType) {
        List<DataType> dataTypeList = new ArrayList<>();
        for (DataType dataType : DataType.values()) {
            if (dataType.isAxisAllowed(axisType) && dataType.isChartAllowed(selectedChart)) {
                dataTypeList.add(dataType);
            }
        }
        return dataTypeList;
    }

    private void updateXYAxisChoiceBoxes(){

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
     * Populates the yAxisChoiceBox with DataType enums and sets the default
     * value to CONSUMPTION.
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
            }
        });
        updateYAxisLabels();
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
     * Populates the relativeTimeChoiceBox with String values and sets the default
     * value to "Last 24 hours". Also sets the default values for the datepickers.
     * (which are hidden by default).
     */
    private void initializeTimeBoxes()
    {
        relativeTimeChoiceBox.getItems().addAll(RelativeTimePeriod.values());
        relativeTimeChoiceBox.setValue(RelativeTimePeriod.LAST_24_HOURS);

        fromDatePicker.setValue(RelativeTimePeriod.LAST_24_HOURS.getFromDate());            
        toDatePicker.setValue(LocalDate.now()); 

        relativeTimeChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            fromDatePicker.setValue(newValue.getFromDate());            
            toDatePicker.setValue(LocalDate.now()); 
        });
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
        // TODO these should actually modify the underlying start and end date combo boxes..
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
            try {
                saveSettingsController = SaveSettingsController.getInstance();
                saveSettingsController.addListener(this);
                Stage stage = new Stage();
                stage.setTitle("NordicWatt - Save preset");
                stage.getIcons().add(new Image("file:doc/logo_small.png"));
                for ( RequestControllerListener l : listeners )
                {
                    l.LoadScene(Scenes.SaveSettingsWindow.toString(),stage, 400, 200);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
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
            loadSettingsController = LoadSettingsController.getInstance();
            try {
                loadSettingsController.addListener(this);
                Stage stage = new Stage();
                stage.setTitle("NordicWatt - Load preset");
                stage.getIcons().add(new Image("file:doc/logo_small.png"));
                for ( RequestControllerListener l : listeners )
                {
                    l.LoadScene(Scenes.LoadSettingsWindow.toString(),stage, 400, 200);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }
    @Override
    public void saveSettings(String id)
    {
        try {
                SettingsData settingsData = new SettingsData(chartTypeChoiceBox.getValue(), xAxisChoiceBox.getValue(), yAxisChoiceBox.getValue(), relativeTimeToggle.isSelected(), relativeTimeChoiceBox.getValue(), fromDatePicker.getValue(), toDatePicker.getValue(),locationChoiceBox.getValue());
                dataManager.savePreset(id, settingsData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadSettings(SettingsData settingsData)
    {
        chartTypeChoiceBox.setValue(settingsData.getChartType());
        xAxisChoiceBox.setValue(settingsData.getXAxis());
        yAxisChoiceBox.setValue(settingsData.getYAxis());
        relativeTimeToggle.setSelected(settingsData.isRelativeTime());
        relativeTimeChoiceBox.setValue(settingsData.getRelativeTimePeriod());
        fromDatePicker.setValue(settingsData.getStarttime());
        toDatePicker.setValue(settingsData.getEndtime());
        locationChoiceBox.setValue(settingsData.getLocation());
    }
}
