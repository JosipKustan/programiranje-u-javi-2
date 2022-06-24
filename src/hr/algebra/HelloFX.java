/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra;

import hr.algebra.rmi.RMIServiceHost;
import hr.algebra.tcp.Server;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author jkustan
 */
public class HelloFX extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("PlayingGrid.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Senet - here since 2600 b.C.");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        initProcesses();
        launch(args);
    }

    private static void initProcesses() {
        RMIServiceHost.startServices();
        Server.startServer();
    }
}
