package com.pageOfficeServer.loginkingdee;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.cache.*;
import com.pageOfficeServer.util.httpUtil.BaseResult;
import com.pageOfficeServer.util.httpUtil.CodeConstant;
import com.pageOfficeServer.util.httpUtil.SystemServiceConstant;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class CacheMap {

    /**
     * @author: xn-huxin
     * @Date: 2019/7/18 10:00
     * @Description:
     */
    private static LoadingCache<String, Object> cache = CacheBuilder.newBuilder().maximumSize(10000)
            .expireAfterWrite(1, TimeUnit.HOURS)
            .initialCapacity(10).build(new CacheLoader<String, Object>() {
                @Override
                public String load(String key) {
                    String tenantId = SystemServiceConstant.SRV_CON_TENANT_ID;
                    String account = SystemServiceConstant.SRV_CON_YOUKE_NAME;
                    String password = SystemServiceConstant.SRV_CON_YOUKE_PASSWORD;
                    System.out.println("tenantId => " + tenantId + ", account => " + account + ", password => " + password);
                    // 登录认证
                    BaseResult<Object> result = AuthUtil.auth(tenantId, account, password);
                    if (CodeConstant.SUCCESS_CODE == result.getCode()) {
                        JSONObject resultData = JSONObject.parseObject(JSONObject.toJSONString(result.getData(),
                                SerializerFeature.DisableCircularReferenceDetect));
                        String accessToken = resultData.getString("access_token");
//                        log.info("通用access_token => " + resultData.getString("access_token"));
                        System.out.println("通用access_token => " + resultData.getString("access_token"));
                        return accessToken;
                    } else {
                        System.out.println("通用access_token获取失败，请检查配置文件的账号和密码是否正确。");

                    }
                    return null;
                }
            });


    /**
     * @author: xn-huxin
     * @Date: 2019/7/18 10:00
     * @Description:
     */
    public static Object get(String key) {
        return StringUtils.isNotEmpty(key) ? cache.getIfPresent(key) : null;
    }
    /**
     * @author: xn-huxin
     * @Date: 2019/7/18 10:00
     * @Description:
     */
    public static Object getUncheck(String key) {
        return StringUtils.isNotEmpty(key) ? cache.getUnchecked(key) : null;
    }

    /**
     * @author: xn-huxin
     * @Date: 2019/7/18 10:00
     * @Description:
     */
    public static void put(String key, Object value) {
        if (StringUtils.isNotEmpty(key) && value != null) {
            cache.put(key, value);
        }
    }

    /**
     * @author: xn-huxin
     * @Date: 2019/7/18 10:00
     * @Description:
     */
    public static void remove(String key) {
        if (StringUtils.isNotEmpty(key)) {
            cache.invalidate(key);
        }
    }

    /**
     * @author: xn-huxin
     * @Date: 2019/7/18 10:00
     * @Description:
     */
    public static void remove(List<String> keys) {
        if (keys != null && keys.size() > 0) {
            cache.invalidateAll(keys);
        }
    }
}
