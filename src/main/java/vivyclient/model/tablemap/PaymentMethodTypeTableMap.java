package vivyclient.model.tablemap;

import vivyclient.model.tablemap.BaseTableMap;
import vivyclient.model.tablemap.TableMapAttribute;

public class PaymentMethodTypeTableMap extends BaseTableMap {
   private static final String TABLE_NAME = "PaymentMethodType";

   public String getTableName() {
      return "PaymentMethodType";
   }

   public void initialise() {
      this.addAttribute(new TableMapAttribute("name", "name"), true);
   }
}
