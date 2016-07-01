/*
 * SimpleJXTableDemo.java is a 1.45 application that requires no other files. It is derived from
 * SimpleTableDemo in the Swing tutorial.
 */
package errordetection;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import jxl.read.biff.BiffException;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.action.AbstractActionExt;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.sort.RowFilters;
import org.jdesktop.swingx.table.ColumnFactory;
import org.jdesktop.swingx.table.TableColumnExt;
import org.jdesktop.swingx.decorator.ComponentAdapter;


/**
 * This SimpleJXTableDemo is a very simple example of how to use the extended features of the
 * JXTable in the SwingX project. The major features are covered, step-by-step. You can run
 * this demo from the command-line without arguments
 * java org.jdesktop.demo.sample.SimpleJXTableDemo
 *
 * If looking at the source, the interesting code is in configureJXTable().
 *
 * This is derived from the SimpleTableDemo in the Swing tutorial.
 *
 * @author Patrick Wright (with help from the Swing tutorial :))
 */
public class MyJXTable  {
    private static final Color MARKER_COLOUR = Color.GREEN;
    private static final Color INVALID_COLOUR = Color.RED;
    private static final Color DUR_COLOUR = Color.ORANGE;
    private static final Color TIME_COLOUR = Color.BLUE;
    private static final Color CASE_COLOUR = Color.MAGENTA;
    private static final Color ERROR_COLOUR = Color.RED;
    private static final Color WARNING_COLOUR = Color.YELLOW;
    private static int BAR_NUMS = 100;
    private static Integer[] INDEX_ARRAY = new Integer[]{4, 15, 32, 36, 58, 74, 92};
    private static List<Integer> ERROR_INDEX_LIST = new LinkedList(Arrays.asList(INDEX_ARRAY));
    private static List INVALID_ERROR_LIST = new LinkedList(Arrays.asList(INDEX_ARRAY));
    private static List DUR_ERROR_LIST = new LinkedList(Arrays.asList(INDEX_ARRAY));
    private static List TIME_ERROR_LIST = new LinkedList(Arrays.asList(INDEX_ARRAY));
    private static List CASE_ERROR_LIST = new LinkedList(Arrays.asList(INDEX_ARRAY));
    private static List INSUFF_ERROR_LIST = new LinkedList(Arrays.asList(INDEX_ARRAY));
    private static List ERROR_INDEX_LIST_LIGHT = new LinkedList(Arrays.asList(INDEX_ARRAY));
    private static List INVALID_ERROR_LIST_LIGHT = new LinkedList(Arrays.asList(INDEX_ARRAY));
    private static List DUR_ERROR_LIST_LIGHT = new LinkedList(Arrays.asList(INDEX_ARRAY));
    private static List TIME_ERROR_LIST_LIGHT = new LinkedList(Arrays.asList(INDEX_ARRAY));
    private static List CASE_ERROR_LIST_LIGHT = new LinkedList(Arrays.asList(INDEX_ARRAY));
    private static List INSUFF_ERROR_LIST_LIGHT = new LinkedList(Arrays.asList(INDEX_ARRAY));
    private static JXTable jxTable = new JXTable();
    private JFileChooser fileChooser;
    private JPanel config;
    private static String address;
    private static List<DEEvent> eventlog;
    private static TableRowSorter<TableModel> sorter;
    private static SampleTableModel model;
    private static ListIndexBar bar;
    
    public MyJXTable() {
        this.address = "/Day&Night.csv";
        this.BAR_NUMS = 100;
        this.bar = new ListIndexBar(BAR_NUMS);
    }
    public MyJXTable(int num, String filename) {
        this.BAR_NUMS = num;
        this.address = filename;
        this.bar = new ListIndexBar(BAR_NUMS);
    }
    public MyJXTable(int num, String filename, List<DEEvent> e) {
        this.BAR_NUMS = num;
        this.address = filename;
        this.eventlog = e;
        this.bar = new ListIndexBar(BAR_NUMS);
    }
          
 
    private JComponent initUI() {
        JComponent content = new JPanel(new BorderLayout());
        jxTable = initTable();
        configureJXTable(jxTable);
        
        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(jxTable);
        
        //Add the scroll pane to this panel.
        content.add(scrollPane, BorderLayout.CENTER);
        
        content.add(initConfigPanel(jxTable), BorderLayout.NORTH);
        return content;
    }
    
