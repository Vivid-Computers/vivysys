package vivyclient.model;

import vivyclient.data.TransactionContainer;
import vivyclient.model.Address;
import vivyclient.model.BaseModel;
import vivyclient.model.Customer;
import vivyclient.model.tablemap.BaseTableMap;
import vivyclient.model.tablemap.CustomerAddressTableMap;

public class CustomerAddress extends BaseModel {
   private Customer customer;
   private Address address;

   public BaseTableMap getTableMap() {
      return new CustomerAddressTableMap();
   }

   public Customer getCustomer() {
      return this.customer;
   }

   public void setCustomer(Customer customer) {
      this.customer = customer;
   }

   public Address getAddress() {
      return this.address;
   }

   public void setAddress(Address address) {
      this.address = address;
   }

   public String toString() {
      return this.address != null?this.address.toString():"CustomerAddress " + this.objectId;
   }

   public void save(TransactionContainer transaction) throws Exception {
      boolean transactionOwner = false;

      try {
         if(transaction == null) {
            transaction = new TransactionContainer();
            transactionOwner = true;
         }

         this.address.save(transaction);
         super.save(transaction);
         if(transactionOwner) {
            transaction.commit();
         }

      } catch (Exception var4) {
         if(transactionOwner) {
            transaction.rollback();
         }

         throw var4;
      }
   }
}
