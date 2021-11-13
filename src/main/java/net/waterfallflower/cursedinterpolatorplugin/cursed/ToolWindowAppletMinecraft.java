package net.waterfallflower.cursedinterpolatorplugin.cursed;

import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class ToolWindowAppletMinecraft implements ToolWindowFactory, DumbAware {

    private static UnknownUnexpectedAppletMinecraft GUI;
    private static Container A1;

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        if(GUI == null) {
            GUI = new UnknownUnexpectedAppletMinecraft();
            A1 = GUI.getContentPane();
        }
        Content content = ContentFactory.SERVICE.getInstance().createContent((JComponent) A1, "", false);
        toolWindow.getContentManager().addContent(content);
    }
}
