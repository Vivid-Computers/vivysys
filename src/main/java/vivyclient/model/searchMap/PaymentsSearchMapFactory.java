package vivyclient.model.searchMap;

import vivyclient.model.searchMap.AllPaymentsCustomerOrderedSearchMap;
import vivyclient.model.searchMap.BaseSearchMap;
import vivyclient.model.searchMap.CustomerPaymentSearchMap;
import vivyclient.model.searchMap.CustomerPaymentsBeforeDateSearchMap;
import vivyclient.model.searchMap.CustomerPaymentsFromDateSearchMap;

public class PaymentsSearchMapFactory {
   public static BaseSearchMap getCustomerSearchMap() {
      return new CustomerPaymentSearchMap();
   }

   public static BaseSearchMap getFromDateCustomerSearchMap() {
      return new CustomerPaymentsFromDateSearchMap();
   }

   public static BaseSearchMap getBeforeDateCustomerSearchMap() {
      return new CustomerPaymentsBeforeDateSearchMap();
   }

   public static BaseSearchMap getAllCustomerOrderedSearchMap() {
      return new AllPaymentsCustomerOrderedSearchMap();
   }
}
