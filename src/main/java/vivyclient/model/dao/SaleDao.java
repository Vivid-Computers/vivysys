package vivyclient.model.dao;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import vivyclient.data.TransactionContainer;
import vivyclient.error.SQLLog;
import vivyclient.model.Product;
import vivyclient.model.Sale;
import vivyclient.model.SaleDetail;
import vivyclient.model.dao.BaseDao;

public class SaleDao extends BaseDao {
   public static List getSaleDetailsForDispatch(Sale sale, TransactionContainer transaction) throws Exception {
      boolean transactionOwner = false;
      Statement stmt = null;
      ResultSet rs = null;

      try {
         if(transaction == null) {
            transaction = new TransactionContainer();
            transactionOwner = true;
         }

         String e = "SELECT sd.id saleDetailId, sd.quantity, sd.unitPrice, (select sum(dd.quantity) from DispatchDetail dd where dd.saleDetailId = sd.id) dispatchedQuantity, p.id productId, p.name from SaleDetail sd, Product p where sd.productId = p.id AND sd.saleId = " + sale.getObjectId();
         SQLLog.writeQuery(e, transaction);
         stmt = transaction.getConnection().createStatement();
         rs = stmt.executeQuery(e);
         ArrayList result = new ArrayList();

         while(rs.next()) {
            SaleDetail saleDetail = new SaleDetail(rs.getInt("saleDetailId"));
            saleDetail.setSale(sale);
            saleDetail.setQuantity(rs.getBigDecimal("quantity"));
            saleDetail.setUnitPrice(rs.getBigDecimal("unitPrice"));
            saleDetail.setDispatchedQuantity(rs.getBigDecimal("dispatchedQuantity"));
            Product product = new Product(rs.getInt("productId"));
            product.setName(rs.getString("name"));
            saleDetail.setProduct(product);
            result.add(saleDetail);
         }

         close(stmt);
         if(transactionOwner) {
            transaction.commit();
         }

         return result;
      } catch (Exception var9) {
         close(stmt);
         if(transactionOwner) {
            transaction.rollback();
         }

         throw var9;
      }
   }
}
