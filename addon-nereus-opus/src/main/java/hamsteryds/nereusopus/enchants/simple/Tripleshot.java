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

public class Tripleshot extends EventExecutor {
   public Tripleshot(File file) {
      super(file);
   }

   @Override
   public void shootBow(int level, EntityShootBowEvent event) {
      AbstractArrow arrow = (AbstractArrow)event.getProjectile();
      Location loc = arrow.getLocation();
      Vector origin = arrow.getVelocity();
      int angle = (int)this.getValue("angle", level);

      for (int i = -1; i <= 1; i++) {
         if (i != 0) {
            Arrow extra = arrow.getWorld().spawnArrow(loc, origin.clone().rotateAroundY(Math.toRadians(angle * i)), (float)origin.length(), 0.0F);
            extra.setShooter(arrow.getShooter());
            FoliaUtils.runTask(extra.getLocation(), task -> extra.setPickupStatus(PickupStatus.DISALLOWED));
         }
      }
   }
}
