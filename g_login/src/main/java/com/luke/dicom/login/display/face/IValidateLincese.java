package com.luke.dicom.login.display.face;

import com.luke.dicom.login.display.listener.LoginListenerParams;

public interface IValidateLincese {

    /**
     * 验证程序是否激活
     */
    void validateLincese(LoginListenerParams loginParams) ;

}
