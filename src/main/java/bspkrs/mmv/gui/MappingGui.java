/*
 * Copyright (C) 2013 bspkrs
 * Portions Copyright (C) 2013 Alex "immibis" Campbell
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package bspkrs.mmv.gui;

import bspkrs.mmv.McpMappingLoader;
import bspkrs.mmv.McpMappingLoader.CantLoadMCPMappingException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import immibis.bon.IProgressListener;
import immibis.bon.gui.Reference;
import immibis.bon.gui.Side;
import net.glasslauncher.cursedinterpolator.gui.VersionDownloadPanel;
import net.waterfallflower.cursedinterpolatorplugin.CursedInterpolatorSettingsStorage;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.ScrollPaneConstants;
import javax.swing.SortOrder;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.prefs.Preferences;

public class MappingGui extends JFrame {
    public static final String VERSION_NUMBER = MappingGui.class.getPackage().getImplementationVersion();
    private static final long serialVersionUID = 1L;
    private final static String PREFS_KEY_MCPDIR = "mcpDir";
    private final static String PREFS_KEY_SIDE = "side";
    private final static String PREFS_KEY_CLASS_SORT = "classSort";
    private final static String PREFS_KEY_METHOD_SORT = "methodSort";
    private final static String PREFS_KEY_FIELD_SORT = "fieldSort";
    private final Preferences prefs = Preferences.userNodeForPackage(MappingGui.class);
    private final String mcfTopic = "http://www.minecraftforum.net/topic/2115030-";
    public JFrame frmMcpMappingViewer;
    public JButton btnRefreshTables;
    private JComboBox<Side> cmbSide;

    private JCheckBox chkForceRefresh;
    private JPanel pnlProgress;
    private JProgressBar progressBar;
    private JPanel pnlFilter;
    private JTextField edtFilter;
    private JButton btnSearch;
    private List<RowSorter.SortKey> classSort = new ArrayList<>();
    private List<RowSorter.SortKey> methodSort = new ArrayList<>();
    private List<RowSorter.SortKey> fieldSort = new ArrayList<>();
    private JTable tblClasses;
    private JTable tblMethods;
    private JTable tblFields;
    private Thread curTask = null;
    private Map<String, McpMappingLoader> mcpInstances = new HashMap<>();
    private McpMappingLoader currentLoader;
    private DefaultTableModel classesDefaultModel = new DefaultTableModel(
            new Object[][]{
                    {},
            },
            new String[]{
                    "Pkg name", "SRG name", "Obf name", "Client Only"
            }
    ) {
        private static final long serialVersionUID = 1L;
        boolean[] columnEditables = new boolean[]{
                false, false, false, false
        };

        Class[] columnTypes = new Class[]{
                String.class, String.class, String.class, Boolean.class
        };

        @Override
        public Class getColumnClass(int columnIndex) {
            return columnTypes[columnIndex];
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return columnEditables[column];
        }
    };
    private DefaultTableModel methodsDefaultModel = new DefaultTableModel(
            new Object[][]{
                    {},
            },
            new String[]{
                    "MCP Name", "SRG Name", "Obf Name", "SRG Descriptor", "Comment", "Client Only"
            }
    ) {
        private static final long serialVersionUID = 1L;
        boolean[] columnEditables = new boolean[]{
                false, false, false, false, false, false
        };

        Class[] columnTypes = new Class[]{
                String.class, String.class, String.class, String.class, String.class, Boolean.class
        };

        @Override
        public Class getColumnClass(int columnIndex) {
            return columnTypes[columnIndex];
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return columnEditables[column];
        }
    };
    private DefaultTableModel fieldsDefaultModel = new DefaultTableModel(
            new Object[][]{
                    {},
            },
            new String[]{
                    "MCP Name", "SRG Name", "Obf Name", "Comment", "Client Only"
            }
    ) {
        private static final long serialVersionUID = 1L;
        boolean[] columnEditables = new boolean[]{
                false, false, false, false, false
        };
        Class[] columnTypes = new Class[]{
                String.class, String.class, String.class, String.class, Boolean.class
        };

        @Override
        public Class getColumnClass(int columnIndex) {
            return columnTypes[columnIndex];
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return columnEditables[column];
        }
    };


    public Project project;

    /**
     * Create the application.
     */
    public MappingGui(ToolWindow toolWindow, Project p) {
        this.project = p;
        initialize();
    }

    private static String getPrintableStackTrace(Throwable e, Set<StackTraceElement> stopAt) {
        StringBuilder s = new StringBuilder(e.toString());
        int numPrinted = 0;
        for (StackTraceElement ste : e.getStackTrace()) {
            boolean stopHere = false;
            if (stopAt.contains(ste) && numPrinted > 0)
                stopHere = true;
            else {
                s.append("\n    at ").append(ste.toString());
                numPrinted++;
                if (ste.getClassName().startsWith("javax.swing."))
                    stopHere = true;
            }

            if (stopHere) {
                int numHidden = e.getStackTrace().length - numPrinted;
                s.append("\n    ... ").append(numHidden).append(" more");
                break;
            }
        }
        return s.toString();
    }

    private static String getStackTraceMessage(String prefix, Throwable e) {
        StringBuilder s = new StringBuilder(prefix);

        s.append("\n").append(getPrintableStackTrace(e, Collections.emptySet()));
        while (e.getCause() != null) {
            Set<StackTraceElement> stopAt = new HashSet<>(Arrays.asList(e.getStackTrace()));
            e = e.getCause();
            s.append("\nCaused by: ").append(getPrintableStackTrace(e, stopAt));
        }
        return s.toString();
    }

    public static void showHTMLDialog(Component parentComponent,
                                      Object message, String title, int messageType) {
        JLabel label = new JLabel();
        Font font = label.getFont();

        String style = "font-family:" + font.getFamily() + ";" + "font-weight:" + (font.isBold() ? "bold" : "normal") + ";" + "font-size:" + font.getSize() + "pt;";
        JEditorPane ep = new JEditorPane("text/html", "<html><body style=\"" + style + "\">" + message.toString() + "</body></html>");

        ep.addHyperlinkListener(e -> {
            if (e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED))
                try {
                    Desktop.getDesktop().browse(e.getURL().toURI());
                } catch (Throwable ignore) {
                }
        });
        ep.setEditable(false);
        ep.setBackground(label.getBackground());
        JOptionPane.showMessageDialog(parentComponent, ep, title, messageType);
    }

    private void savePrefs() {

        prefs.put(PREFS_KEY_SIDE, Objects.requireNonNull(cmbSide.getSelectedItem()).toString());

        if (tblClasses.getRowSorter().getSortKeys().size() > 0) {
            int i = tblClasses.getRowSorter().getSortKeys().get(0).getColumn() + 1;
            SortOrder order = tblClasses.getRowSorter().getSortKeys().get(0).getSortOrder();
            prefs.putInt(PREFS_KEY_CLASS_SORT, order == SortOrder.DESCENDING ? i * -1 : i);
        } else
            prefs.putInt(PREFS_KEY_CLASS_SORT, 1);

        if (tblMethods.getRowSorter().getSortKeys().size() > 0) {
            int i = tblMethods.getRowSorter().getSortKeys().get(0).getColumn() + 1;
            SortOrder order = tblMethods.getRowSorter().getSortKeys().get(0).getSortOrder();
            prefs.putInt(PREFS_KEY_METHOD_SORT, order == SortOrder.DESCENDING ? i * -1 : i);
        } else
            prefs.putInt(PREFS_KEY_METHOD_SORT, 1);

        if (tblFields.getRowSorter().getSortKeys().size() > 0) {
            int i = tblFields.getRowSorter().getSortKeys().get(0).getColumn() + 1;
            SortOrder order = tblFields.getRowSorter().getSortKeys().get(0).getSortOrder();
            prefs.putInt(PREFS_KEY_FIELD_SORT, order == SortOrder.DESCENDING ? i * -1 : i);
        } else
            prefs.putInt(PREFS_KEY_FIELD_SORT, 1);
    }

    private void loadPrefs() {
        try {
            Side side = Side.valueOf(prefs.get(PREFS_KEY_SIDE, Side.Universal.toString()));
            cmbSide.setSelectedItem(side);

            classSort.clear();
            methodSort.clear();
            fieldSort.clear();

            int i = prefs.getInt(PREFS_KEY_CLASS_SORT, 1);
            classSort.add(new RowSorter.SortKey(Math.abs(i) - 1, (i > 0 ? SortOrder.ASCENDING : SortOrder.DESCENDING)));
            tblClasses.getRowSorter().setSortKeys(classSort);

            i = prefs.getInt(PREFS_KEY_METHOD_SORT, 1);
            methodSort.add(new RowSorter.SortKey(Math.abs(i) - 1, (i > 0 ? SortOrder.ASCENDING : SortOrder.DESCENDING)));
            tblMethods.getRowSorter().setSortKeys(methodSort);

            i = prefs.getInt(PREFS_KEY_FIELD_SORT, 1);
            fieldSort.add(new RowSorter.SortKey(Math.abs(i) - 1, (i > 0 ? SortOrder.ASCENDING : SortOrder.DESCENDING)));
            tblFields.getRowSorter().setSortKeys(fieldSort);
        } catch (Exception e) {
            try {
                prefs.clear();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frmMcpMappingViewer = new JFrame();
        frmMcpMappingViewer.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent arg0) {
                savePrefs();
            }
        });
        frmMcpMappingViewer.setTitle("Cursed Interpolator " + VERSION_NUMBER);
        frmMcpMappingViewer.setBounds(100, 100, 955, 621);
        frmMcpMappingViewer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmMcpMappingViewer.getContentPane().setLayout(new BorderLayout(0, 0));

        JSplitPane splitMain = new JSplitPane();
        splitMain.setDividerSize(3);
        splitMain.setResizeWeight(0.5);
        splitMain.setContinuousLayout(true);
        splitMain.setMinimumSize(new Dimension(179, 80));
        splitMain.setPreferredSize(new Dimension(179, 80));
        splitMain.setOrientation(JSplitPane.VERTICAL_SPLIT);

        JScrollPane scrlpnClasses = new JBScrollPane();
        scrlpnClasses.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        splitMain.setLeftComponent(scrlpnClasses);

        tblClasses = new JBTable();
        tblClasses.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        scrlpnClasses.setViewportView(tblClasses);
        tblClasses.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblClasses.getSelectionModel().addListSelectionListener(new ClassTableSelectionListener(tblClasses));
        tblClasses.setAutoCreateRowSorter(true);
        tblClasses.setEnabled(false);
        tblClasses.setModel(classesDefaultModel);
        tblClasses.setFillsViewportHeight(true);
        tblClasses.setCellSelectionEnabled(true);
        frmMcpMappingViewer.getContentPane().add(splitMain, BorderLayout.CENTER);

        JSplitPane splitMembers = new JSplitPane();
        splitMembers.setDividerSize(3);
        splitMembers.setResizeWeight(0.5);
        splitMembers.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitMain.setRightComponent(splitMembers);

        JScrollPane scrlpnMethods = new JBScrollPane();
        scrlpnMethods.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        splitMembers.setLeftComponent(scrlpnMethods);

        tblMethods = new JBTable();
        tblMethods.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tblMethods.setCellSelectionEnabled(true);
        tblMethods.setFillsViewportHeight(true);
        tblMethods.setAutoCreateRowSorter(true);
        tblMethods.setEnabled(false);
        tblMethods.setModel(methodsDefaultModel);
        scrlpnMethods.setViewportView(tblMethods);

        JScrollPane scrlpnFields = new JBScrollPane();
        scrlpnFields.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        splitMembers.setRightComponent(scrlpnFields);

        tblFields = new JBTable();
        tblFields.setCellSelectionEnabled(true);
        tblFields.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tblFields.setAutoCreateRowSorter(true);
        tblFields.setEnabled(false);
        tblFields.setModel(fieldsDefaultModel);
        tblFields.setFillsViewportHeight(true);
        scrlpnFields.setViewportView(tblFields);

        JPanel pnlHeader = new JPanel();
        frmMcpMappingViewer.getContentPane().add(pnlHeader, BorderLayout.NORTH);
        pnlHeader.setLayout(new BorderLayout(0, 0));

        JPanel pnlControls = new JPanel();
        pnlHeader.add(pnlControls, BorderLayout.NORTH);
        pnlControls.setSize(new Dimension(0, 40));
        pnlControls.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 2));

        JLabel lblSide = new JLabel("Side");
        pnlControls.add(lblSide);

        cmbSide = new ComboBox<>();
        cmbSide.setModel(new DefaultComboBoxModel<>(Side.values()));
        pnlControls.add(cmbSide);

        btnRefreshTables = new JButton("Reload/Load");
        btnRefreshTables.setEnabled(false);
        btnRefreshTables.addActionListener(new RefreshActionListener());
        pnlControls.add(btnRefreshTables);

        chkForceRefresh = new JCheckBox("Force reload");
        chkForceRefresh.setToolTipText("Force a reload from the MCP conf folder files instead of the session cache.");
        pnlControls.add(chkForceRefresh);

        pnlProgress = new JPanel();
        pnlProgress.setVisible(false);
        pnlHeader.add(pnlProgress, BorderLayout.SOUTH);
        pnlProgress.setLayout(new BorderLayout(0, 0));

        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setString("");
        progressBar.setForeground(UIManager.getColor("ProgressBar.foreground"));
        pnlProgress.add(progressBar);

        pnlFilter = new JPanel();
        FlowLayout flowLayout = (FlowLayout) pnlFilter.getLayout();
        flowLayout.setVgap(2);
        flowLayout.setAlignment(FlowLayout.LEFT);
        pnlFilter.setVisible(true);
        pnlHeader.add(pnlFilter, BorderLayout.CENTER);

        JLabel lblFilter = new JLabel("Search");
        pnlFilter.add(lblFilter);

        edtFilter = new JTextField();
        edtFilter.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                edtFilter.select(0, edtFilter.getText().length());
            }
        });
        edtFilter.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    btnSearch.doClick();
            }
        });
        pnlFilter.add(edtFilter);
        edtFilter.setColumns(40);

        btnSearch = new JButton("Search");
        btnSearch.setToolTipText("");
        btnSearch.addActionListener(new SearchActionListener());
        pnlFilter.add(btnSearch);
        edtFilter.setEnabled(false);
        btnSearch.setEnabled(false);

        JSeparator separator = new JSeparator();
        separator.setPreferredSize(new Dimension(1, 12));
        separator.setOrientation(SwingConstants.VERTICAL);
        pnlFilter.add(separator);

        JLabel lblAbout = new JLabel("About");
        pnlFilter.add(lblAbout);
        lblAbout.setForeground(Color.BLUE);
        lblAbout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblAbout.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String message = "<center>Cursed Interpolator " + VERSION_NUMBER + "<br/>" +
                        "Copyright (C) 2013 bspkrs<br/>" +
                        "Portions Copyright (C) 2013 Alex \"immibis\" Campbell<br/><br/>" +
                        "Author: calmilamsy<br/><br/>" +
                        "<h3>Credits:</h3>" +
                        "bspkrs (for the <a href=\"https://github.com/bspkrs/MCPMappingViewer\">program this is a fork of</a>), " +
                        "immibis (for <a href=\"https://github.com/immibis/bearded-octo-nemesis\">BON</a> code), " +
                        "Searge et al (for <a href=\"http://mcp.ocean-labs.de\">MCP</a>),<br/>" +
                        "Fabric (for <a href=\"https://fabricmc.net\">Enigma</a>), " +
                        "Cursed Fabric (for <a href=\"https://minecraft-cursed-legacy.github.io/\">Their unified b1.7.3 JAR</a>)<br/><br/>" +
                        "<h3>Cursed Interpolator Links:</h3>" +
                        "<a href=\"https://github.com/calmilamsy/cursed-interpolator\">Github Repo</a><br/>" +
                        "<a href=\"https://github.com/calmilamsy/cursed-interpolator/blob/master/change.log\">Change Log</a><br/>" +
                        "<a href=\"https://github.com/calmilamsy/cursed-interpolator/releases\">Binary Downloads</a><br/>" +
                        "<h3>MCP Mapping Viewer links:</h3>" +
                        "<a href=\"" + mcfTopic + "\">MCF Thread</a><br/>" +
                        "<a href=\"https://github.com/bspkrs/MCPMappingViewer\">Github Repo</a><br/>" +
                        "<a href=\"https://github.com/bspkrs/MCPMappingViewer/blob/master/change.log\">Change Log</a><br/>" +
                        "<a href=\"http://bspk.rs/MC/MCPMappingViewer/index.html\">Binary Downloads</a><br/>" +
                        "<a href=\"https://raw.github.com/bspkrs/MCPMappingViewer/master/LICENSE\">License</a><br/>" +
                        "<a href=\"https://twitter.com/bspkrs\">bspkrs on Twitter</a></center>";
                showHTMLDialog(MappingGui.this, message, "About Cursed Interpolator", JOptionPane.PLAIN_MESSAGE);
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                savePrefs();
            }
        });
        loadPrefs();
    }

    class ClassTableSelectionListener implements ListSelectionListener {
        private final JTable table;

        public ClassTableSelectionListener(JTable table) {
            this.table = table;
        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting() && !table.getModel().equals(classesDefaultModel)) {
                int i = table.getSelectedRow();
                if (i > -1) {
                    savePrefs();
                    String pkg = (String) table.getModel().getValueAt(table.convertRowIndexToModel(i), 0);
                    String name = (String) table.getModel().getValueAt(table.convertRowIndexToModel(i), 1);
                    tblMethods.setModel(currentLoader.getMethodModel(pkg + "/" + name));
                    tblMethods.setEnabled(true);
                    tblFields.setModel(currentLoader.getFieldModel(pkg + "/" + name));
                    tblFields.setEnabled(true);
                    new TableColumnAdjuster(tblMethods).adjustColumns();
                    new TableColumnAdjuster(tblFields).adjustColumns();
                    loadPrefs();
                } else {
                    tblMethods.setModel(methodsDefaultModel);
                    tblMethods.setEnabled(false);
                    tblFields.setModel(fieldsDefaultModel);
                    tblFields.setEnabled(false);
                }
            }
        }
    }

    class SearchActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (curTask != null && curTask.isAlive())
                return;

            savePrefs();

            edtFilter.setEnabled(false);
            btnSearch.setEnabled(false);
            pnlProgress.setVisible(true);
            tblClasses.setModel(classesDefaultModel);
            tblClasses.setEnabled(false);
            tblMethods.setModel(methodsDefaultModel);
            tblMethods.setEnabled(false);
            tblFields.setModel(fieldsDefaultModel);
            tblFields.setEnabled(false);

            curTask = new Thread(() -> {
                boolean crashed = false;

                try {
                    IProgressListener progress = new IProgressListener() {
                        private String currentText;

                        @Override
                        public void start(final int max, final String text) {
                            currentText = text.equals("") ? " " : text;
                            SwingUtilities.invokeLater(() -> {
                                progressBar.setString(currentText);
                                if (max >= 0)
                                    progressBar.setMaximum(max);
                                progressBar.setValue(0);
                            });
                        }

                        @Override
                        public void set(final int value) {
                            SwingUtilities.invokeLater(() -> progressBar.setValue(value));
                        }

                        @Override
                        public void setMax(final int max) {
                            SwingUtilities.invokeLater(() -> progressBar.setMaximum(max));
                        }
                    };

                    progress.start(0, "Searching MCP objects for input");
                    tblClasses.setModel(currentLoader.getSearchResults(edtFilter.getText(), progress));
                    tblClasses.setEnabled(true);
                    new TableColumnAdjuster(tblClasses).adjustColumns();
                    loadPrefs();
                } catch (Exception e1) {
                    String s = getStackTraceMessage("An error has occurred - give calmilamsy this stack trace (which has been copied to the clipboard)\n", e1);

                    System.err.println(s);

                    crashed = true;

                    final String errMsg = s;
                    SwingUtilities.invokeLater(() -> {
                        progressBar.setString(" ");
                        progressBar.setValue(0);

                        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(errMsg), null);
                        JOptionPane.showMessageDialog(MappingGui.this, errMsg, "MMV - Error", JOptionPane.ERROR_MESSAGE);
                    });
                } finally {
                    if (!crashed) {
                        SwingUtilities.invokeLater(() -> {
                            progressBar.setString(" ");
                            progressBar.setValue(0);
                            edtFilter.setEnabled(true);
                        });
                    }
                    pnlProgress.setVisible(false);
                    edtFilter.setEnabled(true);
                    btnSearch.setEnabled(true);
                }
            });

            curTask.start();
        }
    }

    class RefreshActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (curTask != null && curTask.isAlive())
                return;

            final Side side = Objects.requireNonNull((Side) cmbSide.getSelectedItem());

            final File mcpDir = new File(CursedInterpolatorSettingsStorage.getInstance().MCP_LOCATION);

            String error = null;

            if (!(new File(mcpDir, "conf/interpolator/mappings.tiny")).exists())
                error = "Cursed mapping have not been set up!";

            if (!mcpDir.isDirectory())
                error = "Folder not found (at " + mcpDir + ")";

            if (error != null) {
                JOptionPane.showMessageDialog(MappingGui.this, error, "MMV - Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            //TODO: Implement this one.
            /*
            if (cmbMCPDirPath.getSelectedIndex() != 0) {
                String selItem = (String) cmbMCPDirPath.getSelectedItem();
                DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) cmbMCPDirPath.getModel();

                if (model.getIndexOf(selItem) != -1)
                    model.removeElement(selItem);

                cmbMCPDirPath.insertItemAt(selItem, 0);
                cmbMCPDirPath.setSelectedItem(selItem);
            }

             */

            savePrefs();

            pnlFilter.setVisible(false);
            pnlProgress.setVisible(true);
            tblClasses.setModel(classesDefaultModel);
            tblClasses.setEnabled(false);
            tblMethods.setModel(methodsDefaultModel);
            tblMethods.setEnabled(false);
            tblFields.setModel(fieldsDefaultModel);
            tblFields.setEnabled(false);

            curTask = new Thread(() -> {
                boolean crashed = false;

                try {
                    IProgressListener progress = new IProgressListener() {
                        private String currentText;

                        @Override
                        public void start(final int max, final String text) {
                            currentText = text.equals("") ? " " : text;
                            SwingUtilities.invokeLater(() -> {
                                progressBar.setString(currentText);
                                if (max >= 0)
                                    progressBar.setMaximum(max);
                                progressBar.setValue(0);
                            });
                        }

                        @Override
                        public void set(final int value) {
                            SwingUtilities.invokeLater(() -> progressBar.setValue(value));
                        }

                        @Override
                        public void setMax(final int max) {
                            SwingUtilities.invokeLater(() -> progressBar.setMaximum(max));
                        }
                    };

                    if (!mcpInstances.containsKey(mcpDir.getAbsolutePath() + " " + side) || chkForceRefresh.isSelected()) {
                        progress.start(0, "Reading MCP configuration");
                        currentLoader = new McpMappingLoader(side, mcpDir, progress);
                        mcpInstances.put(mcpDir.getAbsolutePath() + " " + side, currentLoader);
                        chkForceRefresh.setSelected(false);
                    } else
                        currentLoader = mcpInstances.get(mcpDir.getAbsolutePath() + " " + side);

                    tblClasses.setModel(currentLoader.getClassModel());
                    tblClasses.setEnabled(true);
                    new TableColumnAdjuster(tblClasses).adjustColumns();
                    //                        TableRowSorter trs = (TableRowSorter) tblClasses.getRowSorter();
                    //                        trs.setComparator(2, McpMappingLoader.OBF_COMPARATOR);
                    loadPrefs();
                } catch (CantLoadMCPMappingException e1) {
                    String s = getStackTraceMessage("", e1);

                    System.err.println(s);

                    crashed = true;

                    final String errMsg = s;
                    SwingUtilities.invokeLater(() -> {
                        progressBar.setString(" ");
                        progressBar.setValue(0);

                        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(errMsg), null);
                        JOptionPane.showMessageDialog(MappingGui.this, errMsg, "MMV - Error", JOptionPane.ERROR_MESSAGE);
                    });
                } catch (Exception e1) {
                    String s = getStackTraceMessage("An error has occurred - give calmilamsy this stack trace (which has been copied to the clipboard)\n", e1);

                    System.err.println(s);

                    crashed = true;

                    final String errMsg = s;
                    SwingUtilities.invokeLater(() -> {
                        progressBar.setString(" ");
                        progressBar.setValue(0);

                        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(errMsg), null);
                        JOptionPane.showMessageDialog(MappingGui.this, errMsg, "MMV - Error", JOptionPane.ERROR_MESSAGE);
                    });
                } finally {
                    if (!crashed) {
                        SwingUtilities.invokeLater(() -> {
                            progressBar.setString(" ");
                            progressBar.setValue(0);
                            edtFilter.setEnabled(true);
                        });
                    }
                    pnlProgress.setVisible(false);
                    pnlFilter.setVisible(true);
                    edtFilter.setEnabled(true);
                    btnSearch.setEnabled(true);
                }
            });

            curTask.start();
        }
    }
}
