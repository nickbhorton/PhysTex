package font.editor.javaAttempt;

import javax.swing.*;

public class MainWindow {
    private JFrame window;
    private JPanel canvas;
    public MainWindow(){
        window = new JFrame();
        window.setTitle("Font Editor");
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setSize(800, 800);
        window.setLocationRelativeTo(null);
        canvas = new Canvas();
        window.add(canvas);
    }
    public void show(){
        window.setVisible(true);
    }
}
