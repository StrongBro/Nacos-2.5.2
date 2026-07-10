/*
 * Copyright 1999-2023 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.nacos.persistence.datasource;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.alibaba.nacos.common.utils.Preconditions;
import com.alibaba.nacos.common.utils.StringUtils;
import com.alibaba.nacos.persistence.utils.DatasourcePlatformUtil;
import com.alibaba.nacos.plugin.datasource.constants.DataSourceConstant;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.alibaba.nacos.common.utils.CollectionUtils.getOrDefault;

/**
 * Properties of external DataSource.
 *
 * @author Nacos
 */
public class ExternalDataSourceProperties {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ExternalDataSourceProperties.class);

    private static final String JDBC_DRIVER_NAME = "com.mysql.cj.jdbc.Driver";

    private static final String TEST_QUERY_DEFAULT = "SELECT 1";

    private static final String TEST_QUERY_ORACLE = "SELECT * FROM DUAL";

    private Integer num;

    private String driverClassName;

    public void setDriverClassName(String driverClassName) {
        driverClassName = StringUtils.isEmpty(driverClassName) ? JDBC_DRIVER_NAME : driverClassName;
        this.driverClassName = driverClassName;
    }

    public String getDriverClassName() {
        this.driverClassName = StringUtils.isEmpty(this.driverClassName) ? JDBC_DRIVER_NAME : this.driverClassName;
        return driverClassName;
    }

    private List<String> url = new ArrayList<>();
    
    private List<String> user = new ArrayList<>();
    
    private List<String> password = new ArrayList<>();
    
    public void setNum(Integer num) {
        this.num = num;
    }
    
    public void setUrl(List<String> url) {
        this.url = url;
    }
    
    public void setUser(List<String> user) {
        this.user = user;
    }
    
    public void setPassword(List<String> password) {
        this.password = password;
    }
    
    /**
     * Build serveral HikariDataSource.
     *
     * @param environment {@link Environment}
     * @param callback    Callback function when constructing data source
     * @return List of {@link HikariDataSource}
     */
    List<HikariDataSource> build(Environment environment, Callback<HikariDataSource> callback) {
        List<HikariDataSource> dataSources = new ArrayList<>();
        Binder.get(environment).bind("db", Bindable.ofInstance(this));
        Preconditions.checkArgument(Objects.nonNull(num), "db.num is null");
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(user), "db.user or db.user.[index] is null");
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(password), "db.password or db.password.[index] is null");
        for (int index = 0; index < num; index++) {
            int currentSize = index + 1;
            Preconditions.checkArgument(url.size() >= currentSize, "db.url.%s is null", index);
            DataSourcePoolProperties poolProperties = DataSourcePoolProperties.build(environment);
            if (StringUtils.isEmpty(poolProperties.getDataSource().getDriverClassName())) {
                poolProperties.setDriverClassName(this.getDriverClassName());
            }
            LOGGER.info("Build datasource, index: {}, url: {}, driver: {}",
                    index, url.get(index), poolProperties.getDataSource().getDriverClassName());
            poolProperties.setJdbcUrl(url.get(index).trim());
            poolProperties.setUsername(getOrDefault(user, index, user.get(0)).trim());
            poolProperties.setPassword(getOrDefault(password, index, password.get(0)).trim());
            HikariDataSource ds = poolProperties.getDataSource();
            if (StringUtils.isEmpty(ds.getConnectionTestQuery())) {
                //base on the platform(spring.sql.init.platform=?) to set the connection test query
                String platform = DatasourcePlatformUtil.getDatasourcePlatform(DataSourceConstant.MYSQL).toLowerCase();
                if (DataSourceConstant.ORACLE.equals(platform) || DataSourceConstant.DM.equals(platform)) {
                    ds.setConnectionTestQuery(TEST_QUERY_ORACLE);
                } else {
                    ds.setConnectionTestQuery(TEST_QUERY_DEFAULT);
                }
            }
            
            dataSources.add(ds);
            callback.accept(ds);
        }
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(dataSources), "no datasource available");
        return dataSources;
    }
    
    interface Callback<D> {
        
        /**
         * Perform custom logic.
         *
         * @param datasource dataSource.
         */
        void accept(D datasource);
    }
}
