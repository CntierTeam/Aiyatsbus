package hamsteryds.nereusopus.listeners.executors;

import cc.polarastrum.aiyatsbus.core.AiyatsbusEnchantment;
import cc.polarastrum.aiyatsbus.core.AiyatsbusUtilsKt;
import cc.polarastrum.aiyatsbus.core.data.CheckType;
import cc.polarastrum.aiyatsbus.impl.LevelFixer;
import hamsteryds.nereusopus.NereusOpus;
import hamsteryds.nereusopus.enchants.EnchantmentLoader;
import hamsteryds.nereusopus.enchants.internal.CustomEnchantment;
import hamsteryds.nereusopus.enchants.internal.entries.SkillEnchantment;
import hamsteryds.nereusopus.listeners.executors.entries.CheckListeners;
import hamsteryds.nereusopus.utils.DebugUtils;
import hamsteryds.nereusopus.utils.FoliaUtils;
import hamsteryds.nereusopus.utils.InventoryUtils;
import hamsteryds.nereusopus.utils.MechanismUtils;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public abstract class EventExecutor extends CustomEnchantment implements EventFunctions {
   private static final HashMap<ActionType, Method> executors = new HashMap<>();
   public static List<ActionType> types = List.of(ActionType.DAMAGED_BY_ENTITY, ActionType.BLOCK_BREAK);
   private static int stamp = 0;

   public EventExecutor(File file) {
      super(file);
   }

   public static void initialize() {
      try {
         for (Method method : EventFunctions.class.getDeclaredMethods()) {
            char[] chars = method.getName().toCharArray();
            StringBuilder actionName = new StringBuilder();

            for (char letter : chars) {
               if (letter < 'Z' && letter > '@') {
                  actionName.append("_");
                  actionName.append(letter);
               } else {
                  actionName.append(Character.toChars(letter - ' '));
               }
            }

            executors.put(ActionType.valueOf(actionName.toString()), method);
         }
      } catch (Exception var10) {
         var10.printStackTrace();
      }

      FoliaUtils.runAtFixedRate(task -> {
         stamp += 20;

         for (Player player : Bukkit.getOnlinePlayers()) {
            execute(player, ActionType.TICK_TASK, null, InventoryUtils.ALL);
         }
      }, 0L, 20L);
   }

   public static void execute(Entity tmp, ActionType type, Event event, EquipmentSlot... slots) {
      if (tmp instanceof LivingEntity entity) {
         HashMap<EquipmentSlot, ItemStack> equippedItems = InventoryUtils.getEquippedItems(entity);
         if (equippedItems.size() != 0) {
            for (EquipmentSlot slot : slots) {
               ItemStack item = equippedItems.get(slot);
               execute(tmp, item, type, event, slot);
            }
         }
      }
   }

   public static void execute(Entity tmp, ItemStack item, ActionType type, Event event, EquipmentSlot slot) {
      if (item != null) {
         if (item.getItemMeta() != null) {
            if (!CheckListeners.events.contains(event)) {
               if (tmp instanceof LivingEntity) {
                  if (tmp instanceof Player player && event != null && types.contains(type) && !item.getType().toString().contains("HOE")) {
                     if (!MechanismUtils.checkCooldown(player, type + ":" + slot.toString(), 0.15, false)) {
                        return;
                     }

                     MechanismUtils.addCooldown(player, type + ":" + slot);
                  }

                  LivingEntity entity = (LivingEntity)tmp;
                  if (!((item.getType() == Material.BOW || item.getType() == Material.CROSSBOW) && event instanceof EntityDamageByEntityEvent evt)
                     || evt.getDamager() instanceof AbstractArrow) {
                     Object[][] enchants = AiyatsbusUtilsKt.getFastFixedEnchants(item);
                     if (LevelFixer.INSTANCE.fix(item, enchants)) {
                        enchants = AiyatsbusUtilsKt.getFastFixedEnchants(item);
                     }

                     for (Object[] obj : enchants) {
                        AiyatsbusEnchantment ench = (AiyatsbusEnchantment)obj[0];
                        int level = (Integer)obj[1];
                        CustomEnchantment enchantment = EnchantmentLoader.byKey.get(ench.getBasicData().getId());
                        if (enchantment instanceof EventExecutor executor
                           && executor.getLimitations().checkAvailable(CheckType.USE, item, entity, slot, false).isSuccess()
                           && executor.getBasicData().getEnable()
                           && MechanismUtils.checkPercent(executor, level)
                           && MechanismUtils.checkRequireFull(executor, entity)
                           && MechanismUtils.checkShiftIgnored(executor, entity)) {
                           DebugUtils.debug(enchantment.toString(), entity);
                           if (NereusOpus.getInstance().isFolia() && (executor instanceof FoliaNeeded || executor instanceof SkillEnchantment)) {
                              if (event != null) {
                                 executor.call(level, type, entity, event);
                              } else {
                                 executor.call(level, slot, (Player)entity, type);
                              }
                           } else if (event != null) {
                              executor.call(level, type, entity, event);
                           } else {
                              executor.call(level, slot, (Player)entity, type);
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public void call(int level, ActionType type, LivingEntity entity, Event event) {
      try {
         executors.get(type).invoke(this, level, event);
         executors.get(ActionType.TRIGGER).invoke(this, level, type, event, entity);
      } catch (InvocationTargetException | IllegalAccessException var6) {
         var6.printStackTrace();
      }
   }

   public void call(int level, EquipmentSlot slot, Player player, ActionType type) {
      try {
         executors.get(type).invoke(this, level, slot, player, stamp);
         executors.get(ActionType.TRIGGER).invoke(this, level, type, null, player);
      } catch (InvocationTargetException | IllegalAccessException var6) {
         var6.printStackTrace();
      }
   }
}
