package vivyclient.model;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import vivyclient.data.TransactionContainer;
import vivyclient.exception.AppRuntimeException;
import vivyclient.model.Address;
import vivyclient.model.BaseModel;
import vivyclient.model.Customer;
import vivyclient.model.DurationType;
import vivyclient.model.PaymentMethodType;
import vivyclient.model.SaleDetail;
import vivyclient.model.SaleStatus;
import vivyclient.model.Site;
import vivyclient.model.dao.SaleDao;
import vivyclient.model.searchMap.SaleDetailSearchMapFactory;
import vivyclient.model.tablemap.BaseTableMap;
import vivyclient.model.tablemap.SaleTableMap;
import vivyclient.shared.Constants;
import vivyclient.util.Settings;
import vivyclient.util.ViewUtil;

public class Sale extends BaseModel {
   private Customer customer;
   private Site site;
   private Calendar saleDate;
   private Address deliveryAddress;
   private Address billingAddress;
   private boolean hasGST;
   private BigDecimal freightCost;
   private PaymentMethodType paymentMethod;
   private String paymentDetails1;
   private String paymentDetails2;
   private String paymentDetails3;
   private String customerComments;
   private String saleComments;
   private String custref;
   private SaleStatus status;
   private DurationType paymentDurationType;
   private int paymentTermsMultiplier;
   private Calendar saleDispatchedDate;
   private List saleLines;

   public Sale() {
   }

   public Sale(int objectId) {
      this.objectId = objectId;
   }

   public BaseTableMap getTableMap() {
      return new SaleTableMap();
   }

   public List getSaleDetailsForDispatch(TransactionContainer transaction) throws Exception {
      return SaleDao.getSaleDetailsForDispatch(this, transaction);
   }

   public String toString() {
      return "Sale " + this.objectId + " (" + ViewUtil.calendarDisplay(this.getSaleDate()) + ")";
   }

