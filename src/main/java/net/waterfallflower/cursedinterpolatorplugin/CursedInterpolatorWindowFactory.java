package net.waterfallflower.cursedinterpolatorplugin;

import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import net.waterfallflower.cursedinterpolatorplugin.table.MappingsViewerToolWindow;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class CursedInterpolatorWindowFactory implements ToolWindowFactory, DumbAware {

    public MappingsViewerToolWindow GUI;

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        GUI = new MappingsViewerToolWindow(toolWindow, project);
        Content content = ContentFactory.SERVICE.getInstance().createContent((JComponent) GUI.getContentPane(), "", false);
        toolWindow.getContentManager().addContent(content);
    }
}
