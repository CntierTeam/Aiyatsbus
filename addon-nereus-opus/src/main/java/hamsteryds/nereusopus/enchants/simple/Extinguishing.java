package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.FoliaUtils;
import java.io.File;
import org.bukkit.event.entity.EntityDamageEvent;

public class Extinguishing extends EventExecutor {
   public Extinguishing(File file) {
      super(file);
   }

   @Override
   public void damaged(int level, EntityDamageEvent event) {
      if (event.getEntity().getFireTicks() > 0 && event.getCause().toString().contains("FIRE")) {
         event.setCancelled(true);
         event.setCancelled(true);
         FoliaUtils.runTask(event.getEntity(), t -> event.getEntity().setFireTicks(0));
      }
   }
}
