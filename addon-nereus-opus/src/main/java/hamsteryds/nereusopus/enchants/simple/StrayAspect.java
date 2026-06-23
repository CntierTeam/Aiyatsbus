package hamsteryds.nereusopus.enchants.simple;

import taboolib.library.xseries.XPotion;
import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.listeners.executors.FoliaNeeded;
import hamsteryds.nereusopus.utils.TeamUtils;
import java.io.File;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class StrayAspect extends EventExecutor implements FoliaNeeded {
   public StrayAspect(File file) {
      super(file);
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      if (event.getEntity() instanceof LivingEntity) {
         LivingEntity creature = (LivingEntity)event.getEntity();
         LivingEntity damager = (LivingEntity)event.getDamager();
         if (creature instanceof Player && event.getDamager() instanceof Player && TeamUtils.isTeammate((Player)creature, (Player)damager)) {
            return;
         }

         creature.addPotionEffect(XPotion.SLOWNESS.buildPotionEffect((int)this.getValue("duration", level), (int)this.getValue("amplifier", level) - 1));
      }
   }
}
