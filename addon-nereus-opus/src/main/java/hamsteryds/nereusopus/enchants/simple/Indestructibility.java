package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import java.io.File;
import org.bukkit.event.player.PlayerItemDamageEvent;

public class Indestructibility extends EventExecutor {
   public Indestructibility(File file) {
      super(file);
   }

   @Override
   public void itemDamage(int level, PlayerItemDamageEvent event) {
      event.setCancelled(true);
   }
}
