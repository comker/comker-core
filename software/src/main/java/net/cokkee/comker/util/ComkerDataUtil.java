package net.cokkee.comker.util;

import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.lang.reflect.InvocationTargetException;
import net.cokkee.comker.exception.ComkerCopyPropertiesException;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author drupalex
 */
public class ComkerDataUtil {

    public static final String EMPTY_STRING = "";

    //--------------------------------------------------------------------------

    public static final String UUID_PATTERN =
            "^[a-fA-F0-9]{8}-?[a-fA-F0-9]{4}-?[a-fA-F0-9]{4}-?[a-fA-F0-9]{4}-?[a-fA-F0-9]{12}$";

    public static boolean verifyUUID(String uuid) {
        if (uuid == null) return false;
        return uuid.matches(UUID_PATTERN);
    }
    
    //--------------------------------------------------------------------------

    public static final String AUTHORITY_PATTERN =
            "^[A-Z][A-Z0-9_]{0,31}$";

    public static boolean verifyAuthority(String authority) {
        if (authority == null) return false;
        return authority.matches(AUTHORITY_PATTERN);
    }

    //--------------------------------------------------------------------------

    public static final String CODE_PATTERN =
            "^[A-Za-z0-9_\\-]{1,32}$";

    public static boolean verifyCode(String code) {
        if (code == null) return false;
        return code.matches(CODE_PATTERN);
    }

    //--------------------------------------------------------------------------

    private static final String EMAIL_PATTERN =
		"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
		+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static boolean verifyEmail(String email) {
        if (email == null) return false;
        return email.matches(EMAIL_PATTERN);
    }

    //--------------------------------------------------------------------------

    private static final String USERNAME_PATTERN =
		"^[a-z0-9_-]{3,16}$";

    public static boolean verifyUsername(String username) {
        if (username == null) return false;
        return username.matches(USERNAME_PATTERN);
    }

    //--------------------------------------------------------------------------
    /* http://stackoverflow.com/questions/3802192/regexp-java-for-password-validation
        ^                 # start-of-string
        (?=.*[0-9])       # a digit must occur at least once
        (?=.*[a-z])       # a lower case letter must occur at least once
        (?=.*[A-Z])       # an upper case letter must occur at least once
        (?=.*[@#$%^&+=])  # a special character must occur at least once
        (?=\S+$)          # no whitespace allowed in the entire string
        .{8,}             # anything, at least eight places though
        $                 # end-of-string
    */
    private static final String PASSWORD_PATTERN =
		"^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

    public static boolean verifyPassword(String password) {
        if (password == null) return false;
        return password.matches(PASSWORD_PATTERN);
    }

    //--------------------------------------------------------------------------

    private static final String FULLNAME_PATTERN =
            "^[a-zA-Z_ÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶ" +
            "ẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợ" +
            "ụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ\\s]+$";

    public static boolean verifyFullname(String fullname) {
        if (fullname == null) return false;
        return fullname.matches(FULLNAME_PATTERN);
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
            throw new ComkerCopyPropertiesException("IllegalAccessException", e);
        } catch (InvocationTargetException e) {
            throw new ComkerCopyPropertiesException("InvocationTargetException", e);
        } catch (NoSuchMethodException e) {
            throw new ComkerCopyPropertiesException("NoSuchMethodException", e);
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

    //--------------------------------------------------------------------------
    
    public static String convertObjectToString(Object value) {
        if (value == null) return null;
        return value.toString();
    }
    
    //--------------------------------------------------------------------------
    
    public static boolean isStringEmpty(String s) {
        return (s == null || s.length() == 0 || s.trim().length() == 0);
    }
}
