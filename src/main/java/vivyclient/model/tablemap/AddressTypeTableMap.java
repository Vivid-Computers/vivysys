package vivyclient.model.tablemap;

import vivyclient.model.tablemap.BaseTableMap;
import vivyclient.model.tablemap.TableMapAttribute;

public class AddressTypeTableMap extends BaseTableMap {
   private static final String TABLE_NAME = "AddressType";

   public String getTableName() {
      return "AddressType";
   }

   public void initialise() {
      this.addAttribute(new TableMapAttribute("name", "name"), true);
   }
}
