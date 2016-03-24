package vivyclient.model;

import java.math.BigDecimal;
import vivyclient.model.BaseModel;
import vivyclient.model.Product;
import vivyclient.model.Purchase;
import vivyclient.model.tablemap.BaseTableMap;
import vivyclient.model.tablemap.SaleDetailTableMap;

public class PurchaseDetail extends BaseModel {
   private Purchase purchase;
   private Product product;
   private int quantity;
   private BigDecimal unitCost;
   private String warranty;

   public BaseTableMap getTableMap() {
      return new SaleDetailTableMap();
   }

   public Purchase getPurchase() {
      return this.purchase;
   }

   public void setPurchase(Purchase purchase) {
      this.purchase = purchase;
   }

   public Product getProduct() {
      return this.product;
   }

   public void setProduct(Product product) {
      this.product = product;
   }

   public int getQuantity() {
      return this.quantity;
   }

   public void setQuantity(int quantity) {
      this.quantity = quantity;
   }

   public BigDecimal getUnitCost() {
      return this.unitCost;
   }

   public void setUnitCost(BigDecimal unitCost) {
      this.unitCost = unitCost;
   }

   public String getWarranty() {
      return this.warranty;
   }

   public void setWarranty(String warranty) {
      this.warranty = warranty;
   }
}
