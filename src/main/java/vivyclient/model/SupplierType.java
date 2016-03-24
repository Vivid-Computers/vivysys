package vivyclient.model;

import vivyclient.model.BaseModel;
import vivyclient.model.tablemap.BaseTableMap;
import vivyclient.model.tablemap.SupplierTypeTableMap;

public class SupplierType extends BaseModel {
   public static final int DELIVERY_SUPPLIER_TYPE_ID = 1;
   public static final SupplierType DELIVERY_SUPPLIER_TYPE = new SupplierType(1);
   private String name;
   private String description;

   public SupplierType() {
   }

   public SupplierType(int objectId) {
      super(objectId);
   }

   public String toString() {
      return this.name != null?this.name:super.toString();
   }

   public BaseTableMap getTableMap() {
      return new SupplierTypeTableMap();
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getDescription() {
      return this.description;
   }

   public void setDescription(String description) {
      this.description = description;
   }
}
