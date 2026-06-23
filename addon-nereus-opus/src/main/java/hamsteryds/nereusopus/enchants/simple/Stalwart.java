package hamsteryds.nereusopus.enchants.simple;

import taboolib.library.xseries.XPotion;
import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.listeners.executors.FoliaNeeded;
import java.io.File;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;

public class Stalwart extends EventExecutor implements FoliaNeeded {
   public Stalwart(File file) {
      super(file);
   }

   @Override
   public void damaged(int level, EntityDamageEvent event) {
      if (event.getEntity() instanceof LivingEntity) {
         LivingEntity creature = (LivingEntity)event.getEntity();
         creature.addPotionEffect(XPotion.RESISTANCE.buildPotionEffect((int)this.getValue("duration", level), (int)this.getValue("amplifier", level) - 1));
      }
   }
}
