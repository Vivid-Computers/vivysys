package vivyclient.model.searchMap;

import vivyclient.model.searchMap.BaseSearchMap;
import vivyclient.model.tablemap.TableMapAttribute;

class AllPaymentsCustomerOrderedSearchMap extends BaseSearchMap {
   public void initialise() {
      this.addOrderCriteria(new TableMapAttribute("customer", "customerId"));
   }
}
