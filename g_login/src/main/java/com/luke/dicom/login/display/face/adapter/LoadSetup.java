package com.luke.dicom.login.display.face.adapter;

import com.luke.dicom.login.display.face.ILoadSetup;
import com.luke.dicom.login.display.listener.LoginListenerParams;

public class LoadSetup implements ILoadSetup {
    @Override
    public void loadStup(LoginListenerParams loginParams) {
        try{
            Thread.sleep(100l);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
