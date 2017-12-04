package forms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
    private JMenuBar jMenuBar = new JMenuBar();
    private  int columnCount;
    private int columnName;
    private int rowCount;
    private File workfile;
    private int[][] freeCell;
    private int[][] Cickle;
    private int [][] raspred;
    private int [][] result;
    private int [][] znaki;

    int check=0;
    private int[][] newPath;

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
        for(int i=0;i<array.length;i++)
        {
            for (int j=0;j<array[0].length;j++)
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

        result = new int[workarray.length+1][workarray[0].length+1];
        raspred = new int[workarray.length+1][workarray[0].length+1];
        for(int i=0;i<workarray.length;i++)
        {
            result[i][0]=workarray[i][0];
        }
        for(int i=0;i<workarray[0].length;i++)
        {
            result[0][i]=workarray[0][i];
        }


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
                raspred[y][x]=workarray[y][0];
                workarray[0][x]-=workarray[y][0];
                workarray[y][0] = 0;

            }else if(workarray[0][x]==workarray[y][0])
            {

                //if mag = stor both 0
                raspred[y][x]=workarray[y][0];
                workarray[0][x]=0;
                workarray[y][0]=0;
            }else
            {
                //if stor > mag
                //stor - mag
                //mag = 0
                raspred[y][x]=workarray[0][x];
                workarray[y][0]-=workarray[0][x];
                workarray[0][x] = 0;
            }
            res += "x = " + x + " y = " + y + " cost = " + allCoordinate.get(0).cost + "<br>";
            result[y][x]=allCoordinate.get(0).cost;
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
        //JOptionPane.showMessageDialog(null,res);
        int[][] patiences = new int [result.length][result[0].length];
        int columnPatiens = result.length-1;
        for(int i=1;i<patiences[columnPatiens].length;i++)patiences[patiences.length-1][i]=-1;
        for(int i=1;i<patiences.length;i++)patiences[i][patiences[i].length-1]=-1;
        patiences[1][patiences[1].length-1]=0;


        /*
        int columnCheck=1;
        for(int i=2;i<result[0].length;i++)
        {
            if(result[1][i]>0)
            {
                columnCheck=i;
            }
        }
        //[i][j] i - row, j - column.
        //Ui + Vj = (Cij-cast)
        //for column
        patiences[patiences[0].length-1][columnCheck]=result[1][columnCheck]-patiences[1][patiences[1].length-1];
        */

        int allU=0;
        int allV=0;
        for(int i=1;i<patiences.length-1;i++)
        {
            allU+=patiences[i][patiences[i].length-1];
        }
        for(int i=1;i<patiences[0].length-1;i++)
        {
            allV+=patiences[patiences[patiences.length-1].length-1][i];
        }


        while ((allU<0)||(allV<0))
         {

            for(int i=1;i<patiences.length-1;i++)
            {
                for(int j = 1;j<patiences[0].length-1;j++)
                {
                    if(result[i][j]>0)
                    {
                        if(patiences[i][patiences[i].length-1]>-1) patiences[patiences.length-1][j]=result[i][j]-patiences[i][patiences[i].length-1];
                        if(patiences[patiences.length-1][j]>-1) patiences[i][patiences[i].length-1]=result[i][j]-patiences[patiences.length-1][j];
                    }
                }
            }
            allU=0;
            allV=0;
            for(int i=1;i<patiences.length-1;i++)
            {
                allU+=patiences[i][patiences[i].length-1];
            }
            for(int i=1;i<patiences[0].length-1;i++)
            {
                allV+=patiences[patiences[patiences.length-1].length-1][i];
            }
        }
        //считаем свободные
        //ΔCij = Cij – (Ui + Vj )
        freeCell = new int [result.length][result[0].length];
        for(int i=1;i<patiences.length-1;i++) {
            for (int j = 1; j < patiences[0].length - 1; j++) {
                if (raspred[i][j] == 0) {
                    freeCell[i][j]= workarray[i][j]-(patiences[i][patiences[i].length-1]+patiences[patiences.length-1][j]);
                }
            }
        }
        int check =0;
        for(int i=1;i<patiences.length-1;i++)for (int j = 1; j < patiences[0].length - 1; j++)if (freeCell[i][j] < 0) check=1;
        while(check==1)
        {

            int minx=1,miny=1;
            for(int i=1;i<patiences.length-1;i++)
                for (int j = 1; j < patiences[0].length - 1; j++)
                    if (freeCell[i][j] < freeCell[minx][miny])
                    {
                        minx=i;
                        miny=j;
                    };
            //find cikl
            int[][] path=new int [result.length][result[0].length];
            Cickle = new int [result.length][result[0].length];
            znaki = new int [result.length][result[0].length];
            path[minx][miny]=1;
            znaki[minx][miny]=1;
            for(int i=1;i<raspred[0].length-1;i++)
            {
                if(i==miny)continue;
                dfs(minx,i,copyArray(path),minx,miny,1,copyArray(znaki),-1);

            }

            int znach=0;
            for (int i=0;i<array.length;i++)
                for (int j=0;j<array[i].length;j++)
                {
                    if(Cickle[i][j]>0) {
                        if ((i == minx) && (j == miny))continue;
                        if(znach==0)znach=raspred[i][j];
                        else if(znach>raspred[i][j])znach=raspred[i][j];
                    }
                }
            for (int i=0;i<array.length;i++)
                for (int j=0;j<array[i].length;j++)
                {
                    if(Cickle[i][j]>0) {
                        if(znaki[i][j]>0)raspred[i][j]+=znach;
                        else raspred[i][j]-=znach;
                    }
                }
            patiences = new int [result.length][result[0].length];
            columnPatiens = result.length-1;
            for(int i=1;i<patiences[columnPatiens].length;i++)patiences[patiences.length-1][i]=-1;
            for(int i=1;i<patiences.length;i++)patiences[i][patiences[i].length-1]=-1;
            patiences[1][patiences[1].length-1]=0;
            allU=0;
            allV=0;
            for(int i=1;i<patiences.length-1;i++)
            {
                allU+=patiences[i][patiences[i].length-1];
            }
            for(int i=1;i<patiences[0].length-1;i++)
            {
                allV+=patiences[patiences[patiences.length-1].length-1][i];
            }

            while ((allU<0)||(allV<0))
            {

                for(int i=1;i<patiences.length-1;i++)
                {
                    for(int j = 1;j<patiences[0].length-1;j++)
                    {
                        if(result[i][j]>0)
                        {
                            if(patiences[i][patiences[i].length-1]>-1) patiences[patiences.length-1][j]=result[i][j]-patiences[i][patiences[i].length-1];
                            if(patiences[patiences.length-1][j]>-1) patiences[i][patiences[i].length-1]=result[i][j]-patiences[patiences.length-1][j];
                        }
                    }
                }
                allU=0;
                allV=0;
                for(int i=1;i<patiences.length-1;i++)
                {
                    allU+=patiences[i][patiences[i].length-1];
                }
                for(int i=1;i<patiences[0].length-1;i++)
                {
                    allV+=patiences[patiences[patiences.length-1].length-1][i];
                }
            }
            freeCell = new int [result.length][result[0].length];

                //find pot
            for(int i=1;i<patiences.length-1;i++) {
                for (int j = 1; j < patiences[0].length - 1; j++) {

                        freeCell[i][j]= workarray[i][j]-(patiences[i][patiences[i].length-1]+patiences[patiences.length-1][j]);

                }
            }
            for(int i=1;i<patiences.length-1;i++)for (int j = 1; j < patiences[0].length - 1; j++)if (freeCell[i][j] < 0) check=1;

        }
        int i=0;
    }
    private void dfs(int x,int y,int[][] path,int loopx,int loopy,int dir,int[][] znak, int zn)
    {
        if((x==loopx)&&(y==loopy))
        {
            Cickle=copyArray(path);
            znaki=copyArray(znak);
            return;
        }

        if(path[x][y]==0)
        {
            if(raspred[x][y]!=0)
            {
                path[x][y]=1;
                if(zn>0)znak[x][y]=1;
                else znak[x][y]=-1;
                zn=recers(zn);
                if(dir%2==0)
                {
                    dir++;
                    for(int i=1;i<raspred[0].length-1;i++)
                    {
                        dfs(x,i,copyArray(path),loopx,loopy,dir,copyArray(znak),zn);
                    }
                }else
                {
                    dir++;
                    for (int i=1;i<raspred.length-1;i++)
                    {
                        newPath=copyArray(path);
                        dfs(i,y,copyArray(path),loopx,loopy,dir,copyArray(znak),zn);
                    }
                }
            }
        }

    }

    private int recers(int zn)
    {
        if(zn>0)return -1;
        else return 1;
    }
    private int[][] copyArray(int[][] array)
    {
        int[][] result =new  int[array.length][array[0].length];
        for (int i=0;i<array.length;i++)
            for (int j=0;j<array[i].length;j++)result[i][j]=array[i][j];
        return result;
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
