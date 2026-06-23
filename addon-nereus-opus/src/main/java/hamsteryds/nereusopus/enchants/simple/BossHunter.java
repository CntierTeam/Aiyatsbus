package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import java.io.File;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class BossHunter extends EventExecutor {
   public BossHunter(File file) {
      super(file);
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      if (event.getEntity().getType() == EntityType.ENDER_DRAGON || event.getEntity().getType() == EntityType.WITHER) {
         event.setDamage(event.getDamage() * this.getValue("damage-multiplier", level));
      }
   }
}
