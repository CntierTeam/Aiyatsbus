package hamsteryds.nereusopus.enchants;

import cc.polarastrum.aiyatsbus.core.Aiyatsbus;
import taboolib.common.io.FileKt;
import taboolib.module.configuration.Configuration;
import taboolib.module.configuration.Type;
import hamsteryds.nereusopus.NereusOpus;
import hamsteryds.nereusopus.enchants.internal.CustomEnchantment;
import hamsteryds.nereusopus.enchants.internal.MigrateNereusOpus;
import hamsteryds.nereusopus.enchants.internal.data.CustomRarity;
import hamsteryds.nereusopus.utils.StringUtils;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class EnchantmentLoader {
   public static final Map<String, CustomEnchantment> byKey = new ConcurrentHashMap<>();
   private static boolean copied = false;

   public static void loadEnchantments(JavaPlugin plugin) {
      loadEnchantments(plugin, true);
   }

   public static void loadEnchantments() {
      loadEnchantments(JavaPlugin.getPlugin(NereusOpus.class), false);
   }

   public static void loadEnchantments(JavaPlugin plugin, boolean onlyMigrate) {
      File folder = new File(plugin.getDataFolder(), "enchantments");
      File configFile = new File(plugin.getDataFolder(), "config.yml");
      YamlConfiguration config = null;
      if (configFile.exists()) {
         config = YamlConfiguration.loadConfiguration(configFile);
      }

      if (!folder.exists()) {
         folder.mkdirs();
      }

      CustomRarity.rarities.clear();
      File rarityFile = new File(folder, "rarities.yml");
      if (!rarityFile.exists()) {
         plugin.saveResource("enchantments/rarities.yml", true);
      }

      Configuration customRarity = Configuration.Companion.loadFromFile(rarityFile, Type.YAML, true);

      for (String rarityId : customRarity.getKeys(false)) {
         CustomRarity.registerRarity(rarityId, customRarity.getConfigurationSection(rarityId));
      }

      plugin.saveResource("enchantments/enchants.yml", true);
      Configuration enchants = Configuration.Companion.loadFromFile(new File(folder, "enchants.yml"), Type.YAML, true);

      try {
         for (String path : enchants.getKeys(false)) {
            for (String enchant : enchants.getStringList(path)) {
               File folder0 = new File(folder, path);
               if (!folder0.exists()) {
                  folder0.mkdirs();
               }

               File file = new File(folder0, enchant + ".yml");
               if (!file.exists()) {
                  if (config != null && !config.getBoolean("save-when-missing", true)) {
                     continue;
                  }

                  plugin.saveResource("enchantments/" + path + "/" + enchant + ".yml", true);
               }

               Configuration current = Configuration.Companion.loadFromFile(file, Type.YAML, true);
               if (!current.contains("basic")) {
                  if (!copied) {
                     FileKt.deepCopyTo(folder, new File(plugin.getDataFolder(), "enchantments-backup"));
                     copied = true;
                  }

                  MigrateNereusOpus.migrate(current);
               }

               if (path.equalsIgnoreCase("curse") && !current.contains("alternative.is-cursed") && !current.contains("alternative.is_cursed")) {
                  current.set("alternative.is-cursed", true);
                  current.saveToFile(file);
               }

               if (!onlyMigrate) {
                  CustomEnchantment ench = (CustomEnchantment)Class.forName("hamsteryds.nereusopus.enchants." + path + "." + StringUtils.keyToName(enchant))
                     .getConstructor(File.class)
                     .newInstance(file);
                  Aiyatsbus.INSTANCE.api().getEnchantmentManager().register(ench);
                  byKey.put(ench.getBasicData().getId(), ench);
               }
            }
         }
      } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException | ClassNotFoundException var16) {
         var16.printStackTrace();
      }
   }
}
