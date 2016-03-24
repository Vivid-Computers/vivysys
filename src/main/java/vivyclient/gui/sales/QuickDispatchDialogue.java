package vivyclient.gui.sales;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import vivyclient.Client;
import vivyclient.data.TransactionContainer;
import vivyclient.exception.StaleModelException;
import vivyclient.exception.UserInputException;
import vivyclient.gui.common.PanelUtil;
import vivyclient.model.Dispatch;
import vivyclient.model.DispatchDetail;
import vivyclient.model.Sale;
import vivyclient.model.SaleStatus;
import vivyclient.model.Supplier;
import vivyclient.model.SupplierType;
import vivyclient.model.searchMap.SupplierSearchMapFactory;
import vivyclient.print.PrinterGateway;
import vivyclient.print.sale.DispatchPackingSlipPrinter;
import vivyclient.shared.Constants;
import vivyclient.util.DialogueUtil;
import vivyclient.util.Settings;
import vivyclient.util.ViewUtil;

public class QuickDispatchDialogue extends JDialog {
   private static final String NAME = "QuickDispatchDialogue";
   private Sale sale;
   private Dispatch dispatch;
   boolean isNew;
   private JLabel lDeliveredBy;
   private JTextField tAttention;
   private JLabel lTrackingNo;
   private JTextField tSaleName;
   private JButton bPrintPackingSlip;
   private JTextField tDate;
   private JTextField tObjectId;
   private JLabel lDate;
   private JLabel lObjectId;
   private JButton bSaveDispatch;
   private JLabel lAttention;
   private JTextField tTrackingNo;
   private JSeparator jSeparator1;
   private JPanel jPanel1;
   private JComboBox cDeliveredBy;
   private JLabel lTitle;
   private JLabel lSale;

   public QuickDispatchDialogue(Frame parent, boolean modal, Sale sale) throws Exception {
      super(parent, modal);
      this.sale = sale;
      this.dispatch = new Dispatch();
      this.dispatch.setDispatchDate(Calendar.getInstance());
      this.initComponents();
      this.populateLists();
      this.refresh();
      this.setSize(new Dimension(Settings.getWidth("QuickDispatchDialogue"), Settings.getHeight("QuickDispatchDialogue")));
      this.setLocation(Settings.getXPos("QuickDispatchDialogue"), Settings.getYPos("QuickDispatchDialogue"));
      this.setVisible(true);
   }

   private void populateLists() throws Exception {
      Supplier criteria = new Supplier();
      criteria.setSupplierType(SupplierType.DELIVERY_SUPPLIER_TYPE);
      List deliverySuppliers = Supplier.findAll(criteria, SupplierSearchMapFactory.getSupplierTypeSearchMap(), (TransactionContainer)null);
      this.cDeliveredBy.setModel(new DefaultComboBoxModel(deliverySuppliers.toArray()));
   }

   private void refresh() throws Exception {
      this.isNew = !this.dispatch.exists((TransactionContainer)null);
      PanelUtil.setComboSelection(this.cDeliveredBy, this.dispatch.getSupplier());
      this.tDate.setText(ViewUtil.calendarDisplay(this.dispatch.getDispatchDate()));
      this.tTrackingNo.setText(this.dispatch.getTrackingId());
      if(this.isNew) {
         this.tObjectId.setText("");
         this.tObjectId.setEditable(true);
         this.bSaveDispatch.setEnabled(true);
         this.bPrintPackingSlip.setEnabled(false);
         this.tAttention.setText(this.sale.getDeliveryAddress().getDeliveryName());
      } else {
         this.tObjectId.setText(String.valueOf(this.dispatch.getObjectId()));
         this.tObjectId.setEditable(false);
         this.bSaveDispatch.setEnabled(false);
         this.bPrintPackingSlip.setEnabled(true);
         this.tAttention.setText(this.dispatch.getAttention());
      }

      this.tSaleName.setText(this.sale.toString());
   }

