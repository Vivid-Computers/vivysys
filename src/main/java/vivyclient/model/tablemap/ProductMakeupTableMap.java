package vivyclient.model.tablemap;

import vivyclient.model.tablemap.BaseTableMap;
import vivyclient.model.tablemap.TableMapAttribute;

public class ProductMakeupTableMap extends BaseTableMap {
   private static final String TABLE_NAME = "ProductMakeup";

   public String getTableName() {
      return "ProductMakeup";
   }

   public void initialise() {
      this.addAttribute(new TableMapAttribute("product", "productId"));
      this.addAttribute(new TableMapAttribute("part", "partId"));
      this.addAttribute(new TableMapAttribute("quantity", "quantity"));
      this.addAttribute(new TableMapAttribute("warrantyDurationType", "warrantyDurationTypeId"));
      this.addAttribute(new TableMapAttribute("warrantyDurationMultiplier", "warrantyDurationMultiplier"));
      this.addAttribute(new TableMapAttribute("warrantyComments", "warrantyComments"));
   }
}
