package vivyclient.gui.products;

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
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.tree.DefaultTreeModel;
import vivyclient.Client;
import vivyclient.gui.products.ProductPickerTreeListener;
import vivyclient.gui.products.ProductTree;
import vivyclient.gui.products.ProductTreeWillExpandListener;
import vivyclient.model.Product;
import vivyclient.util.Settings;

public class ProductPickerDialogue extends JDialog {
   private Product selectedProduct;
   private Product hiddenSelectedProduct;
   private boolean isCancelled;
   private Object lock;
   private ProductTree productDataTree;
   private static final String NAME = "ProductPickerDialogue";
   private JButton bCancel;
   private JSeparator jSeparator1;
   private JTree productTree;
   private JScrollPane productTreeScrollPane;
   private JButton bOkay;
   private JLabel lTitle;

   public static Product getUserSelectedProduct() throws Exception {
      Object lock = new Object();
      ProductPickerDialogue dialogue = new ProductPickerDialogue(Client.getMainFrame(), true, lock);
      dialogue.show();

      while(dialogue.getSelectedProduct() == null && !dialogue.getIsCancelled()) {
         try {
            synchronized(lock) {
               lock.wait();
            }
         } catch (InterruptedException var5) {
            ;
         }
      }

      return dialogue.getSelectedProduct();
   }

   public ProductPickerDialogue(Frame parent, boolean modal, Object lock) throws Exception {
      super(parent, modal);
      this.initComponents();
      this.lock = lock;
      this.isCancelled = false;
      this.bOkay.setEnabled(false);
      this.initialiseTree();
      this.setSize(Settings.getWidth("ProductPickerDialogue"), Settings.getHeight("ProductPickerDialogue"));
      this.setLocation(Settings.getXPos("ProductPickerDialogue"), Settings.getYPos("ProductPickerDialogue"));
   }

   private void initialiseTree() throws Exception {
      this.productDataTree = new ProductTree();
      this.productTree.setRootVisible(false);
      this.productTree.setShowsRootHandles(true);
      this.productTree.addTreeSelectionListener(new ProductPickerTreeListener(this, this.productTree));
      this.productTree.addTreeWillExpandListener(new ProductTreeWillExpandListener((DefaultTreeModel)this.productTree.getModel()));
      this.productTree.setModel(new DefaultTreeModel(this.productDataTree));
   }

   private void initComponents() {
      this.lTitle = new JLabel();
      this.jSeparator1 = new JSeparator();
      this.productTreeScrollPane = new JScrollPane();
      this.productTree = new JTree();
      this.bOkay = new JButton();
      this.bCancel = new JButton();
      this.getContentPane().setLayout(new GridBagLayout());
      this.setTitle("Product Selection");
      this.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent evt) {
            ProductPickerDialogue.this.closeDialog(evt);
         }
      });
      this.lTitle.setFont(new Font("Arial", 1, 14));
      this.lTitle.setForeground(SystemColor.activeCaption);
      this.lTitle.setText("Select Product");
      GridBagConstraints gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.anchor = 17;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(5, 5, 3, 5);
      this.getContentPane().add(this.lTitle, gridBagConstraints);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.ipadx = 3;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(0, 0, 10, 0);
      this.getContentPane().add(this.jSeparator1, gridBagConstraints);
      this.productTreeScrollPane.setBorder((Border)null);
      this.productTreeScrollPane.setPreferredSize(new Dimension(78, 100));
      this.productTreeScrollPane.setViewportView(this.productTree);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 1;
      gridBagConstraints.ipadx = 200;
      gridBagConstraints.ipady = 180;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.weighty = 1.0D;
      gridBagConstraints.insets = new Insets(5, 5, 5, 5);
      this.getContentPane().add(this.productTreeScrollPane, gridBagConstraints);
      this.bOkay.setText("Okay");
      this.bOkay.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent evt) {
            ProductPickerDialogue.this.bOkayClick(evt);
         }
      });
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 3;
      gridBagConstraints.anchor = 13;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(5, 0, 5, 5);
      this.getContentPane().add(this.bOkay, gridBagConstraints);
      this.bCancel.setText("Cancel");
      this.bCancel.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent evt) {
            ProductPickerDialogue.this.bCancelClicked(evt);
         }
      });
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 3;
      gridBagConstraints.anchor = 13;
      gridBagConstraints.insets = new Insets(5, 0, 5, 5);
      this.getContentPane().add(this.bCancel, gridBagConstraints);
      this.pack();
   }

   private void bCancelClicked(MouseEvent evt) {
      this.selectedProduct = null;
      this.isCancelled = true;
      this.notifiedClose();
   }

   private void bOkayClick(MouseEvent evt) {
      this.selectedProduct = this.hiddenSelectedProduct;
      this.notifiedClose();
   }

   private void closeDialog(WindowEvent evt) {
      this.selectedProduct = null;
      this.isCancelled = true;
      this.notifiedClose();
   }

   private void notifiedClose() {
      Object var1 = this.lock;
      synchronized(this.lock) {
         Settings.setWidth("ProductPickerDialogue", (int)this.getSize().getWidth());
         Settings.setHeight("ProductPickerDialogue", (int)this.getSize().getHeight());
         Settings.setXPos("ProductPickerDialogue", this.getLocation().x);
         Settings.setYPos("ProductPickerDialogue", this.getLocation().y);
         this.setVisible(false);
         this.dispose();
         this.lock.notifyAll();
      }
   }

   public Product getHiddenSelectedProduct() {
      return this.hiddenSelectedProduct;
   }

   public void setHiddenSelectedProduct(Product hiddenSelectedProduct) {
      this.hiddenSelectedProduct = hiddenSelectedProduct;
   }

   public Product getSelectedProduct() {
      return this.selectedProduct;
   }

   public void setSelectedProduct(Product selectedProduct) {
      this.selectedProduct = selectedProduct;
   }

   public boolean getIsCancelled() {
      return this.isCancelled;
   }

   public void setIsCancelled(boolean isCancelled) {
      this.isCancelled = isCancelled;
   }

   public void setOkayButtonEnabled(boolean enabled) {
      this.bOkay.setEnabled(enabled);
   }
}
