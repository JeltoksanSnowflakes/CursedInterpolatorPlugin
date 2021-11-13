package net.waterfallflower.cursedinterpolatorplugin;

import bspkrs.mmv.gui.MappingGui;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class CursedInterpolatorWindowFactory implements ToolWindowFactory, DumbAware {

    public MappingGui GUI;

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        GUI = new MappingGui(toolWindow, project);
        Content content = ContentFactory.SERVICE.getInstance().createContent((JComponent) GUI.frmMcpMappingViewer.getContentPane(), "", false);
        toolWindow.getContentManager().addContent(content);
    }
}
