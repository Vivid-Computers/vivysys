package vivyclient.model.tablemap;

import vivyclient.model.tablemap.BaseTableMap;
import vivyclient.model.tablemap.TableMapAttribute;

public class SubCategoryTableMap extends BaseTableMap {
   private static final String TABLE_NAME = "SubCategory";

   public String getTableName() {
      return "SubCategory";
   }

   public void initialise() {
      this.addAttribute(new TableMapAttribute("category", "categoryId"));
      this.addAttribute(new TableMapAttribute("name", "name"));
      this.addAttribute(new TableMapAttribute("displayOrder", "displayOrder"));
      this.addAttribute(new TableMapAttribute("notes", "notes"));
   }
}
