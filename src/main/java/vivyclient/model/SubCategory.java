package vivyclient.model;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Calendar;
import vivyclient.model.BaseModel;
import vivyclient.model.Category;
import vivyclient.model.dao.BaseDao;
import vivyclient.model.dao.SubCategoryDao;
import vivyclient.model.tablemap.BaseTableMap;
import vivyclient.model.tablemap.SubCategoryTableMap;

public class SubCategory extends BaseModel implements Transferable {
   private Category category;
   private String name;
   private String notes;
   private int displayOrder;
   public static final DataFlavor SUBCATEGORY_FLAVOUR = new DataFlavor("application/x-java-jvm-local-objectref", "SubCategory");
   private static DataFlavor[] supported;

   public SubCategory() {
      this.initialise();
   }

   public SubCategory(int objectId) {
      this.initialise();
      this.objectId = objectId;
   }

   public SubCategory(int objectId, Category category, String name, String notes, int displayOrder, int updateId, Calendar lastUpdate) {
      this.objectId = objectId;
      this.updateId = updateId;
      this.lastUpdate = lastUpdate;
      this.category = category;
      this.name = name;
      this.notes = notes;
      this.displayOrder = displayOrder;
   }

   private void initialise() {
      this.objectId = -1;
      this.updateId = -1;
      this.lastUpdate = null;
      this.category = null;
      this.name = null;
      this.notes = null;
      this.displayOrder = 0;
   }

   public DataFlavor[] getTransferDataFlavors() {
      return supported;
   }

   public boolean isDataFlavorSupported(DataFlavor flavor) {
      return flavor == SUBCATEGORY_FLAVOUR;
   }

   public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
      if(this.isDataFlavorSupported(flavor)) {
         return this;
      } else {
         throw new UnsupportedFlavorException(flavor);
      }
   }

   public String toString() {
      return this.name != null && this.name.trim().length() > 0?this.name:"SubCategory: " + this.objectId;
   }

   public Category getCategory() {
      return this.category;
   }

   public void setCategory(Category category) {
      this.category = category;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getNotes() {
      return this.notes;
   }

   public void setNotes(String notes) {
      this.notes = notes;
   }

   public int getDisplayOrder() {
      return this.displayOrder;
   }

   public void setDisplayOrder(int displayOrder) {
      this.displayOrder = displayOrder;
   }

   public BaseDao getModelDao() {
      return new SubCategoryDao();
   }

   public BaseTableMap getTableMap() {
      return new SubCategoryTableMap();
   }

   static {
      supported = new DataFlavor[]{SUBCATEGORY_FLAVOUR};
   }
}
