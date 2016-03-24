package vivyclient.model.searchMap;

import vivyclient.model.searchMap.BaseSearchMap;
import vivyclient.model.searchMap.CategorySearchMap;

public class SubCategorySearchMapFactory {
   public static BaseSearchMap getCategorySearchMap() {
      return new CategorySearchMap();
   }
}
