package vivyclient.model.searchMap;

import vivyclient.model.searchMap.BaseSearchMap;
import vivyclient.model.searchMap.DispatchDetailSearchMap;
import vivyclient.model.searchMap.SaleSearchMap;

public class DispatchSearchMapFactory {
   public static BaseSearchMap getDispatchDetailSearchMap() {
      return new DispatchDetailSearchMap();
   }

   public static BaseSearchMap getSaleSearchMap() {
      return new SaleSearchMap();
   }
}
