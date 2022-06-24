/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra;

import hr.algebra.rmi.PlayerTwoClient;
import hr.algebra.rmi.RMIServiceHost;
import hr.algebra.tcp.Client;
import hr.algebra.tcp.Server;
import hr.algebra.utils.util;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author jkustan
 */
public class PlayingGridController implements Initializable {

    static int stickSum;
    public int clickCount = 0;
    public String lastMovedPlayerId = "";
    public String turn = "cat";
    int nodeMoveCounter;
    int nodeInGameCounter;

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
    private Label turnLabel;
    @FXML
    private Label youWinLabel;
    @FXML
    private ImageView dog5;

    public static PlayingGridController game;
    @FXML
    private Label serverStatus;

    private boolean unlocked = true;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        int[][] initialState = new int[2][5];
        initialState[0][0] = 0;
        initialState[0][1] = 2;
        initialState[0][2] = 4;
        initialState[0][3] = 6;
        initialState[0][4] = 8;
        initialState[1][0] = 1;
        initialState[1][1] = 3;
        initialState[1][2] = 5;
        initialState[1][3] = 7;
        initialState[1][4] = 9;
        setupBoard(initialState);
        game = this;
        setServerStatus(RMIServiceHost.isInitialized(), Server.isInitialized());
        if (!Server.isInitialized()) {
            new Thread(() -> {
                while (true) {
                    SaveGame servergamestate = new Client().synchronizeGameState(null);
                    if (unlocked) {
                        unlocked = false;
//                        if (servergamestate.lastMovedPlayerId != lastMovedPlayerId) {
//                            lastMovedPlayerId = servergamestate.lastMovedPlayerId;
//                            for (Node node : gameGrid.getChildren()) {
//                                if (!node.getId().equals(lastMovedPlayerId)) {
//                                    System.out.println("Its me");
//                                    Platform.runLater(() -> movePlayer(node));
//                                    break;
//                                }
//                            }
//                        }
                        setupBoard(servergamestate.state);
                        setSticks(servergamestate.sticks);
                        System.out.println("Client turn: " + turn + " Server turn: " + servergamestate.turn);
                        if (servergamestate.clickCount != clickCount) {
                            clickCount = servergamestate.clickCount;
                            Platform.runLater(() -> {
                                doGameLogic();
                            });
                            if (!servergamestate.turn.equals(turn)) {
                                turn = servergamestate.turn;
                                Platform.runLater(() -> {
                                    turnLabel.setText(turn.substring(0, 1).toUpperCase() + turn.substring(1));
                                    gameGrid.getChildren().forEach((node) -> {
                                        node.setEffect(null);
                                    });
                                });
                            }
                        }
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(PlayingGridController.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        unlocked = true;
                    }
                }
            }
            ).start();
        }
    }

    public void setupBoard(int[][] boardstate) {
        for (Node node : gameGrid.getChildren()) {
            if (node.getId().startsWith("dog")) {
                int dognumber = Integer.parseInt(String.valueOf(node.getId().charAt(node.getId().length() - 1))) - 1;
                placeNodeAt(node, Math.floorDiv(boardstate[0][dognumber], 10), boardstate[0][dognumber] % 10);
            }
            if (node.getId().startsWith("cat")) {
                int catnumber = Integer.parseInt(String.valueOf(node.getId().charAt(node.getId().length() - 1))) - 1;
                placeNodeAt(node, Math.floorDiv(boardstate[1][catnumber], 10), boardstate[1][catnumber] % 10);
            }
        }
    }

    public SaveGame save = new SaveGame();

    @FXML
    private void saveGame(ActionEvent event) {
        System.out.println("Saving...");
        save.state = getBoardState();
        SaveGame.save(save);
    }

    @FXML
    private void loadGame(ActionEvent event) {
        System.out.println("Loading...");
        SaveGame load = SaveGame.load();
        setupBoard(load.state);

    }

