package com.luke.dicom.login.display.listener;

import com.luke.dicom.login.display.LoginDisplayParam;
import com.luke.dicom.login.display.face.*;
import org.eclipse.swt.widgets.*;

import java.util.HashMap;
import java.util.Map;

public class LoginListenerParams extends LoginDisplayParam {

    private LoginListenerParams(){} ;

    public LoginListenerParams(Text txtLogin, Text txtPassword, Shell shell, Canvas canvas, ProgressBar progressBar, Label label) {
        this.txtLogin = txtLogin;
        this.txtPassword = txtPassword;
        this.shell = shell;
        this.canvas = canvas;
        this.progressBar = progressBar;
        this.label = label;
    }

    public LoginListenerParams(Text txtLogin, Text txtPassword, Shell shell, Canvas canvas, ProgressBar progressBar, Label label, ILoadSetup iLoadStup, IOKShell iokShell, IStartService iStartService, IValidateLincese iValidateLincese,Ilogin ilogin) {
        this.txtLogin = txtLogin;
        this.txtPassword = txtPassword;
        this.shell = shell;
        this.canvas = canvas;
        this.progressBar = progressBar;
        this.label = label;
        this.iLoadStup = iLoadStup;
        this.iokShell = iokShell;
        this.iStartService = iStartService;
        this.iValidateLincese = iValidateLincese;
        this.ilogin = ilogin ;
    }

    /*login组件直接提供*/

    /**登录名输入框*/
    Text txtLogin = null ;
    /**密码输入框*/
    Text txtPassword = null ;
    /**shell窗体*/
    Shell shell = null ;
    /**背景画面，这是其它图形组件的直接上级*/
    Canvas canvas = null ;
    /**进度条组件 */
    ProgressBar progressBar ;
    /**显示信息标签*/
    Label label = null ;

    public Text getTxtLogin() {
        return txtLogin;
    }

    public void setTxtLogin(Text txtLogin) {
        this.txtLogin = txtLogin;
    }

    public Text getTxtPassword() {
        return txtPassword;
    }

    public void setTxtPassword(Text txtPassword) {
        this.txtPassword = txtPassword;
    }

    public Shell getShell() {
        return shell;
    }

    public void setShell(Shell shell) {
        this.shell = shell;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }


}
