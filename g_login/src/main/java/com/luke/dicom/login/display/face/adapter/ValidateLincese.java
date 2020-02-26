package com.luke.dicom.login.display.face.adapter;

import com.luke.dicom.login.display.face.IValidateLincese;
import com.luke.dicom.login.display.listener.LoginListenerParams;

public class ValidateLincese implements IValidateLincese {
    @Override
    public void validateLincese(LoginListenerParams loginParams) {
        try{
            Thread.sleep(100l);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
