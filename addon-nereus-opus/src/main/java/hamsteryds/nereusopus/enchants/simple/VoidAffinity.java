package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import java.io.File;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class VoidAffinity extends EventExecutor {
   public VoidAffinity(File file) {
      super(file);
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      World world = event.getEntity().getWorld();
      if (world.getEnvironment() == Environment.THE_END) {
         event.setDamage(event.getDamage() * this.getValue("damage-multiplier", level));
      }
   }
}
