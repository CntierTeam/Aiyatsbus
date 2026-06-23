package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import java.io.File;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Force extends EventExecutor {
   public Force(File file) {
      super(file);
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      double multiplier = this.getValue("damage-multiplier", level);
      if (event.getDamager() instanceof Arrow) {
         if (this.getBool("disable-on-players", true) && event.getEntity() instanceof Player) {
            return;
         }

         event.setDamage(event.getDamage() * multiplier);
      }
   }
}
