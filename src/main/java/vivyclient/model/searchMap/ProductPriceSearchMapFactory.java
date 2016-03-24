package vivyclient.model.searchMap;

import vivyclient.model.searchMap.BaseSearchMap;
import vivyclient.model.searchMap.PPProductSearchMap;
import vivyclient.model.searchMap.PPSiteProductSearchMap;

public class ProductPriceSearchMapFactory {
   public static BaseSearchMap getProductSearchMap() {
      return new PPProductSearchMap();
   }

   public static BaseSearchMap getSiteProductSearchMap() {
      return new PPSiteProductSearchMap();
   }
}
