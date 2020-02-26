package com.luke.dicom.login.display;

import com.luke.dicom.login.display.face.ILoadSetup;
import com.luke.dicom.login.display.face.IOKShell;
import com.luke.dicom.login.display.face.IStartService;
import com.luke.dicom.login.display.face.IValidateLincese;
import com.luke.dicom.login.display.listener.BtnLoginMouseListener;
import com.luke.dicom.login.display.listener.LoginListenerParams;
import com.luke.dicom.login.wb.swt.SWTResourceManager;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;

public class LoginDisplay {

    private static org.apache.log4j.Logger log = Logger.getLogger(LoginDisplay.class) ;
    public static final String ApplicationName = "luke-program" ;



    Display display = new Display() ;
    Shell shell = new Shell(display, SWT.NONE) ;
    Canvas canvas = new Canvas(shell,SWT.NONE) ;
    Label label = new Label(canvas,SWT.NONE) ;
    Text txtLoginName = new Text(canvas,SWT.BORDER) ;
    Text txtPassword = new Text(canvas,SWT.BORDER);
    Button btnLogin = new Button(canvas,SWT.NONE);
    ProgressBar progressBar = new ProgressBar(canvas,SWT.NONE) ;
    LoginListenerParams params ;

    public LoginDisplay(LoginDisplayParam param){
       this.params = new LoginListenerParams(txtLoginName, txtPassword, shell, canvas, progressBar, label,
               param.getiLoadStup(), param.getIokShell(),param.getiStartService(),param.getiValidateLincese(),param.getIlogin()) ;
       initShow();
    }

    public LoginDisplay(){
        this.params = new LoginListenerParams(txtLoginName, txtPassword, shell, canvas, progressBar, label) ;
        initShow();
    }

    private void initShow(){
        shell.setSize(1034,538);
        shell.setLocation(Display.getCurrent().getClientArea().width/2-shell.getShell().getSize().x/2
                ,Display.getCurrent().getClientArea().height/2-shell.getShell().getSize().y/2);
        shell.setText(ApplicationName);

        canvas.setBackgroundMode(SWT.INHERIT_FORCE);
        canvas.setBounds(0,0,1034,538);
        canvas.setBackgroundImage(SWTResourceManager.getImage(LoginDisplay.class,"/image/login.jpg"));

        label.setAlignment(SWT.LEFT);
        label.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        label.setBounds(10,10,300,30);
        label.setText(ApplicationName);

        Label lblLoginName = new Label(canvas,SWT.NONE) ;
        Label lblPassword = new  Label(canvas,SWT.NONE) ;


        lblLoginName.setAlignment(SWT.RIGHT);
        lblLoginName.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
        lblLoginName.setBounds(600,300,100,30);
        lblLoginName.setText("登录名：");

        lblPassword.setAlignment(SWT.RIGHT);
        lblPassword.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
        lblPassword.setBounds(600,340,100,30);
        lblPassword.setText("密  码：");

        txtLoginName.setBounds(710,290,150,30) ;
        txtPassword .setBounds(710,330,150,30) ;

        btnLogin.setBounds(710,370,150,30);
        btnLogin.setText("登录");

        progressBar = new ProgressBar(canvas,SWT.NONE) ;
        progressBar.setMaximum(100);
        progressBar.setMinimum(10);


        btnLogin.addMouseListener(new BtnLoginMouseListener(params));

        //显示窗体
        shell.open();                     //5.显示shell窗体

        //。。。事件                      //6.注册事件
        //窗体是否关闭
        while(!display.isDisposed()){  //这里判断线程是否关闭，因为shell在登录之后会关闭
            //display线程状态是否工作
            if(!display.readAndDispatch()){
                //display线程休眠
                display.sleep() ;
            }
        }
        //注销display对象
        display.dispose();                  //7. 销毁display
    }

}
