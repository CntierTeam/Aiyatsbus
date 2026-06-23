package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.FoliaUtils;
import java.io.File;
import org.bukkit.event.entity.EntityShootBowEvent;

public class Marksman extends EventExecutor {
   public Marksman(File file) {
      super(file);
   }

   @Override
   public void shootBow(int level, EntityShootBowEvent event) {
      event.getProjectile().setGravity(false);
      FoliaUtils.runDelayed(event.getProjectile(), task -> event.getProjectile().setGravity(true), (long)this.getValue("duration", level));
   }
}