    /** Initialize our JXTable; this is standard stuff, just as with JTable */
    private JXTable initTable() {
        // boilerplate table-setup; this would be the same for a JTable
        jxTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // if we would want a per-table ColumnFactory we would have
        // to set it here, before setting the model
        // table.setColumnFactory(myVerySpecialColumnFactory);
        model = new SampleTableModel();
        jxTable.setModel(model);
//        model.loadData();
        System.out.println("filename: " + this.address);
        if(address!=null){
            String[] folders = address.split("/");
            String filename = folders[folders.length - 1];
            model.loadDataFromCSV(address);
    //        model.loadDataFromEventLog(eventlog);
        }
        else{
            model.loadDefaultData();
        }
        return jxTable;
    }
       
    public void setFilename(String filename){
        this.address = filename;
//        this.initTable();
    }
    
    public void setEventLog(List<DEEvent> e){
        this.eventlog = e;
    }
    
    /** For demo purposes, the special features of the JXTable are configured here. There is
     * otherwise no reason not to do this in initTable().
     */
    private void configureJXTable(JXTable jxTable) {
        // set the number of visible rows
        jxTable.setVisibleRowCount(30);
        // set the number of visible columns
        jxTable.setVisibleColumnCount(8);
        // This turns horizontal scrolling on or off. If the table is too large for the scrollpane,
        // and horizontal scrolling is off, columns will be resized to fit within the pane, which can
        // cause them to be unreadable. Setting this flag causes the table to be scrollable right to left.
        jxTable.setHorizontalScrollEnabled(true);

        // This shows the column control on the right-hand of the header.
        // All there is to it--users can now select which columns to view
        jxTable.setColumnControlVisible(true);
        
        // our data is pulling in too many columns by default, so let's hide some of them
        // column visibility is a property of the TableColumnExt class; we can look up a
        // TCE using a column's display name or its index

        // Sorting by clicking on column headers is on by default. However, the comparison
        // between rows uses a default compare on the column's type, and elevations
        // are not sorting how we want.
        //
        // We will override the Comparator assigned to the TableColumnExt instance assigned
        // to the elevation column. TableColumnExt has a property comparator will be used
        // by JXTable's sort methods. 
        // By using a custom Comparator we can control how sorting in any column takes place
        Comparator numberComparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                Double d1 = Double.valueOf(o1 == null ? "0" : (String)o1);
                Double d2 = Double.valueOf(o2 == null ? "0" : (String)o2);
                return d1.compareTo(d2);
            }
        };
        
        // comparators are good for special situations where the default comparator doesn't
        // understand our data.
        
        // setting the custom comparator 
//        jxTable.getColumnExt("ELEVATION").setComparator(numberComparator);
//        jxTable.getColumnExt("TEMPERATURE").setComparator(numberComparator);
        
        
        // We'll add a highlighter to offset different row numbers
        // Note the setHighlighters() takes an array parameter; you can chain these together.
        jxTable.setHighlighters(
                HighlighterFactory.createSimpleStriping());
        
        jxTable.addHighlighter(new ColorHighlighter(HighlightPredicate.ROLLOVER_ROW, Color.BLACK,
                Color.WHITE));
        
        // add a filter: include countries starting with a only
//        int col = jxTable.getColumn("COUNTRY").getModelIndex();
//        jxTable.setRowFilter(RowFilters.regexFilter(0, "^A", col));
        
        // resize all the columns in the table to fit their contents
        // this is available as an item in the column control drop down as well, so the user can trigger it.
        int margin = 5;
        jxTable.packTable(margin);
        
        // we want the country name to always show, so we'll repack just that column
        // we can set a max size; if -1, the column is forced to be as large as necessary for the
        // text
        margin = 10;
        int max = -1;
        // JW: don't - all column indices are view coordinates
        // JW: maybe we need xtable api to take a TableColumn as argument?
        //jxTable.packColumn(jxTable.getColumnExt("COUNTRY").getModelIndex(), margin, max);
