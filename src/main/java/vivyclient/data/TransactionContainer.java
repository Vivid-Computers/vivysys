package vivyclient.data;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import vivyclient.data.ConnectionBroker;
import vivyclient.error.SQLLog;
import vivyclient.exception.AppRuntimeException;
import vivyclient.model.BaseModel;

public class TransactionContainer {
   private static int transactionNumberSequence = 0;
   private Connection connection;
   private int transactionNumber;
   private boolean readOnly;
   private List savedModels;

   public TransactionContainer() throws Exception {
      this(false);
   }

   public TransactionContainer(boolean readOnly) throws Exception {
      this.readOnly = readOnly;
      this.transactionNumber = transactionNumberSequence++;
      this.connection = ConnectionBroker.getConnection();
      this.connection.setAutoCommit(false);
      this.connection.setTransactionIsolation(2);
      if(readOnly) {
         this.connection.setReadOnly(true);
      } else {
         SQLLog.writeUpdate("--BEGIN--------", this);
         this.savedModels = new ArrayList();
      }

   }

   public Connection getConnection() throws Exception {
      return this.connection;
   }

   public void rollback() throws Exception {
      SQLLog.writeUpdate("--ROLLBACK--------", this);
      if(this.readOnly) {
         throw new AppRuntimeException();
      } else {
         this.connection.rollback();
         this.connection.close();

         for(int i = 0; i < this.savedModels.size(); ++i) {
            BaseModel model = (BaseModel)this.savedModels.get(i);
            model.setUpdateId(model.getUpdateId() - 1);
         }

      }
   }

   public void addSavedModel(BaseModel model) {
      this.savedModels.add(model);
   }

   public void commit() throws Exception {
      SQLLog.writeUpdate("--COMMIT-----------", this);
      if(this.readOnly) {
         throw new AppRuntimeException();
      } else {
         this.connection.commit();
         this.connection.close();
      }
   }

   public void close() throws Exception {
      if(this.readOnly) {
         this.connection.close();
      } else {
         throw new AppRuntimeException();
      }
   }

   public int getTransactionNumber() {
      return this.transactionNumber;
   }

   public String getIdentifier() {
      return (this.readOnly?"R":"W") + this.transactionNumber;
   }
}
