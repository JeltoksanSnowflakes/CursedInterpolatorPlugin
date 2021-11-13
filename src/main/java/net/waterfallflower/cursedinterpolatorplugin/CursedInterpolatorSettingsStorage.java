package net.waterfallflower.cursedinterpolatorplugin;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
        name = "net.waterfallflower.cursedinterpolatorplugin.CursedInterpolatorSettingsStorage",
        storages = @Storage("cursed_interpolator.xml")
)
public class CursedInterpolatorSettingsStorage implements PersistentStateComponent<CursedInterpolatorSettingsStorage> {

    public String MCP_LOCATION = "";

    /**
     * true -> Custom .tiny file.
     * false -> Github commit.
     */
    public boolean USE_TINY_OR_GITHUB = false;

    public String TINY_FILE_LOCATION = "";
    public String MAPPINGS_INFO = "";

    @Override
    public @Nullable CursedInterpolatorSettingsStorage getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull CursedInterpolatorSettingsStorage state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public static CursedInterpolatorSettingsStorage getInstance() {
        return ApplicationManager.getApplication().getService(CursedInterpolatorSettingsStorage.class);
    }
}
