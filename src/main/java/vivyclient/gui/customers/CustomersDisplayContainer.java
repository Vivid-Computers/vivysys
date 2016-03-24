package vivyclient.gui.customers;

import java.awt.Component;
import java.awt.Cursor;
import javax.swing.JMenuBar;
import vivyclient.model.Customer;

public interface CustomersDisplayContainer {
   String getName();

   JMenuBar getJMenuBar();

   void addToJMenuBar(Component var1);

   void resetJMenuBar();

   void customerSelectionChange(Customer var1);

   void setContainerCursor(Cursor var1);
}
