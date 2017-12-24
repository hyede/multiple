package com.yede.multiple.utils;


import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 动态数据源（需要继承AbstractRoutingDataSource）
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    private Map<Object, Object> dataSources = new HashMap<>();
    private DataSource defaultDataSource;

    public DynamicDataSource(DataSource defaultDataSource) {
        super();
        this.defaultDataSource = defaultDataSource;
        this.dataSources.put(DatabaseContextHolder.DEFAULT_DATA_SOURCE_KEY, defaultDataSource);
        this.setTargetDataSources(this.dataSources);
        this.setDefaultTargetDataSource(defaultDataSource);
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return DatabaseContextHolder.getDataSourceKey();
    }

    public void addDataSource(String key, DataSource dataSource) {
        this.dataSources.put(key, dataSource);
        this.setTargetDataSources(this.dataSources);
    }

    public void removeDataSource(String key) {
        this.dataSources.remove(key);
        this.setTargetDataSources(this.dataSources);
    }

    public int getSizeOfDataSources() {
        return this.dataSources.size();
    }

    public Map<Object, Object> getDataSources() {
        return dataSources;
    }

    public DataSource getDefaultDataSource() {
        return defaultDataSource;
    }

}