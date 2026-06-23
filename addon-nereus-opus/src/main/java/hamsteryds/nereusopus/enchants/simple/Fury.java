package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.FoliaUtils;
import java.io.File;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Fury extends EventExecutor {
   public boolean onlyPlayer = this.getBool("only_player", true);

   public Fury(File file) {
      super(file);
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      if (event.getEntity() instanceof LivingEntity creature) {
         if (this.onlyPlayer || creature.getType() != EntityType.PLAYER) {
            return;
         }

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
