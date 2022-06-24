/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra;

import hr.algebra.utils.DocumentationUtils;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

/**
 *
 * @author jkustan
 */
public class SAVEGAME implements Serializable {

    private static final String filename = "save.game";
    public int[][] state = new int[2][5];
    public boolean[] sticks = new boolean[4];

    public SAVEGAME() {
    }
    
    public SAVEGAME(int[][] state, boolean[] sticks) {
        this.state = state;
        this.sticks = sticks;
    }
    
    
//    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
//        s.defaultReadObject();
//    }
//
//    private void writeObject(ObjectOutputStream s) throws IOException {
//        s.defaultWriteObject();
//    }
    public static void save(SAVEGAME savegame) {
        try {
            createSaveFile();
            try ( FileOutputStream fos = new FileOutputStream(filename);  XMLEncoder encoder = new XMLEncoder(fos)) {
                encoder.writeObject(savegame);
            }
        } catch (IOException ex) {
            System.err.println("Cannot access savegame file...");
            createSaveFile();
        }
    }

    private static void createSaveFile() {
        try {
            DocumentationUtils.generateDocumentation();
            File settingsFile = new File(filename);
            settingsFile.createNewFile();
        } catch (IOException ex) {
            System.err.println("Error while creating savegame file");
        }
    }

    public static SAVEGAME load() {
        SAVEGAME decodedSettings = null;
        try ( FileInputStream fis = new FileInputStream(filename);  XMLDecoder decoder = new XMLDecoder(fis)) {
            decodedSettings = (SAVEGAME) decoder.readObject();
        } catch (IOException ex) {
            System.err.println("Cannot access savegame file...");
            createSaveFile();
        }
        return decodedSettings;
    }

}
