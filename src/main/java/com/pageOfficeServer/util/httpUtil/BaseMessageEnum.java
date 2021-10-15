package com.pageOfficeServer.util.httpUtil;

/**
 * 基础国际化消息枚举类
 * 
 * @author wangcheng
 *
 */
public enum BaseMessageEnum {

	/**
	 * b2b-base模块
	 */
	BASE_QUERY_AD_SUCCESS("base_query_ad_success"), // 查询轮播图成功
	BASE_QUERY_AD_ERROR("base_query_ad_error"), // 查询轮播图失败
	BASE_QUERY_PROVINCE_SUCCESS("base_query_province_success"), // 查询渠道省份区域成功
	BASE_QUERY_PROVINCE_ERROR("base_query_province_error"), // 查询渠道省份区域失败
	BASE_QUERY_ADDRESS_SUCCESS("base_query_address_success"), // 查询地址地区成功
	BASE_QUERY_ADDRESS_ERROR("base_query_address_error"), // 查询地址地区失败
	BASE_QUERY_ADDRESS_PLUGALL_SUCCESS("base_query_address_plugall_success"), // 查询地址插件数据源成功
	BASE_QUERY_ADDRESS_PLUGALL_ERROR("base_query_address_plugall_error"), // 查询地址插件数据源失败
	BASE_QUERY_ASSISTANTDATA_SUCCESS("base_query_assistantdata_success"), // 辅助资料查询成功
	BASE_QUERY_ASSISTANTDATA_ERROR("base_query_assistantdata_error"), // 辅助资料查询失败
	BASE_SEND_VERIFY_CODE_SUCCESS("base_send_verify_code_success"), // 发送验证码成功
	BASE_SEND_VERIFY_CODE_ERROR("base_send_verify_code_error"), // 发送验证码失败
	BASE_BINDING_PHONE_SUCCESS("base_binding_phone_success"), // 绑定手机成功
	BASE_BINDING_PHONE_ERROR("base_binding_phone_error"), // 绑定手机失败
	BASE_VERIFY_CODE_SUCCESS("base_verify_code_success"), // 验证成功
	BASE_VERIFY_CODE_ERROR("base_verify_code_error"), // 验证失败
	BASE_QUERY_BILL_LIST_SUCCESS("base_query_bill_list_success"), // 查询收款记录列表成功
	BASE_QUERY_BILL_LIST_ERROR("base_query_bill_list_error"), // 查询收款记录列表失败
	BASE_QUERY_BILL_DETAIL_SUCCESS("base_query_bill_detail_success"), // 查询收款记录详情成功
	BASE_QUERY_BILL_DETAIL_ERROR("base_query_bill_detail_error"), // 查询收款记录详情失败
	BASE_QUERY_BALANCE_SUCCESS("base_query_balance_success"), // 查询预收款余额成功
	BASE_QUERY_BALANCE_ERROR("base_query_balance_error"), // 查询预收款余额失败
	BASE_QUERY_FEEDBACK_LIST_SUCCESS("base_query_feedback_list_success"), // 查询反馈意见列表成功
	BASE_QUERY_FEEDBACK_LIST_ERROR("base_query_feedback_list_error"), // 查询反馈意见列表失败
	BASE_QUERY_FEEDBACK_DETAIL_SUCCESS("base_query_feedback_detail_success"), // 查询反馈意见详情成功
	BASE_QUERY_FEEDBACK_DETAIL_ERROR("base_query_feedback_detail_error"), // 查询反馈意见详情失败
	BASE_QUERY_MONEY_DIARY_SUCCESS("base_query_money_diary_success"), // 查询余额日志流水成功
	BASE_QUERY_MONEY_DIARY_ERROR("base_query_money_diary_error"), // 查询余额日志流水失败
	BASE_BANK_LIST_SUCCESS("base_bank_list_success"), // 查询银行卡列表成功
	BASE_BANK_LIST_ERROR("base_bank_list_error"), // 查询银行卡列表失败
	BASE_DEPOSIT_SUCCESS("base_deposit_success"), // 充值成功
	BASE_DEPOSIT_ERROR("base_deposit_error"), // 充值失败

