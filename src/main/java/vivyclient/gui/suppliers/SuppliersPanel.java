package vivyclient.gui.suppliers;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
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
import vivyclient.gui.common.ModelCollectionPanel;
import vivyclient.gui.common.ModelDisplayContainer;
import vivyclient.gui.products.ExpandableTreeNode;
import vivyclient.gui.suppliers.SupplierPanel;
import vivyclient.model.BaseModel;
import vivyclient.model.Supplier;
import vivyclient.model.searchMap.BaseSearchMap;
import vivyclient.util.DialogueUtil;
import vivyclient.util.Settings;

public class SuppliersPanel extends JPanel implements ModelCollectionPanel, TreeWillExpandListener {
   public static final String DISPLAY_UNSORTED = "displayUnsorted";
   private static final String SUPPLIERS_NODE_TITLE = "Suppliers";
   private static Dimension DEFAULT_MINIMUM_PANEL_SIZE = new Dimension(10, 10);
   private String displayType;
   private ModelDisplayContainer parent;
   public static final int SAVE_SUPPLIER_ACTION = 1;
   public static final int ADD_SUPPLIER_ACTION = 2;
   public static final int DELETE_SUPPLIER_ACTION = 3;
   private JTree suppliersTree;
   private JSplitPane centerSplitPane;
   private JScrollPane suppliersTreeScrollPane;

   public SuppliersPanel(String displayType, ModelDisplayContainer parent) throws Exception {
      this.displayType = displayType;
      this.parent = parent;
      this.initComponents();
      this.centerSplitPane.setDividerLocation(Settings.getDividerLocation(parent.getName()));
      this.centerSplitPane.setResizeWeight(0.25D);
      this.suppliersTree.addTreeWillExpandListener(this);
      this.refreshSuppliersList(displayType);
   }

   private void refreshSuppliersList(String displayType) throws Exception {
      if(!displayType.equals("displayUnsorted")) {
         throw new AppRuntimeException();
      } else {
         DefaultMutableTreeNode root = new DefaultMutableTreeNode("Suppliers");
         Supplier criteria = new Supplier();
         List suppliers = Supplier.findAll(criteria, (BaseSearchMap)null, (TransactionContainer)null);
         Iterator i = suppliers.iterator();

         while(i.hasNext()) {
            root.add(new DefaultMutableTreeNode(i.next()));
         }

         this.suppliersTree.setModel(new DefaultTreeModel(root));
         this.suppliersTree.setRootVisible(true);
         this.suppliersTree.setShowsRootHandles(false);
         this.centerSplitPane.setRightComponent(new JPanel());
         this.resetFrameMenu();
      }
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
      this.suppliersTreeScrollPane = new JScrollPane();
      this.suppliersTree = new JTree();
      this.setLayout(new BorderLayout());
      this.centerSplitPane.setDividerSize(8);
      this.centerSplitPane.setOneTouchExpandable(true);
      this.suppliersTree.addTreeSelectionListener(new TreeSelectionListener() {
         public void valueChanged(TreeSelectionEvent evt) {
            SuppliersPanel.this.treeSelectionChange(evt);
         }
      });
      this.suppliersTreeScrollPane.setViewportView(this.suppliersTree);
      this.centerSplitPane.setLeftComponent(this.suppliersTreeScrollPane);
      this.add(this.centerSplitPane, "Center");
   }

