package vivyclient.model.tablemap;

import java.util.ArrayList;
import java.util.List;
import vivyclient.model.tablemap.TableMapAttribute;

public abstract class BaseTableMap {
   private List mapAttributes = new ArrayList();
   private TableMapAttribute defaultOrderingAttribute;

   public BaseTableMap() {
      this.initialise();
      this.addAttribute(new TableMapAttribute("updateId", "updateId"));
      this.addAttribute(new TableMapAttribute("lastUpdate", "lastUpdate", true));
      this.addAttribute(new TableMapAttribute("recordEntryType", "recordEntryTypeId"));
   }

   public abstract void initialise();

   public abstract String getTableName();

   public void addAttribute(TableMapAttribute attribute) {
      this.addAttribute(attribute, false);
   }

   public void addAttribute(TableMapAttribute attribute, boolean isDefaultOrderingAttribute) {
      this.mapAttributes.add(attribute);
      if(isDefaultOrderingAttribute) {
         this.defaultOrderingAttribute = attribute;
      }

   }

   public List getMapAttributes() {
      return this.mapAttributes;
   }

   public void setMapAttributes(List mapAttributes) {
      this.mapAttributes = mapAttributes;
   }

   public TableMapAttribute getDefaultOrderingAttribute() {
      return this.defaultOrderingAttribute;
   }
}
