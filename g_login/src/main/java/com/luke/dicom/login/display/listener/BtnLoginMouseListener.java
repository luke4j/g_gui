package com.luke.dicom.login.display.listener;

import com.luke.jdbc.JDBCDriver;
import org.apache.log4j.Logger;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Properties;

public class BtnLoginMouseListener extends MouseAdapter {

    private static Properties properties = null ;
    static {
        try{
            properties = new Properties() ;
            String filePath = BtnLoginMouseListener.class.getClassLoader().getResource("login_sql/sql.properties").getFile() ;
            properties.load(new FileInputStream(new File(filePath)));
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static Logger log = Logger.getLogger(BtnLoginMouseListener.class) ;

    Text txtLogin = null ;
    Text txtPassword = null ;
    Shell shell = null ;

    public BtnLoginMouseListener(Text txtLogin,Text txtPassword){
        this.txtLogin = txtLogin ;
        this.txtPassword = txtPassword ;
    }

    private Boolean isClick = false ;

    @Override
    public void mouseDown(MouseEvent e){
        if(e.button==1){
            this.isClick = true ;
        }
    }

    @Override
    public void mouseUp(MouseEvent e) {
        super.mouseUp(e);
        if(e.button==1&&isClick){
            runClick() ;
        }
        isClick = false ;
    }

    private void runClick(){
        String sql_login = properties.getProperty("sql_login") ;
        sql_login = sql_login.replace("$login_name$",this.txtLogin.getText().trim()).replace("$pwd$",this.txtPassword.getText().trim()) ;
        Map<String,Object> loginObject = JDBCDriver.getDriver().runFindSqlOneResult(sql_login) ;
    }
}
