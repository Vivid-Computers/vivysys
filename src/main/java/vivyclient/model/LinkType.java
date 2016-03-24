package vivyclient.model;

import vivyclient.model.BaseModel;
import vivyclient.model.tablemap.BaseTableMap;
import vivyclient.model.tablemap.LinkTypeTableMap;

public class LinkType extends BaseModel {
   private String name;
   private String urlStructure;

   public LinkType() {
   }

   public LinkType(int objectId) {
      super(objectId);
   }

   public BaseTableMap getTableMap() {
      return new LinkTypeTableMap();
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String toString() {
      return this.name != null?this.name:"LinkType " + this.objectId;
   }

   public String getUrlStructure() {
      return this.urlStructure;
   }

   public void setUrlStructure(String urlStructure) {
      this.urlStructure = urlStructure;
   }
}
