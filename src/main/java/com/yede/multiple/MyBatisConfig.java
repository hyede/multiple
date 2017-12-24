package com.yede.multiple;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.yede.multiple.utils.DynamicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * springboot集成mybatis的基本入口 1）创建数据源(如果采用的是默认的tomcat-jdbc数据源，则不需要)
 * 2）创建SqlSessionFactory 3）配置事务管理器，除非需要使用事务，否则不用配置
 */
@Configuration // 该注解类似于spring配置文件
@MapperScan(basePackages = "com.yede.multiple.mapper")
public class MyBatisConfig   {

    @Autowired
    private DBConfig dbConfig;
    private static final Logger logger = LoggerFactory.getLogger(MyBatisConfig.class);

    @Autowired
    private Environment env;



    @Bean
    @Primary
    public DataSource dataSource() throws SQLException, PropertyVetoException {
        DataSource dataSource;
        if(dbConfig.getDbType().equalsIgnoreCase(DBConfig.DB_MYSQL)) {
            logger.info("use mysql db");
            DataSource defaultDataSource = this.createDataSource(dbConfig.getSchema());
            DynamicDataSource multiTenantDataSource = new DynamicDataSource(defaultDataSource);
            multiTenantDataSource = this.initMultiTenants(multiTenantDataSource);
            multiTenantDataSource.afterPropertiesSet();
            logger.info("Total of data sources: " + multiTenantDataSource.getSizeOfDataSources());
            dataSource = multiTenantDataSource;
        } else {
            logger.info("use embedded h2 db");
            dataSource = new EmbeddedDatabaseBuilder()
                    .setType(EmbeddedDatabaseType.H2)
                    .build();
        }
        return dataSource;
    }

    /**
     * 根据数据源创建SqlSessionFactory
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory( DynamicDataSource ds) throws Exception {
        SqlSessionFactoryBean fb = new SqlSessionFactoryBean();
        fb.setDataSource(ds);// 指定数据源(这个必须有，否则报错)
        // 下边两句仅仅用于*.xml文件，如果整个持久层操作不需要使用到xml文件的话（只用注解就可以搞定），则不加
//        fb.setTypeAliasesPackage(env.getProperty("mybatis.typeAliasesPackage"));// 指定基包
        fb.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources(env.getProperty("mybatis.config-locations")));//

        fb.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources(env.getProperty("mybatis.mapperLocations")));//

        return fb.getObject();
    }

    /**
     * 配置事务管理器
     */
    @Bean
    public DataSourceTransactionManager transactionManager(DataSource dataSource) throws Exception {
        return new DataSourceTransactionManager(dataSource);
    }


    private String getUrl(String schema) {
        String url = dbConfig.getDb() + "/" + schema + "?useUnicode=" + dbConfig.isUseUnicode() + "&characterEncoding=" + dbConfig.getEncoding();
        return url;
    }

    private DynamicDataSource initMultiTenants(DynamicDataSource dataSource) throws SQLException, PropertyVetoException {
        String sql = "select ORGANIZATION_CODE, ORGANIZATION_SCHEMA from ORGANIZATION";
        Connection connection = dataSource.getDefaultDataSource().getConnection();
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            String organizationCode = rs.getString(1);
            String schema = rs.getString(2);
            DataSource ds = this.createDataSource(schema);
            dataSource.addDataSource(organizationCode, ds);
        }
        ps.close();
        connection.close();
        return dataSource;
    }


    private DataSource createDataSource(String schema) throws PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setJdbcUrl(this.getUrl(schema));
        dataSource.setUser(dbConfig.getUsername());
        dataSource.setPassword(dbConfig.getPassword());
        dataSource.setDriverClass(dbConfig.getDriverClassName());
        dataSource.setInitialPoolSize(dbConfig.getInitialPoolSize());
        dataSource.setMinPoolSize(dbConfig.getMinPoolSize());
        dataSource.setMaxPoolSize(dbConfig.getMaxPoolSize());
        dataSource.setMaxIdleTime(dbConfig.getMaxIdleTime());
        dataSource.setAcquireIncrement(dbConfig.getAcquireIncrement());
        return dataSource;
    }

}