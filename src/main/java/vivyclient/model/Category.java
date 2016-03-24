package vivyclient.model;

import java.util.Calendar;
import vivyclient.model.BaseModel;
import vivyclient.model.ProductType;
import vivyclient.model.dao.BaseDao;
import vivyclient.model.dao.CategoryDao;
import vivyclient.model.tablemap.BaseTableMap;
import vivyclient.model.tablemap.CategoryTableMap;

public class Category extends BaseModel {
   private int categoryId;
   private String name;
   private int displayOrder;
   private String notes;
   private ProductType productType;

   public Category() {
      this.initialise();
   }

   public Category(int objectId) {
      this.initialise();
      this.objectId = objectId;
   }

   public Category(int objectId, String name, int displayOrder, String notes, ProductType productType, int updateId, Calendar lastUpdate) {
      this.objectId = objectId;
      this.name = name;
      this.displayOrder = displayOrder;
      this.notes = notes;
      this.productType = productType;
      this.updateId = updateId;
      this.lastUpdate = lastUpdate;
   }

   private void initialise() {
      this.objectId = -1;
      this.updateId = -1;
      this.lastUpdate = null;
      this.name = null;
      this.displayOrder = -1;
      this.notes = null;
      this.productType = null;
   }

   public String toString() {
      return this.name != null && this.name.trim().length() > 0?this.name:"Category: " + this.objectId;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public int getDisplayOrder() {
      return this.displayOrder;
   }

   public void setDisplayOrder(int displayOrder) {
      this.displayOrder = displayOrder;
   }

   public String getNotes() {
      return this.notes;
   }

   public void setNotes(String notes) {
      this.notes = notes;
   }

   public ProductType getProductType() {
      return this.productType;
   }

   public void setProductType(ProductType productType) {
      this.productType = productType;
   }

   public BaseDao getModelDao() {
      return new CategoryDao();
   }

   public BaseTableMap getTableMap() {
      return new CategoryTableMap();
   }
}
