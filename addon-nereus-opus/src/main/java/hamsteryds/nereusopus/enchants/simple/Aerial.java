package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import java.io.File;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Aerial extends EventExecutor {
   public Aerial(File file) {
      super(file);
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      if (event.getDamager() instanceof Projectile projectile && projectile.getShooter() instanceof LivingEntity creature && !creature.isOnGround()) {
         event.setDamage(event.getDamage() * this.getValue("damage-multiplier", level));
      }
   }
}
