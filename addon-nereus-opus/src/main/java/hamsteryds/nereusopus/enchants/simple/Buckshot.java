package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.listeners.executors.FoliaNeeded;
import hamsteryds.nereusopus.utils.FoliaUtils;
import java.io.File;
import org.bukkit.Location;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.util.Vector;

public class Buckshot extends EventExecutor implements FoliaNeeded {
   public Buckshot(File file) {
      super(file);
   }

   @Override
   public void shootBow(int level, EntityShootBowEvent event) {
      AbstractArrow arrow = (AbstractArrow)event.getProjectile();
      Location loc = arrow.getLocation();
      Vector origin = arrow.getVelocity();
      double spread = this.getValue("spread", level);

      for (int i = 0; i < this.getValue("amount", level); i++) {
         Arrow extra = arrow.getWorld()
            .spawnArrow(
               loc, origin.clone().add(new Vector(spread * Math.random(), spread * Math.random(), spread * Math.random())), (float)origin.length(), 0.0F
            );
         extra.setShooter(arrow.getShooter());
         FoliaUtils.runTask(extra, task -> extra.setPickupStatus(PickupStatus.DISALLOWED));
      }
   }
}
