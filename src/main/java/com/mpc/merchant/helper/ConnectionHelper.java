package com.mpc.merchant.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.List;
import java.util.Map;

@Component
public class ConnectionHelper {
    private Connection connection = null;
    private PropertiesHelper applicationProperties = new PropertiesHelper("application.properties");

    private Logger log = LogManager.getLogger(getClass());

    private String select = "";
    private String tableName = "";
    private String where = "where ";

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
            e.printStackTrace();
        }

        return connection;
    }

    public ResultSet executeQuery(String sql){
        ResultSet resultSet = null;
        try {
            Statement statement = (Statement) connection.createStatement();
            resultSet = statement.executeQuery(sql);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultSet;
    }

    public ResultSet executeQuery(String sql, List<String> params) {
        ResultSet resultSet = null;
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            Integer i = 1;
            for (String param : params) {
                statement.setObject(i, param);
                i++;
            }
            statement.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultSet;
    }

    public Integer executeUpdate(String sql){
        Integer status = null;
        try {
            Statement statement = (Statement) connection.createStatement();
            status = statement.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    public Object save(Object object){
        try{
            StringHelper stringHelper = new StringHelper();
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> map = objectMapper.convertValue(object, Map.class);
            String sql = "insert into "+ stringHelper.StrConvertCU(object.getClass().getSimpleName());
            String field = "(";
            String values = " values(";

            for (Map.Entry<String, Object> mapObj : map.entrySet()){
                field += stringHelper.StrConvertCU(mapObj.getKey())+",";

                if (mapObj.getValue() instanceof String) {
                    values += "'" + mapObj.getValue() + "',";
                }else if (mapObj.getValue() instanceof Long){
                    values += "'"+ new DateFormaterHelper().timestampToDB( new Timestamp((Long) mapObj.getValue()) ) +"',";
                }else{
                    values += mapObj.getValue()+",";
                }
            }

            field += ")";
            values += ")";
            field = field.replace(",)",")");
            values = values.replace(",)",")");

            sql = sql+field+values;
            log.debug("Query: "+sql);
            this.executeUpdate(sql);

        }catch (Exception e){
            e.printStackTrace();
        }

        return object;
    }

    public ConnectionHelper select(String select){
        this.select = "select * from "+select+" ";
        this.tableName = select;
        return this;
    }

    public ConnectionHelper where(String field, String value){
        this.where += field+"='"+value+"' and ";
        return this;
    }

    public ResultSet first(){
        ResultSet resultSet = null;
        String sql = "";
        try{
            if (where.equals("where ")){
                sql = this.select+" limit 1";
            }else{
                sql = this.select+this.where+"= limit 1";
            }

            sql = sql.replace(" and =","");
            log.debug("Query: "+sql);

            resultSet = this.executeQuery(sql);
        }catch (Exception e){
            e.printStackTrace();
        }

        return resultSet;
    }

    public ResultSet get(){
        ResultSet resultSet = null;
        String sql = "";
        try{
            if (where.equals("where ")){
                sql = this.select;
            }else{
                sql = this.select+this.where+"=";
            }

            sql = sql.replace(" and =","");
            log.debug("Query: "+sql);

            resultSet = this.executeQuery(sql);
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultSet;
    }

    public ResultSet paginate(String showPage){
        ResultSet resultSet = null;
        String sql = "";
        try {
            if (where.equals("where ")){
                sql = this.select+" limit "+showPage+" offset 0";
            }else{
                sql = this.select+this.where+"="+" limit "+showPage+" offset 0";
            }
            sql = sql.replace(" and =","");
            log.debug("Query: "+sql);

            resultSet = this.executeQuery(sql);
        }catch (Exception e){
            e.printStackTrace();
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
                offset = (new Integer(showPage) * new Integer(page)) - 1;
            }

            if (where.equals("where ")){
                sql = this.select+" limit "+showPage+" offset "+offset;
            }else{
                sql = this.select+this.where+"="+" limit "+showPage+" offset "+offset;
            }

            sql = sql.replace(" and =","");
            log.debug("Query: "+sql);

            resultSet = this.executeQuery(sql);
        }catch (Exception e){
            e.printStackTrace();
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
                sql = "select count(*) as count from "+this.tableName+" "+this.where;
            }

            sql = sql.replace(" and =","");
            log.debug("Query: "+sql);

            resultSet = this.executeQuery(sql);
            resultSet.next();
            count = new Integer(resultSet.getString("count"));
        }catch (Exception e){
            e.printStackTrace();
        }

        return count;
    }
}
