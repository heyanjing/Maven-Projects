package com.he.configuration;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;
import java.net.URL;

/**
 * 加载属性文件
 * http://commons.apache.org/proper/commons-configuration/userguide_v1.10/howto_properties.html#Properties_files
 * If you do not specify an absolute path, the file will be searched automatically in the following locations:
 * in the current directory
 * in the user home directory
 * in the classpath
 */
public class Configurations {

    public static PropertiesConfiguration newPropertiesConfiguration(Object obj) {
        org.apache.commons.configuration2.builder.fluent.Configurations configs = new org.apache.commons.configuration2.builder.fluent.Configurations();
        PropertiesConfiguration properties = null;
        try {
            if (obj instanceof String) {
                properties = configs.properties((String) obj);
            } else if (obj instanceof File) {
                properties = configs.properties((File) obj);
            } else if (obj instanceof URL) {
                properties = configs.properties((URL) obj);
            } else {
            }
        } catch (ConfigurationException e) {
        }
        return properties;
    }


}
