package vivyclient.gui.customers;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;
import vivyclient.Client;
import vivyclient.data.TransactionContainer;
import vivyclient.exception.AppRuntimeException;
import vivyclient.exception.StaleModelException;
import vivyclient.exception.UserInputException;
import vivyclient.gui.common.EditPanel;
import vivyclient.gui.customers.CustomerAddressPanel;
import vivyclient.gui.customers.CustomerPanel;
import vivyclient.gui.customers.CustomerTreeCellRenderer;
import vivyclient.gui.customers.CustomersDisplayContainer;
import vivyclient.gui.customers.PaymentPanel;
import vivyclient.gui.customers.PrintCustomerStatementFrame;
import vivyclient.gui.products.ExpandableTreeNode;
import vivyclient.model.Address;
import vivyclient.model.Customer;
import vivyclient.model.CustomerAddress;
import vivyclient.model.Payment;
import vivyclient.model.searchMap.CustomerSearchMapFactory;
import vivyclient.util.DialogueUtil;
import vivyclient.util.Settings;

public class CustomersPanel extends JPanel implements TreeWillExpandListener {
   public static final String DISPLAY_UNSORTED = "displayUnsorted";
   private static final String CUSTOMERS_NODE_TITLE = "Customers";
   public static final String MEMBERS_NODE_TITLE = "Members";
   public static final String ADDRESSES_NODE_TITLE = "Addresses";
   public static final String PAYMENTS_NODE_TITLE = "Payments";
   private static Dimension DEFAULT_MINIMUM_PANEL_SIZE = new Dimension(10, 10);
   private String displayType;
   private CustomersDisplayContainer parent;
   public static final int SAVE_CUSTOMER_ACTION = 1;
   public static final int ADD_CUSTOMER_ACTION = 2;
   public static final int ADD_MEMBER_CUSTOMER_ACTION = 3;
   public static final int DELETE_CUSTOMER_ACTION = 4;
   public static final int SAVE_ADDRESS_ACTION = 5;
   public static final int ADD_ADDRESS_ACTION = 6;
   public static final int DELETE_ADDRESS_ACTION = 7;
   public static final int ADD_PAYMENT_ACTION = 8;
   public static final int SAVE_PAYMENT_ACTION = 9;
   public static final int PRINT_CUSTOMER_STATEMENT_ACTION = 10;
   private JSplitPane centerSplitPane;
   private JTree customerTree;
   private JScrollPane customerTreeScrollPane;

   public CustomersPanel(String displayType, CustomersDisplayContainer parent) throws Exception {
      this.displayType = displayType;
      this.parent = parent;
      this.initComponents();
      this.customerTree.setCellRenderer(new CustomerTreeCellRenderer());
      this.centerSplitPane.setDividerLocation(Settings.getDividerLocation(parent.getName()));
      this.centerSplitPane.setResizeWeight(0.25D);
      this.customerTree.addTreeWillExpandListener(this);
      this.refreshCustomerList(displayType);
   }

   private void refreshCustomerList(String displayType) throws Exception {
      if(displayType.equals("displayUnsorted")) {
         DefaultMutableTreeNode root = new DefaultMutableTreeNode("Customers");
         Customer criteria = new Customer();
         criteria.setCustomerGroup((Customer)null);
         List groups = Customer.findAll(criteria, CustomerSearchMapFactory.getCustomerGroupSearchMap(), (TransactionContainer)null);

         for(int i = 0; i < groups.size(); ++i) {
            root.add(new ExpandableTreeNode(groups.get(i)));
         }

         this.customerTree.setModel(new DefaultTreeModel(root));
         this.customerTree.setRootVisible(true);
         this.customerTree.setShowsRootHandles(false);
      }

      this.centerSplitPane.setRightComponent(new JPanel());
      this.resetFrameMenu();
   }

   public void setRightComponent(JComponent component) {
      int dividerLocation = this.centerSplitPane.getDividerLocation();
      component.setMinimumSize(DEFAULT_MINIMUM_PANEL_SIZE);
      this.centerSplitPane.setRightComponent(component);
      this.centerSplitPane.setDividerLocation(dividerLocation);
      component.validate();
      this.validate();
   }

