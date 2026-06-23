package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.listeners.executors.FoliaNeeded;
import hamsteryds.nereusopus.utils.PermissionUtils;
import java.io.File;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Deflection extends EventExecutor implements FoliaNeeded {
   public Deflection(File file) {
      super(file);
   }

   @Override
   public void damagedByEntity(int level, EntityDamageByEntityEvent event) {
      if (event.getEntity() instanceof Player && event.getDamager() instanceof LivingEntity) {
         Player player = (Player)event.getEntity();
         LivingEntity creature = (LivingEntity)event.getDamager();
         if (player.isBlocking()) {
            if (!PermissionUtils.hasDamagePermission(player, creature)) {
               return;
            }

            creature.damage(event.getDamage() * this.getValue("percent", level) / 100.0);
            player.setCooldown(Material.SHIELD, (int)(this.getValue("cooldown", level) * 20.0));
         }
      }
   }
}
