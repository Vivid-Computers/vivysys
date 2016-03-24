package vivyclient.model.searchMap;

import vivyclient.model.searchMap.BaseSearchMap;
import vivyclient.model.tablemap.TableMapAttribute;

class CustomerPaymentsBeforeDateSearchMap extends BaseSearchMap {
   public void initialise() {
      this.addWhereCriteria(new TableMapAttribute("customer", "customerId"));
      this.addWhereCriteria(new TableMapAttribute("date", "paymentDate"), "lessThan");
      this.addOrderCriteria(new TableMapAttribute("date", "paymentDate"));
   }
}
