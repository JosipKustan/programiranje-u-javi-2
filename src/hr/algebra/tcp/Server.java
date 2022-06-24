/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.tcp;

import hr.algebra.PlayingGridController;
import hr.algebra.SaveGame;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jkustan
 */
public class Server {

    public static final int PORT = 421;

    private static Thread serverThread = null;

    private static boolean initialized = false;

    public static boolean isInitialized() {
        return initialized;
    }

    public static void startServer() {
        if (serverThread == null) {
            serverThread = new Thread(() -> receiveMessages());
            serverThread.start();
        }
    }

    public static void stopServer() {
        serverThread.interrupt();
    }

    private static void receiveMessages() {
        try ( ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.err.println("Server listening on port: " + serverSocket.getLocalPort());
            initialized = true;
            while (!Thread.currentThread().isInterrupted()) {
                Socket clientSocket = serverSocket.accept();
                System.err.println("Client connected from port:  " + clientSocket.getPort());
                new Thread(() -> processMessage(clientSocket)).start();
            }
        } catch (BindException ex) {
            System.err.println("Unable to bind TCP Port " + PORT);
            initialized = false;
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void processMessage(Socket clientSocket) {
        try ( ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());  ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream())) {
            String clickedId = (String) ois.readObject();
            if (clickedId != null) {
                PlayingGridController.game.clickPiece(clickedId);
            }
            oos.writeObject(
                    new SaveGame(
                            PlayingGridController.game.getBoardState(), 
                            PlayingGridController.game.countSticks(), 
                            PlayingGridController.game.turn,
                            PlayingGridController.game.clickCount,
                            PlayingGridController.game.lastMovedPlayerId
                    )
            );
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
