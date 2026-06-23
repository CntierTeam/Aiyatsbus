package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import java.io.File;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;

public class Marking extends EventExecutor {
   public static HashMap<UUID, Long> lastAttackStamp = new HashMap<>();

   public Marking(File file) {
      super(file);
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      if (event.getDamager() instanceof Projectile) {
         ProjectileSource source = ((Projectile)event.getDamager()).getShooter();
         if (source instanceof Entity) {
            LivingEntity creature = (LivingEntity)source;
            if (lastAttackStamp.containsKey(creature.getUniqueId())) {
               long stamp = lastAttackStamp.get(creature.getUniqueId());
               if (System.currentTimeMillis() - stamp <= this.getValue("duration", level) * 50.0) {
                  event.setDamage(event.getDamage() * this.getValue("damage-multiplier", level));
               }
            }

            lastAttackStamp.put(creature.getUniqueId(), System.currentTimeMillis());
         }
      }
   }
}
