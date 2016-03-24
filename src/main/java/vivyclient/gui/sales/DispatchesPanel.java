package vivyclient.gui.sales;

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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
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
import vivyclient.gui.products.ExpandableTreeNode;
import vivyclient.gui.sales.DispatchPanel;
import vivyclient.gui.sales.DispatchesDisplayContainer;
import vivyclient.model.Dispatch;
import vivyclient.model.Sale;
import vivyclient.model.searchMap.BaseSearchMap;
import vivyclient.model.searchMap.DispatchSearchMapFactory;
import vivyclient.print.PrinterGateway;
import vivyclient.print.sale.DispatchPackingSlipPrinter;
import vivyclient.util.DialogueUtil;
import vivyclient.util.Settings;

public class DispatchesPanel extends JPanel implements TreeWillExpandListener {
   public static final String DISPLAY_UNSORTED = "displayUnsorted";
   public static final String DISPLAY_BY_SALE = "displayBySale";
   public static final String DISPLAY_BY_DATE = "displayByDate";
   private static final String DISPATCHES_NODE_TITLE = "Dispatches";
   private static Dimension DEFAULT_MINIMUM_PANEL_SIZE = new Dimension(10, 10);
   private String displayType;
   private DispatchesDisplayContainer parent;
   public static final int SAVE_DISPATCH_ACTION = 1;
   public static final int ADD_DISPATCH_ACTION = 2;
   public static final int ADD_SALE_DISPATCH_ACTION = 3;
   public static final int DELETE_DISPATCH_ACTION = 4;
   public static final int PRINT_DISPATCH_INVOICE_ACTION = 5;
   public static final int PREVIEW_PRINT_DISPATCH_INVOICE_ACTION = 6;
   private JScrollPane dispatchTreeScrollPane;
   private JSplitPane centerSplitPane;
   private JTree dispatchTree;

   public DispatchesPanel(String displayType, DispatchesDisplayContainer parent) throws Exception {
      this.displayType = displayType;
      this.parent = parent;
      this.initComponents();
      this.centerSplitPane.setDividerLocation(Settings.getDividerLocation(parent.getName()));
      this.centerSplitPane.setResizeWeight(0.25D);
      this.dispatchTree.addTreeWillExpandListener(this);
      this.refreshDispatchList(displayType);
   }

