package vivyclient.model.searchMap;

import vivyclient.model.searchMap.BaseSearchMap;
import vivyclient.model.searchMap.ProductTypeSearchMap;

public class CategorySearchMapFactory {
   public static BaseSearchMap getProductTypeSearchMap() {
      return new ProductTypeSearchMap();
   }
}
