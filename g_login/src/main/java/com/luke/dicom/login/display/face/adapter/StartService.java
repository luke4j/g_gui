package com.luke.dicom.login.display.face.adapter;

import com.luke.dicom.login.display.face.IStartService;
import com.luke.dicom.login.display.listener.LoginListenerParams;

public class StartService implements IStartService {
    @Override
    public void startService(LoginListenerParams loginParams) {
        try{
            Thread.sleep(100l);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
