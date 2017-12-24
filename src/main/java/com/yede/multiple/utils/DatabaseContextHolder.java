package com.yede.multiple.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseContextHolder {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseContextHolder.class);
    public static final String DEFAULT_DATA_SOURCE_KEY = "hzwitwin";

    private static final ThreadLocal<String> dataSourceKey = new ThreadLocal();

    public static void setDataSourceKey(String key) {
        logger.debug("set data source key as: " + key);
        dataSourceKey.set(key);
    }

    public static String getDataSourceKey() {
        return dataSourceKey.get();
    }

    public static void clearDataSourceKey() {
        dataSourceKey.remove();
        logger.debug("clear the data source key");
    }
}
