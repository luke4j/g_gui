package com.luke.jdbc;

import com.mysql.cj.protocol.Resultset;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.*;

public class JDBCDriver {

    Connection connection = null ;

    private JDBCDriver(){} ;
    public static final JDBCDriver getDriver(){
        return new JDBCDriver() ;
    }


    private static String name ;
    private static String pwd ;
    private static String url ;
    static {
        try{
            Properties properties = new Properties() ;
            properties.load(new FileInputStream(new File(JDBCDriver.class.getClassLoader().getResource("jdbc.properties").getFile())));
            Class.forName(properties.getProperty("driver")) ;
            url = properties.getProperty("url") ;
            name = properties.getProperty("name") ;
            pwd = properties.getProperty("pwd") ;
        }catch (FileNotFoundException e){
            System.out.println("=================jdbc配置文件丢失=======================");
        }catch (ClassNotFoundException e){
            System.out.println("=================不能加载jdbc驱动=======================");
        }catch (Exception e){
            System.out.println("=================程序异常===============================");
        }
    }

    /**
     * 返回jdbc连接
     * @return
     */
    public Connection getConnection(){
        try{
            if(connection!=null&&!connection.isClosed()){
                return connection ;
            }
            connection = DriverManager.getConnection(url,name,pwd) ;
            return connection ;
        }catch (SQLException e){
            e.printStackTrace();
        }
        closeResources(null,null,this.connection);
        return getConnection() ;
    }

    /**
     * 关闭数据库资源
     * @param rs
     * @param ps
     * @param connection
     */
    private void closeResources(ResultSet rs,PreparedStatement ps,Connection connection){
        try{
            if(rs!=null&&!rs.isClosed()){
                rs.close();
            }
            if(ps!=null&&!ps.isClosed()){
                ps.close();
            }
            if(connection!=null&&!connection.isClosed()){
                connection.close();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * 执行单条非查询语句
     * @param sql
     * @return
     */
    public Boolean runSql(String sql){
        try{
            PreparedStatement pst = this.getConnection().prepareStatement(sql) ;
            pst.execute() ;
            closeResources(null,pst,null);
            return true ;
        }catch (SQLException e){
            e.printStackTrace();
        }
        throw new RuntimeException("执行sql 语句 ["+sql+"]"+"失败") ;
    }

    /**
     * 事务执行多条非查询语句
     * @param sqls
     * @return
     */
    public Boolean runSqls(List<String> sqls){
        Boolean isOk = true ;
        try{
            this.getConnection().setAutoCommit(false);
            for(String sql :sqls){
                isOk = runSql(sql) ;
                if(!isOk){
                    break ;
                }
            }
            this.getConnection().commit();
        }catch (Exception e){
            e.printStackTrace();
        }
        return isOk ;
    }

    /**
     * 执行查询语句<br>
     *例如：<br>
     *     String sql = "select id as id ,name as name ,login_name as login_name ,pwd as pwd from dt_user " ;<br>
     *     keys = new String[]{id,name,login_name,pwd} ;<br>
     *     JDBCDriver.create().runFindSql(sql,keys) ;<br>
     * @param sql
     * @param keys
     * @return
     */
    public List<Map<String,Object>> runFindSql(String sql,String...keys){
        sql = sql.toLowerCase() ;
        if(sql.indexOf("select")==-1){
            throw new RuntimeException("查询sql语句中列名请使用别名，例如：select id as id ,name as name ,login_name as login_name... from ...") ;
        }
        if(sql.indexOf("from")==-1){
            throw new RuntimeException("查询sql语句中列名请使用别名，例如：select id as id ,name as name ,login_name as login_name... from ...") ;
        }

        List<Map<String,Object>> results = new ArrayList<Map<String,Object>>(1000) ;
        try{
            PreparedStatement ps = this.getConnection().prepareStatement(sql) ;
            ResultSet rs = ps.executeQuery() ;
            Map<String,Object> result = null ;
            while (rs.next()){
                result = new HashMap<String,Object>() ;
                for(String key:keys){
                    result.put(key,rs.getObject(key)) ;
                }
                results.add(result) ;
            }

            return results ;
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage()) ;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage()) ;
        }
    }


    public List<Map<String,Object>> runFindSql(String sql){
        sql = sql.toLowerCase() ;
        if(sql.indexOf("as")==-1){
            throw new RuntimeException("查询sql语句中列名请使用别名，例如：select id as id ,name as name ,login_name as login_name... from ...") ;
        }

        String[] columns = sql.split("as") ;
        List<String > keys = new ArrayList<>(columns.length) ;
        for(int i = 0 ;i < columns.length ;i++ ){
            if(i==0) continue;
            if(i==(columns.length-1)){
                keys.add(columns[i].split(" ,")[0].trim().split(" ")[0]) ;
            }else{
                keys.add(columns[i].split(" ,")[0].trim()) ;
            }
        }
        String[] keys2 = new String[keys.size()] ;
        keys.toArray(keys2) ;
        return runFindSql(sql,keys2) ;
    }

    public Map<String,Object> runFindSqlOneResult(String sql){
        List<Map<String,Object>> rt = this.runFindSql(sql) ;
        if(rt.size()==1)
            return rt.get(0) ;
        throw new RuntimeException("查询数据异常：sql :"+sql) ;
    }



}
