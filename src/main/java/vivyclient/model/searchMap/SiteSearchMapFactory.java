package vivyclient.model.searchMap;

import vivyclient.model.searchMap.AllSitesSearchMap;
import vivyclient.model.searchMap.BaseSearchMap;

public class SiteSearchMapFactory {
   public static BaseSearchMap getAllSearchMap() {
      return new AllSitesSearchMap();
   }
}
