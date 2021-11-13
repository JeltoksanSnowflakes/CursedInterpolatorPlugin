package net.glasslauncher.cursedinterpolator.gui;

import com.intellij.openapi.ui.ComboBox;
import net.glasslauncher.common.FileUtils;
import net.glasslauncher.cursedinterpolator.objects.GithubCommit;

import javax.swing.*;
import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

public class VersionDownloadPanel extends JDialog {

    public VersionDownloadPanel(String basePath) {

        setModal(true);
        setTitle("Download Mappings");
        JPanel content = new JPanel();
        setPreferredSize(new Dimension(400, 200));
        setMinimumSize(new Dimension(400, 200));

        JComboBox<String> repoBox = new ComboBox<>();
        repoBox.addItem("calmilamsy/BIN-Mappings");
        repoBox.addItem("minecraft-cursed-legacy/Plasma");
        repoBox.setPreferredSize(new Dimension(200, repoBox.getHeight()));

        JComboBox<String> commitBox = new ComboBox<>();
        commitBox.setPreferredSize(new Dimension(200, repoBox.getHeight()));

        repoBox.addActionListener((event) -> updateCommits(commitBox, repoBox));

        JButton remapButton = new JButton("Download");
        remapButton.setPreferredSize(new Dimension(200, repoBox.getHeight()));
        remapButton.addActionListener((event) -> {
            try {
                downloadMappings(basePath, (String) repoBox.getSelectedItem(), (String) commitBox.getSelectedItem());
                JOptionPane.showMessageDialog(this, "Downloaded!", "Info", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        updateCommits(commitBox, repoBox);

        content.add(repoBox);
        content.add(commitBox);
        content.add(remapButton);
        add(content);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void updateCommits(JComboBox<String> commitBox, JComboBox<String> repoBox) {
        try {
            System.out.println("Getting latest 25 commits for \"" + repoBox.getSelectedItem() + "\"");
            commitBox.removeAllItems();
            for (GithubCommit commit : GithubCommit.getCommits((String) repoBox.getSelectedItem())) {
                commitBox.addItem(commit.sha.substring(0, 7));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static File downloadMappings(String basePath, String repo, String commit) throws IOException {
        URL jarUrl = new URL("https://jitpack.io/com/github/" + repo + "/" + commit + "/" + repo.split("/")[1] + "-" + commit + ".jar");
        File tempDir = new File(basePath, "conf/interpolator");
        File tempFile = new File(tempDir, "mappings.jar");
        File mappingsFile = new File(tempDir, "mappings/mappings/mappings.tiny");
        File cachedFile = new File(tempDir, "mappings.tiny");
        cachedFile.delete();
        System.out.println("Selected repo: \"" + repo + "\"");
        System.out.println("Selected commit: \"" + commit + "\"");
        System.out.println("Downloading \"" + jarUrl + "\"");
        System.out.println("Target path: " + basePath);
        tempDir.mkdir();
        FileUtils.downloadFile(jarUrl.toString(), tempDir.getAbsolutePath(), null, tempFile.getName());
        FileUtils.extractZip(tempFile.getAbsolutePath(), new File(tempDir, "mappings").getAbsolutePath());
        if (!mappingsFile.exists()) {
            throw new FileNotFoundException("mappings.tiny not found inside jar! Make sure the target jar is a valid jar before trying again.");
        }
        mappingsFile.renameTo(cachedFile);

        tempFile.delete();
        deleteDirectory((new File(tempDir, "mappings")));
        return cachedFile;
    }

    public static boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }
}
