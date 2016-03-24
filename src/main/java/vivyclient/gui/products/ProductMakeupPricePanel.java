package vivyclient.gui.products;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import vivyclient.Client;
import vivyclient.data.TransactionContainer;
import vivyclient.exception.BusinessException;
import vivyclient.exception.StaleModelException;
import vivyclient.gui.DisplayPane;
import vivyclient.gui.PopupListener;
import vivyclient.gui.products.ProductMakeupDialogue;
import vivyclient.gui.products.ProductPriceDialogue;
import vivyclient.model.Product;
import vivyclient.model.ProductMakeup;
import vivyclient.model.ProductPrice;
import vivyclient.model.Site;
import vivyclient.model.searchMap.ProductMakeupSearchMapFactory;
import vivyclient.model.searchMap.ProductPriceSearchMapFactory;
import vivyclient.util.DialogueUtil;

public class ProductMakeupPricePanel extends JPanel {
   public static final int NEW_PRODUCT_PRICE_ACTION = 1;
   public static final int MODIFY_PRODUCT_PRICE_ACTION = 2;
   public static final int DELETE_PRODUCT_PRICE_ACTION = 3;
   public static final int NEW_PRODUCT_MAKEUP_ACTION = 4;
   public static final int MODIFY_PRODUCT_MAKEUP_ACTION = 5;
   public static final int DELETE_PRODUCT_MAKEUP_ACTION = 6;
   private Product product;
   private DisplayPane parent;
   private JScrollPane jScrollPane2;
   private JScrollPane jScrollPane1;
   private JTree productPriceTree;
   private JTree productMakeupTree;

   public ProductMakeupPricePanel(Product product, DisplayPane parent) throws Exception {
      this.product = product;
      this.parent = parent;
      this.initComponents();
      this.setValues();
      this.productPriceTree.addMouseListener(new ProductMakeupPricePanel.PricePopupListener(this, product, this.productPriceTree));
      this.productMakeupTree.addMouseListener(new ProductMakeupPricePanel.MakeupPopupListener(this, product, this.productMakeupTree));
   }

   private void setValues() throws Exception {
      DefaultMutableTreeNode root = new DefaultMutableTreeNode("Pricing");
      this.setPrices(root, this.product);
      this.productPriceTree.setModel(new DefaultTreeModel(root));
      Enumeration e = root.children();

      while(e.hasMoreElements()) {
         this.productPriceTree.expandPath(new TreePath(((DefaultMutableTreeNode)e.nextElement()).getPath()));
      }

      DefaultMutableTreeNode makeupRoot = new DefaultMutableTreeNode("Composition");
      this.setMakeup(makeupRoot, this.product);
      this.productMakeupTree.setModel(new DefaultTreeModel(makeupRoot));
   }

   private void setPrices(DefaultMutableTreeNode priceNode, Product product) throws Exception {
      List sites = Site.cachedFindAll(new Site());
      ProductPrice criteria = new ProductPrice();
      criteria.setProduct(product);
      List prices = ProductPrice.findAll(criteria, ProductPriceSearchMapFactory.getProductSearchMap(), (TransactionContainer)null);

      for(int i = 0; i < sites.size(); ++i) {
         Site site = (Site)sites.get(i);
         DefaultMutableTreeNode siteNode = new DefaultMutableTreeNode(site);

         for(int j = 0; j < prices.size(); ++j) {
            ProductPrice price = (ProductPrice)prices.get(j);
            if(price.getSite().equals(site)) {
               price.setSite(site);
               siteNode.add(new DefaultMutableTreeNode(price));
            }
         }

         priceNode.add(siteNode);
      }

   }

   private void setMakeup(DefaultMutableTreeNode makeupRoot, Product product) throws Exception {
      ProductMakeup criteria = new ProductMakeup();
      criteria.setProduct(product);
      List parts = ProductMakeup.findAll(criteria, ProductMakeupSearchMapFactory.getProductSearchMap(), (TransactionContainer)null);

      for(int i = 0; i < parts.size(); ++i) {
         ProductMakeup part = (ProductMakeup)parts.get(i);
         part.getPart().populate((TransactionContainer)null);
         makeupRoot.add(new DefaultMutableTreeNode(part));
      }

   }

   public void newPriceAdded(ProductPrice productPrice) throws Exception {
      DefaultMutableTreeNode pricing = (DefaultMutableTreeNode)this.productPriceTree.getModel().getRoot();

      for(int i = 0; i < pricing.getChildCount(); ++i) {
         DefaultMutableTreeNode siteNode = (DefaultMutableTreeNode)pricing.getChildAt(i);
         Site site = (Site)siteNode.getUserObject();
         if(site.getObjectId() == productPrice.getSite().getObjectId()) {
            ((DefaultTreeModel)this.productPriceTree.getModel()).insertNodeInto(new DefaultMutableTreeNode(productPrice), siteNode, siteNode.getChildCount());
         }
      }

   }

