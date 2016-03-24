package vivyclient.model.tablemap;

import vivyclient.model.tablemap.BaseTableMap;
import vivyclient.model.tablemap.TableMapAttribute;

public class AddressTableMap extends BaseTableMap {
   private static final String TABLE_NAME = "Address";

   public String getTableName() {
      return "Address";
   }

   public void initialise() {
      this.addAttribute(new TableMapAttribute("addressType", "addressTypeId"));
      this.addAttribute(new TableMapAttribute("deliveryName", "deliveryName"));
      this.addAttribute(new TableMapAttribute("addressLine1", "addressLine1"));
      this.addAttribute(new TableMapAttribute("addressLine2", "addressLine2"));
      this.addAttribute(new TableMapAttribute("city", "city"));
      this.addAttribute(new TableMapAttribute("country", "country"));
      this.addAttribute(new TableMapAttribute("region", "regionId"));
      this.addAttribute(new TableMapAttribute("isOldAddress", "isOldAddress"));
   }
}
