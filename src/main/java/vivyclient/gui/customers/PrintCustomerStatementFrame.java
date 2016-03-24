package vivyclient.gui.customers;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import vivyclient.Client;
import vivyclient.exception.UserInputException;
import vivyclient.gui.common.PanelUtil;
import vivyclient.model.Address;
import vivyclient.model.Customer;
import vivyclient.print.PrinterGateway;
import vivyclient.print.customer.CustomerStatement;
import vivyclient.print.customer.CustomerStatementPrinter;
import vivyclient.util.DialogueUtil;
import vivyclient.util.Settings;

public class PrintCustomerStatementFrame extends JInternalFrame {
   private static final String NAME = "PrintCustomerStatementFrame";
   boolean isNew;
   private Customer customer;
   private JButton bPrintPreview;
   private JTextField tFromDate;
   private JLabel lAddress;
   private JButton bPrintPackingSlip;
   private JLabel lFromDate;
   private JTextField tCustomer;
   private JSeparator jSeparator1;
   private JLabel lTitle;
   private JComboBox cAddress;
   private JLabel lCustomer;

   public PrintCustomerStatementFrame(Customer customer) throws Exception {
      this.customer = customer;
      this.initComponents();
      this.populateLists();
      this.refresh();
      this.setSize(new Dimension(Settings.getWidth("PrintCustomerStatementFrame"), Settings.getHeight("PrintCustomerStatementFrame")));
      this.setLocation(Settings.getXPos("PrintCustomerStatementFrame"), Settings.getYPos("PrintCustomerStatementFrame"));
   }

   private void populateLists() throws Exception {
      Address[] addresses = new Address[this.customer.getAddressLinkCount()];

      for(int i = 0; i < addresses.length; ++i) {
         addresses[i] = this.customer.getAddressLink(i).getAddress();
      }

      this.cAddress.setModel(new DefaultComboBoxModel(addresses));
   }

   private void refresh() throws Exception {
      this.tCustomer.setText(this.customer.toString());
      PanelUtil.setComboSelection(this.cAddress, this.customer.getDefaultBillingAddress());
      this.tFromDate.setText("");
   }

   private void print(boolean preview) {
      try {
         this.setCursor(new Cursor(3));
         CustomerStatement e = new CustomerStatement();
         e.setCustomer(this.customer);
         e.setStatementFromDate(PanelUtil.getValidatedCalendarRead(this.tFromDate, "From Date", true));
         e.setAddress((Address)PanelUtil.getValidatedComboItem(this.cAddress, "Address", true));
         e.populate();
         CustomerStatementPrinter statementPrinter = new CustomerStatementPrinter(e);
         PrinterGateway.handlePrintRequest(statementPrinter, preview);
      } catch (UserInputException var8) {
         DialogueUtil.handleUserInputException(var8, "Incorrect Input", "Incorrect Input", Client.getMainFrame());
      } catch (Exception var9) {
         DialogueUtil.handleException(var9, "Error printing customer statement", "Print Error", true, Client.getMainFrame());
      } finally {
         this.setCursor(new Cursor(0));
      }

   }