//        int viewIndex = jxTable.convertColumnIndexToView(jxTable.getColumnExt("COUNTRY").getModelIndex());
//        jxTable.packColumn(viewIndex, margin, max);
    }
    
    /** This shows off some additional JXTable configuration, controlled by checkboxes in a Panel. */
    private JPanel initConfigPanel(final JXTable jxTable) {
        config = new JPanel();
        FlowLayout fll = (FlowLayout)config.getLayout();
        fll.setAlignment(FlowLayout.LEFT);
        fll.setHgap(30);
        
        // This shows or hides the column control--note this is possible at runtime
        final JCheckBox control = new JCheckBox();
        control.setSelected(jxTable.isColumnControlVisible());
        control.setAction(new AbstractAction("Show column control") {
            public void actionPerformed(ActionEvent e) {
                jxTable.setColumnControlVisible(control.isSelected());
            }
        });
        
        
        // turn sorting by column on or off
        // bug: there is no API to read the current value! we will assume it is false
        final JCheckBox sorting = new JCheckBox();
        
        sorting.setSelected(jxTable.isSortable());
        sorting.setAction(new AbstractAction("Sortable") {
            public void actionPerformed(ActionEvent e) {
                jxTable.setSortable(sorting.isSelected());
            }
        });
        
        // add checkbox for horizontal scrolling. basically, the table has an action for this,
        // and we need to link the checkbox up in both directions--so that if the property changes
        // the checkbox is updated, and vice-versa. we use an AbstractActionExt to make this easier.
        // you aren't supposed to understand this :) and yes, it will be refactored
        final JCheckBox horiz = new JCheckBox();
        
        AbstractActionExt hA = (AbstractActionExt)jxTable.getActionMap().get(JXTable.HORIZONTALSCROLL_ACTION_COMMAND);
        hA.addPropertyChangeListener(new PropertyChangeListener(){
            public void propertyChange(PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                
                if (propertyName.equals("selected")) {
                    Boolean selected = (Boolean)evt.getNewValue();
                    horiz.setSelected(selected.booleanValue());
                }
            }
        });
        horiz.addItemListener(hA);
        horiz.setAction(hA);
        
        fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a file to save");
//        FileNameExtensionFilter filter = new FileNameExtensionFilter("csv");
//        fileChooser.setFileFilter(filter);
        fileChooser.setAutoscrolls(true);
        fileChooser.setApproveButtonText("Save");
        
        JButton button_ExportFile = new JButton("export");
        JButton button_ImportFile = new JButton("import");
//        JMenu menu = new JMenu("File");
//        JMenuBar menuBar = new JMenuBar();
//        menuBar.add(menu);
//        menu.setMnemonic(KeyEvent.VK_A);
//        menu.getAccessibleContext().setAccessibleDescription(
//            "Manage the table");
//        JMenuItem menuItem_ImportFile = new JMenuItem("import file",
//                KeyEvent.VK_T);
//                menuItem_ImportFile.getAccessibleContext().setAccessibleDescription(
//                        "This doesn't really do anything");
        button_ImportFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItem_ImportFileActionPerformed(evt);
            }
        });
