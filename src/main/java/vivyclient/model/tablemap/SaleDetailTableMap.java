package vivyclient.model.tablemap;

import vivyclient.model.tablemap.BaseTableMap;
import vivyclient.model.tablemap.TableMapAttribute;

public class SaleDetailTableMap extends BaseTableMap {
   private static final String TABLE_NAME = "SaleDetail";

   public String getTableName() {
      return "SaleDetail";
   }

   public void initialise() {
      this.addAttribute(new TableMapAttribute("sale", "saleId"));
      this.addAttribute(new TableMapAttribute("product", "productId"));
      this.addAttribute(new TableMapAttribute("warrantyDuration", "warrantyDurationTypeId"));
      this.addAttribute(new TableMapAttribute("warrantyDurationMultiplier", "warrantyDurationMultiplier"));
      this.addAttribute(new TableMapAttribute("warrantyComments", "warrantyComments"));
      this.addAttribute(new TableMapAttribute("quantity", "quantity"));
      this.addAttribute(new TableMapAttribute("unitPrice", "unitPrice"));
      this.addAttribute(new TableMapAttribute("comments", "comments"));
   }
}
