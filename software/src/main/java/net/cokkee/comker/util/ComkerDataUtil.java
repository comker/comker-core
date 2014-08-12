package net.cokkee.comker.util;

import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.lang.reflect.InvocationTargetException;
import net.cokkee.comker.exception.ComkerIllegalAccessException;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author drupalex
 */
public class ComkerDataUtil {

    public static final String EMPTY_STRING = "";

    //--------------------------------------------------------------------------

    public static final String CODE_PATTERN =
            "^[A-Z0-9_\\-]{1,32}$";

    public static boolean verifyCode(String code) {
        if (code == null) return false;
        return code.matches(CODE_PATTERN);
    }

    //--------------------------------------------------------------------------

    public static String mergeStringArray(String[] strs) {
        if (strs == null) return null;
        
        StringBuilder sb = new StringBuilder();
        if (strs.length >= 1) {
            sb.append(strs[0]);
        }

        for(int i=1; i<strs.length; i++) {
            sb.append(",").append(strs[i]);
        }

        return sb.toString();
    }

    public static String[] splitStringArray(String str) {
        if (str == null) return null;

        String[] strs = str.split(",");
        for(int i=0; i<strs.length; i++) {
            strs[i] = strs[i].trim();
        }

        return strs;
    }

    /**
     * copies properties from one object to another
     *
     * @param src
     *            the source object
     * @param dest
     *            the destination object
     * @param properties
     *            a list of property names that are to be copied. Each value has
     *            the format "srcProperty destProperty". For example,
     *            "name fullName" indicates that you want to copy the src.name
     *            value to dest.fullName. If both the srcProperty and
     *            destProperty property have the same name, you can omit the
     *            destProperty. For example, "name" indicates that you want to
     *            copy src.name to dest.name.
     *
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public static void copyProperties(Object src, Object dest, String... properties) {
        try {
            if (properties != null && properties.length > 0) {
                for (String property : properties) {
                    String[] arr = property.split(" ");
                    String srcProperty;
                    String destProperty;
                    if (arr.length == 2) {
                        srcProperty = arr[0];
                        destProperty = arr[1];
                    } else {
                        srcProperty = property;
                        destProperty = property;
                    }
                    BeanUtils.setProperty(dest, destProperty,
                            BeanUtils.getProperty(src, srcProperty));
                }
            } else {
                BeanUtils.copyProperties(dest, src);
            }
        } catch (IllegalAccessException e) {
            throw new ComkerIllegalAccessException(404, "IllegalAccessException");
        } catch (InvocationTargetException e) {
            throw new ComkerIllegalAccessException(404, "InvocationTargetException");
        } catch (NoSuchMethodException e) {
            throw new ComkerIllegalAccessException(404, "NoSuchMethodException");
        }
    }

    public static void copyPropertiesExcludes(Object src, Object dest, String[] excluded) {
        org.springframework.beans.BeanUtils.copyProperties(src, dest, excluded);
    }

    //--------------------------------------------------------------------------

    public static String convertObjectToJson(Object entity) {
        Gson gson = new Gson();
        return gson.toJson(entity);
    }

    public static <T> T convertJsonToObject(String json, Class<T> clazz) {
        Gson gson = new Gson();
        return gson.fromJson(json, clazz);
    }

    //--------------------------------------------------------------------------
    
    public static String convertObjectToXStream(Object entity) {
        XStream xstream = new XStream(new DomDriver());
        return xstream.toXML(entity);
    }

    public static Object convertXStreamToObject(String json) {
        XStream xstream = new XStream(new DomDriver());
        return xstream.fromXML(json);
    }
}