	/**
	 * b2b-cart模块
	 */
	CART_QUERY_CART_SUCCESS("cart_query_cart_success"), // 查询购物车成功
	CART_QUERY_CART_ERROR("cart_query_cart_error"), // 查询购物车失败
	CART_SAVE_GOODS_SUCCESS("cart_save_goods_success"), // 加入购物车成功
	CART_SAVE_GOODS_ERROR("cart_save_goods_error"), // 加入购物车失败
	CART_UPDATE_CART_SUCCESS("cart_update_cart_success"), // 操作成功
	CART_UPDATE_CART_ERROR("cart_update_cart_error"), // 操作失败
	CART_DELETE_CART_SUCCESS("cart_delete_cart_success"), // 删除成功
	CART_DELETE_CART_ERROR("cart_delete_cart_error"), // 删除失败

	/**
	 * b2b-cms模块
	 */
	CMS_QUERY_ARTICLE_LIST_SUCCESS("cms_query_article_list_success"), // 查询文章列表成功
	CMS_QUERY_ARTICLE_LIST_ERROR("cms_query_article_list_error"), // 查询文章列表失败
	CMS_QUERY_ARTICLE_DETAIL_SUCCESS("cms_query_article_detail_success"), // 查询文章详情成功
	CMS_QUERY_ARTICLE_DETAIL_ERROR("cms_query_article_detail_error"), // 查询文章详情失败
	CMS_QUERY_ARTICLE_CATALOG_SUCCESS("cms_query_article_catalog_success"), // 查询文章目录成功
	CMS_QUERY_ARTICLE_CATALOG_ERROR("cms_query_article_catalog_error"), // 查询文章目录失败

	/**
	 * b2b-comment模块
	 */
	COMMENT_ORDER_COMMENT_ITEM_QUERY_SUCCESS("comment_order_comment_item_query_success"), // 查询订单评价项成功
	COMMENT_ORDER_COMMENT_ITEM_QUERY_ERROR("comment_order_comment_item_query_error"), // 查询订单评价项失败
	COMMENT_ORDER_COMMENT_QUERY_SUCCESS("comment_order_comment_query_success"), // 查询订单评价成功
	COMMENT_ORDER_COMMENT_QUERY_ERROR("comment_order_comment_query_error"), // 查询订单评价失败
	COMMENT_ORDER_COMMENT_ADD_SUCCESS("comment_order_comment_add_success"), // 新增订单评价成功
	COMMENT_ORDER_COMMENT_ADD_ERROR("comment_order_comment_add_error"), // 新增订单评价失败
	COMMENT_QUERY_ITEM_EVALUATE_SUCCESS("comment_query_item_evaluate_success"), // 查询商品评论成功
	COMMENT_QUERY_ITEM_EVALUATE_ERROR("comment_query_item_evaluate_error"), // 查询商品评论失败
	COMMENT_QUERY_ITEM_EVALUATE_TAG_SUCCESS("comment_query_item_evaluate_tag_success"), // 查询商品评论项成功
	COMMENT_QUERY_ITEM_EVALUATE_TAG_ERROR("comment_query_item_evaluate_tag_error"), // 查询商品评论项失败
	COMMENT_SAVE_ITEM_EVALUATE_SUCCESS("comment_save_item_evaluate_success"), // 新增商品评价成功
	COMMENT_SAVE_ITEM_EVALUATE_ERROR("comment_save_item_evaluate_error"), // 新增商品评价失败
	COMMENT_QUERY_CMP_RESULT_SUCCESS("comment_query_cmp_result_success"), // 查询商品评价综合评价信息成功
	COMMENT_QUERY_CMP_RESULT_ERROR("comment_query_cmp_result_error"), // 查询商品评价综合评价信息失败
	COMMENT_QUERY_EVALUATE_LABEL_SUCCESS("comment_query_evaluate_label_success"), // 查询商品评论标签成功
	COMMENT_QUERY_EVALUATE_LABEL_ERROR("comment_query_evaluate_label_error"), // 查询商品评论标签失败

	/**
	 * b2b-file模块
	 */
	FILE_UPLOAD_IMAGE_SUCCESS("file_upload_image_success"), // 上传图片成功
	FILE_UPLOAD_IMAGE_ERROR("file_upload_image_error"), // 上传图片失败
	FILE_DOWNLOAD_IMAGE_ERROR("file_download_image_error"), // 下载图片失败
	FILE_GET_IMAGE_FULL_URL_SUCCESS("file_get_image_full_url_success"), // 获取图片路径成功
	FILE_GET_IMAGE_FULL_URL_ERROR("file_get_image_full_url_error"), // 获取图片路径失败

