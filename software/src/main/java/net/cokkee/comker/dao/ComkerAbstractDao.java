package net.cokkee.comker.dao;

import java.util.List;
import java.util.Map;
import net.cokkee.comker.model.ComkerPager;

/**
 *
 * @author drupalex
 */
public interface ComkerAbstractDao {

    public static final String FIELD_AUTHORITY = "authority";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_CODE = "code";
    public static final String FIELD_EMAIL = "email";
    public static final String FIELD_USERNAME = "username";
    public static final String FIELD_FULLNAME = "fullname";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_PARENT = "parent";

    Integer count(String query, Map<String,Object> params);
    
    List findAll(String query, Map<String,Object> params, ComkerPager filter);
}
