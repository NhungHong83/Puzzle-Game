package puzzleGame;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class Control {

    private PuzzleGUI pz;
    private int size = 0;
    private JButton[][] matrix;
    private int countMove = 0;
    private Timer timer;
    private int countTime = 0;
    private boolean isStart = false;//
    private boolean isNewGame = false;

    public Control(PuzzleGUI pz) {
        this.pz = pz;
        setSize();
        addButton();
    }

    public void newGame() { //isStart:f=>chay, khi win=>isStart = false=>chay,isStart = true
        isNewGame = true;//?
        countMove = 0;
        countTime = 0;
        pz.lblCountMove.setText("0");
        pz.lblCountTime.setText("0");
        addButton();
        countTime();
        isStart = true;//?
        
    }

    public void setSize() {
        Toolkit t = Toolkit.getDefaultToolkit();
        Dimension d = t.getScreenSize();
        int lastIndex = (d.height-200) / 100;
        for (int i = 3; i <= lastIndex; i++) {
            pz.cbxSize.addItem(i+"x"+i);
        }
    }

    //add button vao panel
    public void addButton() {
        //int size = pz.cbxSize.getSelectedIndex()+3;//
        String txt = pz.cbxSize.getSelectedItem().toString();//3x3; 4x4
        size = Integer.parseInt(txt.split("x")[0]);//
        pz.getPnLayout().removeAll();
        pz.pnLayout.setLayout(new GridLayout(size, size, 10, 10));
        pz.pnLayout.setPreferredSize(new Dimension(100 * size, 100 * size));
        matrix = new JButton[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                JButton btn = new JButton(i * size + j + 1 + "");
                matrix[i][j] = btn;
                pz.pnLayout.add(btn);
                btn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (!isNewGame) {
                            JOptionPane.showMessageDialog(null, "Press new game button");
                            return;
                        }
                        if (checkMove(btn)) {
                            moveButton(btn);
                            if (checkWin()) {
                                timer.stop();
                                isStart = false;
                                isNewGame = false;
                                JOptionPane.showMessageDialog(pz, "You Win");
                            }
                        }
                    }
                });
            }
        }
        matrix[size - 1][size - 1].setText("");
        mixButton();
        pz.pack();//
    }
    //
    
    //find position of empty button
    public Point getPosEmpty() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (matrix[i][j].getText().equals("")) {
                    return new Point(i, j);
                }
            }
        }
        return null;
    }

    //mix button
    public void mixButton() {
        for (int k = 0; k < 1000; k++) {
            Point p = getPosEmpty();
            int i = p.x;
            int j = p.y;
            Random r = new Random();
            int choice = r.nextInt(4);
            String txt = "";
            switch (choice) {
                case 0://doi voi button tren
                    if (i > 0) {
                        txt = matrix[i - 1][j].getText();
                        matrix[i][j].setText(txt);
                        matrix[i - 1][j].setText("");
                    }
                    break;
                case 1://doi voi button duoi
                    if (i < size - 1) {
                        txt = matrix[i + 1][j].getText();
                        matrix[i][j].setText(txt);
                        matrix[i + 1][j].setText("");
                    }
                    break;
                case 2://doi voi button trai
                    if (j > 0) {
                        txt = matrix[i][j - 1].getText();
                        matrix[i][j].setText(txt);
                        matrix[i][j - 1].setText("");
                    }
                    break;
                case 3://doi voi button phai
                    if (j < size - 1) {
                        txt = matrix[i][j + 1].getText();
                        matrix[i][j].setText(txt);
                        matrix[i][j + 1].setText("");
                    }
                    break;
            }
        }
    }

    //check xem button click vao co duoc doi voi btn rong hay ko
    public Point getPosClick(JButton btn) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (matrix[i][j].getText().equals(btn.getText())) {
                    return new Point(i, j);
                }
            }
        }
        return null;
    }

    public boolean checkMove(JButton btn) {
        if (btn.getText().equals("")) {
            return false;
        }
        Point cb = getPosClick(btn);
        Point eb = getPosEmpty();
        if (cb.x == eb.x && Math.abs(cb.y - eb.y) == 1) {
            return true;
        }
        if (cb.y == eb.y && Math.abs(cb.x - eb.x) == 1) {
            return true;
        }
        return false;
    }

    public void moveButton(JButton btn) {
        Point eb = getPosEmpty();
        String txt = btn.getText();
        matrix[eb.x][eb.y].setText(txt);
        btn.setText("");
        countMove++;
        pz.lblCountMove.setText(countMove + "");
    }

    public boolean checkWin() {
        if (!matrix[size - 1][size - 1].getText().equals("")) {//Khi o cuoi cung !=null
            return false;
        }
        //Khi o cuoi cung rong thi no lam cai đống này
        int number = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                number++;
                String txt = matrix[i][j].getText();//""
//                    String txt="";
                if (txt.equals("")) {
                    txt = size * size + "";////O rong phai co gia tri la size*size
                }//
                //
                int value = Integer.parseInt(txt);//txt
                if (number != value) {//So di theo thu tu
                    return false;
                }
            }
        }
        return true;
    }

    public void countTime() {
        if (isStart) {//true
            timer.stop();
        }
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                countTime++;
                pz.lblCountTime.setText(countTime + "");
            }
        });
        timer.start();
    }
    
//Ban dau f=>new game1=>time.start()=>isStart=true=>stop()=>new game 2
//                            t...         t...                t= 0 ...      
    
}
