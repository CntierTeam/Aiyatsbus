package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.listeners.executors.FoliaNeeded;
import java.io.File;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;

public class Levitate extends EventExecutor implements FoliaNeeded {
   public Levitate(File file) {
      super(file);
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      if (event.getEntity() instanceof LivingEntity) {
         LivingEntity creature = (LivingEntity)event.getEntity();
         creature.addPotionEffect(PotionEffectType.LEVITATION.createEffect((int)this.getValue("duration", level), (int)this.getValue("amplifier", level) - 1));
      }
   }
}
