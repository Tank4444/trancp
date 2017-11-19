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
import java.util.Collections;
import java.util.Comparator;

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
        updateData();
        int storesMas=0;
        for(int i=1;i<array.length;i++) {
            storesMas+=Integer.parseInt(array[i][0]);
        }
        int magazineMas=0;
        for(int i=1;i<array[0].length;i++) {
            magazineMas+=Integer.parseInt(array[0][i]);
        }
        String res="<html>";

        res+="Stores = "+String.valueOf(storesMas)+"<br>";
        res+="Magazine = "+String.valueOf(magazineMas)+"<br>";
        if (storesMas != magazineMas) {
            res+="Транспортная задача открытая<br>";
            res+="</html>";
            JOptionPane.showMessageDialog(null,res);
            return;
        }
        res+="Транспортная задача закрытая<br>";
        res+="</html>";
        JOptionPane.showMessageDialog(null,res);

        int[][] workarray = new int[array.length][array[0].length];
        for(int i=0;i<array[0].length;i++)
        {
            for (int j=0;j<array.length;j++)
            {
                if((i==0)&&(j==0)) workarray[i][j]=0;
                else workarray[i][j]=Integer.parseInt(array[i][j]);
            }
        }
        ArrayList<Coordinate> allCoordinate = new ArrayList<Coordinate>();
        for(int i=1;i<array.length;i++)
        {
            for (int j=1;j<array[0].length;j++)
            {
                allCoordinate.add(new Coordinate(j,i,workarray[i][j]));
            }
        }

            Collections.sort(allCoordinate, new Comparator<Coordinate>() {
                public int compare(Coordinate o1, Coordinate o2) {
                    if(o1.cost>o2.cost)return 1;
                    else return -1;
                }
            });


        res="<html>Результат(ячейки)<br>";


        /*
            while ((storesMas != 0) && (magazineMas != 0)) {


                Coordinate coordinate = allCoordinate.get(0);

                for(Coordinate coordinate1:allCoordinate)
                {
                    if(coordinate.cost>coordinate1.cost)coordinate=coordinate1;
                }
                if(!((workarray[0][coordinate.x]==0)||(workarray[coordinate.y][0]==0)))
                {
                    if (workarray[0][coordinate.x] > workarray[coordinate.y][0]) {

                        workarray[0][coordinate.x] -= workarray[coordinate.y][0];
                        workarray[coordinate.y][0] = 0;

                    } else if (workarray[0][coordinate.y] == workarray[coordinate.x][0]) {
                        workarray[coordinate.x][0] = 0;
                        workarray[0][coordinate.y] = 0;
                    } else {

                        workarray[coordinate.y][0] -= workarray[0][coordinate.x];
                        workarray[0][coordinate.x] = 0;

                    }
                    res += "x = " + coordinate.y + " y = " + coordinate.x + " cost = " + coordinate.cost + "<br>";

                    storesMas = 0;
                    for (int i = 1; i < array.length; i++) {
                        storesMas += workarray[i][0];
                    }
                    magazineMas = 0;
                    for (int i = 1; i < array[0].length; i++) {
                        magazineMas += workarray[0][i];
                    }
                }
                allCoordinate.remove(coordinate);

            }
            res+="</html>";
            JOptionPane.showMessageDialog(null,res);
        */
        while (!allCoordinate.isEmpty())
        {
            int x = allCoordinate.get(0).x;
            int y = allCoordinate.get(0).y;
            // [0] [x] magazine
            // [y] [0] store
            if((workarray[0][x]==0)||(workarray[y][0]==0))
            {
                allCoordinate.remove(0);
                continue;
            }
            if(workarray[0][x] > workarray[y][0])
            {
                //if mag > stor
                //mag - stor
                //stor = 0
                workarray[0][x]-=workarray[y][0];
                workarray[y][0] = 0;
            }else if(workarray[0][x]==workarray[y][0])
            {

                //if mag = stor both 0
                workarray[0][x]=0;
                workarray[y][0]=0;
            }else
            {
                //if stor > mag
                //stor - mag
                //mag = 0
                workarray[y][0]-=workarray[0][x];
                workarray[0][x] = 0;
            }
            res += "x = " + x + " y = " + y + " cost = " + allCoordinate.get(0).cost + "<br>";
            allCoordinate.remove(0);
        }
        storesMas = 0;
        for (int i = 1; i < array.length; i++) {
            storesMas += workarray[i][0];
        }
        magazineMas = 0;
        for (int i = 1; i < array[0].length; i++) {
            magazineMas += workarray[0][i];
        }
        res+="stor = "+storesMas+" mag = "+magazineMas+"</html>";
        JOptionPane.showMessageDialog(null,res);

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

    private class Coordinate {
        int x,y,cost;
        Coordinate(int x,int y,int cost) {
            this.x=x;
            this.y=y;
            this.cost=cost;
        }
    }
}
