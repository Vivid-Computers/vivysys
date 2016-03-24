package vivyclient.model.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;
import vivyclient.data.ConnectionBroker;
import vivyclient.data.TransactionContainer;
import vivyclient.error.ErrorWriter;
import vivyclient.error.SQLLog;
import vivyclient.exception.AppRuntimeException;
import vivyclient.exception.StaleModelException;
import vivyclient.model.BaseModel;
import vivyclient.model.dao.TableMapDaoHelper;
import vivyclient.model.searchMap.BaseSearchMap;
import vivyclient.util.Settings;

public class BaseDao {
   public static final String DATABASE_SAVE_DATE_FORMAT = "yyyyMMdd HH:mm:ss";
   private static final String NULL_SQL_STRING = "NULL";

   public static int getUpdateId(BaseModel model, TransactionContainer transaction) throws Exception {
      boolean transactionOwner = false;
      Statement stmt = null;
      ResultSet rs = null;
      boolean updateId = true;

      try {
         if(transaction == null) {
            transaction = new TransactionContainer();
            transactionOwner = true;
         }

         stmt = transaction.getConnection().createStatement();
         String e = "SELECT updateId FROM " + model.getTableMap().getTableName() + " WITH (UPDLOCK) WHERE id=" + model.getObjectId();
         SQLLog.writeQuery(e, transaction);
         rs = stmt.executeQuery(e);
         if(rs.next()) {
            int updateId1 = rs.getInt("updateId");
            close(stmt);
            if(transactionOwner) {
               transaction.commit();
            }

            return updateId1;
         } else {
            throw new Exception("updateId could not be found from table \"" + model.getTableMap().getTableName() + "\" for record \"" + model.getObjectId() + "\"");
         }
      } catch (Exception var7) {
         close(stmt);
         if(transactionOwner) {
            transaction.rollback();
         }

         throw var7;
      }
   }

   public int getNextObjectId(BaseModel model, TransactionContainer transaction) throws Exception {
      return this.getNextObjectId(model, Settings.getNullInt(), transaction);
   }

   public int getNextObjectId(BaseModel model, int ceiling, TransactionContainer transaction) throws Exception {
      boolean transactionOwner = false;
      Statement stmt = null;
      ResultSet rs = null;
      boolean objectId = true;

      try {
         if(transaction == null) {
            transaction = new TransactionContainer();
            transactionOwner = true;
         }

         stmt = transaction.getConnection().createStatement();
         String e = "SELECT max(id) currentMax FROM " + model.getTableMap().getTableName() + " WITH (UPDLOCK)";
         if(ceiling != Settings.getNullInt()) {
            e = e + " WHERE id < " + ceiling;
         }

         SQLLog.writeQuery(e, transaction);
         rs = stmt.executeQuery(e);
         if(rs.next()) {
            int objectId1 = rs.getInt("currentMax") + 1;
            close(stmt);
            if(transactionOwner) {
               transaction.commit();
            }

            return objectId1;
         } else {
            Exception ex = new Exception("Valid objectId could not be found from table \"" + model.getTableMap().getTableName() + "\" for record \"" + model.getObjectId() + "\"");
            throw ex;
         }
      } catch (Exception var10) {
         close(stmt);
         if(transactionOwner) {
            transaction.rollback();
         }

         throw var10;
      }
   }

   public static Vector getDistinctAttributeList(String tableName, String attributeName) {
      Connection c = null;
      Statement stmt = null;
      ResultSet rs = null;
      Vector attributeList = new Vector();

      try {
         try {
            c = ConnectionBroker.getConnection();
            stmt = c.createStatement();
            String ex = "SELECT DISTINCT(" + attributeName + ") attribute FROM " + tableName + " ORDER BY " + attributeName;
            SQLLog.writeQuery(ex, (TransactionContainer)null);
            rs = stmt.executeQuery(ex);

            while(rs.next()) {
               attributeList.add(rs.getString("attribute"));
            }
         } catch (Exception var12) {
            throw var12;
         } finally {
            close(stmt);
            if(c != null) {
               c.close();
            }

         }
      } catch (Exception var14) {
         ErrorWriter.writeException(var14);
      }

      return attributeList;
   }

