package org.example.controller;

import java.io.IOException;
import java.util.List;

import org.example.App;
import org.example.controller.factory.ChartFactory;
import org.example.model.data.ChartRequest;
import org.example.model.data.DataResult;
import org.example.model.services.DataManager;
import org.example.model.services.DataManagerListener;
import org.example.model.session.SessionChangeData;
import org.example.types.Scenes;
import org.example.utils.EnvironmentVariables;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.Chart;
import javafx.stage.Stage;

/**
 * Loads views and controllers. Starts from mainworkspace. Handles the 
 * application-level logic, like creating new tabs and charts.
 * 
 * @author Antti Hakkarainen
 */
public class PrimaryController implements DataManagerListener, SessionControllerListener {
    private static PrimaryController instance;
    private RequestController requestController;
    private final DataManager dataManager = DataManager.getInstance();
    private final SessionController sessionManager = SessionController.getInstance();
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
     */
    public void init(Stage stage) throws IOException {
        if (this.stage == null) {
            this.stage = stage;
        }

        EnvironmentVariables.getInstance();
        EnvironmentVariables.load(".env");

        LoadScene(Scenes.MainWorkspace.toString());        
        dataManager.registerListener(this);

        sessionManager.registerListener(this);
        sessionManager.loadSession();
    }

      
    /**
     * Creates a new window for the application
     * 
     * @param sceneName
     * @throws IOException
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
     * @param fxml
     * @return
     * @throws IOException
     */
    private Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        Parent parent = fxmlLoader.load(); 

        this.requestController = fxmlLoader.getController();
        System.out.println("PrimaryController: loaded " + fxml + ".fxml, controller: " + requestController.toString());
        
        return parent;
    }

    /**
     * Is called when datamanager has the data on hand and ready to be used.
     * PrimaryController must be registered as datamanager's observer beforehand.
     */
    @Override
    public void onDataReadyForChart(List<DataResult> data, Exception exception) {
        if (exception == null) {
            for (DataResult result : data) {
                String tabId = result.getRequest().getChartMetadata().getTabId();
                String chartId = result.getRequest().getChartMetadata().getChartId();
                ChartRequest chartRequest = sessionManager.getChartRequest(tabId, chartId);

                Chart chart = chartFactory.generateChart(
                    result.getData(), 
                    chartRequest
                    );
                requestController.addChartToTab(tabId, chart);
            }           
        }
        else {
            System.out.println("Controller got exception");
            exception.printStackTrace();  
        }
    }


    /**
     * Is called when session's state is changed. For example a new tab was
     * created, or a new tab was created, removed, changed etc. This is used
     * to reflect the internal session's state to the user interface.
     */
    @Override
    public void onSessionChange(SessionChangeData data) {
        System.out.println(
            "PrimaryController: Session changed: " + data.getType().toString());
          

        switch (data.getType()) {
            case TAB_ADDED:
                requestController.addNewTabToUI(data);
                break;

            case TAB_REMOVED:
                requestController.removeTabFromUI(data.getId());
                break;

            case TAB_MOVED:
                // TODO: handle tab moved event
                break;

            case TAB_UPDATED:
                // TODO: handle tab updated event
                break;

            case CHART_ADDED:                
                // TODO probably do nothing here since datamanager will trigger onDataReadyForChart event
                break;

            case CHART_REMOVED:
                // TODO: handle chart removed event
                break;

            case CHART_MOVED:
                // TODO: handle chart moved event
                break;

            case CHART_UPDATED:
                // TODO: handle chart updated event
                break;

            default:
                System.out.println("Unknown session change type: " + data.getType().toString());
                break;
        }
    }
}