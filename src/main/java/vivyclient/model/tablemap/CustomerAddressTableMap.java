package vivyclient.model.tablemap;

import vivyclient.model.tablemap.BaseTableMap;
import vivyclient.model.tablemap.TableMapAttribute;

public class CustomerAddressTableMap extends BaseTableMap {
   private static final String TABLE_NAME = "CustomerAddress";

   public String getTableName() {
      return "CustomerAddress";
   }

   public void initialise() {
      this.addAttribute(new TableMapAttribute("customer", "customerId"));
      this.addAttribute(new TableMapAttribute("address", "addressId"));
   }
}
