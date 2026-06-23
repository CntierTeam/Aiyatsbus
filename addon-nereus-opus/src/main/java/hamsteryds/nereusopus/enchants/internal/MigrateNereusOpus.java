package hamsteryds.nereusopus.enchants.internal;

import taboolib.library.configuration.ConfigurationSection;
import taboolib.module.configuration.Configuration;
import taboolib.module.configuration.Type;
import hamsteryds.nereusopus.enchants.internal.data.AttainSource;
import hamsteryds.nereusopus.enchants.internal.data.CustomRarity;
import hamsteryds.nereusopus.library.parrotx.utils.FileUtil;
import java.util.ArrayList;
import java.util.List;

public class MigrateNereusOpus {
   public static void migrate(Configuration oldConfig) {
      Configuration newConfig = Configuration.Companion.empty(Type.YAML, true);
      newConfig.set("basic.id", FileUtil.getNoExFilename(oldConfig.getFile()));
      newConfig.set("basic.name", oldConfig.getString("displayName"));
      newConfig.set("basic.enable", oldConfig.getBoolean("enabled"));
      newConfig.set("basic.max-level", oldConfig.getInt("maxLevel"));
      newConfig.set("display.description.general", oldConfig.getString("description"));
      newConfig.set("alternative.grindstoneable", oldConfig.getBoolean("grindstoneable"));
      String rarityId = oldConfig.getString("rarity");
      CustomRarity rarity = CustomRarity.fromId(rarityId);
      newConfig.set("rarity", rarity(rarityId));
      newConfig.set("alternative.is-tradeable", rarity.canAttainFrom(AttainSource.VILLAGER));
      newConfig.set("alternative.is-treasure", !rarity.canAttainFrom(AttainSource.ENCHANTING_TABLE));
      newConfig.set("alternative.is-discoverable", rarity.canAttainFrom(AttainSource.LOOT_CHEST));
      newConfig.set("targets", oldConfig.getStringList("limits.targets"));
      List<String> limitations = new ArrayList<>();
      oldConfig.getStringList("limits.conflicts").forEach(limit -> limitations.add("CONFLICT_ENCHANT:" + limit));
      oldConfig.getStringList("limits.neededEnchants").forEach(limit -> limitations.add("DEPENDENCE_ENCHANT:" + limit));
      String permission = oldConfig.getString("limits.permission");
      if (permission != null && !permission.isEmpty()) {
         limitations.add("PERMISSION:" + oldConfig.getString("limits.permission"));
      }

      oldConfig.getStringList("limits.papi").forEach(limit -> limitations.add("PAPI_EXPRESSION:" + limit));
      newConfig.set("limits.withLore", oldConfig.getStringList("limits.withLore"));
      newConfig.set("limits.withoutLore", oldConfig.getStringList("limits.withoutLore"));
      newConfig.set("limits.withName", oldConfig.getString("limits.withName"));
      newConfig.set("limits.withoutName", oldConfig.getString("limits.withoutName"));
      newConfig.setComment("limits", "\u6682\u4e0d\u652f\u6301 limits \u5185\u8fd9\u56db\u9879\u914d\u7f6e");
      newConfig.set("limitations", limitations);
      ConfigurationSection params = oldConfig.getConfigurationSection("params");
      if (params != null) {
         for (String key : params.getKeys(false)) {
            if (!params.isConfigurationSection(key) && !params.isList(key)) {
               String oldText = params.getString(key);
               if (!oldText.contains("damage") && !oldText.contains("maxBlood") && !oldText.contains("amount") && !oldText.contains("dist")) {
                  if (params.getString(key).contains("level")) {
                     newConfig.set("variables.leveled." + key, ":" + params.getString(key).replace("level", "{level}"));
                  } else {
                     newConfig.set("variables.ordinary." + key, params.get(key));
                  }
               } else {
                  newConfig.set("params." + key, params.get(key));
               }
            } else {
               newConfig.set("params." + key, params.get(key));
            }
         }
      }

      newConfig.saveToFile(oldConfig.getFile());
   }

   private static String rarity(String origin) {
      return switch (origin) {
         case "common" -> "\u666e\u901a";
         case "uncommon" -> "\u7f55\u89c1";
         case "rare" -> "\u7cbe\u826f";
         case "epic" -> "\u53f2\u8bd7";
         case "legendary" -> "\u4f20\u5947";
         case "unknown" -> "\u7a00\u4e16";
         case "curse" -> "\u8bc5\u5492";
         case "artifact" -> "\u76ae\u80a4";
         default -> origin;
      };
   }
}
