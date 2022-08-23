package com.github.tvbox.osc.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.github.catvod.crawler.SpiderDebug;
import com.github.tvbox.osc.api.ApiConfig;
import com.github.tvbox.osc.base.App;
import com.github.tvbox.osc.bean.AbsSortJson;
import com.github.tvbox.osc.bean.AbsSortXml;
import com.github.tvbox.osc.bean.MovieSort;
import com.github.tvbox.osc.bean.SourceBean;
import com.github.tvbox.osc.server.ControlManager;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.LinkedHashMap;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author pj567
 * @date :2020/12/21
 * @description:
 */
public class DefaultConfig {

    public static List<MovieSort.SortData> adjustSort(String sourceKey, List<MovieSort.SortData> list, boolean withMy) {
        List<MovieSort.SortData> data = new ArrayList<>();
        if (sourceKey != null) {
            SourceBean sb = ApiConfig.get().getSource(sourceKey);
            ArrayList<String> categories = sb.getCategories();
            if (!categories.isEmpty()) {
                for (String cate : categories) {
                    for (MovieSort.SortData sortData : list) {
                        if (sortData.name.equals(cate)) {
                            if (sortData.filters == null)
                                sortData.filters = new ArrayList<>();
                            data.add(sortData);
                        }
                    }
                }
            } else {
                for (MovieSort.SortData sortData : list) {
                    if (sortData.filters == null)
                        sortData.filters = new ArrayList<>();
                    data.add(sortData);
                }
            }
        }
        if (withMy) {
            data.add(0, new MovieSort.SortData("my0", "我的"));
            data.add(1, new MovieSort.SortData("my1", "豆瓣", readAssetsTextReturnStr("douban")));
        }
        Collections.sort(data);
        return data;
    }