	/**
	 * b2b-goods模块
	 */
	GOODS_QUERY_CUSTOMER_SUCCESS("goods_query_customer_success"), // 渠道部分信息查询成功
	GOODS_QUERY_CUSTOMER_ERROR("goods_query_customer_error"), // 渠道部分信息查询失败
	GOODS_QUERY_GOODS_CATEGORY_SUCCESS("goods_query_goods_category_success"), // 查询商品分类成功
	GOODS_QUERY_GOODS_CATEGORY_ERROR("goods_query_goods_category_error"), // 查询商品分类失败
	GOODS_QUERY_GOODS_DETAIL_SUCCESS("goods_query_goods_detail_success"), // 查询商品详情成功
	GOODS_QUERY_GOODS_DETAIL_ERROR("goods_query_goods_detail_error"), // 查询商品详情失败
	GOODS_GOODS_COLLECT_QUERY_SUCCESS("goods_goods_collect_query_success"), // 查询收藏商品成功
	GOODS_GOODS_COLLECT_QUERY_ERROR("goods_goods_collect_query_error"), // 查询收藏商品失败
	GOODS_GOODS_COLLECT_QTY_SUCCESS("goods_goods_collect_qty_success"), // 查询收藏商品数量成功
	GOODS_GOODS_COLLECT_QTY_ERROR("goods_goods_collect_qty_error"), // 查询收藏商品数量失败
	GOODS_GOODS_COLLECT_ADD_SUCCESS("goods_goods_collect_add_success"), // 商品收藏成功
	GOODS_GOODS_COLLECT_ADD_ERROR("goods_goods_collect_add_error"), // 商品收藏失败
	GOODS_GOODS_COLLECT_DEL_SUCCESS("goods_goods_collect_del_success"), // 取消收藏商品成功
	GOODS_GOODS_COLLECT_DEL_ERROR("goods_goods_collect_del_error"), // 取消收藏商品失败
	GOODS_GOODS_COLLECT_STATE_SUCCESS("goods_goods_collect_state_success"), // 查询商品是否已收藏成功
	GOODS_GOODS_COLLECT_STATE_ERROR("goods_goods_collect_state_error"), // 查询商品是否已收藏失败
	GOODS_GOODS_PRICE_QUERY_SUCCESS("goods_goods_price_query_success"), // 查询商品价格成功
	GOODS_GOODS_PRICE_QUERY_ERROR("goods_goods_price_query_error"), // 查询商品价格失败
	GOODS_GOODS_STORE_QUERY_SUCCESS("goods_goods_store_query_success"), // 商品库存查询成功
	GOODS_GOODS_STORE_QUERY_ERROR("goods_goods_store_query_error"), // 商品库存查询失败
	GOODS_GOODS_PRICE_STORE_QUERY_SUCCESS("goods_goods_price_store_query_success"), // 商品价格库存查询成功
	GOODS_GOODS_PRICE_STORE_QUERY_ERROR("goods_goods_price_store_query_error"), // 商品价格库存查询失败
	GOODS_GOODS_RESTRICT_QUERY_SUCCESS("goods_goods_restrict_query_success"), // 受限商品查询成功
	GOODS_GOODS_RESTRICT_QUERY_ERROR("goods_goods_restrict_query_error"), // 受限商品查询失败
	GOODS_QUERY_GOODS_LIST_SUCCESS("goods_query_goods_list_success"), // 查询商品列表成功
	GOODS_QUERY_GOODS_LIST_ERROR("goods_query_goods_list_error"), // 查询商品列表失败
	GOODS_GOODS_WEIGHT_RANGE_QUERY_SUCCESS("goods_goods_weight_range_query_success"), // 商品重量范围查询成功
	GOODS_GOODS_WEIGHT_RANGE_QUERY_ERROR("goods_goods_weight_range_query_error"), // 商品重量范围查询失败
	GOODS_GOODS_BUY_QUANTITY_LIST_QUERY_SUCCESS("goods_goods_buy_quantity_list_query_success"), // 商品最大最少订货批量查询成功
	GOODS_GOODS_BUY_QUANTITY_LIST_QUERY_ERROR("goods_goods_buy_quantity_list_query_error"), // 商品最大最少订货批量查询失败
	GOODS_GOODS_WEIGHT_RANGE_LIST_QUERY_SUCCESS("goods_goods_weight_range_list_query_success"), // 商品重量范围批量查询成功
	GOODS_GOODS_WEIGHT_RANGE_LIST_QUERY_ERROR("goods_goods_weight_range_list_query_error"), // 商品重量范围批量查询失败
	/**
	 * b2b-log模块
	 */

