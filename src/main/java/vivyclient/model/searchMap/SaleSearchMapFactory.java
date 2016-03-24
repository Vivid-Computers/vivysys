package vivyclient.model.searchMap;

import vivyclient.model.searchMap.BaseSearchMap;
import vivyclient.model.tablemap.TableMapAttribute;

public class SaleSearchMapFactory {
   private static SaleSearchMapFactory factory = new SaleSearchMapFactory();
   private BaseSearchMap statusSearchMap = new SaleSearchMapFactory.StatusSearchMap();
   private BaseSearchMap customerSearchMap = new SaleSearchMapFactory.CustomerSearchMap();

   public static BaseSearchMap getSaleStatusSearchMap() {
      return factory.statusSearchMap;
   }

   public static BaseSearchMap getCustomerSearchMap() {
      return factory.customerSearchMap;
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
   }

   class CustomerSearchMap extends BaseSearchMap {
      public void initialise() {
         this.addWhereCriteria(new TableMapAttribute("customer", "customerId"));
         this.addOrderCriteria(new TableMapAttribute("saleDate", "saleDate"));
      }
   }

   private class StatusSearchMap extends BaseSearchMap {
      private StatusSearchMap() {
      }

      public void initialise() {
         this.addWhereCriteria(new TableMapAttribute("status", "saleStatusId"));
         this.addOrderCriteria(new TableMapAttribute("saleDate", "saleDate"));
      }

      // $FF: synthetic method
      StatusSearchMap(SaleSearchMapFactory.SyntheticClass_1 x1) {
         this();
      }
   }
}
