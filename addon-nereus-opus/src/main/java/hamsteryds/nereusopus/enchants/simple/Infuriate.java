package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.FoliaUtils;
import java.io.File;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Infuriate extends EventExecutor {
   public Infuriate(File file) {
      super(file);
   }

   @Override
   public void damagedByEntity(int level, EntityDamageByEntityEvent event) {
      if (event.getEntity() instanceof Player && event.getDamager() instanceof LivingEntity) {
         Player player = (Player)event.getEntity();
         LivingEntity creature = (LivingEntity)event.getDamager();
         if (player.isBlocking()) {
            double range = this.getValue("range", level);
            FoliaUtils.runTask(event.getDamager(), task -> {
               for (Entity entity : event.getDamager().getNearbyEntities(range, range, range)) {
                  if (entity instanceof Mob mob) {
                     mob.setTarget(creature);
                  }
               }
            });
         }
      }
   }
}
