package vivyclient.model.searchMap;

import vivyclient.model.searchMap.BaseSearchMap;
import vivyclient.model.tablemap.TableMapAttribute;

class SubCategorySearchMap extends BaseSearchMap {
   public void initialise() {
      this.addWhereCriteria(new TableMapAttribute("subCategory", "subCategoryId"));
      this.addOrderCriteria(new TableMapAttribute("name", "name"));
   }
}
