package vivyclient.model.dao;

import vivyclient.data.TransactionContainer;
import vivyclient.model.BaseModel;
import vivyclient.model.dao.BaseDao;

public class SubCategoryDao extends BaseDao {
   public static int USER_ENTERED_ID_CEILING = 1000;

   public int getNextObjectId(BaseModel model, TransactionContainer transaction) throws Exception {
      return this.getNextObjectId(model, USER_ENTERED_ID_CEILING, transaction);
   }
}