    public int[][] getBoardState() {
        int[][] returndata = new int[2][5];
        for (Node node : gameGrid.getChildren()) {
            if (node.getId().startsWith("dog")) {
                int dognumber = Integer.parseInt(String.valueOf(node.getId().charAt(node.getId().length() - 1))) - 1;
                int row = getNodeRowIndex(node);
                int column = getNodeColumnIndex(node);
                returndata[0][dognumber] = 10 * row + column;
            }
            if (node.getId().startsWith("cat")) {
                int catnumber = Integer.parseInt(String.valueOf(node.getId().charAt(node.getId().length() - 1))) - 1;
                int row = getNodeRowIndex(node);
                int column = getNodeColumnIndex(node);
                returndata[1][catnumber] = 10 * row + column;
            }
        }
        return returndata;
    }

    public boolean[] countSticks() {
        boolean[] sticks = new boolean[4];
        sticks[0] = stick1.isVisible();
        sticks[1] = stick2.isVisible();
        sticks[2] = stick3.isVisible();
        sticks[3] = stick4.isVisible();
        System.out.println("" + sticks[0] + sticks[1] + sticks[2] + sticks[3]);
        return sticks;
    }

    private void setSticks(boolean[] sticks) {
        stick1.setVisible(sticks[0]);
        stick2.setVisible(sticks[1]);
        stick3.setVisible(sticks[2]);
        stick4.setVisible(sticks[3]);
        stickSum = calculateStickSum();
    }

    private int calculateStickSum() {
        stickSum = 0;
        stickSum += stick1.isVisible() ? 0 : 1;
        stickSum += stick2.isVisible() ? 0 : 1;
        stickSum += stick3.isVisible() ? 0 : 1;
        stickSum += stick4.isVisible() ? 0 : 1;
        return stickSum;
    }

    @FXML
    private void loadDocumentation(ActionEvent event) {
        DocumentationUtils.generateDocumentation();
    }

    public static final class MyRunnable implements Runnable {

        private final ImageView štapić;

        public MyRunnable(final ImageView štapić) {
            this.štapić = štapić;
        }

        @Override
        public void run() {
            System.out.println("Thread " + štapić.getId() + " started.");
            throwStick(štapić);
            System.out.println("Thread " + štapić.getId() + " finished.");
        }
    }

    @FXML
    private void handleThrowButton(ActionEvent event) {
        if (RMIServiceHost.isInitialized()) {
            executeLocalStickThrow();
        } else {
            executeRemoteStickThrow();
        }
    }

