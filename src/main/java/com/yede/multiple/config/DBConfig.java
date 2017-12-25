package com.yede.multiple.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DBConfig {

    public static final String DB_MYSQL = "MYSQL";
    public static final String DB_H2 = "H2";

    @Value(value = "${witwin.datasource.db:''}")
    private String db = "";
    @Value(value = "${witwin.datasource.useUnicode:true}")
    private boolean useUnicode = true;
    @Value(value = "${witwin.datasource.characterEncoding:'UTF-8'}")
    private String encoding = "";
    @Value(value = "${witwin.datasource.schema:'hzwitwin'}")
    private String schema = "";
    @Value(value = "${witwin.datasource.username:''}")
    private String username = "";
    @Value(value = "${witwin.datasource.password:''}")
    private String password = "";
    @Value(value = "${witwin.datasource.driver-class-name:'com.mysql.jdbc.Driver'}")
    private String driverClassName = "";
    @Value(value = "${witwin.datasource.db.type:'H2'}")
    private String dbType = "H2";

    @Value(value = "${c3p0.minPoolSize:5}")
    private int minPoolSize = 5;
    @Value(value = "${c3p0.maxPoolSize:30}")
    private int maxPoolSize = 30;
    @Value(value = "${c3p0.initialPoolSize:10}")
    private int initialPoolSize = 10;
    @Value(value = "${c3p0.maxIdleTime:60}")
    private int maxIdleTime = 60;
    @Value(value = "${c3p0.acquireIncrement:5}")
    private int acquireIncrement = 5;

    public String getDb() {
        return db;
    }

    public boolean isUseUnicode() {
        return useUnicode;
    }

    public String getEncoding() {
        return encoding;
    }

    public String getSchema() {
        return schema;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public String getDbType() {
        return dbType;
    }

    public int getMinPoolSize() {
        return minPoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public int getInitialPoolSize() {
        return initialPoolSize;
    }

    public int getMaxIdleTime() {
        return maxIdleTime;
    }

    public int getAcquireIncrement() {
        return acquireIncrement;
    }
}