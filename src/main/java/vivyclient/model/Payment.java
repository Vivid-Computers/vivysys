package vivyclient.model;

import java.math.BigDecimal;
import java.util.Calendar;
import vivyclient.model.BaseModel;
import vivyclient.model.Customer;
import vivyclient.model.PaymentMethodType;
import vivyclient.model.tablemap.BaseTableMap;
import vivyclient.model.tablemap.PaymentTableMap;
import vivyclient.util.ViewUtil;

public class Payment extends BaseModel {
   private Customer customer;
   private BigDecimal amount;
   private PaymentMethodType method;
   private Calendar date;
   private String details1;
   private String details2;
   private String details3;

   public Payment() {
   }

   public Payment(int objectId) {
      this.objectId = objectId;
   }

   public BaseTableMap getTableMap() {
      return new PaymentTableMap();
   }

   public String toString() {
      return this.date != null && this.amount != null?ViewUtil.calendarDisplay(this.date) + " - $" + ViewUtil.currencyDisplay(this.amount):super.toString();
   }

   public Customer getCustomer() {
      return this.customer;
   }

   public void setCustomer(Customer customer) {
      this.customer = customer;
   }

   public BigDecimal getAmount() {
      return this.amount;
   }

   public void setAmount(BigDecimal amount) {
      this.amount = amount;
   }

   public PaymentMethodType getMethod() {
      return this.method;
   }

   public void setMethod(PaymentMethodType method) {
      this.method = method;
   }

   public Calendar getDate() {
      return this.date;
   }

   public void setDate(Calendar date) {
      this.date = date;
   }

   public String getDetails1() {
      return this.details1;
   }

   public void setDetails1(String details1) {
      this.details1 = details1;
   }

   public String getDetails2() {
      return this.details2;
   }

   public void setDetails2(String details2) {
      this.details2 = details2;
   }

   public String getDetails3() {
      return this.details3;
   }

   public void setDetails3(String details3) {
      this.details3 = details3;
   }
}
