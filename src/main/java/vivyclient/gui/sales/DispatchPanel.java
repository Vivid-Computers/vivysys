package vivyclient.gui.sales;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import vivyclient.Client;
import vivyclient.data.TransactionContainer;
import vivyclient.gui.common.EditPanel;
import vivyclient.gui.common.PanelUtil;
import vivyclient.gui.sales.DispatchDetailTableModel;
import vivyclient.model.BaseModel;
import vivyclient.model.Dispatch;
import vivyclient.model.DispatchDetail;
import vivyclient.model.SaleStatus;
import vivyclient.model.Supplier;
import vivyclient.model.SupplierType;
import vivyclient.model.searchMap.SupplierSearchMapFactory;
import vivyclient.shared.Constants;
import vivyclient.util.ViewUtil;

public class DispatchPanel extends JPanel implements EditPanel {
   private Dispatch dispatch;
   private boolean isNew = true;
   private DispatchDetailTableModel tableModel;
   private JLabel lDeliveredBy;
   private JTextField tAttention;
   private JLabel lTrackingNo;
   private JScrollPane itemsScrollPane;
   private JTextField tDate;
   private JTable itemsTable;
   private JTextField tObjectId;
   private JLabel lDate;
   private JLabel lObjectId;
   private JLabel lAttention;
   private JPanel makeupPanel;
   private JTextField tTrackingNo;
   private JSeparator jSeparator1;
   private JTextField tSale;
   private JComboBox cDeliveredBy;
   private JButton bChangeSale;
   private JLabel lTitle;
   private JLabel lSale;

   public DispatchPanel(Dispatch dispatch) throws Exception {
      this.dispatch = dispatch;
      if(dispatch.getSale() != null && dispatch.getSale().getStatus() == null) {
         dispatch.getSale().populate((TransactionContainer)null);
      }

      this.initComponents();
      this.populateLists();
      this.setValues();
      this.tableModel = new DispatchDetailTableModel(dispatch, this.itemsTable);
      this.itemsTable.setModel(this.tableModel);
   }

   private void populateLists() throws Exception {
      Supplier criteria = new Supplier();
      criteria.setSupplierType(SupplierType.DELIVERY_SUPPLIER_TYPE);
      List deliverySuppliers = Supplier.findAll(criteria, SupplierSearchMapFactory.getSupplierTypeSearchMap(), (TransactionContainer)null);
      this.cDeliveredBy.setModel(new DefaultComboBoxModel(deliverySuppliers.toArray()));
   }

   private void setValues() throws Exception {
      this.isNew = !this.dispatch.exists((TransactionContainer)null);
      if(this.isNew) {
         this.tDate.setText(ViewUtil.calendarDisplay(Calendar.getInstance()));
         this.tObjectId.setText("");
         this.tTrackingNo.setText("");
         if(this.dispatch.getSale() != null) {
            this.tSale.setText(this.dispatch.getSale().toString());
            this.tAttention.setText(this.dispatch.getSale().getDeliveryAddress().getDeliveryName());
         } else {
            this.tSale.setText("");
            this.tAttention.setText("");
         }
      } else {
         this.tObjectId.setEditable(false);
         this.tDate.setText(ViewUtil.calendarDisplay(this.dispatch.getDispatchDate()));
         this.tObjectId.setText(ViewUtil.intDisplay(this.dispatch.getObjectId(), false));
         this.tTrackingNo.setText(this.dispatch.getTrackingId());
         this.tSale.setText(this.dispatch.getSale().toString());
         this.tAttention.setText(this.dispatch.getAttention());
      }

   }

   public boolean exit() throws Exception {
      return true;
   }

   public void refresh() throws Exception {
      this.setValues();
   }

