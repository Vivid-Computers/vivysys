package vivyclient.model.tablemap;

import vivyclient.model.tablemap.BaseTableMap;
import vivyclient.model.tablemap.TableMapAttribute;

public class PurchaseTableMap extends BaseTableMap {
   private static final String TABLE_NAME = "Purchase";

   public String getTableName() {
      return "Purchase";
   }

   public void initialise() {
      this.addAttribute(new TableMapAttribute("supplier", "supplierId"));
      this.addAttribute(new TableMapAttribute("purchaseDate", "purchaseDate"));
      this.addAttribute(new TableMapAttribute("hasGST", "hasGST"));
      this.addAttribute(new TableMapAttribute("freight", "freight"));
      this.addAttribute(new TableMapAttribute("supplierAddress", "supplierAddress"));
      this.addAttribute(new TableMapAttribute("paymentMethod", "paymentMethod"));
      this.addAttribute(new TableMapAttribute("paymentDetails", "paymentDetails"));
   }
}
