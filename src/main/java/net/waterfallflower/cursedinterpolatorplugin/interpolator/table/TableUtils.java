package net.waterfallflower.cursedinterpolatorplugin.interpolator.table;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class TableUtils {

    public static String getPrintableStackTrace(Throwable e, Set<StackTraceElement> stopAt) {
        StringBuilder s = new StringBuilder(e.toString());
        int numPrinted = 0;
        for (StackTraceElement ste : e.getStackTrace()) {
            boolean stopHere = false;
            if (stopAt.contains(ste) && numPrinted > 0)
                stopHere = true;
            else {
                s.append("\n    at ").append(ste.toString());
                numPrinted++;
                if (ste.getClassName().startsWith("javax.swing."))
                    stopHere = true;
            }

            if (stopHere) {
                int numHidden = e.getStackTrace().length - numPrinted;
                s.append("\n    ... ").append(numHidden).append(" more");
                break;
            }
        }
        return s.toString();
    }

    public static String getStackTraceMessage(String prefix, Throwable e) {
        StringBuilder s = new StringBuilder(prefix);

        s.append("\n").append(getPrintableStackTrace(e, Collections.emptySet()));
        while (e.getCause() != null) {
            Set<StackTraceElement> stopAt = new HashSet<>(Arrays.asList(e.getStackTrace()));
            e = e.getCause();
            s.append("\nCaused by: ").append(getPrintableStackTrace(e, stopAt));
        }
        return s.toString();
    }

    public static void showHTMLDialog(Component parentComponent,
                                      Object message, String title, int messageType) {
        JLabel label = new JLabel();
        Font font = label.getFont();

        String style = "font-family:" + font.getFamily() + ";" + "font-weight:" + (font.isBold() ? "bold" : "normal") + ";" + "font-size:" + font.getSize() + "pt;";
        JEditorPane ep = new JEditorPane("text/html", "<html><body style=\"" + style + "\">" + message.toString() + "</body></html>");

        ep.addHyperlinkListener(e -> {
            if (e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED))
                try {
                    Desktop.getDesktop().browse(e.getURL().toURI());
                } catch (Throwable ignore) {
                }
        });
        ep.setEditable(false);
        ep.setBackground(label.getBackground());
        JOptionPane.showMessageDialog(parentComponent, ep, title, messageType);
    }

}
