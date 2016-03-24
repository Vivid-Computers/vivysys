package vivyclient.model.searchMap;

import vivyclient.model.searchMap.BaseSearchMap;
import vivyclient.model.tablemap.TableMapAttribute;

class CustomerPaymentSearchMap extends BaseSearchMap {
   public void initialise() {
      this.addWhereCriteria(new TableMapAttribute("customer", "customerId"));
      this.addOrderCriteria(new TableMapAttribute("date", "paymentDate"));
   }
}
