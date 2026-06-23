package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import java.io.File;
import org.bukkit.World;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Nocturnal extends EventExecutor {
   public Nocturnal(File file) {
      super(file);
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      World world = event.getEntity().getWorld();
      if (world.getTime() >= 13000L || world.getTime() <= 500L) {
         event.setDamage(event.getDamage() * this.getValue("damage-multiplier", level));
      }
   }
}
