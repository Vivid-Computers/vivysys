package vivyclient.model;

import vivyclient.model.BaseModel;
import vivyclient.model.tablemap.BaseTableMap;
import vivyclient.model.tablemap.FeaturedLevelTableMap;

public class FeaturedLevel extends BaseModel {
   private String name;

   public BaseTableMap getTableMap() {
      return new FeaturedLevelTableMap();
   }

   public String toString() {
      return this.name != null?this.name:super.toString();
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }
}
