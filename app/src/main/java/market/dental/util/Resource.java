package market.dental.util;

import java.util.Currency;
import java.util.Locale;

/**
 * Created by kemalsamikaraca on 15.01.2018.
 */

public class Resource {

    public static String ajax_login = "http://dental.market/api/set/login";
    public static String ajax_register = "http://dental.market/api/set/register";
    public static String ajax_profile_update = "http://dental.market/api/set/updateProfile";
    public static String ajax_logout = "http://dental.market/api/set/logout";
    public static String ajax_forgot_password = "http://dental.market/api/set/forgotPassword";
    public static String ajax_get_banner_images_url = "http://dental.market/api/get/banners";
    public static String ajax_get_categories = "http://dental.market/api/get/categories";
    public static String ajax_get_products_homeproduct_url = "http://dental.market/api/get/homeProducts";
    public static String ajax_get_products_by_category = "http://dental.market/api/get/productsByCategory";
    public static String ajax_get_product_detail_url = "http://dental.market/api/get/product";
    public static String ajax_get_product_by_search_key = "http://dental.market/api/get/searchProducts";
    public static String ajax_get_professions = "http://dental.market/api/get/jobs";
    public static String ajax_get_city_list = "http://dental.market/api/get/cities";
    public static String ajax_get_borough_list = "http://dental.market/api/get/towns";
    public static String ajax_get_conversation_list = "http://dental.market/api/get/userMessages";
    public static String ajax_get_message_list = "http://dental.market/api/get/convoMessages";

    public static String DOMAIN_NAME = "http://dental.market";
    public static String SHAREDPREF_SEARCH_KEY = "SHAREDPREF_SEARCH_KEY";
    public static String STATIC_ANDROID_API_TOKEN = "izcfpXu74i6YgIxSG712AEaohc0FfYpC";
    public static String VALUE_API_TOKEN = "";
    public static String KEY_API_TOKEN = "api_token";
    public static String KEY_PRODUCT_ID = "KEY_PRODUCT_ID";
    public static String KEY_PRODUCT_DESC = "KEY_PRODUCT_DESC";
    public static String KEY_CATEGORY_ID = "KEY_CATEGORY_ID";
    public static String KEY_MESSAGE_RECEIVER_ID = "KEY_MESSAGE_RECEIVER_ID";
    public static String KEY_CONVERSATION_ID = "KEY_CONVERSATION_ID";

    public static void setDefaultAPITOKEN(){
        VALUE_API_TOKEN = STATIC_ANDROID_API_TOKEN;
    }
}
