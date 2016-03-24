package vivyclient.model;

import java.math.BigDecimal;
import vivyclient.model.BaseModel;
import vivyclient.model.Dispatch;
import vivyclient.model.SaleDetail;
import vivyclient.model.tablemap.BaseTableMap;
import vivyclient.model.tablemap.DispatchDetailTableMap;

public class DispatchDetail extends BaseModel {
   private Dispatch dispatch;
   private SaleDetail shippedSaleDetail;
   private BigDecimal quantity;
   private String serialNumbers;

   public BaseTableMap getTableMap() {
      return new DispatchDetailTableMap();
   }

   public Dispatch getDispatch() {
      return this.dispatch;
   }

   public void setDispatch(Dispatch dispatch) {
      this.dispatch = dispatch;
   }

   public SaleDetail getShippedSaleDetail() {
      return this.shippedSaleDetail;
   }

   public void setShippedSaleDetail(SaleDetail shippedSaleDetail) {
      this.shippedSaleDetail = shippedSaleDetail;
   }

   public BigDecimal getQuantity() {
      return this.quantity;
   }

   public void setQuantity(BigDecimal quantity) {
      this.quantity = quantity;
   }

   public String getSerialNumbers() {
      return this.serialNumbers;
   }

   public void setSerialNumbers(String serialNumbers) {
      this.serialNumbers = serialNumbers;
   }

   public String toString() {
      return this.quantity + " of " + this.shippedSaleDetail.toString();
   }
}
