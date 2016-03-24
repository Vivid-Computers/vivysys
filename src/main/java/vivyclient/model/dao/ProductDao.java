package vivyclient.model.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import vivyclient.data.ConnectionBroker;
import vivyclient.data.TransactionContainer;
import vivyclient.error.ErrorWriter;
import vivyclient.error.SQLLog;
import vivyclient.model.BaseModel;
import vivyclient.model.DurationType;
import vivyclient.model.Product;
import vivyclient.model.ProductMakeup;
import vivyclient.model.ProductType;
import vivyclient.model.dao.BaseDao;

public class ProductDao extends BaseDao {
   public static int USER_ENTERED_PRODUCT_ID_CEILING = 200000;

   public int getNextObjectId(BaseModel model, TransactionContainer transaction) throws Exception {
      return this.getNextObjectId(model, USER_ENTERED_PRODUCT_ID_CEILING, transaction);
   }

   public static int countAll(TransactionContainer transaction) throws Exception {
      boolean transactionOwner = false;
      Statement stmt = null;
      ResultSet rs = null;
      boolean count = true;

      try {
         if(transaction == null) {
            transaction = new TransactionContainer();
            transactionOwner = true;
         }

         stmt = transaction.getConnection().createStatement();
         String e = "SELECT COUNT(p.id) productCount FROM Product p, SubCategory sc, Category c, ProductType pt WHERE p.subcategoryId = sc.id AND sc.categoryId = c.id AND c.productTypeId = pt.id ";
         SQLLog.writeQuery(e, transaction);
         rs = stmt.executeQuery(e);
         if(rs.next()) {
            int count1 = rs.getInt("productCount");
            close(stmt);
            if(transactionOwner) {
               transaction.commit();
            }

            return count1;
         } else {
            throw new Exception("A Product count could not be found");
         }
      } catch (Exception var6) {
         close(stmt);
         if(transactionOwner) {
            transaction.rollback();
         }

         throw var6;
      }
   }

   public static Vector getDistinctAttributeList(String attributeName, ProductType productType) {
      Connection c = null;
      Statement stmt = null;
      ResultSet rs = null;
      Vector attributeList = new Vector();

      try {
         try {
            c = ConnectionBroker.getConnection();
            stmt = c.createStatement();
            String ex = "SELECT DISTINCT(" + attributeName + ") attribute FROM Product p, SubCategory sc, Category c";
            if(productType != null) {
               ex = ex + " WHERE p.subCategoryId = sc.id AND sc.categoryId = c.id AND c.productTypeId = " + productType.getObjectId();
            }

            ex = ex + " ORDER BY " + attributeName;
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

   public static List getProductMakeupList(Product product, TransactionContainer transaction) throws Exception {
      boolean transactionOwner = false;
      Statement stmt = null;
      ResultSet rs = null;

      try {
         if(transaction == null) {
            transaction = new TransactionContainer();
            transactionOwner = true;
         }

         String e = "SELECT pm.id productMakeupId, pm.quantity, pm.warrantyDurationTypeId, pm.warrantyDurationMultiplier, pm.warrantyComments, pm.updateId, p.id productId, p.name FROM ProductMakeup pm, Product p WHERE pm.partId = p.id AND pm.productId = " + product.getObjectId();
         SQLLog.writeQuery(e, transaction);
         stmt = transaction.getConnection().createStatement();
         rs = stmt.executeQuery(e);
         ArrayList result = new ArrayList();

         while(rs.next()) {
            ProductMakeup productMakeup = new ProductMakeup(rs.getInt("productMakeupId"));
            productMakeup.setQuantity(rs.getInt("quantity"));
            int durationTypeId = rs.getInt("warrantyDurationTypeId");
            if(rs.wasNull()) {
               productMakeup.setWarrantyDurationType((DurationType)null);
            } else {
               productMakeup.setWarrantyDurationType(new DurationType(durationTypeId));
            }

            productMakeup.setWarrantyDurationMultiplier(rs.getInt("warrantyDurationMultiplier"));
            productMakeup.setWarrantyComments(rs.getString("warrantyComments"));
            productMakeup.setUpdateId(rs.getInt("updateId"));
            Product part = new Product(rs.getInt("productId"));
            part.setName(rs.getString("name"));
            productMakeup.setPart(part);
            productMakeup.setProduct(product);
            result.add(productMakeup);
         }

         close(stmt);
         if(transactionOwner) {
            transaction.commit();
         }

         return result;
      } catch (Exception var10) {
         close(stmt);
         if(transactionOwner) {
            transaction.rollback();
         }

         throw var10;
      }
   }
}
