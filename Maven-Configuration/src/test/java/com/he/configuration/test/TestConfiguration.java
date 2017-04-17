package com.he.configuration.test;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Configurations;

// http://commons.apache.org/proper/commons-configuration/
public class TestConfiguration {
    public static  void testProperties() throws Exception {
        Configurations configs = new Configurations();
        PropertiesConfiguration properties = configs.properties("configx.properties");
        properties=com.he.configuration.Configurations.newPropertiesConfiguration("configx.properties");

        properties.setProperty("database.port", 8299);
        System.out.println(properties.getString("database.port"));
        System.out.println(properties.getString("database.port"));
        System.err.println(properties.getBoolean("flag"));

    }

    /**
     *
     * 不能保存进去
     */

    public static void testXml() throws Exception {
        Configurations configs = new Configurations();
        FileBasedConfigurationBuilder<XMLConfiguration> builder = configs.xmlBuilder("paths.xml");
        XMLConfiguration config = builder.getConfiguration();

        config.addProperty("newProperty", "newValue");

        builder.save();
    }
}