//        JMenuItem menuItem_ExportFile = new JMenuItem("export file",
//                KeyEvent.VK_T);
//                menuItem_ImportFile.getAccessibleContext().setAccessibleDescription(
//                        "This doesn't really do anything");
        button_ExportFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    saveJTableAsCSVActionPerformed(evt);
                } catch (IOException ex) {
                    Logger.getLogger(MyJXTable.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
//        menu.add(menuItem_ImportFile);
//        menu.add(menuItem_ExportFile);
        
        String[] filterlist = {"Default","All Errors","invalid & insuff errors","Duration errors","Time errors","Case errors"};
        final JComboBox filterbox = new JComboBox(filterlist);
        filterbox.setSelectedIndex(0);
        filterbox.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                String item = (String)filterbox.getSelectedItem();
                switch(item){
                    case "Default": 
                        bar.clearMarkers();
                        bar.clearMedMarkers();
                        break;
                    case "All Errors":
                        RowFilter<Object, Object> filter = new RowFilter<Object,Object>(){  
                        public boolean include(RowFilter.Entry entry){//entry：入口，登记  
                            Integer population = (Integer)entry.getIdentifier();//population:人口  
                            //intValue()以 int 类型返回该 Integer 的值。  
                            return ERROR_INDEX_LIST.contains(population);//过虑大于1的行  
                            //因为filter的返回值定义为Integer,所以表格内Object[][]中出现其它类型会出错  
                            }  
                        };   
                        sorter = new TableRowSorter<TableModel>(model);
//                        sorter.setRowFilter(filter); 
//                        jxTable.setRowSorter(sorter);
                      
                        final HighlightPredicate myPredicate = new HighlightPredicate() {
                            @Override 
                            public boolean isHighlighted(
                                  Component renderer, 
                                  ComponentAdapter adapter) {

                                  return INVALID_ERROR_LIST.contains(adapter.row);
                            }
                        };
                        ColorHighlighter highlighter = new ColorHighlighter(
                            myPredicate,
                            ERROR_COLOUR,   // background color
                            null);       // no change in foreground color                       
                        final HighlightPredicate myPredicateMed = new HighlightPredicate() {
                            @Override 
                            public boolean isHighlighted(
                                  Component renderer, 
                                  ComponentAdapter adapter) {

                                  return ERROR_INDEX_LIST.contains(adapter.row);
                            }
                        };
                        ColorHighlighter highlighterMed = new ColorHighlighter(
                            myPredicateMed,
                            WARNING_COLOUR,   // background color
                            null);       // no change in foreground color
                        jxTable.setHighlighters(highlighterMed);
                        jxTable.addHighlighter(highlighter);
                        bar.clearMarkers();
                        bar.clearMedMarkers();
                        bar.setForeground(ERROR_COLOUR);
//                        bar.addLightMarkers(ERROR_INDEX_LIST);
                        
                        bar.addMedMarkers(ERROR_INDEX_LIST_LIGHT); 
                        bar.addMarkers(INVALID_ERROR_LIST);
//                        frame.add(bar);
                        break;
                    case "invalid & insuff errors":
                        filter = new RowFilter<Object,Object>(){  
                        public boolean include(RowFilter.Entry entry){//entry：入口，登记  
                            Integer population = (Integer)entry.getIdentifier();//population:人口  
                            //intValue()以 int 类型返回该 Integer 的值。  
                            return INVALID_ERROR_LIST.contains(population);//过虑大于1的行  
                            //因为filter的返回值定义为Integer,所以表格内Object[][]中出现其它类型会出错  
                            }  
                        };   
                        sorter = new TableRowSorter<TableModel>(model);
//                        sorter.setRowFilter(filter); 
//                        jxTable.setRowSorter(sorter);
                        final HighlightPredicate myPredicateInv = new HighlightPredicate() {
                            @Override 
                            public boolean isHighlighted(
                                  Component renderer, 
                                  ComponentAdapter adapter) {

                                  return INVALID_ERROR_LIST.contains(adapter.row);
                            }
                        };
                        ColorHighlighter highlighterInv = new ColorHighlighter(
                            myPredicateInv,
                            ERROR_COLOUR,   // background color
                            null);       // no change in foreground color
                         final HighlightPredicate myPredicateInsuff = new HighlightPredicate() {
                            @Override 
                            public boolean isHighlighted(
                                  Component renderer, 
                                  ComponentAdapter adapter) {

                                  return INSUFF_ERROR_LIST_LIGHT.contains(adapter.row);
                            }
                        };
                        ColorHighlighter highlighterInsuff = new ColorHighlighter(
                            myPredicateInsuff,
                            WARNING_COLOUR,   // background color
                            null);       // no change in foreground color
                        jxTable.setHighlighters(highlighterInsuff);
                        jxTable.addHighlighter(highlighterInv);
                        bar.clearMarkers();
                        bar.clearMedMarkers();
                        bar.setForeground(ERROR_COLOUR);
//                        bar.addLightMarkers(ERROR_INDEX_LIST);
                        
                        bar.addMedMarkers(INSUFF_ERROR_LIST_LIGHT); 
                        bar.addMarkers(INVALID_ERROR_LIST);
//                        frame.add(bar);
//                        frame.add(bar);
                        break;
                    case "Duration errors":
                        filter = new RowFilter<Object,Object>(){  
                        public boolean include(RowFilter.Entry entry){//entry：入口，登记  
                            Integer population = (Integer)entry.getIdentifier();//population:人口  
                            //intValue()以 int 类型返回该 Integer 的值。  
                            return DUR_ERROR_LIST.contains(population);//过虑大于1的行  
                            //因为filter的返回值定义为Integer,所以表格内Object[][]中出现其它类型会出错  
                            }  
                        };   
                        sorter = new TableRowSorter<TableModel>(model);
//                        sorter.setRowFilter(filter);
//                        jxTable.setRowSorter(sorter);
                        final HighlightPredicate myPredicateDur = new HighlightPredicate() {
                            @Override 
                            public boolean isHighlighted(
                                  Component renderer, 
                                  ComponentAdapter adapter) {

                                  return DUR_ERROR_LIST.contains(adapter.row);
                            }
                        };
                        ColorHighlighter highlighterDur = new ColorHighlighter(
                              myPredicateDur,
                              ERROR_COLOUR,   // background color
                              null);       // no change in foreground color
                        final HighlightPredicate myPredicateDurLight = new HighlightPredicate() {
                            @Override 
                            public boolean isHighlighted(
                                  Component renderer, 
                                  ComponentAdapter adapter) {

                                  return DUR_ERROR_LIST_LIGHT.contains(adapter.row);
                            }
                        };
                        ColorHighlighter highlighterDurLight = new ColorHighlighter(
                              myPredicateDurLight,
                              WARNING_COLOUR,   // background color
                              null);       // no change in foreground color
                        jxTable.setHighlighters(highlighterDurLight);
                        jxTable.addHighlighter(highlighterDur);
                        
                        bar.clearMarkers();
                        bar.clearMedMarkers();
                        bar.setForeground(ERROR_COLOUR);
//                        bar.addLightMarkers(ERROR_INDEX_LIST);
                        
                        bar.addMedMarkers(DUR_ERROR_LIST_LIGHT); 
                        bar.addMarkers(DUR_ERROR_LIST);
//                        frame.add(bar);
                        break;
                    case "Time errors":
                        filter = new RowFilter<Object,Object>(){  
                        public boolean include(RowFilter.Entry entry){//entry：入口，登记  
                            Integer population = (Integer)entry.getIdentifier();//population:人口  
                            //intValue()以 int 类型返回该 Integer 的值。  
                            return TIME_ERROR_LIST.contains(population);//过虑大于1的行  
                            //因为filter的返回值定义为Integer,所以表格内Object[][]中出现其它类型会出错  
                            }  
                        };   
                        sorter = new TableRowSorter<TableModel>(model);
//                        sorter.setRowFilter(filter); 
//                        jxTable.setRowSorter(sorter);
                        final HighlightPredicate myPredicateTime = new HighlightPredicate() {
                            @Override 
                            public boolean isHighlighted(
                                  Component renderer, 
                                  ComponentAdapter adapter) {

                                  return TIME_ERROR_LIST.contains(adapter.row);
                            }
                        };
                        ColorHighlighter highlighterTime = new ColorHighlighter(
                              myPredicateTime,
                              ERROR_COLOUR,   // background color
                              null);       // no change in foreground color
                        final HighlightPredicate myPredicateTimeLight = new HighlightPredicate() {
                            @Override 
                            public boolean isHighlighted(
                                  Component renderer, 
                                  ComponentAdapter adapter) {

                                  return TIME_ERROR_LIST_LIGHT.contains(adapter.row);
                            }
                        };
                        ColorHighlighter highlighterTimeLight = new ColorHighlighter(
                              myPredicateTimeLight,
                              WARNING_COLOUR,   // background color
                              null);       // no change in foreground color
                        jxTable.setHighlighters(highlighterTimeLight);
                        jxTable.addHighlighter(highlighterTime);
                        bar.clearMarkers();
                        bar.clearMedMarkers();
                        bar.setForeground(ERROR_COLOUR);
                        bar.addMedMarkers(TIME_ERROR_LIST_LIGHT);
                        bar.addMarkers(TIME_ERROR_LIST);
//                        frame.add(bar);
                        break;
                    case "Case errors":
                        filter = new RowFilter<Object,Object>(){  
                        public boolean include(RowFilter.Entry entry){//entry：入口，登记  
                            Integer population = (Integer)entry.getIdentifier(); 
                            //intValue()以 int 类型返回该 Integer 的值。  
                            return CASE_ERROR_LIST.contains(population);
                            //因为filter的返回值定义为Integer,所以表格内Object[][]中出现其它类型会出错  
                            }  
                        };   
                        sorter = new TableRowSorter<TableModel>(model);
//                        sorter.setRowFilter(filter); 
//                        jxTable.setRowSorter(sorter);
                        final HighlightPredicate myPredicateCase = new HighlightPredicate() {
                            @Override 
                            public boolean isHighlighted(
                                  Component renderer, 
                                  ComponentAdapter adapter) {

                                  return CASE_ERROR_LIST.contains(adapter.row);
                            }
                        };
                        ColorHighlighter highlighterCase = new ColorHighlighter(
                              myPredicateCase,
                              ERROR_COLOUR,   // background color
                              null);       // no change in foreground color
                        final HighlightPredicate myPredicateCaseLight = new HighlightPredicate() {
                            @Override 
                            public boolean isHighlighted(
                                  Component renderer, 
                                  ComponentAdapter adapter) {

                                  return CASE_ERROR_LIST_LIGHT.contains(adapter.row);
                            }
                        };
                        ColorHighlighter highlighterCaseLight = new ColorHighlighter(
                              myPredicateCaseLight,
                              WARNING_COLOUR,   // background color
                              null);       // no change in foreground color
                        jxTable.setHighlighters(highlighterCaseLight);
                        jxTable.addHighlighter(highlighterCase);
                        bar.clearMarkers();
                        bar.clearMedMarkers();
                        bar.setForeground(ERROR_COLOUR);
                        bar.addMedMarkers(CASE_ERROR_LIST_LIGHT);
                        bar.addMarkers(CASE_ERROR_LIST);
//                        frame.add(bar);
                        break;
                }
            }
        });
        
        
