package vivyclient.model.searchMap;

import vivyclient.model.searchMap.BaseSearchMap;
import vivyclient.model.searchMap.SupplierTypeSearchMap;

public class SupplierSearchMapFactory {
   public static BaseSearchMap getSupplierTypeSearchMap() {
      return new SupplierTypeSearchMap();
   }
}