   public boolean save() throws Exception {
      boolean insert = this.isNew;
      this.dispatch.setSupplier((Supplier)PanelUtil.getValidatedComboItem(this.cDeliveredBy, "Deliverer", true));
      this.dispatch.setDispatchDate(PanelUtil.getValidatedCalendarRead(this.tDate, "Dispatch Date", true));
      this.dispatch.setTrackingId(this.tTrackingNo.getText());
      this.dispatch.setAttention(this.tAttention.getText());
      if(this.isNew) {
         PanelUtil.validatedObjectIdRead(this.dispatch, this.tObjectId);
      }

      ArrayList dispatched = new ArrayList();
      boolean complete = true;

      for(int i = 0; i < this.tableModel.getDispatchItems().size(); ++i) {
         if(((DispatchDetail)this.tableModel.getDispatchItems().get(i)).getQuantity().compareTo(Constants.ZERO_BIG_DECIMAL) > 0) {
            dispatched.add(this.tableModel.getDispatchItems().get(i));
            BigDecimal dispatchedQuantity = ((DispatchDetail)this.tableModel.getDispatchItems().get(i)).getQuantity().add(((DispatchDetail)this.tableModel.getDispatchItems().get(i)).getShippedSaleDetail().getDispatchedQuantity());
            if(dispatchedQuantity.compareTo(((DispatchDetail)this.tableModel.getDispatchItems().get(i)).getShippedSaleDetail().getQuantity()) < 0) {
               complete = false;
            }
         } else {
            complete = false;
         }
      }

      this.dispatch.setDispatchContent(dispatched);
      this.dispatch.save((TransactionContainer)null);
      this.refresh();
      Client.showInfoMessage((insert?"New ":"") + "Dispatch saved");
      if(complete && !this.dispatch.getSale().getStatus().equals(SaleStatus.ORDER_DISPATCHED_STATUS) && 0 == JOptionPane.showConfirmDialog(Client.getMainFrame(), "All items for this Sale have been dispatched - do you want the Sale Status updated?", "Dispatched Sale Status", 0, 3)) {
         this.dispatch.getSale().setStatus(SaleStatus.ORDER_DISPATCHED_STATUS);
         this.dispatch.getSale().setSaleDispatchedDate(Calendar.getInstance());
         this.dispatch.getSale().save((TransactionContainer)null);
         Client.showInfoMessage("Sale updated to \'Dispatched\'");
      }

      return insert;
   }

   public BaseModel getModel() {
      return this.dispatch;
   }

   public Dispatch getDispatchForPrint() throws Exception {
      Dispatch dispatchClone = new Dispatch();
      dispatchClone.setObjectId(this.dispatch.getObjectId());
      dispatchClone.setDispatchDate(this.dispatch.getDispatchDate());
      dispatchClone.setSale(this.dispatch.getSale());
      dispatchClone.setSupplier(this.dispatch.getSupplier());
      dispatchClone.setTrackingId(this.dispatch.getTrackingId());
      dispatchClone.setAttention(this.dispatch.getAttention());
      dispatchClone.setDispatchContent(this.tableModel.getDispatchItems());
      dispatchClone.getSale().populate((TransactionContainer)null);
      dispatchClone.getSupplier().populate((TransactionContainer)null);
      dispatchClone.getSale().getBillingAddress().populate((TransactionContainer)null);
      dispatchClone.getSale().getDeliveryAddress().populate((TransactionContainer)null);
      dispatchClone.getSale().getCustomer().populate((TransactionContainer)null);
      return dispatchClone;
   }

