package hamsteryds.nereusopus.utils;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class InformationUtils {
   public static void sendMsg(Entity entity, String msg) {
      entity.sendMessage(msg);
   }

   public static void broadcastMsg(String msg) {
      for (Player player : Bukkit.getOnlinePlayers()) {
         player.sendMessage(msg);
      }
   }

   public static void sendTitle(Entity entity, String title, String subTitle) {
      if (entity instanceof Player player) {
         player.sendTitle(title, subTitle, 10, 70, 20);
      }
   }

   public static void broadcastTitle(String title, String subTitle) {
      for (Player player : Bukkit.getOnlinePlayers()) {
         player.sendTitle(title, subTitle, 10, 70, 20);
      }
   }

   public static void sendTitle(Entity entity, String title, String subTitle, int in, int stay, int out) {
      if (entity instanceof Player player) {
         player.sendTitle(title, subTitle, in, stay, out);
      }
   }

   public static void sendActionBar(Entity entity, String msg) {
      if (entity instanceof Player player) {
         player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msg));
      }
   }

   @SafeVarargs
   public static <E> void sendInfo(Entity who, String notice, E... params) {
      String form = notice.split(":")[0];
      String content = StringUtils.replace(notice.split(":")[1], params);
      if (content.length() > 0) {
         if (form == "@msg" || form == "@message" || form == "@\u804a\u5929\u680f" || form == "@\u804a\u5929" || form == "@\u804a\u5929\u4fe1\u606f") {
            sendMsg(who, content);
         } else if (form == "@actionbar" || form == "@\u884c\u4e3a\u680f") {
            sendActionBar(who, content);
         } else if (form == "@title" || form == "@\u5927\u6807\u9898") {
            sendTitle(who, content, "");
         } else if (form == "@subtitle" || form == "@\u5c0f\u6807\u9898") {
            sendTitle(who, "", content);
         }
      }
   }
}
