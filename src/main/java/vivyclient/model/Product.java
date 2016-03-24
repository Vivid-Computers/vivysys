package vivyclient.model;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import vivyclient.data.TransactionContainer;
import vivyclient.exception.BusinessException;
import vivyclient.model.BaseModel;
import vivyclient.model.DurationType;
import vivyclient.model.FeaturedLevel;
import vivyclient.model.LinkType;
import vivyclient.model.ProductMakeup;
import vivyclient.model.ProductPrice;
import vivyclient.model.ProductType;
import vivyclient.model.SubCategory;
import vivyclient.model.dao.BaseDao;
import vivyclient.model.dao.ProductDao;
import vivyclient.model.searchMap.ProductPriceSearchMapFactory;
import vivyclient.model.tablemap.BaseTableMap;
import vivyclient.model.tablemap.ProductTableMap;
import vivyclient.util.Settings;

public class Product extends BaseModel implements Transferable {
   private String name;
   private String manufacturer;
   private SubCategory subCategory;
   private String platform;
   private String media;
   private double freightUnits;
   private boolean academic;
   private String description;
   private String linkUrl;
   private LinkType linkType;
   private String imageName;
   private DurationType warrantyDurationType;
   private int warrantyDurationMultiplier;
   private String warrantyComments;
   private FeaturedLevel featuredLevel;
   private List productPrices;
   private List productMakeup;
   private static HashMap distinctLists = new HashMap();
   public static final String MANUFACTURER_FIELD = "manufacturer";
   public static final String WARRANTY_FIELD = "warranty";
   public static final String FREIGHT_UNITS_FIELD = "freightUnits";
   public static final String PLATFORM_FIELD = "platform";
   public static final String MEDIA_FIELD = "media";
   public static final DataFlavor PRODUCT_FLAVOUR = new DataFlavor("application/x-java-jvm-local-objectref", "Product");

   public Product() {
      this.initialise();
   }

   public Product(int objectId) {
      this.initialise();
      this.objectId = objectId;
   }

   public Product(int objectId, String name, String manufacturer, SubCategory subCategory, String warranty, String platform, String media, double freightUnits, boolean academic, String description, boolean featured, String linkUrl, String imageName, int updateId, Calendar lastUpdate) {
      this.objectId = objectId;
      this.name = name;
      this.manufacturer = manufacturer;
      this.subCategory = subCategory;
      this.platform = platform;
      this.media = media;
      this.freightUnits = freightUnits;
      this.academic = academic;
      this.description = description;
      this.linkUrl = linkUrl;
      this.imageName = imageName;
      this.updateId = updateId;
      this.lastUpdate = lastUpdate;
   }

   private void initialise() {
      this.objectId = Settings.getNullInt();
      this.name = null;
      this.manufacturer = null;
      this.subCategory = null;
      this.platform = null;
      this.media = null;
      this.freightUnits = 0.0D;
      this.academic = false;
      Object description = null;
      this.linkUrl = null;
      this.imageName = null;
      this.updateId = Settings.getNullInt();
      this.lastUpdate = null;
   }

   public void save(TransactionContainer transaction) throws Exception {
      super.save(transaction);
      clearAttributeLists(this.getSubCategory().getCategory().getProductType());
   }

   public static int countAll(TransactionContainer transaction) throws Exception {
      return ProductDao.countAll(transaction);
   }

   private static void clearAttributeLists(ProductType productType) {
      HashMap var1 = distinctLists;
      synchronized(distinctLists) {
         distinctLists.remove(Integer.toString(productType.getObjectId()));
      }
   }

   public static Vector getDistinctList(ProductType productType, String fieldName) throws Exception {
      HashMap var2 = distinctLists;
      synchronized(distinctLists) {
         String typeKey;
         if(productType == null) {
            typeKey = null;
         } else {
            typeKey = Integer.toString(productType.getObjectId());
         }

         HashMap productTypeLists = (HashMap)distinctLists.get(typeKey);
         if(productTypeLists == null) {
            productTypeLists = new HashMap();
            distinctLists.put(typeKey, productTypeLists);
         }

         Vector fieldList = (Vector)productTypeLists.get(fieldName);
         if(fieldList == null) {
            fieldList = ProductDao.getDistinctAttributeList(fieldName, productType);
            productTypeLists.put(fieldName, fieldList);
         }

         return fieldList;
      }
   }

   public DataFlavor[] getTransferDataFlavors() {
      DataFlavor[] supported = new DataFlavor[]{PRODUCT_FLAVOUR};
      return supported;
   }

   public boolean isDataFlavorSupported(DataFlavor flavor) {
      return flavor == PRODUCT_FLAVOUR;
   }

