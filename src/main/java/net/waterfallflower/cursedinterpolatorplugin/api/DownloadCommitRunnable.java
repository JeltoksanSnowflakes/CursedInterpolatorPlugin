package net.waterfallflower.cursedinterpolatorplugin.api;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import lombok.SneakyThrows;
import net.glasslauncher.common.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.function.Consumer;

public class DownloadCommitRunnable extends Task.Backgroundable {

    private final String LOCATION;
    private final String REPO;
    private final String COMMIT;
    private final Consumer<Byte> ADDITIONAL_CONSUMER;

    public DownloadCommitRunnable(String file, String repo, String commit) {
        this(file, repo, commit, null);
    }

    public DownloadCommitRunnable(@NotNull String file, @NotNull String repo, @NotNull String commit, @Nullable Consumer<Byte> additional) {
        super(null, "Resolving mappings: " + repo + "." + commit);
        this.LOCATION = file;
        this.REPO = repo;
        this.COMMIT = commit.substring(0, 7);
        this.ADDITIONAL_CONSUMER = additional;
    }

    private boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }

    @SneakyThrows
    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        URL jarUrl = new URL("https://jitpack.io/com/github/" + REPO + "/" + COMMIT + "/" + REPO.split("/")[1] + "-" + COMMIT + ".jar");
        File tempDir = new File(LOCATION, "conf/interpolator");
        File tempFile = new File(tempDir, REPO.replace('/', '.') + "-" + COMMIT + ".jar");
        File mappingsFile = new File(tempDir, "mappings/mappings/mappings.tiny");
        File cachedFile = new File(tempDir, REPO.replace('/', '.') + "-" + COMMIT +  ".tiny");
        cachedFile.delete();

        indicator.setFraction(0.0D);

        tempDir.mkdir();
        indicator.setText2("Downloading: " + jarUrl);
        new DownloadInstance(indicator).downloadFile(jarUrl.toString(), tempDir.getAbsolutePath(), null, tempFile.getName());

        indicator.setText2("Extracting: " + tempFile.getAbsolutePath());
        FileUtils.extractZip(tempFile.getAbsolutePath(), new File(tempDir, "mappings").getAbsolutePath());
        if (!mappingsFile.exists()) {
            throw new FileNotFoundException("mappings.tiny not found inside jar! Make sure the target jar is a valid jar before trying again.");
        }
        mappingsFile.renameTo(cachedFile);

        tempFile.delete();
        deleteDirectory((new File(tempDir, "mappings")));

        if(ADDITIONAL_CONSUMER != null)
            ADDITIONAL_CONSUMER.accept((byte) 0);
    }
}
