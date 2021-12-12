package net.waterfallflower.cursedinterpolatorplugin;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

@State(
        name = "net.waterfallflower.cursedinterpolatorplugin.CursedInterpolatorSettingsStorage",
        storages = @Storage("cursed_interpolator.xml")
)
public class CursedInterpolatorSettingsStorage implements PersistentStateComponent<CursedInterpolatorSettingsStorage> {

    /*
     * true -> Custom .tiny file.
     * false -> Github commit.
     */
    public boolean USE_TINY_OR_GITHUB = false;
    public String MCP_LOCATION = "";
    public String TINY_FILE_LOCATION = "";
    public String MAPPINGS_INFO = "";
    public String MAPPINGS_COMMIT = "";

    public String GUI_SIDE = "CLIENT";
    public int CLASS_SORT;
    public int METHOD_SORT;
    public int FIELD_SORT;

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

    public static File getMappings() {
        if(getInstance().USE_TINY_OR_GITHUB)
            return new File(getInstance().TINY_FILE_LOCATION);
        return new File(getInstance().MCP_LOCATION, "conf/interpolator/" + getInstance().MAPPINGS_INFO.replace('/', '.') + "-" + getInstance().MAPPINGS_COMMIT.substring(0, 7) + ".tiny");
    }

}
