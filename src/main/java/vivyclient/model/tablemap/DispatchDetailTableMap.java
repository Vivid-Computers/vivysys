package vivyclient.model.tablemap;

import vivyclient.model.tablemap.BaseTableMap;
import vivyclient.model.tablemap.TableMapAttribute;

public class DispatchDetailTableMap extends BaseTableMap {
   private static final String TABLE_NAME = "DispatchDetail";

   public String getTableName() {
      return "DispatchDetail";
   }

   public void initialise() {
      this.addAttribute(new TableMapAttribute("dispatch", "dispatchId"));
      this.addAttribute(new TableMapAttribute("shippedSaleDetail", "saleDetailId"));
      this.addAttribute(new TableMapAttribute("quantity", "quantity"));
      this.addAttribute(new TableMapAttribute("serialNumbers", "serialNumbers"));
   }
}
