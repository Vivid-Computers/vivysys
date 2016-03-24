package vivyclient.model.tablemap;

import vivyclient.model.tablemap.BaseTableMap;
import vivyclient.model.tablemap.TableMapAttribute;

public class SaleTableMap extends BaseTableMap {
   private static final String TABLE_NAME = "Sale";

   public String getTableName() {
      return "Sale";
   }

   public void initialise() {
      this.addAttribute(new TableMapAttribute("customer", "customerId"));
      this.addAttribute(new TableMapAttribute("site", "siteId"));
      this.addAttribute(new TableMapAttribute("saleDate", "saleDate"));
      this.addAttribute(new TableMapAttribute("deliveryAddress", "deliveryAddressId"));
      this.addAttribute(new TableMapAttribute("billingAddress", "billingAddressId"));
      this.addAttribute(new TableMapAttribute("hasGST", "hasGST"));
      this.addAttribute(new TableMapAttribute("freightCost", "freight"));
      this.addAttribute(new TableMapAttribute("paymentMethod", "paymentMethodTypeId"));
      this.addAttribute(new TableMapAttribute("paymentDetails1", "paymentDetails1"));
      this.addAttribute(new TableMapAttribute("paymentDetails2", "paymentDetails2"));
      this.addAttribute(new TableMapAttribute("paymentDetails3", "paymentDetails3"));
      this.addAttribute(new TableMapAttribute("customerComments", "customerComments"));
      this.addAttribute(new TableMapAttribute("saleComments", "saleComments"));
      this.addAttribute(new TableMapAttribute("custref", "custref"));
      this.addAttribute(new TableMapAttribute("status", "saleStatusId"));
      this.addAttribute(new TableMapAttribute("paymentDurationType", "paymentTermsDurationId"));
      this.addAttribute(new TableMapAttribute("paymentTermsMultiplier", "paymentTermsMultiplier"));
      this.addAttribute(new TableMapAttribute("saleDispatchedDate", "saleDispatchedDate"));
   }
}
