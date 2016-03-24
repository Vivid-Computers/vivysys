package vivyclient.model.dao;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import vivyclient.data.TransactionContainer;
import vivyclient.error.SQLLog;
import vivyclient.model.Customer;
import vivyclient.model.DurationType;
import vivyclient.model.Product;
import vivyclient.model.Sale;
import vivyclient.model.SaleDetail;
import vivyclient.model.SaleStatus;
import vivyclient.model.dao.BaseDao;

public class CustomerStatementDao extends BaseDao {
   public static List getSaleList(Customer customer, Calendar date, boolean beforeDate, TransactionContainer transaction) throws Exception {
      boolean transactionOwner = false;
      Statement stmt = null;
      ResultSet rs = null;

      ArrayList saleId1;
      try {
         if(transaction == null) {
            transaction = new TransactionContainer(true);
            transactionOwner = true;
         }

         String findString = "SELECT s.id AS saleId, s.saleDate, s.hasGST, s.freight, s.custRef, s.paymentTermsDurationId, s.paymentTermsMultiplier, sd.id AS saleDetailId, sd.quantity, sd.unitPrice, p.id AS productId, p.name FROM Sale s, SaleDetail sd, Product p WHERE s.id = sd.saleId AND s.saleDate " + (beforeDate?"< ":">= ") + BaseDao.getSQLValue(date) + " AND sd.productId = p.id" + " AND s.customerId = " + customer.getObjectId() + " AND NOT s.saleStatusId = " + SaleStatus.CANCELLED_STATUS.getObjectId() + " ORDER BY s.saleDate, s.id";
         SQLLog.writeQuery(findString, transaction);
         stmt = transaction.getConnection().createStatement();
         rs = stmt.executeQuery(findString);
         ArrayList result = new ArrayList();
         Sale sale = null;

         while(rs.next()) {
            int saleId = rs.getInt("saleId");
            if(sale == null || sale.getObjectId() != saleId) {
               sale = new Sale(saleId);
               Calendar saleDetail = Calendar.getInstance();
               saleDetail.setTime(rs.getDate("saleDate"));
               sale.setSaleDate(saleDetail);
               sale.setHasGST(rs.getInt("hasGST") > 0);
               sale.setFreightCost(rs.getBigDecimal("freight"));
               sale.setCustref(rs.getString("custRef"));
               int product = rs.getInt("paymentTermsDurationId");
               if(!rs.wasNull()) {
                  sale.setPaymentDurationType(new DurationType(product));
               }

               sale.setPaymentTermsMultiplier(rs.getInt("paymentTermsMultiplier"));
               sale.setCustomer(customer);
               sale.setSaleLines(new ArrayList());
               result.add(sale);
            }

            SaleDetail saleDetail1 = new SaleDetail(rs.getInt("saleDetailId"));
            saleDetail1.setSale(sale);
            saleDetail1.setQuantity(rs.getBigDecimal("quantity"));
            saleDetail1.setUnitPrice(rs.getBigDecimal("unitPrice"));
            Product product1 = new Product(rs.getInt("productId"));
            product1.setName(rs.getString("name"));
            saleDetail1.setProduct(product1);
            sale.getSaleLines().add(saleDetail1);
         }

         close(stmt);
         saleId1 = result;
      } finally {
         close(stmt);
         if(transactionOwner) {
            transaction.close();
         }

      }

      return saleId1;
   }
}
