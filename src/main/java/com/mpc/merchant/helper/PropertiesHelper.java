package com.mpc.merchant.helper;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesHelper {
    private Logger log = LogManager.getLogger(getClass());
    public String locfile = null;

    public PropertiesHelper() {

    }

    public PropertiesHelper(String locfile) {
        this.locfile = locfile;
    }

    public String getPropertis(String key){
        String value = null;
        try {
            Properties properties = new Properties();
            ClassPathResource classPathResource = new ClassPathResource(locfile);
            InputStream inputStream = classPathResource.getInputStream();
            properties.load(inputStream);

            value = properties.getProperty(key);
        }catch (Exception e){
            e.printStackTrace();
        }
        return value;
    }

    public String getPropertis(String locFile, String key){
        String value = null;
        try {
            Properties properties = new Properties();
            ClassPathResource classPathResource = new ClassPathResource(locFile);
            InputStream inputStream = classPathResource.getInputStream();
            properties.load(inputStream);

            value = properties.getProperty(key);
        }catch (Exception e){
            e.printStackTrace();
        }
        return value;
    }
}
