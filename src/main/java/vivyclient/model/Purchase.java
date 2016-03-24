package vivyclient.model;

import java.math.BigDecimal;
import java.util.Calendar;
import vivyclient.model.Address;
import vivyclient.model.BaseModel;
import vivyclient.model.Supplier;
import vivyclient.model.tablemap.BaseTableMap;
import vivyclient.model.tablemap.PurchaseTableMap;

public class Purchase extends BaseModel {
   private Supplier supplier;
   private Calendar purchaseDate;
   private boolean hasGST;
   private BigDecimal freight;
   private Address supplierAddress;
   private String paymentMethod;
   private String paymentDetails;

   public BaseTableMap getTableMap() {
      return new PurchaseTableMap();
   }

   public Supplier getSupplier() {
      return this.supplier;
   }

   public void setSupplier(Supplier supplier) {
      this.supplier = supplier;
   }

   public Calendar getPurchaseDate() {
      return this.purchaseDate;
   }

   public void setPurchaseDate(Calendar purchaseDate) {
      this.purchaseDate = purchaseDate;
   }

   public boolean isHasGST() {
      return this.hasGST;
   }

   public void setHasGST(boolean hasGST) {
      this.hasGST = hasGST;
   }

   public BigDecimal getFreight() {
      return this.freight;
   }

   public void setFreight(BigDecimal freight) {
      this.freight = freight;
   }

   public Address getSupplierAddress() {
      return this.supplierAddress;
   }

   public void setSupplierAddress(Address supplierAddress) {
      this.supplierAddress = supplierAddress;
   }

   public String getPaymentMethod() {
      return this.paymentMethod;
   }

   public void setPaymentMethod(String paymentMethod) {
      this.paymentMethod = paymentMethod;
   }

   public String getPaymentDetails() {
      return this.paymentDetails;
   }

   public void setPaymentDetails(String paymentDetails) {
      this.paymentDetails = paymentDetails;
   }
}
