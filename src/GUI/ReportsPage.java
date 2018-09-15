package GUI;

import BL.AuthService;
import DTO.DateReportDto;
import DTO.EmployeeDto;
import Entities.Employee.Profession;
import com.google.gson.Gson;
import com.toedter.calendar.JCalendar;

import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReportsPage extends JPanel {

    private Controller controller;

    private SpringLayout theLayout = new SpringLayout();
    private int fontSize = 30;
    private Font font = new Font("Candara", 0, fontSize); //Custom page font

    private JLabel labelPickDate = new JLabel("Daily Reports:", JLabel.TRAILING);
    private CJPanel calendarPanel;
    private JCalendar fieldCalendar = new JCalendar();
    private String chosenDate;

    private CJPanel subPanel;
    private CJButton btnBack = new CJButton("Back",font);
    private CJButton btnWordFile = new CJButton("Open in Word",font);
    private CJButton btnShowReport = new CJButton("Show Report",font);


    public ReportsPage(Controller controller) throws HeadlessException {

        this.controller = controller;

        setLayout(theLayout);

        labelPickDate.setFont(new Font("Candara",0,60));
        add(labelPickDate);

        theLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER,labelPickDate,0,SpringLayout.HORIZONTAL_CENTER,this);
        theLayout.putConstraint(SpringLayout.NORTH,labelPickDate,20,SpringLayout.NORTH,this);

        buildCalendar();

        buildSubPanel();
    }

    private void buildCalendar(){

        calendarPanel = new CJPanel(new GridLayout(),controller.getAppFrame().getWidth()*0.8,controller.getAppFrame().getHeight()*0.4);

        theLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, calendarPanel, 0, SpringLayout.HORIZONTAL_CENTER, this);
        theLayout.putConstraint(SpringLayout.NORTH, calendarPanel, 120, SpringLayout.NORTH, this);

        calendarPanel.add(fieldCalendar);

        add(calendarPanel);

        fieldCalendar.addPropertyChangeListener(evt -> chosenDate = new SimpleDateFormat("yyyy-MM-dd").format(fieldCalendar.getDate()));
    }

    private void buildSubPanel(){

        subPanel = new CJPanel(new SpringLayout(),controller.getAppFrame().getWidth()*0.8,controller.getAppFrame().getHeight()*0.1);

        theLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, subPanel, 0, SpringLayout.HORIZONTAL_CENTER, this);
        theLayout.putConstraint(SpringLayout.NORTH, subPanel, 70, SpringLayout.SOUTH, calendarPanel);


        subPanel.add(btnBack);
        subPanel.add(btnWordFile);
        subPanel.add(btnShowReport);

        SpringUtilities.makeCompactGrid(subPanel,1,3,25,10,50,5);

        add(subPanel);

        btnBack.addActionListener(e -> {
            setVisible(false);
            controller.showBranchPage();
        });

        btnWordFile.addActionListener(e ->  {

                    /* TODO: 10/09/2018
                     1) Create the desired report word file using the String date from this.chosenDate.
                     2) Save the file in the path mentioned below ".idea/dataSources/wordReportsFiles/" recommend to save file name same as date.
                     3) Open file using the file's path like example below.
                    */

                    //Example word file
                    //+"dateReport" + date+ ".docx"
                    try
                    {
                        if (Desktop.isDesktopSupported()) {
                            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = format.parse(chosenDate);
                            PrintStream out = new PrintStream(Controller.echoSocket.getOutputStream());
                            Gson gson = new Gson();
                            DateReportDto dateReportDto = new DateReportDto("reportByDate", date,
                                    AuthService.getInstance().getCurrentEmployee().getBranchNumber());

                            out.println(gson.toJson(dateReportDto));

                            DataInputStream in = new DataInputStream(Controller.echoSocket.getInputStream());
                            String response = in.readLine();

                            if (response.equals("true")) {

                                Desktop.getDesktop().open(new File(".idea/dataSources/wordReportsFiles/" + "dateReport" + chosenDate + ".docx"));
                            } else {
                                JOptionPane.showMessageDialog(new JFrame(), "report can;t be created!", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }

                    //Desktop.getDesktop().open(new File(".idea/dataSources/wordReportsFiles/Example.docx"));
                    // Desktop.getDesktop().open(new File(".idea/dataSources/wordReportsFiles/" + fileName + ".docx"));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
        });

        btnShowReport.addActionListener(e -> {
            //maybe show report using Json like in WebBrowser?
        });
    }


    @Override
    protected void paintComponent(Graphics g) { g.drawImage(controller.getInnerPageImage(),0,0,null); }
}
