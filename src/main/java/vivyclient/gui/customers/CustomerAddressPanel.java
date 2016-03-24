package vivyclient.gui.customers;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;
import vivyclient.Client;
import vivyclient.data.TransactionContainer;
import vivyclient.gui.common.AddressPanel;
import vivyclient.gui.common.EditPanel;
import vivyclient.gui.customers.CustomerAddressDefaultsDialogue;
import vivyclient.model.BaseModel;
import vivyclient.model.Customer;
import vivyclient.model.CustomerAddress;

public class CustomerAddressPanel extends JPanel implements EditPanel {
   private CustomerAddress customerAddress;
   private AddressPanel addressPanel;
   private JSeparator jSeparator1;
   private JLabel lTitle;

   public CustomerAddressPanel(CustomerAddress customerAddress) throws Exception {
      this.customerAddress = customerAddress;
      this.addressPanel = new AddressPanel(customerAddress.getAddress());
      this.initComponents();
      GridBagConstraints gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.fill = 2;
      gridBagConstraints.anchor = 11;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.weighty = 1.0D;
      this.add(this.addressPanel, gridBagConstraints);
      this.refresh();
   }

   public boolean exit() throws Exception {
      return false;
   }

   public BaseModel getModel() {
      return this.customerAddress;
   }

   public void refresh() throws Exception {
      this.addressPanel.refresh();
   }

   public boolean save() throws Exception {
      this.readUserValues();
      boolean newAddress = !this.customerAddress.exists((TransactionContainer)null);
      Customer customer = this.customerAddress.getCustomer();
      if(newAddress) {
         customer.addAddressLink(this.customerAddress);
      }

      customer.save((TransactionContainer)null);
      if(newAddress && (customer.getDefaultBillingAddress() == null || customer.getDefaultDeliveryAddress() == null)) {
         CustomerAddressDefaultsDialogue dialogue = new CustomerAddressDefaultsDialogue(Client.getMainFrame(), true, this.customerAddress);
         dialogue.setVisible(true);
         if(dialogue.getDefaultSelected()) {
            customer.save((TransactionContainer)null);
         }
      }

      this.refresh();
      return newAddress;
   }

   public void readUserValues() throws Exception {
      this.addressPanel.readUserValues();
   }

   private void initComponents() {
      this.lTitle = new JLabel();
      this.jSeparator1 = new JSeparator();
      this.setLayout(new GridBagLayout());
      this.setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));
      this.lTitle.setFont(new Font("Arial", 1, 14));
      this.lTitle.setForeground(SystemColor.activeCaption);
      this.lTitle.setText("Customer Address");
      GridBagConstraints gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.insets = new Insets(0, 5, 3, 5);
      gridBagConstraints.anchor = 18;
      gridBagConstraints.weightx = 1.0D;
      this.add(this.lTitle, gridBagConstraints);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.ipadx = 3;
      gridBagConstraints.insets = new Insets(0, 0, 8, 0);
      gridBagConstraints.anchor = 11;
      gridBagConstraints.weightx = 1.0D;
      this.add(this.jSeparator1, gridBagConstraints);
   }
}
