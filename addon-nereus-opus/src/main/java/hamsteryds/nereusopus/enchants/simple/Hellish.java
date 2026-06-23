package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import java.io.File;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Hellish extends EventExecutor {
   public Hellish(File file) {
      super(file);
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      World world = event.getEntity().getWorld();
      if (world.getEnvironment() == Environment.NETHER) {
         event.setDamage(event.getDamage() * this.getValue("damage-multiplier", level));
      }
   }
}
