package com.luke.dicom.login.display.face.adapter;

import com.luke.dicom.login.display.LoginDisplay;
import com.luke.dicom.login.display.face.IOKShell;
import com.luke.dicom.login.display.listener.LoginListenerParams;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Shell;

public class OKShell implements IOKShell {
    @Override
    public void okShell(LoginListenerParams loginParams) {
        loginParams.getShell().getDisplay().asyncExec(new Runnable() {
            @Override
            public void run() {
                Shell shell = new Shell(loginParams.getShell().getDisplay()) ;
                shell.setText(LoginDisplay.ApplicationName);
                shell.setLayout(new GridLayout(2, false));
                shell.setMaximized(true);
                shell.open() ;
                loginParams.setMainShell(shell);
                loginParams.getShell().close();
            }
        });


    }
}
