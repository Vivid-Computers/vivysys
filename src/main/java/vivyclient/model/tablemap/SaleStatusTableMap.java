package vivyclient.model.tablemap;

import vivyclient.model.tablemap.BaseTableMap;
import vivyclient.model.tablemap.TableMapAttribute;

public class SaleStatusTableMap extends BaseTableMap {
   private static final String TABLE_NAME = "SaleStatus";

   public String getTableName() {
      return "SaleStatus";
   }

   public void initialise() {
      this.addAttribute(new TableMapAttribute("name", "name"), true);
   }
}
