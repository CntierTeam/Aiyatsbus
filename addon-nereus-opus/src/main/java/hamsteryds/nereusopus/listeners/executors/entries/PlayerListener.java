package hamsteryds.nereusopus.listeners.executors.entries;

import taboolib.library.xseries.XAttribute;
import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent;
import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent;
import hamsteryds.nereusopus.ConfigReader;
import hamsteryds.nereusopus.enchants.EnchantmentLoader;
import hamsteryds.nereusopus.enchants.PublicTasks;
import hamsteryds.nereusopus.enchants.internal.CustomEnchantment;
import hamsteryds.nereusopus.enchants.simple.Injure;
import hamsteryds.nereusopus.listeners.executors.ActionType;
import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.InventoryUtils;
import hamsteryds.nereusopus.utils.stats.Pair;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;

public class PlayerListener implements Listener {
   @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
   public void onDamage(PlayerItemDamageEvent event) {
      EventExecutor.execute(event.getPlayer(), ActionType.ITEM_DAMAGE, event, InventoryUtils.HANDS);
   }

   @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
   public void onInteractEntity(PlayerInteractAtEntityEvent event) {
      Player player = event.getPlayer();
      EventExecutor.execute(player, ActionType.INTERACT_ENTITY, event, InventoryUtils.HANDS);
   }

   @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
   public void onFish(PlayerFishEvent event) {
      Player player = event.getPlayer();
      EventExecutor.execute(player, ActionType.FISH, event, InventoryUtils.HANDS);
   }

   @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
   public void onInteract(PlayerInteractEntityEvent event) {
      Player player = event.getPlayer();
      EventExecutor.execute(player, ActionType.INTERACT_RIGHT, new PlayerInteractEvent(player, Action.RIGHT_CLICK_AIR, null, null, null), InventoryUtils.HANDS);
   }

   @EventHandler(priority = EventPriority.LOWEST)
   public void onJoin(PlayerJoinEvent event) {
      Player player = event.getPlayer();
      if (ConfigReader.bloodFixer) {
         player.resetMaxHealth();
         player.getAttribute((Attribute)XAttribute.MAX_HEALTH.get())
            .getModifiers()
            .forEach(modifier -> player.getAttribute((Attribute)XAttribute.MAX_HEALTH.get()).removeModifier(modifier));
      }

      if (ConfigReader.speedFixer) {
         player.setWalkSpeed(0.2F);
         player.getAttribute((Attribute)XAttribute.MOVEMENT_SPEED.get())
            .getModifiers()
            .forEach(modifier -> player.getAttribute((Attribute)XAttribute.MOVEMENT_SPEED.get()).removeModifier(modifier));
      }
   }

   @EventHandler(priority = EventPriority.LOWEST)
   public void onQuit(PlayerQuitEvent event) {
      Player player = event.getPlayer();
      AttributeInstance maxHealth = player.getAttribute((Attribute)XAttribute.MAX_HEALTH.get());

      assert maxHealth != null;

      maxHealth.getModifiers().forEach(modifier -> {
         if (modifier.getName().equalsIgnoreCase("thrive")) {
            maxHealth.removeModifier(modifier);
         }
      });
      AttributeInstance moveSpeed = player.getAttribute((Attribute)XAttribute.MOVEMENT_SPEED.get());

      assert moveSpeed != null;

      moveSpeed.getModifiers().forEach(modifier -> {
         if (modifier.getName().equalsIgnoreCase("streamlining")) {
            moveSpeed.removeModifier(modifier);
         }
      });
      PublicTasks.clearPlayerCache(player.getUniqueId());
   }

   @EventHandler(priority = EventPriority.HIGH)
   public void onInteract(PlayerInteractEvent event) {
      Player player = event.getPlayer();
      if (event.getHand() != EquipmentSlot.OFF_HAND) {
         if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            EventExecutor.execute(player, ActionType.INTERACT_LEFT_BLOCK, event, InventoryUtils.HANDS);
            EventExecutor.execute(player, ActionType.INTERACT_LEFT, event, InventoryUtils.HANDS);
         } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            EventExecutor.execute(player, ActionType.INTERACT_RIGHT_BLOCK, event, InventoryUtils.HANDS);
            EventExecutor.execute(player, ActionType.INTERACT_RIGHT, event, InventoryUtils.HANDS);
         } else if (event.getAction() == Action.LEFT_CLICK_AIR) {
            EventExecutor.execute(player, ActionType.INTERACT_LEFT_AIR, event, InventoryUtils.HANDS);
            EventExecutor.execute(player, ActionType.INTERACT_LEFT, event, InventoryUtils.HANDS);
         } else if (event.getAction() == Action.RIGHT_CLICK_AIR) {
            EventExecutor.execute(player, ActionType.INTERACT_RIGHT_AIR, event, InventoryUtils.HANDS);
            EventExecutor.execute(player, ActionType.INTERACT_RIGHT, event, InventoryUtils.HANDS);
         }
      }
   }

   @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
   public void onJump(PlayerJumpEvent event) {
      EventExecutor.execute(event.getPlayer(), ActionType.PLAYER_JUMP, event, InventoryUtils.ALL);
   }

   @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
   public void onElytraBoost(PlayerElytraBoostEvent event) {
      EventExecutor.execute(event.getPlayer(), ActionType.ELYTRA_BOOST, event, InventoryUtils.ARMORS);
   }

   @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
   public void onHunger(FoodLevelChangeEvent event) {
      if (event.getFoodLevel() <= event.getEntity().getFoodLevel()) {
         EventExecutor.execute(event.getEntity(), ActionType.HUNGER, event, InventoryUtils.ALL);
      }
   }

   @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
   public void onPickUpExp(PlayerPickupExperienceEvent event) {
      EventExecutor.execute(event.getPlayer(), ActionType.PICK_UP_EXPERIENCE, event, InventoryUtils.ALL);
   }

   @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
   public void onHeldItem(PlayerItemHeldEvent event) {
      Player player = event.getPlayer();
      player.getAttribute((Attribute)XAttribute.ATTACK_SPEED.get()).setBaseValue(4.0);
      EventExecutor.execute(player, player.getInventory().getItem(event.getNewSlot()), ActionType.ITEM_HELD, event, EquipmentSlot.HAND);
   }

   @EventHandler(priority = EventPriority.LOWEST)
   public void onRegainHealth(EntityRegainHealthEvent event) {
      if (event.getEntity() instanceof LivingEntity creature && Injure.stamps.containsKey(creature.getUniqueId())) {
         Pair<Long, Integer> value = Injure.stamps.get(creature.getUniqueId());
         CustomEnchantment enchant = EnchantmentLoader.byKey.get("injure");
         if (System.currentTimeMillis() - value.getFirst() > 50.0 * enchant.getValue("duration", value.getSecond())) {
            return;
         }

         event.setAmount(event.getAmount() * Math.max(enchant.getValue("heal-multiplier", value.getSecond()), 0.0));
      }
   }
}
