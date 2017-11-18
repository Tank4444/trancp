package forms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Magazine;
import entity.Program;
import entity.Road;
import entity.Store;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class mainForm extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JButton addMag;
    private JButton addStore;
    private JTable table;
    private DefaultTableModel model;
    private String[][] array;
    JMenuBar jMenuBar = new JMenuBar();
    private  int columnCount;
    private int columnName;
    private int rowCount;
    private File workfile;

    public mainForm() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        table.setShowGrid(true);
        table.setShowVerticalLines(true);
        table.setShowHorizontalLines(true);
        MatteBorder border = new MatteBorder(2,2,2,2,Color.green);
        table.setBorder(border);
        columnCount=1;
        rowCount=1;
        columnName=0;
        array = new String[rowCount][columnCount];
        array[rowCount-1][columnCount-1]= "<html>right - magazine;<br>down - store</html>";
        model=new DefaultTableModel();

        table.setModel(model);

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
                        array = loadFromFile(workfile);
                        flash();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    flash();

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
                    saveInFile(workfile,array);
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
        model.addColumn(columnName);
        columnName++;
    }

    private void addStore()
    {
        model.addRow(new Object[]{""});
        rowCount++;
    }

    private void flash()
    {

        model = new DefaultTableModel();
        table.setRowHeight(50);
        for(int i=0;i<array[0].length;i++)
        {
            model.addColumn(String.valueOf(i));
        }
        for(int i=0;i<array.length;i++)
        {
            model.addRow(new Object[]{""});
        }
        for(int i=0;i<array.length;i++)
        {
            for(int j=0;j<array[0].length;j++)
            {
                model.setValueAt(array[i][j],i,j);
            }
        }

        table.setModel(model);

    }

    private void updateData()
    {
        array= new String[model.getRowCount()][model.getColumnCount()];
        for(int i=0;i<model.getRowCount();i++)
        {
            for(int j=0;j<model.getColumnCount();j++)
            {
                array[i][j]=String.valueOf(model.getValueAt(i,j));
            }
        }
    }
    public static void main(String[] args) {
        mainForm dialog = new mainForm();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    public String[][] loadFromFile(File file) throws IOException {
        //Этот спец. объект для построения строки
        StringBuilder sb = new StringBuilder();
        try {
            //Объект для чтения файла в буфер
            BufferedReader in = new BufferedReader(new FileReader( file.getAbsoluteFile()));
            try {
                //В цикле построчно считываем файл
                String s;
                while ((s = in.readLine()) != null) {
                    sb.append(s);
                }
            } finally {
                //Также не забываем закрыть файл
                in.close();
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        ObjectMapper mapper = new ObjectMapper();
        return (String[][]) mapper.readValue(sb.toString(),String[][].class);

    }
    public void saveInFile(File file,String[][] array)
    {
        //work with file
        try {
            PrintWriter writer = new PrintWriter(file.getAbsoluteFile());
            ObjectMapper mapper = new ObjectMapper();
            writer.print(mapper.writeValueAsString(array));
            writer.close();
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
        } catch (JsonProcessingException e2) {
            e2.printStackTrace();
        }


    }
}
