package vivyclient.model.tablemap;

import vivyclient.model.tablemap.BaseTableMap;
import vivyclient.model.tablemap.TableMapAttribute;

public class SupplierTableMap extends BaseTableMap {
   private static final String TABLE_NAME = "Supplier";

   public String getTableName() {
      return "Supplier";
   }

   public void initialise() {
      this.addAttribute(new TableMapAttribute("supplierType", "supplierTypeId"));
      this.addAttribute(new TableMapAttribute("name", "name"));
      this.addAttribute(new TableMapAttribute("phone", "phone"));
      this.addAttribute(new TableMapAttribute("fax", "fax"));
      this.addAttribute(new TableMapAttribute("email", "email"));
      this.addAttribute(new TableMapAttribute("url", "url"));
      this.addAttribute(new TableMapAttribute("trackingUrl", "trackingUrl"));
   }
}
