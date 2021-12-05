package net.waterfallflower.cursedinterpolatorplugin.api.ui;

import javax.swing.*;
import java.awt.*;

public class SmallButton extends JButton {

    public SmallButton() {
        super();
        setPreferredSize(new Dimension(16, 16));
        setBorderPainted(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if(getIcon() != null)
            getIcon().paintIcon(this, g, 0, 0);
    }

}

