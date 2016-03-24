package vivyclient.gui.customers;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import javax.swing.JInternalFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.event.TreeSelectionEvent;
import vivyclient.gui.customers.CustomersDisplayContainer;
import vivyclient.gui.customers.CustomersPanel;
import vivyclient.model.Customer;
import vivyclient.util.Settings;

public class CustomerFrame extends JInternalFrame implements CustomersDisplayContainer {
   private static final String NAME = "CustomerFrame";
   private String displayType;
   private CustomersPanel customersPanel;
   private JMenuBar customerMenu;

   public CustomerFrame(String displayType) throws Exception {
      this.displayType = displayType;
      this.initComponents();
      this.customersPanel = new CustomersPanel(displayType, this);
      this.getContentPane().add(this.customersPanel);
      this.setSize(new Dimension(Settings.getWidth("CustomerFrame"), Settings.getHeight("CustomerFrame")));
      this.setLocation(Settings.getXPos("CustomerFrame"), Settings.getYPos("CustomerFrame"));
      this.setVisible(true);
   }

   private void initComponents() {
      this.customerMenu = new JMenuBar();
      this.setClosable(true);
      this.setIconifiable(true);
      this.setMaximizable(true);
      this.setResizable(true);
      this.setTitle("Customers");
      this.addInternalFrameListener(new InternalFrameListener() {
         public void internalFrameOpened(InternalFrameEvent evt) {
         }

         public void internalFrameClosing(InternalFrameEvent evt) {
            CustomerFrame.this.frameClosing(evt);
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
      this.setJMenuBar(this.customerMenu);
      this.pack();
   }

   private void selectedCustomerChange(TreeSelectionEvent evt) {
   }

   private void frameClosing(InternalFrameEvent evt) {
      Settings.setWidth("CustomerFrame", (int)this.getSize().getWidth());
      Settings.setHeight("CustomerFrame", (int)this.getSize().getHeight());
      Settings.setXPos("CustomerFrame", this.getLocation().x);
      Settings.setYPos("CustomerFrame", this.getLocation().y);
      Settings.setMaximized("CustomerFrame", this.isMaximum());
      this.customersPanel.closing();
      this.setVisible(false);
      this.dispose();
   }

   public void customerSelectionChange(Customer customer) {
   }

   public void resetJMenuBar() {
      this.customerMenu.removeAll();
      JPanel filler = new JPanel();
      filler.setMinimumSize(new Dimension(1, 20));
      filler.setMaximumSize(new Dimension(1, 20));
      filler.setPreferredSize(new Dimension(1, 20));
      this.customerMenu.add(filler);
      this.customerMenu.repaint();
   }

   public void addToJMenuBar(Component component) {
      this.customerMenu.add(component);
      this.customerMenu.validate();
      this.customerMenu.repaint();
   }

   public void setContainerCursor(Cursor cursor) {
      this.setCursor(cursor);
   }
}
