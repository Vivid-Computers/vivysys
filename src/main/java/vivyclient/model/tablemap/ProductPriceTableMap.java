package vivyclient.model.tablemap;

import vivyclient.model.tablemap.BaseTableMap;
import vivyclient.model.tablemap.TableMapAttribute;

public class ProductPriceTableMap extends BaseTableMap {
   private static final String TABLE_NAME = "ProductPrice";

   public String getTableName() {
      return "ProductPrice";
   }

   public void initialise() {
      this.addAttribute(new TableMapAttribute("product", "productId"));
      this.addAttribute(new TableMapAttribute("quantity", "quantity"));
      this.addAttribute(new TableMapAttribute("site", "siteId"));
      this.addAttribute(new TableMapAttribute("unitPrice", "unitPrice"));
      this.addAttribute(new TableMapAttribute("specialPrice", "specialPrice"));
   }
}
