package vivyclient.gui.sales;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
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
import vivyclient.exception.BusinessException;
import vivyclient.exception.StaleModelException;
import vivyclient.exception.UserInputException;
import vivyclient.gui.common.EditPanel;
import vivyclient.gui.products.ExpandableTreeNode;
import vivyclient.gui.sales.QuickDispatchDialogue;
import vivyclient.gui.sales.SalePanel;
import vivyclient.gui.sales.SaleTreeCellRenderer;
import vivyclient.model.Customer;
import vivyclient.model.Dispatch;
import vivyclient.model.PaymentMethodType;
import vivyclient.model.Sale;
import vivyclient.model.SaleStatus;
import vivyclient.model.searchMap.BaseSearchMap;
import vivyclient.model.searchMap.CustomerSearchMapFactory;
import vivyclient.model.searchMap.DispatchSearchMapFactory;
import vivyclient.model.searchMap.SaleSearchMapFactory;
import vivyclient.print.PrinterGateway;
import vivyclient.print.sale.InvoicePrinter;
import vivyclient.util.DialogueUtil;
import vivyclient.util.Settings;

public class SaleFrame extends JInternalFrame implements TreeWillExpandListener {
   private static final String NAME = "SaleFrame";
   private static Dimension DEFAULT_MINIMUM_PANEL_SIZE = new Dimension(10, 10);
   public static final String CUSTOMER_MEMBERS_NODE_TITLE = "Members";
   public static final String DISPLAY_UNSORTED = "displayUnsorted";
   public static final String DISPLAY_BY_STATUS = "displayByStatus";
   public static final String DISPLAY_BY_CUSTOMER = "displayByCustomer";
   private String displayType;
   private JScrollPane saleTreeScrollPane;
   private JMenuBar frameMenu;
   private JSplitPane centerSplitPane;
   private JTree saleTree;

   public SaleFrame(String displayType) {
      this.displayType = displayType;

      try {
         this.initComponents();
         this.setSize(new Dimension(Settings.getWidth("SaleFrame"), Settings.getHeight("SaleFrame")));
         this.setLocation(Settings.getXPos("SaleFrame"), Settings.getYPos("SaleFrame"));
         this.centerSplitPane.setDividerLocation(Settings.getDividerLocation("SaleFrame"));
         this.centerSplitPane.setResizeWeight(0.25D);
         this.saleTree.addTreeWillExpandListener(this);
         this.saleTree.setCellRenderer(new SaleTreeCellRenderer());
         this.setValues();
         this.setVisible(true);
      } catch (Exception var3) {
         DialogueUtil.handleException(var3, "Error Displaying Sales", "ERROR", true, Client.getMainFrame());
         this.setVisible(false);
         this.dispose();
      }

   }

   private void setValues() throws Exception {
      this.refreshSaleList(this.displayType);
   }

