package vivyclient.gui.products;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import vivyclient.Client;
import vivyclient.exception.AppRuntimeException;
import vivyclient.exception.StaleModelException;
import vivyclient.exception.UserInputException;
import vivyclient.gui.IconBar;
import vivyclient.gui.ModifyPanel;
import vivyclient.gui.ModifyPanelListener;
import vivyclient.gui.products.CategoryPanel;
import vivyclient.gui.products.ExpandableTreeNode;
import vivyclient.gui.products.ProductFrame;
import vivyclient.gui.products.ProductPanel;
import vivyclient.gui.products.ProductTypePanel;
import vivyclient.gui.products.SubCategoryPanel;
import vivyclient.model.BaseModel;
import vivyclient.model.Category;
import vivyclient.model.Product;
import vivyclient.model.ProductType;
import vivyclient.model.SubCategory;
import vivyclient.util.DialogueUtil;

public class TreeListener implements TreeSelectionListener, ModifyPanelListener {
   private ProductFrame productFrame;
   private JTree tree;
   private boolean allowSelectionChangeEvent = true;
   private IconBar iconBar;
   private DefaultMutableTreeNode selectedNode;

   public TreeListener(ProductFrame productFrame, JTree tree, IconBar iconBar) {
      this.productFrame = productFrame;
      this.tree = tree;
      this.iconBar = iconBar;
      this.selectedNode = null;
   }

   public void valueChanged(TreeSelectionEvent e) {
      if(this.allowSelectionChangeEvent) {
         try {
            if(this.tree.getSelectionPath() != null && (!(this.productFrame.getRightComponent() instanceof ModifyPanel) || ((ModifyPanel)this.productFrame.getRightComponent()).exit())) {
               this.selectedNode = (DefaultMutableTreeNode)this.tree.getSelectionPath().getLastPathComponent();
               Object ex = this.selectedNode.getUserObject();
               if(ex instanceof Product) {
                  this.productSelected((Product)ex, this.selectedNode, e);
               } else if(ex instanceof SubCategory) {
                  this.subCategorySelected((SubCategory)ex, this.selectedNode, e);
               } else if(ex instanceof Category) {
                  this.categorySelected((Category)ex, this.selectedNode, e);
               } else if(ex instanceof ProductType) {
                  this.productTypeSelected((ProductType)ex, this.selectedNode, e);
               } else {
                  this.rootSelected(this.selectedNode, e);
               }
            }
         } catch (StaleModelException var4) {
            if(e.getOldLeadSelectionPath() != null) {
               this.allowSelectionChangeEvent = false;
               this.tree.setSelectionPath(e.getOldLeadSelectionPath());
               this.allowSelectionChangeEvent = true;
            }

            int action = DialogueUtil.getActionForStaleModelException(var4, Client.getMainFrame());
         } catch (UserInputException var5) {
            if(e.getOldLeadSelectionPath() != null) {
               this.allowSelectionChangeEvent = false;
               this.tree.setSelectionPath(e.getOldLeadSelectionPath());
               this.allowSelectionChangeEvent = true;
            }

            DialogueUtil.handleUserInputException(var5, "Bad User Input:", "Error", Client.getMainFrame());
         } catch (Exception var6) {
            if(e.getOldLeadSelectionPath() != null) {
               this.allowSelectionChangeEvent = false;
               this.tree.setSelectionPath(e.getOldLeadSelectionPath());
               this.allowSelectionChangeEvent = true;
            }

            DialogueUtil.handleException(var6, "Selection error:", "Error", true, Client.getMainFrame());
         }

      }
   }

