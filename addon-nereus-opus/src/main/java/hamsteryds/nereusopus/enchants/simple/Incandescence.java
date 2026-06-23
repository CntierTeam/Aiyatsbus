package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.listeners.executors.FoliaNeeded;
import java.io.File;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Incandescence extends EventExecutor implements FoliaNeeded {
   public Incandescence(File file) {
      super(file);
   }

   @Override
   public void damagedByEntity(int level, EntityDamageByEntityEvent event) {
      event.getDamager().setFireTicks((int)this.getValue("duration", level));
   }
}
