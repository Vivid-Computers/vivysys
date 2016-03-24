package vivyclient.model.tablemap;

import vivyclient.model.tablemap.BaseTableMap;
import vivyclient.model.tablemap.TableMapAttribute;

public class DurationTypeTableMap extends BaseTableMap {
   private static final String TABLE_NAME = "DurationType";

   public String getTableName() {
      return "DurationType";
   }

   public void initialise() {
      this.addAttribute(new TableMapAttribute("name", "name"), true);
   }
}
