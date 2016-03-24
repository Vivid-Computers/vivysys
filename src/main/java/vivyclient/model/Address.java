package vivyclient.model;

import java.util.ArrayList;
import java.util.List;
import vivyclient.model.AddressType;
import vivyclient.model.BaseModel;
import vivyclient.model.Region;
import vivyclient.model.tablemap.AddressTableMap;
import vivyclient.model.tablemap.BaseTableMap;

public class Address extends BaseModel {
   private AddressType addressType;
   private String deliveryName;
   private String addressLine1;
   private String addressLine2;
   private String city;
   private String country;
   private Region region;
   private boolean isOldAddress;

   public String toString() {
      String summary = this.addressLine1;
      if(this.addressLine2 != null) {
         summary = summary + ", " + this.addressLine2;
      }

      if(this.city != null) {
         summary = summary + ", " + this.city;
      }

      return summary;
   }

   public AddressType getAddressType() {
      return this.addressType;
   }

   public void setAddressType(AddressType addressType) {
      this.addressType = addressType;
   }

   public String getDeliveryName() {
      return this.deliveryName;
   }

   public void setDeliveryName(String deliveryName) {
      this.deliveryName = deliveryName;
   }

   public String getAddressLine1() {
      return this.addressLine1;
   }

   public void setAddressLine1(String addressLine1) {
      this.addressLine1 = addressLine1;
   }

   public String getAddressLine2() {
      return this.addressLine2;
   }

   public void setAddressLine2(String addressLine2) {
      this.addressLine2 = addressLine2;
   }

   public String getCity() {
      return this.city;
   }

   public void setCity(String city) {
      this.city = city;
   }

   public String getCountry() {
      return this.country;
   }

   public void setCountry(String country) {
      this.country = country;
   }

   public Region getRegion() {
      return this.region;
   }

   public void setRegion(Region region) {
      this.region = region;
   }

   public BaseTableMap getTableMap() {
      return new AddressTableMap();
   }

   public boolean getIsOldAddress() {
      return this.isOldAddress;
   }

   public void setIsOldAddress(boolean isOldAddress) {
      this.isOldAddress = isOldAddress;
   }

   public List getLines() {
      ArrayList lines = new ArrayList();
      if(this.deliveryName != null && this.deliveryName.trim().length() > 0) {
         lines.add(this.deliveryName);
      }

      lines.addAll(this.getNamelessLines());
      return lines;
   }

   public List getNamelessLines() {
      ArrayList lines = new ArrayList();
      if(this.addressLine1 != null && this.addressLine1.trim().length() > 0) {
         lines.add(this.addressLine1);
      }

      if(this.addressLine2 != null && this.addressLine2.trim().length() > 0) {
         lines.add(this.addressLine2);
      }

      if(this.city != null && this.city.trim().length() > 0) {
         lines.add(this.city);
      }

      if(this.country != null && this.country.trim().length() > 0) {
         lines.add(this.country);
      }

      return lines;
   }
}
