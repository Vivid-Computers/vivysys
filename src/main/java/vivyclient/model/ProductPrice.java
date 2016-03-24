package vivyclient.model;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import vivyclient.data.TransactionContainer;
import vivyclient.exception.BusinessException;
import vivyclient.model.BaseModel;
import vivyclient.model.Product;
import vivyclient.model.Site;
import vivyclient.model.searchMap.ProductPriceSearchMapFactory;
import vivyclient.model.tablemap.BaseTableMap;
import vivyclient.model.tablemap.ProductPriceTableMap;
import vivyclient.util.Settings;

public class ProductPrice extends BaseModel {
   private Product product;
   private int quantity;
   private Site site;
   private BigDecimal unitPrice;
   private boolean specialPrice;

   public ProductPrice() {
      this.initialise();
   }

   public ProductPrice(int objectId) {
      this.initialise();
      this.objectId = objectId;
   }

   public ProductPrice(int objectId, Product product, int quantity, Site site, BigDecimal unitPrice, boolean specialPrice, int updateId, Calendar lastUpdate) {
      this.objectId = objectId;
      this.product = product;
      this.quantity = quantity;
      this.site = site;
      this.unitPrice = unitPrice;
      this.specialPrice = specialPrice;
      this.updateId = updateId;
      this.lastUpdate = lastUpdate;
   }

   private void initialise() {
      this.objectId = Settings.getNullInt();
      this.product = null;
      this.quantity = 0;
      this.site = null;
      this.unitPrice = null;
      this.specialPrice = false;
      this.updateId = Settings.getNullInt();
      this.lastUpdate = null;
   }

   public Product getProduct() {
      return this.product;
   }

   public void setProduct(Product product) {
      this.product = product;
   }

   public int getQuantity() {
      return this.quantity;
   }

   public void setQuantity(int quantity) {
      this.quantity = quantity;
   }

   public Site getSite() {
      return this.site;
   }

   public void setSite(Site site) {
      this.site = site;
   }

   public BigDecimal getUnitPrice() {
      return this.unitPrice;
   }

   public void setUnitPrice(BigDecimal unitPrice) {
      this.unitPrice = unitPrice;
   }

   public boolean getSpecialPrice() {
      return this.specialPrice;
   }

   public void setSpecialPrice(boolean specialPrice) {
      this.specialPrice = specialPrice;
   }

   public String toString() {
      return this.quantity + " @ $" + this.unitPrice.toString() + (this.specialPrice?"*":"");
   }

   public boolean overlapsWith(ProductPrice compareTo) {
      return this.quantity == compareTo.getQuantity() && this.unitPrice.equals(compareTo.getUnitPrice())?(this.site.equals(compareTo.getSite())?true:(this.site.getObjectId() == 0?true:compareTo.getSite().getObjectId() == 0)):false;
   }

   public static void verifyProductPriceOkay(ProductPrice productPrice, boolean isNew) throws Exception {
      ProductPrice criteria;
      List sitePrices;
      int i;
      ProductPrice existingPrice;
      if(productPrice.getSite().getObjectId() == 0) {
         criteria = new ProductPrice();
         criteria.setProduct(productPrice.getProduct());
         sitePrices = findAll(criteria, ProductPriceSearchMapFactory.getProductSearchMap(), (TransactionContainer)null);

         for(i = 0; i < sitePrices.size(); ++i) {
            existingPrice = (ProductPrice)sitePrices.get(i);
            if(existingPrice.getQuantity() == productPrice.getQuantity() && existingPrice.getUnitPrice().compareTo(productPrice.getUnitPrice()) == 0 && !existingPrice.equals(productPrice)) {
               throw new BusinessException("The Price-Quantity combination already exists for \'" + existingPrice.getSite().getName() + "\'");
            }
         }
      } else {
         criteria = new ProductPrice();
         criteria.setSite(productPrice.getSite());
         criteria.setProduct(productPrice.getProduct());
         sitePrices = findAll(criteria, ProductPriceSearchMapFactory.getSiteProductSearchMap(), (TransactionContainer)null);

         for(i = 0; i < sitePrices.size(); ++i) {
            existingPrice = (ProductPrice)sitePrices.get(i);
            if(existingPrice.getQuantity() == productPrice.getQuantity() && existingPrice.getUnitPrice().compareTo(productPrice.getUnitPrice()) == 0 && !existingPrice.equals(productPrice)) {
               throw new BusinessException("The Price-Quantity combination already exists for \'" + existingPrice.getSite().getName() + "\'");
            }
         }

         criteria.setSite(new Site(0));
         sitePrices = findAll(criteria, ProductPriceSearchMapFactory.getSiteProductSearchMap(), (TransactionContainer)null);

         for(i = 0; i < sitePrices.size(); ++i) {
            existingPrice = (ProductPrice)sitePrices.get(i);
            if(existingPrice.getQuantity() == productPrice.getQuantity() && existingPrice.getUnitPrice().compareTo(productPrice.getUnitPrice()) == 0 && !existingPrice.equals(productPrice)) {
               throw new BusinessException("The Price-Quantity combination already exists for \'" + existingPrice.getSite().getName() + "\'");
            }
         }
      }

   }

   public BaseTableMap getTableMap() {
      return new ProductPriceTableMap();
   }
}
