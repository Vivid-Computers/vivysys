package vivyclient.model;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import vivyclient.data.TransactionContainer;
import vivyclient.model.Address;
import vivyclient.model.BaseModel;
import vivyclient.model.CustomerAddress;
import vivyclient.model.CustomerType;
import vivyclient.model.Payment;
import vivyclient.model.searchMap.CustomerAddressSearchMapFactory;
import vivyclient.model.searchMap.CustomerSearchMapFactory;
import vivyclient.model.searchMap.PaymentsSearchMapFactory;
import vivyclient.model.tablemap.BaseTableMap;
import vivyclient.model.tablemap.CustomerTableMap;

public class Customer extends BaseModel {
   private CustomerType customerType;
   private String title;
   private String surname;
   private String firstName;
   private String organisationName;
   private Calendar customerSince;
   private String phone;
   private String phone2;
   private String fax;
   private String cellphone;
   private String loginEmail;
   private String email2;
   private String password;
   private BigDecimal creditLimit;
   private BigDecimal openingBalance;
   private Calendar openingBalanceDueDate;
   private Customer customerGroup;
   private boolean restrictedGroup;
   private Address defaultBillingAddress;
   private Address defaultDeliveryAddress;
   private List children;
   private List addressLinks;
   private List payments;
   private List sales;
   private BigDecimal paymentTotal;
   private BigDecimal saleTotal;
   private BigDecimal overdueBalance;
   private BigDecimal balance;

   public Customer() {
   }

   public Customer(int objectId) {
      super(objectId);
   }

   public String getSurname() {
      return this.surname;
   }

   public void setSurname(String surname) {
      this.surname = surname;
   }

   public String getFirstName() {
      return this.firstName;
   }

   public void setFirstName(String firstName) {
      this.firstName = firstName;
   }

   public Calendar getCustomerSince() {
      return this.customerSince;
   }

   public void setCustomerSince(Calendar customerSince) {
      this.customerSince = customerSince;
   }

   public String getPhone() {
      return this.phone;
   }

   public void setPhone(String phone) {
      this.phone = phone;
   }

   public String getPhone2() {
      return this.phone2;
   }

   public void setPhone2(String phone2) {
      this.phone2 = phone2;
   }

   public String getCellphone() {
      return this.cellphone;
   }

   public void setCellphone(String cellphone) {
      this.cellphone = cellphone;
   }

   public String getFax() {
      return this.fax;
   }

   public void setFax(String fax) {
      this.fax = fax;
   }

