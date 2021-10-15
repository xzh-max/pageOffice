package com.pageOfficeServer.util.httpUtil;

public class SystemServiceConstant {

    //业务中台访问协议
    public static final String SRV_CON_PROTOCOL = "http";

    //业务e签宝中台前缀
    public static final String SRV_ESIGN_SIGN_CON_PREFIX = "/ierp/kapi/app/wens_electronic_sign/";
    //业务e签宝签署回调中台访问后缀
    public static final String SRV_ESIGN_SIGN_CON_SUFFIX = "esign/invoke";

    //业务e签宝个人认证回调中台访问后缀
    public static final String SRV_ESIGN_PERSONAL_CON_SUFFIX = "esignPersonNotify/invoke";

    //业务e签宝企业回调中台访问后缀
    public static final String SRV_ESIGN_COMPANY_CON_SUFFIX = "esignCompanyNotify/invoke";

    //业务e签宝企业获取养户签署信息中台访问后缀
    public static final String SRV_ESIGN_REARER_INFO_SUFFIX = "esignRearerByRearerId/invoke";

    //业务e签宝个人认证中台访问后缀
    public static final String SRV_ESIGN_CER_PERSON_CON_SUFFIX = "esignPersonCertification/invoke";

    //业务e签宝企业认证中台访问后缀
    public static final String SRV_ESIGN_CER_COMPANY_CON_SUFFIX = "esignCompCertification/invoke";

    //业务中台地址
    public static final String SRV_CON_HOST = "10.11.40.23";

    //业务中台端口
    public static final String SRV_CON_PORT = "80";

    //登录接口前缀
    public static final String SRV_CON_LOGIN_URL_SUFFIX = "/ierp/api/";

    //数据中心id
    public static final String SRV_CON_ACCOUNT_ID="1546506236161526280";

    //租户编码id
    public static final String SRV_CON_TENANT_ID="wens";

    //游客账号
    public static final String SRV_CON_YOUKE_NAME="17673132565";

    //游客密码
    public static final String SRV_CON_YOUKE_PASSWORD="youke1234";

    //游客token的rediskey
    public static final String COMMON_ACCESS_TOKEN_KEY="con_token_redis_key";
}
