package vivyclient.model.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Statement;
import vivyclient.data.TransactionContainer;
import vivyclient.error.SQLLog;
import vivyclient.model.SaleStatus;
import vivyclient.model.dao.BaseDao;
import vivyclient.print.reports.GSTTotals;
import vivyclient.shared.Constants;

public class GSTTotalsReportDao extends BaseDao {
   public static void populateTotals(GSTTotals totals, TransactionContainer transaction) throws Exception {
      boolean transactionOwner = false;
      Statement stmt = null;
      ResultSet rs = null;

      try {
         if(transaction == null) {
            transaction = new TransactionContainer(true);
            transactionOwner = true;
         }

         String findString = "SELECT s.hasGST, SUM(sd.quantity * sd.unitPrice) AS saleLineTotal, SUM(s.freight) AS saleFreightTotal FROM Sale s, SaleDetail sd WHERE s.id = sd.saleId AND NOT s.saleStatusId = " + SaleStatus.CANCELLED_STATUS.getObjectId() + " " + "AND s.saleDate >= " + getSQLValue(totals.getPeriodStart()) + " " + "AND s.saleDate <= " + getSQLValue(totals.getPeriodEnd()) + " " + "GROUP BY s.hasGST";
         SQLLog.writeQuery(findString, transaction);
         stmt = transaction.getConnection().createStatement();
         rs = stmt.executeQuery(findString);
         totals.setGstSaleTotals(Constants.ZERO_BIG_DECIMAL);
         totals.setGstExemptSaleTotals(Constants.ZERO_BIG_DECIMAL);

         while(rs.next()) {
            boolean hasGST = rs.getInt("hasGST") == 1;
            BigDecimal total = rs.getBigDecimal("saleLineTotal").add(rs.getBigDecimal("saleFreightTotal"));
            if(hasGST) {
               totals.setGstSaleTotals(total);
            } else {
               totals.setGstExemptSaleTotals(total);
            }
         }

         close(stmt);
      } finally {
         close(stmt);
         if(transactionOwner) {
            transaction.close();
         }

      }
   }
}
