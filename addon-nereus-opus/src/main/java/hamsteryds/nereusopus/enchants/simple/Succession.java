package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.FoliaUtils;
import java.io.File;
import org.bukkit.Location;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.util.Vector;

public class Succession extends EventExecutor {
   public Succession(File file) {
      super(file);
   }

   @Override
   public void shootBow(int level, EntityShootBowEvent event) {
      AbstractArrow arrow = (AbstractArrow)event.getProjectile();
      Location loc = arrow.getLocation();
      Vector origin = arrow.getVelocity();

      for (int i = 0; i < this.getValue("amount", level); i++) {
         Arrow extra = arrow.getWorld()
            .spawnArrow(loc.clone().add(Math.random() * 0.3, Math.random() * 0.3, Math.random() * 0.3), origin, (float)origin.length(), 0.0F);
         FoliaUtils.runTask(extra, task -> {
            extra.setShooter(arrow.getShooter());
            extra.setPickupStatus(PickupStatus.DISALLOWED);
         });
      }
   }
}
