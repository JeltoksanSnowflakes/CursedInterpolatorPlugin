package net.waterfallflower.cursedinterpolatorplugin.interpolator.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.util.NlsContexts;
import net.waterfallflower.cursedinterpolatorplugin.CursedInterpolatorSettingsStorage;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class CursedInterpolatorConfigurable implements Configurable {

    protected CursedInterpolatorSettingsForm mainLabel;


    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "Cursed Interpolator Config";
    }

    @Override
    public @Nullable JComponent createComponent() {
        mainLabel = new CursedInterpolatorSettingsForm();
        return mainLabel.getMainPanel();
    }

    @Override
    public boolean isModified() {
        CursedInterpolatorSettingsStorage settings = CursedInterpolatorSettingsStorage.getInstance();
        return !mainLabel.getBoxString().equals(settings.MCP_LOCATION) ||
                settings.USE_TINY_OR_GITHUB != mainLabel.useTinyOrGithub() ||
                !mainLabel.getTinyFileLocation().equals(settings.TINY_FILE_LOCATION) ||
                !mainLabel.getGithubRepo().equals(settings.MAPPINGS_INFO) ||
                !mainLabel.getGithubCommit().equals(settings.MAPPINGS_COMMIT);
    }

    @Override
    public void apply() {
        CursedInterpolatorSettingsStorage settings = CursedInterpolatorSettingsStorage.getInstance();
        settings.MCP_LOCATION = mainLabel.getBoxString();
        settings.USE_TINY_OR_GITHUB = mainLabel.useTinyOrGithub();
        settings.TINY_FILE_LOCATION = mainLabel.getTinyFileLocation();
        settings.MAPPINGS_INFO = mainLabel.getGithubRepo();
        settings.MAPPINGS_COMMIT = mainLabel.getGithubCommit();
    }

    @Override
    public void reset() {
        CursedInterpolatorSettingsStorage settings = CursedInterpolatorSettingsStorage.getInstance();
        mainLabel.setBoxString(settings.MCP_LOCATION);
        mainLabel.setTinyOrGithub(settings.USE_TINY_OR_GITHUB);
        mainLabel.setTinyFileLocation(settings.TINY_FILE_LOCATION);
        mainLabel.setGithubRepo(settings.MAPPINGS_INFO);
        mainLabel.setGithubCommit(settings.MAPPINGS_COMMIT);
    }
}
