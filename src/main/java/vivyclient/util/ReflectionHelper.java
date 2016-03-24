package vivyclient.util;

import java.lang.reflect.Method;

public class ReflectionHelper {
   public static Method getGetter(Class target, String propertyName) throws Exception {
      return target.getMethod(getMethodName(propertyName, true), (Class[])null);
   }

   public static Method getSetter(Class target, String propertyName, Class paramType) throws Exception {
      Class[] param = new Class[]{paramType};
      return target.getMethod(getMethodName(propertyName, false), param);
   }

   public static Class getPropertyType(Class target, String propertyName) throws Exception {
      return getGetter(target, propertyName).getReturnType();
   }

   private static String getMethodName(String propertyName, boolean read) {
      return (read?"get":"set") + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1, propertyName.length());
   }
}
