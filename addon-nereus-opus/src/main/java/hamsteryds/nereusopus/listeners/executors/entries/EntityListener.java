package hamsteryds.nereusopus.listeners.executors.entries;

import com.destroystokyo.paper.event.entity.EntityJumpEvent;
import hamsteryds.nereusopus.listeners.executors.ActionType;
import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.InventoryUtils;
import hamsteryds.nereusopus.utils.NBTUtils;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class EntityListener implements Listener {
   public static HashMap<UUID, ItemStack> projectiles = new HashMap<>();

   @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
   public void onDamage(EntityDamageEvent event) {
      EventExecutor.execute(event.getEntity(), ActionType.DAMAGED, event, InventoryUtils.ALL);
      Entity entity = event.getEntity();
      if (NBTUtils.has("xray", entity.getPersistentDataContainer(), PersistentDataType.STRING)) {
         event.setCancelled(true);
      }
   }

   @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
   public void onTarget(EntityTargetLivingEntityEvent event) {
      EventExecutor.execute(event.getTarget(), ActionType.BE_TARGETED, event, InventoryUtils.ALL);
   }

   @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
   public void onSpawnItem(ItemSpawnEvent event) {
      Item item = event.getEntity();
      if (item.getThrower() != null) {
         NBTUtils.write("thrown", item.getPersistentDataContainer(), PersistentDataType.INTEGER, 1);
      }
   }

   @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
   public void onDamageByBlock(EntityDamageByBlockEvent event) {
      EventExecutor.execute(event.getEntity(), ActionType.DAMAGED_BY_BLOCK, event, InventoryUtils.ALL);
   }

   @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
   public void onDamageByEntity(EntityDamageByEntityEvent event) {
      Entity damager = event.getDamager();
      Entity damagee = event.getEntity();
      if (damager instanceof Projectile projectile) {
         if (projectile.getShooter() instanceof LivingEntity) {
            LivingEntity creature = (LivingEntity)projectile.getShooter();
            damager = creature;
         }

         if (projectile instanceof AbstractArrow || projectile instanceof FishHook) {
            EventExecutor.execute(damager, projectiles.get(projectile.getUniqueId()), ActionType.ATTACK_ENTITY, event, EquipmentSlot.HAND);
            PersistentDataContainer pdc = projectile.getPersistentDataContainer();
            double shotAssist = NBTUtils.has("shot_assist", pdc, PersistentDataType.DOUBLE)
               ? NBTUtils.read("shot_assist", pdc, PersistentDataType.DOUBLE)
               : 1.0;
            event.setDamage(event.getDamage() * shotAssist);
         }

         if (projectile instanceof Trident trident) {
            EventExecutor.execute(damager, trident.getItem(), ActionType.ATTACK_ENTITY, event, EquipmentSlot.HAND);
         }
      } else {
         EventExecutor.execute(damager, ActionType.ATTACK_ENTITY, event, InventoryUtils.HANDS);
      }

      EventExecutor.execute(damagee, ActionType.DAMAGED_BY_ENTITY, event, InventoryUtils.ALL);
   }

   @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
   public void onLaunchProjectile(EntityShootBowEvent event) {
      EventExecutor.execute(event.getEntity(), ActionType.SHOOT_BOW, event, InventoryUtils.ALL);
      projectiles.put(event.getProjectile().getUniqueId(), event.getBow());
   }

   @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
   public void onDeath(EntityDeathEvent event) {
      EventExecutor.execute(event.getEntity(), ActionType.DEATH, event, InventoryUtils.ALL);
   }

   @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
   public void onJump(EntityJumpEvent event) {
      EventExecutor.execute(event.getEntity(), ActionType.ENTITY_JUMP, event, InventoryUtils.ALL);
   }

   @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
   public void onProjectileLaunch(ProjectileLaunchEvent event) {
      if (event.getEntity().getShooter() instanceof LivingEntity) {
         LivingEntity shooter = (LivingEntity)event.getEntity().getShooter();
         EventExecutor.execute(shooter, ActionType.PROJECTILE_LAUNCH, event, InventoryUtils.HANDS);
      }
   }

   @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
   public void onRegainHealth(EntityRegainHealthEvent event) {
      EventExecutor.execute(event.getEntity(), ActionType.REGAIN_HEALTH, event, InventoryUtils.ALL);
   }

   @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
   public void onProjectileHit(ProjectileHitEvent event) {
      if (event.getEntity().getShooter() instanceof LivingEntity) {
         LivingEntity shooter = (LivingEntity)event.getEntity().getShooter();
         if (event.getHitBlock() != null) {
            EventExecutor.execute(shooter, ActionType.PROJECTILE_HIT_BLOCK, event, InventoryUtils.HANDS);
         }

         if (event.getHitEntity() != null) {
            EventExecutor.execute(shooter, ActionType.PROJECTILE_HIT_ENTITY, event, InventoryUtils.HANDS);
         }
      }
   }

   @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
   public void onKill(EntityDeathEvent event) {
      if (event.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent attackedEvent) {
         if (attackedEvent.getDamager() instanceof LivingEntity) {
            LivingEntity creature = (LivingEntity)attackedEvent.getDamager();
            EventExecutor.execute(creature, ActionType.KILL, event, InventoryUtils.HANDS);
         }

         if (attackedEvent.getDamager() instanceof Projectile projectile && projectile.getShooter() instanceof LivingEntity creature) {
            EventExecutor.execute(creature, ActionType.KILL, event, InventoryUtils.HANDS);
         }
      }
   }
}
