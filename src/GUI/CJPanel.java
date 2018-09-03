package GUI;

import javax.swing.*;
import java.awt.*;

public class CJPanel extends JPanel {
    private int frameSizeWidth = (int) (Toolkit.getDefaultToolkit().getScreenSize().width*0.5);
    private int frameSizeHeight = (int)(Toolkit.getDefaultToolkit().getScreenSize().height*0.8);

    public CJPanel(){
        super();
        setOpaque(false);
    }


    public CJPanel(LayoutManager theLayout, int width, int height){
        //Build the main panel
        super();
        setPreferredSize(new Dimension(width,height));
        setLayout(theLayout);
        setOpaque(false);
    }

    public CJPanel(LayoutManager theLayout, double width, double height){
        //Build the main panel
        super();
        setPreferredSize(new Dimension((int)width,(int)height));
        setLayout(theLayout);
        setOpaque(false);
    }

    public CJPanel(LayoutManager theLayout, Dimension dimension){
        //Build the main panel
        super();
        setPreferredSize(dimension);
        setLayout(theLayout);
        setOpaque(false);
    }

    public CJPanel(LayoutManager theLayout){
        //Build the main panel
        super();
        setLayout(theLayout);
        setOpaque(false);
    }

    public int getFrameSizeHeight() {
        return frameSizeHeight;
    }

    public int getFrameSizeWidth() {
        return frameSizeWidth;
    }
}
