package GUI;

import javax.swing.*;
import java.awt.*;

public class CJPanel extends JPanel {
    private int frameSizeWidth = (int) (Toolkit.getDefaultToolkit().getScreenSize().width*0.5);
    private int frameSizeHeight = (int)(Toolkit.getDefaultToolkit().getScreenSize().height*0.8);
    private Color backgroundColor = new Color(155, 187, 180);
    private CJPanel prevPanel;

    public CJPanel(){
        super();
        setBackground(backgroundColor);
    }


    public CJPanel(LayoutManager theLayout, int width, int height){
        //Build the main panel
        super();
        setPreferredSize(new Dimension(width,height));
        setLayout(theLayout);
        setBackground(backgroundColor);
    }

    public CJPanel(LayoutManager theLayout, double width, double height){
        //Build the main panel
        super();
        setPreferredSize(new Dimension((int)width,(int)height));
        setLayout(theLayout);
        setBackground(backgroundColor);
    }

    public CJPanel(LayoutManager theLayout, Dimension dimension){
        //Build the main panel
        super();
        setPreferredSize(dimension);
        setLayout(theLayout);
        setBackground(backgroundColor);
    }

    public CJPanel(LayoutManager theLayout){
        //Build the main panel
        super();
        setLayout(theLayout);
        setBackground(backgroundColor);
    }

    public int getFrameSizeHeight() {
        return frameSizeHeight;
    }

    public int getFrameSizeWidth() {
        return frameSizeWidth;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public CJPanel getPrevPanel() {
        return prevPanel;
    }

    public void setPrevPanel(CJPanel prevPanel) {
        this.prevPanel = prevPanel;
    }
}
