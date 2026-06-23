package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.FoliaUtils;
import hamsteryds.nereusopus.utils.WorldUtils;
import java.io.File;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.util.Vector;

public class Rapid extends EventExecutor {
   public Rapid(File file) {
      super(file);
   }

   @Override
   public void shootBow(int level, EntityShootBowEvent event) {
      AbstractArrow arrow = (AbstractArrow)event.getProjectile();
      Vector origin = arrow.getVelocity();
      double velocity = this.getValue("velocity", level);
      boolean direct = this.getBool("direct", false);
      if (this.getBool("delay", true)) {
         FoliaUtils.runDelayed(arrow, task -> {
            if (direct) {
               arrow.setVelocity(origin.multiply(velocity));
            } else {
               WorldUtils.addVelocity(arrow, origin.multiply(velocity), false);
            }
         }, 1L);
      } else if (direct) {
         arrow.setVelocity(origin.multiply(velocity));
      } else {
         WorldUtils.addVelocity(arrow, origin.multiply(velocity), false);
      }
   }
}