	/**
	 * b2b-login模块
	 */
	LOGIN_LOGIN_USER_SUCCESS("login_login_user_success"), // 登录认证成功
	LOGIN_LOGIN_USER_ERROR("login_login_user_error"), // 登录认证失败
	LOGIN_LOGIN_OUT_SUCCESS("login_login_out_success"), // 退出登录成功
	LOGIN_LOGIN_OUT_ERROR("login_login_out_error"), // 退出登录失败
	LOGIN_PUBLIC_KEYS_SUCCESS("login_public_keys_success"), // 获取公钥成功
	LOGIN_PUBLIC_KEYS_ERROR("login_public_keys_error"), // 获取公钥失败
	LOGIN_PASSWORD_CHANGE_SUCCESS("login_password_change_success"), // 密码修改成功
	LOGIN_PASSWORD_CHANGE_ERROR("login_password_change_error"), // 密码修改失败

	/**
	 * b2b-marketing模块
	 */
	PROMOTION_PROMOTION_POLICY_QUERY_SUCCESS("promotion_promotion_policy_query_success"), // 根据商品查询促销政策列表成功
	PROMOTION_PROMOTION_POLICY_QUERY_ERROR("promotion_promotion_policy_query_error"), // 根据商品查询促销政策列表失败
	PROMOTION_PROMOTION_EXECUTE_SUCCESS("promotion_promotion_execute_success"), // 匹配并执行促销成功
	PROMOTION_PROMOTION_EXECUTE_ERROR("promotion_promotion_execute_error"), // 匹配并执行促销失败
	
	/**
	 * b2b-notice模块
	 */
	NOTICE_QUERY_NOTICE_LIST_SUCCESS("notice_query_notice_list_success"), // 查询通知列表成功
	NOTICE_QUERY_NOTICE_LIST_ERROR("notice_query_notice_list_error"), // 查询通知列表失败
	NOTICE_QUERY_NOTICE_DETAIL_SUCCESS("notice_query_notice_detail_success"), // 查询通知详情成功
	NOTICE_QUERY_NOTICE_DETAIL_ERROR("notice_query_notice_detail_error"), // 查询通知详情失败
	NOTICE_QUERY_NOTICE_COUNT_SUCCESS("notice_query_notice_count_success"), // 查询通知数量成功
	NOTICE_QUERY_NOTICE_COUNT_ERROR("notice_query_notice_count_error"), // 查询通知数量失败
	NOTICE_UPDATE_NOTICE_READ_SUCCESS("notice_update_notice_read_success"), // 修改通知状态成功
	NOTICE_UPDATE_NOTICE_READ_ERROR("notice_update_notice_read_error"), //修改通知状态失败

