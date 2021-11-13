package net.waterfallflower.cursedinterpolatorplugin.settings;


import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.ui.JBColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CursedInterpolatorSettingsForm {
    private JPanel mainPanel;
    private JLabel MCP_LOC_LABEL;
    private TextFieldWithBrowseButton MCP_LOC_BOX;
    private JLabel MAPPINGS_LABEL;
    private JPanel mainSettingsLabel;
    private JPanel mainInfo;
    private JLabel NOTE_LABEL;
    private JRadioButton USE_TINY_FILE;
    private JRadioButton USE_GITHUB_COMMIT;
    private JPanel SUB_TINY_FILE;
    private JPanel SUB_GITHUB_COMMIT;
    private JLabel TINY_FILE_LABEL;
    private TextFieldWithBrowseButton TINY_FILE_FIELD;
    private JLabel MCP_LOC_L2;
    private JLabel GITHUB_COMMIT_LABEL;
    private JTextField GITHUB_COMMIT_FIELD;
    private JPanel MCP_GENERAL;


    public JPanel getMainPanel() {
        return mainPanel;
    }

    public String getBoxString() {
        return MCP_LOC_BOX.getText();
    }

    public void setBoxString(String s) {
        MCP_LOC_BOX.setText(s);
    }

    public boolean useTinyOrGithub() {
        return USE_TINY_FILE.isSelected();
    }

    public void setTinyOrGithub(boolean b) {
        if(b)
            USE_TINY_FILE.setSelected(true);
        else
            USE_GITHUB_COMMIT.setSelected(true);
    }

    public String getTinyFileLocation() {
        return TINY_FILE_FIELD.getText();
    }

    public void setTinyFileLocation(String s) {
        TINY_FILE_FIELD.setText(s);
    }

    public String getGithubCommitLocation() {
        return GITHUB_COMMIT_FIELD.getText();
    }

    public void setGithubCommitLocation(String s) {
        GITHUB_COMMIT_FIELD.setText(s);
    }



    private void checkStatus1() {
        for(Component q : SUB_TINY_FILE.getComponents())
            if(!q.getName().startsWith("USE_"))
                q.setEnabled(USE_TINY_FILE.isSelected());

        for(Component q : SUB_GITHUB_COMMIT.getComponents())
            if(!q.getName().startsWith("USE_"))
                q.setEnabled(USE_GITHUB_COMMIT.isSelected());
    }

    public CursedInterpolatorSettingsForm() {
        MCP_LOC_BOX.addBrowseFolderListener("Select MCP Folder", "", null, FileChooserDescriptorFactory.createSingleFolderDescriptor());
        TINY_FILE_FIELD.addBrowseFolderListener("Select Tiny Mappings File", "", null, FileChooserDescriptorFactory.createSingleFileNoJarsDescriptor());

        NOTE_LABEL.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String message = "Search is global and returns a set of classes that contain a match for the input. \n" +
                        "Search is case sensitive!\n\nData elements searched on:\n" +
                        "Classes:\n    ~ Pkg Name\n    ~ SRG Name\n    ~ Obf Name\n" +
                        "Methods/Fields:\n    ~ SRG Name\n    ~ Obf Name\n    ~ MCP Name\n    ~ Comment";
                JOptionPane.showMessageDialog(null, message, "Search Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        NOTE_LABEL.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        NOTE_LABEL.setForeground(JBColor.BLUE);

        USE_TINY_FILE.addItemListener(itemEvent -> checkStatus1());
        USE_GITHUB_COMMIT.addItemListener(itemEvent -> checkStatus1());
    }
}
