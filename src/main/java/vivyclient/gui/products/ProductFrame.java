package vivyclient.gui.products;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.tree.DefaultTreeModel;
import vivyclient.Client;
import vivyclient.gui.DisplayPane;
import vivyclient.gui.IconBar;
import vivyclient.gui.products.ProductTree;
import vivyclient.gui.products.ProductTreeWillExpandListener;
import vivyclient.gui.products.TreeListener;
import vivyclient.util.DialogueUtil;
import vivyclient.util.Settings;

public class ProductFrame extends JInternalFrame implements Runnable, DisplayPane {
   private static Dimension DEFAULT_MINIMUM_PANEL_SIZE = new Dimension(10, 10);
   private static final String NAME = "ProductFrame";
   private ProductTree productDataTree;
   private JTree productTree;
   private Client parentClient;
   private JSplitPane splitPane;
   private IconBar iconBar;

   public ProductFrame(Client parentClient) {
      this.parentClient = parentClient;
      this.addInternalFrameListener(new InternalFrameAdapter() {
         public void internalFrameClosing(InternalFrameEvent e) {
            ProductFrame.this.closing(e);
         }
      });
      this.setTitle("Products");
      this.setClosable(true);
      this.setResizable(true);
      this.setMaximizable(true);
      this.setIconifiable(true);
      this.setSize(new Dimension(Settings.getWidth("ProductFrame"), Settings.getHeight("ProductFrame")));
      this.setLocation(Settings.getXPos("ProductFrame"), Settings.getYPos("ProductFrame"));
      this.iconBar = new IconBar(this);
      this.getContentPane().add(this.iconBar, "North");
      (new Thread(this)).start();
   }

   public void closing(InternalFrameEvent e) {
      Settings.setWidth("ProductFrame", (int)this.getSize().getWidth());
      Settings.setHeight("ProductFrame", (int)this.getSize().getHeight());
      Settings.setXPos("ProductFrame", this.getLocation().x);
      Settings.setYPos("ProductFrame", this.getLocation().y);
      Settings.setMaximized("ProductFrame", this.isMaximum());
      Settings.setDividerLocation("ProductFrame", this.splitPane.getDividerLocation());
      this.setVisible(false);
      this.dispose();
   }

   public void run() {
      try {
         this.productDataTree = new ProductTree();
         this.productTree = new JTree(this.productDataTree);
         this.productTree.setBorder((Border)null);
         this.productTree.setRootVisible(false);
         this.productTree.setShowsRootHandles(true);
         this.productTree.addTreeSelectionListener(new TreeListener(this, this.productTree, this.iconBar));
         this.productTree.addTreeWillExpandListener(new ProductTreeWillExpandListener((DefaultTreeModel)this.productTree.getModel()));
         JPanel e = new JPanel();
         e.add(new JLabel("Starting position"), "Center");
         this.splitPane = new JSplitPane(1, new JScrollPane(this.productTree), e);
         this.splitPane.setContinuousLayout(true);
         this.splitPane.setDividerSize(4);
         this.splitPane.setDividerLocation(Settings.getDividerLocation("ProductFrame"));
         this.splitPane.setResizeWeight(0.25D);
         this.splitPane.setBorder((Border)null);
         this.getContentPane().add(this.splitPane, "Center");
         this.setVisible(true);
      } catch (Exception var2) {
         DialogueUtil.handleException(var2, "Error Loading ProductFrame", "Loading Error", true, this.parentClient);
         this.dispose();
      }

   }

   private void initialiseMenu() {
   }

   public void setRightComponent(JComponent component) {
      int dividerLocation = this.splitPane.getDividerLocation();
      component.setMinimumSize(DEFAULT_MINIMUM_PANEL_SIZE);
      this.splitPane.setRightComponent(component);
      this.splitPane.setDividerLocation(dividerLocation);
      component.validate();
      this.validate();
   }

   public Component getRightComponent() {
      return this.splitPane.getRightComponent();
   }

   public IconBar getIconBar() {
      return this.iconBar;
   }

   public Component getOuterFrame() {
      return this;
   }
}