   private void save() throws Exception {
      this.dispatch.setSupplier((Supplier)PanelUtil.getValidatedComboItem(this.cDeliveredBy, "Deliverer", true));
      this.dispatch.setDispatchDate(PanelUtil.getValidatedCalendarRead(this.tDate, "Dispatch Date", true));
      this.dispatch.setTrackingId(this.tTrackingNo.getText());
      this.dispatch.setAttention(this.tAttention.getText());
      this.dispatch.setSale(this.sale);
      if(this.isNew) {
         PanelUtil.validatedObjectIdRead(this.dispatch, this.tObjectId);
      }

      for(int i = 0; i < this.sale.getSaleLineCount(); ++i) {
         DispatchDetail detail = new DispatchDetail();
         detail.setShippedSaleDetail(this.sale.getSaleLine(i));
         detail.setQuantity(this.sale.getSaleLine(i).getQuantity());
         this.sale.getSaleLine(i).setDispatchedQuantity(Constants.ZERO_BIG_DECIMAL);
         this.dispatch.addDispatchContent(detail);
      }

      this.dispatch.save((TransactionContainer)null);
      this.refresh();
      Client.showInfoMessage("New Dispatch saved");
      if(!this.sale.getStatus().equals(SaleStatus.ORDER_DISPATCHED_STATUS) && 0 == JOptionPane.showConfirmDialog(Client.getMainFrame(), "All items for this Sale have been dispatched - do you want the Sale Status updated?", "Dispatched Sale Status", 0, 3)) {
         this.sale.setStatus(SaleStatus.ORDER_DISPATCHED_STATUS);
         this.sale.setSaleDispatchedDate(Calendar.getInstance());
         this.sale.save((TransactionContainer)null);
         Client.showInfoMessage("Sale updated to \'Dispatched\'");
      }

   }

