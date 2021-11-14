package net.waterfallflower.cursedinterpolatorplugin.table;

import bspkrs.mmv.McpMappingLoader;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import immibis.bon.gui.Side;
import net.waterfallflower.cursedinterpolatorplugin.CursedInterpolatorSettingsStorage;
import net.waterfallflower.cursedinterpolatorplugin.table.listener.ClassTableSelectionListener;
import net.waterfallflower.cursedinterpolatorplugin.table.listener.RefreshActionListener;
import net.waterfallflower.cursedinterpolatorplugin.table.listener.SearchActionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MappingsViewerToolWindow extends JFrame {

    private final Project PROJECT_INSTANCE;
    private final ToolWindow TOOL_WINDOW_INSTANCE;

    public Thread FRAME_THREAD_SECOND;
    public McpMappingLoader CURRENT_INSTANCE;

    public JBTable TABLE_CLASSES;
    public JBTable TABLE_METHODS;
    public JBTable TABLE_FIELDS;

    public JPanel PANEL_PROGRESSBAR;
    public JProgressBar BAR_PROGRESSBAR;

    public JTextField FIELD_TABLE_SEARCH;
    public JButton BUTTON_TABLE_SEARCH;
    public JButton BUTTON_REFRESH_TABLE;
    public JComboBox<Side> BOX_TABLE_SIDE;

    private final List<RowSorter.SortKey> classSort = new ArrayList<>();
    private final List<RowSorter.SortKey> methodSort = new ArrayList<>();
    private final List<RowSorter.SortKey> fieldSort = new ArrayList<>();

    public MappingsViewerToolWindow(ToolWindow window, Project project) {
        this.TOOL_WINDOW_INSTANCE = window;
        this.PROJECT_INSTANCE = project;

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                savePrefs();
            }
        });

        setBounds(100, 100, 955, 621);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout(0, 0));


        JSplitPane SPLIT_MAIN = new JSplitPane();
        SPLIT_MAIN.setDividerSize(3);
        SPLIT_MAIN.setResizeWeight(0.5);
        SPLIT_MAIN.setContinuousLayout(true);
        SPLIT_MAIN.setMinimumSize(new Dimension(179, 80));
        SPLIT_MAIN.setPreferredSize(new Dimension(179, 80));
        SPLIT_MAIN.setOrientation(JSplitPane.VERTICAL_SPLIT);

        JScrollPane TABLE_CLASSES_SCROLL = new JBScrollPane();
        TABLE_CLASSES_SCROLL.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        SPLIT_MAIN.setLeftComponent(TABLE_CLASSES_SCROLL);

        TABLE_CLASSES = new JBTable();
        TABLE_CLASSES.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TABLE_CLASSES_SCROLL.setViewportView(TABLE_CLASSES);
        TABLE_CLASSES.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        TABLE_CLASSES.getSelectionModel().addListSelectionListener(new ClassTableSelectionListener(this, TABLE_CLASSES));
        TABLE_CLASSES.setAutoCreateRowSorter(true);
        TABLE_CLASSES.setEnabled(false);
        TABLE_CLASSES.setModel(TableModels.classesDefaultModel);
        TABLE_CLASSES.setFillsViewportHeight(true);
        TABLE_CLASSES.setCellSelectionEnabled(true);
        getContentPane().add(SPLIT_MAIN, BorderLayout.CENTER);

        JSplitPane SPLIT_MEMBERS = new JSplitPane();
        SPLIT_MEMBERS.setDividerSize(3);
        SPLIT_MEMBERS.setResizeWeight(0.5);
        SPLIT_MEMBERS.setOrientation(JSplitPane.VERTICAL_SPLIT);
        SPLIT_MAIN.setRightComponent(SPLIT_MEMBERS);

        JScrollPane TABLE_METHODS_SCROLL = new JBScrollPane();
        TABLE_METHODS_SCROLL.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        SPLIT_MEMBERS.setLeftComponent(TABLE_METHODS_SCROLL);

        TABLE_METHODS = new JBTable();
        TABLE_METHODS.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TABLE_METHODS.setCellSelectionEnabled(true);
        TABLE_METHODS.setFillsViewportHeight(true);
        TABLE_METHODS.setAutoCreateRowSorter(true);
        TABLE_METHODS.setEnabled(false);
        TABLE_METHODS.setModel(TableModels.methodsDefaultModel);
        TABLE_METHODS_SCROLL.setViewportView(TABLE_METHODS);

        JScrollPane TABLE_FIELDS_SCROLL = new JBScrollPane();
        TABLE_FIELDS_SCROLL.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        SPLIT_MEMBERS.setRightComponent(TABLE_FIELDS_SCROLL);

        TABLE_FIELDS = new JBTable();
        TABLE_FIELDS.setCellSelectionEnabled(true);
        TABLE_FIELDS.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TABLE_FIELDS.setAutoCreateRowSorter(true);
        TABLE_FIELDS.setEnabled(false);
        TABLE_FIELDS.setModel(TableModels.fieldsDefaultModel);
        TABLE_FIELDS.setFillsViewportHeight(true);
        TABLE_FIELDS_SCROLL.setViewportView(TABLE_FIELDS);

        JPanel HEADER = new JPanel();
        getContentPane().add(HEADER, BorderLayout.NORTH);
        HEADER.setLayout(new BorderLayout(0, 0));

        JPanel CONTROLS = new JPanel();
        HEADER.add(CONTROLS, BorderLayout.NORTH);
        CONTROLS.setSize(new Dimension(0, 40));
        CONTROLS.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 2));

        BOX_TABLE_SIDE = new ComboBox<>();
        BOX_TABLE_SIDE.setModel(new DefaultComboBoxModel<>(Side.values()));
        CONTROLS.add(BOX_TABLE_SIDE);

        FIELD_TABLE_SEARCH = new JTextField();
        FIELD_TABLE_SEARCH.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                FIELD_TABLE_SEARCH.select(0, FIELD_TABLE_SEARCH.getText().length());
            }
        });
        FIELD_TABLE_SEARCH.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    BUTTON_TABLE_SEARCH.doClick();
            }
        });
        CONTROLS.add(FIELD_TABLE_SEARCH);
        FIELD_TABLE_SEARCH.setColumns(40);

        BUTTON_TABLE_SEARCH = new JButton("Search");
        BUTTON_TABLE_SEARCH.setToolTipText("");
        BUTTON_TABLE_SEARCH.addActionListener(new SearchActionListener(this));
        CONTROLS.add(BUTTON_TABLE_SEARCH);
        FIELD_TABLE_SEARCH.setEnabled(false);
        BUTTON_TABLE_SEARCH.setEnabled(false);

        BUTTON_REFRESH_TABLE = new JButton("Reload/Load");
        BUTTON_REFRESH_TABLE.addActionListener(new RefreshActionListener(this));
        CONTROLS.add(BUTTON_REFRESH_TABLE);

        PANEL_PROGRESSBAR = new JPanel();
        PANEL_PROGRESSBAR.setVisible(false);
        HEADER.add(PANEL_PROGRESSBAR, BorderLayout.SOUTH);
        PANEL_PROGRESSBAR.setLayout(new BorderLayout(0, 0));

        BAR_PROGRESSBAR = new JProgressBar();
        BAR_PROGRESSBAR.setStringPainted(true);
        BAR_PROGRESSBAR.setString("");
        BAR_PROGRESSBAR.setForeground(UIManager.getColor("ProgressBar.foreground"));
        PANEL_PROGRESSBAR.add(BAR_PROGRESSBAR);

        JSeparator SEPARATOR = new JSeparator();
        SEPARATOR.setPreferredSize(new Dimension(1, 12));
        SEPARATOR.setOrientation(SwingConstants.VERTICAL);
        HEADER.add(SEPARATOR);

        loadPrefs();
    }


    public void savePrefs() {
        CursedInterpolatorSettingsStorage.getInstance().GUI_SIDE = Objects.requireNonNull(BOX_TABLE_SIDE.getSelectedItem()).toString();

        if (TABLE_CLASSES.getRowSorter().getSortKeys().size() > 0) {
            int i = TABLE_CLASSES.getRowSorter().getSortKeys().get(0).getColumn() + 1;
            CursedInterpolatorSettingsStorage.getInstance().CLASS_SORT = TABLE_CLASSES.getRowSorter().getSortKeys().get(0).getSortOrder() == SortOrder.DESCENDING ? i * -1 : i;
        } else
            CursedInterpolatorSettingsStorage.getInstance().CLASS_SORT = 1;

        if (TABLE_METHODS.getRowSorter().getSortKeys().size() > 0) {
            int i = TABLE_METHODS.getRowSorter().getSortKeys().get(0).getColumn() + 1;
            CursedInterpolatorSettingsStorage.getInstance().METHOD_SORT = TABLE_METHODS.getRowSorter().getSortKeys().get(0).getSortOrder() == SortOrder.DESCENDING ? i * -1 : i;
        } else
            CursedInterpolatorSettingsStorage.getInstance().METHOD_SORT = 1;

        if (TABLE_FIELDS.getRowSorter().getSortKeys().size() > 0) {
            int i = TABLE_FIELDS.getRowSorter().getSortKeys().get(0).getColumn() + 1;
            CursedInterpolatorSettingsStorage.getInstance().FIELD_SORT = TABLE_FIELDS.getRowSorter().getSortKeys().get(0).getSortOrder() == SortOrder.DESCENDING ? i * -1 : i;
        } else
            CursedInterpolatorSettingsStorage.getInstance().FIELD_SORT = 1;
    }

    public void loadPrefs() {
        try {
            BOX_TABLE_SIDE.setSelectedItem(Side.valueOf(CursedInterpolatorSettingsStorage.getInstance().GUI_SIDE));

            classSort.clear();
            methodSort.clear();
            fieldSort.clear();

            int i = CursedInterpolatorSettingsStorage.getInstance().CLASS_SORT;
            classSort.add(new RowSorter.SortKey(Math.abs(i) - 1, (i > 0 ? SortOrder.ASCENDING : SortOrder.DESCENDING)));
            TABLE_CLASSES.getRowSorter().setSortKeys(classSort);

            i = CursedInterpolatorSettingsStorage.getInstance().METHOD_SORT;
            methodSort.add(new RowSorter.SortKey(Math.abs(i) - 1, (i > 0 ? SortOrder.ASCENDING : SortOrder.DESCENDING)));
            TABLE_METHODS.getRowSorter().setSortKeys(methodSort);

            i = CursedInterpolatorSettingsStorage.getInstance().FIELD_SORT;
            fieldSort.add(new RowSorter.SortKey(Math.abs(i) - 1, (i > 0 ? SortOrder.ASCENDING : SortOrder.DESCENDING)));
            TABLE_FIELDS.getRowSorter().setSortKeys(fieldSort);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
