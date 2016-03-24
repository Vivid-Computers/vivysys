package vivyclient.gui.sales;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import vivyclient.Client;
import vivyclient.data.TransactionContainer;
import vivyclient.exception.AppRuntimeException;
import vivyclient.exception.UserInputException;
import vivyclient.gui.common.EditPanel;
import vivyclient.gui.common.PanelUtil;
import vivyclient.gui.common.TextEntryDialogue;
import vivyclient.gui.customers.CustomerSelectorDialogue;
import vivyclient.gui.sales.SaleDetailTableModel;
import vivyclient.gui.sales.SaleLinePriceEditor;
import vivyclient.gui.sales.SaleLineProductEditor;
import vivyclient.model.Address;
import vivyclient.model.BaseModel;
import vivyclient.model.Customer;
import vivyclient.model.DurationType;
import vivyclient.model.PaymentMethodType;
import vivyclient.model.Region;
import vivyclient.model.Sale;
import vivyclient.model.SaleDetail;
import vivyclient.model.SaleStatus;
import vivyclient.model.Site;
import vivyclient.shared.Constants;
import vivyclient.util.DialogueUtil;
import vivyclient.util.ViewUtil;

public class SalePanel extends JPanel implements TableModelListener, EditPanel {
   private Sale sale;
   private SaleDetailTableModel saleLineTableModel;
   private boolean isNew;
   private final int INITIAL_ROWHEIGHT = 20;
   private JSeparator jSeparator2;
   private JLabel lDelivery;
   private JLabel lFreight;
   private JLabel lSite;
   private JTextField tSaletDate;
   private JLabel lPaymentMethod;
   private JButton bEditSaleComments;
   private JComboBox cPaymentTermsType;
   private JLabel lSaleComments;
   private JLabel lSaleTerms;
   private JLabel lBillTo;
   private JLabel lSubTotal;
   private JPanel customerPanel;
   private JLabel lName;
   private JComboBox cBillingAddress;
   private JPanel paymentsPanel;
   private JTextField tSubTotal;
   private JLabel lStatus;
   private JComboBox cDeliveryAddress;
   private JButton bSelectCustomer;
   private JTextField tFreightCost;
   private JComboBox cStatus;
   private JComboBox cSite;
   private JButton bEditCustomerComments;
   private JLabel lSaleDate;
   private JCheckBox cHasGST;
   private JScrollPane saleDetailScrollPane;
   private JLabel jLabel2;
   private JTextField tPaymentDetails3;
   private JTextField tGST;
   private JLabel lSaleId;
   private JLabel lTotal;
   private JPanel commentsPanel;
   private JTextField tObjectId;
   private JLabel lCustRef;
   private JLabel lCustomerComments;
   private JTextField tPaymentDetails1;
   private JTextField tCustomerName;
   private JTextField tCustomerRef;
   private JLabel lPaymentDue;
   private JTextField tTotal;
   private JTextField tPaymentDetails2;
   private JTextField tPaymentDue;
   private JLabel lHasGST;
   private JSeparator jSeparator1;
   private JTextField tSaleComments;
   private JTable saleDetailTable;
   private JTextField tCustomerComments;
   private JTextField tPaymentTermsMultiplierValue;
   private JLabel lTitle;
   private JComboBox cPaymentMethod;
   private JPanel paymentTermsPanel;

   public SalePanel(Sale sale) throws Exception {
      this.sale = sale;
      this.isNew = !sale.exists((TransactionContainer)null);
      if(!this.isNew) {
         sale.deepPopulate();
      }

      this.initComponents();
      TableColumn productColumn = this.saleDetailTable.getColumn(this.getSaleLineTableModel().getColumnName(0));
      SaleLineProductEditor productEditor = new SaleLineProductEditor();
      productColumn.setCellEditor(productEditor);
      TableColumn priceColumn = this.saleDetailTable.getColumn(this.getSaleLineTableModel().getColumnName(4));
      SaleLinePriceEditor priceEditor = new SaleLinePriceEditor();
      priceColumn.setCellEditor(priceEditor);
      this.saleDetailTable.setRowHeight(20);
      this.initialiseLists();
      this.refresh();
   }

