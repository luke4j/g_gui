package com.luke.dicom.login.display.face.adapter;

import com.luke.dicom.login.display.LoginDisplay;
import com.luke.dicom.login.display.face.Ilogin;
import com.luke.dicom.login.display.listener.LoginListenerParams;
import com.luke.jdbc.JDBCDriver;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

import java.util.Map;
import java.util.Properties;

public class Login implements Ilogin {
    @Override
    public void login(LoginListenerParams param) {
        try{
            param.getLabel().setText(LoginDisplay.ApplicationName+"====正在登录");
            Properties properties = (Properties)param.getExtParam().get("properties") ;
            String sql_login = properties.getProperty("sql_login") ;
            sql_login = sql_login.replace("$login_name$",param.getTxtLogin().getText().trim()).replace("$pwd$",param.getTxtPassword().getText().trim()) ;
            Map<String,Object> loginObject = JDBCDriver.getDriver().runFindSqlOneResult(sql_login) ;
        }catch (Exception e){
            MessageBox messageBox = new MessageBox(param.getShell(), SWT.ICON_WARNING) ;
            messageBox.setMessage("登录失败");
            messageBox.setText("失败");
            messageBox.open() ;
            param.getLabel().setText(LoginDisplay.ApplicationName+"====登录失败");
            throw new RuntimeException("登录失败") ;
        }
    }
}
