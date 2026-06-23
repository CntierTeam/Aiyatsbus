package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.listeners.executors.FoliaNeeded;
import java.io.File;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;

public class Shadowless extends EventExecutor implements FoliaNeeded {
   public Shadowless(File file) {
      super(file);
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      int duration = (int)this.getValue("duration", level);
      int amplifier = (int)this.getValue("amplifier", level);
      if (event.getEntity() instanceof LivingEntity && event.getDamager() instanceof LivingEntity) {
         LivingEntity creature = (LivingEntity)event.getEntity();
         LivingEntity damager = (LivingEntity)event.getDamager();
         if (event.getDamage() >= creature.getHealth()) {
            damager.addPotionEffect(PotionEffectType.INVISIBILITY.createEffect(duration, amplifier - 1));
         }
      }
   }
}
