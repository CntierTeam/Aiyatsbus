package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.listeners.executors.FoliaNeeded;
import hamsteryds.nereusopus.utils.WorldUtils;
import java.io.File;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

public class Annihilate extends EventExecutor implements FoliaNeeded {
   public Annihilate(File file) {
      super(file);
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      if (event.getDamager() instanceof LivingEntity) {
         LivingEntity creature = (LivingEntity)event.getDamager();
         Vector velocity = creature.getEyeLocation().getDirection().multiply(this.getValue("velocity", level));
         WorldUtils.addVelocity(event.getEntity(), velocity, true);
      }
   }
}
