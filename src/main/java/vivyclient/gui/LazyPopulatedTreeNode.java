package vivyclient.gui;

import javax.swing.tree.DefaultMutableTreeNode;

public class LazyPopulatedTreeNode extends DefaultMutableTreeNode {
   private boolean populated;

   public LazyPopulatedTreeNode(Object object) {
      super(object);
      this.populated = true;
   }

   public LazyPopulatedTreeNode(Object object, boolean populated) {
      super(object);
      this.populated = populated;
   }

   public boolean getPopulated() {
      return this.populated;
   }

   public void setPopulated(boolean populated) {
      this.populated = populated;
   }
}
