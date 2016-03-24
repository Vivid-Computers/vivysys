package vivyclient.model.tablemap;

import vivyclient.model.tablemap.BaseTableMap;
import vivyclient.model.tablemap.TableMapAttribute;

public class LinkTypeTableMap extends BaseTableMap {
   private static final String TABLE_NAME = "LinkType";

   public String getTableName() {
      return "LinkType";
   }

   public void initialise() {
      this.addAttribute(new TableMapAttribute("name", "name"), true);
      this.addAttribute(new TableMapAttribute("urlStructure", "urlStructure"));
   }
}
