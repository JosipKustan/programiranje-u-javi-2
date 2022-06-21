/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra;

import hr.algebra.utils.util;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author jkustan
 */
public class PlayingGridController implements Initializable {
    
    
    int stickSum;
    

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
    @FXML
    private HBox stickBox1;
    @FXML
    private ImageView stick1_white;
    @FXML
    private ImageView stick2_white;
    @FXML
    private ImageView stick3_white;
    @FXML
    private ImageView stick4_white;
    @FXML
    private ImageView dog1;
    @FXML
    private ImageView cat1;
    @FXML
    private ImageView cat2;
    @FXML
    private ImageView cat3;
    @FXML
    private ImageView cat4;
    @FXML
    private ImageView cat5;
    @FXML
    private ImageView dog2;
    @FXML
    private ImageView dog3;
    @FXML
    private ImageView dog4;
    @FXML
    private ImageView dog5;
    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

    @FXML
    private void handleButton(ActionEvent event) {
        System.out.println("I hate life, but i managed to install this shit again");
        resetSticks();
        throwStick(stick1);
        throwStick(stick2);
        throwStick(stick3);
        throwStick(stick4);
        //random true or false
       //set sticks
        //sum of random 
       //moveCount
       throwButton.setDisable(true);
        System.out.println(stickSum);
    }

    //Rules
   
    private void throwStick(ImageView stick) {
        if (util.getRandomBoolean()) {
            stick.setVisible(false);
            stickSum++;
        }
    }

    private void resetSticks() {
        stickSum = 0;
        stick1.setVisible(true);
        stick2.setVisible(true);
        stick3.setVisible(true);
        stick4.setVisible(true);
    }

    @FXML
    private void handleClicked(MouseEvent event) {
         Node clickedNode = (Node) event.getTarget();
        
        if(throwButton.isDisabled()){
             //make a move
              System.out.println(GridPane.getRowIndex(clickedNode)+", "+GridPane.getColumnIndex(clickedNode));
         GridPane.setColumnIndex(clickedNode, GridPane.getColumnIndex(clickedNode)+stickSum);
             /*gameGrid.getChildren().forEach(node -> {
                 System.out.println(GridPane.getRowIndex(node)+", "+GridPane.getColumnIndex(node));
             });*/
             //enable button  
        }
        System.out.println(clickedNode);
    }
}
