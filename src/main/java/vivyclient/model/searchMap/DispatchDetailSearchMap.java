package vivyclient.model.searchMap;

import vivyclient.model.searchMap.BaseSearchMap;
import vivyclient.model.tablemap.TableMapAttribute;

class DispatchDetailSearchMap extends BaseSearchMap {
   public void initialise() {
      this.addWhereCriteria(new TableMapAttribute("dispatch", "dispatchId"));
      this.addOrderCriteria(new TableMapAttribute("quantity", "quantity"));
   }
}
