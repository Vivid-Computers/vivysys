package vivyclient.model.dao;

import java.lang.reflect.Method;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import vivyclient.data.TransactionContainer;
import vivyclient.error.ErrorWriter;
import vivyclient.error.SQLLog;
import vivyclient.exception.AppRuntimeException;
import vivyclient.exception.ModelNotFoundException;
import vivyclient.exception.StaleModelException;
import vivyclient.model.BaseModel;
import vivyclient.model.dao.BaseDao;
import vivyclient.model.searchMap.BaseSearchMap;
import vivyclient.model.tablemap.BaseTableMap;
import vivyclient.model.tablemap.TableMapAttribute;
import vivyclient.util.ReflectionHelper;
import vivyclient.util.Settings;

public class TableMapDaoHelper {
   private static final String NULL_SQL_STRING = "NULL";
   private static final String IS_NULL_WHERE_CLAUSE = "IS NULL";
   // $FF: synthetic field
   static Class class$java$lang$String;
   // $FF: synthetic field
   static Class class$java$math$BigDecimal;
   // $FF: synthetic field
   static Class class$java$util$Calendar;

   public static void populate(BaseModel model, TransactionContainer transaction) throws Exception {
      boolean transactionOwner = false;
      ResultSet rs = null;
      Statement stmt = null;

      try {
         if(transaction == null) {
            transaction = new TransactionContainer();
            transactionOwner = true;
         }

         stmt = transaction.getConnection().createStatement();
         String e = "SELECT " + getColumnList(model.getTableMap()) + " FROM " + model.getTableMap().getTableName() + " WHERE id = " + model.getObjectId();
         SQLLog.writeQuery(e, transaction);
         rs = stmt.executeQuery(e);
         if(!rs.next()) {
            throw new ModelNotFoundException(model);
         } else {
            List attributes = model.getTableMap().getMapAttributes();

            for(int i = 0; i < attributes.size(); ++i) {
               setValue(model, (TableMapAttribute)attributes.get(i), rs);
            }

            BaseDao.close(stmt);
            if(transactionOwner) {
               transaction.commit();
            }

         }
      } catch (Exception var8) {
         BaseDao.close(stmt);
         if(transactionOwner) {
            transaction.rollback();
         }

         throw var8;
      }
   }

   public static List findAll(BaseModel type, TransactionContainer transaction) throws Exception {
      ArrayList result = new ArrayList();
      boolean transactionOwner = false;
      ResultSet rs = null;
      Statement stmt = null;

      try {
         if(transaction == null) {
            transaction = new TransactionContainer();
            transactionOwner = true;
         }

         stmt = transaction.getConnection().createStatement();
         String e = "SELECT id, " + getColumnList(type.getTableMap()) + " FROM " + type.getTableMap().getTableName();
         if(type.getTableMap().getDefaultOrderingAttribute() != null) {
            e = e + " ORDER BY " + type.getTableMap().getDefaultOrderingAttribute().getColumnName();
         }

         SQLLog.writeQuery(e, transaction);
         rs = stmt.executeQuery(e);
         List attributes = type.getTableMap().getMapAttributes();

         while(rs.next()) {
            BaseModel model = (BaseModel)type.getClass().newInstance();
            model.setObjectId(rs.getInt("id"));

            for(int i = 0; i < attributes.size(); ++i) {
               setValue(model, (TableMapAttribute)attributes.get(i), rs);
            }

            result.add(model);
         }

         BaseDao.close(stmt);
         if(transactionOwner) {
            transaction.commit();
         }

         return result;
      } catch (Exception var10) {
         BaseDao.close(stmt);
         if(transactionOwner) {
            transaction.rollback();
         }

         throw var10;
      }
   }

