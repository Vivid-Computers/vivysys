package vivyclient.model;

import vivyclient.model.BaseModel;
import vivyclient.model.tablemap.BaseTableMap;
import vivyclient.model.tablemap.RegionTableMap;

public class Region extends BaseModel {
   public static final Region AUCKLAND_REGION = new Region(1);
   public static final Region OTHER_NORTH_ISLAND_REGION = new Region(2);
   public static final Region SOUTH_ISLAND_REGION = new Region(3);
   public static final Region INTERNATIONAL_REGION = new Region(4);
   private String name;

   public Region() {
   }

   public Region(int objectId) {
      this.objectId = objectId;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public BaseTableMap getTableMap() {
      return new RegionTableMap();
   }

   public String toString() {
      return this.name != null?this.name:"Region " + this.objectId;
   }
}