   private void treeSelectionChange(TreeSelectionEvent evt) {
      try {
         this.parent.setContainerCursor(new Cursor(3));
         this.resetFrameMenu();
         if(this.suppliersTree.getSelectionPath() != null) {
            DefaultMutableTreeNode ex = (DefaultMutableTreeNode)this.suppliersTree.getSelectionPath().getLastPathComponent();
            Object selectedNodeObject = ex.getUserObject();
            if(selectedNodeObject.equals("Suppliers")) {
               this.supplierCollectionSelected(ex);
            } else if(selectedNodeObject instanceof Supplier) {
               this.supplierSelected(ex);
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

   private void supplierCollectionSelected(DefaultMutableTreeNode selectedNode) throws Exception {
      JButton newButton = new JButton(new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/New16.gif")));
      newButton.setMargin(new Insets(0, 0, 0, 0));
      newButton.setToolTipText("Add New Supplier");
      newButton.setMnemonic(KeyStroke.getKeyStroke(78, 2).getKeyCode());
      newButton.addActionListener(new SuppliersPanel.EditListener((EditPanel)null, selectedNode, 2));
      this.parent.addToJMenuBar(newButton);
      this.setRightComponent(new JPanel());
      this.parent.modelSelectionChange((BaseModel)null);
   }

   private void supplierSelected(DefaultMutableTreeNode selectedNode) throws Exception {
      SupplierPanel editPanel = new SupplierPanel((Supplier)selectedNode.getUserObject());
      JButton saveButton = new JButton(new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/Save16.gif")));
      saveButton.setMargin(new Insets(0, 0, 0, 0));
      saveButton.setToolTipText("Save Supplier");
      saveButton.setMnemonic(KeyStroke.getKeyStroke(83, 2).getKeyCode());
      saveButton.addActionListener(new SuppliersPanel.EditListener(editPanel, selectedNode, 1));
      this.parent.addToJMenuBar(saveButton);
      this.setRightComponent((JPanel)editPanel);
      this.parent.modelSelectionChange(editPanel.getModel());
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
               DefaultTreeModel model = (DefaultTreeModel)this.suppliersTree.getModel();
               Object nodeObject = e.getUserObject();
               if(!(nodeObject instanceof Supplier)) {
                  throw new AppRuntimeException();
               }

               e.setHasBeenExpanded(true);
            }
         } catch (Exception var8) {
            DialogueUtil.handleException(var8, "Error loading contents of \'" + treeExpansionEvent.getPath().getLastPathComponent().toString() + "\'", "Load Error", true, Client.getMainFrame());
            throw new ExpandVetoException(treeExpansionEvent);
         } finally {
            this.parent.setContainerCursor(new Cursor(0));
         }
      }

   }

   private DefaultMutableTreeNode insertNode(Object object, DefaultMutableTreeNode parent) throws Exception {
      DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(object);
      DefaultTreeModel model = (DefaultTreeModel)this.suppliersTree.getModel();
      model.insertNodeInto(newNode, parent, model.getChildCount(parent));
      TreePath insertedPath = new TreePath(newNode.getPath());
      this.suppliersTree.setSelectionPath(insertedPath);
      this.suppliersTree.scrollPathToVisible(insertedPath);
      return newNode;
   }

   public boolean closing() {
      Settings.setDividerLocation(this.parent.getName(), this.centerSplitPane.getDividerLocation());
      return true;
   }

   class EditListener implements ActionListener {
      private EditPanel editPanel;
      private int action;
      private DefaultMutableTreeNode selectedNode;

      public EditListener(EditPanel editPanel, DefaultMutableTreeNode selectedNode, int action) {
         this.editPanel = editPanel;
         this.selectedNode = selectedNode;
         this.action = action;
      }

      public void actionPerformed(ActionEvent evt) {
         try {
            SuppliersPanel.this.parent.setContainerCursor(new Cursor(3));
            if(this.action == 1) {
               if(this.editPanel.save()) {
                  SuppliersPanel.this.insertNode(this.editPanel.getModel(), this.selectedNode);
               }

               SuppliersPanel.this.parent.modelSelectionChange(this.editPanel.getModel());
            } else {
               if(this.action != 2) {
                  throw new AppRuntimeException();
               }

               Supplier ex = new Supplier();
               SupplierPanel action1 = new SupplierPanel(ex);
               SuppliersPanel.this.resetFrameMenu();
               JButton saveButton = new JButton(new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/Save16.gif")));
               saveButton.setMargin(new Insets(0, 0, 0, 0));
               saveButton.setToolTipText("Save New Supplier");
               saveButton.setMnemonic(KeyStroke.getKeyStroke(83, 2).getKeyCode());
               saveButton.addActionListener(SuppliersPanel.this.new EditListener(action1, this.selectedNode, 1));
               SuppliersPanel.this.parent.addToJMenuBar(saveButton);
               SuppliersPanel.this.setRightComponent(action1);
               SuppliersPanel.this.parent.modelSelectionChange(ex);
            }
         } catch (StaleModelException var10) {
            int action = DialogueUtil.getActionForStaleModelException(var10, Client.getMainFrame());
         } catch (UserInputException var11) {
            DialogueUtil.handleUserInputException(var11, "", "Invalid Input", Client.getMainFrame());
         } catch (Exception var12) {
            DialogueUtil.handleException(var12, "Error performing action", "Error", true, Client.getMainFrame());
         } finally {
            SuppliersPanel.this.parent.setContainerCursor(new Cursor(0));
         }

      }
   }
}
