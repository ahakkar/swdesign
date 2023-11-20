package fi.nordicwatt.controller;

import java.io.IOException;

import fi.nordicwatt.App;
import fi.nordicwatt.Constants;
import fi.nordicwatt.controller.factory.ChartFactory;
import fi.nordicwatt.model.data.ChartRequest;
import fi.nordicwatt.model.datamodel.RequestBundle;
import fi.nordicwatt.model.datamodel.ResponseBundle;
import fi.nordicwatt.model.service.DataManager;
import fi.nordicwatt.model.service.DataManagerListener;
import fi.nordicwatt.model.session.SessionChangeData;
import fi.nordicwatt.types.Scenes;
import fi.nordicwatt.utils.ApiSettings;
import fi.nordicwatt.utils.CustomAlerts;
import fi.nordicwatt.utils.Logger;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.Chart;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Loads views and controllers. Loads mainworkspace.fxml first. Handles the application-level logic,
 * like forwarding requests when other classes ask "what should we do with this event?".
 * 
 * Listens (=subscribes to) to DataManager, RequestController and SessionController events.
 * 
 * @author Antti Hakkarainen
 */
public class PrimaryController
        implements DataManagerListener, SessionControllerListener, RequestControllerListener {
    private static PrimaryController instance;
    private RequestController requestController;
    private final DataManager dataManager = DataManager.getInstance();
    private final SessionController sessionController = SessionController.getInstance();
    private final ChartFactory chartFactory = ChartFactory.getInstance();
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
     */
    public void init(Stage stage) throws IOException {
        if (this.stage == null) {
            this.stage = stage;
        }

        LoadScene(Scenes.MainWorkspace.toString(), stage, 1400, 1000);
        dataManager.registerListener(this);
        sessionController.registerListener(this);
        sessionController.loadSession();

        // Load api keys, and check if they exist (can't check for validity)
        ApiSettings.getInstance();
        ApiSettings.load();
        if (!ApiSettings.areApiKeysEntered()) {
            displayApiSettingsWindow();
        }
    }

    /**
     * Displays a window for changing API keys
     * 
     * @throws IOException
     */
    public void displayApiSettingsWindow() throws IOException {
        Stage apiSettingsStage = new Stage();
        apiSettingsStage.setTitle("NordicWatt - Api options");
        apiSettingsStage.getIcons().add(new Image("file:doc/logo_small.png"));

        LoadScene(Scenes.ApiOptions.toString(), apiSettingsStage,
                Constants.APISETTINGS_WINDOW_WIDTH, Constants.APISETTINGS_WINDOW_HEIGHT);
    }


    /**
     * Creates a new window for the application
     * 
     * @param sceneName name of the scene to be loaded, without .fxml extension
     * @param stageToUse the stage where the scene will be loaded to.
     * @param width of the window.
     * @param height of the window.
     * @return The scene loaded.
     * @throws IOException if the scene file is not found
     */
    @Override
    public Scene LoadScene(String sceneName, Stage stageToUse, int width, int height)
            throws IOException {
        Scene sceneToOpen = new Scene(loadFXML(sceneName), width, height);
        stageToUse.setScene(sceneToOpen);
        stageToUse.show();
        return sceneToOpen;
    }


    /**
     * Loads a new view and controller and registers the controller to requestController variable.
     * 
     * @param sceneName name of the fxml file to be loaded, without .fxml extension
     * @return Parent object of the loaded scene
     * @throws IOException if the scene file is not found
     */
    private Parent loadFXML(String sceneName) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(sceneName + ".fxml"));
            Parent parent = fxmlLoader.load();
            if (sceneName.equals(Scenes.MainWorkspace.toString())) {
                this.requestController = fxmlLoader.getController();
                requestController.addListener(instance);
            }

            return parent;
        } catch (IOException e) {
            System.err.println("PrimaryController: Failed to load scene: " + sceneName);
            throw e;
        }
    }

    /**
     * Is called when datamanager has the data on hand and ready to be used. PrimaryController must
     * be registered as datamanager's observer beforehand.
     * 
     * @param data data from the DataManager used to create charts
     *
     */
    @Override
    public void dataRequestSuccess(ResponseBundle data) {
        Logger.log("dataRequestSuccess: " + data.toString());

        ChartRequest chartRequest = sessionController.getPendingChartRequest();

        if (chartRequest == null) {
            Logger.log("PrimaryController: chartRequest is null, can't generate chart");
            return;
        }

        Chart chart = chartFactory.generateChart(chartRequest, data);
        chart.setId(chartRequest.getChartId());
        String tabId = sessionController.getPendingTabId();

        // execute the chart adding to UI in JavaFX application thread
        Platform.runLater(() -> {
            sessionController.pendingChartRequestSuccess(true);
            requestController.addChartToTab(tabId, chart);
            requestController.restoreGenerateButtons();
        });
    }


    /**
     * In case of failure, notify sessionController about failure. It will discard pending request
     * and tab. Restore generate button functionality, display an error to user about the cause of
     * the failure. *
     */
    @Override
    public void dataRequestFailure(RequestBundle requests, Exception e) {
        Platform.runLater(() -> {
            sessionController.pendingChartRequestSuccess(false);
            requestController.restoreGenerateButtons();
            CustomAlerts.displayAlert(AlertType.ERROR, "Exception from DataManager",
                    "Can't create a chart because of an exception.\nResponse error message below:",
                    e.getMessage());
            Logger.log("dataRequestFailure: " + e.toString());
        });

    }


    /**
     * Is called when session's state is changed. For example a new tab was created, or a new tab
     * was created, removed, changed etc. This is used to reflect the internal session's state to
     * the user interface.
     * 
     * @param data data about the session change
     * @throws IllegalArgumentException if the session change type is unknown
     */
    @Override
    public void onSessionChange(SessionChangeData data) throws IllegalArgumentException {
        switch (data.getType()) {
            case TAB_ADDED:
                Logger.log("[PrymaryController] Tab added to session, tab: " + data.getTabId());
                requestController.addNewTabToUI(data);
                break;

            case TAB_REMOVED:
                Logger.log("[PrymaryController] Tab removed from session, tab: " + data.getTabId());
                requestController.removeTabFromUI(data.getTabId());
                break;

            case TAB_MOVED:
                // Not implemented
                break;

            case TAB_UPDATED:
                // Not implemented
                break;

            case CHART_ADDED:
                // requestController.displayProgressIndicator(data);
                break;

            case CHART_REMOVED:
                Logger.log("[PrymaryController] Chart removed from session, tab: " + data.getTabId()
                        + " chart: " + data.getChartId());
                requestController.removeChartFromUI(data);
                break;

            case CHART_MOVED:
                // Not implemented
                break;

            case CHART_UPDATED:
                // Not implemented
                break;

            default:
                Logger.log("[PrymaryController] Unknown session change type: "
                        + data.getType().toString());
                throw new IllegalArgumentException(
                        "[PrimaryController]: Unknown session change type: "
                                + data.getType().toString());
        }
    }

}
