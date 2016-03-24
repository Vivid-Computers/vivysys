package vivyclient.model.tablemap;

import vivyclient.model.tablemap.BaseTableMap;
import vivyclient.model.tablemap.TableMapAttribute;

public class ProductTableMap extends BaseTableMap {
   private static final String TABLE_NAME = "Product";

   public String getTableName() {
      return "Product";
   }

   public void initialise() {
      this.addAttribute(new TableMapAttribute("name", "name"));
      this.addAttribute(new TableMapAttribute("manufacturer", "manufacturer"));
      this.addAttribute(new TableMapAttribute("subCategory", "subCategoryId"));
      this.addAttribute(new TableMapAttribute("warrantyDurationType", "warrantyDurationTypeId"));
      this.addAttribute(new TableMapAttribute("warrantyDurationMultiplier", "warrantyDurationMultiplier"));
      this.addAttribute(new TableMapAttribute("warrantyComments", "warrantyComments"));
      this.addAttribute(new TableMapAttribute("platform", "platform"));
      this.addAttribute(new TableMapAttribute("media", "media"));
      this.addAttribute(new TableMapAttribute("freightUnits", "freightUnits"));
      this.addAttribute(new TableMapAttribute("academic", "academic"));
      this.addAttribute(new TableMapAttribute("description", "description"));
      this.addAttribute(new TableMapAttribute("featuredLevel", "featured"));
      this.addAttribute(new TableMapAttribute("linkUrl", "linkUrl"));
      this.addAttribute(new TableMapAttribute("linkType", "linkTypeId"));
      this.addAttribute(new TableMapAttribute("imageName", "imageName"));
   }
}
