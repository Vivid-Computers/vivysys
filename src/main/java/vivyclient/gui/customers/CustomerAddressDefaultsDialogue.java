package vivyclient.gui.customers;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import vivyclient.model.CustomerAddress;

public class CustomerAddressDefaultsDialogue extends JDialog {
   private CustomerAddress customerAddress;
   private boolean defaultSelected = false;
   private JCheckBox cDelivery;
   private JLabel jLabel1;
   private JCheckBox cBilling;
   private JButton jButton1;

   public CustomerAddressDefaultsDialogue(Frame parent, boolean modal, CustomerAddress customerAddress) {
      super(parent, modal);
      this.customerAddress = customerAddress;
      this.initComponents();
      this.refresh();
      int x = parent.getX() + (parent.getWidth() - this.getWidth()) / 2;
      int y = parent.getY() + (parent.getHeight() - this.getHeight()) / 2;
      this.setLocation(x, y);
   }

   private void refresh() {
      this.cBilling.setSelected(this.customerAddress.getCustomer().getDefaultBillingAddress() == null);
      this.cDelivery.setSelected(this.customerAddress.getCustomer().getDefaultDeliveryAddress() == null);
   }

   private void initComponents() {
      this.jLabel1 = new JLabel();
      this.cDelivery = new JCheckBox();
      this.cBilling = new JCheckBox();
      this.jButton1 = new JButton();
      this.getContentPane().setLayout(new GridBagLayout());
      this.setTitle("Set Default Customer Addresses");
      this.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent evt) {
            CustomerAddressDefaultsDialogue.this.closeDialog(evt);
         }
      });
      this.jLabel1.setText("Make this the default Address for:");
      GridBagConstraints gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.insets = new Insets(10, 5, 0, 5);
      gridBagConstraints.anchor = 17;
      gridBagConstraints.weightx = 1.0D;
      this.getContentPane().add(this.jLabel1, gridBagConstraints);
      this.cDelivery.setText("Delivery");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 2;
      gridBagConstraints.insets = new Insets(0, 15, 0, 0);
      gridBagConstraints.anchor = 17;
      gridBagConstraints.weightx = 1.0D;
      this.getContentPane().add(this.cDelivery, gridBagConstraints);
      this.cBilling.setText("Billing");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.insets = new Insets(0, 15, 0, 0);
      gridBagConstraints.anchor = 17;
      gridBagConstraints.weightx = 1.0D;
      this.getContentPane().add(this.cBilling, gridBagConstraints);
      this.jButton1.setText("Okay");
      this.jButton1.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent evt) {
            CustomerAddressDefaultsDialogue.this.jButton1MouseClicked(evt);
         }
      });
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 3;
      gridBagConstraints.insets = new Insets(5, 0, 10, 0);
      gridBagConstraints.anchor = 11;
      gridBagConstraints.weighty = 1.0D;
      this.getContentPane().add(this.jButton1, gridBagConstraints);
      this.pack();
   }

   private void jButton1MouseClicked(MouseEvent evt) {
      if(this.cBilling.isSelected()) {
         this.customerAddress.getCustomer().setDefaultBillingAddress(this.customerAddress.getAddress());
         this.defaultSelected = true;
      }

      if(this.cDelivery.isSelected()) {
         this.customerAddress.getCustomer().setDefaultDeliveryAddress(this.customerAddress.getAddress());
         this.defaultSelected = true;
      }

      this.closeDialog((WindowEvent)null);
   }

   private void closeDialog(WindowEvent evt) {
      this.setVisible(false);
      this.dispose();
   }

   public boolean getDefaultSelected() {
      return this.defaultSelected;
   }
}
