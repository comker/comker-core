package net.cokkee.comker.storage;

/**
 *
 * @author drupalex
 */
public interface ComkerSettingStorage {

    <T> void define(String spotCode, String username, String keyCode, Class<T> clazz, T defaultValue);

    <T> T getValue(String spotCode, String username, String keyCode, Class<T> clazz);

    <T> void setValue(String spotCode, String username, String keyCode, Class<T> clazz, T value);
}
