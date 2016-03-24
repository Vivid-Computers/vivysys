package vivyclient.model.searchMap;

import vivyclient.model.searchMap.BaseSearchMap;
import vivyclient.model.tablemap.TableMapAttribute;

class AllSitesSearchMap extends BaseSearchMap {
   public void initialise() {
      this.addOrderCriteria(new TableMapAttribute("name", "name"));
   }
}