   private void refreshDispatchList(String displayType) throws Exception {
      DefaultMutableTreeNode root;
      List sales;
      int i;
      if(displayType.equals("displayUnsorted")) {
         root = new DefaultMutableTreeNode("Dispatches");
         Dispatch criteria = new Dispatch();
         sales = Dispatch.findAll(criteria, (BaseSearchMap)null, (TransactionContainer)null);

         for(i = 0; i < sales.size(); ++i) {
            root.add(new DefaultMutableTreeNode(sales.get(i)));
         }

         this.dispatchTree.setModel(new DefaultTreeModel(root));
         this.dispatchTree.setRootVisible(true);
         this.dispatchTree.setShowsRootHandles(false);
      } else if(displayType.equals("displayBySale")) {
         root = new DefaultMutableTreeNode("Sales");
         Sale var6 = new Sale();
         sales = Sale.findAll(var6, (BaseSearchMap)null, (TransactionContainer)null);

         for(i = 0; i < sales.size(); ++i) {
            root.add(new ExpandableTreeNode(sales.get(i)));
         }

         this.dispatchTree.setModel(new DefaultTreeModel(root));
         this.dispatchTree.setRootVisible(false);
         this.dispatchTree.setShowsRootHandles(true);
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
      this.dispatchTreeScrollPane = new JScrollPane();
      this.dispatchTree = new JTree();
      this.setLayout(new BorderLayout());
      this.centerSplitPane.setDividerSize(8);
      this.centerSplitPane.setOneTouchExpandable(true);
      this.dispatchTree.addTreeSelectionListener(new TreeSelectionListener() {
         public void valueChanged(TreeSelectionEvent evt) {
            DispatchesPanel.this.customerSelectionChange(evt);
         }
      });
      this.dispatchTreeScrollPane.setViewportView(this.dispatchTree);
      this.centerSplitPane.setLeftComponent(this.dispatchTreeScrollPane);
      this.add(this.centerSplitPane, "Center");
   }

   private void customerSelectionChange(TreeSelectionEvent evt) {
      try {
         this.setCursor(new Cursor(3));
         this.resetFrameMenu();
         if(this.dispatchTree.getSelectionPath() != null) {
            DefaultMutableTreeNode ex = (DefaultMutableTreeNode)this.dispatchTree.getSelectionPath().getLastPathComponent();
            Object selectedNodeObject = ex.getUserObject();
            if(selectedNodeObject.equals("Dispatches")) {
               this.dispatchCollectionSelected(ex);
            } else if(selectedNodeObject instanceof Dispatch) {
               this.dispatchSelected(ex);
            } else if(selectedNodeObject instanceof Sale) {
               this.saleSelected(ex);
            } else {
               this.setRightComponent(new JPanel());
            }
         } else {
            this.setRightComponent(new JPanel());
         }
      } catch (Exception var7) {
         DialogueUtil.handleException(var7, "Selection error:", "Error", true, Client.getMainFrame());
      } finally {
         this.setCursor(new Cursor(0));
      }

   }

   private void dispatchCollectionSelected(DefaultMutableTreeNode selectedNode) throws Exception {
      JButton newButton = new JButton(new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/New16.gif")));
      newButton.setMargin(new Insets(0, 0, 0, 0));
      newButton.setToolTipText("Add New Dispatch");
      newButton.addActionListener(new DispatchesPanel.DispatchListener((EditPanel)null, selectedNode, 2));
      this.parent.addToJMenuBar(newButton);
      this.setRightComponent(new JPanel());
      this.parent.dispatchSelectionChange((Dispatch)null);
   }

   private void dispatchSelected(DefaultMutableTreeNode selectedNode) throws Exception {
      DispatchPanel dispatchPanel = new DispatchPanel((Dispatch)selectedNode.getUserObject());
      JButton saveButton = new JButton(new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/Save16.gif")));
      saveButton.setMargin(new Insets(0, 0, 0, 0));
      saveButton.setToolTipText("Save Dispatch");
      saveButton.setMnemonic(KeyStroke.getKeyStroke(83, 2).getKeyCode());
      saveButton.addActionListener(new DispatchesPanel.DispatchListener(dispatchPanel, selectedNode, 1));
      this.parent.addToJMenuBar(saveButton);
      JButton printButton = new JButton(new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/Print16.gif")));
      printButton.setMargin(new Insets(0, 0, 0, 0));
      printButton.setToolTipText("Print Dispatch Slip");
      printButton.setMnemonic(KeyStroke.getKeyStroke(80, 2).getKeyCode());
      printButton.addActionListener(new DispatchesPanel.DispatchListener(dispatchPanel, selectedNode, 5));
      this.parent.addToJMenuBar(printButton);
      JButton printPreviewButton = new JButton(new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/Magnify.gif")));
      printPreviewButton.setMargin(new Insets(0, 0, 0, 0));
      printPreviewButton.setToolTipText("Print Preview of Dispatch Slip");
      printPreviewButton.setMnemonic(KeyStroke.getKeyStroke(86, 2).getKeyCode());
      printPreviewButton.addActionListener(new DispatchesPanel.DispatchListener(dispatchPanel, selectedNode, 6));
      this.parent.addToJMenuBar(printPreviewButton);
      this.setRightComponent(dispatchPanel);
      this.parent.dispatchSelectionChange((Dispatch)dispatchPanel.getModel());
   }

   private void saleSelected(DefaultMutableTreeNode selectedNode) throws Exception {
      JButton newButton = new JButton(new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/New16.gif")));
      newButton.setMargin(new Insets(0, 0, 0, 0));
      newButton.setToolTipText("Add Dispatch");
      newButton.addActionListener(new DispatchesPanel.DispatchListener((EditPanel)null, selectedNode, 3));
      this.parent.addToJMenuBar(newButton);
      this.setRightComponent(new JPanel());
      this.parent.dispatchSelectionChange((Dispatch)null);
   }

   private void resetFrameMenu() {
      this.parent.resetJMenuBar();
   }

   public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
   }

   public void treeWillExpand(TreeExpansionEvent treeExpansionEvent) throws ExpandVetoException {
      if(treeExpansionEvent.getPath().getLastPathComponent() instanceof ExpandableTreeNode) {
         try {
            this.setCursor(new Cursor(3));
            ExpandableTreeNode e = (ExpandableTreeNode)treeExpansionEvent.getPath().getLastPathComponent();
            if(!e.getHasBeenExpanded()) {
               DefaultTreeModel model = (DefaultTreeModel)this.dispatchTree.getModel();
               Object nodeObject = e.getUserObject();
               if(!(nodeObject instanceof Sale)) {
                  throw new AppRuntimeException();
               }

               Sale sale = (Sale)nodeObject;
               Dispatch criteria = new Dispatch();
               criteria.setSale(sale);
               List saleDispatches = Dispatch.findAll(criteria, DispatchSearchMapFactory.getSaleSearchMap(), (TransactionContainer)null);

               for(int i = 0; i < saleDispatches.size(); ++i) {
                  model.insertNodeInto(new DefaultMutableTreeNode(saleDispatches.get(i)), e, e.getChildCount());
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

   private DefaultMutableTreeNode insertNode(Object object, DefaultMutableTreeNode parent) throws Exception {
      DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(object);
      DefaultTreeModel model = (DefaultTreeModel)this.dispatchTree.getModel();
      model.insertNodeInto(newNode, parent, model.getChildCount(parent));
      TreePath insertedPath = new TreePath(newNode.getPath());
      this.dispatchTree.setSelectionPath(insertedPath);
      this.dispatchTree.scrollPathToVisible(insertedPath);
      return newNode;
   }

   public void closing() {
      Settings.setDividerLocation(this.parent.getName(), this.centerSplitPane.getDividerLocation());
   }

   class DispatchListener implements ActionListener {
      private EditPanel editPanel;
      private int action;
      private DefaultMutableTreeNode selectedNode;

      public DispatchListener(EditPanel editPanel, DefaultMutableTreeNode selectedNode, int action) {
         this.editPanel = editPanel;
         this.selectedNode = selectedNode;
         this.action = action;
      }

      public void actionPerformed(ActionEvent evt) {
         try {
            DispatchesPanel.this.setCursor(new Cursor(3));
            DefaultMutableTreeNode ex;
            if(this.action == 1) {
               if(this.editPanel.save()) {
                  ex = DispatchesPanel.this.insertNode(this.editPanel.getModel(), this.selectedNode);
               }

               DispatchesPanel.this.parent.dispatchSelectionChange((Dispatch)this.editPanel.getModel());
            } else {
               Dispatch ex1;
               if(this.action == 2) {
                  ex1 = new Dispatch();
                  DispatchPanel action1 = new DispatchPanel(ex1);
                  DispatchesPanel.this.resetFrameMenu();
                  JButton dispatchPanel = new JButton(new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/Save16.gif")));
                  dispatchPanel.setMargin(new Insets(0, 0, 0, 0));
                  dispatchPanel.setToolTipText("Save New Dispatch");
                  dispatchPanel.addActionListener(DispatchesPanel.this.new DispatchListener(action1, this.selectedNode, 1));
                  DispatchesPanel.this.parent.addToJMenuBar(dispatchPanel);
                  DispatchesPanel.this.setRightComponent(action1);
                  DispatchesPanel.this.parent.dispatchSelectionChange((Dispatch)null);
               } else if(this.action == 3) {
                  ex = this.selectedNode;
                  Dispatch action2 = new Dispatch();
                  action2.setSale((Sale)ex.getUserObject());
                  DispatchPanel dispatchPanel1 = new DispatchPanel(action2);
                  DispatchesPanel.this.resetFrameMenu();
                  JButton saveButton = new JButton(new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/Save16.gif")));
                  saveButton.setMargin(new Insets(0, 0, 0, 0));
                  saveButton.setToolTipText("Save New Dispatch");
                  saveButton.addActionListener(DispatchesPanel.this.new DispatchListener(dispatchPanel1, this.selectedNode, 1));
                  DispatchesPanel.this.parent.addToJMenuBar(saveButton);
                  DispatchesPanel.this.setRightComponent(dispatchPanel1);
                  DispatchesPanel.this.parent.dispatchSelectionChange((Dispatch)null);
               } else if(this.action == 4) {
                  if(DialogueUtil.confirmForDelete("Dispatch", Client.getMainFrame())) {
                     ((Dispatch)this.selectedNode.getUserObject()).delete((TransactionContainer)null);
                     DispatchesPanel.this.parent.dispatchSelectionChange((Dispatch)null);
                  }
               } else if(this.action == 5) {
                  ex1 = ((DispatchPanel)this.editPanel).getDispatchForPrint();
                  PrinterGateway.handlePrintRequest(new DispatchPackingSlipPrinter(ex1), false);
               } else if(this.action == 6) {
                  ex1 = ((DispatchPanel)this.editPanel).getDispatchForPrint();
                  PrinterGateway.handlePrintRequest(new DispatchPackingSlipPrinter(ex1), true);
               }
            }
         } catch (StaleModelException var11) {
            int action = DialogueUtil.getActionForStaleModelException(var11, Client.getMainFrame());
         } catch (UserInputException var12) {
            DialogueUtil.handleUserInputException(var12, "", "Invalid Input", Client.getMainFrame());
         } catch (Exception var13) {
            DialogueUtil.handleException(var13, "Error performing action", "Error", true, Client.getMainFrame());
         } finally {
            DispatchesPanel.this.setCursor(new Cursor(0));
         }

      }
   }
}
