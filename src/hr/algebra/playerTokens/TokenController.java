/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.playerTokens;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author jkustan
 */
public class TokenController implements Initializable {
    public static EventHandler<MouseEvent> playerEventHandler(){
            return event -> {
                Node node = (Node) event.getTarget();
                int row = GridPane.getRowIndex(node);
                int column = GridPane.getColumnIndex(node);
                System.out.println(node);
            };
        }
    /**
     *
     * @param gameGrid
     * @throws FileNotFoundException
     */
    public static void initPlayers(GridPane gameGrid) throws FileNotFoundException {
        
    }
    
    
  

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Init players");
    }    

    
    
}
