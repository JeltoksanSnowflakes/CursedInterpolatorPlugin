package net.waterfallflower.cursedinterpolatorplugin.cursed;

import javax.swing.*;
import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

public class UnknownUnexpectedAppletMinecraft extends JApplet {

    static {
        addToClasspath(new File("C:\\Users\\Public\\AppData\\Roaming\\rubeta\\bin\\minecraft.jar"),
                new File("F:\\WORKSPACES\\1.7.3-LTS-master\\jars\\bin\\lwjgl_util.jar"),
                new File("F:\\WORKSPACES\\1.7.3-LTS-master\\jars\\bin\\lwjgl.jar"),
                new File("F:\\WORKSPACES\\1.7.3-LTS-master\\jars\\bin\\jinput.jar"));
        addLibraryPath("F:\\WORKSPACES\\1.7.3-LTS-master\\jars\\bin\\natives");
    }

    public static URLClassLoader CLASSLOADER_;
    public static URL[] toURL(File... files) throws MalformedURLException {
        URL[] send = new URL[files.length];
        for(int i = 0; i < files.length; i++) {
            send[i] = files[i].toURI().toURL();
        }
        return send;
    }

    public static void addToClasspath(File... file) {
        try {
            CLASSLOADER_ = new URLClassLoader(toURL(file), UnknownUnexpectedAppletMinecraft.class.getClassLoader());
        } catch (Exception ex) {
            throw new RuntimeException("Cannot load library from jar file '" + file.toString() + "'. Reason: " + ex.getMessage());
        }
    }

    public static void addLibraryPath(String pathToAdd) {
        try {
            final Field usrPathsField = ClassLoader.class.getDeclaredField("usr_paths");
            usrPathsField.setAccessible(true);

            //get array of paths
            final String[] paths = (String[])usrPathsField.get(null);

            //check if the path to add is already present
            for(String path : paths) {
                if(path.equals(pathToAdd)) {
                    return;
                }
            }

            //add the new path
            final String[] newPaths = Arrays.copyOf(paths, paths.length + 1);
            newPaths[newPaths.length-1] = pathToAdd;
            usrPathsField.set(null, newPaths);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    private Applet getAppletInstance() {
        try {
            Class<?> c = Class.forName("net.minecraft.client.MinecraftApplet", true, CLASSLOADER_);
            for(int i = 0; i < c.getDeclaredConstructors().length; i++) {
                System.out.println(c.getDeclaredConstructors()[i].toGenericString());
            }
            Applet a = (Applet) c.getDeclaredConstructor().newInstance();
            a.setStub(new AppletStub() {
                @Override
                public boolean isActive() {
                    return true;
                }

                @Override
                public URL getDocumentBase() {
                    try {
                        return new File("F:\\WORKSPACES\\1.7.3-LTS-master\\jars\\bin\\wtfhtml.html").toURI().toURL();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                @Override
                public URL getCodeBase() {
                    return UnknownUnexpectedAppletMinecraft.this.getCodeBase();
                }

                @Override
                public String getParameter(String s) {
                    if(s.equals("fullscreen"))
                        return "false";

                    if(s.equals("username"))
                        return "Player228";

                    if(s.equals("sessionid"))
                        return "";

                    try{
                        return UnknownUnexpectedAppletMinecraft.this.getParameter(s);
                    }catch (NullPointerException e) {
                        return null;
                    }
                }

                @Override
                public AppletContext getAppletContext() {
                    return UnknownUnexpectedAppletMinecraft.this.getAppletContext();
                }

                @Override
                public void appletResize(int i, int i1) {
                    UnknownUnexpectedAppletMinecraft.this.setSize(i, i1);
                }
            });
            return a;
        } catch (IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
            return null;
        }
    }

    JPanel container;
    JButton button;

    public UnknownUnexpectedAppletMinecraft() {
        super();
        this.setSize(400, 400);
        container = new JPanel();
        container.setLayout(new BorderLayout());
        button = new JButton("Load it");
        button.setVisible(true);
        button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                button.setVisible(false);
                Applet secondApplet = getAppletInstance();
                secondApplet.setSize(400, 400);

                container.add(secondApplet);

                new Thread(() -> {
                    secondApplet.init();
                    secondApplet.start();
                }).start();
            }
        });

        container.add(button);
        add(container);
    }

    @Override
    public void init() {
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
