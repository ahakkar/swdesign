package fi.nordicwatt.controller;

import java.io.IOException;
import java.util.List;

import fi.nordicwatt.App;
import fi.nordicwatt.controller.factory.ChartFactory;
import fi.nordicwatt.model.data.ChartRequest;
import fi.nordicwatt.model.data.DataResult;
import fi.nordicwatt.model.services.DataManager;
import fi.nordicwatt.model.services.DataManagerListener;
import fi.nordicwatt.model.session.SessionChangeData;
import fi.nordicwatt.types.Scenes;
import fi.nordicwatt.utils.CustomAlerts;
import fi.nordicwatt.utils.EnvironmentVariables;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.Chart;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * Loads views and controllers. Loads mainworkspace.fxml first. Handles the 
 * application-level logic, like forwarding requests when other classes ask
 * "what should we do with this event?".
 * 
 * Listens (=subscribes to) to DataManager and SessionController events.
 * 
 * @author Antti Hakkarainen
 */
public class PrimaryController implements DataManagerListener, SessionControllerListener
{
    private static PrimaryController instance;
    private RequestController requestController;
    private final DataManager dataManager = DataManager.getInstance();
    private final SessionController sessionController = SessionController.getInstance();
    private final ChartFactory chartFactory = ChartFactory.getInstance();
    private static Scene scene;
    private Stage stage;

    public PrimaryController() {}

    public static PrimaryController getInstance() {
        if (instance == null) {
            instance = new PrimaryController();
        }
        return instance;
    }

    /**
     * Loads up enviroment variables (api keys) and the initial view
     * 
     * @param stage created in App.class
     * @throws IOException
     * 
     * // TODO possibly refactor enviroment variable (=APIKEY) loading somewhere else
     */
    public void init(Stage stage) throws IOException {
        if (this.stage == null) {
            this.stage = stage;
        }

        EnvironmentVariables.getInstance();
        EnvironmentVariables.load(".env");

        LoadScene(Scenes.MainWorkspace.toString());        
        dataManager.registerListener(this);

        sessionController.registerListener(this);
        sessionController.loadSession();
    }

      
    /**
     * Creates a new window for the application
     * 
     * @param sceneName     name of the scene to be loaded, without .fxml extension
     * @throws IOException  if the scene file is not found
     */
    private void LoadScene(String sceneName) throws IOException {
        scene = new Scene(loadFXML(sceneName), 1400, 1000);
        stage.setScene(scene);
        stage.show();
    }


    /**
     * Loads a new view and controller and registers the controller to 
     * requestController variable.
     * 
     * @param sceneName      name of the fxml file to be loaded, without .fxml extension
     * @return               Parent object of the loaded scene
     * @throws IOException   if the scene file is not found
     */
    private Parent loadFXML(String sceneName) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(sceneName + ".fxml"));
            Parent parent = fxmlLoader.load(); 

            this.requestController = fxmlLoader.getController();
            
            return parent;
        }
        catch (IOException e) {
            System.err.println("PrimaryController: Failed to load scene: " + sceneName);
            throw e;
        }
    }

    /**
     * Is called when datamanager has the data on hand and ready to be used.
     * PrimaryController must be registered as datamanager's observer beforehand.
     * 
     * @param data       data from the DataManager used to create charts
     * @param exception  possible exception from the API
     */
    @Override
    public void onDataReadyForChart(
        List<DataResult> data,
        Exception exception
    ) {
        if (exception == null && !data.isEmpty() && data != null) {
            for (DataResult result : data) {     
                String tabId = result.getRequest().getChartMetadata().getTabId();
                String chartId = result.getRequest().getChartMetadata().getChartId();
                ChartRequest chartRequest = sessionController.getChartRequest(tabId, chartId);
               
                if(chartRequest == null) {
                    System.err.println("PrimaryController: chartRequest is null, can't generate chart");
                    return;
                }
                Chart chart = chartFactory.generateChart(
                    result.getData(), 
                    chartRequest
                    );
                chart.setId(chartId);
               
                // execute the chart adding to UI in JavaFX application thread
                Platform.runLater(() -> {
                    requestController.addChartToTab(tabId, chart);
                });
            }           
        }
        else {
            // Reroll back the placeholder chart from UI and SessionController
            for (DataResult result : data) {
                rollBackChart(result, exception);
            }
        }
    }

    private void rollBackChart(DataResult result, Exception exception) {        
        String tabId = result.getRequest().getChartMetadata().getTabId();
        String chartId = result.getRequest().getChartMetadata().getChartId();
        sessionController.removeChart(tabId, chartId);
        Platform.runLater(() -> {
            CustomAlerts.displayAlert(
                    AlertType.ERROR,
                    "Exception from DataManager",
                    "Can't create a chart because of an exception.\nResponse error message below:",
                    exception.toString()
            );
         });

    }


    /**
     * Is called when session's state is changed. For example a new tab was
     * created, or a new tab was created, removed, changed etc. This is used
     * to reflect the internal session's state to the user interface.
     * 
     * @param data                      data about the session change
     * @throws IllegalArgumentException if the session change type is unknown
     */
    @Override
    public void onSessionChange(
        SessionChangeData data
    ) throws IllegalArgumentException {   
        
        // System.out.println("[PrimaryController]: Session change: " + data.getType().toString());

        switch (data.getType()) {
            case TAB_ADDED:
                requestController.addNewTabToUI(data);
                break;

            case TAB_REMOVED:
                requestController.removeTabFromUI(data.getTabId());
                break;

            case TAB_MOVED:
                // TODO: handle tab moved event
                break;

            case TAB_UPDATED:
                // TODO: handle tab updated event
                break;

            case CHART_ADDED:                
                requestController.displayProgressIndicator(data);
                break;

            case CHART_REMOVED:
                requestController.removeChartFromUI(data);
                break;

            case CHART_MOVED:
                // TODO: handle chart moved event
                break;

            case CHART_UPDATED:
                // TODO: handle chart updated event
                break;
  
            default:
                throw new IllegalArgumentException(
                    "[PrimaryController]: Unknown session change type: " + 
                    data.getType().toString()
                );
        }
    }
}