package net.waterfallflower.cursedinterpolatorplugin.interpolator.table.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.CaretModel;
import net.waterfallflower.cursedinterpolatorplugin.interpolator.CursedInterpolatorWindowFactory;
import net.waterfallflower.cursedinterpolatorplugin.interpolator.table.listener.SearchActionListener;
import org.jetbrains.annotations.NotNull;

public class ActionSearchInMappings extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        CursedInterpolatorWindowFactory.triggerCheck(e.getProject(), true);

        CaretModel caretModel = e.getRequiredData(CommonDataKeys.EDITOR).getCaretModel();
        String selectedText = caretModel.getCurrentCaret().getSelectedText();

        if(selectedText != null && selectedText.length() > 0) {
            CursedInterpolatorWindowFactory.GUI.FIELD_TABLE_SEARCH.setText(selectedText);
            new SearchActionListener(CursedInterpolatorWindowFactory.GUI).actionPerformed(null);
        }
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setEnabledAndVisible(
                e.getProject() != null &&
                e.getData(CommonDataKeys.EDITOR) != null
        );
    }
}
