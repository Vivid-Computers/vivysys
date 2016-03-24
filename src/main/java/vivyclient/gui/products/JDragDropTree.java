package vivyclient.gui.products;

import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.Autoscroll;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.Timer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import vivyclient.data.TransactionContainer;
import vivyclient.error.ErrorWriter;
import vivyclient.gui.products.ProductTree;
import vivyclient.model.Category;
import vivyclient.model.Product;
import vivyclient.model.SubCategory;

public class JDragDropTree extends JTree implements DropTargetListener, DragSourceListener, DragGestureListener, Autoscroll {
   DropTarget dropTarget = null;
   DragSource dragSource = null;
   private TreePath sourcePath;
   private static final int AUTOSCROLL_MARGIN = 8;
   private TreePath lastPath = null;
   private Point lastPoint = new Point();
   private Timer hoverTimer;
   private ProductTree root;
   private DefaultTreeModel model;
   private DefaultMutableTreeNode dragged = null;
   private boolean scroll = false;

   public JDragDropTree(ProductTree root) {
      super(new DefaultTreeModel(root));
      this.model = (DefaultTreeModel)this.treeModel;
      this.root = root;
      this.dropTarget = new DropTarget(this, this);
      this.dropTarget.setDefaultActions(2);
      this.dragSource = new DragSource();
      this.dragSource.createDefaultDragGestureRecognizer(this, 2, this);
      this.hoverTimer = new Timer(2000, new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            if(JDragDropTree.this.lastPath != null) {
               if(!JDragDropTree.this.isRootPath(JDragDropTree.this.lastPath)) {
                  if(JDragDropTree.this.isExpanded(JDragDropTree.this.lastPath)) {
                     JDragDropTree.this.collapsePath(JDragDropTree.this.lastPath);
                  } else {
                     JDragDropTree.this.expandPath(JDragDropTree.this.lastPath);
                  }

               }
            }
         }
      });
      this.hoverTimer.setRepeats(false);
   }

   public void dragEnter(DropTargetDragEvent event) {
      event.acceptDrag(2);
      this.scroll = true;
   }

   public void drop(DropTargetDropEvent event) {
      Point dropPoint = event.getLocation();
      TreePath path = this.getPathForLocation(dropPoint.x, dropPoint.y);
      this.scroll = false;
      if(path == null) {
         event.rejectDrop();
      } else {
         try {
            Transferable ufException = event.getTransferable();
            Object target = ((DefaultMutableTreeNode)path.getLastPathComponent()).getUserObject();
            this.setSelectionPath((TreePath)null);
            if(target instanceof Category && ufException.isDataFlavorSupported(SubCategory.SUBCATEGORY_FLAVOUR)) {
               Category targetSubCategory1 = (Category)target;
               event.acceptDrop(2);
               SubCategory dropped1 = (SubCategory)ufException.getTransferData(SubCategory.SUBCATEGORY_FLAVOUR);
               dropped1.setCategory(targetSubCategory1);

               try {
                  dropped1.save((TransactionContainer)null);
                  this.moveNode(this.dragged, (DefaultMutableTreeNode)this.dragged.getParent(), (DefaultMutableTreeNode)path.getLastPathComponent());
                  event.getDropTargetContext().dropComplete(true);
                  event.dropComplete(true);
               } catch (Exception var10) {
                  ErrorWriter.writeException(var10);
               }
            } else if(target instanceof SubCategory && ufException.isDataFlavorSupported(Product.PRODUCT_FLAVOUR)) {
               SubCategory targetSubCategory = (SubCategory)target;
               event.acceptDrop(2);
               Product dropped = (Product)ufException.getTransferData(Product.PRODUCT_FLAVOUR);
               dropped.setSubCategory(targetSubCategory);

               try {
                  dropped.save((TransactionContainer)null);
                  this.moveNode(this.dragged, (DefaultMutableTreeNode)this.dragged.getParent(), (DefaultMutableTreeNode)path.getLastPathComponent());
                  event.getDropTargetContext().dropComplete(true);
                  event.dropComplete(true);
               } catch (Exception var9) {
                  ErrorWriter.writeException(var9);
               }
            } else {
               Toolkit.getDefaultToolkit().beep();
               event.rejectDrop();
            }
         } catch (IOException var11) {
            ErrorWriter.writeException(var11);
         } catch (UnsupportedFlavorException var12) {
            ErrorWriter.writeException(var12);
            event.rejectDrop();
         }

      }
   }

   private void moveNode(DefaultMutableTreeNode node, DefaultMutableTreeNode currentParent, DefaultMutableTreeNode targetParent) {
      this.model.removeNodeFromParent(node);
      this.model.insertNodeInto(node, targetParent, targetParent.getChildCount());
      TreeNode[] path = node.getPath();
      this.scrollPathToVisible(new TreePath(node.getPath()));
   }

   public void dragGestureRecognized(DragGestureEvent event) {
      Point dragOrigin = event.getDragOrigin();
      TreePath path = this.getPathForLocation(dragOrigin.x, dragOrigin.y);
      if(path != null) {
         if(path.getPathCount() > 2) {
            Rectangle raPath = this.getPathBounds(path);
            JLabel lbl = (JLabel)this.getCellRenderer().getTreeCellRendererComponent(this, path.getLastPathComponent(), false, this.isExpanded(path), this.getModel().isLeaf(path.getLastPathComponent()), 0, false);
            lbl.setSize((int)raPath.getWidth(), (int)raPath.getHeight());
            BufferedImage ghostImage = new BufferedImage((int)raPath.getWidth(), (int)raPath.getHeight(), 3);
            this.setSelectionPath(path);
            this.dragged = (DefaultMutableTreeNode)path.getLastPathComponent();
            Transferable selected = (Transferable)this.dragged.getUserObject();
            this.sourcePath = path;
            event.startDrag((Cursor)null, ghostImage, new Point(5, 5), selected, this);
         }
      }
   }

   public void dragDropEnd(DragSourceDropEvent event) {
      this.hoverTimer.stop();
      this.scroll = false;
      if(event.getDropSuccess()) {
         System.out.println("[JDragDropTree] drag drop finished");
      }

   }

   public void dropActionChanged(DropTargetDragEvent event) {
   }

   public void dragEnter(DragSourceDragEvent event) {
   }

   public void dragExit(DragSourceEvent event) {
   }

   public void dragOver(DragSourceDragEvent event) {
   }

   public void dropActionChanged(DragSourceDragEvent event) {
   }

   public void dragExit(DropTargetEvent event) {
   }

   public void dragOver(DropTargetDragEvent e) {
      Point pt = e.getLocation();
      if(!pt.equals(this.lastPoint)) {
         this.lastPoint = pt;
         Graphics2D g2 = (Graphics2D)this.getGraphics();
         TreePath path = this.getPathForLocation(pt.x, pt.y);
         this.setSelectionPath(path);
         path = this.getClosestPathForLocation(pt.x, pt.y);
         if(path != this.lastPath) {
            this.lastPath = path;
            this.hoverTimer.restart();
         }

      }
   }

   public void autoscroll(Point pt) {
      if(this.scroll) {
         int nRow = this.getRowForLocation(pt.x, pt.y);
         if(nRow >= 0) {
            Rectangle raOuter = this.getBounds();
            nRow = pt.y + raOuter.y <= 8?(nRow <= 0?0:nRow - 1):(nRow < this.getRowCount() - 1?nRow + 1:nRow);
            this.scrollRowToVisible(nRow);
         }
      }
   }

   public Insets getAutoscrollInsets() {
      Rectangle raOuter = this.getBounds();
      Rectangle raInner = this.getParent().getBounds();
      return new Insets(raInner.y - raOuter.y + 8, raInner.x - raOuter.x + 8, raOuter.height - raInner.height - raInner.y + raOuter.y + 8, raOuter.width - raInner.width - raInner.x + raOuter.x + 8);
   }

   private boolean isRootPath(TreePath path) {
      return this.isRootVisible() && this.getRowForPath(path) == 0;
   }
}
