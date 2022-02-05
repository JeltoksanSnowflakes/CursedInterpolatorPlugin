package net.waterfallflower.cursedinterpolatorplugin.interpolator;

import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.ContentFactory;
import net.glasslauncher.cursedinterpolator.objects.GithubCommit;
import net.waterfallflower.cursedinterpolatorplugin.interpolator.table.MappingsViewerToolWindow;
import net.waterfallflower.cursedinterpolatorplugin.interpolator.table.listener.RefreshActionListener;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class CursedInterpolatorWindowFactory implements ToolWindowFactory, DumbAware {

    public static List<GithubCommit> CURRENT_COMMIT_LIST = new ArrayList<>();

    public static MappingsViewerToolWindow GUI;

    public static void triggerCheck(@NotNull Project project, boolean autoLoad) {
        if(GUI == null)
            ToolWindowManager.getInstance(project).getToolWindow("CursedInterpolator.Main").show();

        if(autoLoad && !CursedInterpolatorWindowFactory.GUI.CAN_USE)
            new RefreshActionListener(CursedInterpolatorWindowFactory.GUI).actionPerformed(null);
    }

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        GUI = new MappingsViewerToolWindow(toolWindow, project);
        toolWindow.getContentManager().addContent(ContentFactory.SERVICE.getInstance().createContent((JComponent) GUI.getContentPane(), "", false));
    }
}
