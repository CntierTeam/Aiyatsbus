package hamsteryds.nereusopus.enchants;

import taboolib.library.xseries.XAttribute;
import taboolib.module.nms.MinecraftVersion;
import hamsteryds.nereusopus.enchants.internal.CustomEnchantment;
import hamsteryds.nereusopus.enchants.simple.Streamlining;
import hamsteryds.nereusopus.enchants.simple.Thrive;
import hamsteryds.nereusopus.utils.AttributeUtils;
import hamsteryds.nereusopus.utils.FoliaUtils;
import hamsteryds.nereusopus.utils.InventoryUtils;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class PublicTasks {
   private static final NamespacedKey thriveKey = NamespacedKey.minecraft("thrive");
   private static final NamespacedKey streamliningKey = NamespacedKey.minecraft("streamlining");
   public static HashMap<CustomEnchantment, Method> taskingEnchants = new HashMap<>();
   public static HashMap<String, CustomEnchantment> taskingEnchantsName = new HashMap<>();
   private static final HashMap<UUID, Integer> cachedThriveLevel = new HashMap<>();
   private static final HashMap<UUID, Integer> cachedStreamliningLevel = new HashMap<>();

   public static void registerTaskEnchant(CustomEnchantment enchant, Class clazz) {
      try {
         taskingEnchants.put(enchant, clazz.getDeclaredMethod("run", Player.class, int.class, int.class));
         taskingEnchantsName.put(enchant.getBasicData().getName(), enchant);
      } catch (Exception var3) {
         var3.printStackTrace();
      }
   }

   public static void initialize() {
      FoliaUtils.runAtFixedRate(
         task -> (new Runnable() {
               int counter = 0;

               @Override
               public void run() {
                  for (Player player : Bukkit.getOnlinePlayers()) {
                     PlayerInventory inv = player.getInventory();

                     for (int i = 0; i < inv.getSize(); i++) {
                        ItemStack item = inv.getItem(i);
                        if (item != null) {
                           Map<Enchantment, Integer> enchants = item.getEnchantments();

                           for (Enchantment ench : enchants.keySet()) {
                              CustomEnchantment enchant = EnchantmentLoader.byKey.get(ench.getKey().getKey());
                              if (enchant != null && PublicTasks.taskingEnchantsName.containsKey(enchant.getBasicData().getName())) {
                                 CustomEnchantment customEnchant = PublicTasks.taskingEnchantsName.get(enchant.getBasicData().getName());
                                 if (customEnchant.getBasicData().getEnable()) {
                                    int level = enchants.get(ench);
                                    if (level >= 1 && PublicTasks.shouldRun(customEnchant, level, this.counter)) {
                                       try {
                                          PublicTasks.taskingEnchants.get(customEnchant).invoke(enchant, player, i, level);
                                       } catch (InvocationTargetException | IllegalAccessException var22) {
                                          var22.printStackTrace();
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }

                     int maxThrive = 0;
                     int maxStreamlining = 0;
                     Thrive thrive = (Thrive)EnchantmentLoader.byKey.get("thrive");
                     Streamlining streamlining = (Streamlining)EnchantmentLoader.byKey.get("streamlining");

                     for (EquipmentSlot slot : InventoryUtils.ARMORS) {
                        ItemStack item = inv.getItem(slot);
                        if (item.getType() != Material.AIR) {
                           Map<Enchantment, Integer> enchants = item.getEnchantments();
                           if (thrive != null && enchants.containsKey(thrive.getEnchantment())) {
                              maxThrive += Math.max(0, enchants.get(thrive.getEnchantment()));
                           }

                           if (streamlining != null && enchants.containsKey(streamlining.getEnchantment())) {
                              maxStreamlining += Math.max(0, enchants.get(streamlining.getEnchantment()));
                           }
                        }
                     }

                     if (thrive != null && thrive.getBasicData().getEnable() && !player.isDead()) {
                        UUID uuid = player.getUniqueId();
                        Integer cachedLevel = PublicTasks.cachedThriveLevel.get(uuid);
                        if (cachedLevel == null || cachedLevel != maxThrive) {
                           AttributeInstance maxHealth = player.getAttribute((Attribute)XAttribute.MAX_HEALTH.get());
                           if (maxHealth != null) {
                              double currentHealth = player.getHealth();
                              double oldMaxHealth = maxHealth.getValue();
                              List<AttributeModifier> modifiersToRemove = new ArrayList<>();
                              maxHealth.getModifiers().forEach(modifierxxx -> {
                                 if (MinecraftVersion.INSTANCE.isHigherOrEqual(13)) {
                                    if (AttributeUtils.getKey(modifierxxx).equals(PublicTasks.thriveKey)) {
                                       modifiersToRemove.add(modifierxxx);
                                    }
                                 } else if (modifierxxx.getName().equalsIgnoreCase("thrive")) {
                                    modifiersToRemove.add(modifierxxx);
                                 }
                              });

                              for (AttributeModifier modifier : modifiersToRemove) {
                                 maxHealth.removeModifier(modifier);
                              }

                              if (maxThrive > 0) {
                                 AttributeModifier modifier;
                                 if (MinecraftVersion.INSTANCE.isHigherOrEqual(13)) {
                                    modifier = AttributeUtils.newByKey(
                                       PublicTasks.thriveKey, thrive.getValue("max-health-increase", maxThrive), Operation.ADD_NUMBER
                                    );
                                 } else {
                                    modifier = new AttributeModifier("thrive", thrive.getValue("max-health-increase", maxThrive), Operation.ADD_NUMBER);
                                 }

                                 maxHealth.addModifier(modifier);
                              }

                              double newMaxHealth = maxHealth.getValue();
                              if (newMaxHealth > oldMaxHealth && oldMaxHealth > 0.0) {
                                 double healthPercentage = Math.min(1.0, currentHealth / oldMaxHealth);
                                 double newHealth = healthPercentage * newMaxHealth;
                                 newHealth = Math.max(0.5, Math.min(newHealth, newMaxHealth));
                                 player.setHealth(newHealth);
                              }

                              PublicTasks.cachedThriveLevel.put(uuid, maxThrive);
                           }
                        }
                     }

                     if (streamlining != null && streamlining.getBasicData().getEnable()) {
                        UUID uuid = player.getUniqueId();
                        Integer cachedLevel = PublicTasks.cachedStreamliningLevel.get(uuid);
                        if (cachedLevel == null || cachedLevel != maxStreamlining) {
                           AttributeInstance moveSpeed = player.getAttribute((Attribute)XAttribute.MOVEMENT_SPEED.get());

                           assert moveSpeed != null;

                           List<AttributeModifier> modifiersToRemove = new ArrayList<>();
                           moveSpeed.getModifiers().forEach(modifierxxx -> {
                              if (MinecraftVersion.INSTANCE.isHigherOrEqual(13)) {
                                 if (AttributeUtils.getKey(modifierxxx).equals(PublicTasks.streamliningKey)) {
                                    modifiersToRemove.add(modifierxxx);
                                 }
                              } else if (modifierxxx.getName().equalsIgnoreCase("streamlining")) {
                                 modifiersToRemove.add(modifierxxx);
                              }
                           });

                           for (AttributeModifier modifier : modifiersToRemove) {
                              moveSpeed.removeModifier(modifier);
                           }

                           if (maxStreamlining > 0) {
                              AttributeModifier modifier;
                              if (MinecraftVersion.INSTANCE.isHigherOrEqual(13)) {
                                 modifier = AttributeUtils.newByKey(
                                    PublicTasks.streamliningKey, streamlining.getValue("speed", maxStreamlining), Operation.ADD_NUMBER
                                 );
                              } else {
                                 modifier = new AttributeModifier("streamlining", streamlining.getValue("speed", maxStreamlining), Operation.ADD_NUMBER);
                              }

                              moveSpeed.addModifier(modifier);
                           }

                           PublicTasks.cachedStreamliningLevel.put(uuid, maxStreamlining);
                        }
                     }
                  }

                  this.counter++;
               }
            })
            .run(),
         0L,
         20L
      );
   }

   public static boolean shouldRun(CustomEnchantment enchant, int level, int counter) {
      return counter * 20 % (int)enchant.getValue("repeat-ticks", level, "20") == 0;
   }

   public static void clearPlayerCache(UUID uuid) {
      cachedThriveLevel.remove(uuid);
      cachedStreamliningLevel.remove(uuid);
   }
}
