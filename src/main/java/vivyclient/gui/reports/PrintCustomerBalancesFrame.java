package vivyclient.gui.reports;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.border.TitledBorder;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import vivyclient.Client;
import vivyclient.print.PrinterGateway;
import vivyclient.print.reports.CustomerBalances;
import vivyclient.print.reports.CustomerBalancesReportPrinter;
import vivyclient.util.DialogueUtil;
import vivyclient.util.Settings;

public class PrintCustomerBalancesFrame extends JInternalFrame {
   private static final String NAME = "PrintCustomerBalancesReportFrame";
   private JButton bPrintPreview;
   private JRadioButton rNameOrder;
   private JRadioButton rBalanceCustomers;
   private JRadioButton rAllCustomers;
   private ButtonGroup viewByButtonGroup;
   private JButton bPrintPackingSlip;
   private JRadioButton rBalanceOrder;
   private JRadioButton rOverdueOrder;
   private JPanel orderPanel;
   private JPanel viewPanel;
   private JRadioButton rOverdueCustomers;
   private JSeparator jSeparator1;
   private JLabel lTitle;
   private ButtonGroup orderByButtonGroup;

   public PrintCustomerBalancesFrame() {
      this.initComponents();
      this.refresh();
      this.setSize(new Dimension(Settings.getWidth("PrintCustomerBalancesReportFrame"), Settings.getHeight("PrintCustomerBalancesReportFrame")));
      this.setLocation(Settings.getXPos("PrintCustomerBalancesReportFrame"), Settings.getYPos("PrintCustomerBalancesReportFrame"));
   }

   private void refresh() {
      this.rAllCustomers.setActionCommand("all");
      this.rOverdueCustomers.setActionCommand("overdue");
      this.rBalanceCustomers.setActionCommand("debt");
      this.rNameOrder.setActionCommand("nameOrder");
      this.rOverdueOrder.setActionCommand("overdueOrder");
      this.rBalanceOrder.setActionCommand("balanceOrder");
      this.rAllCustomers.setSelected(true);
      this.rNameOrder.setSelected(true);
   }

   private void print(boolean preview) {
      try {
         this.setCursor(new Cursor(3));
         CustomerBalances e = new CustomerBalances();
         e.setShowType(this.viewByButtonGroup.getSelection().getActionCommand());
         e.setOrderType(this.orderByButtonGroup.getSelection().getActionCommand());
         e.populate();
         CustomerBalancesReportPrinter reportPrinter = new CustomerBalancesReportPrinter(e);
         PrinterGateway.handlePrintRequest(reportPrinter, preview);
      } catch (Exception var7) {
         DialogueUtil.handleException(var7, "Error printing report", "Print Error", true, Client.getMainFrame());
      } finally {
         this.setCursor(new Cursor(0));
      }

   }

