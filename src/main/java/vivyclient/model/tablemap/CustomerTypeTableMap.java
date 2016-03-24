package vivyclient.model.tablemap;

import vivyclient.model.tablemap.BaseTableMap;
import vivyclient.model.tablemap.TableMapAttribute;

public class CustomerTypeTableMap extends BaseTableMap {
   private static final String TABLE_NAME = "CustomerType";

   public String getTableName() {
      return "CustomerType";
   }

   public void initialise() {
      this.addAttribute(new TableMapAttribute("name", "name"), true);
   }
}
