package org.example.Views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.example.App;
import org.example.Services.PrimaryService;

import java.io.IOException;

/**
 * TODO do we need this if view is contained in .fxml? Maybe some views are created on the fly?
 */
public class PrimaryView {

    private static Scene scene;
    public PrimaryView(Stage stage) throws IOException {
        PrimaryService.getInstance();
        scene = new Scene(loadFXML("primary"), 1400, 1000);
        stage.setScene(scene);
        stage.show();
        initializeComponentData();
    }


    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }


    private void initializeComponentData(){
        initializeLineChart();

    }


    private void initializeLineChart() {
        // Attach Line Chart to listen to services ObservableList<PrimaryModel>
        // ObservableList<PrimaryModel> list = primaryService.getSomething();
    }

}
