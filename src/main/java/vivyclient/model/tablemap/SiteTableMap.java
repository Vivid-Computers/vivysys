package vivyclient.model.tablemap;

import vivyclient.model.tablemap.BaseTableMap;
import vivyclient.model.tablemap.TableMapAttribute;

public class SiteTableMap extends BaseTableMap {
   private static final String TABLE_NAME = "Site";

   public String getTableName() {
      return "Site";
   }

   public void initialise() {
      this.addAttribute(new TableMapAttribute("name", "name"), true);
   }
}
