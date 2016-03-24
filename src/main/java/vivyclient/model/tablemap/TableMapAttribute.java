package vivyclient.model.tablemap;

public class TableMapAttribute {
   private String propertyName;
   private String columnName;
   private boolean saveDatabaseDefault;

   public TableMapAttribute(String propertyName, String columnName) {
      this(propertyName, columnName, false);
   }

   public TableMapAttribute(String propertyName, String columnName, boolean saveDatabaseDefault) {
      this.propertyName = propertyName;
      this.columnName = columnName;
      this.saveDatabaseDefault = saveDatabaseDefault;
   }

   public String getPropertyName() {
      return this.propertyName;
   }

   public void setPropertyName(String propertyName) {
      this.propertyName = propertyName;
   }

   public String getColumnName() {
      return this.columnName;
   }

   public void setColumnName(String columnName) {
      this.columnName = columnName;
   }

   public boolean getSaveDatabaseDefault() {
      return this.saveDatabaseDefault;
   }

   public void setSaveDatabaseDefault(boolean saveDatabaseDefault) {
      this.saveDatabaseDefault = saveDatabaseDefault;
   }
}