   private void initComponents() {
      this.viewByButtonGroup = new ButtonGroup();
      this.orderByButtonGroup = new ButtonGroup();
      this.lTitle = new JLabel();
      this.bPrintPackingSlip = new JButton();
      this.bPrintPreview = new JButton();
      this.jSeparator1 = new JSeparator();
      this.viewPanel = new JPanel();
      this.rAllCustomers = new JRadioButton();
      this.rOverdueCustomers = new JRadioButton();
      this.rBalanceCustomers = new JRadioButton();
      this.orderPanel = new JPanel();
      this.rNameOrder = new JRadioButton();
      this.rOverdueOrder = new JRadioButton();
      this.rBalanceOrder = new JRadioButton();
      this.getContentPane().setLayout(new GridBagLayout());
      this.setClosable(true);
      this.setResizable(true);
      this.addInternalFrameListener(new InternalFrameListener() {
         public void internalFrameOpened(InternalFrameEvent evt) {
         }

         public void internalFrameClosing(InternalFrameEvent evt) {
            PrintCustomerBalancesFrame.this.formInternalFrameClosing(evt);
         }

         public void internalFrameClosed(InternalFrameEvent evt) {
         }

         public void internalFrameIconified(InternalFrameEvent evt) {
         }

         public void internalFrameDeiconified(InternalFrameEvent evt) {
         }

         public void internalFrameActivated(InternalFrameEvent evt) {
         }

         public void internalFrameDeactivated(InternalFrameEvent evt) {
         }
      });
      this.lTitle.setFont(new Font("Arial", 1, 14));
      this.lTitle.setForeground(SystemColor.activeCaption);
      this.lTitle.setText("Customer Balances Report");
      GridBagConstraints gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.anchor = 18;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(0, 5, 3, 5);
      this.getContentPane().add(this.lTitle, gridBagConstraints);
      this.bPrintPackingSlip.setText("Print");
      this.bPrintPackingSlip.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent evt) {
            PrintCustomerBalancesFrame.this.bPrintPackingSlipMouseClicked(evt);
         }
      });
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 4;
      gridBagConstraints.anchor = 12;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.weighty = 1.0D;
      gridBagConstraints.insets = new Insets(15, 0, 0, 5);
      this.getContentPane().add(this.bPrintPackingSlip, gridBagConstraints);
      this.bPrintPreview.setText("Print Preview");
      this.bPrintPreview.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent evt) {
            PrintCustomerBalancesFrame.this.bPrintPreviewMouseClicked(evt);
         }
      });
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 4;
      gridBagConstraints.anchor = 12;
      gridBagConstraints.weighty = 1.0D;
      gridBagConstraints.insets = new Insets(15, 0, 0, 5);
      this.getContentPane().add(this.bPrintPreview, gridBagConstraints);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.ipadx = 3;
      gridBagConstraints.anchor = 11;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(0, 0, 8, 0);
      this.getContentPane().add(this.jSeparator1, gridBagConstraints);
      this.viewPanel.setLayout(new GridBagLayout());
      this.viewPanel.setBorder(new TitledBorder("View"));
      this.rAllCustomers.setFont(new Font("Dialog", 1, 10));
      this.rAllCustomers.setText("All Customers");
      this.viewByButtonGroup.add(this.rAllCustomers);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.anchor = 17;
      gridBagConstraints.weightx = 1.0D;
      this.viewPanel.add(this.rAllCustomers, gridBagConstraints);
      this.rOverdueCustomers.setFont(new Font("Dialog", 1, 10));
      this.rOverdueCustomers.setText("Customers with a Overdue Balance");
      this.viewByButtonGroup.add(this.rOverdueCustomers);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.anchor = 17;
      gridBagConstraints.weightx = 1.0D;
      this.viewPanel.add(this.rOverdueCustomers, gridBagConstraints);
      this.rBalanceCustomers.setFont(new Font("Dialog", 1, 10));
      this.rBalanceCustomers.setText("Customers with an Outstanding Balance");
      this.viewByButtonGroup.add(this.rBalanceCustomers);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 2;
      gridBagConstraints.anchor = 17;
      gridBagConstraints.weightx = 1.0D;
      this.viewPanel.add(this.rBalanceCustomers, gridBagConstraints);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 2;
      this.getContentPane().add(this.viewPanel, gridBagConstraints);
      this.orderPanel.setLayout(new GridBagLayout());
      this.orderPanel.setBorder(new TitledBorder("Order By"));
      this.rNameOrder.setFont(new Font("Dialog", 1, 10));
      this.rNameOrder.setText("Name");
      this.orderByButtonGroup.add(this.rNameOrder);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.anchor = 17;
      gridBagConstraints.weightx = 1.0D;
      this.orderPanel.add(this.rNameOrder, gridBagConstraints);
      this.rOverdueOrder.setFont(new Font("Dialog", 1, 10));
      this.rOverdueOrder.setText("Overdue Balance");
      this.orderByButtonGroup.add(this.rOverdueOrder);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.anchor = 17;
      gridBagConstraints.weightx = 1.0D;
      this.orderPanel.add(this.rOverdueOrder, gridBagConstraints);
      this.rBalanceOrder.setFont(new Font("Dialog", 1, 10));
      this.rBalanceOrder.setText("Balance");
      this.orderByButtonGroup.add(this.rBalanceOrder);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 2;
      gridBagConstraints.anchor = 17;
      gridBagConstraints.weightx = 1.0D;
      this.orderPanel.add(this.rBalanceOrder, gridBagConstraints);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 3;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 2;
      this.getContentPane().add(this.orderPanel, gridBagConstraints);
      this.pack();
   }

   private void bPrintPreviewMouseClicked(MouseEvent evt) {
      this.print(true);
   }

   private void bPrintPackingSlipMouseClicked(MouseEvent evt) {
      this.print(false);
   }

   private void formInternalFrameClosing(InternalFrameEvent evt) {
      Settings.setWidth("PrintCustomerBalancesReportFrame", (int)this.getSize().getWidth());
      Settings.setHeight("PrintCustomerBalancesReportFrame", (int)this.getSize().getHeight());
      Settings.setXPos("PrintCustomerBalancesReportFrame", this.getLocation().x);
      Settings.setYPos("PrintCustomerBalancesReportFrame", this.getLocation().y);
      this.setVisible(false);
      this.dispose();
   }
}
