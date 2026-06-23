package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.MechanismUtils;
import java.io.File;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Criticals extends EventExecutor {
   public Criticals(File file) {
      super(file);
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      if (MechanismUtils.checkCritical(event.getDamager())) {
         event.setDamage(event.getDamage() * this.getValue("damage-multiplier", level));
      }
   }
}
