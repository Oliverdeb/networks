import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by dbroli001 on 2017/03/30.
 */
public class testing {
    public static void main(String[] args) {

        JFrame test = new JFrame("test");
        KeyListener f = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {

            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                System.out.println("testing " + keyEvent.getKeyCode() + " char=" + keyEvent.getKeyChar());
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {

            }
        };
        test.addKeyListener(f);
//        test.add(new JPanel());
        test.setBounds(100,100,100,100);
        test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        test.setVisible(true);





    }

}