   private void initialiseLists() throws Exception {
      this.cStatus.setModel(new DefaultComboBoxModel(SaleStatus.cachedFindAll(new SaleStatus()).toArray()));
      List sites = Site.cachedFindAll(new Site());
      Site[] display = new Site[sites.size()];
      int paymentDurationTypes = 0;

      for(int n = 1; paymentDurationTypes < sites.size(); ++paymentDurationTypes) {
         Site site = (Site)sites.get(paymentDurationTypes);
         if(site.getObjectId() != 0) {
            display[n++] = site;
         }
      }

      this.cSite.setModel(new DefaultComboBoxModel(display));
      PanelUtil.populateComboList(this.cPaymentMethod, new PaymentMethodType(), true);
      ArrayList var6 = new ArrayList();
      var6.add((Object)null);
      var6.addAll(DurationType.cachedFindAll(new DurationType()));
      var6.remove(DurationType.LIFETIME_DURATION_TYPE);
      this.cPaymentTermsType.setModel(new DefaultComboBoxModel(var6.toArray()));
   }

   public void refresh() throws Exception {
      this.isNew = !this.sale.exists((TransactionContainer)null);
      PanelUtil.setComboSelection(this.cStatus, this.sale.getStatus());
      if(this.sale.getSite() == null) {
         this.cSite.setSelectedIndex(0);
      } else {
         PanelUtil.setComboSelection(this.cSite, this.sale.getSite());
      }

      if(this.isNew) {
         this.tObjectId.setEditable(true);
         this.tObjectId.setText("");
      } else {
         this.tObjectId.setEditable(false);
         this.tObjectId.setText(String.valueOf(this.sale.getObjectId()));
      }

      this.tSaletDate.setText(ViewUtil.calendarDisplay(this.sale.getSaleDate()));
      if(this.sale.getCustomer() != null) {
         this.customerChange();
         this.tCustomerName.setText(this.sale.getCustomer().toString());
      } else {
         this.tCustomerName.setText("");
      }

      this.tCustomerRef.setText(this.sale.getCustref());
      this.customerChange();
      PanelUtil.setComboSelection(this.cDeliveryAddress, this.sale.getDeliveryAddress());
      PanelUtil.setComboSelection(this.cBillingAddress, this.sale.getBillingAddress());
      PanelUtil.setComboSelection(this.cPaymentMethod, this.sale.getPaymentMethod());
      this.tPaymentDetails1.setText(this.sale.getPaymentDetails1());
      this.tPaymentDetails2.setText(this.sale.getPaymentDetails2());
      this.tPaymentDetails3.setText(this.sale.getPaymentDetails3());
      if(this.sale.getPaymentDurationType() != null) {
         this.tPaymentTermsMultiplierValue.setText(String.valueOf(this.sale.getPaymentTermsMultiplier()));
      } else {
         this.tPaymentTermsMultiplierValue.setText("");
      }

      PanelUtil.setComboSelection(this.cPaymentTermsType, this.sale.getPaymentDurationType());
      this.tCustomerComments.setText(this.sale.getCustomerComments());
      this.tSaleComments.setText(this.sale.getSaleComments());
      this.cHasGST.setSelected(this.sale.getHasGST());
      if(this.sale.getFreightCost() != null && !this.sale.getFreightCost().equals(Constants.UNDEFINED_FREIGHT_COST)) {
         this.tFreightCost.setText(ViewUtil.currencyDisplay(this.sale.getFreightCost()));
      } else {
         this.tFreightCost.setText("");
      }

      this.paymentTermsChange();
      this.cDeliveryAddressActionPerformed((ActionEvent)null);
      this.setTotalsValues();
   }

