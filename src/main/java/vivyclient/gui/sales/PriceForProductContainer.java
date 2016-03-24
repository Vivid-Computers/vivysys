package vivyclient.gui.sales;

import java.math.BigDecimal;
import vivyclient.model.Product;
import vivyclient.util.ViewUtil;

public class PriceForProductContainer {
   private BigDecimal unitPrice;
   private Product product;

   public PriceForProductContainer(BigDecimal unitPrice, Product product) {
      this.unitPrice = unitPrice;
      this.product = product;
   }

   public BigDecimal getUnitPrice() {
      return this.unitPrice;
   }

   public void setUnitPrice(BigDecimal unitPrice) {
      this.unitPrice = unitPrice;
   }

   public Product getProduct() {
      return this.product;
   }

   public void setProduct(Product product) {
      this.product = product;
   }

   public String toString() {
      return this.unitPrice == null?"unset":ViewUtil.currencyDisplay(this.unitPrice);
   }
}
