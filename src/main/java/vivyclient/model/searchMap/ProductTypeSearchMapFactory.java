package vivyclient.model.searchMap;

import vivyclient.model.searchMap.AllProductTypesSearchMap;
import vivyclient.model.searchMap.BaseSearchMap;

public class ProductTypeSearchMapFactory {
   public static BaseSearchMap getAllSearchMap() {
      return new AllProductTypesSearchMap();
   }
}
