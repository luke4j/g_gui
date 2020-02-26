package com.luke.dicom.login.display;

import com.luke.dicom.login.display.face.*;
import com.luke.dicom.login.display.face.adapter.*;
import org.eclipse.swt.widgets.Shell;

import java.util.HashMap;
import java.util.Map;

public class LoginDisplayParam {

    protected ILoadSetup iLoadStup = new LoadSetup();
    protected IOKShell iokShell = new OKShell();
    protected IStartService iStartService = new StartService();
    protected IValidateLincese iValidateLincese = new ValidateLincese();
    protected Ilogin ilogin = new Login() ;

    /**保存一些扩展参数，您愿意怎么用就怎么用，看这类型就知道了*/
    Map<String ,Object> extParam = new HashMap<String,Object>() ;
    /**登录成功后会设置这个属性，您就可以在这个Shell中做您想要做的事*/
    Shell mainShell ;


    public Map<String, Object> getExtParam() {
        return extParam;
    }

    public void setExtParam(Map<String, Object> extParam) {
        this.extParam = extParam;
    }

    public Shell getMainShell() {
        return mainShell;
    }

    public void setMainShell(Shell mainShell) {
        this.mainShell = mainShell;
    }

    public Ilogin getIlogin() {
        return ilogin;
    }

    public void setIlogin(Ilogin ilogin) {
        this.ilogin = ilogin;
    }

    public ILoadSetup getiLoadStup() {
        return iLoadStup;
    }

    public void setiLoadStup(ILoadSetup iLoadStup) {
        this.iLoadStup = iLoadStup;
    }

    public IOKShell getIokShell() {
        return iokShell;
    }

    public void setIokShell(IOKShell iokShell) {
        this.iokShell = iokShell;
    }

    public IStartService getiStartService() {
        return iStartService;
    }

    public void setiStartService(IStartService iStartService) {
        this.iStartService = iStartService;
    }

    public IValidateLincese getiValidateLincese() {
        return iValidateLincese;
    }

    public void setiValidateLincese(IValidateLincese iValidateLincese) {
        this.iValidateLincese = iValidateLincese;
    }
}
