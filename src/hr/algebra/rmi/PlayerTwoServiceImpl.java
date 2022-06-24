/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.rmi;

import hr.algebra.PlayingGridController;
import java.rmi.RemoteException;
import javafx.application.Platform;

/**
 *
 * @author jkustan
 */
public class PlayerTwoServiceImpl implements PlayerTwoService {

    @Override
    public void throwSticks() throws RemoteException {
        Platform.runLater(() -> PlayingGridController.game.doGameLogic());
    }
}
