package vivyclient.model.tablemap;

import vivyclient.model.tablemap.BaseTableMap;
import vivyclient.model.tablemap.TableMapAttribute;

public class SupplierTypeTableMap extends BaseTableMap {
   private static final String TABLE_NAME = "SupplierType";

   public String getTableName() {
      return "SupplierType";
   }

   public void initialise() {
      this.addAttribute(new TableMapAttribute("name", "name"));
      this.addAttribute(new TableMapAttribute("description", "description"));
   }
}
