package vivyclient.gui.products;

import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import vivyclient.Client;
import vivyclient.gui.products.ExpandableTreeNode;
import vivyclient.gui.products.ProductTree;
import vivyclient.util.DialogueUtil;

public class ProductTreeWillExpandListener implements TreeWillExpandListener {
   private DefaultTreeModel model;

   public ProductTreeWillExpandListener(DefaultTreeModel model) {
      this.model = model;
   }

   public void treeWillCollapse(TreeExpansionEvent treeExpansionEvent) throws ExpandVetoException {
   }

   public void treeWillExpand(TreeExpansionEvent treeExpansionEvent) throws ExpandVetoException {
      if(treeExpansionEvent.getPath().getLastPathComponent() instanceof ExpandableTreeNode) {
         try {
            ProductTree.populateNodeForExpansion((ExpandableTreeNode)treeExpansionEvent.getPath().getLastPathComponent(), this.model, treeExpansionEvent);
         } catch (Exception var3) {
            DialogueUtil.handleException(var3, "Error loading contents of \'" + treeExpansionEvent.getPath().getLastPathComponent().toString() + "\'", "Load Error", true, Client.getMainFrame());
            throw new ExpandVetoException(treeExpansionEvent);
         }
      }

   }
}
