package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.listeners.executors.FoliaNeeded;
import hamsteryds.nereusopus.utils.PermissionUtils;
import java.io.File;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Zeus extends EventExecutor implements FoliaNeeded {
   public Zeus(File file) {
      super(file);
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      if (event.getEntity() instanceof LivingEntity creature && event.getDamager() instanceof Player player) {
         if (!PermissionUtils.hasDamagePermission(player, creature)) {
            return;
         }

         creature.getWorld().strikeLightningEffect(creature.getLocation());
         creature.damage(this.getValue("damage", level));
      }
   }
}
