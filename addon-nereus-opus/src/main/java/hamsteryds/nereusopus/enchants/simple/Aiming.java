package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.listeners.executors.FoliaNeeded;
import hamsteryds.nereusopus.utils.FoliaUtils;
import hamsteryds.nereusopus.utils.PermissionUtils;
import hamsteryds.nereusopus.utils.WorldUtils;
import java.io.File;
import org.bukkit.Location;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.util.Vector;

public class Aiming extends EventExecutor implements FoliaNeeded {
   public Aiming(File file) {
      super(file);
   }

   @Override
   public void shootBow(int level, EntityShootBowEvent event) {
      if (!this.getBool("require-full-charge") || !(event.getForce() < 1.0F)) {
         double range = this.getValue("range", level);
         long ticks = (long)this.getValue("repeat-ticks", level);
         AbstractArrow arrow = (AbstractArrow)event.getProjectile();
         Entity who = event.getEntity();
         arrow.setGlowing(true);
         arrow.setShooter(event.getEntity());
         FoliaUtils.runAtFixedRate(
            arrow,
            task -> {
               if (arrow.isDead()) {
                  task.cancel();
               } else if (arrow.isInBlock()) {
                  task.cancel();
               } else {
                  for (Entity entity : arrow.getNearbyEntities(range, range, range)) {
                     if (entity.getUniqueId() != who.getUniqueId()
                        && entity instanceof LivingEntity livingEntity
                        && !(entity instanceof ArmorStand)
                        && !PermissionUtils.checkIfIsNPC(entity)
                        && livingEntity.hasLineOfSight(who)) {
                        LivingEntity creature = (LivingEntity)entity;
                        Location arrowLoc = arrow.getLocation();
                        Location destination = creature.getLocation();
                        Vector vector = destination.subtract(arrowLoc).toVector().normalize();
                        Vector origin = arrow.getVelocity();
                        if (origin.add(vector.multiply(origin.length() / 2.0)).length() < 5.0) {
                           WorldUtils.addVelocity(arrow, origin.add(vector.multiply(origin.length() / 2.0)), false);
                        }
                        break;
                     }
                  }
               }
            },
            1L,
            ticks
         );
      }
   }
}
