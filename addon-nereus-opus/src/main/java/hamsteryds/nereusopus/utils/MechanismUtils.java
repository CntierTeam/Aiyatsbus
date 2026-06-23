package hamsteryds.nereusopus.utils;

import cc.polarastrum.aiyatsbus.core.compat.AntiGriefChecker;
import hamsteryds.nereusopus.enchants.internal.CustomEnchantment;
import hamsteryds.nereusopus.enchants.internal.entries.SkillEnchantment;
import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class MechanismUtils {
   public static final HashMap<UUID, HashMap<String, Long>> usedStamps = new HashMap<>();
   public static HashMap<UUID, HashMap<String, Boolean>> cache = new HashMap<>();

   public static boolean checkCooldown(Player player, String key, double cd, boolean info) {
      UUID uuid = player.getUniqueId();
      double rate = 1.0;

      for (String permission : SkillEnchantment.rates.keySet()) {
         if (player.hasPermission(permission)) {
            rate = Math.min(rate, SkillEnchantment.rates.get(permission));
         }
      }

      if (usedStamps.containsKey(uuid) && usedStamps.get(uuid).containsKey(key)) {
         long dist = System.currentTimeMillis() - usedStamps.get(uuid).get(key) - (long)(cd * rate * 1000.0);
         if (dist >= 0L) {
            return true;
         } else {
            if (info) {
               DebugUtils.debug("\u53d1\u9001\u51b7\u5374\u4fe1\u606f", player);
               String message = SkillEnchantment.cdMessage.replace("{dist}", -dist / 1000L + "").replace("{key}", key);
               if (SkillEnchantment.cdForm.equals("actionbar")) {
                  InformationUtils.sendActionBar(player, message);
               } else if (SkillEnchantment.cdForm.equals("message")) {
                  InformationUtils.sendMsg(player, message);
               } else if (SkillEnchantment.cdForm.equals("title")) {
                  InformationUtils.sendTitle(player, message, "");
               } else if (SkillEnchantment.cdForm.equals("subtitle")) {
                  InformationUtils.sendTitle(player, "", message);
               }
            }

            return false;
         }
      } else {
         return true;
      }
   }

   public static void addCooldown(Player player, String key) {
      UUID uuid = player.getUniqueId();
      if (!usedStamps.containsKey(uuid)) {
         usedStamps.put(uuid, new HashMap<>());
      }

      usedStamps.get(uuid).put(key, System.currentTimeMillis());
   }

   public static boolean checkPercent(CustomEnchantment enchant, int level) {
      try {
         return Math.random() * 100.0 <= enchant.getValue("chance", level, "100");
      } catch (Throwable var3) {
         System.out.println("MechanismUtils checkPercent \u51fa\u73b0\u95ee\u9898");
         System.out.println("\u9644\u9b54: " + enchant);
         throw var3;
      }
   }

   public static boolean checkRequireFull(CustomEnchantment enchant, LivingEntity entity) {
      if (!(entity instanceof Player player)) {
         return true;
      } else {
         return enchant.getBool("require-full-charge", false) ? player.getAttackCooldown() >= 1.0 : true;
      }
   }

   public static boolean checkPermission(CustomEnchantment enchant, Block block, LivingEntity creature, boolean... flag) {
      return creature instanceof Player player && enchant.getBool("permission-check", true)
         ? AntiGriefChecker.INSTANCE.canBreak(player, block.getLocation())
         : true;
   }

   public static boolean checkCritical(Entity attacker) {
      return attacker.getFallDistance() > 0.0F && !attacker.isOnGround();
   }

   public static boolean checkShiftIgnored(EventExecutor executor, LivingEntity entity) {
      if (!(entity instanceof Player player)) {
         return true;
      } else {
         return executor.getBool("shift-ignored", false) ? !player.isSneaking() : true;
      }
   }
}
