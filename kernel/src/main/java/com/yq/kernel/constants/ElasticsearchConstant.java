package com.yq.kernel.constants;

import java.util.Collections;
import java.util.Map;

/**
 * <p> es 相关常量</p>
 * @author youq  2019/1/7 9:37
 */
public class ElasticsearchConstant {

    /**
     * 一次查询最大限制数
     */
    public static final Integer QUERY_MAX_LIMIT = 10000;

    /**
     * user index
     */
    public static final String USER_INDEX = "user_index";

    /**
     * user type
     */
    public static final String USER_TYPE = "user_type";

    //region  the http method
    public static final String METHOD_PUT = "PUT";

    public static final String METHOD_DELETE = "DELETE";

    public static final String METHOD_GET = "GET";

    public static final String METHOD_POST = "POST";

    public static final String METHOD_HEAD = "HEAD";
    //endregion

    public static final Map<String, String> RESULT_JSON_FORMAT = Collections.singletonMap("pretty", "true");

}