//        config.add(fileChooser);
//        jxTable.setRowSorter(sorter);
        config.add(button_ImportFile);
        config.add(button_ExportFile);
//        config.add(control);
        config.add(sorting);
        config.add(horiz);
        config.add(filterbox);  
        return config;
    }
    
    private void saveJTableAsCSVActionPerformed(ActionEvent evt) throws IOException{
        int response = fileChooser.showOpenDialog(config);
        if (response == JFileChooser.APPROVE_OPTION) {
            // Read csv file here
            String out_address = fileChooser.getSelectedFile().getAbsolutePath();
            System.out.println(out_address);
            File file = new File(out_address + ".csv");
            ExportJTable e1 = new ExportJTable();
            e1.exportToCSV(jxTable, file);
        }
    }
    
    /**
     * Initialize application wide behaviour.
     * Here: install a shared custom ColumnFactory to configure 
     * column titles in all JXTables. 
     */
    private static void initApplicationDefaults() {
        ColumnFactory.setInstance(createColumnFactory());
    }

    /**
     * A ColumnFactory is used by JXTable to create and 
     * configure all columns. It can be set per-application (before
     * creating an JXTable) or per-table (before setting the model).
     * 
     * This ColumnFactory changes the column titles to a more human readable
     * form. The column title is a convenience property on TableColumnExt, it's
     * the String representation of the headerValue.
     * 
     * @return a custom ColumnFactory which sets column title
     *   while keeping the identifier as the old header value.
     */
    private static ColumnFactory createColumnFactory() {
        ColumnFactory factory = new ColumnFactory() {
            /**
             * We'll do a trick, though, and that is to set the identifiers
             * of each column to their current header value, so we can still
             * use the same names for identifiers in the rest of our code.
             * 
             * First, a trick: by default, the "identifier" for a
             * TableColumn is actually null unless we specifically set it;
             * the header value is used instead. By doing this get, we're
             * pulling the header value, and setting that as the identifier;
             * then we can change the header value independently. 
             */
            @Override
            public void configureTableColumn(TableModel model,
                    TableColumnExt columnExt) {
                super.configureTableColumn(model, columnExt);
                columnExt.setIdentifier(columnExt.getIdentifier());
                // ...and now change the title
                String title = columnExt.getTitle();
                title = title.substring(0, 1).toUpperCase()
                        + title.substring(1).toLowerCase();
                columnExt.setTitle(title.replace('_', ' '));
            }

        };
        return factory;
    }
    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {     
        initApplicationDefaults();
        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);
        
        //Create and set up the window.
