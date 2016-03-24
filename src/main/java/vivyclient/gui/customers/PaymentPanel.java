package vivyclient.gui.customers;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import vivyclient.data.TransactionContainer;
import vivyclient.gui.common.EditPanel;
import vivyclient.gui.common.PanelUtil;
import vivyclient.model.BaseModel;
import vivyclient.model.Payment;
import vivyclient.model.PaymentMethodType;
import vivyclient.util.ViewUtil;

public class PaymentPanel extends JPanel implements EditPanel {
   private Payment payment;
   private boolean isNew;
   private JLabel lPaymentMethod;
   private JTextField tPaymentDetails3;
   private JTextField tDate;
   private JTextField tCustomer;
   private JTextField tObjectId;
   private JTextField tPaymentDetails1;
   private JLabel lDate;
   private JLabel lObjectId;
   private JTextField tPaymentDetails2;
   private JSeparator jSeparator1;
   private JPanel jPanel1;
   private JLabel lAmount;
   private JLabel lTitle;
   private JTextField tAmount;
   private JComboBox cPaymentMethod;
   private JLabel lCustomer;

   public PaymentPanel(Payment payment) throws Exception {
      this.payment = payment;
      this.initComponents();
      this.initialiseLists();
      this.refresh();
   }

   private void initialiseLists() throws Exception {
      ArrayList paymentMethodTypes = new ArrayList();
      paymentMethodTypes.add((Object)null);
      paymentMethodTypes.addAll(PaymentMethodType.cachedFindAll(new PaymentMethodType()));
      paymentMethodTypes.remove(PaymentMethodType.ON_ACCOUNT_PAYMENT_METHOD_TYPE);
      this.cPaymentMethod.setModel(new DefaultComboBoxModel(paymentMethodTypes.toArray()));
   }

   public boolean exit() throws Exception {
      return false;
   }

   public BaseModel getModel() {
      return this.payment;
   }

   public void refresh() throws Exception {
      this.isNew = !this.payment.exists((TransactionContainer)null);
      if(this.isNew) {
         this.tObjectId.setText("");
      } else {
         this.tObjectId.setText(String.valueOf(this.payment.getObjectId()));
         this.tObjectId.setEditable(false);
      }

      this.tCustomer.setText(this.payment.getCustomer() != null?this.payment.getCustomer().getDefaultDeliveryName():"");
      this.tAmount.setText(ViewUtil.currencyDisplay(this.payment.getAmount()));
      PanelUtil.setComboSelection(this.cPaymentMethod, this.payment.getMethod());
      this.tDate.setText(ViewUtil.calendarDisplay(this.payment.getDate() != null?this.payment.getDate():Calendar.getInstance()));
      this.tPaymentDetails1.setText(this.payment.getDetails1());
      this.tPaymentDetails2.setText(this.payment.getDetails2());
      this.tPaymentDetails3.setText(this.payment.getDetails3());
   }

   public boolean save() throws Exception {
      boolean wasNew = this.isNew;
      if(this.isNew) {
         PanelUtil.validatedObjectIdRead(this.payment, this.tObjectId);
      }

      this.payment.setAmount(PanelUtil.getValidatedCurrencyValue(this.tAmount, "Payment Amount", true, 1));
      this.payment.setDate(PanelUtil.getValidatedCalendarRead(this.tDate, "Payment Date", true));
      this.payment.setMethod((PaymentMethodType)PanelUtil.getValidatedComboItem(this.cPaymentMethod, "Payment Method", true));
      this.payment.setDetails1(this.tPaymentDetails1.getText().trim());
      this.payment.setDetails2(this.tPaymentDetails2.getText().trim());
      this.payment.setDetails3(this.tPaymentDetails3.getText().trim());
      this.payment.save((TransactionContainer)null);
      return wasNew;
   }

