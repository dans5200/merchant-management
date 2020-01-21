package com.mpc.merchant.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ConnectionHelper {
    private Connection connection = null;
    private PropertiesHelper applicationProperties = new PropertiesHelper("application.properties");

    private Logger log = LogManager.getLogger(getClass());

    private String select = "*";
    private String tableName = "";
    private String where = "where ";
    private List<Object> param = new ArrayList<>();


    public ConnectionHelper() {
       connection =  setConnection();
    }

    public Connection setConnection(){
        try {
            Class.forName(applicationProperties.getPropertis("spring.datasource.driver-class-name"));
            this.connection =  DriverManager.getConnection(
                    applicationProperties.getPropertis("spring.datasource.url"),
                    applicationProperties.getPropertis("spring.datasource.username"),
                    applicationProperties.getPropertis("spring.datasource.password")
            );
        }catch (Exception e){
            log.error(e);
        }

        return connection;
    }

    private ResultSet executeQuery(String sql){
        ResultSet resultSet = null;
        try {
            Statement statement = (Statement) connection.createStatement();
            resultSet = statement.executeQuery(sql);

        } catch (Exception e) {
            log.error(e);
        }

        return resultSet;
    }

    private ResultSet executeQuery(String sql, List<Object> params){
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            Integer i = 1;
            for (Object param : params) {
                preparedStatement.setObject(i, param);
                i++;
            }
            resultSet = preparedStatement.executeQuery();
        } catch (Exception e) {
            log.error(e);
        }

        return resultSet;
    }

    private Integer executeUpdate(String sql){
        Integer status = null;
        try {
            Statement statement = (Statement) connection.createStatement();
            status = statement.executeUpdate(sql);
        } catch (Exception e) {
            log.error(e);
        }

        return status;
    }

    private Integer executeUpdate(String sql, List<Object> params){
        Integer status = null;
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            Integer i = 1;
            for (Object param : params) {
                statement.setObject(i, param);
                i++;
            }
            status = statement.executeUpdate();
        } catch (Exception e) {
            log.error(e);
        }

        return status;
    }

    public Object save(Object object){
        try{
            param = new ArrayList<>();
            StringHelper stringHelper = new StringHelper();
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> map = objectMapper.convertValue(object, Map.class);
            String sql = "insert into "+ stringHelper.strConvertCU(object.getClass().getSimpleName());
            String field = "(";
            String values = " values(";

            for (Map.Entry<String, Object> mapObj : map.entrySet()){
                field += stringHelper.strConvertCU(mapObj.getKey())+",";
                values += "?,";
                if (mapObj.getValue() instanceof String) {
                    param.add(mapObj.getValue());
                }else if (mapObj.getValue() instanceof Long){
                    param.add( new DateFormaterHelper().timestampToDB( new Timestamp((Long) mapObj.getValue()) ) );
                }else{
                    param.add(mapObj.getValue());
                }
            }

            field += ")";
            values += ")";
            field = field.replace(",)",")");
            values = values.replace(",)",")");

            sql = sql+field+values;
            log.debug("Query: "+sql);
            log.debug("Param: "+param);
            this.executeUpdate(sql,param);

        }catch (Exception e){
            log.error(e);
        }

        return object;
    }

    public ConnectionHelper select(String select){
        this.select = select;
        return this;
    }

    public ConnectionHelper table(String tableName){
        this.tableName = tableName;
        return this;
    }

    public ConnectionHelper where(String field, Object value){
        if (value instanceof String){
            this.where += field+"=? and ";
        }else{
            this.where += field+"=? and ";
        }
        param.add(value);
        return this;
    }

    public ConnectionHelper where(String field, String operator, Object value){
        if (value instanceof String){
            this.where += field+" "+operator+" ? and ";
        }else{
            this.where += field+" "+operator+" ? and ";
        }
        param.add(value);
        return this;
    }

    public ConnectionHelper whereDateBetween(String date, Integer dateIncrease){

        return this;
    }

    public ConnectionHelper findBy(Map<String, Object> findValue){
        StringHelper stringHelper = new StringHelper();
        param = new ArrayList<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> map = objectMapper.convertValue(findValue, Map.class);

            where += "(";
            for (Map.Entry<String, Object> mapObj : map.entrySet()){
                where += stringHelper.strConvertCU(mapObj.getKey()) +" like ? or ";
                param.add("%"+mapObj.getValue()+"%");
            }
            where += ")";

            where = where.replace("or )",") and ");
        }catch (Exception e){
            log.error(e);
        }

        return this;
    }

    public ResultSet first(){
        ResultSet resultSet = null;
        String sql = "";
        try{
            if (where.equals("where ")){
                sql = "select "+this.select+" from "+this.tableName+" limit 1";
            }else{
                sql = "select "+this.select+" from "+this.tableName+" "+this.where+"= limit 1";
//                sql = this.select+this.where+"= limit 1";
            }

            sql = sql.replace(" and =","");
            log.debug("Query: "+sql);
            log.debug("Param: "+param);

            resultSet = this.executeQuery(sql, param);
        }catch (Exception e){
            log.error(e);
        }

        return resultSet;
    }

    public ResultSet get(){
        ResultSet resultSet = null;
        String sql = "";
        try{
            if (where.equals("where ")){
//                sql = this.select;
                sql = "select "+this.select+" from "+this.tableName;
            }else{
//                sql = this.select+this.where+"=";
                sql = "select "+this.select+" from "+this.tableName+" "+this.where+"=";
            }

            sql = sql.replace(" and =","");
            sql = sql.replace("  and =","");
            log.debug("Query: "+sql);
            log.debug("Param: "+param);

            resultSet = this.executeQuery(sql, param);
        }catch (Exception e){
            log.error(e);
        }
        return resultSet;
    }

    public ResultSet paginate(String showPage){
        ResultSet resultSet = null;
        String sql = "";
        try {
            if (where.equals("where ")){
//                sql = this.select+" limit "+showPage+" offset 0";
                sql = "select "+this.select+" from "+ this.tableName+" limit "+showPage+" offset 0";
            }else{
//                sql = this.select+this.where+"="+" limit "+showPage+" offset 0";
                sql = "select "+this.select+" from "+ this.tableName+" "+this.where+"= limit "+showPage+" offset 0";
            }
            sql = sql.replace(" and =","");
            log.debug("Query: "+sql);
            log.debug("Param: "+param);

            resultSet = this.executeQuery(sql, param);
        }catch (Exception e){
            log.error(e);
        }

        return resultSet;
    }

    public ResultSet paginate(String showPage, String page){
        ResultSet resultSet = null;
        Integer offset = 0;
        String sql = "";
        try {
            if (page.equals("1")){
                offset = 0;
            }else{
                offset = (new Integer(showPage) * (new Integer(page) - 1) );
            }

            if (where.equals("where ")){
//                sql = this.select+" limit "+showPage+" offset "+offset;
                sql = "select "+this.select+" from "+ this.tableName+" limit "+showPage+" offset "+offset;
            }else{
//                sql = this.select+this.where+"="+" limit "+showPage+" offset "+offset;
                sql = "select "+this.select+" from "+ this.tableName+" "+this.where+"= limit "+showPage+" offset "+offset;
            }

            sql = sql.replace(" and =","");
            log.debug("Query: "+sql);
            log.debug("Param: "+param);

            resultSet = this.executeQuery(sql, param);
        }catch (Exception e){
            log.error(e);
        }

        return resultSet;
    }

    public Integer count(){
        ResultSet resultSet = null;
        String sql = "";
        Integer count = 0;
        try {
            if (where.equals("where ")){
                sql = "select count(*) as count from "+this.tableName+" ";
            }else{
                sql = "select count(*) as count from "+this.tableName+" "+this.where+"=";
            }

            sql = sql.replace(" and =","");
            log.debug("Query: "+sql);
            log.debug("Param: "+param);

            resultSet = this.executeQuery(sql, param);
            resultSet.next();
            count = new Integer(resultSet.getString("count"));
        }catch (Exception e){
            log.error(e);
        }

        return count;
    }
}
