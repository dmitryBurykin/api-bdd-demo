package org.db.tests.api.bdd.dataproviders;

import org.db.tests.api.bdd.exceptions.FrameworkException;

import java.io.*;
import java.util.Properties;

/**
 * Created by dmitry on 02.02.19.
 * Класс читает свойства проекта из файла resources/project.properties
 */
public class ProjectPropertiesFileReader {

    private Properties properties;
    private final String propsFilePath = "project.properties";

    public ProjectPropertiesFileReader() {

        try{
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(propsFilePath);
            if (inputStream == null) {
                // Если же ресурс не загрузили, то пытаемся из файловой системы
                inputStream = new FileInputStream(propsFilePath);
            }
            properties = new Properties();
            properties.load(inputStream);
        }
        catch (IOException e){
            e.printStackTrace();
            throw new FrameworkException("Файл со свойствами проекта не найден: " + propsFilePath);
        }
    }

    /**
     * @return value of specified Property name
     */
    public String getProperty(String propertyName){
//        String propertyName = "project.app.url";
        String propertyValue = properties.getProperty(propertyName);
        if (propertyValue!=null){
            return propertyValue;
        }
        else{
            throw new FrameworkException(String.format("Свойство [%s] не найдено в файле: '%s'",
                    propertyName, propsFilePath));
        }
    }

}
