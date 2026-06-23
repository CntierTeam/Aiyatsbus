package hamsteryds.nereusopus.utils;

import taboolib.platform.util.BukkitSkull;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class ItemUtils {
   public static final NamespacedKey enchantKey;
   public static final NamespacedKey enchantsInfoKey;
   public static final NamespacedKey configParamKey;
   public static final NamespacedKey configValueKey;
   private static Field profileField;

   public static ItemStack make(Material type, String displayName, String... lore) {
      return make(type, displayName, Arrays.asList(lore));
   }

   public static ItemStack make(Material type, Component displayName, String... lore) {
      List<String> lores = Arrays.asList(lore);
      ItemStack item = new ItemStack(type);
      ItemMeta meta = item.getItemMeta();
      meta.displayName(displayName);
      if (lores.size() != 0) {
         meta.setLore(List.of(lore));
      }

      item.setItemMeta(meta);
      return item;
   }

   public static ItemStack make(Material type, int amount, String displayName, String... lore) {
      ItemStack item = make(type, displayName, Arrays.asList(lore));
      item.setAmount(amount);
      return item;
   }

   public static ItemStack make(Material type, String displayName, List<String> lore) {
      ItemStack item = new ItemStack(type);
      ItemMeta meta = item.getItemMeta();
      if (!displayName.equalsIgnoreCase("")) {
         meta.setDisplayName(displayName);
      }

      if (lore.size() != 0) {
         meta.setLore(lore);
      }

      item.setItemMeta(meta);
      return item;
   }

   public static ItemStack setName(ItemStack item, String name) {
      ItemMeta meta = item.getItemMeta();
      meta.setDisplayName(name);
      item.setItemMeta(meta);
      return item;
   }

   public static ItemStack addDurability(ItemStack item, double percent) {
      if (item == null || !isDamageable(item)) {
         return item;
      } else if (percent < 1.0 && percent > 0.0) {
         item.setDurability((short)Math.max(item.getDurability() - item.getType().getMaxDurability() * percent, 0.0));
         return item;
      } else {
         return addDurability(item, (int)percent);
      }
   }

   public static ItemStack addDurability(ItemStack item, short point) {
      if (item == null || !isDamageable(item)) {
         return item;
      } else if (item.hasItemMeta() && item.getItemMeta().isUnbreakable()) {
         return item;
      } else {
         item.setDurability((short)(item.getDurability() + point));
         return item.getDurability() >= item.getType().getMaxDurability() ? null : item;
      }
   }

   public static boolean isDamageable(ItemStack itemStack) {
      return itemStack.getType().getMaxDurability() > 0;
   }

   public static ItemStack setSkull(ItemStack item, String headBase64) {
      return BukkitSkull.INSTANCE.applySkull(item, headBase64);
   }

   public static ItemStack fromString(String string) {
      String[] splited = string.replace("&", "\u00a7").split(";");
      int length = splited.length;
      return make(Material.valueOf(splited[0]), length >= 2 ? splited[1] : "", length >= 3 ? splited[2].split(",") : new String[0]);
   }

   public static Map<Enchantment, Integer> getEnchants(ItemStack item) {
      if (item == null) {
         return new HashMap<>();
      } else if (item.getItemMeta() instanceof EnchantmentStorageMeta) {
         EnchantmentStorageMeta meta = (EnchantmentStorageMeta)item.getItemMeta();
         return meta.getStoredEnchants();
      } else {
         return item.getEnchantments();
      }
   }

   public static void setEnchants(ItemStack item, Map<Enchantment, Integer> addedEnchants) {
      Map<Enchantment, Integer> enchants = getEnchants(item);
      if (item != null) {
         for (Enchantment enchantment : enchants.keySet()) {
            item.removeEnchantment(enchantment);
         }

         if (item.getItemMeta() instanceof EnchantmentStorageMeta) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta)item.getItemMeta();

            for (Enchantment enchant : enchants.keySet()) {
               meta.removeStoredEnchant(enchant);
            }

            for (Enchantment enchant : addedEnchants.keySet()) {
               meta.addStoredEnchant(enchant, addedEnchants.get(enchant), true);
            }

            item.setItemMeta(meta);
         } else {
            ItemMeta meta = item.getItemMeta();

            for (Enchantment enchant : enchants.keySet()) {
               meta.removeEnchant(enchant);
            }

            for (Enchantment enchant : addedEnchants.keySet()) {
               meta.addEnchant(enchant, addedEnchants.get(enchant), true);
            }

            item.setItemMeta(meta);
         }
      }
   }

   public static List<String> getLore(ItemStack item) {
      return (List<String>)(item != null && item.getItemMeta() != null && item.getItemMeta().getLore() != null
         ? item.getItemMeta().getLore()
         : new ArrayList<>());
   }

   public static String getDisplayName(ItemStack item) {
      return item != null && item.getItemMeta() != null && item.getItemMeta().getDisplayName() != null ? item.getItemMeta().getDisplayName() : "";
   }

   public static <T> T read(@NotNull ItemStack item, NamespacedKey key, PersistentDataType<T, T> type) {
      ItemMeta meta = item.getItemMeta();
      PersistentDataContainer pdc = meta.getPersistentDataContainer();
      return (T)(pdc.has(key, type) ? pdc.get(key, type) : null);
   }

   public static <T> ItemStack write(@NotNull ItemStack item, NamespacedKey key, PersistentDataType<T, T> type, T value) {
      ItemMeta meta = item.getItemMeta();
      PersistentDataContainer pdc = meta.getPersistentDataContainer();
      if (pdc.has(key, type)) {
         pdc.remove(key);
      }

      pdc.set(key, type, value);
      item.setItemMeta(meta);
      return item;
   }

   public static void clearEnchants(@NotNull ItemStack item) {
      ItemMeta meta = item.getItemMeta();
      PersistentDataContainer pdc = meta.getPersistentDataContainer();
      NamespacedKey key = enchantsInfoKey;
      if (pdc.has(key, PersistentDataType.STRING)) {
         pdc.remove(key);
      }
   }

   public static ItemStack addDurability(ItemStack item, int added) {
      ItemMeta meta = item.getItemMeta();
      if (meta == null) {
         return item;
      } else if (meta.isUnbreakable()) {
         return item;
      } else {
         if (meta instanceof Damageable && item.getType().getMaxDurability() >= 30) {
            Damageable damageable = (Damageable)meta;
            if (damageable.getDamage() < item.getType().getMaxDurability()) {
               int newDamage = damageable.getDamage() + added;
               damageable.setDamage(Math.max(newDamage, 0));
               item.setItemMeta(damageable);
            } else {
               item.setType(Material.AIR);
            }
         }

         return item;
      }
   }

   static {
      try {
         SkullMeta meta = (SkullMeta)new ItemStack(Material.PLAYER_HEAD).getItemMeta();
         profileField = meta.getClass().getDeclaredField("profile");
         profileField.setAccessible(true);
      } catch (NoSuchFieldException var1) {
         var1.printStackTrace();
      }

      enchantKey = new NamespacedKey("summericebearstore", "enchant");
      enchantsInfoKey = new NamespacedKey("summericebearstore", "enchantsinfo");
      configParamKey = new NamespacedKey("summericebearstore", "configparam");
      configValueKey = new NamespacedKey("summericebearstore", "configvalue");
   }
}