   private void productSelected(Product product, DefaultMutableTreeNode node, TreeSelectionEvent e) throws Exception {
      this.productFrame.getIconBar().resetBar();
      this.productFrame.setRightComponent(new ProductPanel(product, this));
      this.productFrame.getIconBar().addSaveButton(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            TreeListener.this.saveProduct();
         }
      }, "Save Product");
   }

   private void subCategorySelected(SubCategory subCategory, DefaultMutableTreeNode node, TreeSelectionEvent e) throws Exception {
      this.productFrame.getIconBar().resetBar();
      this.productFrame.setRightComponent(new SubCategoryPanel(subCategory, this));
      this.productFrame.getIconBar().addNewButton(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            TreeListener.this.newProduct();
         }
      }, "New Product");
      this.productFrame.getIconBar().addSaveButton(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            TreeListener.this.saveSubCategory();
         }
      }, "Save SubCategory");
   }

   private void categorySelected(Category category, DefaultMutableTreeNode node, TreeSelectionEvent e) throws Exception {
      this.productFrame.getIconBar().resetBar();
      this.productFrame.setRightComponent(new CategoryPanel(category, this));
      this.productFrame.getIconBar().addNewButton(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            TreeListener.this.newSubCategory();
         }
      }, "New SubCategory");
      this.productFrame.getIconBar().addSaveButton(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            TreeListener.this.saveCategory();
         }
      }, "Save Category");
   }

   private void productTypeSelected(ProductType productType, DefaultMutableTreeNode node, TreeSelectionEvent e) throws Exception {
      this.productFrame.getIconBar().resetBar();
      this.productFrame.setRightComponent(new ProductTypePanel(productType, this));
      this.productFrame.getIconBar().addNewButton(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            TreeListener.this.newCategory();
         }
      }, "New Category");
      this.productFrame.getIconBar().addSaveButton(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            TreeListener.this.saveProductType();
         }
      }, "Save ProductType");
   }

   private void rootSelected(DefaultMutableTreeNode node, TreeSelectionEvent e) {
      this.productFrame.getIconBar().resetBar();
      this.productFrame.setRightComponent(new JLabel("Displaying loads\'a products!!!"));
   }

   private void newProduct() {
      try {
         Object e = ((DefaultMutableTreeNode)this.tree.getSelectionPath().getLastPathComponent()).getUserObject();
         if(!(e instanceof SubCategory)) {
            throw new UserInputException("SubCategory must be selected before selecting new Product", this.tree);
         }

         this.productFrame.getIconBar().resetBar();
         Product newProduct = new Product();
         newProduct.setSubCategory((SubCategory)e);
         this.productFrame.setRightComponent(new ProductPanel(newProduct, this));
         this.productFrame.getIconBar().addSaveButton(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
               TreeListener.this.saveProduct();
            }
         }, "Save Product");
      } catch (UserInputException var3) {
         DialogueUtil.handleUserInputException(var3, "Cannot create new Product:", "Error", this.productFrame);
      } catch (Exception var4) {
         DialogueUtil.handleException(var4, "Cannot create new Product:", "Error", true, this.productFrame);
      }

   }

   private boolean saveProduct() {
      boolean result = false;

      try {
         ((ModifyPanel)this.productFrame.getRightComponent()).save();
         result = true;
      } catch (StaleModelException var4) {
         DialogueUtil.getActionForStaleModelException(var4, this.productFrame);
      } catch (UserInputException var5) {
         DialogueUtil.handleUserInputException(var5, "Bad User Input:", "Save Error", this.productFrame);
      } catch (Exception var6) {
         DialogueUtil.handleException(var6, "Product could not be saved:", "Error Saving Product", true, this.productFrame);
      }

      return result;
   }

   private void newSubCategory() {
      try {
         Object e = ((DefaultMutableTreeNode)this.tree.getSelectionPath().getLastPathComponent()).getUserObject();
         if(!(e instanceof Category)) {
            throw new UserInputException("Category must be selected before selecting new SubCategory", this.tree);
         }

         this.productFrame.getIconBar().resetBar();
         SubCategory subCategory = new SubCategory();
         subCategory.setCategory((Category)e);
         this.productFrame.setRightComponent(new SubCategoryPanel(subCategory, this));
         this.productFrame.getIconBar().addSaveButton(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
               TreeListener.this.saveSubCategory();
            }
         }, "Save SubCategory");
      } catch (UserInputException var3) {
         DialogueUtil.handleUserInputException(var3, "Cannot create new SubCategory:", "Error", this.productFrame);
      } catch (Exception var4) {
         DialogueUtil.handleException(var4, "Cannot create new SubCategory:", "Error", true, this.productFrame);
      }

   }

   private boolean saveSubCategory() {
      boolean result = false;

      try {
         ((ModifyPanel)this.productFrame.getRightComponent()).save();
         result = true;
      } catch (StaleModelException var4) {
         DialogueUtil.getActionForStaleModelException(var4, this.productFrame);
      } catch (UserInputException var5) {
         DialogueUtil.handleUserInputException(var5, "Bad User Input:", "Save Error", this.productFrame);
      } catch (Exception var6) {
         DialogueUtil.handleException(var6, "SubCategory could not be saved:", "Error Saving SubCategory", true, this.productFrame);
      }

      return result;
   }

   private void newCategory() {
      try {
         Object e = ((DefaultMutableTreeNode)this.tree.getSelectionPath().getLastPathComponent()).getUserObject();
         if(!(e instanceof ProductType)) {
            throw new UserInputException("ProductType must be selected before selecting new Category", this.tree);
         }

         this.productFrame.getIconBar().resetBar();
         Category category = new Category();
         category.setProductType((ProductType)e);
         this.productFrame.setRightComponent(new CategoryPanel(category, this));
         this.productFrame.getIconBar().addSaveButton(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
               TreeListener.this.saveCategory();
            }
         }, "Save Category");
      } catch (UserInputException var3) {
         DialogueUtil.handleUserInputException(var3, "Cannot create new Category:", "Error", this.productFrame);
      } catch (Exception var4) {
         DialogueUtil.handleException(var4, "Cannot create new Category:", "Error", true, this.productFrame);
      }

   }

   private boolean saveCategory() {
      boolean result = false;

      try {
         ((ModifyPanel)this.productFrame.getRightComponent()).save();
         result = true;
      } catch (StaleModelException var4) {
         DialogueUtil.getActionForStaleModelException(var4, this.productFrame);
      } catch (UserInputException var5) {
         DialogueUtil.handleUserInputException(var5, "Bad User Input:", "Save Error", this.productFrame);
      } catch (Exception var6) {
         DialogueUtil.handleException(var6, "Category could not be saved:", "Error Saving Category", true, this.productFrame);
      }

      return result;
   }

   private boolean saveProductType() {
      boolean result = false;

      try {
         ((ModifyPanel)this.productFrame.getRightComponent()).save();
         result = true;
      } catch (StaleModelException var4) {
         DialogueUtil.getActionForStaleModelException(var4, this.productFrame);
      } catch (UserInputException var5) {
         DialogueUtil.handleUserInputException(var5, "Bad User Input:", "Save Error", this.productFrame);
      } catch (Exception var6) {
         DialogueUtil.handleException(var6, "ProductType could not be saved:", "Error Saving Category", true, this.productFrame);
      }

      return result;
   }

   public void insertNode(BaseModel model) throws Exception {
      if(this.selectedNode == null) {
         throw new AppRuntimeException("Cannot insert into tree: node not selected.");
      } else {
         Object newNode;
         if(model instanceof Product) {
            newNode = new DefaultMutableTreeNode(model);
         } else {
            newNode = new ExpandableTreeNode(model);
         }

         if(!(this.selectedNode instanceof ExpandableTreeNode) || ((ExpandableTreeNode)this.selectedNode).getHasBeenExpanded()) {
            ((DefaultTreeModel)this.tree.getModel()).insertNodeInto((MutableTreeNode)newNode, this.selectedNode, this.tree.getModel().getChildCount(this.selectedNode));
            TreePath newNodePath = new TreePath(((DefaultMutableTreeNode)newNode).getPath());
            this.tree.scrollPathToVisible(newNodePath);
            this.tree.setSelectionPath(newNodePath);
         }

      }
   }

   public void saveCompleted(BaseModel model, boolean savedNew) throws Exception {
      if(savedNew) {
         this.insertNode(model);
      }

   }
}
