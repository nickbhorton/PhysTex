package font.editor.javaAttempt;

import font.editor.javaAttempt.MainWindow;

import javax.swing.*;

public class Editor {
    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainWindow main = new MainWindow();
                main.show();
            }
        });
    }
}
