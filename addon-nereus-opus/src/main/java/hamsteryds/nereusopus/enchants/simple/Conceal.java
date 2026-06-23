package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import java.io.File;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

public class Conceal extends EventExecutor {
   public Conceal(File file) {
      super(file);
   }

   @Override
   public void beTargeted(int level, EntityTargetLivingEntityEvent event) {
      Entity entity = event.getEntity();
      if (event.getReason() == TargetReason.CLOSEST_ENTITY) {
         try {
            double distance = entity.getLocation().distance(event.getTarget().getLocation());
            double range = this.getValue("range", level);
            if (distance >= range) {
               event.setCancelled(true);
            }
         } catch (Exception var8) {
         }
      }
   }
}
