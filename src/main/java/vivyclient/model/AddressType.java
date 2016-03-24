package vivyclient.model;

import vivyclient.model.BaseModel;
import vivyclient.model.tablemap.AddressTypeTableMap;
import vivyclient.model.tablemap.BaseTableMap;

public class AddressType extends BaseModel {
   public static final AddressType STREET_ADDRESS_TYPE = new AddressType(1);
   public static final AddressType POSTAL_ADDRESS_TYPE = new AddressType(2);
   public static final AddressType BUSINESS_ADDRESS_TYPE = new AddressType(3);
   private static AddressTypeTableMap tableMap = new AddressTypeTableMap();
   private String name;

   public AddressType() {
   }

   public AddressType(int objectId) {
      super(objectId);
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String toString() {
      return this.name != null?this.name:"AddressType " + this.objectId;
   }

   public BaseTableMap getTableMap() {
      return tableMap;
   }
}