   private void initComponents() {
      this.centerSplitPane = new JSplitPane();
      this.customerTreeScrollPane = new JScrollPane();
      this.customerTree = new JTree();
      this.setLayout(new BorderLayout());
      this.centerSplitPane.setBorder((Border)null);
      this.centerSplitPane.setDividerSize(8);
      this.centerSplitPane.setOneTouchExpandable(true);
      this.customerTree.addTreeSelectionListener(new TreeSelectionListener() {
         public void valueChanged(TreeSelectionEvent evt) {
            CustomersPanel.this.customerSelectionChange(evt);
         }
      });
      this.customerTreeScrollPane.setViewportView(this.customerTree);
      this.centerSplitPane.setLeftComponent(this.customerTreeScrollPane);
      this.add(this.centerSplitPane, "Center");
   }

   private void customerSelectionChange(TreeSelectionEvent evt) {
      try {
         this.parent.setContainerCursor(new Cursor(3));
         this.resetFrameMenu();
         if(this.customerTree.getSelectionPath() != null) {
            DefaultMutableTreeNode ex = (DefaultMutableTreeNode)this.customerTree.getSelectionPath().getLastPathComponent();
            Object selectedNodeObject = ex.getUserObject();
            if(selectedNodeObject.equals("Customers")) {
               this.customerCollectionSelected(ex);
            } else if(selectedNodeObject instanceof Customer) {
               this.customerSelected(ex);
            } else if(selectedNodeObject.equals("Members")) {
               this.memberCollectionSelected(ex);
            } else if(selectedNodeObject.equals("Addresses")) {
               this.addressCollectionSelected(ex);
            } else if(selectedNodeObject instanceof CustomerAddress) {
               this.customerAddressSelected(ex);
            } else if(selectedNodeObject.equals("Payments")) {
               this.paymentCollectionSelected(ex);
            } else if(selectedNodeObject instanceof Payment) {
               this.paymentSelected(ex);
            } else {
               this.setRightComponent(new JPanel());
            }
         } else {
            this.setRightComponent(new JPanel());
         }
      } catch (Exception var7) {
         DialogueUtil.handleException(var7, "Selection error:", "Error", true, Client.getMainFrame());
      } finally {
         this.parent.setContainerCursor(new Cursor(0));
      }

   }

