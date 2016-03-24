package vivyclient.model.searchMap;

import vivyclient.model.searchMap.BaseSearchMap;
import vivyclient.model.searchMap.SubCategorySearchMap;

public class ProductSearchMapFactory {
   public static BaseSearchMap getSubCategorySearchMap() {
      return new SubCategorySearchMap();
   }
}
