package com.luke.dicom.login.display.face;

import com.luke.dicom.login.display.listener.LoginListenerParams;

public interface IStartService {
    /**
     * 启动所有服务
     */
    void startService(LoginListenerParams loginParams) ;
}
