package vivyclient.model;

import java.math.BigDecimal;
import vivyclient.model.BaseModel;
import vivyclient.model.DurationType;
import vivyclient.model.Product;
import vivyclient.model.Sale;
import vivyclient.model.tablemap.BaseTableMap;
import vivyclient.model.tablemap.SaleDetailTableMap;

public class SaleDetail extends BaseModel {
   private Sale sale;
   private Product product;
   private BigDecimal quantity;
   private BigDecimal unitPrice;
   private DurationType warrantyDuration;
   private int warrantyDurationMultiplier;
   private String warrantyComments;
   private BigDecimal dispatchedQuantity;
   private String comments;

   public SaleDetail() {
   }

   public SaleDetail(int objectId) {
      super(objectId);
   }

   public BigDecimal getLineTotal() {
      return this.unitPrice.multiply(this.quantity);
   }

   public BaseTableMap getTableMap() {
      return new SaleDetailTableMap();
   }

   public String toString() {
      return this.product != null?this.quantity.toString() + " * " + this.product.toString():"SaleDetail " + this.objectId;
   }

   public Sale getSale() {
      return this.sale;
   }

   public void setSale(Sale sale) {
      this.sale = sale;
   }

   public Product getProduct() {
      return this.product;
   }

   public void setProduct(Product product) {
      this.product = product;
   }

   public BigDecimal getQuantity() {
      return this.quantity;
   }

   public void setQuantity(BigDecimal quantity) {
      this.quantity = quantity;
   }

   public BigDecimal getUnitPrice() {
      return this.unitPrice;
   }

   public void setUnitPrice(BigDecimal unitPrice) {
      this.unitPrice = unitPrice;
   }

   public DurationType getWarrantyDuration() {
      return this.warrantyDuration;
   }

   public void setWarrantyDuration(DurationType warrantyDuration) {
      this.warrantyDuration = warrantyDuration;
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

   public BigDecimal getDispatchedQuantity() {
      return this.dispatchedQuantity;
   }

   public void setDispatchedQuantity(BigDecimal dispatchedQuantity) {
      this.dispatchedQuantity = dispatchedQuantity;
   }

   public String getComments() {
      return this.comments;
   }

   public void setComments(String comments) {
      this.comments = comments;
   }
}