   private void customerCollectionSelected(DefaultMutableTreeNode selectedNode) throws Exception {
      JButton newButton = new JButton(new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/New16.gif")));
      newButton.setMargin(new Insets(0, 0, 0, 0));
      newButton.setToolTipText("Add New Customer");
      newButton.addActionListener(new CustomersPanel.CustomerListener((EditPanel)null, selectedNode, 2));
      this.parent.addToJMenuBar(newButton);
      this.setRightComponent(new JPanel());
      this.parent.customerSelectionChange((Customer)null);
   }

   private void customerSelected(DefaultMutableTreeNode selectedNode) throws Exception {
      CustomerPanel customerPanel = new CustomerPanel((Customer)selectedNode.getUserObject());
      JButton saveButton = new JButton(new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/Save16.gif")));
      saveButton.setMargin(new Insets(0, 0, 0, 0));
      saveButton.setToolTipText("Save Customer");
      saveButton.setMnemonic(KeyStroke.getKeyStroke(83, 2).getKeyCode());
      saveButton.addActionListener(new CustomersPanel.CustomerListener(customerPanel, selectedNode, 1));
      this.parent.addToJMenuBar(saveButton);
      JButton statementButton = new JButton(new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/Print16.gif")));
      statementButton.setMargin(new Insets(0, 0, 0, 0));
      statementButton.setToolTipText("Print Customer Statement");
      statementButton.setMnemonic(KeyStroke.getKeyStroke(83, 2).getKeyCode());
      statementButton.addActionListener(new CustomersPanel.CustomerListener((EditPanel)null, selectedNode, 10));
      this.parent.addToJMenuBar(statementButton);
      this.setRightComponent(customerPanel);
      this.parent.customerSelectionChange((Customer)customerPanel.getModel());
   }

   private void memberCollectionSelected(DefaultMutableTreeNode selectedNode) throws Exception {
      JButton newButton = new JButton(new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/New16.gif")));
      newButton.setMargin(new Insets(0, 0, 0, 0));
      newButton.setToolTipText("Add Customer to Group");
      newButton.addActionListener(new CustomersPanel.CustomerListener((EditPanel)null, selectedNode, 3));
      this.parent.addToJMenuBar(newButton);
      this.setRightComponent(new JPanel());
      this.parent.customerSelectionChange((Customer)null);
   }

   private void addressCollectionSelected(DefaultMutableTreeNode selectedNode) throws Exception {
      JButton newButton = new JButton(new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/New16.gif")));
      newButton.setMargin(new Insets(0, 0, 0, 0));
      newButton.setToolTipText("Add Customer Address");
      newButton.addActionListener(new CustomersPanel.CustomerListener((EditPanel)null, selectedNode, 6));
      this.parent.addToJMenuBar(newButton);
      this.setRightComponent(new JPanel());
      this.parent.customerSelectionChange((Customer)null);
   }

   private void paymentCollectionSelected(DefaultMutableTreeNode selectedNode) throws Exception {
      JButton newButton = new JButton(new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/New16.gif")));
      newButton.setMargin(new Insets(0, 0, 0, 0));
      newButton.setToolTipText("Add Payment");
      newButton.addActionListener(new CustomersPanel.CustomerListener((EditPanel)null, selectedNode, 8));
      this.parent.addToJMenuBar(newButton);
      this.setRightComponent(new JPanel());
      this.parent.customerSelectionChange((Customer)null);
   }

   private void paymentSelected(DefaultMutableTreeNode selectedNode) throws Exception {
      PaymentPanel paymentPanel = new PaymentPanel((Payment)selectedNode.getUserObject());
      JButton saveButton = new JButton(new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/Save16.gif")));
      saveButton.setMargin(new Insets(0, 0, 0, 0));
      saveButton.setToolTipText("Save Payment");
      saveButton.setMnemonic(KeyStroke.getKeyStroke(83, 2).getKeyCode());
      saveButton.addActionListener(new CustomersPanel.CustomerListener(paymentPanel, selectedNode, 9));
      this.parent.addToJMenuBar(saveButton);
      this.setRightComponent(paymentPanel);
      this.parent.customerSelectionChange((Customer)null);
   }

   private void customerAddressSelected(DefaultMutableTreeNode selectedNode) throws Exception {
      CustomerAddressPanel customerAddressPanel = new CustomerAddressPanel((CustomerAddress)selectedNode.getUserObject());
      JButton saveButton = new JButton(new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/Save16.gif")));
      saveButton.setMargin(new Insets(0, 0, 0, 0));
      saveButton.setToolTipText("Save Customer Address");
      saveButton.setMnemonic(KeyStroke.getKeyStroke(83, 2).getKeyCode());
      saveButton.addActionListener(new CustomersPanel.CustomerListener(customerAddressPanel, selectedNode, 5));
      this.parent.addToJMenuBar(saveButton);
      this.setRightComponent(customerAddressPanel);
      this.parent.customerSelectionChange((Customer)null);
   }

   private void resetFrameMenu() {
      this.parent.resetJMenuBar();
   }

   public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
   }

   public void treeWillExpand(TreeExpansionEvent treeExpansionEvent) throws ExpandVetoException {
      if(treeExpansionEvent.getPath().getLastPathComponent() instanceof ExpandableTreeNode) {
         try {
            this.parent.setContainerCursor(new Cursor(3));
            ExpandableTreeNode e = (ExpandableTreeNode)treeExpansionEvent.getPath().getLastPathComponent();
            if(!e.getHasBeenExpanded()) {
               DefaultTreeModel model = (DefaultTreeModel)this.customerTree.getModel();
               Object nodeObject = e.getUserObject();
               if(!(nodeObject instanceof Customer)) {
                  throw new AppRuntimeException();
               }

               Customer customer = (Customer)nodeObject;
               DefaultMutableTreeNode childrenNode = new DefaultMutableTreeNode("Members");
               model.insertNodeInto(childrenNode, e, e.getChildCount());

               for(int addressNode = 0; addressNode < customer.getChildCount(); ++addressNode) {
                  model.insertNodeInto(new ExpandableTreeNode(customer.getChild(addressNode)), childrenNode, childrenNode.getChildCount());
               }

               DefaultMutableTreeNode var15 = new DefaultMutableTreeNode("Addresses");
               model.insertNodeInto(var15, e, e.getChildCount());

               for(int paymentsNode = 0; paymentsNode < customer.getAddressLinkCount(); ++paymentsNode) {
                  model.insertNodeInto(new DefaultMutableTreeNode(customer.getAddressLink(paymentsNode)), var15, var15.getChildCount());
               }

               DefaultMutableTreeNode var16 = new DefaultMutableTreeNode("Payments");
               model.insertNodeInto(var16, e, e.getChildCount());

               for(int i = 0; i < customer.getPaymentCount(); ++i) {
                  model.insertNodeInto(new DefaultMutableTreeNode(customer.getPayment(i)), var16, var16.getChildCount());
               }

               e.setHasBeenExpanded(true);
            }
         } catch (Exception var13) {
            DialogueUtil.handleException(var13, "Error loading contents of \'" + treeExpansionEvent.getPath().getLastPathComponent().toString() + "\'", "Load Error", true, Client.getMainFrame());
            throw new ExpandVetoException(treeExpansionEvent);
         } finally {
            this.parent.setContainerCursor(new Cursor(0));
         }
      }

   }

   private DefaultMutableTreeNode insertNode(Object object, DefaultMutableTreeNode parent) throws Exception {
      DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(object);
      if(object instanceof Customer) {
         newNode.add(new DefaultMutableTreeNode("Members"));
         newNode.add(new DefaultMutableTreeNode("Addresses"));
      }

      DefaultTreeModel model = (DefaultTreeModel)this.customerTree.getModel();
      model.insertNodeInto(newNode, parent, model.getChildCount(parent));
      TreePath insertedPath = new TreePath(newNode.getPath());
      this.customerTree.setSelectionPath(insertedPath);
      this.customerTree.scrollPathToVisible(insertedPath);
      return newNode;
   }

   public void closing() {
      Settings.setDividerLocation(this.parent.getName(), this.centerSplitPane.getDividerLocation());
   }

   class CustomerListener implements ActionListener {
      private EditPanel editPanel;
      private int action;
      private DefaultMutableTreeNode selectedNode;

      public CustomerListener(EditPanel editPanel, DefaultMutableTreeNode selectedNode, int action) {
         this.editPanel = editPanel;
         this.selectedNode = selectedNode;
         this.action = action;
      }

      public void actionPerformed(ActionEvent evt) {
         try {
            CustomersPanel.this.parent.setContainerCursor(new Cursor(3));
            DefaultMutableTreeNode ex;
            Customer action1;
            if(this.action == 1) {
               if(this.editPanel.save()) {
                  ex = CustomersPanel.this.insertNode(this.editPanel.getModel(), this.selectedNode);
                  action1 = (Customer)this.editPanel.getModel();
                  if(0 == JOptionPane.showConfirmDialog(Client.getMainFrame(), "Do you wish to enter an Address for " + action1.toString() + "?", "Enter Address", 0, 3)) {
                     this.addNewAddress(action1, (DefaultMutableTreeNode)ex.getChildAt(1));
                     Client.showInfoMessage("New Customer Saved");
                  } else {
                     Client.showInfoMessage("Customer Saved");
                  }
               }

               CustomersPanel.this.parent.customerSelectionChange((Customer)this.editPanel.getModel());
            } else if(this.action == 5) {
               if(this.editPanel.save()) {
                  CustomersPanel.this.insertNode(this.editPanel.getModel(), this.selectedNode);
                  Client.showInfoMessage("New Address Saved");
               } else {
                  CustomersPanel.this.customerTree.repaint();
                  Client.showInfoMessage("Address Saved");
               }
            } else if(this.action == 2) {
               Customer ex1 = new Customer();
               CustomerPanel action2 = new CustomerPanel(ex1);
               CustomersPanel.this.resetFrameMenu();
               JButton customerPanel = new JButton(new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/Save16.gif")));
               customerPanel.setMargin(new Insets(0, 0, 0, 0));
               customerPanel.setToolTipText("Save New Customer");
               customerPanel.addActionListener(CustomersPanel.this.new CustomerListener(action2, this.selectedNode, 1));
               CustomersPanel.this.parent.addToJMenuBar(customerPanel);
               CustomersPanel.this.setRightComponent(action2);
               CustomersPanel.this.parent.customerSelectionChange((Customer)null);
            } else if(this.action == 3) {
               ex = (DefaultMutableTreeNode)this.selectedNode.getParent();
               action1 = new Customer();
               action1.setCustomerGroup((Customer)ex.getUserObject());
               CustomerPanel customerPanel1 = new CustomerPanel(action1);
               CustomersPanel.this.resetFrameMenu();
               JButton saveButton = new JButton(new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/Save16.gif")));
               saveButton.setMargin(new Insets(0, 0, 0, 0));
               saveButton.setToolTipText("Save New Customer");
               saveButton.addActionListener(CustomersPanel.this.new CustomerListener(customerPanel1, this.selectedNode, 1));
               CustomersPanel.this.parent.addToJMenuBar(saveButton);
               CustomersPanel.this.setRightComponent(customerPanel1);
               CustomersPanel.this.parent.customerSelectionChange((Customer)null);
            } else if(this.action == 4) {
               if(DialogueUtil.confirmForDelete("Customer", Client.getMainFrame())) {
                  ((Customer)this.selectedNode.getUserObject()).delete((TransactionContainer)null);
                  CustomersPanel.this.parent.customerSelectionChange((Customer)null);
               }
            } else if(this.action == 6) {
               ex = (DefaultMutableTreeNode)this.selectedNode.getParent();
               this.addNewAddress((Customer)ex.getUserObject(), this.selectedNode);
               CustomersPanel.this.parent.customerSelectionChange((Customer)null);
            } else if(this.action == 8) {
               ex = (DefaultMutableTreeNode)this.selectedNode.getParent();
               this.addPayment((Customer)ex.getUserObject(), this.selectedNode);
               CustomersPanel.this.parent.customerSelectionChange((Customer)null);
            } else if(this.action == 9) {
               if(this.editPanel.save()) {
                  CustomersPanel.this.insertNode(this.editPanel.getModel(), this.selectedNode);
                  Client.showInfoMessage("New Payment Saved");
               } else {
                  CustomersPanel.this.customerTree.repaint();
                  Client.showInfoMessage("Payment Saved");
               }
            } else if(this.action == 10) {
               PrintCustomerStatementFrame ex2 = new PrintCustomerStatementFrame((Customer)this.selectedNode.getUserObject());
               Client.getMainFrame();
               Client.addInternalFrame(ex2);
               ex2.setVisible(true);
            }
         } catch (StaleModelException var11) {
            int action = DialogueUtil.getActionForStaleModelException(var11, Client.getMainFrame());
         } catch (UserInputException var12) {
            DialogueUtil.handleUserInputException(var12, "", "Invalid Input", Client.getMainFrame());
         } catch (Exception var13) {
            DialogueUtil.handleException(var13, "Error performing action", "Error", true, Client.getMainFrame());
         } finally {
            CustomersPanel.this.parent.setContainerCursor(new Cursor(0));
         }

      }

      private void addNewAddress(Customer customer, DefaultMutableTreeNode selectedNode) throws Exception {
         CustomerAddress newAddress = new CustomerAddress();
         newAddress.setCustomer(customer);
         newAddress.setAddress(new Address());
         newAddress.getAddress().setDeliveryName(newAddress.getCustomer().getDefaultDeliveryName());
         CustomerAddressPanel addressPanel = new CustomerAddressPanel(newAddress);
         CustomersPanel.this.resetFrameMenu();
         JButton saveButton = new JButton(new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/Save16.gif")));
         saveButton.setMargin(new Insets(0, 0, 0, 0));
         saveButton.setToolTipText("Save New Customer Address");
         saveButton.addActionListener(CustomersPanel.this.new CustomerListener(addressPanel, selectedNode, 5));
         CustomersPanel.this.parent.addToJMenuBar(saveButton);
         CustomersPanel.this.setRightComponent(addressPanel);
      }

      private void addPayment(Customer customer, DefaultMutableTreeNode selectedNode) throws Exception {
         Payment payment = new Payment();
         payment.setCustomer(customer);
         PaymentPanel paymentPanel = new PaymentPanel(payment);
         CustomersPanel.this.resetFrameMenu();
         JButton saveButton = new JButton(new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/Save16.gif")));
         saveButton.setMargin(new Insets(0, 0, 0, 0));
         saveButton.setToolTipText("Save New Payment");
         saveButton.addActionListener(CustomersPanel.this.new CustomerListener(paymentPanel, selectedNode, 9));
         CustomersPanel.this.parent.addToJMenuBar(saveButton);
         CustomersPanel.this.setRightComponent(paymentPanel);
      }
   }
}
