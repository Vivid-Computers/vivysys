package vivyclient.model.searchMap;

import java.util.ArrayList;
import java.util.List;
import vivyclient.model.tablemap.TableMapAttribute;

public abstract class BaseSearchMap {
   public static final String VALUE_EQUALS = "equals";
   public static final String VALUE_NOT_EQUALS = "notEquals";
   public static final String VALUE_GREATER_THAN = "greaterThan";
   public static final String VALUE_LESS_THAN = "lessThan";
   public static final String VALUE_GREATER_THAN_OR_EQUALS = "greaterThanOrEqual";
   public static final String VALUE_LESS_THAN_OR_EQUALS = "lessThanOrEqual";
   List whereCriteria = new ArrayList();
   List whereConditions = new ArrayList();
   List orderCriteria = new ArrayList();

   public BaseSearchMap() {
      this.initialise();
   }

   public abstract void initialise();

   public void addWhereCriteria(TableMapAttribute attribute) {
      this.whereCriteria.add(attribute);
      this.whereConditions.add("equals");
   }

   public void addWhereCriteria(TableMapAttribute attribute, String whereCondition) {
      this.whereCriteria.add(attribute);
      this.whereConditions.add(whereCondition);
   }

   public void addOrderCriteria(TableMapAttribute attribute) {
      this.orderCriteria.add(attribute);
   }

   public List getWhereCriteria() {
      return this.whereCriteria;
   }

   public List getWhereConditions() {
      return this.whereConditions;
   }

   public List getOrderCriteria() {
      return this.orderCriteria;
   }
}
