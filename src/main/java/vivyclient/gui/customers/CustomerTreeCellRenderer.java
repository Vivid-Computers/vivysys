package vivyclient.gui.customers;

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import vivyclient.model.AddressType;
import vivyclient.model.Customer;
import vivyclient.model.CustomerAddress;
import vivyclient.model.CustomerType;
import vivyclient.model.Payment;

public class CustomerTreeCellRenderer extends DefaultTreeCellRenderer {
   ImageIcon personCustomerIcon = new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/Person.gif"));
   ImageIcon organisationCustomerIcon = new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/Shop16.gif"));
   ImageIcon membersIcon = new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/People.gif"));
   ImageIcon addressesIcon = new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/Addresses.gif"));
   ImageIcon streetAddressIcon = new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/House.gif"));
   ImageIcon postalAddressIcon = new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/MailBox.gif"));
   ImageIcon mailAddressIcon = new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/Mail.gif"));
   ImageIcon paymentsIcon = new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/MoneyBag.gif"));
   ImageIcon paymentIcon = new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/MoneyPile.gif"));

   public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
      super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
      if(value instanceof DefaultMutableTreeNode) {
         Object object = ((DefaultMutableTreeNode)value).getUserObject();
         if(object instanceof Customer) {
            if(CustomerType.ORGANISATION_CUSTOMER_TYPE.equals(((Customer)object).getCustomerType())) {
               this.setIcon(this.organisationCustomerIcon);
            } else {
               this.setIcon(this.personCustomerIcon);
            }
         } else if(object instanceof CustomerAddress) {
            if(AddressType.STREET_ADDRESS_TYPE.equals(((CustomerAddress)object).getAddress().getAddressType())) {
               this.setIcon(this.streetAddressIcon);
            } else if(AddressType.POSTAL_ADDRESS_TYPE.equals(((CustomerAddress)object).getAddress().getAddressType())) {
               this.setIcon(this.postalAddressIcon);
            } else {
               this.setIcon(this.mailAddressIcon);
            }
         } else if(object instanceof Payment) {
            this.setIcon(this.paymentIcon);
         } else if(object instanceof String) {
            if("Members".equals(object)) {
               this.setIcon(this.membersIcon);
               this.setToolTipText("View all of this Customer\'s Members");
            } else if("Addresses".equals(object)) {
               this.setIcon(this.addressesIcon);
               this.setToolTipText("View all of this Customer\'s Address records");
            } else if("Payments".equals(object)) {
               this.setIcon(this.paymentsIcon);
               this.setToolTipText("View all of this Customer\'s Payment records");
            }
         } else {
            this.setToolTipText((String)null);
         }
      } else {
         this.setToolTipText((String)null);
      }

      return this;
   }
}
