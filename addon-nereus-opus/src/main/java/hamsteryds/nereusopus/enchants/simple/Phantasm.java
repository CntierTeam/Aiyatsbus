package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import java.io.File;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Wither;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Phantasm extends EventExecutor {
   public Phantasm(File file) {
      super(file);
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      if (event.getEntity() instanceof Zombie
         || event.getEntity() instanceof Skeleton
         || event.getEntity() instanceof Wither
         || event.getEntity() instanceof Phantom
         || event.getEntity() instanceof WitherSkeleton) {
         event.setDamage(event.getDamage() * this.getValue("damage-multiplier", level));
      }
   }
}
