package vivyclient.model;

import vivyclient.model.BaseModel;
import vivyclient.model.tablemap.BaseTableMap;
import vivyclient.model.tablemap.CustomerTypeTableMap;

public class CustomerType extends BaseModel {
   public static final CustomerType PERSON_CUSTOMER_TYPE = new CustomerType(1);
   public static final CustomerType ORGANISATION_CUSTOMER_TYPE = new CustomerType(2);
   private String name;

   public CustomerType() {
   }

   public CustomerType(int objectId) {
      super(objectId);
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String toString() {
      return this.name != null?this.name:"CustomerType " + this.objectId;
   }

   public BaseTableMap getTableMap() {
      return new CustomerTypeTableMap();
   }
}
