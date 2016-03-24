package vivyclient.model.searchMap;

import vivyclient.model.searchMap.BaseSearchMap;
import vivyclient.model.tablemap.TableMapAttribute;

class ProductTypeSearchMap extends BaseSearchMap {
   public void initialise() {
      this.addWhereCriteria(new TableMapAttribute("productType", "productTypeId"));
      this.addOrderCriteria(new TableMapAttribute("name", "name"));
   }
}
