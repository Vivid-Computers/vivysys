package vivyclient.print.reports;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import vivyclient.data.TransactionContainer;
import vivyclient.exception.AppRuntimeException;
import vivyclient.model.Customer;
import vivyclient.model.Payment;
import vivyclient.model.Sale;
import vivyclient.model.dao.CustomerBalancesReportDao;
import vivyclient.model.searchMap.PaymentsSearchMapFactory;
import vivyclient.shared.Constants;
import vivyclient.util.Util;

public class CustomerBalances {
   public static final String SHOW_ALL_CUSTOMERS = "all";
   public static final String SHOW_IN_DEBT_CUSTOMERS = "debt";
   public static final String SHOW_OVERDUE_DEBT_CUSTOMERS = "overdue";
   public static final String ORDER_BY_CUSTOMER_NAME = "nameOrder";
   public static final String ORDER_BY_CUSTOMER_BALANCE = "balanceOrder";
   public static final String ORDER_BY_OVERDUE_BALANCE = "overdueOrder";
   private List customers;
   private String showType;
   private String orderType;

   public void populate() throws Exception {
      TransactionContainer transaction = new TransactionContainer(true);

      try {
         Payment criteria = new Payment();
         List payments = Payment.findAll(criteria, PaymentsSearchMapFactory.getAllCustomerOrderedSearchMap(), transaction);
         this.customers = CustomerBalancesReportDao.getCustomersSaleList(transaction);
         Iterator customerIterator = this.customers.iterator();
         Customer currentCustomer = new Customer();
         Iterator i = payments.iterator();

         while(i.hasNext()) {
            Payment payment = (Payment)i.next();

            while(!payment.getCustomer().equals(currentCustomer)) {
               currentCustomer = (Customer)customerIterator.next();
               currentCustomer.setPaymentList(new ArrayList());
            }

            currentCustomer.getPaymentList().add(payment);
         }

         while(customerIterator.hasNext()) {
            currentCustomer = (Customer)customerIterator.next();
            currentCustomer.setPaymentList(new ArrayList());
         }

         this.initialise();
      } finally {
         transaction.close();
      }
   }

   private void initialise() throws Exception {
      Iterator i = this.customers.iterator();

      while(i.hasNext()) {
         Customer customer = (Customer)i.next();
         BigDecimal sales = Constants.ZERO_BIG_DECIMAL;
         BigDecimal payments = Constants.ZERO_BIG_DECIMAL;
         BigDecimal overdueBalance = Constants.ZERO_BIG_DECIMAL;
         BigDecimal balance = Constants.ZERO_BIG_DECIMAL;

         Iterator p;
         BigDecimal saleAmount;
         for(p = customer.getSales().iterator(); p.hasNext(); balance = balance.add(saleAmount)) {
            Sale payment = (Sale)p.next();
            saleAmount = payment.getTotalCost();
            sales = sales.add(saleAmount);
            if(payment.paymentOverdue()) {
               overdueBalance = overdueBalance.add(saleAmount);
            }
         }

         if(customer.getPaymentList() == null) {
            throw new AppRuntimeException();
         }

         Payment payment1;
         for(p = customer.getPaymentList().iterator(); p.hasNext(); balance = balance.subtract(payment1.getAmount())) {
            payment1 = (Payment)p.next();
            payments = payments.add(payment1.getAmount());
            overdueBalance = overdueBalance.subtract(payment1.getAmount());
         }

         if(customer.getOpeningBalance() != null) {
            balance = balance.add(customer.getOpeningBalance());
            if(customer.getOpeningBalanceDueDate() != null && Util.getStartOfToday().after(customer.getOpeningBalanceDueDate())) {
               overdueBalance = overdueBalance.add(customer.getOpeningBalance());
            }
         }

         customer.setSaleTotal(sales);
         customer.setPaymentTotal(payments);
         customer.setOverdueBalance(overdueBalance);
         customer.setBalance(balance);
      }

      this.orderCustomers();
   }

   private void orderCustomers() {
      Comparator comparator;
      if(this.orderType.equals("balanceOrder")) {
         comparator = new Comparator() {
            public int compare(Object o1, Object o2) {
               return -1 * ((Customer)o1).getBalance().compareTo(((Customer)o2).getBalance());
            }

            public boolean equals(Object obj) {
               return false;
            }
         };
      } else if(this.orderType.equals("nameOrder")) {
         comparator = new Comparator() {
            public int compare(Object o1, Object o2) {
               return ((Customer)o1).getDefaultDeliveryName() == null?(((Customer)o2).getDefaultDeliveryName() != null?-1:0):(((Customer)o2).getDefaultDeliveryName() == null?1:((Customer)o1).getDefaultDeliveryName().compareTo(((Customer)o2).getDefaultDeliveryName()));
            }

            public boolean equals(Object obj) {
               return false;
            }
         };
      } else {
         if(!this.orderType.equals("overdueOrder")) {
            throw new AppRuntimeException();
         }

         comparator = new Comparator() {
            public int compare(Object o1, Object o2) {
               return -1 * ((Customer)o1).getOverdueBalance().compareTo(((Customer)o2).getOverdueBalance());
            }

            public boolean equals(Object obj) {
               return false;
            }
         };
      }

      Collections.sort(this.customers, comparator);
   }

   public List getCustomers() {
      return this.customers;
   }

   public void setCustomers(List customers) {
      this.customers = customers;
   }

   public String getShowType() {
      return this.showType;
   }

   public void setShowType(String showType) {
      this.showType = showType;
   }

   public String getOrderType() {
      return this.orderType;
   }

   public void setOrderType(String orderType) {
      this.orderType = orderType;
   }
}
