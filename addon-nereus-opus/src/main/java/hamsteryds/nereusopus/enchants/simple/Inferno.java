package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.listeners.executors.FoliaNeeded;
import java.io.File;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Inferno extends EventExecutor implements FoliaNeeded {
   public Inferno(File file) {
      super(file);
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      event.getEntity().setFireTicks((int)this.getValue("duration", level));
   }
}
