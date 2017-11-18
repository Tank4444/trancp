package forms;

import entity.Magazine;
import entity.Program;
import entity.Road;
import entity.Store;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class mainForm extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JButton addMag;
    private JButton addStore;
    private JTextField storeField;
    private JTextField magField;
    private JTable table;
    private DefaultTableModel model;
    private Program program;
    JMenuBar jMenuBar = new JMenuBar();
    private  int columnCount;
    private File workfile;

    public mainForm() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        program = new Program();
        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        addMag.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addMagazine();
            }
        });

        addStore.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addStore();
            }
        });
        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        jMenuBar.add(fileMenu());

        setJMenuBar(jMenuBar);

        flash();


    }

    private JMenu fileMenu()
    {
        // Создание выпадающего меню
        final JMenu file = new JMenu("Файл");
        // Пункт меню "Открыть"
        JMenuItem open = new JMenuItem("Открыть");
        open.setAccelerator(KeyStroke.getKeyStroke('O', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        open.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Open graph");
                int userSelection = fileChooser.showOpenDialog(getParent());
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    workfile = fileChooser.getSelectedFile();
                    try {
                        program = program.loadFromFile(workfile);
                        flash();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }


                }
            }
        });
        // Пункт меню из команды с выходом из программы
        JMenuItem exit = new JMenuItem("Выход");
        exit.setAccelerator(KeyStroke.getKeyStroke('E',Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        // Create
        JMenuItem create = new JMenuItem("Создать");
        create.setAccelerator(KeyStroke.getKeyStroke('C',Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        // Save
        JMenuItem save = new JMenuItem("Сохранить");
        save.setAccelerator(KeyStroke.getKeyStroke('S',Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Save Graph");
                int userSelection = fileChooser.showSaveDialog(getParent());
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    workfile = fileChooser.getSelectedFile();
                    //save hear
                    updateData();
                    program.saveInFile(workfile);
                }
            }
        });
        // Delete
        JMenuItem del = new JMenuItem("Удалить");
        del.setAccelerator(KeyStroke.getKeyStroke('D',Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

        // Добавим в меню пункта open
        file.add(open);
        file.add(create);
        file.add(save);
        file.add(del);
        file.addSeparator();
        file.add(exit);

        return file;
    }
    private void onOK()
    {
        // add your code here
        dispose();
    }

    private void onCancel()
    {
        // add your code here if necessary
        dispose();
    }

    private void addMagazine()
    {
        if (!magField.getText().equals("")) {
            Magazine magazine = new Magazine(magField.getText());
            program.addMagazine(magazine);
            for(Store store:program.getStores())
            {
                Road road = new Road(store,magazine,0);
                if(!program.getRoads().contains(road))
                {
                    program.addRoad(road);
                }
            }
            flash();
        }
    }

    private void addStore()
    {
        if(!storeField.getText().equals("")) {
            Store store = new Store(storeField.getText());
            program.addStore(store);
            for(Magazine magazine:program.getMagazines())
            {
                Road road = new Road(store,magazine,0);
                if(!program.getRoads().contains(road))
                {
                    program.addRoad(road);
                }
            }
            flash();
        }
    }

    private void flash()
    {
        model = new DefaultTableModel();
        table.setRowHeight(50);

        //outMag
        columnCount=0;
        model.addColumn(String.valueOf(columnCount));
        columnCount++;
        for (Magazine magazine:program.getMagazines())
        {
            model.addColumn(String.valueOf(columnCount));
            columnCount++;
        }
        Object[] objects= new Object[columnCount];
        objects[0]="<html>right - magazine;<br>down - store</html>";
        for(int i = 0;i<columnCount;i++)
        {
            if(i==program.getMagazines().size())break;
            objects[i+1] = String.valueOf(program.getMagazines().get(i).getValue()) ;
        }
        model.addRow(objects);
        //outStoreAndRoad
        for(Store store:program.getStores())
        {
            objects= new Object[columnCount];
            objects[0]=String.valueOf(store.getValue());
            ArrayList<Road> finded = new ArrayList<Road>();
            int count =1;
            for(int i=0;i<program.getRoads().size();i++)
            {
                if(count==columnCount)break;
                if(program.getRoads().get(i).getStore().getValue()==store.getValue())
                {
                    objects[count] = program.getRoads().get(i).getValue();
                    count++;
                }
            }
            /*
            for(Road road:program.getRoads()){
                if(store.equals(road.getStore()))finded.add(road);
            }
            int count = 1;
            for(Road road:finded)
            {
                objects[count] = road.getValue();
            }
            */
            model.addRow(objects);
        }
        table.removeAll();
        table.setModel(model);

    }

    private void updateData()
    {
        Program program = new Program();
    }
    public static void main(String[] args) {
        mainForm dialog = new mainForm();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
