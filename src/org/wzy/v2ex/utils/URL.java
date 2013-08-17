package org.wzy.v2ex.utils;

/**
 * Created with IntelliJ IDEA.
 * User: zeyiwu
 * Date: 13-8-8
 * Time: 下午2:07
 */
public class URL {
    public static final String API_HOST_URL = "http://www.v2ex.com";
    public static final String LATEST_URL = API_HOST_URL + "/api/topics/latest.json";
    public static final String NODE_URL = API_HOST_URL + "/api/topics/show.json?node_name=%s";
    public static final String TOPIC_URL = API_HOST_URL + "/api/topics/show.json?id=%d";
    public static final String REPLY_URL = API_HOST_URL + "/api/replies/show.json?topic_id=%d";
}
