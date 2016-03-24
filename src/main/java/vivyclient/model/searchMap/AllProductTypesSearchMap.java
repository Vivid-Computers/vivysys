package vivyclient.model.searchMap;

import vivyclient.model.searchMap.BaseSearchMap;
import vivyclient.model.tablemap.TableMapAttribute;

class AllProductTypesSearchMap extends BaseSearchMap {
   public void initialise() {
      this.addOrderCriteria(new TableMapAttribute("name", "name"));
   }
}
