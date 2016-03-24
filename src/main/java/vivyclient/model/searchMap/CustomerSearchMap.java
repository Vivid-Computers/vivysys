package vivyclient.model.searchMap;

import vivyclient.model.searchMap.BaseSearchMap;
import vivyclient.model.tablemap.TableMapAttribute;

class CustomerSearchMap extends BaseSearchMap {
   public void initialise() {
      this.addWhereCriteria(new TableMapAttribute("customer", "customerId"));
      this.addOrderCriteria(new TableMapAttribute("objectId", "id"));
   }
}