   public String getPassword() {
      return this.password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public Customer getCustomerGroup() {
      return this.customerGroup;
   }

   public void setCustomerGroup(Customer customerGroup) {
      this.customerGroup = customerGroup;
   }

   public BigDecimal getCreditLimit() {
      return this.creditLimit;
   }

   public void setCreditLimit(BigDecimal creditLimit) {
      this.creditLimit = creditLimit;
   }

   public BigDecimal getOpeningBalance() {
      return this.openingBalance;
   }

   public void setOpeningBalance(BigDecimal openingBalance) {
      this.openingBalance = openingBalance;
   }

   public boolean getRestrictedGroup() {
      return this.restrictedGroup;
   }

   public void setRestrictedGroup(boolean restrictedGroup) {
      this.restrictedGroup = restrictedGroup;
   }

   public String getOrganisationName() {
      return this.organisationName;
   }

   public void setOrganisationName(String organisationName) {
      this.organisationName = organisationName;
   }

   public CustomerType getCustomerType() {
      return this.customerType;
   }

   public void setCustomerType(CustomerType customerType) {
      this.customerType = customerType;
   }

   public String getEmail2() {
      return this.email2;
   }

   public void setEmail2(String email2) {
      this.email2 = email2;
   }

   public String getLoginEmail() {
      return this.loginEmail;
   }

   public void setLoginEmail(String loginEmail) {
      this.loginEmail = loginEmail;
   }

   public BaseTableMap getTableMap() {
      return new CustomerTableMap();
   }

   public String toString() {
      if(this.customerType != null) {
         if(CustomerType.PERSON_CUSTOMER_TYPE.equals(this.customerType)) {
            if(this.surname != null) {
               return this.surname + (this.firstName != null?", " + this.firstName:"");
            }
         } else if(this.organisationName != null) {
            return this.organisationName;
         }
      }

      return "Customer " + this.objectId;
   }

   public String getFullName() {
      String name = "";
      if(this.customerType.equals(CustomerType.PERSON_CUSTOMER_TYPE) && this.title != null) {
         name = this.title + " ";
      }

      return name + this.getDefaultDeliveryName();
   }

   public String getDefaultDeliveryName() {
      return this.customerType.equals(CustomerType.PERSON_CUSTOMER_TYPE)?(this.firstName != null?this.firstName + " " + this.surname:this.surname):this.organisationName;
   }

   public void save(TransactionContainer transaction) throws Exception {
      boolean transactionOwner = false;

      try {
         if(transaction == null) {
            transaction = new TransactionContainer();
            transactionOwner = true;
         }

         super.save(transaction);
         saveAll(this.children, transaction);
         saveAll(this.addressLinks, transaction);
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

   public int getAddressLinkCount() throws Exception {
      this.lazyAddressLinkInitialise();
      return this.addressLinks.size();
   }

   public CustomerAddress getAddressLink(int index) throws Exception {
      this.lazyAddressLinkInitialise();
      return (CustomerAddress)this.addressLinks.get(index);
   }

   public void addAddressLink(CustomerAddress addressLink) throws Exception {
      this.lazyAddressLinkInitialise();
      this.addressLinks.add(addressLink);
   }

   public void removeAddressLink(CustomerAddress addressLink) throws Exception {
      this.lazyAddressLinkInitialise();
      this.addressLinks.remove(addressLink);
      this.addChildForDeletion(addressLink);
   }

   private void lazyAddressLinkInitialise() throws Exception {
      if(this.addressLinks == null) {
         TransactionContainer transaction = new TransactionContainer(true);

         try {
            CustomerAddress criteria = new CustomerAddress();
            criteria.setCustomer(this);
            this.addressLinks = CustomerAddress.findAll(criteria, CustomerAddressSearchMapFactory.getCustomerSearchMap(), transaction);

            for(int i = 0; i < this.addressLinks.size(); ++i) {
               ((CustomerAddress)this.addressLinks.get(i)).getAddress().populate(transaction);
            }
         } finally {
            transaction.close();
         }
      }

   }

   public int getChildCount() throws Exception {
      this.lazyChildInitialise();
      return this.children.size();
   }

   public Customer getChild(int index) throws Exception {
      this.lazyChildInitialise();
      return (Customer)this.children.get(index);
   }

   public void addChild(Customer child) throws Exception {
      this.lazyChildInitialise();
      this.children.add(child);
   }

   public void removeChild(Customer child) throws Exception {
      this.lazyChildInitialise();
      this.children.remove(child);
      this.addChildForDeletion(child);
   }

   private void lazyChildInitialise() throws Exception {
      if(this.children == null) {
         TransactionContainer transaction = new TransactionContainer(true);

         try {
            Customer criteria = new Customer();
            criteria.setCustomerGroup(this);
            this.children = findAll(criteria, CustomerSearchMapFactory.getCustomerGroupSearchMap(), transaction);
         } finally {
            transaction.close();
         }
      }

   }

   public int getPaymentCount() throws Exception {
      this.lazyPaymentInitialise();
      return this.payments.size();
   }

   public Payment getPayment(int index) throws Exception {
      this.lazyPaymentInitialise();
      return (Payment)this.payments.get(index);
   }

   public void addChild(Payment payment) throws Exception {
      this.lazyPaymentInitialise();
      this.payments.add(payment);
   }

   public void removePayment(Payment payment) throws Exception {
      this.lazyPaymentInitialise();
      this.payments.remove(payment);
      this.addChildForDeletion(payment);
   }

   public List getPaymentList() {
      return this.payments;
   }

   public void setPaymentList(List payments) {
      this.payments = payments;
   }

   private void lazyPaymentInitialise() throws Exception {
      if(this.payments == null) {
         TransactionContainer transaction = new TransactionContainer(true);

         try {
            Payment criteria = new Payment();
            criteria.setCustomer(this);
            this.payments = Payment.findAll(criteria, PaymentsSearchMapFactory.getCustomerSearchMap(), transaction);
         } finally {
            transaction.close();
         }
      }

   }

   public String getTitle() {
      return this.title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public Address getDefaultBillingAddress() {
      return this.defaultBillingAddress;
   }

   public void setDefaultBillingAddress(Address defaultBillingAddress) {
      this.defaultBillingAddress = defaultBillingAddress;
   }

   public Address getDefaultDeliveryAddress() {
      return this.defaultDeliveryAddress;
   }

   public void setDefaultDeliveryAddress(Address defaultDeliveryAddress) {
      this.defaultDeliveryAddress = defaultDeliveryAddress;
   }

   public Calendar getOpeningBalanceDueDate() {
      return this.openingBalanceDueDate;
   }

   public void setOpeningBalanceDueDate(Calendar openingBalanceDueDate) {
      this.openingBalanceDueDate = openingBalanceDueDate;
   }

   public List getSales() {
      return this.sales;
   }

   public void setSales(List sales) {
      this.sales = sales;
   }

   public BigDecimal getPaymentTotal() {
      return this.paymentTotal;
   }

   public void setPaymentTotal(BigDecimal paymentTotal) {
      this.paymentTotal = paymentTotal;
   }

   public BigDecimal getSaleTotal() {
      return this.saleTotal;
   }

   public void setSaleTotal(BigDecimal saleTotal) {
      this.saleTotal = saleTotal;
   }

   public BigDecimal getOverdueBalance() {
      return this.overdueBalance;
   }

   public void setOverdueBalance(BigDecimal overdueBalance) {
      this.overdueBalance = overdueBalance;
   }

   public BigDecimal getBalance() {
      return this.balance;
   }

   public void setBalance(BigDecimal balance) {
      this.balance = balance;
   }
}
