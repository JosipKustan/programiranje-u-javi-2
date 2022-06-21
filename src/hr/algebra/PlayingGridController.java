/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra;

import hr.algebra.playerTokens.TokenController;
import static hr.algebra.playerTokens.TokenController.playerEventHandler;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author jkustan
 */
public class PlayingGridController implements Initializable {

    @FXML
    private ImageView boardImage;
    @FXML
    private Button throwButton;
    @FXML
    private HBox stickBox;
    @FXML
    private GridPane gameGrid;
    @FXML
    private ImageView stick1;
    @FXML
    private ImageView stick2;
    @FXML
    private ImageView stick3;
    @FXML
    private ImageView stick4;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            initGrid();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PlayingGridController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void handleButton(ActionEvent event) {
        System.out.println("I hate life, but i managed to install this shit again");
    }

    //Rules
    private void initGrid() throws FileNotFoundException {
        Image catImage = new Image("file:playerTokens/cat.png");
        Image dogImage = new Image("file:playerTokens/dog.png");
        for (int i = 0; i < 10; i++) {
            ImageView player = new ImageView();
            player.setOnMouseClicked(playerEventHandler());
            player.setFitWidth(50);
            player.setFitHeight(60);
            if (i % 2 == 0) {
                player.setId("cat" + i);
                player.setImage(catImage);
            } else {
                player.setId("dog" + i);
                player.setImage(dogImage);
            }
            gameGrid.add(player, i, 0);
        }
        Node node = gameGrid.getChildren().get(3);
        int row = GridPane.getRowIndex(node);
        int column = GridPane.getColumnIndex(node);
        System.out.println(row + " " + column);
        TokenController.initPlayers(gameGrid);

    }

    /*@FXML
    private void handleClicked(MouseEvent event) {
        Node node = (Node) event.getTarget();
        if(node!=null){
            int row = GridPane.getRowIndex(node);
            int column = GridPane.getColumnIndex(node);
        }
        System.out.println(node);
    }*/
}