   private void initComponents() {
      this.lTitle = new JLabel();
      this.jSeparator1 = new JSeparator();
      this.lDeliveredBy = new JLabel();
      this.cDeliveredBy = new JComboBox();
      this.lDate = new JLabel();
      this.tDate = new JTextField();
      this.lTrackingNo = new JLabel();
      this.tTrackingNo = new JTextField();
      this.lObjectId = new JLabel();
      this.tObjectId = new JTextField();
      this.lSale = new JLabel();
      this.tSaleName = new JTextField();
      this.jPanel1 = new JPanel();
      this.bPrintPackingSlip = new JButton();
      this.bSaveDispatch = new JButton();
      this.lAttention = new JLabel();
      this.tAttention = new JTextField();
      this.getContentPane().setLayout(new GridBagLayout());
      this.setTitle("Quick Dispatch");
      this.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent evt) {
            QuickDispatchDialogue.this.closeDialog(evt);
         }
      });
      this.lTitle.setFont(new Font("Arial", 1, 14));
      this.lTitle.setForeground(SystemColor.activeCaption);
      this.lTitle.setText("Quick-Dispatch");
      GridBagConstraints gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.anchor = 18;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(0, 5, 3, 5);
      this.getContentPane().add(this.lTitle, gridBagConstraints);
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
      this.lDeliveredBy.setText("Delivered By:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.anchor = 12;
      gridBagConstraints.insets = new Insets(0, 5, 0, 5);
      this.getContentPane().add(this.lDeliveredBy, gridBagConstraints);
      this.cDeliveredBy.setMinimumSize(new Dimension(31, 20));
      this.cDeliveredBy.setPreferredSize(new Dimension(31, 20));
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      this.getContentPane().add(this.cDeliveredBy, gridBagConstraints);
      this.lDate.setText("Date:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 2;
      gridBagConstraints.gridy = 2;
      gridBagConstraints.anchor = 12;
      gridBagConstraints.insets = new Insets(0, 10, 0, 5);
      this.getContentPane().add(this.lDate, gridBagConstraints);
      this.tDate.setText("jTextField1");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 3;
      gridBagConstraints.gridy = 2;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(0, 0, 0, 5);
      this.getContentPane().add(this.tDate, gridBagConstraints);
      this.lTrackingNo.setText("Tracking No:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 3;
      gridBagConstraints.anchor = 12;
      gridBagConstraints.insets = new Insets(0, 5, 0, 5);
      this.getContentPane().add(this.lTrackingNo, gridBagConstraints);
      this.tTrackingNo.setText("jTextField1");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 3;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(2, 0, 0, 0);
      this.getContentPane().add(this.tTrackingNo, gridBagConstraints);
      this.lObjectId.setText("Id:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 2;
      gridBagConstraints.gridy = 3;
      gridBagConstraints.anchor = 12;
      gridBagConstraints.insets = new Insets(2, 10, 0, 5);
      this.getContentPane().add(this.lObjectId, gridBagConstraints);
      this.tObjectId.setText("jTextField1");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 3;
      gridBagConstraints.gridy = 3;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(0, 0, 0, 5);
      this.getContentPane().add(this.tObjectId, gridBagConstraints);
      this.lSale.setText("Sale:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 4;
      gridBagConstraints.anchor = 12;
      gridBagConstraints.insets = new Insets(0, 5, 0, 5);
      this.getContentPane().add(this.lSale, gridBagConstraints);
      this.tSaleName.setEditable(false);
      this.tSaleName.setText("jTextField1");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 4;
      gridBagConstraints.gridwidth = 2;
      gridBagConstraints.fill = 2;
      gridBagConstraints.insets = new Insets(0, 0, 0, 5);
      gridBagConstraints.weightx = 1.0D;
      this.getContentPane().add(this.tSaleName, gridBagConstraints);
      this.jPanel1.setLayout(new GridBagLayout());
      this.bPrintPackingSlip.setText("Print");
      this.bPrintPackingSlip.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent evt) {
            QuickDispatchDialogue.this.bPrintPackingSlipMouseClicked(evt);
         }
      });
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.anchor = 12;
      gridBagConstraints.weighty = 1.0D;
      gridBagConstraints.insets = new Insets(15, 0, 0, 5);
      this.jPanel1.add(this.bPrintPackingSlip, gridBagConstraints);
      this.bSaveDispatch.setText("Save Dispatch");
      this.bSaveDispatch.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent evt) {
            QuickDispatchDialogue.this.bSaveDispatchMouseClicked(evt);
         }
      });
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.anchor = 12;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.weighty = 1.0D;
      gridBagConstraints.insets = new Insets(15, 0, 0, 5);
      this.jPanel1.add(this.bSaveDispatch, gridBagConstraints);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 6;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 1;
      gridBagConstraints.insets = new Insets(0, 5, 10, 5);
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.weighty = 1.0D;
      this.getContentPane().add(this.jPanel1, gridBagConstraints);
      this.lAttention.setText("Attention:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 5;
      gridBagConstraints.insets = new Insets(2, 0, 0, 5);
      gridBagConstraints.anchor = 13;
      this.getContentPane().add(this.lAttention, gridBagConstraints);
      this.tAttention.setText("jTextField1");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 5;
      gridBagConstraints.gridwidth = 2;
      gridBagConstraints.fill = 2;
      gridBagConstraints.insets = new Insets(2, 0, 0, 0);
      gridBagConstraints.weightx = 1.0D;
      this.getContentPane().add(this.tAttention, gridBagConstraints);
      this.pack();
   }

   private void bPrintPackingSlipMouseClicked(MouseEvent evt) {
      try {
         PrinterGateway.handlePrintRequest(new DispatchPackingSlipPrinter(this.dispatch), false);
      } catch (Exception var3) {
         DialogueUtil.handleException(var3, "Error Printing Dispatch", "Error", true, Client.getMainFrame());
      }

   }

   private void bSaveDispatchMouseClicked(MouseEvent evt) {
      try {
         this.save();
      } catch (StaleModelException var4) {
         int action = DialogueUtil.getActionForStaleModelException(var4, Client.getMainFrame());
      } catch (UserInputException var5) {
         DialogueUtil.handleUserInputException(var5, "", "Invalid Input", Client.getMainFrame());
      } catch (Exception var6) {
         DialogueUtil.handleException(var6, "Error Saving Dispatch", "Error", true, Client.getMainFrame());
      }

   }

   private void closeDialog(WindowEvent evt) {
      Settings.setWidth("QuickDispatchDialogue", (int)this.getSize().getWidth());
      Settings.setHeight("QuickDispatchDialogue", (int)this.getSize().getHeight());
      Settings.setXPos("QuickDispatchDialogue", this.getLocation().x);
      Settings.setYPos("QuickDispatchDialogue", this.getLocation().y);
      this.setVisible(false);
      this.dispose();
   }
}
