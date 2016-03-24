package vivyclient.model.searchMap;

import vivyclient.model.searchMap.BaseSearchMap;
import vivyclient.model.tablemap.TableMapAttribute;

class SupplierTypeSearchMap extends BaseSearchMap {
   public void initialise() {
      this.addWhereCriteria(new TableMapAttribute("supplierType", "supplierTypeId"));
      this.addOrderCriteria(new TableMapAttribute("name", "name"));
   }
}
