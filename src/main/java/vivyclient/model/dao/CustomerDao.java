package vivyclient.model.dao;

import java.util.List;
import vivyclient.data.TransactionContainer;
import vivyclient.model.Customer;
import vivyclient.model.dao.BaseDao;

public class CustomerDao extends BaseDao {
   public List findUnpopulatedCustomersWithChildCount(Customer parent) throws Exception {
      TransactionContainer transaction = new TransactionContainer(true);

      try {
         String findString = "SELECT id, (SELECT COUNT(*) FROM Customer WHERE parent = c.id) AS childCount FROM Customer c";
         if(parent == null) {
            findString = findString + "WHERE parent IS NULL";
         } else {
            (new StringBuffer()).append(findString).append(" WHERE parent=").append(parent.getObjectId()).toString();
         }
      } finally {
         transaction.close();
      }

      return null;
   }
}
