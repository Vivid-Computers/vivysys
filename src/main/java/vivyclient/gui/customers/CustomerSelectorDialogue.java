package vivyclient.gui.customers;

import java.awt.Component;
import java.awt.Cursor;
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
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import vivyclient.Client;
import vivyclient.gui.customers.CustomersDisplayContainer;
import vivyclient.gui.customers.CustomersPanel;
import vivyclient.model.Customer;
import vivyclient.util.Settings;

public class CustomerSelectorDialogue extends JDialog implements CustomersDisplayContainer {
   private CustomersPanel customersPanel;
   private Object lock;
   private static final String NAME = "CustomerSelectorDialogue";
   private JButton bCancel;
   private JPanel jPanel2;
   private JPanel jPanel1;
   private JSeparator jSeparator1;
   private JMenuBar customerMenu;
   private JButton bOkay;
   private JLabel lTitle;
   private Customer selectedCustomer;
   private boolean isCancelled;

   public static Customer getUserSelectedCustomer() throws Exception {
      Object lock = new Object();
      CustomerSelectorDialogue dialogue = new CustomerSelectorDialogue(Client.getMainFrame(), true, lock);
      dialogue.show();

      while(dialogue.getSelectedCustomer() == null && !dialogue.getIsCancelled()) {
         try {
            synchronized(lock) {
               lock.wait();
            }
         } catch (InterruptedException var5) {
            ;
         }
      }

      return dialogue.getSelectedCustomer();
   }

   public CustomerSelectorDialogue(Frame parent, boolean modal, Object lock) throws Exception {
      super(parent, modal);
      this.lock = lock;
      this.initComponents();
      this.customersPanel = new CustomersPanel("displayUnsorted", this);
      this.getContentPane().add(this.customersPanel, "Center");
      this.setSize(new Dimension(Settings.getWidth("CustomerSelectorDialogue"), Settings.getHeight("CustomerSelectorDialogue")));
      this.setLocation(Settings.getXPos("CustomerSelectorDialogue"), Settings.getYPos("CustomerSelectorDialogue"));
   }

   private void initComponents() {
      this.jPanel1 = new JPanel();
      this.lTitle = new JLabel();
      this.jSeparator1 = new JSeparator();
      this.jPanel2 = new JPanel();
      this.bOkay = new JButton();
      this.bCancel = new JButton();
      this.customerMenu = new JMenuBar();
      this.setTitle("Customer Selection");
      this.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent evt) {
            CustomerSelectorDialogue.this.closeDialog(evt);
         }
      });
      this.jPanel1.setLayout(new GridBagLayout());
      this.lTitle.setFont(new Font("Arial", 1, 14));
      this.lTitle.setForeground(SystemColor.activeCaption);
      this.lTitle.setText("Select Customer");
      GridBagConstraints gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.insets = new Insets(5, 5, 3, 5);
      gridBagConstraints.anchor = 17;
      gridBagConstraints.weightx = 1.0D;
      this.jPanel1.add(this.lTitle, gridBagConstraints);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.ipadx = 3;
      gridBagConstraints.insets = new Insets(0, 0, 10, 0);
      gridBagConstraints.weightx = 1.0D;
      this.jPanel1.add(this.jSeparator1, gridBagConstraints);
      this.getContentPane().add(this.jPanel1, "North");
      this.jPanel2.setLayout(new GridBagLayout());
      this.bOkay.setText("Okay");
      this.bOkay.setEnabled(false);
      this.bOkay.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent evt) {
            CustomerSelectorDialogue.this.bOkayMouseClicked(evt);
         }
      });
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 3;
      gridBagConstraints.insets = new Insets(5, 0, 5, 5);
      gridBagConstraints.anchor = 13;
      gridBagConstraints.weightx = 1.0D;
      this.jPanel2.add(this.bOkay, gridBagConstraints);
      this.bCancel.setText("Cancel");
      this.bCancel.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent evt) {
            CustomerSelectorDialogue.this.bCancelMouseClicked(evt);
         }
      });
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 3;
      gridBagConstraints.insets = new Insets(5, 0, 5, 5);
      gridBagConstraints.anchor = 13;
      this.jPanel2.add(this.bCancel, gridBagConstraints);
      this.getContentPane().add(this.jPanel2, "South");
      this.setJMenuBar(this.customerMenu);
      this.pack();
   }

   private void bCancelMouseClicked(MouseEvent evt) {
      this.selectedCustomer = null;
      this.isCancelled = true;
      this.notifiedClose();
   }

   private void bOkayMouseClicked(MouseEvent evt) {
      this.notifiedClose();
   }

   private void closeDialog(WindowEvent evt) {
      this.selectedCustomer = null;
      this.isCancelled = true;
      this.notifiedClose();
   }

   public void addToJMenuBar(Component component) {
      this.customerMenu.add(component);
      this.customerMenu.validate();
      this.customerMenu.repaint();
   }

   public void customerSelectionChange(Customer customer) {
      this.selectedCustomer = customer;
      this.bOkay.setEnabled(this.selectedCustomer != null);
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

   public void setContainerCursor(Cursor cursor) {
      this.setCursor(cursor);
   }

   public Customer getSelectedCustomer() {
      return this.selectedCustomer;
   }

   public void setSelectedCustomer(Customer selectedCustomer) {
      this.selectedCustomer = selectedCustomer;
   }

   public boolean getIsCancelled() {
      return this.isCancelled;
   }

   public void setIsCancelled(boolean isCancelled) {
      this.isCancelled = isCancelled;
   }

   private void notifiedClose() {
      Object var1 = this.lock;
      synchronized(this.lock) {
         Settings.setWidth("CustomerSelectorDialogue", (int)this.getSize().getWidth());
         Settings.setHeight("CustomerSelectorDialogue", (int)this.getSize().getHeight());
         Settings.setXPos("CustomerSelectorDialogue", this.getLocation().x);
         Settings.setYPos("CustomerSelectorDialogue", this.getLocation().y);
         this.customersPanel.closing();
         this.setVisible(false);
         this.dispose();
         this.lock.notifyAll();
      }
   }
}
