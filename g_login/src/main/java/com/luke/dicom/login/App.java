package com.luke.dicom.login;

import com.luke.dicom.login.display.LoginDisplay;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class App {
    public static void main(String[] args) throws Exception{
        File file = new File(App.class.getClassLoader().getResource("log4j.properties").getFile()) ;
        Properties properties = new Properties() ;
        properties.load(new FileInputStream(file));
        PropertyConfigurator.configure(properties);



        new LoginDisplay() ;
    }
}