//        final JXFrame frame = new JXFrame("EventLog", true);
        //Create and set up the content pane.
        JComponent newContentPane = new MyJXTable(BAR_NUMS,address).initUI();
        newContentPane.setOpaque(true); //content panes must be opaque
        final JXFrame frame = new JXFrame("EventLog", true);
        frame.setContentPane(newContentPane);
        
        //Display the window.
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // create the list index bar and configure it
//        final ListIndexBar bar = new ListIndexBar(BAR_NUMS);
        bar.setBackground(new Color(0, 255, 0, 0));        
        bar.setForeground(MARKER_COLOUR);
        bar.setOpaque(true);
        // add a set of example markers
//        bar.addMarkers(ERROR_INDEX_LIST);

        // add a selection listener to select the corresponding item in the list when the marker is selected
        bar.addSelectionListener(new ListSelectionListener() {
          @Override
          public void valueChanged(ListSelectionEvent e) {
            int selectedIndex = e.getFirstIndex();
            System.out.println("index selected " + selectedIndex);
            // mark selected row
            jxTable.setRowSelectionInterval(selectedIndex,selectedIndex);
            // scroll selected row into center of viewport
            if (!(jxTable.getParent() instanceof JViewport)) {
                return;
            }
            JViewport viewport = (JViewport) jxTable.getParent();
            Rectangle rect = jxTable.getCellRect(selectedIndex, 0, true);
            Rectangle viewRect = viewport.getViewRect();
            rect.setLocation(rect.x - viewRect.x, rect.y - viewRect.y);

            int centerX = (viewRect.width - rect.width) / 2;
            int centerY = (viewRect.height - rect.height) / 2;
            if (rect.x < centerX) {
              centerX = -centerX;
            }
            if (rect.y < centerY) {
              centerY = -centerY;
            }
            rect.translate(centerX, centerY);
            viewport.scrollRectToVisible(rect);
            }
        });  
