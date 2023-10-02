package org.example.controller;

import java.io.IOException;

import org.example.App;
import org.example.controller.factory.ChartFactory;
import org.example.model.data.ApiDataRequest;
import org.example.model.data.ApiDataResult;
import org.example.types.Scenes;
import org.example.utils.EnvironmentVariables;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

/**
 * Loads views and controllers. Starts from mainworkspace
 * 
 * @author Antti Hakkarainen
 */
public class PrimaryController {
    private static PrimaryController instance;
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
        registerAsObserver();
    }

       
    private void LoadScene(String sceneName) throws IOException {
        scene = new Scene(loadFXML(sceneName), 1400, 1000);
        stage.setScene(scene);
        stage.show();
    }


    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }


    /**
     * Register primarycontroller as datamanager's observer, so datamanager
     * can notify listeners when the data is ready
     */
    private void registerAsObserver() {
        // TODO create new datamanager instance?
        // TODO register itself as an observer in datamanager
    }


    /**
     * Is called when datamanager has the data on hand and ready to be used.
     * PrimaryController must be registered as datamanager's observer beforehand.
     */
    public void onDataReady() {
        // Fetch the data and update the view
        // createChart();
    }


    /**
     * Get chart data from datamanager, use ChartFactory to generate chart,
     * command RequestController to display it
     */
    private void createChart() {
        // TODO get chart data from datamanager
        //ApiDataResult chartData = primaryService.getApiData();

        // TODO use chartfactory to generate a new chart based on the data
        //ChartFactory cf = new ChartFactory(); 
        //XYChart<?, ?> chart = cf.generateChart(chartData);

        // TODO update view with chart
            
    };
}