   private void initComponents() {
      this.lTitle = new JLabel();
      this.jSeparator1 = new JSeparator();
      this.tTrackingNo = new JTextField();
      this.lTrackingNo = new JLabel();
      this.lDeliveredBy = new JLabel();
      this.cDeliveredBy = new JComboBox();
      this.makeupPanel = new JPanel();
      this.itemsScrollPane = new JScrollPane();
      this.itemsTable = new JTable();
      this.lDate = new JLabel();
      this.tDate = new JTextField();
      this.lObjectId = new JLabel();
      this.tObjectId = new JTextField();
      this.lSale = new JLabel();
      this.tSale = new JTextField();
      this.bChangeSale = new JButton();
      this.lAttention = new JLabel();
      this.tAttention = new JTextField();
      this.setLayout(new GridBagLayout());
      this.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
      this.lTitle.setFont(new Font("Arial", 1, 14));
      this.lTitle.setForeground(SystemColor.activeCaption);
      this.lTitle.setText("Dispatch");
      GridBagConstraints gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.anchor = 18;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(0, 5, 3, 5);
      this.add(this.lTitle, gridBagConstraints);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.ipadx = 3;
      gridBagConstraints.anchor = 11;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(0, 0, 8, 0);
      this.add(this.jSeparator1, gridBagConstraints);
      this.tTrackingNo.setText("jTextField1");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 3;
      gridBagConstraints.fill = 2;
      gridBagConstraints.insets = new Insets(2, 0, 0, 0);
      gridBagConstraints.weightx = 1.0D;
      this.add(this.tTrackingNo, gridBagConstraints);
      this.lTrackingNo.setText("Tracking No:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 3;
      gridBagConstraints.insets = new Insets(2, 0, 0, 5);
      gridBagConstraints.anchor = 13;
      this.add(this.lTrackingNo, gridBagConstraints);
      this.lDeliveredBy.setText("Delivered By:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.insets = new Insets(0, 0, 0, 5);
      gridBagConstraints.anchor = 13;
      this.add(this.lDeliveredBy, gridBagConstraints);
      this.cDeliveredBy.setMinimumSize(new Dimension(31, 20));
      this.cDeliveredBy.setPreferredSize(new Dimension(31, 20));
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      this.add(this.cDeliveredBy, gridBagConstraints);
      this.makeupPanel.setLayout(new GridBagLayout());
      this.makeupPanel.setBorder(new TitledBorder("Content"));
      this.itemsTable.setModel(new DefaultTableModel(new Object[0][], new String[0]));
      this.itemsScrollPane.setViewportView(this.itemsTable);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.gridheight = 0;
      gridBagConstraints.fill = 1;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.weighty = 1.0D;
      this.makeupPanel.add(this.itemsScrollPane, gridBagConstraints);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 6;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 1;
      gridBagConstraints.insets = new Insets(2, 0, 0, 0);
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.weighty = 1.0D;
      this.add(this.makeupPanel, gridBagConstraints);
      this.lDate.setText("Date:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 2;
      gridBagConstraints.gridy = 2;
      gridBagConstraints.insets = new Insets(0, 10, 0, 5);
      gridBagConstraints.anchor = 13;
      this.add(this.lDate, gridBagConstraints);
      this.tDate.setText("jTextField1");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 3;
      gridBagConstraints.gridy = 2;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      this.add(this.tDate, gridBagConstraints);
      this.lObjectId.setText("Id:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 2;
      gridBagConstraints.gridy = 3;
      gridBagConstraints.insets = new Insets(2, 10, 0, 5);
      gridBagConstraints.anchor = 13;
      this.add(this.lObjectId, gridBagConstraints);
      this.tObjectId.setText("jTextField1");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 3;
      gridBagConstraints.gridy = 3;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(2, 0, 0, 0);
      this.add(this.tObjectId, gridBagConstraints);
      this.lSale.setText("Sale:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 4;
      gridBagConstraints.insets = new Insets(2, 0, 0, 5);
      gridBagConstraints.anchor = 13;
      this.add(this.lSale, gridBagConstraints);
      this.tSale.setEditable(false);
      this.tSale.setText("jTextField1");
      this.tSale.setEnabled(false);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 4;
      gridBagConstraints.fill = 2;
      gridBagConstraints.insets = new Insets(2, 0, 0, 0);
      this.add(this.tSale, gridBagConstraints);
      this.bChangeSale.setText("...");
      this.bChangeSale.setMargin(new Insets(2, 2, 2, 2));
      this.bChangeSale.setMaximumSize(new Dimension(20, 20));
      this.bChangeSale.setMinimumSize(new Dimension(20, 20));
      this.bChangeSale.setPreferredSize(new Dimension(20, 20));
      this.bChangeSale.setEnabled(false);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 2;
      gridBagConstraints.gridy = 4;
      gridBagConstraints.anchor = 17;
      gridBagConstraints.insets = new Insets(2, 2, 0, 0);
      this.add(this.bChangeSale, gridBagConstraints);
      this.lAttention.setText("Attention:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 5;
      gridBagConstraints.insets = new Insets(2, 0, 0, 5);
      gridBagConstraints.anchor = 13;
      this.add(this.lAttention, gridBagConstraints);
      this.tAttention.setText("jTextField1");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 5;
      gridBagConstraints.fill = 2;
      gridBagConstraints.insets = new Insets(2, 0, 0, 0);
      gridBagConstraints.weightx = 1.0D;
      this.add(this.tAttention, gridBagConstraints);
   }
}
