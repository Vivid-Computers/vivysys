package vivyclient.model.tablemap;

import vivyclient.model.tablemap.BaseTableMap;
import vivyclient.model.tablemap.TableMapAttribute;

public class RegionTableMap extends BaseTableMap {
   private static final String TABLE_NAME = "Region";

   public String getTableName() {
      return "Region";
   }

   public void initialise() {
      this.addAttribute(new TableMapAttribute("name", "name"), true);
   }
}
