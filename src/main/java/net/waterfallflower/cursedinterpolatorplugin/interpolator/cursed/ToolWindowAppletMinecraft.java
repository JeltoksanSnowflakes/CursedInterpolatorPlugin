package net.waterfallflower.cursedinterpolatorplugin.interpolator.cursed;

import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

@Deprecated
@SuppressWarnings({"deprecation", "all"})
public class ToolWindowAppletMinecraft implements ToolWindowFactory, DumbAware {

    private static UnknownUnexpectedAppletMinecraft GUI;

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        if(GUI == null)
            GUI = new UnknownUnexpectedAppletMinecraft();
        toolWindow.getContentManager().addContent(ContentFactory.SERVICE.getInstance().createContent((JComponent) GUI.getContentPane(), "", false));
    }
}
