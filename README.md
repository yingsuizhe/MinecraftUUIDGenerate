我的世界uuid查询工具

在线uuid: 调用官方API `https://api.mojang.com/users/profiles/minecraft/<playerName>`

离线uuid: 使用 `UUID.nameUUIDFromBytes(("OfflinePlayer:" + playerName).getBytes(StandardCharsets.UTF_8)).toString();`方法
