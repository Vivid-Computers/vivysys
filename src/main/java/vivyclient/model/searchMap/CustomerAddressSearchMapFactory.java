package vivyclient.model.searchMap;

import vivyclient.model.searchMap.BaseSearchMap;
import vivyclient.model.searchMap.CustomerSearchMap;

public class CustomerAddressSearchMapFactory {
   public static BaseSearchMap getCustomerSearchMap() {
      return new CustomerSearchMap();
   }
}
