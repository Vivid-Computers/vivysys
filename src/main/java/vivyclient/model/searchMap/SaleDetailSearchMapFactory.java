package vivyclient.model.searchMap;

import vivyclient.model.searchMap.BaseSearchMap;
import vivyclient.model.searchMap.SDSaleSearchMap;

public class SaleDetailSearchMapFactory {
   public static BaseSearchMap getSaleSearchMap() {
      return new SDSaleSearchMap();
   }
}
