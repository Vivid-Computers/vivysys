package vivyclient.model;

import vivyclient.model.BaseModel;
import vivyclient.model.tablemap.BaseTableMap;
import vivyclient.model.tablemap.RecordEntryTypeTableMap;

public class RecordEntryType extends BaseModel {
   public static RecordEntryType VIVY_CLIENT_ENTRY_TYPE = new RecordEntryType(2);

   public RecordEntryType() {
   }

   public RecordEntryType(int objectId) {
      super(objectId);
   }

   public BaseTableMap getTableMap() {
      return new RecordEntryTypeTableMap();
   }
}