   public void priceDeleted(DefaultMutableTreeNode selectedNode) {
      ((DefaultTreeModel)this.productPriceTree.getModel()).removeNodeFromParent(selectedNode);
   }

   public void newPartAdded(ProductMakeup part) throws Exception {
      DefaultMutableTreeNode makeup = (DefaultMutableTreeNode)this.productMakeupTree.getModel().getRoot();
      ((DefaultTreeModel)this.productMakeupTree.getModel()).insertNodeInto(new DefaultMutableTreeNode(part), makeup, makeup.getChildCount());
   }

   public void partDeleted(DefaultMutableTreeNode selectedNode) {
      ((DefaultTreeModel)this.productMakeupTree.getModel()).removeNodeFromParent(selectedNode);
   }

   private void initComponents() {
      this.jScrollPane2 = new JScrollPane();
      this.productPriceTree = new JTree();
      this.jScrollPane1 = new JScrollPane();
      this.productMakeupTree = new JTree();
      this.setLayout(new GridBagLayout());
      this.productPriceTree.setForeground(new Color(0, 0, 0));
      this.productPriceTree.setShowsRootHandles(true);
      this.jScrollPane2.setViewportView(this.productPriceTree);
      GridBagConstraints gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.fill = 1;
      gridBagConstraints.insets = new Insets(0, 0, 0, 2);
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.weighty = 1.0D;
      this.add(this.jScrollPane2, gridBagConstraints);
      this.jScrollPane1.setViewportView(this.productMakeupTree);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.fill = 1;
      gridBagConstraints.insets = new Insets(0, 2, 0, 0);
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.weighty = 1.0D;
      this.add(this.jScrollPane1, gridBagConstraints);
   }

   class ProductMakeupPopupListener implements ActionListener {
      private ProductMakeupPricePanel makeupPanel;
      private ProductMakeup productMakeup;
      private int action;
      private DefaultMutableTreeNode selectedNode;

      public ProductMakeupPopupListener(ProductMakeupPricePanel makeupPanel, DefaultMutableTreeNode selectedNode, ProductMakeup productMakeup, int action) {
         this.makeupPanel = makeupPanel;
         this.selectedNode = selectedNode;
         this.productMakeup = productMakeup;
         this.action = action;
      }

      public void actionPerformed(ActionEvent evt) {
         try {
            if(this.action == 5) {
               (new ProductMakeupDialogue(this.makeupPanel, true, this.productMakeup, false)).setVisible(true);
            } else if(this.action == 4) {
               (new ProductMakeupDialogue(this.makeupPanel, true, this.productMakeup, true)).setVisible(true);
            } else if(this.action == 6 && DialogueUtil.confirmForDelete("ProductPrice", Client.getMainFrame())) {
               this.productMakeup.delete((TransactionContainer)null);
               this.makeupPanel.partDeleted(this.selectedNode);
            }
         } catch (StaleModelException var4) {
            int action = DialogueUtil.getActionForStaleModelException(var4, Client.getMainFrame());
         } catch (Exception var5) {
            DialogueUtil.handleException(var5, "Error accessing Product Makeup", "Error", true, Client.getMainFrame());
         }

      }
   }

   class ProductPricePopupListener implements ActionListener {
      private ProductMakeupPricePanel pricePanel;
      private ProductPrice productPrice;
      private int action;
      private DefaultMutableTreeNode selectedNode;

      public ProductPricePopupListener(ProductMakeupPricePanel pricePanel, DefaultMutableTreeNode selectedNode, ProductPrice productPrice, int action) {
         this.pricePanel = pricePanel;
         this.selectedNode = selectedNode;
         this.productPrice = productPrice;
         this.action = action;
      }

      public void actionPerformed(ActionEvent evt) {
         try {
            if(this.action == 2) {
               (new ProductPriceDialogue(this.pricePanel, true, this.productPrice, false)).setVisible(true);
            } else if(this.action == 3 && DialogueUtil.confirmForDelete("ProductPrice", Client.getMainFrame())) {
               this.productPrice.delete((TransactionContainer)null);
               this.pricePanel.priceDeleted(this.selectedNode);
            }
         } catch (StaleModelException var4) {
            int action = DialogueUtil.getActionForStaleModelException(var4, Client.getMainFrame());
         } catch (Exception var5) {
            DialogueUtil.handleException(var5, "Error accessing Product Price", "Error", true, Client.getMainFrame());
         }

      }
   }

   class SitePopupListener implements ActionListener {
      private ProductMakeupPricePanel pricePanel;
      private Site site;
      private Product product;
      private int action;

      public SitePopupListener(ProductMakeupPricePanel pricePanel, Site site, Product product, int action) {
         this.pricePanel = pricePanel;
         this.site = site;
         this.product = product;
         this.action = action;
      }

