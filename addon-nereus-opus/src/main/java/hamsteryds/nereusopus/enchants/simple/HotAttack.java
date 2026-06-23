package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import java.io.File;
import org.bukkit.block.Block;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class HotAttack extends EventExecutor {
   public HotAttack(File file) {
      super(file);
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      Block block = event.getEntity().getLocation().getBlock();
      if (block.getTemperature() >= 1.0) {
         event.setDamage(event.getDamage() * this.getValue("damage-multiplier", level));
      }
   }
}
