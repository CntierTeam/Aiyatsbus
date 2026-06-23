package hamsteryds.nereusopus.enchants.curse;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import java.io.File;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class HarmlessnessCurse extends EventExecutor {
   public HarmlessnessCurse(File file) {
      super(file);
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      event.setCancelled(true);
   }
}
