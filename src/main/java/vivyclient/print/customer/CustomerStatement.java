package vivyclient.print.customer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import vivyclient.data.TransactionContainer;
import vivyclient.model.Address;
import vivyclient.model.Customer;
import vivyclient.model.Payment;
import vivyclient.model.Sale;
import vivyclient.model.dao.CustomerStatementDao;
import vivyclient.model.searchMap.PaymentsSearchMapFactory;

public class CustomerStatement {
   private Customer customer;
   private Address address;
   private Calendar statementFromDate;
   private List paymentsList;
   private List salesList;
   private List combinedList;
   private List openingSalesList;
   private List openingPaymentsList;

   public void populate() throws Exception {
      TransactionContainer transaction = new TransactionContainer(true);

      try {
         this.openingSalesList = CustomerStatementDao.getSaleList(this.customer, this.statementFromDate, true, transaction);
         this.salesList = CustomerStatementDao.getSaleList(this.customer, this.statementFromDate, false, transaction);
         Payment criteria = new Payment();
         criteria.setCustomer(this.customer);
         criteria.setDate(this.statementFromDate);
         this.openingPaymentsList = Payment.findAll(criteria, PaymentsSearchMapFactory.getBeforeDateCustomerSearchMap(), transaction);
         this.paymentsList = Payment.findAll(criteria, PaymentsSearchMapFactory.getFromDateCustomerSearchMap(), transaction);
         this.createCombinedList();
      } finally {
         transaction.close();
      }

   }

   private void createCombinedList() {
      this.combinedList = new ArrayList(this.paymentsList);
      this.combinedList.addAll(this.salesList);
      Collections.sort(this.combinedList, new Comparator() {
         public int compare(Object o1, Object o2) {
            Date date1 = o1 instanceof Sale?((Sale)o1).getSaleDate().getTime():((Payment)o1).getDate().getTime();
            Date date2 = o2 instanceof Sale?((Sale)o2).getSaleDate().getTime():((Payment)o2).getDate().getTime();
            return date1.compareTo(date2);
         }
      });
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

   public Calendar getStatementFromDate() {
      return this.statementFromDate;
   }

   public void setStatementFromDate(Calendar statementFromDate) {
      this.statementFromDate = statementFromDate;
   }

   public void setLists(List paymentsList, List salesList) {
      this.paymentsList = paymentsList;
      this.salesList = salesList;
      this.createCombinedList();
   }

   public List getPaymentsList() {
      return this.paymentsList;
   }

   public List getSalesList() {
      return this.salesList;
   }

   public List getCombinedList() {
      return this.combinedList;
   }

   public List getOpeningSalesList() {
      return this.openingSalesList;
   }

   public void setOpeningSalesList(List openingSalesList) {
      this.openingSalesList = openingSalesList;
   }

   public List getOpeningPaymentsList() {
      return this.openingPaymentsList;
   }

   public void setOpeningPaymentsList(List openingPaymentsList) {
      this.openingPaymentsList = openingPaymentsList;
   }
}
