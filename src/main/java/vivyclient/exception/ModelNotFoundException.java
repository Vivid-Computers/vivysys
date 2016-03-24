package vivyclient.exception;

import vivyclient.model.BaseModel;

public class ModelNotFoundException extends Exception {
   private BaseModel model;

   public ModelNotFoundException(BaseModel model) {
      this.model = model;
   }

   public BaseModel getModel() {
      return this.model;
   }

   public void setModel(BaseModel model) {
      this.model = model;
   }
}
