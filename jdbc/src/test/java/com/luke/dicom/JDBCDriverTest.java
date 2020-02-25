package com.luke.dicom;


import com.luke.jdbc.JDBCDriver;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Unit test for simple App.
 */
public class JDBCDriverTest {

    @Test
    public void testGetConnect(){
        Connection connection = JDBCDriver.getDriver().getConnection() ;
        Assert.assertNotNull("创建JDBC Driver 失败",connection);
    }

    @Test
    public void testRunSql(){
        //JDBCDriver.create().runSql("select id as id ,name as name ,login_name as login_name ,pwd as pwd from dt_user ") ;
        JDBCDriver.getDriver().runSql("insert into dt_user (name,login_name,pwd) values('luke','luke','luke') ") ;
    }

    @Test
    public void testRunSqls(){
        //JDBCDriver.create().runSql("select id as id ,name as name ,login_name as login_name ,pwd as pwd from dt_user ") ;
        List<String> sqls = new ArrayList<String>() ;
        sqls.add("insert into dt_user (name,login_name,pwd) values('luke1','luke1','luke1') ") ;
        sqls.add("insert into dt_user (name,login_name,pwd) values('luke2','luke2','luke2') ") ;
        sqls.add("insert into dt_user (name,login_name,pwd) values('luke3','luke3','luke3') ") ;
        sqls.add("insert into dt_user (name,login_name,pwd) values('luke4','luke4','luke4') ") ;
        JDBCDriver.getDriver().runSqls(sqls) ;
    }

    @Test
    public void testRunFindSql(){
        List<Map<String,Object>> rt = JDBCDriver.getDriver().runFindSql("select id as id ,name as name ,login_name as login_name ,pwd as pwd from dt_user") ;
        System.out.println("结果集大小："+rt.size());
        Assert.assertNotNull("检查数据库数据或查看程序",rt);
    }

}
