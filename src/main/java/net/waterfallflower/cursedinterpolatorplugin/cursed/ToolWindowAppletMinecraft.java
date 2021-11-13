package net.waterfallflower.cursedinterpolatorplugin.cursed;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class ToolWindowAppletMinecraft implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        UnknownUnexpectedAppletMinecraft GUI = new UnknownUnexpectedAppletMinecraft();
        Content content = ContentFactory.SERVICE.getInstance().createContent((JComponent) GUI.getContentPane(), "", false);
        toolWindow.getContentManager().addContent(content);
    }
}
