package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CJButton extends JButton {
    private Font font;
//    private Color color;
    
    public CJButton(String title, Font font){
        super(title);
        setFont(font);
        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                setFont(new Font(font.getName(),Font.BOLD,font.getSize()));
            }

            public void mouseExited(MouseEvent evt) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                setFont(font);
            }
        });
    }
}
