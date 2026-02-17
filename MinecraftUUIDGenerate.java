import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MinecraftUUIDGenerate {

    public static void main(String[] args) {
        System.out.println("uuid查询工具 V1.0");

        Scanner scan = new Scanner(System.in);
        System.err.println("请输入游戏名");
        String playerName = scan.nextLine();

        String onlineUUID = getOnlineUUId(playerName);

        String offlineUUID = getOfflineUUID(playerName);

        System.out.println("正版uuid为:" + onlineUUID);
        System.out.println("离线uuid为:" + offlineUUID);
    }

    /**
     * 计算离线uuid
     * 
     * @param playerName 玩家名字
     * @return 离线uuid
     */
    private static String getOfflineUUID(String playerName) {
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + playerName).getBytes(StandardCharsets.UTF_8)).toString();
    }

    /**
     * 通过官方API获取正版玩家的UUID
     * 
     * @param playerName 玩家名字
     * @return 在线uuid
     */
    public static String getOnlineUUId(String name) {
        // 创建HttpClient实例
        HttpClient client = HttpClient.newHttpClient();

        // 创建HttpRequest
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.mojang.com/users/profiles/minecraft/" + name))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        try {
            // 发送请求并获取响应
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // 获取json
            String jsonResponse = response.body();

            Type type = new TypeToken<Map<String, Object>>() {

            }.getType();
            Gson gson = new Gson();
            Map<String, Object> map = gson.fromJson(jsonResponse, type);
            Set<String> keySet = map.keySet();
            if (keySet.contains("errorMessage")) {
                return "不存在此正版账号";
            } else {
                String uuid = (String) map.get("id");
                // 划线拼接规则: 8-4-4-4-12
                return uuid.replaceFirst(
                        "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{12})",
                        "$1-$2-$3-$4-$5");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return "";
    }
}