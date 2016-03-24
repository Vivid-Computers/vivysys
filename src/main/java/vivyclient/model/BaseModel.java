package vivyclient.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import vivyclient.data.TransactionContainer;
import vivyclient.model.RecordEntryType;
import vivyclient.model.dao.BaseDao;
import vivyclient.model.searchMap.BaseSearchMap;
import vivyclient.model.tablemap.BaseTableMap;

public abstract class BaseModel implements Serializable {
   protected int objectId = -1;
   protected int updateId = -1;
   protected Calendar lastUpdate;
   private RecordEntryType recordEntryType;
   protected List forDeletionModels;
   private static HashMap cachedSet = new HashMap();

   public BaseModel() {
      this.recordEntryType = RecordEntryType.VIVY_CLIENT_ENTRY_TYPE;
   }

   public BaseModel(int objectId) {
      this.recordEntryType = RecordEntryType.VIVY_CLIENT_ENTRY_TYPE;
      this.objectId = objectId;
   }

   public BaseDao getModelDao() {
      return new BaseDao();
   }

   public abstract BaseTableMap getTableMap();

   public void populate(TransactionContainer transaction) throws Exception {
      this.getModelDao();
      BaseDao.populate(this, transaction);
   }

   public void save(TransactionContainer transaction) throws Exception {
      boolean transactionOwner = false;

      try {
         if(transaction == null) {
            transaction = new TransactionContainer();
            transactionOwner = true;
         }

         clearCache(this);
         this.getModelDao();
         BaseDao.save(this, transaction);
         deleteAll(this.forDeletionModels, transaction);
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

   public void delete(TransactionContainer transaction) throws Exception {
      clearCache(this);
      deleteAll(this.forDeletionModels, transaction);
      this.getModelDao();
      BaseDao.delete(this, transaction);
   }

   protected static void saveAll(List models, TransactionContainer transaction) throws Exception {
      if(models != null) {
         boolean transactionOwner = false;

         try {
            if(transaction == null) {
               transaction = new TransactionContainer();
               transactionOwner = true;
            }

            for(int e = 0; e < models.size(); ++e) {
               ((BaseModel)models.get(e)).save(transaction);
            }

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

   protected static void deleteAll(List models, TransactionContainer transaction) throws Exception {
      if(models != null) {
         boolean transactionOwner = false;

         try {
            if(transaction == null) {
               transaction = new TransactionContainer();
               transactionOwner = true;
            }

            for(int e = 0; e < models.size(); ++e) {
               ((BaseModel)models.get(e)).delete(transaction);
            }

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

   public boolean exists(TransactionContainer transaction) throws Exception {
      this.getModelDao();
      return BaseDao.exists(this, transaction);
   }

   public static List findAll(BaseModel criteria, BaseSearchMap criteriaMap, TransactionContainer transaction) throws Exception {
      return BaseDao.findAll(criteria, criteriaMap, transaction);
   }

   public static int count(BaseModel criteria, BaseSearchMap criteriaMap, TransactionContainer transaction) throws Exception {
      return BaseDao.count(criteria, criteriaMap, transaction);
   }

   public static List cachedFindAll(BaseModel type) throws Exception {
      List set = (List)cachedSet.get(type.getTableMap().getTableName());
      if(set == null) {
         set = findAll(type, (BaseSearchMap)null, (TransactionContainer)null);
         cachedSet.put(type.getTableMap().getTableName(), set);
      }

      return set;
   }

   private static void clearCache(BaseModel type) {
      cachedSet.remove(type.getTableMap().getTableName());
   }

   public boolean equals(Object compareTo) {
      return compareTo == null?false:(!(compareTo instanceof BaseModel)?false:this.getObjectId() == ((BaseModel)compareTo).getObjectId() && this.getTableMap().getTableName().equals(((BaseModel)compareTo).getTableMap().getTableName()));
   }

   public void addChildForDeletion(BaseModel model) {
      if(this.forDeletionModels == null) {
         this.forDeletionModels = new ArrayList();
      }

      this.forDeletionModels.add(model);
   }

   public String toString() {
      return this.getTableMap().getTableName() + " " + this.objectId;
   }

   public void setObjectId(int objectId) {
      this.objectId = objectId;
   }

   public int getObjectId() {
      return this.objectId;
   }

   public void setUpdateId(int updateId) {
      this.updateId = updateId;
   }

   public int getUpdateId() {
      return this.updateId;
   }

   public void setLastUpdate(Calendar lastUpdate) {
      this.lastUpdate = lastUpdate;
   }

   public Calendar getLastUpdate() {
      return this.lastUpdate;
   }

   public RecordEntryType getRecordEntryType() {
      return this.recordEntryType;
   }

   public void setRecordEntryType(RecordEntryType recordEntryType) {
      this.recordEntryType = recordEntryType;
   }
}
