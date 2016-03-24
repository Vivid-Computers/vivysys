package vivyclient.gui.products;

import java.util.List;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import vivyclient.data.TransactionContainer;
import vivyclient.gui.TaskProgressMonitor;
import vivyclient.gui.products.ExpandableTreeNode;
import vivyclient.model.Category;
import vivyclient.model.Product;
import vivyclient.model.ProductType;
import vivyclient.model.SubCategory;
import vivyclient.model.searchMap.CategorySearchMapFactory;
import vivyclient.model.searchMap.ProductSearchMapFactory;
import vivyclient.model.searchMap.ProductTypeSearchMapFactory;
import vivyclient.model.searchMap.SubCategorySearchMapFactory;

public class ProductTree extends DefaultMutableTreeNode {
   private int targetSize;
   private TaskProgressMonitor progressMonitor;

   public ProductTree() throws Exception {
      super("Products");
      List productTypes = ProductType.findAll(new ProductType(), ProductTypeSearchMapFactory.getAllSearchMap(), (TransactionContainer)null);

      for(int i = 0; i < productTypes.size(); ++i) {
         super.add(new ExpandableTreeNode(productTypes.get(i)));
      }

   }

   public void newFill() {
      System.out.println("[ProductTree] newFill called");
   }

   private static void populateCategories(ProductType productType, DefaultMutableTreeNode node, DefaultTreeModel model) throws Exception {
      Category criteria = new Category();
      criteria.setProductType(productType);
      List categories = Category.findAll(criteria, CategorySearchMapFactory.getProductTypeSearchMap(), (TransactionContainer)null);

      for(int i = 0; i < categories.size(); ++i) {
         model.insertNodeInto(new ExpandableTreeNode(categories.get(i)), node, node.getChildCount());
      }

   }

   private static void populateSubCategories(Category category, DefaultMutableTreeNode node, DefaultTreeModel model) throws Exception {
      SubCategory criteria = new SubCategory();
      criteria.setCategory(category);
      List subCategories = SubCategory.findAll(criteria, SubCategorySearchMapFactory.getCategorySearchMap(), (TransactionContainer)null);

      for(int i = 0; i < subCategories.size(); ++i) {
         model.insertNodeInto(new ExpandableTreeNode(subCategories.get(i)), node, node.getChildCount());
      }

   }

   private static void populateProducts(SubCategory subCategory, DefaultMutableTreeNode node, DefaultTreeModel model) throws Exception {
      Product criteria = new Product();
      criteria.setSubCategory(subCategory);
      List products = Product.findAll(criteria, ProductSearchMapFactory.getSubCategorySearchMap(), (TransactionContainer)null);

      for(int i = 0; i < products.size(); ++i) {
         model.insertNodeInto(new DefaultMutableTreeNode(products.get(i)), node, node.getChildCount());
      }

   }

   public int getTargetSize() {
      return this.targetSize;
   }

   public static void populateNodeForExpansion(ExpandableTreeNode node, DefaultTreeModel model, TreeExpansionEvent treeExpansionEvent) throws Exception {
      Object nodeObject = node.getUserObject();
      if(!node.getHasBeenExpanded()) {
         if(nodeObject instanceof ProductType) {
            populateCategories((ProductType)nodeObject, node, model);
         } else if(nodeObject instanceof Category) {
            populateSubCategories((Category)nodeObject, node, model);
         } else if(nodeObject instanceof SubCategory) {
            populateProducts((SubCategory)nodeObject, node, model);
         }

         node.setHasBeenExpanded(true);
      }

   }
}
