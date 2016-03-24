package vivyclient.gui.products;

import vivyclient.gui.LazyPopulatedTreeNode;

public class ExpandableTreeNode extends LazyPopulatedTreeNode {
   private boolean hasBeenExpanded = false;

   public ExpandableTreeNode(Object object) {
      super(object);
   }

   public boolean isLeaf() {
      return false;
   }

   public boolean getHasBeenExpanded() {
      return this.hasBeenExpanded;
   }

   public void setHasBeenExpanded(boolean hasBeenExpanded) {
      this.hasBeenExpanded = hasBeenExpanded;
   }
}
