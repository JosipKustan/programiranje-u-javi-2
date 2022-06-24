/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.utils;

import com.sun.jndi.fscontext.RefFSContext;
import com.sun.jndi.fscontext.RefFSContextFactory;
import hr.algebra.jndi.InitialDirContextCloseable;
import java.util.Hashtable;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

/**
 *
 * @author dnlbe
 */
public class JndiUtils {
    
    private static final String INITIAL_CONTEXT_FACTORY = RefFSContextFactory.class.getName();
    private static final String PROVIDER_URL_PREFIX = "file:";
    private static boolean found;
    
    public static void search(String root, String fileName, int limit, Consumer<Optional<String>> callback) {
        found = false;
        Thread thread = new Thread(() -> {
            try(InitialDirContextCloseable context = new InitialDirContextCloseable(configureEnvironment(root))) {
                searchForBinding(context, "", fileName, 1, limit, callback);
            } catch (NamingException ex) {
                Logger.getLogger(JndiUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        thread.setDaemon(true);
        thread.start();
    }
    
    
    private static Hashtable<?, ?> configureEnvironment(String root) {
        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
        env.put(Context.PROVIDER_URL, PROVIDER_URL_PREFIX.concat(root));
        return env;
    }

    private static void searchForBinding(Context context, String path, String fileName, int level, int limit, Consumer<Optional<String>> callback) throws NamingException {
        if (level > limit) {
            return;
        }
        NamingEnumeration<Binding> listBindings = context.listBindings(path);
        while (listBindings.hasMoreElements() && !found) {
            Binding binding = listBindings.nextElement();
            System.out.printf("%" + level + "s", " ");
            System.out.println(binding.getName());
            if (binding.getName().equals(fileName)) {
                notifyListener(Optional.of(binding.getObject().toString()), callback);
                found = true;
                return;
            }
            if (binding.getClassName().equals(RefFSContext.class.getName())) {
                searchForBinding(context, path + "/" + binding.getName(), fileName, level + 1, limit, callback);
            }
        }
        if (level == 1 && !found) {
            notifyListener(Optional.empty(), callback);
        }
    }

    private static void notifyListener(Optional<String> path, Consumer<Optional<String>> callback) {
        Platform.runLater(() -> callback.accept(path));
    }
}