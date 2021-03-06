package vivyclient.model.searchMap;

import vivyclient.model.searchMap.BaseSearchMap;
import vivyclient.model.tablemap.TableMapAttribute;

class SaleSearchMap extends BaseSearchMap {
   public void initialise() {
      this.addWhereCriteria(new TableMapAttribute("sale", "saleId"));
      this.addOrderCriteria(new TableMapAttribute("dispatchDate", "dispatchDate"));
   }
}
