package GUI;

import BL.AuthService;
import DTO.DateReportDto;
import Entities.Product;
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

public class ReportsPage extends JPanel {

    private Controller controller;

    private SpringLayout theLayout = new SpringLayout();
    private int fontSize = 30;
    private Font font = new Font("Candara", 0, fontSize); //Custom page font

    private JLabel labelPickDate = new JLabel("Reports:", JLabel.TRAILING);
    private CJPanel calendarPanel;
    private JCalendar fieldCalendar = new JCalendar();
    private String chosenDate;

    private CJPanel subPanel;
    private CJButton btnBack = new CJButton("Back",font);
    private CJButton btnDateReport = new CJButton("By Date",font);
    private CJButton btnTotalReport = new CJButton("By Branch",font);
    private CJButton btnProductReport = new CJButton("By Product",font);

    private ProductReportPage productReportPage;

    public ReportsPage(Controller controller) throws HeadlessException {

        this.controller = controller;
        this.productReportPage = new ProductReportPage(controller);

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
        theLayout.putConstraint(SpringLayout.WEST, subPanel, 10, SpringLayout.WEST, this);
        theLayout.putConstraint(SpringLayout.EAST, subPanel, 10, SpringLayout.EAST, this);

        subPanel.add(btnBack);
        subPanel.add(btnDateReport);
        subPanel.add(btnTotalReport);
        subPanel.add(btnProductReport);

        SpringUtilities.makeCompactGrid(subPanel,1,4,10,10,10,5);

        add(subPanel);

        btnBack.addActionListener(e -> {
            setVisible(false);
            controller.showBranchPage();
        });

        btnDateReport.addActionListener(e ->  {
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

                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
        });

        btnTotalReport.addActionListener(e -> {
            // TODO: 17/10/2018 open report of branch sales.

            try
            {
                if (Desktop.isDesktopSupported()) {
                    PrintStream out = new PrintStream(Controller.echoSocket.getOutputStream());
                    Gson gson = new Gson();
                    DateReportDto dateReportDto = new DateReportDto("salesReportByBranch", null,
                            AuthService.getInstance().getCurrentEmployee().getBranchNumber());

                    out.println(gson.toJson(dateReportDto));

                    DataInputStream in = new DataInputStream(Controller.echoSocket.getInputStream());
                    String response = in.readLine();

                    if (response.equals("true")) {

                        Desktop.getDesktop().open(new File(".idea/dataSources/wordReportsFiles/" + "Branch #" +
                                AuthService.getInstance().getCurrentEmployee().getBranchNumber() + " Sales Report" + ".docx"));
                    } else {
                        JOptionPane.showMessageDialog(new JFrame(), "report can't be created!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

            } catch (IOException e1) {
                e1.printStackTrace();
            }

        });

        btnProductReport.addActionListener(e -> {
            this.productReportPage.setVisible(true);
            JOptionPane.showMessageDialog(new JFrame(), "Click on product from table to show detailed report", "Report by Product", JOptionPane.INFORMATION_MESSAGE);
        });
    }


    @Override
    protected void paintComponent(Graphics g) { g.drawImage(controller.getInnerPageImage(),0,0,null); }
}
