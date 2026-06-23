package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import java.io.File;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Piglin;
import org.bukkit.entity.Strider;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.entity.Zoglin;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class WaterAspect extends EventExecutor {
   public WaterAspect(File file) {
      super(file);
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      if (event.getEntity() instanceof MagmaCube
         || event.getEntity() instanceof Blaze
         || event.getEntity() instanceof Strider
         || event.getEntity() instanceof Piglin
         || event.getEntity() instanceof Zoglin
         || event.getEntity() instanceof WitherSkeleton
         || event.getEntity() instanceof Ghast) {
         event.setDamage(event.getDamage() * this.getValue("damage-multiplier", level));
      }
   }
}
