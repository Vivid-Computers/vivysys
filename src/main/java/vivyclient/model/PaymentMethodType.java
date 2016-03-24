package vivyclient.model;

import java.util.Iterator;
import java.util.List;
import vivyclient.model.BaseModel;
import vivyclient.model.tablemap.BaseTableMap;
import vivyclient.model.tablemap.PaymentMethodTypeTableMap;

public class PaymentMethodType extends BaseModel {
   public static final PaymentMethodType CREDIT_CARD_PAYMENT_METHOD_TYPE = new PaymentMethodType(1);
   public static final PaymentMethodType ON_ACCOUNT_PAYMENT_METHOD_TYPE = new PaymentMethodType(4);
   public static final PaymentMethodType CASH_PAYMENT_METHOD_TYPE = new PaymentMethodType(5);
   private String name;

   public PaymentMethodType() {
   }

   public PaymentMethodType(int objectId) {
      super(objectId);
   }

   public BaseTableMap getTableMap() {
      return new PaymentMethodTypeTableMap();
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String toString() {
      return this.name != null?this.name:"PaymentMethodType " + this.objectId;
   }

   public static PaymentMethodType findPopulatedPaymentMethodType(PaymentMethodType type) throws Exception {
      List paymentMethodTypes = cachedFindAll(type);
      Iterator i = paymentMethodTypes.iterator();

      PaymentMethodType p;
      do {
         if(!i.hasNext()) {
            return null;
         }

         p = (PaymentMethodType)i.next();
      } while(!p.equals(type));

      return p;
   }
}
