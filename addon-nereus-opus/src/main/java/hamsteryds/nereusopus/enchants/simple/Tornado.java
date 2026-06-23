package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.FoliaUtils;
import hamsteryds.nereusopus.utils.WorldUtils;
import java.io.File;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

public class Tornado extends EventExecutor {
   public Tornado(File file) {
      super(file);
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      if (event.getDamager() instanceof LivingEntity && event.getEntity().isOnGround()) {
         Vector velocity = new Vector(0, 1, 0).multiply(this.getValue("velocity", level));
         FoliaUtils.runDelayed(event.getEntity(), task -> WorldUtils.addVelocity(event.getEntity(), velocity, true), 1L);
      }
   }
}
