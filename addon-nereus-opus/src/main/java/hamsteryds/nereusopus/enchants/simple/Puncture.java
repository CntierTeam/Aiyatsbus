package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import java.io.File;
import org.bukkit.entity.Shulker;
import org.bukkit.entity.Turtle;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Puncture extends EventExecutor {
   public Puncture(File file) {
      super(file);
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      if (event.getEntity() instanceof Turtle || event.getEntity() instanceof Shulker) {
         event.setDamage(event.getDamage() * this.getValue("damage-multiplier", level));
      }
   }
}