   public boolean save() throws Exception {
      this.sale.setStatus((SaleStatus)this.cStatus.getSelectedItem());
      this.sale.setSite((Site)this.cSite.getSelectedItem());
      if(this.isNew) {
         PanelUtil.validatedObjectIdRead(this.sale, this.tObjectId);
      }

      this.sale.setSaleDate(PanelUtil.getValidatedCalendarRead(this.tSaletDate, "Sale Date", true));
      if(this.sale.getCustomer() == null) {
         throw new UserInputException("Select Customer", this.bSelectCustomer);
      } else {
         this.sale.setCustref(this.tCustomerRef.getText());
         this.sale.setDeliveryAddress((Address)PanelUtil.getValidatedComboItem(this.cDeliveryAddress, "Delivery Address", true));
         this.sale.setBillingAddress((Address)PanelUtil.getValidatedComboItem(this.cBillingAddress, "Billing Address", true));
         this.sale.setPaymentMethod((PaymentMethodType)PanelUtil.getValidatedComboItem(this.cPaymentMethod, "Payment Method", true));
         this.tPaymentDetails1.setText(this.sale.getPaymentDetails1());
         this.tPaymentDetails2.setText(this.sale.getPaymentDetails2());
         this.tPaymentDetails3.setText(this.sale.getPaymentDetails3());
         this.sale.setPaymentDurationType((DurationType)PanelUtil.getValidatedComboItem(this.cPaymentTermsType, "Payment Terms Type", false));
         this.sale.setPaymentTermsMultiplier(PanelUtil.getValidatedIntValue(this.tPaymentTermsMultiplierValue, "Payment Terms Multiplier", this.sale.getPaymentDurationType() != null, 1));
         this.sale.setCustomerComments(this.tCustomerComments.getText());
         this.sale.setSaleComments(this.tSaleComments.getText());
         this.sale.setFreightCost(PanelUtil.getValidatedCurrencyValue(this.tFreightCost, "Freight Cost", true, 1));
         this.sale.setHasGST(this.cHasGST.isSelected());
         this.validateSaleLines();
         boolean inserted = this.isNew;
         this.sale.save((TransactionContainer)null);
         this.refresh();
         return inserted;
      }
   }

   private void validateSaleLines() throws Exception {
      for(int i = 0; i < this.sale.getSaleLineCount(); ++i) {
         SaleDetail line = this.sale.getSaleLine(i);
         if(line.getProduct() == null) {
            throw new UserInputException("A Sale Line must have a Product", this.saleDetailTable);
         }

         if(line.getUnitPrice() == null) {
            throw new UserInputException("A Sale Line must have a Unit Price", this.saleDetailTable);
         }
      }

   }

   private void setTotalsValues() throws Exception {
      this.tSubTotal.setText(ViewUtil.currencyDisplay(this.sale.getSubTotal()));
      this.tGST.setText(ViewUtil.currencyDisplay(this.sale.getGSTCost()));
      this.tTotal.setText(ViewUtil.currencyDisplay(this.sale.getTotalCost()));
   }

   private void customerChange() throws Exception {
      if(this.sale.getCustomer() != null) {
         Address[] addresses = new Address[this.sale.getCustomer().getAddressLinkCount()];

         for(int i = 0; i < addresses.length; ++i) {
            addresses[i] = this.sale.getCustomer().getAddressLink(i).getAddress();
         }

         this.cDeliveryAddress.setModel(new DefaultComboBoxModel(addresses));
         this.cBillingAddress.setModel(new DefaultComboBoxModel(addresses));
         PanelUtil.setComboSelection(this.cBillingAddress, this.sale.getCustomer().getDefaultBillingAddress());
         PanelUtil.setComboSelection(this.cDeliveryAddress, this.sale.getCustomer().getDefaultDeliveryAddress());
      } else {
         this.cDeliveryAddress.setModel(new DefaultComboBoxModel(new Object[0]));
         this.cBillingAddress.setModel(new DefaultComboBoxModel(new Object[0]));
      }

   }

   public boolean exit() throws Exception {
      return false;
   }

   public BaseModel getModel() {
      return this.sale;
   }

