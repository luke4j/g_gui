package com.luke.dicom.login.display.listener;

import com.luke.dicom.login.display.LoginDisplay;
import com.luke.dicom.login.display.face.*;
import com.luke.jdbc.JDBCDriver;
import org.apache.log4j.Logger;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Properties;

public class BtnLoginMouseListener extends MouseAdapter {

    private static Properties properties = null ;
    static {
        try{
            properties = new Properties() ;
            String filePath = BtnLoginMouseListener.class.getClassLoader().getResource("login_sql/sql.properties").getFile() ;
            properties.load(new FileInputStream(new File(filePath)));
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static Logger log = Logger.getLogger(BtnLoginMouseListener.class) ;

    Text txtLogin ;
    Text txtPassword;
    Shell shell ;
    Canvas canvas ;
    ProgressBar progressBar ;
    Label label ;

    ILoadSetup loadSetup ;
    IOKShell okShell ;
    IStartService startService ;
    IValidateLincese validateLincese ;
    Ilogin login ;

    LoginListenerParams loginParams ;

    public BtnLoginMouseListener(LoginListenerParams loginParams){
        this.loginParams = loginParams ;
        txtLogin = loginParams.getTxtLogin() ;
        txtPassword = loginParams.getTxtPassword();
        shell = loginParams.getShell() ;
        canvas = loginParams.getCanvas() ;
        progressBar = loginParams.getProgressBar() ;
        label = loginParams.getLabel() ;

        loadSetup = loginParams.getiLoadStup() ;
        okShell = loginParams.getIokShell() ;
        startService = loginParams.getiStartService() ;
        validateLincese = loginParams.getiValidateLincese() ;
        login = loginParams.getIlogin() ;

    }
    private Boolean isClick = false ;

    @Override
    public void mouseDown(MouseEvent e){
        if(e.button==1){
            this.isClick = true ;
            this.progressBar.setSelection(0);
        }
    }

    @Override
    public void mouseUp(MouseEvent e) {
        super.mouseUp(e);
        if(e.button==1&&isClick){
            runClick() ;
        }
        isClick = false ;
    }

    private void runClick(){
        progressBar.setBounds(2,523,1025,5);
        final int maximum = progressBar.getMaximum() ;

        try{
            this.login() ;
            progressBar.setSelection(20);
            this.validateLincese() ;
            progressBar.setSelection(40);
            this.loadSetup() ;
            progressBar.setSelection(60);
            this.startService() ;
            progressBar.setSelection(80);
            this.loginOk() ;
            progressBar.setSelection(100);
        }catch (Exception e){
            this.label.setText(e.getMessage());
        }
    }

    private void startService() {
        this.label.setText(LoginDisplay.ApplicationName+"====正在启动服务");
        this.startService.startService(loginParams);
        this.label.setText(LoginDisplay.ApplicationName+"====启动服务完成");
    }

    private void loginOk() {
        this.label.setText(LoginDisplay.ApplicationName+"====登录成功");
        this.okShell.okShell(loginParams);
    }

    private void loadSetup() {
        this.label.setText(LoginDisplay.ApplicationName+"====正在加载配置信息");
        this.loadSetup.loadStup(loginParams);
        this.label.setText(LoginDisplay.ApplicationName+"====加载配置信息完成");
    }

    private void validateLincese() {
        this.label.setText(LoginDisplay.ApplicationName+"====正在验证激活信息");
        this.validateLincese.validateLincese(loginParams);
        this.label.setText(LoginDisplay.ApplicationName+"====验证激活信息成功");
    }

    private void login() {
        this.loginParams.getExtParam().put("properties",properties) ;
        this.loginParams.getIlogin().login(loginParams);
    }
}
