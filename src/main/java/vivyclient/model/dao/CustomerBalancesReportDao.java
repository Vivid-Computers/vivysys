package vivyclient.model.dao;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import vivyclient.data.TransactionContainer;
import vivyclient.error.SQLLog;
import vivyclient.model.Customer;
import vivyclient.model.CustomerType;
import vivyclient.model.DurationType;
import vivyclient.model.Sale;
import vivyclient.model.SaleDetail;
import vivyclient.model.SaleStatus;
import vivyclient.model.dao.BaseDao;
import vivyclient.shared.Constants;

public class CustomerBalancesReportDao extends BaseDao {
   public static List getCustomersSaleList(TransactionContainer transaction) throws Exception {
      boolean transactionOwner = false;
      Statement stmt = null;
      ResultSet rs = null;

      ArrayList customerId1;
      try {
         if(transaction == null) {
            transaction = new TransactionContainer(true);
            transactionOwner = true;
         }

         String findString = "SELECT c.id AS customerId, c.firstName, c.surname, c.organisationName, c.openingBalance, c.openingBalanceDueDate, c.customerTypeId, c.creditLimit, ss.saleId, ss.saleDate, ss.paymentTermsDurationId, ss.paymentTermsMultiplier, ss.freight, ss.hasGST, ss.netSaleLineValue FROM Customer c LEFT OUTER JOIN ( SELECT s.id AS saleId, s.saleDate, s.customerId, s.paymentTermsDurationId, s.paymentTermsMultiplier, s.freight, s.hasGST, (SELECT SUM(sd.quantity * sd.unitPrice) FROM SaleDetail sd WHERE sd.saleId = s.id) AS netSaleLineValue FROM Sale s WHERE NOT s.saleStatusId = " + SaleStatus.CANCELLED_STATUS.getObjectId() + ") ss " + "ON ss.customerId = c.id " + "ORDER BY c.id";
         SQLLog.writeQuery(findString, transaction);
         stmt = transaction.getConnection().createStatement();
         rs = stmt.executeQuery(findString);
         ArrayList result = new ArrayList();
         Customer customer = null;

         while(rs.next()) {
            int customerId = rs.getInt("customerId");
            if(customer == null || customer.getObjectId() != customerId) {
               customer = new Customer(customerId);
               customer.setFirstName(rs.getString("firstName"));
               customer.setSurname(rs.getString("surname"));
               customer.setOrganisationName(rs.getString("organisationName"));
               customer.setCustomerType(new CustomerType(rs.getInt("customerTypeId")));
               customer.setOpeningBalance(rs.getBigDecimal("openingBalance"));
               Date saleId = rs.getDate("openingBalanceDueDate");
               if(saleId != null) {
                  Calendar sale = Calendar.getInstance();
                  sale.setTime(saleId);
                  customer.setOpeningBalanceDueDate(sale);
               }

               customer.setCreditLimit(rs.getBigDecimal("creditLimit"));
               customer.setSales(new ArrayList());
               result.add(customer);
            }

            int saleId1 = rs.getInt("saleId");
            if(!rs.wasNull()) {
               Sale sale1 = new Sale(saleId1);
               Calendar saleDate = Calendar.getInstance();
               saleDate.setTime(rs.getDate("saleDate"));
               sale1.setSaleDate(saleDate);
               sale1.setFreightCost(rs.getBigDecimal("freight"));
               sale1.setHasGST(rs.getInt("hasGST") == 1);
               int durationId = rs.getInt("paymentTermsDurationId");
               if(!rs.wasNull()) {
                  sale1.setPaymentDurationType(new DurationType(durationId));
               }

               sale1.setPaymentTermsMultiplier(rs.getInt("paymentTermsMultiplier"));
               sale1.setCustomer(customer);
               sale1.setSaleLines(new ArrayList());
               SaleDetail saleDetail = new SaleDetail();
               saleDetail.setSale(sale1);
               saleDetail.setQuantity(Constants.ONE_BIG_DECIMAL);
               saleDetail.setUnitPrice(rs.getBigDecimal("netSaleLineValue"));
               if(saleDetail.getUnitPrice() == null) {
                  saleDetail.setUnitPrice(Constants.ZERO_BIG_DECIMAL);
               }

               sale1.getSaleLines().add(saleDetail);
               customer.getSales().add(sale1);
            }
         }

         close(stmt);
         customerId1 = result;
      } finally {
         close(stmt);
         if(transactionOwner) {
            transaction.close();
         }

      }

      return customerId1;
   }
}