//        frame.add(scroll, BorderLayout.CENTER);
        frame.add(bar,BorderLayout.EAST);
    }
    
    public static void setMarkerList(List<Integer> err, List<Integer> invErr, List<Integer> insuffErr, 
            List<Integer> durErr, List<Integer> timeErr, List<Integer> caseErr,
            List<Integer> errLight, List<Integer> invErrLight, List<Integer> insuffErrLight, 
            List<Integer> durErrLight, List<Integer> timeErrLight, List<Integer> caseErrLight
            ){
        MyJXTable.ERROR_INDEX_LIST = err;
        MyJXTable.INVALID_ERROR_LIST = invErr;
        MyJXTable.DUR_ERROR_LIST = durErr;
        MyJXTable.TIME_ERROR_LIST = timeErr;
        MyJXTable.CASE_ERROR_LIST = caseErr;
        MyJXTable.INSUFF_ERROR_LIST = insuffErr;
        for(Integer e : err){
            if(!invErr.contains(e)) MyJXTable.ERROR_INDEX_LIST_LIGHT.add(e);
        }
        for(Integer e : invErrLight){
            if(!invErr.contains(e)) MyJXTable.INVALID_ERROR_LIST_LIGHT.add(e);
        }
        for(Integer e : durErrLight){
            if(!durErr.contains(e)) MyJXTable.DUR_ERROR_LIST_LIGHT.add(e);
        }
        for(Integer e : timeErrLight){
            if(!timeErr.contains(e)) MyJXTable.TIME_ERROR_LIST_LIGHT.add(e);
        }
        for(Integer e : caseErrLight){
            if(!caseErr.contains(e)) MyJXTable.CASE_ERROR_LIST_LIGHT.add(e);
        }
        for(Integer e : insuffErrLight){
            if(!invErr.contains(e)) MyJXTable.INSUFF_ERROR_LIST_LIGHT.add(e);
        }
    }
    

    public static void startTable() {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
    
    public static void main(String args[]) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
    
    private void menuItem_ImportFileActionPerformed(java.awt.event.ActionEvent evt){
        int response = fileChooser.showOpenDialog(config);
        if (response == JFileChooser.APPROVE_OPTION) {
            // Read csv file here
            String address = fileChooser.getSelectedFile().getAbsolutePath();
            System.out.println(address);
            import_display(address);
        }
    }
    
    private static void import_display(String address){
        DEEventLog deeventLog;  
        try {        
            deeventLog = new DEEventLog(address);
             List<DEEvent> errors = deeventLog.detectError();
            List<DEEvent> invErrors = deeventLog.invError();
            List<DEEvent> durErrors = deeventLog.DurError();
            List<DEEvent> TimeErrors = deeventLog.TimeError();
            List<DEEvent> CaseErrors = deeventLog.CaseError();
            List<DEEvent> insuffErrors = deeventLog.insuffError();
            List<Integer> errMarkers = new ArrayList<Integer>();
            List<Integer> invErrMarkers = new ArrayList<Integer>();
            List<Integer> durErrMarkers = new ArrayList<Integer>();
            List<Integer> timeErrMarkers = new ArrayList<Integer>();
            List<Integer> caseErrMarkers = new ArrayList<Integer>(); 
            List<Integer> insuffErrMarkers = new ArrayList<Integer>();

            for(DEEvent e : errors){
                errMarkers.add(e.index());
            };
            for(DEEvent e : invErrors){
                invErrMarkers.add(e.index());
            };
            for(DEEvent e : durErrors){
                durErrMarkers.add(e.index());
            };
            for(DEEvent e : TimeErrors){
                timeErrMarkers.add(e.index());
            };
            for(DEEvent e : CaseErrors){
                caseErrMarkers.add(e.index());
            };
            for(DEEvent e : insuffErrors){
                insuffErrMarkers.add(e.index());
            };

            deeventLog.loosenThreshold();
            List<DEEvent> errorsLight = deeventLog.detectError();
            List<DEEvent> invErrorsLight = deeventLog.invError();
            List<DEEvent> durErrorsLight = deeventLog.DurError();
            List<DEEvent> TimeErrorsLight = deeventLog.TimeError();
            List<DEEvent> CaseErrorsLight = deeventLog.CaseError();
            List<DEEvent> insuffErrorsLight = deeventLog.insuffError();
            List<Integer> errMarkersLight = new ArrayList<Integer>();
            List<Integer> invErrMarkersLight = new ArrayList<Integer>();
            List<Integer> durErrMarkersLight = new ArrayList<Integer>();
            List<Integer> timeErrMarkersLight = new ArrayList<Integer>();
            List<Integer> caseErrMarkersLight = new ArrayList<Integer>(); 
            List<Integer> insuffErrMarkersLight = new ArrayList<Integer>();

            for(DEEvent e : errorsLight){
                errMarkersLight.add(e.index());
            };
            for(DEEvent e : invErrorsLight){
                invErrMarkersLight.add(e.index());
            };
            for(DEEvent e : durErrorsLight){
                durErrMarkersLight.add(e.index());
            };
            for(DEEvent e : TimeErrorsLight){
                timeErrMarkersLight.add(e.index());
            };
            for(DEEvent e : CaseErrorsLight){
                caseErrMarkersLight.add(e.index());
            };
            for(DEEvent e : insuffErrorsLight){
                insuffErrMarkersLight.add(e.index());
            };
            deeventLog.resetThreshold();            
            System.out.println("Marker numbers: "+deeventLog.getEventNum());
            MyJXTable myJXTable = new MyJXTable(deeventLog.getEventNum(),address,deeventLog.events());
            myJXTable.setMarkerList(errMarkers, invErrMarkers, insuffErrMarkers, 
                    durErrMarkers, timeErrMarkers, caseErrMarkers,
                    errMarkersLight, invErrMarkersLight, insuffErrMarkersLight, 
                    durErrMarkersLight, timeErrMarkersLight, caseErrMarkersLight
                    );
            myJXTable.startTable();
        } catch (BiffException ex) {
            Logger.getLogger(MyJXTable.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MyJXTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    class SampleTableModel extends DefaultTableModel {
        void loadData() {
            try {
//                URL url = SampleTableModel.class.getResource("/org/jdesktop/demo/sample/resources/weather.txt");
                String url = "171traces_Corrected.csv";
                loadDataFromCSV(url);
            } catch ( Exception e ) {
                e.printStackTrace();
                loadDefaultData();
            }
        }
        
        private void loadDataFromCSV(String filename) {
            try {
                FileInputStream fstream = new FileInputStream(filename);
                BufferedReader lnr = new BufferedReader(new InputStreamReader(fstream));
                String line = lnr.readLine();
                String[] cols = line.split(",");
                for ( String col : cols ) {
                    addColumn(col);
                }
                System.out.println("column established!");
                while (( line = lnr.readLine()) != null ) {
                    String[] attr = line.split(",");
                    addRow(Arrays.copyOfRange(attr, 0, attr.length));
                }
            } catch ( Exception e ) {
                e.printStackTrace();
                loadDefaultData();
            }
        }
        
        private void loadDataFromEventLog(List<DEEvent> log) {
            try {
                String[] cols = {"Case ID", "Activity", "Start Time", "End Time", "Mid time", "Duration", 
                    "INVALID", "INSUFF", "ACT_DUR_STD", "ACT_DUR_KNN", "ACT_DUR_CLUST",
                    "ACT_TIME_STD", "ACT_TIME_KNN", "CASE_STD", "CASE_RANGE"};
                for ( String col : cols ) {
                    addColumn(col);
                }
                System.out.println("column established!");
                for(DEEvent e : eventlog) {
                    String[] attr = { "" + e.caseID(), "" + e.activity(), "" + e.start(), "" + e.end(),
                    "" + e.midTime(), "" + e.duration(), "" + (e.isInvalid()?1:0), "" + (e.isInsufficient()?1:0),
                    "" + e.errors()[0], "" + e.errors()[1], "" + e.errors()[2], "" + e.errors()[3], "" + e.errors()[4],
                    "" + e.errors()[5], "" + e.errors()[6], "" + e.errors()[7], "" + e.errors()[8]};
                    addRow(Arrays.copyOfRange(attr, 0, attr.length));
                }
            } catch ( Exception e ) {
                e.printStackTrace();
                loadDefaultData();
            }
        }
        
        private void loadDefaultData() {
            int colCnt = 6;
            int rowCnt = 10;
            for ( int i=0; i < colCnt; i++ ) {
                addColumn("Column-" + (i + 1));
            }
            for ( int i=0; i <= rowCnt; i++ ) {
                String[] row = new String[colCnt];
                for ( int j=0; j < colCnt; j++ ) {
                    row[j] = "Row-" + i + "Column-" + (j + 1);
                }
                addRow(row);
            }
        }
    }
}