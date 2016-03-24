package vivyclient.model.searchMap;

import vivyclient.model.searchMap.BaseSearchMap;
import vivyclient.model.tablemap.TableMapAttribute;

public class CustomerSearchMapFactory {
   private static CustomerSearchMapFactory factory = new CustomerSearchMapFactory();
   private CustomerSearchMapFactory.CustomerGroupSearchMap customerGroupSearchMap = new CustomerSearchMapFactory.CustomerGroupSearchMap();

   public static BaseSearchMap getCustomerGroupSearchMap() {
      return factory.customerGroupSearchMap;
   }

   class CustomerGroupSearchMap extends BaseSearchMap {
      public void initialise() {
         this.addWhereCriteria(new TableMapAttribute("customerGroup", "parent"));
         this.addOrderCriteria(new TableMapAttribute("surname", "surname"));
         this.addOrderCriteria(new TableMapAttribute("organisationName", "organisationName"));
      }
   }
}
