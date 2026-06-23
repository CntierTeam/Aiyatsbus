package hamsteryds.nereusopus.enchants.simple;

import taboolib.library.xseries.XPotion;
import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.listeners.executors.FoliaNeeded;
import java.io.File;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Paralyze extends EventExecutor implements FoliaNeeded {
   public Paralyze(File file) {
      super(file);
   }

   @Override
   public void damagedByEntity(int level, EntityDamageByEntityEvent event) {
      if (event.getEntity() instanceof Player && event.getDamager() instanceof LivingEntity) {
         Player player = (Player)event.getEntity();
         LivingEntity creature = (LivingEntity)event.getDamager();
         if (player.isBlocking()) {
            creature.addPotionEffect(
               XPotion.MINING_FATIGUE.buildPotionEffect((int)this.getValue("duration", level), (int)this.getValue("amplifier", level) - 1)
            );
         }
      }
   }
}
