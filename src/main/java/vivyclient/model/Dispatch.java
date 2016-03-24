package vivyclient.model;

import java.util.Calendar;
import java.util.List;
import vivyclient.data.TransactionContainer;
import vivyclient.model.BaseModel;
import vivyclient.model.DispatchDetail;
import vivyclient.model.Sale;
import vivyclient.model.Supplier;
import vivyclient.model.searchMap.DispatchSearchMapFactory;
import vivyclient.model.tablemap.BaseTableMap;
import vivyclient.model.tablemap.DispatchTableMap;

public class Dispatch extends BaseModel {
   private Calendar dispatchDate;
   private Supplier supplier;
   private String trackingId;
   private Sale sale;
   private String attention;
   private List dispatchContent;

   public BaseTableMap getTableMap() {
      return new DispatchTableMap();
   }

   public Calendar getDispatchDate() {
      return this.dispatchDate;
   }

   public void setDispatchDate(Calendar dispatchDate) {
      this.dispatchDate = dispatchDate;
   }

   public Supplier getSupplier() {
      return this.supplier;
   }

   public void setSupplier(Supplier supplier) {
      this.supplier = supplier;
   }

   public String getTrackingId() {
      return this.trackingId;
   }

   public void setTrackingId(String trackingId) {
      this.trackingId = trackingId;
   }

   public Sale getSale() {
      return this.sale;
   }

   public void setSale(Sale sale) {
      this.sale = sale;
   }

   public void save(TransactionContainer transaction) throws Exception {
      boolean transactionOwner = false;

      try {
         if(transaction == null) {
            transaction = new TransactionContainer();
            transactionOwner = true;
         }

         super.save(transaction);
         saveAll(this.dispatchContent, transaction);
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

   public int getDispatchContentCount() throws Exception {
      this.lazyDispatchContentInitialise();
      return this.dispatchContent.size();
   }

   public DispatchDetail getDispatchContent(int index) throws Exception {
      this.lazyDispatchContentInitialise();
      return (DispatchDetail)this.dispatchContent.get(index);
   }

   public void addDispatchContent(DispatchDetail dispatchDetail) throws Exception {
      this.lazyDispatchContentInitialise();
      dispatchDetail.setDispatch(this);
      this.dispatchContent.add(dispatchDetail);
   }

   public void removeDispatchContent(DispatchDetail dispatchDetail) throws Exception {
      this.lazyDispatchContentInitialise();
      this.dispatchContent.remove(dispatchDetail);
      this.addChildForDeletion(dispatchDetail);
   }

   public void setDispatchContent(List newDispatchContent) throws Exception {
      if(this.dispatchContent != null) {
         for(int i = 0; i < this.dispatchContent.size(); ++i) {
            DispatchDetail detail = (DispatchDetail)this.dispatchContent.get(i);
            if(!newDispatchContent.contains(detail)) {
               this.addChildForDeletion(detail);
            }
         }
      }

      this.dispatchContent = newDispatchContent;
   }

   private void lazyDispatchContentInitialise() throws Exception {
      if(this.dispatchContent == null) {
         TransactionContainer transaction = new TransactionContainer(true);

         try {
            DispatchDetail criteria = new DispatchDetail();
            criteria.setDispatch(this);
            this.dispatchContent = DispatchDetail.findAll(criteria, DispatchSearchMapFactory.getDispatchDetailSearchMap(), transaction);
         } finally {
            transaction.close();
         }
      }

   }

   public String getAttention() {
      return this.attention;
   }

   public void setAttention(String attention) {
      this.attention = attention;
   }
}
