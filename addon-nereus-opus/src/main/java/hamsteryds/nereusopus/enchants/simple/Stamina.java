package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import java.io.File;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class Stamina extends EventExecutor {
   public Stamina(File file) {
      super(file);
   }

   @Override
   public void hunger(int level, FoodLevelChangeEvent event) {
      if (event.getFoodLevel() < event.getEntity().getFoodLevel() && event.getEntity() instanceof Player) {
         Player player = (Player)event.getEntity();
         if (player.isSprinting()) {
            event.setCancelled(true);
         }
      }
   }
}
