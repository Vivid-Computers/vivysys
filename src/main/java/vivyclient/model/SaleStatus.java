package vivyclient.model;

import vivyclient.model.BaseModel;
import vivyclient.model.tablemap.BaseTableMap;
import vivyclient.model.tablemap.SaleStatusTableMap;

public class SaleStatus extends BaseModel {
   public static final SaleStatus AWAITING_PROCESSING_STATUS = new SaleStatus(0);
   public static final SaleStatus PROCESSING_STATUS = new SaleStatus(1);
   public static final SaleStatus ORDER_DISPATCHED_STATUS = new SaleStatus(2);
   public static final SaleStatus CANCELLED_STATUS = new SaleStatus(3);
   private String name;

   public SaleStatus() {
   }

   public SaleStatus(int objectId) {
      super(objectId);
   }

   public String toString() {
      return this.name != null?this.name:"Sale Status " + this.objectId;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public BaseTableMap getTableMap() {
      return new SaleStatusTableMap();
   }
}
