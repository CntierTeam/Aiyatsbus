package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.listeners.executors.FoliaNeeded;
import java.io.File;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Swallow extends EventExecutor implements FoliaNeeded {
   public Swallow(File file) {
      super(file);
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      int food = (int)this.getValue("food", level);
      if (event.getDamager() instanceof Player player) {
         player.setFoodLevel(player.getFoodLevel() + food);
      }
   }
}
