package vivyclient.gui.sales;

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import vivyclient.model.Customer;
import vivyclient.model.CustomerType;
import vivyclient.model.Sale;
import vivyclient.model.SaleStatus;

public class SaleTreeCellRenderer extends DefaultTreeCellRenderer {
   ImageIcon saleIcon = new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/CashRegister.gif"));
   ImageIcon saleStatusIcon = null;
   ImageIcon personCustomerIcon = new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/Person.gif"));
   ImageIcon organisationCustomerIcon = new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/Shop16.gif"));
   ImageIcon membersIcon = new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/People.gif"));

   public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
      super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
      if(value instanceof DefaultMutableTreeNode) {
         Object object = ((DefaultMutableTreeNode)value).getUserObject();
         if(object instanceof Sale) {
            this.setIcon(this.saleIcon);
         } else if(!(object instanceof SaleStatus)) {
            if(object instanceof Customer) {
               if(CustomerType.ORGANISATION_CUSTOMER_TYPE.equals(((Customer)object).getCustomerType())) {
                  this.setIcon(this.organisationCustomerIcon);
               } else {
                  this.setIcon(this.personCustomerIcon);
               }
            } else if(object instanceof String) {
               if("Members".equals(object)) {
                  this.setIcon(this.membersIcon);
                  this.setToolTipText("View all of this Customer\'s Members");
               }
            } else {
               this.setToolTipText((String)null);
            }
         }
      } else {
         this.setToolTipText((String)null);
      }

      return this;
   }
}
