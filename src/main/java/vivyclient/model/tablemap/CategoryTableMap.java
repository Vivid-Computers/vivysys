package vivyclient.model.tablemap;

import vivyclient.model.tablemap.BaseTableMap;
import vivyclient.model.tablemap.TableMapAttribute;

public class CategoryTableMap extends BaseTableMap {
   private static final String TABLE_NAME = "Category";

   public String getTableName() {
      return "Category";
   }

   public void initialise() {
      this.addAttribute(new TableMapAttribute("productType", "productTypeId"));
      this.addAttribute(new TableMapAttribute("name", "name"));
      this.addAttribute(new TableMapAttribute("displayOrder", "displayOrder"));
      this.addAttribute(new TableMapAttribute("notes", "notes"));
   }
}
