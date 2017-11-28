package com.water.helper.config;


/**
 * ************************************************************************
 * **                              _oo0oo_                               **
 * **                             o8888888o                              **
 * **                             88" . "88                              **
 * **                             (| -_- |)                              **
 * **                             0\  =  /0                              **
 * **                           ___/'---'\___                            **
 * **                        .' \\\|     |// '.                          **
 * **                       / \\\|||  :  |||// \\                        **
 * **                      / _ ||||| -:- |||||- \\                       **
 * **                      | |  \\\\  -  /// |   |                       **
 * **                      | \_|  ''\---/''  |_/ |                       **
 * **                      \  .-\__  '-'  __/-.  /                       **
 * **                    ___'. .'  /--.--\  '. .'___                     **
 * **                 ."" '<  '.___\_<|>_/___.' >'  "".                  **
 * **                | | : '-  \'.;'\ _ /';.'/ - ' : | |                 **
 * **                \  \ '_.   \_ __\ /__ _/   .-' /  /                 **
 * **            ====='-.____'.___ \_____/___.-'____.-'=====             **
 * **                              '=---='                               **
 * ************************************************************************
 * **                        佛祖保佑      镇类之宝                       **
 * ************************************************************************
 */
@SuppressWarnings("ALL")
public class AppConfig {

    /**
     * 是否打印LogCat
     */
    public static final int LOG_DEBUG = 10 /*10*/;           // 是否打印LogCat >=10都允许打印，小于0的都不允许打印

    /**
     * url
     */
    public static final String BASE_URL = "http://1.89892528.cn:7776/";

    public static final String sServiceUrl = "http://app.taihuawuye.com.cn:8100/appPhone/rest/";

    public static String API_KEY_RESULT = "result";
    public static String API_KEY_ERROR = "errorCode";

    public static String SUCCESS = "1";
    public static String FAILE = "0";
}
