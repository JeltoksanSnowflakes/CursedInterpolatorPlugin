package net.waterfallflower.cursedinterpolatorplugin.interpolator.table;

import javax.swing.table.DefaultTableModel;

public class TableModels {
    public static DefaultTableModel fieldsDefaultModel = new DefaultTableModel(
            new Object[][] {{},},
            new String[] {
                    "MCP Name",
                    "SRG Name",
                    "Obf Name",
                    "Comment",
                    "Client Only"
            }
    ) {
        private static final long serialVersionUID = 1L;

        final Class<?>[] columnTypes = new Class[] {
                String.class,
                String.class,
                String.class,
                String.class,
                Boolean.class
        };

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return columnTypes[columnIndex];
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    public static DefaultTableModel classesDefaultModel = new DefaultTableModel(
            new Object[][] {{},},
            new String[] {
                    "Pkg name",
                    "SRG name",
                    "Obf name",
                    "Client Only"
            }
    ) {
        private static final long serialVersionUID = 1L;
        final Class<?>[] columnTypes = new Class[] {
                String.class,
                String.class,
                String.class,
                Boolean.class
        };

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return columnTypes[columnIndex];
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    public static DefaultTableModel methodsDefaultModel = new DefaultTableModel(
            new Object[][] {{},},
            new String[] {
                    "MCP Name",
                    "SRG Name",
                    "Obf Name",
                    "SRG Descriptor",
                    "Comment",
                    "Client Only"
            }
    ) {
        private static final long serialVersionUID = 1L;

        final Class<?>[] columnTypes = new Class[]{
                String.class,
                String.class,
                String.class,
                String.class,
                String.class,
                Boolean.class
        };

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return columnTypes[columnIndex];
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
}
