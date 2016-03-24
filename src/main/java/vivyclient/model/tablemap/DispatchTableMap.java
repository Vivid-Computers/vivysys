package vivyclient.model.tablemap;

import vivyclient.model.tablemap.BaseTableMap;
import vivyclient.model.tablemap.TableMapAttribute;

public class DispatchTableMap extends BaseTableMap {
   private static final String TABLE_NAME = "Dispatch";

   public String getTableName() {
      return "Dispatch";
   }

   public void initialise() {
      this.addAttribute(new TableMapAttribute("dispatchDate", "dispatchDate"));
      this.addAttribute(new TableMapAttribute("supplier", "supplierId"));
      this.addAttribute(new TableMapAttribute("trackingId", "trackingId"));
      this.addAttribute(new TableMapAttribute("sale", "saleId"));
      this.addAttribute(new TableMapAttribute("attention", "attention"));
   }
}