   private void initComponents() {
      this.lTitle = new JLabel();
      this.jSeparator1 = new JSeparator();
      this.lSaleDate = new JLabel();
      this.tSaletDate = new JTextField();
      this.lSaleId = new JLabel();
      this.tObjectId = new JTextField();
      this.lSite = new JLabel();
      this.cSite = new JComboBox();
      this.customerPanel = new JPanel();
      this.tCustomerName = new JTextField();
      this.lDelivery = new JLabel();
      this.lBillTo = new JLabel();
      this.lName = new JLabel();
      this.lCustRef = new JLabel();
      this.tCustomerRef = new JTextField();
      this.bSelectCustomer = new JButton();
      this.cBillingAddress = new JComboBox();
      this.cDeliveryAddress = new JComboBox();
      this.paymentsPanel = new JPanel();
      this.lPaymentMethod = new JLabel();
      this.cPaymentMethod = new JComboBox();
      this.jLabel2 = new JLabel();
      this.tPaymentDetails1 = new JTextField();
      this.tPaymentDetails2 = new JTextField();
      this.tPaymentDetails3 = new JTextField();
      this.paymentTermsPanel = new JPanel();
      this.lSaleTerms = new JLabel();
      this.tPaymentTermsMultiplierValue = new JTextField();
      this.lPaymentDue = new JLabel();
      this.tPaymentDue = new JTextField();
      this.cPaymentTermsType = new JComboBox();
      this.commentsPanel = new JPanel();
      this.lCustomerComments = new JLabel();
      this.tCustomerComments = new JTextField();
      this.lSaleComments = new JLabel();
      this.tSaleComments = new JTextField();
      this.bEditCustomerComments = new JButton();
      this.bEditSaleComments = new JButton();
      this.lFreight = new JLabel();
      this.tFreightCost = new JTextField();
      this.lSubTotal = new JLabel();
      this.tSubTotal = new JTextField();
      this.lHasGST = new JLabel();
      this.cHasGST = new JCheckBox();
      this.lTotal = new JLabel();
      this.tGST = new JTextField();
      this.tTotal = new JTextField();
      this.saleDetailScrollPane = new JScrollPane();
      this.saleDetailTable = new JTable();
      this.jSeparator2 = new JSeparator();
      this.cStatus = new JComboBox();
      this.lStatus = new JLabel();
      this.setLayout(new GridBagLayout());
      this.setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));
      this.lTitle.setFont(new Font("Arial", 1, 14));
      this.lTitle.setForeground(SystemColor.activeCaption);
      this.lTitle.setText("Sale");
      GridBagConstraints gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.anchor = 18;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(0, 5, 3, 5);
      this.add(this.lTitle, gridBagConstraints);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.ipadx = 3;
      gridBagConstraints.anchor = 11;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(0, 0, 8, 0);
      this.add(this.jSeparator1, gridBagConstraints);
      this.lSaleDate.setText("Date:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 4;
      gridBagConstraints.gridy = 3;
      gridBagConstraints.insets = new Insets(2, 0, 0, 5);
      this.add(this.lSaleDate, gridBagConstraints);
      this.tSaletDate.setText("jTextField1");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 5;
      gridBagConstraints.gridy = 3;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(2, 0, 0, 10);
      this.add(this.tSaletDate, gridBagConstraints);
      this.lSaleId.setText("Sale Id:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 4;
      gridBagConstraints.gridy = 2;
      gridBagConstraints.insets = new Insets(2, 0, 0, 5);
      this.add(this.lSaleId, gridBagConstraints);
      this.tObjectId.setText("jTextField1");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 5;
      gridBagConstraints.gridy = 2;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(2, 0, 0, 0);
      this.add(this.tObjectId, gridBagConstraints);
      this.lSite.setText("Site:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 2;
      gridBagConstraints.gridy = 2;
      gridBagConstraints.insets = new Insets(2, 0, 0, 5);
      this.add(this.lSite, gridBagConstraints);
      this.cSite.setMinimumSize(new Dimension(31, 20));
      this.cSite.setPreferredSize(new Dimension(31, 20));
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 3;
      gridBagConstraints.gridy = 2;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(2, 0, 0, 10);
      this.add(this.cSite, gridBagConstraints);
      this.customerPanel.setLayout(new GridBagLayout());
      this.customerPanel.setBorder(new TitledBorder("Customer"));
      this.tCustomerName.setEditable(false);
      this.tCustomerName.setText("Customer Name");
      this.tCustomerName.setEnabled(false);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(2, 0, 0, 2);
      this.customerPanel.add(this.tCustomerName, gridBagConstraints);
      this.lDelivery.setText("Deliver To:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 3;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.insets = new Insets(2, 0, 0, 5);
      this.customerPanel.add(this.lDelivery, gridBagConstraints);
      this.lBillTo.setText("Bill To:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 3;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.insets = new Insets(2, 0, 0, 5);
      this.customerPanel.add(this.lBillTo, gridBagConstraints);
      this.lName.setText("Name:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.insets = new Insets(2, 0, 0, 5);
      this.customerPanel.add(this.lName, gridBagConstraints);
      this.lCustRef.setText("Ref:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.insets = new Insets(2, 0, 0, 5);
      this.customerPanel.add(this.lCustRef, gridBagConstraints);
      this.tCustomerRef.setText("jTextField1");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.gridwidth = 2;
      gridBagConstraints.fill = 2;
      gridBagConstraints.insets = new Insets(2, 0, 0, 10);
      this.customerPanel.add(this.tCustomerRef, gridBagConstraints);
      this.bSelectCustomer.setText("...");
      this.bSelectCustomer.setToolTipText("Click to select or add Customer");
      this.bSelectCustomer.setMaximumSize(new Dimension(20, 20));
      this.bSelectCustomer.setMinimumSize(new Dimension(20, 20));
      this.bSelectCustomer.setPreferredSize(new Dimension(20, 20));
      this.bSelectCustomer.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent evt) {
            SalePanel.this.bSelectCustomerMouseClicked(evt);
         }
      });
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 2;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.insets = new Insets(2, 0, 0, 10);
      this.customerPanel.add(this.bSelectCustomer, gridBagConstraints);
      this.cBillingAddress.setMinimumSize(new Dimension(31, 20));
      this.cBillingAddress.setPreferredSize(new Dimension(31, 20));
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 4;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(0, 0, 0, 10);
      this.customerPanel.add(this.cBillingAddress, gridBagConstraints);
      this.cDeliveryAddress.setMinimumSize(new Dimension(31, 20));
      this.cDeliveryAddress.setPreferredSize(new Dimension(31, 20));
      this.cDeliveryAddress.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            SalePanel.this.cDeliveryAddressActionPerformed(evt);
         }
      });
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 4;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(2, 0, 0, 10);
      this.customerPanel.add(this.cDeliveryAddress, gridBagConstraints);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 4;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(2, 0, 0, 0);
      this.add(this.customerPanel, gridBagConstraints);
      this.paymentsPanel.setLayout(new GridBagLayout());
      this.paymentsPanel.setBorder(new TitledBorder("Payment"));
      this.lPaymentMethod.setText("Method:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.insets = new Insets(0, 0, 0, 5);
      this.paymentsPanel.add(this.lPaymentMethod, gridBagConstraints);
      this.cPaymentMethod.setMinimumSize(new Dimension(31, 20));
      this.cPaymentMethod.setPreferredSize(new Dimension(31, 20));
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(0, 0, 0, 10);
      this.paymentsPanel.add(this.cPaymentMethod, gridBagConstraints);
      this.jLabel2.setText("Details:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.insets = new Insets(0, 0, 0, 5);
      this.paymentsPanel.add(this.jLabel2, gridBagConstraints);
      this.tPaymentDetails1.setText("jTextField1");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(0, 0, 0, 2);
      this.paymentsPanel.add(this.tPaymentDetails1, gridBagConstraints);
      this.tPaymentDetails2.setText("jTextField2");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(0, 0, 0, 2);
      this.paymentsPanel.add(this.tPaymentDetails2, gridBagConstraints);
      this.tPaymentDetails3.setText("jTextField3");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      this.paymentsPanel.add(this.tPaymentDetails3, gridBagConstraints);
      this.paymentTermsPanel.setLayout(new GridBagLayout());
      this.lSaleTerms.setText("Terms:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.anchor = 12;
      gridBagConstraints.insets = new Insets(0, 0, 0, 5);
      this.paymentTermsPanel.add(this.lSaleTerms, gridBagConstraints);
      this.tPaymentTermsMultiplierValue.setText("jTextField1");
      this.tPaymentTermsMultiplierValue.addFocusListener(new FocusAdapter() {
         public void focusLost(FocusEvent evt) {
            SalePanel.this.tPaymentTermsMultiplierChange(evt);
         }
      });
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(0, 0, 0, 2);
      this.paymentTermsPanel.add(this.tPaymentTermsMultiplierValue, gridBagConstraints);
      this.lPaymentDue.setText("Payment Due:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 3;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.anchor = 12;
      gridBagConstraints.insets = new Insets(0, 0, 0, 5);
      this.paymentTermsPanel.add(this.lPaymentDue, gridBagConstraints);
      this.tPaymentDue.setEditable(false);
      this.tPaymentDue.setText("jTextField1");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 4;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      this.paymentTermsPanel.add(this.tPaymentDue, gridBagConstraints);
      this.cPaymentTermsType.setMinimumSize(new Dimension(31, 20));
      this.cPaymentTermsType.setPreferredSize(new Dimension(31, 20));
      this.cPaymentTermsType.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            SalePanel.this.cPaymentTermsTypeActionPerformed(evt);
         }
      });
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 2;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(0, 0, 0, 10);
      this.paymentTermsPanel.add(this.cPaymentTermsType, gridBagConstraints);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(3, 0, 0, 0);
      this.paymentsPanel.add(this.paymentTermsPanel, gridBagConstraints);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 5;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(2, 0, 0, 0);
      this.add(this.paymentsPanel, gridBagConstraints);
      this.commentsPanel.setLayout(new GridBagLayout());
      this.commentsPanel.setBorder(new TitledBorder("Comments"));
      this.lCustomerComments.setText("Customer\'s:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.insets = new Insets(2, 0, 0, 5);
      this.commentsPanel.add(this.lCustomerComments, gridBagConstraints);
      this.tCustomerComments.setText("jTextField1");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(2, 0, 0, 2);
      this.commentsPanel.add(this.tCustomerComments, gridBagConstraints);
      this.lSaleComments.setText("Sale\'s:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 3;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.insets = new Insets(2, 0, 0, 5);
      this.commentsPanel.add(this.lSaleComments, gridBagConstraints);
      this.tSaleComments.setText("jTextField2");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 4;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(2, 0, 0, 2);
      this.commentsPanel.add(this.tSaleComments, gridBagConstraints);
      this.bEditCustomerComments.setText("...");
      this.bEditCustomerComments.setToolTipText("Click to view");
      this.bEditCustomerComments.setMaximumSize(new Dimension(20, 20));
      this.bEditCustomerComments.setMinimumSize(new Dimension(20, 20));
      this.bEditCustomerComments.setPreferredSize(new Dimension(20, 20));
      this.bEditCustomerComments.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent evt) {
            SalePanel.this.bEditCustomerCommentsMouseClicked(evt);
         }
      });
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 2;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.insets = new Insets(2, 0, 0, 10);
      this.commentsPanel.add(this.bEditCustomerComments, gridBagConstraints);
      this.bEditSaleComments.setText("...");
      this.bEditSaleComments.setToolTipText("Click to view");
      this.bEditSaleComments.setMaximumSize(new Dimension(20, 20));
      this.bEditSaleComments.setMinimumSize(new Dimension(20, 20));
      this.bEditSaleComments.setPreferredSize(new Dimension(20, 20));
      this.bEditSaleComments.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent evt) {
            SalePanel.this.bEditSaleCommentsMouseClicked(evt);
         }
      });
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 5;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.insets = new Insets(2, 0, 0, 0);
      this.commentsPanel.add(this.bEditSaleComments, gridBagConstraints);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 6;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(2, 0, 0, 0);
      this.add(this.commentsPanel, gridBagConstraints);
      this.lFreight.setText("Freight:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 4;
      gridBagConstraints.gridy = 8;
      gridBagConstraints.insets = new Insets(2, 0, 0, 5);
      this.add(this.lFreight, gridBagConstraints);
      this.tFreightCost.setText("freight");
      this.tFreightCost.addFocusListener(new FocusAdapter() {
         public void focusLost(FocusEvent evt) {
            SalePanel.this.tFreightCostFocusLost(evt);
         }
      });
      this.tFreightCost.addVetoableChangeListener(new VetoableChangeListener() {
         public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
            SalePanel.this.tFreightCostVetoableChange(evt);
         }
      });
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 5;
      gridBagConstraints.gridy = 8;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(2, 0, 0, 0);
      this.add(this.tFreightCost, gridBagConstraints);
      this.lSubTotal.setText("SubTotal:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 4;
      gridBagConstraints.gridy = 9;
      gridBagConstraints.insets = new Insets(2, 0, 0, 5);
      this.add(this.lSubTotal, gridBagConstraints);
      this.tSubTotal.setEditable(false);
      this.tSubTotal.setText("subtotal");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 5;
      gridBagConstraints.gridy = 9;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(2, 0, 0, 0);
      this.add(this.tSubTotal, gridBagConstraints);
      this.lHasGST.setText("GST:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 4;
      gridBagConstraints.gridy = 10;
      gridBagConstraints.insets = new Insets(2, 0, 0, 5);
      this.add(this.lHasGST, gridBagConstraints);
      this.cHasGST.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent evt) {
            SalePanel.this.cHasGSTClicked(evt);
         }
      });
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 5;
      gridBagConstraints.gridy = 10;
      gridBagConstraints.anchor = 17;
      gridBagConstraints.insets = new Insets(2, 0, 0, 0);
      this.add(this.cHasGST, gridBagConstraints);
      this.lTotal.setText("Total:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 3;
      gridBagConstraints.gridy = 12;
      gridBagConstraints.gridwidth = 2;
      gridBagConstraints.anchor = 13;
      gridBagConstraints.insets = new Insets(0, 0, 0, 5);
      this.add(this.lTotal, gridBagConstraints);
      this.tGST.setEditable(false);
      this.tGST.setText("gst");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 6;
      gridBagConstraints.gridy = 10;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(2, 0, 0, 0);
      this.add(this.tGST, gridBagConstraints);
      this.tTotal.setEditable(false);
      this.tTotal.setText("total");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 5;
      gridBagConstraints.gridy = 12;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      this.add(this.tTotal, gridBagConstraints);
      this.saleDetailTable.setModel(this.getSaleLineTableModel());
      this.saleDetailScrollPane.setViewportView(this.saleDetailTable);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 7;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 1;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.weighty = 1.0D;
      gridBagConstraints.insets = new Insets(4, 0, 0, 0);
      this.add(this.saleDetailScrollPane, gridBagConstraints);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 11;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(2, 0, 2, 0);
      this.add(this.jSeparator2, gridBagConstraints);
      this.cStatus.setMinimumSize(new Dimension(31, 20));
      this.cStatus.setPreferredSize(new Dimension(31, 20));
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 2;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(2, 0, 0, 10);
      this.add(this.cStatus, gridBagConstraints);
      this.lStatus.setText("Status:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 2;
      gridBagConstraints.insets = new Insets(2, 0, 0, 5);
      this.add(this.lStatus, gridBagConstraints);
   }

   private void bEditSaleCommentsMouseClicked(MouseEvent evt) {
      String comments = TextEntryDialogue.getStringValue(this.tSaleComments.getText(), "Sale Comments");
      if(comments != null) {
         this.tSaleComments.setText(comments);
      }

   }

   private void bEditCustomerCommentsMouseClicked(MouseEvent evt) {
      String comments = TextEntryDialogue.getStringValue(this.tCustomerComments.getText(), "Customer Comments");
      if(comments != null) {
         this.tCustomerComments.setText(comments);
      }

   }

   private void cDeliveryAddressActionPerformed(ActionEvent evt) {
      if(this.isNew) {
         try {
            if(this.cDeliveryAddress.getSelectedItem() == null) {
               this.cHasGST.setSelected(true);
            } else {
               this.cHasGST.setSelected(!Region.INTERNATIONAL_REGION.equals(((Address)this.cDeliveryAddress.getSelectedItem()).getRegion()));
            }

            this.sale.setHasGST(this.cHasGST.isSelected());
            this.setTotalsValues();
         } catch (Exception var3) {
            DialogueUtil.handleException(var3, "Error setting Sale total values", "Error", true, Client.getMainFrame());
         }
      }

   }

   private void tFreightCostFocusLost(FocusEvent evt) {
      try {
         this.sale.setFreightCost(PanelUtil.getValidatedCurrencyValue(this.tFreightCost, "Freight Cost", true, 1));
      } catch (Exception var4) {
         this.sale.setFreightCost((BigDecimal)null);
      }

      try {
         this.setTotalsValues();
      } catch (Exception var3) {
         DialogueUtil.handleException(var3, "Error calculating Sale\'s total values", "ERROR", true, this.tFreightCost);
      }

   }

   private void tFreightCostVetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
   }

   private void bSelectCustomerMouseClicked(MouseEvent evt) {
      try {
         this.setCursor(new Cursor(3));
         Customer e = CustomerSelectorDialogue.getUserSelectedCustomer();
         if(e != null) {
            this.sale.setCustomer(e);
            this.tCustomerName.setText(e.toString());
            this.customerChange();
         }
      } catch (Exception var6) {
         DialogueUtil.handleException(var6, "Error Selecting Customer", "ERROR", true, Client.getMainFrame());
      } finally {
         this.setCursor(new Cursor(0));
      }

   }

   private void cPaymentTermsTypeActionPerformed(ActionEvent evt) {
      this.paymentTermsChange();
   }

   private void tPaymentTermsMultiplierChange(FocusEvent evt) {
      this.paymentTermsChange();
   }

   private void paymentTermsChange() {
      if(this.cPaymentTermsType.getSelectedItem() != null && this.tPaymentTermsMultiplierValue.getText().trim().length() != 0) {
         Calendar dueDate = null;

         int multiplier;
         try {
            dueDate = PanelUtil.getValidatedCalendarRead(this.tSaletDate, "Sale Date", true);
            multiplier = PanelUtil.getValidatedIntValue(this.tPaymentTermsMultiplierValue, "Payment Terms", false, 1);
         } catch (Exception var4) {
            this.tPaymentDue.setText("");
            return;
         }

         dueDate.setLenient(true);
         DurationType durationType = (DurationType)this.cPaymentTermsType.getSelectedItem();
         if(durationType.equals(DurationType.DAYS_DURATION_TYPE)) {
            dueDate.add(5, multiplier);
         } else if(durationType.equals(DurationType.MONTHS_DURATION_TYPE)) {
            dueDate.add(2, multiplier);
         } else if(durationType.equals(DurationType.YEARS_DURATION_TYPE)) {
            dueDate.add(1, multiplier);
         } else {
            if(!durationType.equals(DurationType.BILLING_MONTH_DURATION_TYPE)) {
               throw new AppRuntimeException("Invalid Payment Duration Type: " + durationType.toString());
            }

            dueDate.add(2, multiplier);
            dueDate.set(5, 20);
         }

         this.tPaymentDue.setText(ViewUtil.calendarDisplay(dueDate));
      } else {
         this.tPaymentDue.setText("");
      }

   }

   private void cHasGSTClicked(MouseEvent evt) {
      try {
         this.sale.setHasGST(this.cHasGST.isSelected());
         this.setTotalsValues();
      } catch (Exception var3) {
         DialogueUtil.handleException(var3, "Error calculating Sale totals", "ERROR", true, Client.getMainFrame());
      }

   }

   public TableModel getSaleLineTableModel() {
      if(this.saleLineTableModel == null) {
         this.saleLineTableModel = new SaleDetailTableModel(this.sale, this.saleDetailTable);
         this.saleLineTableModel.addTableModelListener(this);
      }

      return this.saleLineTableModel;
   }

   public void setSaleLineTableModel(SaleDetailTableModel saleLineTableModel) {
      this.saleLineTableModel = saleLineTableModel;
   }

   public void tableChanged(TableModelEvent e) {
      try {
         this.setTotalsValues();
      } catch (Exception var3) {
         DialogueUtil.handleException(var3, "Error calculating Sale totals", "ERROR", true, Client.getMainFrame());
      }

   }
}
