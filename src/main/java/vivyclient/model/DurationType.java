package vivyclient.model;

import vivyclient.model.BaseModel;
import vivyclient.model.tablemap.BaseTableMap;
import vivyclient.model.tablemap.DurationTypeTableMap;

public class DurationType extends BaseModel {
   public static final DurationType DAYS_DURATION_TYPE = new DurationType(1);
   public static final DurationType MONTHS_DURATION_TYPE = new DurationType(2);
   public static final DurationType YEARS_DURATION_TYPE = new DurationType(3);
   public static final DurationType LIFETIME_DURATION_TYPE = new DurationType(4);
   public static final DurationType BILLING_MONTH_DURATION_TYPE = new DurationType(5);
   private static final String DAYS_DURATION_TYPE_SHORT_CODE = "D";
   private static final String MONTHS_DURATION_TYPE_SHORT_CODE = "M";
   private static final String YEARS_DURATION_TYPE_SHORT_CODE = "Y";
   private static final String LIFETIME_DURATION_TYPE_SHORT_CODE = "L";
   private String name;

   public DurationType() {
   }

   public DurationType(int objectId) {
      super(objectId);
   }

   public BaseTableMap getTableMap() {
      return new DurationTypeTableMap();
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String toString() {
      return this.name != null?this.name:"DurationType " + this.objectId;
   }

   public String getShortDisplayLetter() {
      return this.objectId == DAYS_DURATION_TYPE.getObjectId()?"D":(this.objectId == MONTHS_DURATION_TYPE.getObjectId()?"M":(this.objectId == LIFETIME_DURATION_TYPE.getObjectId()?"L":"Y"));
   }

   public static DurationType getDurationTypeForCode(String code) throws IllegalArgumentException {
      if("D".equals(code)) {
         return DAYS_DURATION_TYPE;
      } else if("M".equals(code)) {
         return MONTHS_DURATION_TYPE;
      } else if("Y".equals(code)) {
         return YEARS_DURATION_TYPE;
      } else if("L".equals(code)) {
         return LIFETIME_DURATION_TYPE;
      } else {
         throw new IllegalArgumentException("Duration Code not recognised: " + code);
      }
   }
}
