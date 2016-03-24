package vivyclient.model.searchMap;

import vivyclient.model.searchMap.BaseSearchMap;
import vivyclient.model.tablemap.TableMapAttribute;

class CategorySearchMap extends BaseSearchMap {
   public void initialise() {
      this.addWhereCriteria(new TableMapAttribute("category", "categoryId"));
      this.addOrderCriteria(new TableMapAttribute("name", "name"));
   }
}
