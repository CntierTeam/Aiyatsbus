package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.FoliaUtils;
import java.io.File;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;

public class Collateral extends EventExecutor {
   public Collateral(File file) {
      super(file);
   }

   @Override
   public void shootBow(int level, EntityShootBowEvent event) {
      AbstractArrow arrow = (AbstractArrow)event.getProjectile();
      FoliaUtils.runTask(arrow, t -> arrow.setPierceLevel(level));
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      event.setDamage(event.getDamage() * this.getValue("damage-multiplier", level));
   }
}