   public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
      if(this.isDataFlavorSupported(flavor)) {
         return this;
      } else {
         throw new UnsupportedFlavorException(flavor);
      }
   }

   public String toString() {
      return this.name != null?this.name:"Product: " + this.objectId;
   }

   public BaseDao getModelDao() {
      return new ProductDao();
   }

   public BaseTableMap getTableMap() {
      return new ProductTableMap();
   }

   public int getProductPriceCount() throws Exception {
      this.lazyProductPriceInitialise();
      return this.productPrices.size();
   }

   public ProductPrice getProductPrice(int index) throws Exception {
      this.lazyProductPriceInitialise();
      return (ProductPrice)this.productPrices.get(index);
   }

   public void addProductPrice(ProductPrice productPrice) throws Exception {
      this.lazyProductPriceInitialise();

      for(int i = 0; i < this.productPrices.size(); ++i) {
         ProductPrice existing = (ProductPrice)this.productPrices.get(i);
         if(productPrice.overlapsWith(existing)) {
            throw new BusinessException("ProductPrice overlaps with existing record");
         }
      }

      this.productPrices.add(productPrice);
   }

   public void removeProductPrice(ProductPrice productPrice) throws Exception {
      this.lazyProductPriceInitialise();
      this.productPrices.remove(productPrice);
      this.addChildForDeletion(productPrice);
   }

   private void lazyProductPriceInitialise() throws Exception {
      if(this.productPrices == null) {
         ProductPrice criteria = new ProductPrice();
         criteria.setProduct(this);
         this.productPrices = ProductPrice.findAll(criteria, ProductPriceSearchMapFactory.getProductSearchMap(), (TransactionContainer)null);
      }

   }

   public int getPartCount() throws Exception {
      this.lazyProductMakeupInitialise();
      return this.productMakeup.size();
   }

   public ProductMakeup getPart(int index) throws Exception {
      this.lazyProductMakeupInitialise();
      return (ProductMakeup)this.productMakeup.get(index);
   }

   public void addPart(ProductMakeup part) throws Exception {
      this.lazyProductMakeupInitialise();
      this.productMakeup.add(part);
   }

   public void removePart(ProductMakeup part) throws Exception {
      this.lazyProductMakeupInitialise();
      this.productMakeup.remove(part);
      this.addChildForDeletion(part);
   }

   private void lazyProductMakeupInitialise() throws Exception {
      if(this.productMakeup == null) {
         this.productMakeup = ProductDao.getProductMakeupList(this, (TransactionContainer)null);
      }

   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getManufacturer() {
      return this.manufacturer;
   }

   public void setManufacturer(String manufacturer) {
      this.manufacturer = manufacturer;
   }

   public SubCategory getSubCategory() {
      return this.subCategory;
   }

   public void setSubCategory(SubCategory subCategory) {
      this.subCategory = subCategory;
   }

   public String getPlatform() {
      return this.platform;
   }

   public void setPlatform(String platform) {
      this.platform = platform;
   }

   public String getMedia() {
      return this.media;
   }

   public void setMedia(String media) {
      this.media = media;
   }

   public double getFreightUnits() {
      return this.freightUnits;
   }

   public void setFreightUnits(double freightUnits) {
      this.freightUnits = freightUnits;
   }

   public boolean getAcademic() {
      return this.academic;
   }

   public void setAcademic(boolean academic) {
      this.academic = academic;
   }

   public String getDescription() {
      return this.description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public String getLinkUrl() {
      return this.linkUrl;
   }

   public void setLinkUrl(String linkUrl) {
      this.linkUrl = linkUrl;
   }

   public String getImageName() {
      return this.imageName;
   }

   public void setImageName(String imageName) {
      this.imageName = imageName;
   }

   public DurationType getWarrantyDurationType() {
      return this.warrantyDurationType;
   }

   public void setWarrantyDurationType(DurationType warrantyDurationType) {
      this.warrantyDurationType = warrantyDurationType;
   }

   public int getWarrantyDurationMultiplier() {
      return this.warrantyDurationMultiplier;
   }

   public void setWarrantyDurationMultiplier(int warrantyDurationMultiplier) {
      this.warrantyDurationMultiplier = warrantyDurationMultiplier;
   }

   public String getWarrantyComments() {
      return this.warrantyComments;
   }

   public void setWarrantyComments(String warrantyComments) {
      this.warrantyComments = warrantyComments;
   }

   public LinkType getLinkType() {
      return this.linkType;
   }

   public void setLinkType(LinkType linkType) {
      this.linkType = linkType;
   }

   public FeaturedLevel getFeaturedLevel() {
      return this.featuredLevel;
   }

   public void setFeaturedLevel(FeaturedLevel featuredLevel) {
      this.featuredLevel = featuredLevel;
   }
}
