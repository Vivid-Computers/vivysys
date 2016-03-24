package vivyclient.model;

import vivyclient.model.BaseModel;
import vivyclient.model.SupplierType;
import vivyclient.model.tablemap.BaseTableMap;
import vivyclient.model.tablemap.SupplierTableMap;

public class Supplier extends BaseModel {
   private SupplierType supplierType;
   private String name;
   private String phone;
   private String fax;
   private String email;
   private String url;
   private String trackingUrl;

   public BaseTableMap getTableMap() {
      return new SupplierTableMap();
   }

   public SupplierType getSupplierType() {
      return this.supplierType;
   }

   public void setSupplierType(SupplierType supplierType) {
      this.supplierType = supplierType;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getPhone() {
      return this.phone;
   }

   public void setPhone(String phone) {
      this.phone = phone;
   }

   public String getFax() {
      return this.fax;
   }

   public void setFax(String fax) {
      this.fax = fax;
   }

   public String getEmail() {
      return this.email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getUrl() {
      return this.url;
   }

   public void setUrl(String url) {
      this.url = url;
   }

   public String getTrackingUrl() {
      return this.trackingUrl;
   }

   public void setTrackingUrl(String trackingUrl) {
      this.trackingUrl = trackingUrl;
   }

   public String toString() {
      return this.name != null?this.name:"Supplier: " + this.objectId;
   }
}