   private void initComponents() {
      this.lTitle = new JLabel();
      this.jSeparator1 = new JSeparator();
      this.lPaymentMethod = new JLabel();
      this.cPaymentMethod = new JComboBox();
      this.lCustomer = new JLabel();
      this.tCustomer = new JTextField();
      this.lAmount = new JLabel();
      this.tAmount = new JTextField();
      this.lDate = new JLabel();
      this.tDate = new JTextField();
      this.lObjectId = new JLabel();
      this.tObjectId = new JTextField();
      this.jPanel1 = new JPanel();
      this.tPaymentDetails1 = new JTextField();
      this.tPaymentDetails2 = new JTextField();
      this.tPaymentDetails3 = new JTextField();
      this.setLayout(new GridBagLayout());
      this.setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));
      this.lTitle.setFont(new Font("Arial", 1, 14));
      this.lTitle.setForeground(SystemColor.activeCaption);
      this.lTitle.setText("Payment");
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
      this.lPaymentMethod.setText("Method:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 4;
      gridBagConstraints.insets = new Insets(2, 0, 0, 5);
      gridBagConstraints.anchor = 13;
      this.add(this.lPaymentMethod, gridBagConstraints);
      this.cPaymentMethod.setMinimumSize(new Dimension(31, 20));
      this.cPaymentMethod.setPreferredSize(new Dimension(31, 20));
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 4;
      gridBagConstraints.fill = 2;
      gridBagConstraints.insets = new Insets(2, 0, 0, 0);
      gridBagConstraints.weightx = 1.0D;
      this.add(this.cPaymentMethod, gridBagConstraints);
      this.lCustomer.setText("Customer:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 2;
      gridBagConstraints.anchor = 13;
      gridBagConstraints.insets = new Insets(2, 0, 0, 5);
      this.add(this.lCustomer, gridBagConstraints);
      this.tCustomer.setText("jTextField1");
      this.tCustomer.setEnabled(false);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 2;
      gridBagConstraints.fill = 2;
      gridBagConstraints.insets = new Insets(2, 0, 0, 0);
      this.add(this.tCustomer, gridBagConstraints);
      this.lAmount.setText("Amount:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 3;
      gridBagConstraints.anchor = 13;
      gridBagConstraints.insets = new Insets(2, 0, 0, 5);
      this.add(this.lAmount, gridBagConstraints);
      this.tAmount.setText("jTextField1");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 3;
      gridBagConstraints.fill = 2;
      gridBagConstraints.insets = new Insets(2, 0, 0, 0);
      this.add(this.tAmount, gridBagConstraints);
      this.lDate.setText("Date:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 2;
      gridBagConstraints.gridy = 3;
      gridBagConstraints.insets = new Insets(2, 10, 0, 5);
      gridBagConstraints.anchor = 13;
      this.add(this.lDate, gridBagConstraints);
      this.tDate.setText("jTextField1");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 3;
      gridBagConstraints.gridy = 3;
      gridBagConstraints.fill = 2;
      gridBagConstraints.insets = new Insets(2, 0, 0, 0);
      gridBagConstraints.weightx = 1.0D;
      this.add(this.tDate, gridBagConstraints);
      this.lObjectId.setText("Id:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.insets = new Insets(0, 10, 0, 5);
      gridBagConstraints.anchor = 13;
      this.add(this.lObjectId, gridBagConstraints);
      this.tObjectId.setText("jTextField1");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      this.add(this.tObjectId, gridBagConstraints);
      this.jPanel1.setLayout(new GridBagLayout());
      this.jPanel1.setBorder(new TitledBorder("Payment Details"));
      this.tPaymentDetails1.setText("jTextField1");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.insets = new Insets(1, 5, 0, 5);
      gridBagConstraints.weightx = 1.0D;
      this.jPanel1.add(this.tPaymentDetails1, gridBagConstraints);
      this.tPaymentDetails2.setText("jTextField2");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.fill = 2;
      gridBagConstraints.insets = new Insets(2, 5, 0, 5);
      gridBagConstraints.weightx = 1.0D;
      this.jPanel1.add(this.tPaymentDetails2, gridBagConstraints);
      this.tPaymentDetails3.setText("jTextField3");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 2;
      gridBagConstraints.fill = 2;
      gridBagConstraints.insets = new Insets(2, 5, 5, 5);
      gridBagConstraints.anchor = 11;
      gridBagConstraints.weightx = 1.0D;
      this.jPanel1.add(this.tPaymentDetails3, gridBagConstraints);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 5;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.insets = new Insets(5, 0, 0, 0);
      gridBagConstraints.anchor = 11;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.weighty = 1.0D;
      this.add(this.jPanel1, gridBagConstraints);
   }
}