    public static ArrayList<MovieSort.SortFilter> readAssetsTextReturnStr(String fileName) {
        try {
            ArrayList<MovieSort.SortFilter> sortFilter = new ArrayList<>();
            String  json ="[{\"key\":\"tags\",\"name\":\"分类\",\"value\":[{\"n\":\"全部\",\"v\":\"\"},{\"n\":\"电影\",\"v\":\"电影\"},{\"n\":\"电视剧\",\"v\":\"电视剧\"},{\"n\":\"综艺\",\"v\":\"综艺\"},{\"n\":\"动漫\",\"v\":\"动漫\"},{\"n\":\"纪录片\",\"v\":\"纪录片\"}]},{\"key\":\"countries\",\"name\":\"国家\",\"value\":[{\"n\":\"全部\",\"v\":\"\"},{\"n\":\"中国大陆\",\"v\":\"中国大陆\"},{\"n\":\"中国香港\",\"v\":\"中国香港\"},{\"n\":\"中国台湾\",\"v\":\"中国台湾\"},{\"n\":\"欧美\",\"v\":\"欧美\"},{\"n\":\"美国\",\"v\":\"美国\"},{\"n\":\"法国\",\"v\":\"法国\"},{\"n\":\"英国\",\"v\":\"英国\"},{\"n\":\"日本\",\"v\":\"日本\"},{\"n\":\"韩国\",\"v\":\"韩国\"},{\"n\":\"德国\",\"v\":\"德国\"},{\"n\":\"泰国\",\"v\":\"泰国\"},{\"n\":\"印度\",\"v\":\"印度\"},{\"n\":\"伊朗\",\"v\":\"伊朗\"},{\"n\":\"俄罗斯\",\"v\":\"俄罗斯\"},{\"n\":\"意大利\",\"v\":\"意大利\"},{\"n\":\"西班牙\",\"v\":\"西班牙\"},{\"n\":\"加拿大\",\"v\":\"加拿大\"},{\"n\":\"其他\",\"v\":\"其他\"}]},{\"key\":\"year\",\"name\":\"年份\",\"value\":[{\"n\":\"全部年代\",\"v\":\"\"},{\"n\":\"2022\",\"v\":\"2022,2022\"},{\"n\":\"2021\",\"v\":\"2021,2021\"},{\"n\":\"2020\",\"v\":\"2020,2020\"},{\"n\":\"2019\",\"v\":\"2019,2019\"},{\"n\":\"2010年代\",\"v\":\"2010,2018\"},{\"n\":\"2000年代\",\"v\":\"2000,2009\"},{\"n\":\"90年代\",\"v\":\"1990,1999\"},{\"n\":\"80年代\",\"v\":\"1980,1989\"},{\"n\":\"70年代\",\"v\":\"1970,1979\"},{\"n\":\"更早\",\"v\":\"1,1969\"}]},{\"key\":\"genres\",\"name\":\"类型\",\"value\":[{\"n\":\"全部类型\",\"v\":\"\"},{\"n\":\"剧情\",\"v\":\"剧情\"},{\"n\":\"喜剧\",\"v\":\"喜剧\"},{\"n\":\"动作\",\"v\":\"动作\"},{\"n\":\"爱情\",\"v\":\"爱情\"},{\"n\":\"科幻\",\"v\":\"科幻\"},{\"n\":\"动画\",\"v\":\"动画\"},{\"n\":\"悬疑\",\"v\":\"悬疑\"},{\"n\":\"惊悚\",\"v\":\"惊悚\"},{\"n\":\"恐怖\",\"v\":\"恐怖\"},{\"n\":\"犯罪\",\"v\":\"犯罪\"},{\"n\":\"同性\",\"v\":\"同性\"},{\"n\":\"武侠\",\"v\":\"武侠\"},{\"n\":\"灾难\",\"v\":\"灾难\"},{\"n\":\"冒险\",\"v\":\"冒险\"},{\"n\":\"历史\",\"v\":\"历史\"},{\"n\":\"战争\",\"v\":\"战争\"},{\"n\":\"西部\",\"v\":\"西部\"},{\"n\":\"奇幻\",\"v\":\"奇幻\"},{\"n\":\"传记\",\"v\":\"传记\"},{\"n\":\"歌舞\",\"v\":\"歌舞\"},{\"n\":\"音乐\",\"v\":\"音乐\"},{\"n\":\"情色\",\"v\":\"情色\"}]},{\"key\":\"sort\",\"name\":\"排序\",\"value\":[{\"n\":\"近期热门\",\"v\":\"U\"},{\"n\":\"标记最多\",\"v\":\"T\"},{\"n\":\"评分最高\",\"v\":\"S\"},{\"n\":\"最新上映\",\"v\":\"R\"}]}]";

            //解析为json元素
            JsonElement one = JsonParser.parseString(json);

            if (one.isJsonObject()) {
                sortFilter.add(getSortFilter(one.getAsJsonObject()));
            } else {
                for (JsonElement ele : one.getAsJsonArray()) {
                    sortFilter.add(getSortFilter(ele.getAsJsonObject()));
                }
            }
            return sortFilter;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private static MovieSort.SortFilter getSortFilter(JsonObject obj) {
        String key = obj.get("key").getAsString();
        String name = obj.get("name").getAsString();
        JsonArray kv = obj.getAsJsonArray("value");
        LinkedHashMap<String, String> values = new LinkedHashMap<>();
        for (JsonElement ele : kv) {
            values.put(ele.getAsJsonObject().get("n").getAsString(), ele.getAsJsonObject().get("v").getAsString());
        }
        MovieSort.SortFilter filter = new MovieSort.SortFilter();
        filter.key = key;
        filter.name = name;
        filter.values = values;
        return filter;
    }

    public static int getAppVersionCode(Context mContext) {
        //包管理操作管理类
        PackageManager pm = mContext.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(mContext.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static String getAppVersionName(Context mContext) {
        //包管理操作管理类
        PackageManager pm = mContext.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(mContext.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 后缀
     *
     * @param name
     * @return
     */
    public static String getFileSuffix(String name) {
        if (TextUtils.isEmpty(name)) {
            return "";
        }
        int endP = name.lastIndexOf(".");
        return endP > -1 ? name.substring(endP) : "";
    }

    /**
     * 获取文件的前缀
     *
     * @param fileName
     * @return
     */
    public static String getFilePrefixName(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return "";
        }
        int start = fileName.lastIndexOf(".");
        return start > -1 ? fileName.substring(0, start) : fileName;
    }

    //增加对flv|avi|mkv|rm|wmv|mpg等几种视频格式的支持
    //private static final Pattern snifferMatch = Pattern.compile("http((?!http).){26,}?\\.(m3u8|mp4)\\?.*|http((?!http).){26,}\\.(m3u8|mp4)|http((?!http).){26,}?/m3u8\\?pt=m3u8.*|http((?!http).)*?default\\.ixigua\\.com/.*|http((?!http).)*?cdn-tos[^\\?]*|http((?!http).)*?/obj/tos[^\\?]*|http.*?/player/m3u8play\\.php\\?url=.*|http.*?/player/.*?[pP]lay\\.php\\?url=.*|http.*?/playlist/m3u8/\\?vid=.*|http.*?\\.php\\?type=m3u8&.*|http.*?/download.aspx\\?.*|http.*?/api/up_api.php\\?.*|https.*?\\.66yk\\.cn.*|http((?!http).)*?netease\\.com/file/.*");
    private static final Pattern snifferMatch = Pattern.compile("http((?!http).)*?default\\.365yg\\.com/.*|http((?!http).){26,}?\\.(m3u8|mp4|flv|avi|mkv|rm|wmv|mpg)\\?.*|http((?!http).){26,}\\.(m3u8|mp4|flv|avi|mkv|rm|wmv|mpg)|http((?!http).){26,}?/m3u8\\?pt=m3u8.*|http((?!http).)*?default\\.ixigua\\.com/.*|http((?!http).)*?cdn-tos[^\\?]*|http((?!http).)*?/obj/tos[^\\?]*|http.*?/player/m3u8play\\.php\\?url=.*|http.*?/player/.*?[pP]lay\\.php\\?url=.*|http.*?/playlist/m3u8/\\?vid=.*|http.*?\\.php\\?type=m3u8&.*|http.*?/download.aspx\\?.*|http.*?/api/up_api.php\\?.*|https.*?\\.66yk\\.cn.*|http((?!http).)*?netease\\.com/file/.*");
    public static boolean isVideoFormat(String url) {
        if (url.contains("=http") || url.contains("=https") || url.contains("=https%3a%2f") || url.contains("=http%3a%2f")) {
            return false;
        }
        if (snifferMatch.matcher(url).find()) {
            if (url.contains("cdn-tos") && (url.contains(".js") || url.contains(".css"))) {
                return false;
            }
            return true;
        }
        return false;
    }


    public static String safeJsonString(JsonObject obj, String key, String defaultVal) {
        try {
            if (obj.has(key))
                return obj.getAsJsonPrimitive(key).getAsString().trim();
            else
                return defaultVal;
        } catch (Throwable th) {
        }
        return defaultVal;
    }

    public static int safeJsonInt(JsonObject obj, String key, int defaultVal) {
        try {
            if (obj.has(key))
                return obj.getAsJsonPrimitive(key).getAsInt();
            else
                return defaultVal;
        } catch (Throwable th) {
        }
        return defaultVal;
    }

    public static ArrayList<String> safeJsonStringList(JsonObject obj, String key) {
        ArrayList<String> result = new ArrayList<>();
        try {
            if (obj.has(key)) {
                if (obj.get(key).isJsonObject()) {
                    result.add(obj.get(key).getAsString());
                } else {
                    for (JsonElement opt : obj.getAsJsonArray(key)) {
                        result.add(opt.getAsString());
                    }
                }
            }
        } catch (Throwable th) {
        }
        return result;
    }

    public static String checkReplaceProxy(String urlOri) {
        if (urlOri.startsWith("proxy://"))
            return urlOri.replace("proxy://", ControlManager.get().getAddress(true) + "proxy?");
        return urlOri;
    }
}