package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.enchants.PublicTasks;
import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.listeners.executors.FoliaNeeded;
import hamsteryds.nereusopus.utils.FoliaUtils;
import hamsteryds.nereusopus.utils.PermissionUtils;
import java.io.File;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class Slicing extends EventExecutor implements FoliaNeeded {
   public Slicing(File file) {
      super(file);
      PublicTasks.registerTaskEnchant(this, this.getClass());
   }

   public void run(Player player, int slot, int level) {
      if (slot == 38 && player.isGliding()) {
         double range = this.getValue("range", level);
         FoliaUtils.runTask(player, task -> {
            for (Entity entity : player.getNearbyEntities(range, range, range)) {
               if (entity instanceof LivingEntity creature) {
                  if (!PermissionUtils.hasDamagePermission(player, creature)) {
                     return;
                  }

                  creature.damage(this.getValue("damage-splash", level));
               }
            }
         });
      }
   }
}
