package vivyclient.model;

import java.util.Calendar;
import vivyclient.model.BaseModel;
import vivyclient.model.DurationType;
import vivyclient.model.Product;
import vivyclient.model.tablemap.BaseTableMap;
import vivyclient.model.tablemap.ProductMakeupTableMap;
import vivyclient.util.Settings;
import vivyclient.util.ViewUtil;

public class ProductMakeup extends BaseModel {
   private Product product;
   private Product part;
   private int quantity;
   private DurationType warrantyDurationType;
   private int warrantyDurationMultiplier;
   private String warrantyComments;

   public ProductMakeup() {
      this.initialise();
   }

   public ProductMakeup(int objectId) {
      this.initialise();
      this.objectId = objectId;
   }

   public ProductMakeup(int objectId, Product product, Product part, int quantity, int updateId, Calendar lastUpdate) {
      this.objectId = objectId;
      this.product = product;
      this.part = part;
      this.quantity = quantity;
      this.updateId = updateId;
      this.lastUpdate = lastUpdate;
   }

   private void initialise() {
      this.objectId = Settings.getNullInt();
      this.product = null;
      this.part = null;
      this.quantity = 0;
      this.updateId = Settings.getNullInt();
      this.lastUpdate = null;
   }

   public String toString() {
      return this.part != null?(this.part.getName() != null?this.quantity + " * " + ViewUtil.viewTruncate(this.part.getName(), 20):this.quantity + " * [" + this.part.getObjectId() + "]"):this.quantity + " * ???";
   }

   public BaseTableMap getTableMap() {
      return new ProductMakeupTableMap();
   }

   public Product getProduct() {
      return this.product;
   }

   public void setProduct(Product product) {
      this.product = product;
   }

   public Product getPart() {
      return this.part;
   }

   public void setPart(Product part) {
      this.part = part;
   }

   public int getQuantity() {
      return this.quantity;
   }

   public void setQuantity(int quantity) {
      this.quantity = quantity;
   }

   public DurationType getWarrantyDurationType() {
      return this.warrantyDurationType;
   }

   public void setWarrantyDurationType(DurationType warrantyDurationType) {
      this.warrantyDurationType = warrantyDurationType;
   }

   public int getWarrantyDurationMultiplier() {
      return this.warrantyDurationMultiplier;
   }

   public void setWarrantyDurationMultiplier(int warrantyDurationMultiplier) {
      this.warrantyDurationMultiplier = warrantyDurationMultiplier;
   }

   public String getWarrantyComments() {
      return this.warrantyComments;
   }

   public void setWarrantyComments(String warrantyComments) {
      this.warrantyComments = warrantyComments;
   }
}