   public static List findAll(BaseModel criteria, BaseSearchMap criteriaMap, TransactionContainer transaction) throws Exception {
      ArrayList result = new ArrayList();
      boolean transactionOwner = false;
      ResultSet rs = null;
      Statement stmt = null;

      try {
         if(transaction == null) {
            transaction = new TransactionContainer();
            transactionOwner = true;
         }

         stmt = transaction.getConnection().createStatement();
         String e = "SELECT id, " + getColumnList(criteria.getTableMap()) + " FROM " + criteria.getTableMap().getTableName();
         int attributes;
         if(criteriaMap != null && criteriaMap.getWhereCriteria().size() > 0) {
            e = e + " WHERE ";

            for(attributes = 0; attributes < criteriaMap.getWhereCriteria().size(); ++attributes) {
               if(attributes > 0) {
                  e = e + " AND ";
               }

               e = e + getWhereClause(criteria, (TableMapAttribute)criteriaMap.getWhereCriteria().get(attributes), (String)criteriaMap.getWhereConditions().get(attributes));
            }
         }

         if(criteriaMap != null && criteriaMap.getOrderCriteria().size() > 0) {
            e = e + " ORDER BY ";

            for(attributes = 0; attributes < criteriaMap.getOrderCriteria().size(); ++attributes) {
               if(attributes > 0) {
                  e = e + ", ";
               }

               e = e + ((TableMapAttribute)criteriaMap.getOrderCriteria().get(attributes)).getColumnName();
            }
         } else if(criteria.getTableMap().getDefaultOrderingAttribute() != null) {
            e = e + " ORDER BY " + criteria.getTableMap().getDefaultOrderingAttribute().getColumnName();
         }

         SQLLog.writeQuery(e, transaction);
         stmt.setFetchSize(1000);
         rs = stmt.executeQuery(e);
         List var18 = criteria.getTableMap().getMapAttributes();

         while(rs.next()) {
            BaseModel givens = (BaseModel)criteria.getClass().newInstance();
            givens.setObjectId(rs.getInt("id"));

            for(int conditions = 0; conditions < var18.size(); ++conditions) {
               setValue(givens, (TableMapAttribute)var18.get(conditions), rs);
            }

            result.add(givens);
         }

         if(criteriaMap != null) {
            List var19 = criteriaMap.getWhereCriteria();
            List var20 = criteriaMap.getWhereConditions();

            for(int i = 0; i < var19.size(); ++i) {
               TableMapAttribute attribute = (TableMapAttribute)var19.get(i);
               if(var20.get(i).equals("equals")) {
                  Method getter = ReflectionHelper.getGetter(criteria.getClass(), attribute.getPropertyName());
                  Method setter = ReflectionHelper.getSetter(criteria.getClass(), attribute.getPropertyName(), getter.getReturnType());
                  Object[] givenValue = new Object[]{getter.invoke(criteria, (Object[])null)};

                  for(int j = 0; j < result.size(); ++j) {
                     setter.invoke(result.get(j), givenValue);
                  }
               }
            }
         }

         BaseDao.close(stmt);
         if(transactionOwner) {
            transaction.commit();
         }

         return result;
      } catch (Exception var17) {
         BaseDao.close(stmt);
         if(transactionOwner) {
            transaction.rollback();
         }

         throw var17;
      }
   }

   public static int count(BaseModel criteria, BaseSearchMap criteriaMap, TransactionContainer transaction) throws Exception {
      boolean result = false;
      boolean transactionOwner = false;
      ResultSet rs = null;
      Statement stmt = null;

      try {
         if(transaction == null) {
            transaction = new TransactionContainer();
            transactionOwner = true;
         }

         stmt = transaction.getConnection().createStatement();
         String e = "SELECT COUNT(*) c FROM " + criteria.getTableMap().getTableName();
         if(criteriaMap != null && criteriaMap.getWhereCriteria().size() > 0) {
            e = e + " WHERE ";

            for(int attributes = 0; attributes < criteriaMap.getWhereCriteria().size(); ++attributes) {
               if(attributes > 0) {
                  e = e + " AND ";
               }

               e = e + getWhereClause(criteria, (TableMapAttribute)criteriaMap.getWhereCriteria().get(attributes), (String)criteriaMap.getWhereConditions().get(attributes));
            }
         }

         SQLLog.writeQuery(e, transaction);
         rs = stmt.executeQuery(e);
         List var11 = criteria.getTableMap().getMapAttributes();
         if(rs.next()) {
            int var10 = rs.getInt("c");
            BaseDao.close(stmt);
            if(transactionOwner) {
               transaction.commit();
            }

            return var10;
         } else {
            throw new AppRuntimeException("DB Error - could not count records");
         }
      } catch (Exception var9) {
         BaseDao.close(stmt);
         if(transactionOwner) {
            transaction.rollback();
         }

         throw var9;
      }
   }

   private static void setValue(BaseModel model, TableMapAttribute attribute, ResultSet rs) throws Exception {
      Class type = ReflectionHelper.getPropertyType(model.getClass(), attribute.getPropertyName());
      Object value = null;
      if(type.getName().equals((class$java$lang$String == null?(class$java$lang$String = class$("java.lang.String")):class$java$lang$String).getName())) {
         value = rs.getString(attribute.getColumnName());
      } else if(type.getName().equals(Integer.TYPE.getName())) {
         value = new Integer(rs.getInt(attribute.getColumnName()));
      } else if(type.getName().equals(Long.TYPE.getName())) {
         value = new Long(rs.getLong(attribute.getColumnName()));
      } else if(type.getName().equals(Double.TYPE.getName())) {
         value = new Double(rs.getDouble(attribute.getColumnName()));
      } else if(type.getName().equals(Boolean.TYPE.getName())) {
         value = new Boolean(rs.getInt(attribute.getColumnName()) == 1);
      } else if(type.getName().equals((class$java$math$BigDecimal == null?(class$java$math$BigDecimal = class$("java.math.BigDecimal")):class$java$math$BigDecimal).getName())) {
         value = rs.getBigDecimal(attribute.getColumnName());
      } else if(type.getName().equals((class$java$util$Calendar == null?(class$java$util$Calendar = class$("java.util.Calendar")):class$java$util$Calendar).getName())) {
         Date setter = rs.getDate(attribute.getColumnName());
         if(setter != null) {
            value = Calendar.getInstance();
            ((Calendar)value).setTime(setter);
         } else {
            value = null;
         }
      } else {
         if(!(type.newInstance() instanceof BaseModel)) {
            throw new AppRuntimeException("The population operation for this object has failed");
         }

         int setter1 = rs.getInt(attribute.getColumnName());
         if(!rs.wasNull()) {
            value = type.newInstance();
            ((BaseModel)value).setObjectId(setter1);
         } else {
            value = null;
         }
      }

      Method setter2 = ReflectionHelper.getSetter(model.getClass(), attribute.getPropertyName(), type);
      Object[] params = new Object[]{value};
      setter2.invoke(model, params);
   }

