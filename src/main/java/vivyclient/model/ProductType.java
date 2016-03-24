package vivyclient.model;

import java.util.Calendar;
import vivyclient.model.BaseModel;
import vivyclient.model.tablemap.BaseTableMap;
import vivyclient.model.tablemap.ProductTypeTableMap;

public class ProductType extends BaseModel {
   private String name;
   private int displayOrder;
   private String notes;

   public ProductType() {
      this.initialise();
   }

   public ProductType(int objectId) {
      this.initialise();
      this.objectId = objectId;
   }

   public ProductType(int objectId, String name, int displayOrder, String notes, int updateId, Calendar lastUpdate) {
      this.objectId = objectId;
      this.name = name;
      this.displayOrder = displayOrder;
      this.notes = notes;
      this.updateId = updateId;
      this.lastUpdate = lastUpdate;
   }

   private void initialise() {
      this.objectId = -1;
      this.name = null;
      this.displayOrder = -1;
      this.notes = null;
      this.updateId = -1;
      this.lastUpdate = null;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public int getDisplayOrder() {
      return this.displayOrder;
   }

   public void setDisplayOrder(int displayOrder) {
      this.displayOrder = displayOrder;
   }

   public String getNotes() {
      return this.notes;
   }

   public void setNotes(String notes) {
      this.notes = notes;
   }

   public String toString() {
      return this.name != null?this.name:"ProductType " + this.objectId;
   }

   public BaseTableMap getTableMap() {
      return new ProductTypeTableMap();
   }
}
