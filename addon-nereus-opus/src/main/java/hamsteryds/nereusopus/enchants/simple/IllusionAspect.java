package hamsteryds.nereusopus.enchants.simple;

import taboolib.library.xseries.XPotion;
import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.listeners.executors.FoliaNeeded;
import java.io.File;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class IllusionAspect extends EventExecutor implements FoliaNeeded {
   public IllusionAspect(File file) {
      super(file);
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      if (event.getEntity() instanceof LivingEntity) {
         LivingEntity creature = (LivingEntity)event.getEntity();
         creature.addPotionEffect(XPotion.BLINDNESS.buildPotionEffect((int)this.getValue("duration", level), (int)this.getValue("amplifier", level) - 1));
         creature.addPotionEffect(XPotion.NAUSEA.buildPotionEffect((int)this.getValue("duration", level), (int)this.getValue("amplifier", level) - 1));
      }
   }
}
