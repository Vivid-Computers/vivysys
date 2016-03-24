package vivyclient.model.tablemap;

import vivyclient.model.tablemap.BaseTableMap;
import vivyclient.model.tablemap.TableMapAttribute;

public class ProductTypeTableMap extends BaseTableMap {
   private static final String TABLE_NAME = "ProductType";

   public String getTableName() {
      return "ProductType";
   }

   public void initialise() {
      this.addAttribute(new TableMapAttribute("name", "name"), true);
      this.addAttribute(new TableMapAttribute("displayOrder", "displayOrder"));
      this.addAttribute(new TableMapAttribute("notes", "notes"));
   }
}
