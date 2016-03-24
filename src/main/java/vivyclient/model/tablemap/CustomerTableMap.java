package vivyclient.model.tablemap;

import vivyclient.model.tablemap.BaseTableMap;
import vivyclient.model.tablemap.TableMapAttribute;

public class CustomerTableMap extends BaseTableMap {
   private static final String TABLE_NAME = "Customer";

   public String getTableName() {
      return "Customer";
   }

   public void initialise() {
      this.addAttribute(new TableMapAttribute("customerType", "customerTypeId"));
      this.addAttribute(new TableMapAttribute("surname", "surname"), true);
      this.addAttribute(new TableMapAttribute("firstName", "firstName"));
      this.addAttribute(new TableMapAttribute("organisationName", "organisationName"));
      this.addAttribute(new TableMapAttribute("customerSince", "customerSince"));
      this.addAttribute(new TableMapAttribute("phone", "phone"));
      this.addAttribute(new TableMapAttribute("phone2", "phone2"));
      this.addAttribute(new TableMapAttribute("cellphone", "cellphone"));
      this.addAttribute(new TableMapAttribute("fax", "fax"));
      this.addAttribute(new TableMapAttribute("loginEmail", "email"));
      this.addAttribute(new TableMapAttribute("email2", "email2"));
      this.addAttribute(new TableMapAttribute("password", "password"));
      this.addAttribute(new TableMapAttribute("customerGroup", "parent"));
      this.addAttribute(new TableMapAttribute("creditLimit", "creditLimit"));
      this.addAttribute(new TableMapAttribute("openingBalance", "openingBalance"));
      this.addAttribute(new TableMapAttribute("restrictedGroup", "restrictedGroup"));
      this.addAttribute(new TableMapAttribute("defaultBillingAddress", "defaultBillingAddressId"));
      this.addAttribute(new TableMapAttribute("defaultDeliveryAddress", "defaultDeliveryAddressId"));
      this.addAttribute(new TableMapAttribute("openingBalanceDueDate", "openingBalanceDueDate"));
   }
}
