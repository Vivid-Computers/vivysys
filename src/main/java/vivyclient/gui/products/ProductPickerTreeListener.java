package vivyclient.gui.products;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import vivyclient.Client;
import vivyclient.gui.products.ProductPickerDialogue;
import vivyclient.model.Product;
import vivyclient.util.DialogueUtil;

class ProductPickerTreeListener implements TreeSelectionListener {
   private ProductPickerDialogue dialogue;
   private JTree tree;

   public ProductPickerTreeListener(ProductPickerDialogue dialogue, JTree tree) {
      this.dialogue = dialogue;
      this.tree = tree;
   }

   public void valueChanged(TreeSelectionEvent e) {
      try {
         if(this.tree.getSelectionPath() != null) {
            DefaultMutableTreeNode ex = (DefaultMutableTreeNode)this.tree.getSelectionPath().getLastPathComponent();
            Object selectedNodeObject = ex.getUserObject();
            if(selectedNodeObject instanceof Product) {
               this.productSelected((Product)selectedNodeObject);
            } else {
               this.nothingSelected();
            }
         } else {
            this.nothingSelected();
         }
      } catch (Exception var4) {
         this.nothingSelected();
         DialogueUtil.handleException(var4, "Selection error:", "Error", true, Client.getMainFrame());
      }

   }

   private void productSelected(Product product) {
      this.dialogue.setHiddenSelectedProduct(product);
      this.dialogue.setOkayButtonEnabled(true);
   }

   private void nothingSelected() {
      this.dialogue.setHiddenSelectedProduct((Product)null);
      this.dialogue.setOkayButtonEnabled(false);
   }
}
