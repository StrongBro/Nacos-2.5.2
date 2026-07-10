/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
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

package com.alibaba.nacos.plugin.datasource.mapper.ext;

/**
 * Page limitation Builder.
 *
 * @author BugMaker
 * @date 2026/07/15
 */
public final class PageLimitBuilder {
    public static String offsetSql(int startRow, int pageSize) {
        return " OFFSET " + startRow + " ROWS FETCH NEXT " + pageSize + " ROWS ONLY";
    }

    public static String limitSql(int startRow, int pageSize) {
        return " LIMIT " + pageSize + " OFFSET " + startRow;
    }
}