   private void refreshSaleList(String displayType) throws Exception {
      DefaultMutableTreeNode root;
      List criteria;
      int groups;
      if(displayType.equals("displayUnsorted")) {
         root = new DefaultMutableTreeNode("Sales");
         criteria = Sale.findAll(new Sale(), (BaseSearchMap)null, (TransactionContainer)null);

         for(groups = 0; groups < criteria.size(); ++groups) {
            root.add(new DefaultMutableTreeNode(criteria.get(groups)));
         }

         this.saleTree.setModel(new DefaultTreeModel(root));
         this.saleTree.setRootVisible(true);
         this.saleTree.setShowsRootHandles(false);
      } else if(displayType.equals("displayByStatus")) {
         root = new DefaultMutableTreeNode("Sales");
         criteria = SaleStatus.findAll(new SaleStatus(), (BaseSearchMap)null, (TransactionContainer)null);

         for(groups = 0; groups < criteria.size(); ++groups) {
            root.add(new ExpandableTreeNode(criteria.get(groups)));
         }

         this.saleTree.setModel(new DefaultTreeModel(root));
         this.saleTree.setRootVisible(false);
         this.saleTree.setShowsRootHandles(true);
      } else if(displayType.equals("displayByCustomer")) {
         root = new DefaultMutableTreeNode("Customers");
         Customer var6 = new Customer();
         var6.setCustomerGroup((Customer)null);
         List var7 = Customer.findAll(var6, CustomerSearchMapFactory.getCustomerGroupSearchMap(), (TransactionContainer)null);

         for(int i = 0; i < var7.size(); ++i) {
            root.add(new ExpandableTreeNode(var7.get(i)));
         }

         this.saleTree.setModel(new DefaultTreeModel(root));
         this.saleTree.setRootVisible(false);
         this.saleTree.setShowsRootHandles(true);
      }

      this.setRightComponent(new JPanel());
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

   private void resetFrameMenu() {
      this.frameMenu.removeAll();
      JPanel filler = new JPanel();
      filler.setMinimumSize(new Dimension(1, 20));
      filler.setMaximumSize(new Dimension(1, 20));
      filler.setPreferredSize(new Dimension(1, 20));
      this.frameMenu.add(filler);
   }

   private void insertSaleNode(Sale sale) throws Exception {
   }

   private void initComponents() {
      this.centerSplitPane = new JSplitPane();
      this.saleTreeScrollPane = new JScrollPane();
      this.saleTree = new JTree();
      this.frameMenu = new JMenuBar();
      this.setClosable(true);
      this.setIconifiable(true);
      this.setMaximizable(true);
      this.setResizable(true);
      this.setTitle("Sales");
      this.addInternalFrameListener(new InternalFrameListener() {
         public void internalFrameOpened(InternalFrameEvent evt) {
         }

         public void internalFrameClosing(InternalFrameEvent evt) {
            SaleFrame.this.frameClosing(evt);
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
      this.centerSplitPane.setDividerSize(8);
      this.centerSplitPane.setOneTouchExpandable(true);
      this.saleTree.addTreeSelectionListener(new TreeSelectionListener() {
         public void valueChanged(TreeSelectionEvent evt) {
            SaleFrame.this.selectedSaleChange(evt);
         }
      });
      this.saleTreeScrollPane.setViewportView(this.saleTree);
      this.centerSplitPane.setLeftComponent(this.saleTreeScrollPane);
      this.getContentPane().add(this.centerSplitPane, "Center");
      this.setJMenuBar(this.frameMenu);
      this.pack();
   }

   private void selectedSaleChange(TreeSelectionEvent evt) {
      try {
         this.setCursor(new Cursor(3));
         if(this.saleTree.getSelectionPath() != null) {
            this.resetFrameMenu();
            DefaultMutableTreeNode ex = (DefaultMutableTreeNode)this.saleTree.getSelectionPath().getLastPathComponent();
            Object selectedNodeObject = ex.getUserObject();
            if(selectedNodeObject instanceof Sale) {
               this.saleSelected(ex);
            } else if(selectedNodeObject instanceof SaleStatus) {
               this.saleStatusSelected(ex);
            } else if(selectedNodeObject instanceof Customer) {
               this.customerSelected(ex);
            } else {
               this.setRightComponent(new JPanel());
            }

            this.frameMenu.repaint();
         }
      } catch (Exception var7) {
         DialogueUtil.handleException(var7, "Selection error:", "Error", true, Client.getMainFrame());
      } finally {
         this.setCursor(new Cursor(0));
      }

   }

   private void saleSelected(DefaultMutableTreeNode selectedNode) throws Exception {
      SalePanel salePanel = new SalePanel((Sale)selectedNode.getUserObject());
      JButton saveButton = new JButton(new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/Save16.gif")));
      saveButton.setMargin(new Insets(0, 0, 0, 0));
      saveButton.setToolTipText("Save Sale");
      saveButton.setMnemonic('S');
      saveButton.addActionListener(new SaleFrame.SaleFrameListener(salePanel, selectedNode, 1));
      this.frameMenu.add(saveButton);
      JButton dispatchButton = new JButton(new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/ForkLift.gif")));
      dispatchButton.setToolTipText("Edit Dispatching of this Sale");
      dispatchButton.setMnemonic('D');
      dispatchButton.setMargin(new Insets(0, 0, 0, 0));
      this.frameMenu.add(dispatchButton);
      JButton quickDispatchButton = new JButton(new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/Target.gif")));
      quickDispatchButton.setToolTipText("Quick-Dispatch this Sale");
      quickDispatchButton.setMnemonic('Q');
      quickDispatchButton.setMargin(new Insets(0, 0, 0, 0));
      quickDispatchButton.addActionListener(new SaleFrame.SaleFrameListener(salePanel, selectedNode, 3));
      this.frameMenu.add(quickDispatchButton);
      JButton printButton = new JButton(new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/Print16.gif")));
      printButton.setMargin(new Insets(0, 0, 0, 0));
      printButton.setToolTipText("Print Invoice");
      printButton.setMnemonic('P');
      printButton.addActionListener(new SaleFrame.SaleFrameListener(salePanel, selectedNode, 4));
      this.frameMenu.add(printButton);
      JButton printPreviewButton = new JButton(new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/Magnify.gif")));
      printPreviewButton.setMargin(new Insets(0, 0, 0, 0));
      printPreviewButton.setToolTipText("Print Preview of Invoice");
      printPreviewButton.setMnemonic('V');
      printPreviewButton.addActionListener(new SaleFrame.SaleFrameListener(salePanel, selectedNode, 6));
      this.frameMenu.add(printPreviewButton);
      this.setRightComponent(salePanel);
   }

   private void saleStatusSelected(DefaultMutableTreeNode selectedNode) throws Exception {
      JButton newButton = new JButton(new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/New16.gif")));
      newButton.setMargin(new Insets(0, 0, 0, 0));
      newButton.setToolTipText("New Sale");
      newButton.setMnemonic('N');
      newButton.addActionListener(new SaleFrame.SaleFrameListener((EditPanel)null, selectedNode, 2));
      this.frameMenu.add(newButton);
      this.setRightComponent(new JPanel());
   }

   private void customerSelected(DefaultMutableTreeNode selectedNode) throws Exception {
      JButton newButton = new JButton(new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/New16.gif")));
      newButton.setMargin(new Insets(0, 0, 0, 0));
      newButton.setToolTipText("New Sale");
      newButton.setMnemonic('N');
      newButton.addActionListener(new SaleFrame.SaleFrameListener((EditPanel)null, selectedNode, 5));
      this.frameMenu.add(newButton);
      this.setRightComponent(new JPanel());
   }

   private DefaultMutableTreeNode insertNode(Object object, DefaultMutableTreeNode parent) throws Exception {
      DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(object);
      DefaultTreeModel model = (DefaultTreeModel)this.saleTree.getModel();
      model.insertNodeInto(newNode, parent, model.getChildCount(parent));
      TreePath insertedPath = new TreePath(newNode.getPath());
      this.saleTree.setSelectionPath(insertedPath);
      this.saleTree.scrollPathToVisible(insertedPath);
      return newNode;
   }

   private void frameClosing(InternalFrameEvent evt) {
      Settings.setWidth("SaleFrame", (int)this.getSize().getWidth());
      Settings.setHeight("SaleFrame", (int)this.getSize().getHeight());
      Settings.setXPos("SaleFrame", this.getLocation().x);
      Settings.setYPos("SaleFrame", this.getLocation().y);
      Settings.setMaximized("SaleFrame", this.isMaximum());
      Settings.setDividerLocation("SaleFrame", this.centerSplitPane.getDividerLocation());
      this.setVisible(false);
      this.dispose();
   }

   public void treeWillCollapse(TreeExpansionEvent treeExpansionEvent) throws ExpandVetoException {
   }

   public void treeWillExpand(TreeExpansionEvent treeExpansionEvent) throws ExpandVetoException {
      if(treeExpansionEvent.getPath().getLastPathComponent() instanceof ExpandableTreeNode) {
         try {
            this.setCursor(new Cursor(3));
            ExpandableTreeNode e = (ExpandableTreeNode)treeExpansionEvent.getPath().getLastPathComponent();
            if(!e.getHasBeenExpanded()) {
               DefaultTreeModel model = (DefaultTreeModel)this.saleTree.getModel();
               Object nodeObject = e.getUserObject();
               boolean childIsLeaf = true;
               Object childNodes;
               Sale i;
               if(!this.displayType.equals("displayByCustomer")) {
                  if(!this.displayType.equals("displayByStatus")) {
                     throw new AppRuntimeException();
                  }

                  if(!(nodeObject instanceof SaleStatus)) {
                     throw new AppRuntimeException();
                  }

                  i = new Sale();
                  i.setStatus((SaleStatus)nodeObject);
                  childNodes = Sale.findAll(i, SaleSearchMapFactory.getSaleStatusSearchMap(), (TransactionContainer)null);
                  childIsLeaf = true;
               } else {
                  if(nodeObject instanceof Customer) {
                     i = new Sale();
                     i.setCustomer((Customer)nodeObject);
                     childNodes = Sale.findAll(i, SaleSearchMapFactory.getCustomerSearchMap(), (TransactionContainer)null);
                     model.insertNodeInto(new ExpandableTreeNode("Members"), e, e.getChildCount());
                  } else {
                     if(!nodeObject.equals("Members")) {
                        throw new AppRuntimeException();
                     }

                     Customer var14 = (Customer)((DefaultMutableTreeNode)e.getParent()).getUserObject();

                     for(int i1 = 0; i1 < var14.getChildCount(); ++i1) {
                        model.insertNodeInto(new ExpandableTreeNode(var14.getChild(i1)), e, e.getChildCount());
                     }

                     childNodes = new ArrayList();
                  }

                  childIsLeaf = true;
               }

               for(int var15 = 0; var15 < ((List)childNodes).size(); ++var15) {
                  if(childIsLeaf) {
                     model.insertNodeInto(new DefaultMutableTreeNode(((List)childNodes).get(var15)), e, e.getChildCount());
                  } else {
                     model.insertNodeInto(new ExpandableTreeNode(((List)childNodes).get(var15)), e, e.getChildCount());
                  }
               }

               e.setHasBeenExpanded(true);
            }
         } catch (Exception var12) {
            DialogueUtil.handleException(var12, "Error loading contents of \'" + treeExpansionEvent.getPath().getLastPathComponent().toString() + "\'", "Load Error", true, Client.getMainFrame());
            throw new ExpandVetoException(treeExpansionEvent);
         } finally {
            this.setCursor(new Cursor(0));
         }
      }

   }

   class SaleFrameListener implements ActionListener {
      public static final int SAVE_SALE_ACTION = 1;
      public static final int NEW_SALE_BY_STATUS_ACTION = 2;
      public static final int QUICK_DISPATCH_SALE_ACTION = 3;
      public static final int PRINT_SALE_INVOICE_ACTION = 4;
      public static final int NEW_SALE_BY_CUSTOMER_ACTION = 5;
      public static final int PREVIEW_PRINT_SALE_INVOICE_ACTION = 6;
      private EditPanel editPanel;
      private int action;
      private DefaultMutableTreeNode selectedNode;

      public SaleFrameListener(EditPanel editPanel, DefaultMutableTreeNode selectedNode, int action) {
         this.editPanel = editPanel;
         this.selectedNode = selectedNode;
         this.action = action;
      }

      public void actionPerformed(ActionEvent evt) {
         try {
            SaleFrame.this.setCursor(new Cursor(3));
            if(this.action == 1) {
               if(this.editPanel.save()) {
                  DefaultMutableTreeNode ex = SaleFrame.this.insertNode(this.editPanel.getModel(), this.selectedNode);
                  Client.showInfoMessage("New Sale Saved");
               } else {
                  Client.showInfoMessage("Sale Saved");
               }
            } else {
               SalePanel salePanel;
               JButton saveButton;
               Sale action1;
               if(this.action == 2) {
                  SaleFrame.this.resetFrameMenu();
                  SaleStatus ex1 = (SaleStatus)this.selectedNode.getUserObject();
                  action1 = new Sale();
                  this.setNewSaleDefaults(action1);
                  action1.setStatus(ex1);
                  salePanel = new SalePanel(action1);
                  saveButton = new JButton(new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/Save16.gif")));
                  saveButton.setMargin(new Insets(0, 0, 0, 0));
                  saveButton.setToolTipText("Save Sale");
                  saveButton.setMnemonic('S');
                  saveButton.addActionListener(SaleFrame.this.new SaleFrameListener(salePanel, this.selectedNode, 1));
                  SaleFrame.this.frameMenu.add(saveButton);
                  SaleFrame.this.setRightComponent(salePanel);
                  SaleFrame.this.frameMenu.repaint();
               } else if(this.action == 5) {
                  SaleFrame.this.resetFrameMenu();
                  Customer ex2 = (Customer)this.selectedNode.getUserObject();
                  action1 = new Sale();
                  this.setNewSaleDefaults(action1);
                  action1.setCustomer(ex2);
                  salePanel = new SalePanel(action1);
                  saveButton = new JButton(new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/Save16.gif")));
                  saveButton.setMargin(new Insets(0, 0, 0, 0));
                  saveButton.setToolTipText("Save Sale");
                  saveButton.setMnemonic('S');
                  saveButton.addActionListener(SaleFrame.this.new SaleFrameListener(salePanel, this.selectedNode, 1));
                  SaleFrame.this.frameMenu.add(saveButton);
                  SaleFrame.this.setRightComponent(salePanel);
                  SaleFrame.this.frameMenu.repaint();
               } else if(this.action == 3) {
                  Dispatch ex3 = new Dispatch();
                  ex3.setSale((Sale)this.editPanel.getModel());
                  if(Dispatch.count(ex3, DispatchSearchMapFactory.getSaleSearchMap(), (TransactionContainer)null) != 0) {
                     throw new BusinessException("This Sale already has an associated Dispatch, so Quick-Dispatch is not available");
                  }

                  new QuickDispatchDialogue(Client.getMainFrame(), true, (Sale)this.editPanel.getModel());
               } else if(this.action == 4) {
                  PrinterGateway.handlePrintRequest(new InvoicePrinter((Sale)this.editPanel.getModel()), false);
               } else if(this.action == 6) {
                  PrinterGateway.handlePrintRequest(new InvoicePrinter((Sale)this.editPanel.getModel()), true);
               }
            }
         } catch (StaleModelException var12) {
            int action = DialogueUtil.getActionForStaleModelException(var12, Client.getMainFrame());
         } catch (UserInputException var13) {
            DialogueUtil.handleUserInputException(var13, "", "Invalid Input", Client.getMainFrame());
         } catch (BusinessException var14) {
            DialogueUtil.handleException(var14, "", "Invalid Action", false, Client.getMainFrame());
         } catch (Exception var15) {
            DialogueUtil.handleException(var15, "Error performing action", "Error", true, Client.getMainFrame());
         } finally {
            SaleFrame.this.setCursor(new Cursor(0));
         }

      }

      private void setNewSaleDefaults(Sale sale) {
         sale.setStatus(SaleStatus.PROCESSING_STATUS);
         sale.setSaleDate(Calendar.getInstance());
         sale.setPaymentMethod(PaymentMethodType.ON_ACCOUNT_PAYMENT_METHOD_TYPE);
      }
   }
}
