package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.listeners.executors.FoliaNeeded;
import java.io.File;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;

public class Blind extends EventExecutor implements FoliaNeeded {
   public Blind(File file) {
      super(file);
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      double duration = this.getValue("duration", level);
      double amplifier = this.getValue("amplifier", level);
      if (event.getEntity() instanceof LivingEntity) {
         LivingEntity creature = (LivingEntity)event.getEntity();
         creature.addPotionEffect(PotionEffectType.BLINDNESS.createEffect((int)duration, (int)(amplifier - 1.0)));
      }
   }
}