   public static boolean exists(BaseModel model, TransactionContainer transaction) throws Exception {
      boolean transactionOwner = false;
      Connection c = null;
      Statement stmt = null;
      ResultSet rs = null;

      try {
         if(transaction == null) {
            transaction = new TransactionContainer(true);
            transactionOwner = true;
         }

         c = transaction.getConnection();
         stmt = c.createStatement();
         String e = "SELECT COUNT(*) count FROM " + model.getTableMap().getTableName() + " WHERE id=" + model.getObjectId();
         SQLLog.writeQuery(e, transaction);
         rs = stmt.executeQuery(e);
         if(rs.next()) {
            boolean exists = rs.getInt("count") > 0;
            close(stmt);
            if(transactionOwner) {
               transaction.close();
            }

            return exists;
         } else {
            throw new Exception("Failed to check if the record existed.");
         }
      } catch (Exception var8) {
         close(stmt);
         if(transactionOwner) {
            transaction.rollback();
         }

         throw var8;
      }
   }

   public static void populate(BaseModel model, TransactionContainer transaction) throws Exception {
      TableMapDaoHelper.populate(model, transaction);
   }

   public static void save(BaseModel model, TransactionContainer transaction) throws Exception {
      TableMapDaoHelper.save(model, transaction);
   }

   public static List findAll(BaseModel type, TransactionContainer transaction) throws Exception {
      return TableMapDaoHelper.findAll(type, transaction);
   }

   public static List findAll(BaseModel criteria, BaseSearchMap criteriaMap, TransactionContainer transaction) throws Exception {
      return TableMapDaoHelper.findAll(criteria, criteriaMap, transaction);
   }

   public static int count(BaseModel criteria, BaseSearchMap criteriaMap, TransactionContainer transaction) throws Exception {
      return TableMapDaoHelper.count(criteria, criteriaMap, transaction);
   }

   public static void delete(BaseModel model, TransactionContainer transaction) throws Exception {
      boolean transactionOwner = false;
      Statement stmt = null;

      try {
         if(transaction == null) {
            transaction = new TransactionContainer();
            transactionOwner = true;
         }

         if(model.exists(transaction)) {
            int e = getUpdateId(model, transaction);
            if(e != model.getUpdateId()) {
               throw new StaleModelException(model.getTableMap().getTableName(), "\"" + model.toString() + "\" has been updated by another user and cannot be deleted.");
            }

            stmt = transaction.getConnection().createStatement();
            String deleteString = "DELETE FROM " + model.getTableMap().getTableName() + " WHERE id=" + model.getObjectId();
            SQLLog.writeUpdate(deleteString, transaction);
            stmt.execute(deleteString);
            close(stmt);
         }

         if(transactionOwner) {
            transaction.commit();
         }

      } catch (Exception var6) {
         close(stmt);
         if(transactionOwner) {
            transaction.rollback();
         }

         throw var6;
      }
   }

   public static String stringF(String input) {
      if(input == null) {
         return "null";
      } else {
         input = input.trim();
         if(!Settings.getAllowBlankStringsInDB() && input.length() == 0) {
            return "null";
         } else {
            input = "\'" + replace(input, "\'", "\'\'") + "\'";
            return input;
         }
      }
   }

   public static void close(Statement s) {
      try {
         if(s != null) {
            s.close();
         }
      } catch (Exception var2) {
         ErrorWriter.writeException(var2);
      }

   }

   private static String replace(String source, String from, String to) {
      int sz = from.length();
      int where = source.indexOf(from);
      return where < 0?source:source.substring(0, where) + to + replace(source.substring(where + sz), from, to);
   }

   public static String getSQLValue(Object value) {
      if(value == null) {
         return "NULL";
      } else if(value instanceof String) {
         return stringF((String)value);
      } else if(value instanceof Integer) {
         return ((Integer)value).toString();
      } else if(value instanceof Long) {
         return ((Long)value).toString();
      } else if(value instanceof Double) {
         return ((Double)value).toString();
      } else if(value instanceof Boolean) {
         return ((Boolean)value).booleanValue()?"1":"0";
      } else if(value instanceof BigDecimal) {
         return ((BigDecimal)value).toString();
      } else if(value instanceof Calendar) {
         return "\'" + (new SimpleDateFormat("yyyyMMdd HH:mm:ss")).format(((Calendar)value).getTime()) + "\'";
      } else if(value instanceof BaseModel) {
         return Integer.toString(((BaseModel)value).getObjectId());
      } else {
         throw new AppRuntimeException("Error converting the value of \'" + value.toString() + "\' to an SQL representation.");
      }
   }
}
