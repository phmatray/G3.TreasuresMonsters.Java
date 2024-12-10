package features.inputoutput;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class GameInputGUI implements IGameInput {
    private int lastKey;
    private final Object lock = new Object();

    public GameInputGUI(JFrame frame) {
        lastKey = -1;
        frame.addKeyListener(getKeyAdapter());
        frame.setFocusable(true);
        SwingUtilities.invokeLater(frame::requestFocusInWindow);
    }

    private KeyAdapter getKeyAdapter() {
        return new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int pressedKey = switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP, KeyEvent.VK_W -> ARROW_UP;
                    case KeyEvent.VK_LEFT, KeyEvent.VK_A -> ARROW_LEFT;
                    case KeyEvent.VK_DOWN, KeyEvent.VK_S -> ARROW_DOWN;
                    case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> ARROW_RIGHT;
                    case KeyEvent.VK_H -> 'H';
                    case KeyEvent.VK_Q -> 'Q';
                    default -> -1;
                };

                synchronized (lock) {
                    lastKey = pressedKey;

                    // Notify waiting thread(s) that a key is available
                    lock.notifyAll();
                }
            }
        };
    }

    @Override
    public int readKey() {
        synchronized (lock) {
            while (lastKey == -1) {
                try {
                    lock.wait(); // Wait until a key is pressed
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            int key = lastKey;
            lastKey = -1; // Reset after reading
            return key;
        }
    }
}
