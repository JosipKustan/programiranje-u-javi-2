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

        if (throwButton.isDisabled()) {
            //make a move
            System.out.println(GridPane.getRowIndex(clickedNode) + ", " + GridPane.getColumnIndex(clickedNode));
            movePlayer(clickedNode);
            /*gameGrid.getChildren().forEach(node -> {
                 System.out.println(GridPane.getRowIndex(node)+", "+GridPane.getColumnIndex(node));
             });*/
            //enable button  
        }
        System.out.println(clickedNode);
    }

    private void movePlayer(Node node) {
        int row = (GridPane.getRowIndex(node) == null) ? 0 : GridPane.getRowIndex(node);
        int column = (GridPane.getColumnIndex(node) == null) ? 0 : GridPane.getColumnIndex(node);
        int nextMove = column + stickSum;
        if (nextMove < 10) {
            tryMove(node, row, nextMove);
        } else {
            if (row + 1 > 2) {
                //pawn is out
                deleteNode(node);
            } else {
                int rowToGo = row + 1;
                int columnToGo = nextMove - 10;
                tryMove(node, rowToGo, columnToGo);
            }
        }
        throwButton.setDisable(false);
    }

    private void deleteNode(Node node) {
        gameGrid.getChildren().remove(node);
    }

    private void placeNodeAt(Node node, int row, int column) {
        GridPane.setRowIndex(node, row);
        GridPane.setColumnIndex(node, column);
    }

    private MoveState checkIntersecting(Node node, int row, int column) {

        Node nodeFound = findNodeAt(row, column);
        if (nodeFound != null) {
            return isSameNode(node, nodeFound);
        }
        return MoveState.MOVE;
    }

    private MoveState isSameNode(Node node, Node nodeFound) {
        String nodeId = node.getId().substring(0, 2);
        String nodeFoundId = nodeFound.getId().substring(0, 2);

        return nodeId.equals(nodeFoundId) ? MoveState.NO_MOVE : MoveState.ENEMY;
    }

    private void tryMove(Node node, int row, int column) {
        //move, no_move and enemy
        MoveState moveState = checkIntersecting(node, row, column);
        //if enemy - look for adjecent
        if (moveState == MoveState.ENEMY) {
            moveState = checkAdjecent(row, column);
        }
        //if 3 enemies are infront NO MOVE
        if(checkIfEnemiesAhead(node)){
            moveState=MoveState.NO_MOVE;
        }
        System.out.println(moveState);
        switch (moveState) {
            case MOVE:
                placeNodeAt(node, row, column);
                break;
            case ENEMY:
                swapNodes(node, row, column);
                break;
            default:
                System.out.println("no moves avaliable for this node");
                break;
        }
    }

    private void swapNodes(Node node, int row, int column) {
        int rowCurrent = (GridPane.getRowIndex(node) == null) ? 0 : GridPane.getRowIndex(node);
        int columnCurrent = (GridPane.getColumnIndex(node) == null) ? 0 : GridPane.getColumnIndex(node);
        Node nodeFound = findNodeAt(row, column);
        placeNodeAt(nodeFound, rowCurrent, columnCurrent);
        placeNodeAt(node, row, column);
    }

    private MoveState checkAdjecent(int row, int column) {
        int adjecentSum = 0;
        Node node = findNodeAt(row, column);
        String nodeId = node.getId().substring(0, 2);
        for (int i = -1; i < 2; i++) {
            int columnToFind = column + i;
            //same row
            if (columnToFind < 10 && columnToFind >= 0) {
                Node nodeFound = findNodeAt(row, columnToFind);
                if (nodeFound != null) {
                    String nodeFoundId = nodeFound.getId().substring(0, 2);
                    if (nodeId.equals(nodeFoundId)) {
                        adjecentSum++;
                    }
                }
            } //row is different
            else {
                if (columnToFind > 9) {
                    int newColumn = column + i - 10;
                    Node nodeFound = findNodeAt(row + 1, newColumn);
                    if (nodeFound != null) {
                        String nodeFoundId = nodeFound.getId().substring(0, 2);
                        if (nodeId.equals(nodeFoundId)) {
                            adjecentSum++;
                        }
                    }
                } else {
                    int newColumn = column + i + 10;
                    Node nodeFound = findNodeAt(row - 1, newColumn);
                    if (nodeFound != null) {
                        String nodeFoundId = nodeFound.getId().substring(0, 2);
                        if (nodeId.equals(nodeFoundId)) {
                            adjecentSum++;
                        }
                    }
                }

            }

        }
        System.out.println("found near: "+adjecentSum);
        return adjecentSum > 1 ? MoveState.NO_MOVE : MoveState.ENEMY;
    }

    private Node findNodeAt(int row, int column) {
        for (Node nodeFound : gameGrid.getChildren()) {
            int rowFound = (GridPane.getRowIndex(nodeFound) == null) ? 0 : GridPane.getRowIndex(nodeFound);
            int columnFound = (GridPane.getColumnIndex(nodeFound) == null) ? 0 : GridPane.getColumnIndex(nodeFound);
            if (row == rowFound && column == columnFound) {
                return nodeFound;
            }
        }
        return null;
    }

    private boolean checkIfEnemiesAhead(Node node) {
        int row = (GridPane.getRowIndex(node) == null) ? 0 : GridPane.getRowIndex(node);
        int column = (GridPane.getColumnIndex(node) == null) ? 0 : GridPane.getColumnIndex(node);
        
        
    }
}