   public void save(TransactionContainer transaction) throws Exception {
      boolean transactionOwner = false;

      try {
         if(transaction == null) {
            transaction = new TransactionContainer();
            transactionOwner = true;
         }

         super.save(transaction);
         saveAll(this.saleLines, transaction);
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

   public BigDecimal getSubTotal() throws Exception {
      BigDecimal cost = Constants.ZERO_BIG_DECIMAL;

      for(int i = 0; i < this.getSaleLineCount(); ++i) {
         cost = cost.add(this.getSaleLine(i).getLineTotal());
      }

      return cost.add(this.getFreightCostForAddition());
   }

   public BigDecimal getTotalCost() throws Exception {
      return this.getSubTotal().add(this.getGSTCost());
   }

   public BigDecimal getGSTCost() throws Exception {
      return this.hasGST?Settings.getGSTRate().multiply(this.getSubTotal()):Constants.ZERO_BIG_DECIMAL;
   }

   public BigDecimal getFreightCostForAddition() {
      return this.freightCost != null && !this.freightCost.equals(Constants.UNDEFINED_FREIGHT_COST)?this.freightCost:Constants.ZERO_BIG_DECIMAL;
   }

   public boolean paymentOverdue() {
      Calendar dueDate = this.getPaymentDueDate();
      if(dueDate == null) {
         return false;
      } else {
         Calendar now = Calendar.getInstance();
         now.set(11, 0);
         now.set(12, 0);
         now.set(13, 0);
         now.set(14, 0);
         return now.after(dueDate);
      }
   }

   public Calendar getPaymentDueDate() {
      if(this.paymentDurationType != null) {
         Calendar dueDate = Calendar.getInstance();
         dueDate.setTime(this.saleDate.getTime());
         dueDate.setLenient(true);
         if(this.paymentDurationType.equals(DurationType.DAYS_DURATION_TYPE)) {
            dueDate.add(5, this.paymentTermsMultiplier);
         } else if(this.paymentDurationType.equals(DurationType.MONTHS_DURATION_TYPE)) {
            dueDate.add(2, this.paymentTermsMultiplier);
         } else if(this.paymentDurationType.equals(DurationType.YEARS_DURATION_TYPE)) {
            dueDate.add(1, this.paymentTermsMultiplier);
         } else {
            if(!this.paymentDurationType.equals(DurationType.BILLING_MONTH_DURATION_TYPE)) {
               throw new AppRuntimeException(this.paymentDurationType.toString());
            }

            dueDate.add(2, this.paymentTermsMultiplier);
            dueDate.set(5, 20);
         }

         return dueDate;
      } else {
         return null;
      }
   }

   public int getSaleLineCount() throws Exception {
      this.lazySaleLineInitialise();
      return this.saleLines.size();
   }

   public SaleDetail getSaleLine(int index) throws Exception {
      this.lazySaleLineInitialise();
      return (SaleDetail)this.saleLines.get(index);
   }

   public void addSaleLine(SaleDetail saleLine) throws Exception {
      this.lazySaleLineInitialise();
      this.saleLines.add(saleLine);
      saleLine.setSale(this);
   }

   public void removeSaleLine(SaleDetail saleLine) throws Exception {
      this.lazySaleLineInitialise();
      this.saleLines.remove(saleLine);
      this.addChildForDeletion(saleLine);
   }

   public void removeSaleLineAt(int index) throws Exception {
      this.lazySaleLineInitialise();
      this.addChildForDeletion((BaseModel)this.saleLines.remove(index));
   }

   public void setSaleLines(List saleLines) {
      this.saleLines = saleLines;
   }

   public List getSaleLines() {
      return this.saleLines;
   }

   private void lazySaleLineInitialise() throws Exception {
      if(this.saleLines == null) {
         TransactionContainer transaction = new TransactionContainer(true);

         try {
            SaleDetail criteria = new SaleDetail();
            criteria.setSale(this);
            this.saleLines = SaleDetail.findAll(criteria, SaleDetailSearchMapFactory.getSaleSearchMap(), transaction);

            for(int i = 0; i < this.saleLines.size(); ++i) {
               ((SaleDetail)this.saleLines.get(i)).getProduct().populate(transaction);
            }
         } finally {
            transaction.close();
         }
      }

   }

   public void deepPopulate() throws Exception {
      TransactionContainer transaction = new TransactionContainer(true);

      try {
         this.customer.populate(transaction);
         this.deliveryAddress.populate(transaction);
         this.billingAddress.populate(transaction);
      } finally {
         transaction.close();
      }

   }

   public Customer getCustomer() {
      return this.customer;
   }

   public void setCustomer(Customer customer) {
      this.customer = customer;
   }

   public Site getSite() {
      return this.site;
   }

   public void setSite(Site site) {
      this.site = site;
   }

   public Calendar getSaleDate() {
      return this.saleDate;
   }

   public void setSaleDate(Calendar saleDate) {
      this.saleDate = saleDate;
   }

   public Address getDeliveryAddress() {
      return this.deliveryAddress;
   }

   public void setDeliveryAddress(Address deliveryAddress) {
      this.deliveryAddress = deliveryAddress;
   }

   public Address getBillingAddress() {
      return this.billingAddress;
   }

   public void setBillingAddress(Address billingAddress) {
      this.billingAddress = billingAddress;
   }

   public boolean getHasGST() {
      return this.hasGST;
   }

   public void setHasGST(boolean hasGST) {
      this.hasGST = hasGST;
   }

   public BigDecimal getFreightCost() {
      return this.freightCost;
   }

   public void setFreightCost(BigDecimal freightCost) {
      this.freightCost = freightCost;
   }

   public PaymentMethodType getPaymentMethod() {
      return this.paymentMethod;
   }

   public void setPaymentMethod(PaymentMethodType paymentMethod) {
      this.paymentMethod = paymentMethod;
   }

   public String getPaymentDetails1() {
      return this.paymentDetails1;
   }

   public void setPaymentDetails1(String paymentDetails1) {
      this.paymentDetails1 = paymentDetails1;
   }

   public String getPaymentDetails2() {
      return this.paymentDetails2;
   }

   public void setPaymentDetails2(String paymentDetails2) {
      this.paymentDetails2 = paymentDetails2;
   }

   public String getPaymentDetails3() {
      return this.paymentDetails3;
   }

   public void setPaymentDetails3(String paymentDetails3) {
      this.paymentDetails3 = paymentDetails3;
   }

   public String getCustomerComments() {
      return this.customerComments;
   }

   public void setCustomerComments(String customerComments) {
      this.customerComments = customerComments;
   }

   public String getSaleComments() {
      return this.saleComments;
   }

   public void setSaleComments(String saleComments) {
      this.saleComments = saleComments;
   }

   public String getCustref() {
      return this.custref;
   }

   public void setCustref(String custref) {
      this.custref = custref;
   }

   public SaleStatus getStatus() {
      return this.status;
   }

   public void setStatus(SaleStatus status) {
      this.status = status;
   }

   public DurationType getPaymentDurationType() {
      return this.paymentDurationType;
   }

   public void setPaymentDurationType(DurationType paymentDurationType) {
      this.paymentDurationType = paymentDurationType;
   }

   public int getPaymentTermsMultiplier() {
      return this.paymentTermsMultiplier;
   }

   public void setPaymentTermsMultiplier(int paymentTermsMultiplier) {
      this.paymentTermsMultiplier = paymentTermsMultiplier;
   }

   public Calendar getSaleDispatchedDate() {
      return this.saleDispatchedDate;
   }

   public void setSaleDispatchedDate(Calendar saleDispatchedDate) {
      this.saleDispatchedDate = saleDispatchedDate;
   }
}
