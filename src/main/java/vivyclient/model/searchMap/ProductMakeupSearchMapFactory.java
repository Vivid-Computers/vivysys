package vivyclient.model.searchMap;

import vivyclient.model.searchMap.BaseSearchMap;
import vivyclient.model.searchMap.PMProductSearchMap;

public class ProductMakeupSearchMapFactory {
   public static BaseSearchMap getProductSearchMap() {
      return new PMProductSearchMap();
   }
}