	/**
	 * b2b-order模块
	 */
	ORDER_QUERY_NEEDTIME_SUCCESS("order_query_needtime_success"), // 查询要货时间成功
	ORDER_QUERY_NEEDTIME_ERROR("order_query_needtime_error"), // 查询要货时间失败
	ORDER_QUERY_DISTRIBUT_SUCCESS("order_query_distribut_success"), // 查询配送方式成功
	ORDER_QUERY_DISTRIBUT_ERROR("order_query_distribut_error"), // 查询配送方式失败
	ORDER_PUR_ORDER_DELETE_SUCCESS("order_pur_order_delete_success"), // 删除订单成功
	ORDER_PUR_ORDER_DELETE_ERROR("order_pur_order_delete_error"), // 删除订单失败
	ORDER_ORDER_CANCEL_SUCCESS("order_order_cancel_success"), // 取消订单成功
	ORDER_ORDER_CANCEL_ERROR("order_order_cancel_error"), // 取消订单失败
	ORDER_PUR_ORDER_SAVE_SUCCESS("order_pur_order_save_success"), // 提交订单成功
	ORDER_PUR_ORDER_SAVE_ERROR("order_pur_order_save_error"), // 提交订单失败
	ORDER_ORDER_QUANTITY_SUCCESS("order_order_quantity_success"), // 查询订单数量成功
	ORDER_ORDER_QUANTITY_ERROR("order_order_quantity_error"), // 查询订单数量失败
	ORDER_ORDER_LIST_QUERY_SUCCESS("order_order_list_query_success"), // 查询订单列表成功
	ORDER_ORDER_LIST_QUERY_ERROR("order_order_list_query_error"), // 查询订单列表失败
	ORDER_QUERY_ORDER_DETAIL_SUCCESS("order_query_order_detail_success"), // 查询订单详情成功
	ORDER_QUERY_ORDER_DETAIL_ERROR("order_query_order_detail_error"), // 查询订单详情失败
	ORDER_QUERY_WAREHOUSE_LIST_SUCCESS("order_query_warehouse_list_success"), // 查询提货点列表成功
	ORDER_QUERY_WAREHOUSE_LIST_ERROR("order_query_warehouse_list_error"), // 查询提货点列表失败
	ORDER_SUMMARY_QUERY_SUCCESS("order_summary_query_success"), // 查询订单汇总成功
	ORDER_SUMMARY_QUERY_ERROR("order_summary_query_error"), // 查询订单汇总失败
	ORDER_MODIFY_DRIVER_SUCCESS("order_modify_driver_success"), // 订单修改司机成功
	ORDER_MODIFY_DRIVER_ERROR("order_modify_driver_error"), // 订单修改司机失败

	/**
	 * b2b-payment模块
	 */
	TRANSFER_ADD_SUCCESS("transfer_add_success"), // 余额调拨单新增成功
	TRANSFER_ADD_ERROR("transfer_add_error"), // 余额调拨单新增失败

	/**
	 * b2b-point模块
	 */
	POINT_QUERY_POINTSACC_SUCCESS("point_query_pointsacc_success"), // 积分账户余额查询成功
	POINT_QUERY_POINTSACC_ERROR("point_query_pointsacc_error"), // 积分账户余额查询失败
	POINT_QUERY_POINTSLOG_SUCCESS("point_query_pointslog_success"), // 积分日志查询成功
	POINT_QUERY_POINTSLOG_ERROR("point_query_pointslog_error"), // 积分日志查询失败

	/**
	 * b2b-search模块
	 */
	SEARCH_REFRESH_GOODS_SUCCESS("search_refresh_goods_success"), // 刷新商品成功
	SEARCH_REFRESH_GOODS_ERROR("search_refresh_goods_error"), // 刷新商品失败
	SEARCH_QUERY_GOODS_SUCCESS("search_query_goods_success"), // 查询商品成功
	SEARCH_QUERY_GOODS_ERROR("search_query_goods_error"), // 查询商品失败

