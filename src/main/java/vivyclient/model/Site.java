package vivyclient.model;

import java.util.Calendar;
import vivyclient.model.BaseModel;
import vivyclient.model.tablemap.BaseTableMap;
import vivyclient.model.tablemap.SiteTableMap;
import vivyclient.util.Settings;

public class Site extends BaseModel {
   public static final int ALL_SITES_ID = 0;
   public static Site NOT_A_SITE = new Site(Settings.getNullInt(), "No Site", Settings.getNullInt(), (Calendar)null);
   private String name;

   public Site() {
      this.initialise();
   }

   public Site(int objectId) {
      this.initialise();
      this.objectId = objectId;
   }

   public Site(int objectId, String name, int updateId, Calendar lastUpdate) {
      this.objectId = objectId;
      this.name = name;
      this.updateId = updateId;
      this.lastUpdate = lastUpdate;
   }

   private void initialise() {
      this.objectId = Settings.getNullInt();
      this.name = null;
      this.updateId = Settings.getNullInt();
      this.lastUpdate = null;
   }

   public String toString() {
      return this.name != null?this.name:"Site " + this.objectId;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public BaseTableMap getTableMap() {
      return new SiteTableMap();
   }
}