    public void executeRemoteStickThrow() {
        try {
            new PlayerTwoClient().throwSticks();
            doGameLogic();
        } catch (RemoteException ex) {
            Logger.getLogger(PlayingGridController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setServerStatus(boolean rmi, boolean tcp) {
        String rmistatus = "RMI: " + (rmi ? "💻" : "🤡");
        String tcpstatus = "TCP: " + (tcp ? "💻" : "🤡");
        serverStatus.setText(rmistatus + " " + tcpstatus);
    }

    public void executeLocalStickThrow() {
        clickCount++;
        System.out.println("I hate life, but i managed to install this shit again");
        resetSticks();
        final ForkJoinPool pool = ForkJoinPool.commonPool();

        pool.execute(new MyRunnable(stick1));
        pool.execute(new MyRunnable(stick2));
        pool.execute(new MyRunnable(stick3));
        pool.execute(new MyRunnable(stick4));
        pool.awaitQuiescence(5, TimeUnit.SECONDS);

        save.sticks = countSticks();
        doGameLogic();
    }

    public void doGameLogic() {
        unlocked = false;
        calculateStickSum();
        if (stickSum == 0) {
            changeTurn();
            unlocked = true;
            return;
        }

        //sum alive
        throwButton.setDisable(true);
        //test if avaliable moves
        for (Node node : gameGrid.getChildren()) {
            if (turn.equals(node.getId().substring(0, 3))) {
                nodeInGameCounter++;

                int row = getNodeRowIndex(node);
                int column = getNodeColumnIndex(node);
                int nextMove = column + stickSum;

                if (nextMove < 10) {
                    tryMove(node, row, nextMove, true);
                } else {
                    if (row + 1 > 2) {
                        //pawn is out
                    } else {
                        int rowToGo = row + 1;
                        int columnToGo = nextMove - 10;
                        tryMove(node, rowToGo, columnToGo, true);
                    }
                }

            }
        }
        unlocked = true;
        if (nodeInGameCounter == 0) {
            youWin();
        }
    }

    private static int getNodeColumnIndex(Node node) {
        return (GridPane.getColumnIndex(node) == null) ? 0 : GridPane.getColumnIndex(node);
    }

    private static int getNodeRowIndex(Node node) {
        return (GridPane.getRowIndex(node) == null) ? 0 : GridPane.getRowIndex(node);
    }

    //Rules
    public static void throwStick(ImageView stick) {
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

    public void clickPiece(String clickedId) {
        System.out.println("Clicking id " + clickedId);
        Node clickedNode = null;
        for (Node node : gameGrid.getChildren()) {
            if (node.getId().equals(clickedId)) {
                clickedNode = node;
                System.out.println("Found id " + node.getId());
                break;
            }
        }
        if (clickedNode != null) {
            System.out.println(GridPane.getRowIndex(clickedNode) + ", " + GridPane.getColumnIndex(clickedNode));
            movePlayer(clickedNode);
        }

    }

    @FXML
    private void handleClicked(MouseEvent event) {
        Node clickedNode = (Node) event.getTarget();
        if (!turn.equals(clickedNode.getId().substring(0, 3))) {
            return;
        }
        if (RMIServiceHost.isInitialized() && Server.isInitialized()) {
            if (throwButton.isDisabled()) {
                clickCount++;
                lastMovedPlayerId = clickedNode.getId();
                //make a move
                System.out.println(GridPane.getRowIndex(clickedNode) + ", " + GridPane.getColumnIndex(clickedNode));
                movePlayer(clickedNode);
                /*gameGrid.getChildren().forEach(node -> {
                 System.out.println(GridPane.getRowIndex(node)+", "+GridPane.getColumnIndex(node));
             });*/
                //enable button  
            }
            System.out.println(clickedNode);
        } else {
            if (throwButton.isDisabled()) {
                new Client().synchronizeGameState(clickedNode.getId());
                lastMovedPlayerId = clickedNode.getId();
                movePlayer(clickedNode);
            }
        }
    }

    private void movePlayer(Node node) {
        int row = getNodeRowIndex(node);
        int column = getNodeColumnIndex(node);
        int nextMove = column + stickSum;

        if (nextMove < 10) {
            tryMove(node, row, nextMove, false);
        } else {
            if (row + 1 > 2) {
                //pawn is out
                deleteNode(node);
            } else {
                int rowToGo = row + 1;
                int columnToGo = nextMove - 10;
                tryMove(node, rowToGo, columnToGo, false);
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

    private void tryMove(Node node, int row, int column, boolean isRunThrough) {
        int rowCurrent = getNodeRowIndex(node);
        int columnCurrent = getNodeColumnIndex(node);
        //move, no_move and enemy
        MoveState moveState = checkIntersecting(node, row, column);
        if (stickSum != 0) {

            //if enemy - look for adjecent
            if (moveState == MoveState.ENEMY) {
                moveState = checkAdjecent(row, column);
                //on house of safty
                if (row == 1 && column == 5) {
                    moveState = MoveState.NO_MOVE;
                }
                //on house of happi
                if (row == 2 && column == 5) {
                    moveState = MoveState.NO_MOVE;
                }
                if (row == 2 && column > 6 && rowCurrent == 2 && columnCurrent == 5) {
                    moveState = MoveState.ENEMY_BACK;
                }

            }
            //if 3 enemies are infront NO MOVE
            if (checkIfEnemiesAhead(node)) {
                moveState = MoveState.NO_MOVE;
            }

            if (row == 2 && column > 5) {
                //going  farther of Happiness o
                if (moveState != MoveState.ENEMY_BACK) {
                    if (rowCurrent == 2 && columnCurrent == 5) {
                        //already on house of happyness
                        if (row == 2 && column == 6) {
                            moveState = MoveState.BACK;
                        }
                    } else {
                        moveState = MoveState.NO_MOVE;
                    }
                }

            }
            if (rowCurrent == 2 && columnCurrent == 7 && stickSum != 3) {
                moveState = MoveState.NO_MOVE;
            }
            if (rowCurrent == 2 && columnCurrent == 8 && stickSum != 2) {
                moveState = MoveState.NO_MOVE;
            }
            if (rowCurrent == 2 && columnCurrent == 9 && stickSum != 1) {
                moveState = MoveState.NO_MOVE;
            }
            System.out.println(moveState);
        } else {
            moveState = MoveState.NO_MOVE;
        }

        if (isRunThrough) {

            if (moveState == MoveState.NO_MOVE) {
                nodeMoveCounter++;
            } else {
                DropShadow dropShadowEffect = new DropShadow();
                dropShadowEffect.setRadius(15.0);
                dropShadowEffect.setWidth(30.0);
                dropShadowEffect.setHeight(30.0);
                Color paint = new Color(1.0, 0.0, 0.0876, 1.0);
                dropShadowEffect.setColor(paint);
                node.setEffect(dropShadowEffect);
            }
            return;
        }

        switch (moveState) {
            case MOVE:
                placeNodeAt(node, row, column);
                break;
            case ENEMY:
                swapNodes(node, row, column);
                break;
            case BACK:
                placeNodeBack(node);
                break;
            case ENEMY_BACK:
                placeNodeBack(findNodeAt(row, column));
                placeNodeAt(node, row, column);
                break;
            default:
                System.out.println("no moves avaliable for this node");
                break;
        }
        if (moveState != MoveState.NO_MOVE) {
            changeTurn();
        }

    }

    private void swapNodes(Node node, int row, int column) {
        int rowCurrent = getNodeRowIndex(node);
        int columnCurrent = getNodeColumnIndex(node);
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
        System.out.println("found near: " + adjecentSum);
        return adjecentSum > 1 ? MoveState.NO_MOVE : MoveState.ENEMY;
    }

    private Node findNodeAt(int row, int column) {
        for (Node nodeFound : gameGrid.getChildren()) {
            int rowFound = getNodeRowIndex(nodeFound);
            int columnFound = getNodeColumnIndex(nodeFound);
            if (row == rowFound && column == columnFound) {
                return nodeFound;
            }
        }
        return null;
    }

    private boolean checkIfEnemiesAhead(Node node) {
        int row = getNodeRowIndex(node);
        int column = getNodeColumnIndex(node);
        String nodeId = node.getId().substring(0, 2);
        int enemiesAhead = 0;
        for (int i = 0; i < 3; i++) {
            int columnToFind = column + i;
            //same row
            if (columnToFind < 10 && columnToFind >= 0) {
                Node nodeFound = findNodeAt(row, columnToFind);
                if (nodeFound != null) {
                    String nodeFoundId = nodeFound.getId().substring(0, 2);
                    if (!nodeId.equals(nodeFoundId)) {
                        enemiesAhead++;
                    }
                }
            } //row is different
            else {
                if (columnToFind > 9) {
                    int newColumn = column + i - 10;
                    Node nodeFound = findNodeAt(row + 1, newColumn);
                    if (nodeFound != null) {
                        String nodeFoundId = nodeFound.getId().substring(0, 2);
                        if (!nodeId.equals(nodeFoundId)) {
                            enemiesAhead++;
                        }
                    }
                } else {
                    int newColumn = column + i + 10;
                    Node nodeFound = findNodeAt(row - 1, newColumn);
                    if (nodeFound != null) {
                        String nodeFoundId = nodeFound.getId().substring(0, 2);
                        if (!nodeId.equals(nodeFoundId)) {
                            enemiesAhead++;
                        }
                    }
                }

            }

        }
        return enemiesAhead > 2;
    }

    private void placeNodeBack(Node node) {
        for (int i = 0; i < 14; i++) {
            int column = 5 - i;
            int row = 1;
            if (column < 9) {
                if (column < 0) {
                    column = column + 10;
                    row--;
                }
                if (findNodeAt(row, column) == null) {
                    placeNodeAt(node, row, column);
                    return;
                }
            }

        }
    }

    private void changeTurn() {
        turn = turn.equals("cat") ? "dog" : "cat";
        turnLabel.setText(turn.substring(0, 1).toUpperCase() + turn.substring(1));
        for (Node node : gameGrid.getChildren()) {
            node.setEffect(null);
        }
    }

    private void youWin() {
        youWinLabel.setVisible(true);
    }
}
