package hamsteryds.nereusopus.enchants.simple;

import taboolib.library.xseries.XPotion;
import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.listeners.executors.FoliaNeeded;
import java.io.File;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Frenzy extends EventExecutor implements FoliaNeeded {
   public Frenzy(File file) {
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
            damager.addPotionEffect(XPotion.STRENGTH.buildPotionEffect(duration, amplifier - 1));
         }
      }
   }
}
