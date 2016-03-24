package vivyclient.model.tablemap;

import vivyclient.model.tablemap.BaseTableMap;
import vivyclient.model.tablemap.TableMapAttribute;

public class PaymentTableMap extends BaseTableMap {
   public String getTableName() {
      return "Payment";
   }

   public void initialise() {
      this.addAttribute(new TableMapAttribute("customer", "customerId"));
      this.addAttribute(new TableMapAttribute("amount", "paymentAmount"));
      this.addAttribute(new TableMapAttribute("method", "paymentMethod"));
      this.addAttribute(new TableMapAttribute("date", "paymentDate"));
      this.addAttribute(new TableMapAttribute("details1", "paymentDetails1"));
      this.addAttribute(new TableMapAttribute("details2", "paymentDetails2"));
      this.addAttribute(new TableMapAttribute("details3", "paymentDetails3"));
   }
}