   private void initComponents() {
      this.lTitle = new JLabel();
      this.lCustomer = new JLabel();
      this.tCustomer = new JTextField();
      this.lAddress = new JLabel();
      this.cAddress = new JComboBox();
      this.lFromDate = new JLabel();
      this.tFromDate = new JTextField();
      this.bPrintPackingSlip = new JButton();
      this.bPrintPreview = new JButton();
      this.jSeparator1 = new JSeparator();
      this.getContentPane().setLayout(new GridBagLayout());
      this.setClosable(true);
      this.setResizable(true);
      this.addInternalFrameListener(new InternalFrameListener() {
         public void internalFrameOpened(InternalFrameEvent evt) {
         }

         public void internalFrameClosing(InternalFrameEvent evt) {
            PrintCustomerStatementFrame.this.formInternalFrameClosing(evt);
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
      this.lTitle.setText("Customer Statement");
      GridBagConstraints gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.insets = new Insets(0, 5, 3, 5);
      gridBagConstraints.anchor = 18;
      gridBagConstraints.weightx = 1.0D;
      this.getContentPane().add(this.lTitle, gridBagConstraints);
      this.lCustomer.setText("Customer:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 2;
      gridBagConstraints.insets = new Insets(0, 5, 0, 5);
      gridBagConstraints.anchor = 12;
      this.getContentPane().add(this.lCustomer, gridBagConstraints);
      this.tCustomer.setEditable(false);
      this.tCustomer.setText("jTextField1");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 2;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.insets = new Insets(0, 0, 0, 5);
      gridBagConstraints.weightx = 1.0D;
      this.getContentPane().add(this.tCustomer, gridBagConstraints);
      this.lAddress.setText("Address:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 3;
      gridBagConstraints.insets = new Insets(2, 5, 0, 5);
      gridBagConstraints.anchor = 12;
      this.getContentPane().add(this.lAddress, gridBagConstraints);
      this.cAddress.setMinimumSize(new Dimension(31, 20));
      this.cAddress.setPreferredSize(new Dimension(31, 20));
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 3;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.insets = new Insets(2, 0, 0, 5);
      gridBagConstraints.weightx = 1.0D;
      this.getContentPane().add(this.cAddress, gridBagConstraints);
      this.lFromDate.setText("From Date:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 4;
      gridBagConstraints.insets = new Insets(2, 5, 0, 5);
      gridBagConstraints.anchor = 12;
      this.getContentPane().add(this.lFromDate, gridBagConstraints);
      this.tFromDate.setText("jTextField1");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 4;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.insets = new Insets(2, 0, 0, 5);
      gridBagConstraints.weightx = 1.0D;
      this.getContentPane().add(this.tFromDate, gridBagConstraints);
      this.bPrintPackingSlip.setText("Print");
      this.bPrintPackingSlip.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent evt) {
            PrintCustomerStatementFrame.this.bPrintPackingSlipMouseClicked(evt);
         }
      });
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 5;
      gridBagConstraints.insets = new Insets(15, 0, 0, 5);
      gridBagConstraints.anchor = 12;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.weighty = 1.0D;
      this.getContentPane().add(this.bPrintPackingSlip, gridBagConstraints);
      this.bPrintPreview.setText("Print Preview");
      this.bPrintPreview.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent evt) {
            PrintCustomerStatementFrame.this.bPrintPreviewMouseClicked(evt);
         }
      });
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 2;
      gridBagConstraints.gridy = 5;
      gridBagConstraints.insets = new Insets(15, 0, 0, 5);
      gridBagConstraints.anchor = 12;
      gridBagConstraints.weighty = 1.0D;
      this.getContentPane().add(this.bPrintPreview, gridBagConstraints);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.ipadx = 3;
      gridBagConstraints.insets = new Insets(0, 0, 8, 0);
      gridBagConstraints.anchor = 11;
      gridBagConstraints.weightx = 1.0D;
      this.getContentPane().add(this.jSeparator1, gridBagConstraints);
      this.pack();
   }

   private void bPrintPreviewMouseClicked(MouseEvent evt) {
      this.print(true);
   }

   private void bPrintPackingSlipMouseClicked(MouseEvent evt) {
      this.print(false);
   }

   private void formInternalFrameClosing(InternalFrameEvent evt) {
      Settings.setWidth("PrintCustomerStatementFrame", (int)this.getSize().getWidth());
      Settings.setHeight("PrintCustomerStatementFrame", (int)this.getSize().getHeight());
      Settings.setXPos("PrintCustomerStatementFrame", this.getLocation().x);
      Settings.setYPos("PrintCustomerStatementFrame", this.getLocation().y);
      this.setVisible(false);
      this.dispose();
   }
}