   public static void save(BaseModel model, TransactionContainer transaction) throws Exception {
      boolean transactionOwner = false;
      Statement stmt = null;

      try {
         if(transaction == null) {
            transaction = new TransactionContainer();
            transactionOwner = true;
         }

         stmt = transaction.getConnection().createStatement();
         String e = null;
         if(!model.exists(transaction)) {
            if(model.getObjectId() == Settings.getNullInt()) {
               model.setObjectId(model.getModelDao().getNextObjectId(model, transaction));
            }

            model.setUpdateId(0);
            e = getInsertStatement(model);
         } else {
            int updateId = BaseDao.getUpdateId(model, transaction);
            if(updateId != model.getUpdateId()) {
               throw new StaleModelException(model.getTableMap().getTableName(), "\"" + model.toString() + "\" has been updated by another user");
            }

            model.setUpdateId(model.getUpdateId() + 1);
            transaction.addSavedModel(model);
            e = getUpdateStatement(model);
         }

         SQLLog.writeUpdate(e, transaction);
         stmt.execute(e);
         BaseDao.close(stmt);
         if(transactionOwner) {
            transaction.commit();
         }

         model.setLastUpdate(Calendar.getInstance());
      } catch (Exception var6) {
         BaseDao.close(stmt);
         if(transactionOwner) {
            transaction.rollback();
         }

         throw var6;
      }
   }

   private static String getInsertStatement(BaseModel model) throws Exception {
      String saveString = "INSERT INTO " + model.getTableMap().getTableName() + "(id, " + getColumnList(model.getTableMap()) + ") " + "VALUES (" + model.getObjectId();
      List attributes = model.getTableMap().getMapAttributes();

      for(int i = 0; i < attributes.size(); ++i) {
         saveString = saveString + ", " + getSaveSQLValue(model, (TableMapAttribute)attributes.get(i));
      }

      saveString = saveString + ")";
      return saveString;
   }

   private static String getUpdateStatement(BaseModel model) throws Exception {
      List attributes = model.getTableMap().getMapAttributes();
      String saveString = "UPDATE " + model.getTableMap().getTableName() + " SET ";

      for(int i = 0; i < attributes.size(); ++i) {
         TableMapAttribute attribute = (TableMapAttribute)attributes.get(i);
         if(i > 0) {
            saveString = saveString + ", ";
         }

         saveString = saveString + attribute.getColumnName() + "=" + getSaveSQLValue(model, attribute);
      }

      saveString = saveString + " WHERE id = " + model.getObjectId();
      return saveString;
   }

   private static String getWhereClause(BaseModel model, TableMapAttribute attribute, String whereCondition) throws Exception {
      String toMatch = getSQLValue(model, attribute);
      if(toMatch.equals("NULL")) {
         return attribute.getColumnName() + " " + "IS NULL";
      } else {
         String operator;
         if(whereCondition.equals("equals")) {
            operator = "=";
         } else if(whereCondition.equals("greaterThan")) {
            operator = ">";
         } else if(whereCondition.equals("greaterThanOrEqual")) {
            operator = ">=";
         } else if(whereCondition.equals("lessThan")) {
            operator = "<";
         } else {
            if(!whereCondition.equals("lessThanOrEqual")) {
               if(whereCondition.equals("notEquals")) {
                  return "NOT " + attribute.getColumnName() + "=" + toMatch;
               }

               throw new AppRuntimeException();
            }

            operator = "<=";
         }

         return attribute.getColumnName() + operator + toMatch;
      }
   }

   private static String getSaveSQLValue(BaseModel model, TableMapAttribute attribute) throws Exception {
      return attribute.getSaveDatabaseDefault()?"DEFAULT":getSQLValue(model, attribute);
   }

   private static String getSQLValue(BaseModel model, TableMapAttribute attribute) throws Exception {
      try {
         Method e = ReflectionHelper.getGetter(model.getClass(), attribute.getPropertyName());
         Object value = e.invoke(model, (Object[])null);
         return BaseDao.getSQLValue(value);
      } catch (Exception var4) {
         ErrorWriter.writeException(var4);
         throw new Exception("Exception occured while setting value of " + model.getTableMap().getTableName() + "." + attribute.getPropertyName() + ": " + var4.getMessage());
      }
   }

   private static String getColumnList(BaseTableMap map) {
      String columns = "";
      List mappedColumns = map.getMapAttributes();

      for(int i = 0; i < mappedColumns.size(); ++i) {
         if(columns.length() > 0) {
            columns = columns + ", ";
         }

         columns = columns + ((TableMapAttribute)mappedColumns.get(i)).getColumnName();
      }

      return columns;
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