	/**
	 * b2b-shop模块
	 */
	SHOP_QUERY_RETURNORDER_DETAIL_SUCCESS("shop_query_returnorder_detail_success"), // 查询退货申请详情成功
	SHOP_QUERY_RETURNORDER_DETAIL_ERROR("shop_query_returnorder_detail_error"), // 查询退货申请详情失败
	SHOP_QUERY_RETURNORDER_ORDERLIST_SUCCESS("shop_query_returnorder_orderlist_success"), // 查询退货申请列表成功
	SHOP_QUERY_RETURNORDER_ORDERLIST_ERROR("shop_query_returnorder_orderlist_error"), // 查询退货申请列表失败
	SHOP_DELETE_RETURNORDER_RETURNORDERDELETE_SUCCESS("shop_delete_returnorder_returnorderdelete_success"), // 申请退货删除成功
	SHOP_DELETE_RETURNORDER_RETURNORDERDELETE_ERROR("shop_delete_returnorder_returnorderdelete_error"), // 申请退货删除失败
	SHOP_QUERY_REASON_SUCCESS("shop_query_reason_success"), // 查询退货申请原因成功
	SHOP_QUERY_REASON_ERROR("shop_query_reason_error"), // 查询退货申请原因失败
	SHOP_SAVE_RETURN_ORDER_SUCCESS("shop_save_return_order_success"), // 退货申请成功
	SHOP_SAVE_RETURN_ORDER_ERROR("shop_save_return_order_error"), // 退货申请失败
	SHOP_SHOP_CONCERN_QUERY_SUCCESS("shop_shop_concern_query_success"), // 查询关注店铺成功
	SHOP_SHOP_CONCERN_QUERY_ERROR("shop_shop_concern_query_error"), // 查询关注店铺失败
	SHOP_SHOP_CONCERN_QTY_SUCCESS("shop_shop_concern_qty_success"), // 查询关注店铺数量成功
	SHOP_SHOP_CONCERN_QTY_ERROR("shop_shop_concern_qty_error"), // 查询关注店铺数量失败
	SHOP_SHOP_CONCERN_ADD_SUCCESS("shop_shop_concern_add_success"), // 关注店铺成功
	SHOP_SHOP_CONCERN_ADD_ERROR("shop_shop_concern_add_error"), // 关注店铺失败
	SHOP_SHOP_CONCERN_DEL_SUCCESS("shop_shop_concern_del_success"), // 取消店铺关注成功
	SHOP_SHOP_CONCERN_DEL_ERROR("shop_shop_concern_del_error"), // 取消店铺关注失败
	SHOP_SHOP_CONCERN_STATE_SUCCESS("shop_shop_concern_state_success"), // 查询店铺是否已关注成功
	SHOP_SHOP_CONCERN_STATE_ERROR("shop_shop_concern_state_error"), // 查询店铺是否已关注失败

	/**
	 * b2b-task模块
	 */

