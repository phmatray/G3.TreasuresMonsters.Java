package features.inputoutput;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class GameInputGUI implements IGameInput {
    private int lastKey;

    public GameInputGUI(JFrame frame) {
        lastKey = -1;
        frame.addKeyListener(getKeyAdapter());
        frame.setFocusable(true);
        SwingUtilities.invokeLater(() -> frame.requestFocusInWindow());
    }

    private KeyAdapter getKeyAdapter() {
        return new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (Character.toLowerCase(e.getKeyChar())) {
                    case 'w':
                        lastKey = ARROW_UP;
                        break;
                    case 'a':
                        lastKey = ARROW_LEFT;
                        break;
                    case 's':
                        lastKey = ARROW_DOWN;
                        break;
                    case 'd':
                        lastKey = ARROW_RIGHT;
                        break;
                    default:
                        lastKey = e.getKeyChar();
                        break;
                }
            }
        };
    }

    @Override
    public int readKey() {
        int key = lastKey;
        lastKey = -1; // Reset after reading
        return key;
    }
}
