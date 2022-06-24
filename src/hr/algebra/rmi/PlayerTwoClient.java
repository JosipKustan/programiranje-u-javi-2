/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.rmi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jkustan
 */
public class PlayerTwoClient implements PlayerTwoService {

    private static final String host = "localhost";
    
    private Registry registry;
    private PlayerTwoService stub;
    private boolean initialized = false;

    public PlayerTwoClient() {
        System.out.println("Locating registry on " + host + ":" + RMIServiceHost.RMI_PORT);
        try {
            registry = LocateRegistry.getRegistry(host, RMIServiceHost.RMI_PORT);
            stub = (PlayerTwoService) registry.lookup(PlayerTwoService.REMOTE_OBJECT_NAME);
            initialized = true;
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(PlayerTwoClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void throwSticks() throws RemoteException {
        System.out.println("Attempting to throw some sticks");
        try {
            stub.throwSticks();
        } catch (RemoteException ex) {
            Logger.getLogger(PlayerTwoClient.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("I dropped them...");
        }
    }
}