	/**
	 * b2b-user模块
	 */
	USER_QUERY_CUSTOMER_ADDRESS_SUCCESS("user_query_customer_address_success"), // 配送地址查询成功
	USER_QUERY_CUSTOMER_ADDRESS_ERROR("user_query_customer_address_error"), // 配送地址查询失败
	USER_SAVE_CUSTOMER_ADDRESS_SUCCESS("user_save_customer_address_success"), // 操作成功
	USER_SAVE_CUSTOMER_ADDRESS_ERROR("user_save_customer_address_error"), // 操作失败
	USER_DELETE_CUSTOMER_ADDRESS_SUCCESS("user_delete_customer_address_success"), // 配送地址删除成功
	USER_DELETE_CUSTOMER_ADDRESS_ERROR("user_delete_customer_address_error"), // 配送地址删除失败
	USER_QUERY_CUSTOMER_REL_SUCCESS("user_query_customer_rel_success"), // 关联申请查询成功
	USER_QUERY_CUSTOMER_REL_ERROR("user_query_customer_rel_error"), // 关联申请查询失败
	USER_SAVE_CUSTOMER_REL_SUCCESS("user_save_customer_rel_success"), // 关联申请或取消关联成功
	USER_SAVE_CUSTOMER_REL_ERROR("user_save_customer_rel_error"), // 关联申请或取消关联失败
	USER_QUERY_CUSTOMER_REL_LIST_SUCCESS("user_query_customer_rel_list_success"), // 查询用户可关联渠道列表成功
	USER_QUERY_CUSTOMER_REL_LIST_ERROR("user_query_customer_rel_list_error"), // 查询用户可关联渠道列表失败
	USER_QUERY_CUSTOMER_REQ_SUCCESS("user_query_customer_req_success"), // 渠道申请查询成功
	USER_QUERY_CUSTOMER_REQ_ERROR("user_query_customer_req_error"), // 渠道申请查询失败
	USER_SAVE_CUSTOMER_REQ_SUCCESS("user_save_customer_req_success"), // 会员注册成功
	USER_SAVE_CUSTOMER_REQ_ERROR("user_save_customer_req_error"), // 会员注册失败
	USER_DELETE_CUSTOMER_REQ_SUCCESS("user_delete_customer_req_success"), // 渠道申请删除成功
	USER_DELETE_CUSTOMER_REQ_ERROR("user_delete_customer_req_error"), // 渠道申请删除失败
	USER_AUDIT_CUSTOMER_REQ_SUCCESS("user_audit_customer_req_success"), // 审核进度查询成功
	USER_AUDIT_CUSTOMER_REQ_ERROR("user_audit_customer_req_error"), // 审核进度查询失败
	USER_QUERY_DRIVER_SUCCESS("user_query_driver_success"), // 司机查询成功
	USER_QUERY_DRIVER_ERROR("user_query_driver_error"), // 司机查询失败
	USER_CHECK_DRIVER_SUCCESS("user_check_driver_success"), // 司机新增效验成功
	USER_CHECK_DRIVER_ERROR("user_check_driver_error"), // 司机新增效验失败
	USER_SAVE_DRIVER_SUCCESS("user_save_driver_success"), // 司机新增成功
	USER_SAVE_DRIVER_ERROR("user_save_driver_error"), // 司机新增失败
	USER_DELETE_DRIVER_SUCCESS("user_delete_driver_success"), // 司机删除成功
	USER_DELETE_DRIVER_ERROR("user_delete_driver_error"), // 司机删除失败
	USER_QUERY_CAR_TYPE_SUCCESS("user_query_car_type_success"), // 车辆类型查询成功
	USER_QUERY_CAR_TYPE_ERROR("user_query_car_type_error"), // 车辆类型查询失败
	USER_QUERY_CUSTOMER_SUCCESS("user_query_customer_success"), // 渠道查询成功
	USER_QUERY_CUSTOMER_ERROR("user_query_customer_error"), // 渠道查询失败
	USER_QUERY_REL_CUSTOMER_SUCCESS("user_query_rel_customer_success"), // 关联渠道查询成功
	USER_QUERY_REL_CUSTOMER_ERROR("user_query_rel_customer_error"), // 关联渠道查询失败
	USER_UPDATE_USER_INFO_SUCCESS("user_update_user_info_success"), // 会员信息修改成功
	USER_UPDATE_USER_INFO_ERROR("user_update_user_info_error"), // 会员信息修改失败
	USER_QUERY_USER_INFO_SUCCESS("user_query_user_info_success"), // 会员信息查询成功
	USER_QUERY_USER_INFO_ERROR("user_query_user_info_error"), // 会员信息查询失败
	USER_QUERY_CUSTOMER_GROUP_SUCCESS("user_query_customer_group_success"), // 渠道分组查询成功
	USER_QUERY_CUSTOMER_GROUP_ERROR("user_query_customer_group_error"), // 渠道分组查询失败
	USER_QUERY_CUSTOMER_INFO_SUCCESS("user_query_customer_info_success"), // 渠道信息查询成功
	USER_QUERY_CUSTOMER_INFO_ERROR("user_query_customer_info_error"), // 渠道信息查询失败
	USER_QUERY_EXIST_SUCCESS("user_query_exist_success"), // 查询用户是否存在成功
	USER_QUERY_EXIST_ERROR("user_query_exist_error"), // 查询用户是否存在失败
	USER_QUERY_DELEGATION_USER_LIST_SUCCESS("user_query_delegation_user_list_success"), // 查询委托用户列表成功
	USER_QUERY_DELEGATION_USER_LIST_ERROR("user_query_delegation_user_list_error"), // 查询委托用户列表失败
	USER_ADD_DELEGATION_USER_SUCCESS("user_add_delegation_user_success"), // 受托申请添加成功
	USER_ADD_DELEGATION_USER_ERROR("user_add_delegation_user_error"), // 受托申请添加失败
	USER_DELETE_DELEGATION_USER_SUCCESS("user_delete_delegation_user_success"), // 委托关系删除成功
	USER_DELETE_DELEGATION_USER_ERROR("user_delete_delegation_user_error"), // 委托关系删除失败
	USER_DELEGATION_USER_DETAIL_SUCCESS("user_delegation_user_detail_success"), // 查询委托关系详情成功
	USER_DELEGATION_USER_DETAIL_ERROR("user_delegation_user_detail_error"), // 查询委托关系详情失败
	USER_QUERY_CUSTOMER_AUTH_SUCCESS("user_query_customer_auth_success"), // 供货授权关系查询成功
	USER_QUERY_CUSTOMER_AUTH_ERROR("user_query_customer_auth_error"), // 供货授权关系查询失败
	/**
	 * b2b-common模块
	 */
	COMMON_PARAMS_ERROR("common_params_error"); // 获取params参数失败

	/**
	 * 消息key
	 */
	private final String key;

	private BaseMessageEnum(String key) {
		this.key = key;
	}

	public String getKey() {
		return this.key;
	}

}
