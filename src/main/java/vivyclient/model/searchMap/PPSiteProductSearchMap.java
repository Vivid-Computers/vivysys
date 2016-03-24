package vivyclient.model.searchMap;

import vivyclient.model.searchMap.BaseSearchMap;
import vivyclient.model.tablemap.TableMapAttribute;

class PPSiteProductSearchMap extends BaseSearchMap {
   public void initialise() {
      this.addWhereCriteria(new TableMapAttribute("product", "productId"));
      this.addWhereCriteria(new TableMapAttribute("site", "siteId"));
      this.addOrderCriteria(new TableMapAttribute("quantity", "quantity"));
      this.addOrderCriteria(new TableMapAttribute("unitPrice", "unitPrice"));
   }
}
