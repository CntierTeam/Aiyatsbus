package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.stats.MathUtils;
import java.io.File;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;

public class Optics extends EventExecutor {
   public Optics(File file) {
      super(file);
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      Location damagee = event.getEntity().getLocation();
      Location damager = event.getDamager().getLocation();
      if (event.getDamager() instanceof Projectile) {
         ProjectileSource source = ((Projectile)event.getDamager()).getShooter();
         if (source instanceof Entity) {
            damager = ((Entity)source).getLocation();
         }
      }

      if (damager.getWorld().equals(damagee.getWorld())) {
         event.setDamage(event.getDamage() * MathUtils.calculate(this.getText("damage-multiplier"), "level", level, "dist", damagee.distance(damager)));
      }
   }
}
