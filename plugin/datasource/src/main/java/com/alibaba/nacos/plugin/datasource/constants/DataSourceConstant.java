/*
 * Copyright 1999-2022 Alibaba Group Holding Ltd.
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

package com.alibaba.nacos.plugin.datasource.constants;

/**
 * The data source name.
 *
 * @author hyx
 **/

public class DataSourceConstant {
    public static final String MYSQL = "mysql";

    public static final String MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";

    public static final String SQLSERVER = "sqlserver";

    public static final String SQLSERVER_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    public static final String POSTGRESQL = "postgresql";

    public static final String POSTGRESQL_DRIVER = "org.postgresql.Driver";

    public static final String DM = "dm";

    public static final String DM_DRIVER = "dm.jdbc.driver.DmDriver";

    public static final String ORACLE = "oracle";

    public static final String ORACLE_DRIVER = "oracle.jdbc.driver.OracleDriver";

    public static final String DERBY = "derby";

    public static final String DERBY_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";

}
