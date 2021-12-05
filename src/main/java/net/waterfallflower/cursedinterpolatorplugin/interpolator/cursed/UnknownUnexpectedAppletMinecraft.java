package net.waterfallflower.cursedinterpolatorplugin.interpolator.cursed;

import net.waterfallflower.cursedinterpolatorplugin.CursedInterpolatorSettingsStorage;

import javax.swing.*;
import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;

@Deprecated
@SuppressWarnings({"deprecation", "all"})
public class UnknownUnexpectedAppletMinecraft extends JApplet {

    private CustomChildClasspath INSTANCE;


    private Applet getAppletInstance() {
        try {
            if(INSTANCE == null)
                INSTANCE = new CustomChildClasspath() {
                    @Override
                    protected File[] register() {
                        addLibraryPath(new File(CursedInterpolatorSettingsStorage.getInstance().MCP_LOCATION, "jars/bin/natives").getAbsolutePath());
                        return new File[] {
                                new File(CursedInterpolatorSettingsStorage.getInstance().MCP_LOCATION,"jars/bin/minecraft-b1.7.3BTA.jar"),
                                new File(CursedInterpolatorSettingsStorage.getInstance().MCP_LOCATION,"jars/bin/lwjgl_util.jar"),
                                new File(CursedInterpolatorSettingsStorage.getInstance().MCP_LOCATION,"jars/bin/lwjgl_util.jar"),
                                new File(CursedInterpolatorSettingsStorage.getInstance().MCP_LOCATION,"jars/bin/lwjgl.jar"),
                                new File(CursedInterpolatorSettingsStorage.getInstance().MCP_LOCATION,"jars/bin/jinput.jar")
                        };
                    }
                };

            Class<?> c = INSTANCE.fromName("net.minecraft.client.MinecraftApplet");
            for(int i = 0; i < c.getDeclaredConstructors().length; i++) {
                System.out.println(c.getDeclaredConstructors()[i].toGenericString());
            }
            Applet a = (Applet) c.getDeclaredConstructor(/*EMPTY*/).newInstance(/*EMPTY*/);
            a.setStub(new AppletStub() {
                @Override
                public boolean isActive() {
                    return true;
                }

                @Override
                public URL getDocumentBase() {
                    try {
                        return new File(CursedInterpolatorSettingsStorage.getInstance().MCP_LOCATION,"jars/bin/wtfhtml.html").toURI().toURL();
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
}