      public void actionPerformed(ActionEvent evt) {
         try {
            if(this.action == 1) {
               if(!this.product.exists((TransactionContainer)null)) {
                  throw new BusinessException("The Product must be saved before new Prices can be added");
               }

               ProductPrice ex = new ProductPrice();
               ex.setSite(this.site);
               ex.setProduct(this.product);
               (new ProductPriceDialogue(this.pricePanel, true, ex, true)).setVisible(true);
            }
         } catch (BusinessException var3) {
            DialogueUtil.handleException(var3, "Cannot create new Product Price", "Error", false, Client.getMainFrame());
         } catch (Exception var4) {
            DialogueUtil.handleException(var4, "Error displaying new Product Price dialogue", "Error", true, Client.getMainFrame());
         }

      }
   }

   class MakeupPopupListener extends PopupListener {
      private ProductMakeupPricePanel pricePanel;
      private Product product;
      private JTree tree;
      private JPopupMenu menu = new JPopupMenu();

      public MakeupPopupListener(ProductMakeupPricePanel pricePanel, Product product, JTree tree) {
         this.pricePanel = pricePanel;
         this.product = product;
         this.tree = tree;
      }

      public void popupRequested(MouseEvent e) {
         try {
            this.menu.removeAll();
            Object ex = null;
            TreePath selectedPath = this.tree.getPathForLocation(e.getX(), e.getY());
            if(selectedPath != null && selectedPath.getLastPathComponent() != null) {
               this.tree.setSelectionPath(selectedPath);
               DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)selectedPath.getLastPathComponent();
               ex = selectedNode.getUserObject();
               JMenuItem item;
               if(selectedNode.isRoot()) {
                  item = new JMenuItem("New Part");
                  item.setIcon(new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/New16.gif")));
                  ProductMakeup newPart = new ProductMakeup();
                  newPart.setProduct(this.product);
                  item.addActionListener(ProductMakeupPricePanel.this.new ProductMakeupPopupListener(this.pricePanel, selectedNode, newPart, 4));
                  this.menu.add(item);
               } else {
                  if(!(ex instanceof ProductMakeup)) {
                     return;
                  }

                  item = new JMenuItem("Edit Part");
                  item.setIcon(new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/Edit16.gif")));
                  item.addActionListener(ProductMakeupPricePanel.this.new ProductMakeupPopupListener(this.pricePanel, selectedNode, (ProductMakeup)ex, 5));
                  this.menu.add(item);
                  item = new JMenuItem("Delete Part");
                  item.setIcon(new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/Delete16.gif")));
                  item.addActionListener(ProductMakeupPricePanel.this.new ProductMakeupPopupListener(this.pricePanel, selectedNode, (ProductMakeup)ex, 6));
                  this.menu.add(item);
               }

               this.menu.show(e.getComponent(), e.getX(), e.getY());
            }
         } catch (Exception var7) {
            DialogueUtil.handleException(var7, "Error Displaying Context Menu", "Error", true, Client.getMainFrame());
         }

      }
   }

   class PricePopupListener extends PopupListener {
      private ProductMakeupPricePanel pricePanel;
      private Product product;
      private JTree tree;
      private JPopupMenu menu = new JPopupMenu();

      public PricePopupListener(ProductMakeupPricePanel pricePanel, Product product, JTree tree) {
         this.pricePanel = pricePanel;
         this.product = product;
         this.tree = tree;
      }

      public void popupRequested(MouseEvent e) {
         try {
            this.menu.removeAll();
            Object ex = null;
            TreePath selectedPath = this.tree.getPathForLocation(e.getX(), e.getY());
            if(selectedPath != null && selectedPath.getLastPathComponent() != null) {
               this.tree.setSelectionPath(selectedPath);
               DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)selectedPath.getLastPathComponent();
               ex = selectedNode.getUserObject();
               JMenuItem item;
               if(ex instanceof ProductPrice) {
                  item = new JMenuItem("Edit Price");
                  item.setIcon(new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/Edit16.gif")));
                  item.addActionListener(ProductMakeupPricePanel.this.new ProductPricePopupListener(this.pricePanel, selectedNode, (ProductPrice)ex, 2));
                  this.menu.add(item);
                  item = new JMenuItem("Delete Price");
                  item.setIcon(new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/Delete16.gif")));
                  item.addActionListener(ProductMakeupPricePanel.this.new ProductPricePopupListener(this.pricePanel, selectedNode, (ProductPrice)ex, 3));
                  this.menu.add(item);
               } else {
                  if(!(ex instanceof Site)) {
                     return;
                  }

                  item = new JMenuItem("Edit Site");
                  item.setIcon(new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/Edit16.gif")));
                  this.menu.add(item);
                  item = new JMenuItem("New Price");
                  item.setIcon(new ImageIcon(this.getClass().getResource("/vivyclient/gui/images/New16.gif")));
                  item.addActionListener(ProductMakeupPricePanel.this.new SitePopupListener(this.pricePanel, (Site)ex, this.product, 1));
                  this.menu.add(item);
               }

               this.menu.show(e.getComponent(), e.getX(), e.getY());
            }
         } catch (Exception var6) {
            DialogueUtil.handleException(var6, "Error Displaying Context Menu", "Error", true, Client.getMainFrame());
         }

      }
   }
}
