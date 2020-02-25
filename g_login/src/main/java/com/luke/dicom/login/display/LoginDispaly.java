package com.luke.dicom.login.display;

import com.luke.dicom.login.display.listener.BtnLoginMouseListener;
import com.luke.dicom.login.wb.swt.SWTResourceManager;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.*;

public class LoginDispaly {

    private static org.apache.log4j.Logger log = Logger.getLogger(LoginDispaly.class) ;

    Display display = null ;
    Shell shell = null ;
    Text txtLoginName = null ;
    Text txtPassword = null ;
    Button btnLogin = null ;

    public LoginDispaly(){
        display = new Display() ;
        shell = new Shell(display, SWT.NONE) ;
        shell.setSize(1034,538);
        shell.setLocation(Display.getCurrent().getClientArea().width/2-shell.getShell().getSize().x/2
        ,Display.getCurrent().getClientArea().height/2-shell.getShell().getSize().y/2);
        shell.setText("luke-program");

        Canvas canvas = new Canvas(shell,SWT.NONE) ;
        canvas.setBackgroundMode(SWT.INHERIT_FORCE);
        canvas.setBounds(0,0,1034,538);
        canvas.setBackgroundImage(SWTResourceManager.getImage(LoginDispaly.class,"/image/login.jpg"));
        Label label = new Label(canvas,SWT.NONE) ;
        label.setAlignment(SWT.RIGHT);
        label.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        label.setBounds(10,10,100,30);
        label.setText("luke-program");

        Label lblLoginName = new Label(canvas,SWT.NONE) ;
        Label lblPassword = new  Label(canvas,SWT.NONE) ;
        txtLoginName = new Text(canvas,SWT.BORDER) ;
        txtPassword = new Text(canvas,SWT.BORDER) ;
        btnLogin = new Button(canvas,SWT.NONE) ;

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

        btnLogin.addMouseListener(new BtnLoginMouseListener(this.txtLoginName,this.txtPassword));

        //显示窗体
        shell.open();                     //5.显示shell窗体

        //。。。事件                      //6.注册事件
        //窗体是否关闭
        while(!shell.isDisposed()){
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
