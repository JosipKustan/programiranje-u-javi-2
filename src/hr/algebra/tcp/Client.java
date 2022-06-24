/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.tcp;

import hr.algebra.SaveGame;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jkustan
 */
public class Client {

    public SaveGame synchronizeGameState(String clickedId) {
        try ( Socket clientSocket = new Socket("localhost", Server.PORT)) {
            System.err.println("Client connecting onto: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
            return tcpStream(clientSocket, clickedId);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private SaveGame tcpStream(Socket clientSocket, String clickedId) {
        try (
                 ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());  ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream())) {
            oos.writeObject(clickedId);
            SaveGame save = (SaveGame) ois.readObject();
            return save;
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
