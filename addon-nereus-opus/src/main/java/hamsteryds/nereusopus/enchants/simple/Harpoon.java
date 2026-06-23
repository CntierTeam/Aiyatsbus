package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.listeners.executors.FoliaNeeded;
import hamsteryds.nereusopus.utils.PermissionUtils;
import java.io.File;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.PlayerFishEvent;

public class Harpoon extends EventExecutor implements FoliaNeeded {
   public Harpoon(File file) {
      super(file);
   }

   @Override
   public void fish(int level, PlayerFishEvent event) {
      if (event.getCaught() instanceof LivingEntity) {
         LivingEntity creature = (LivingEntity)event.getCaught();
         if (!PermissionUtils.hasDamagePermission(event.getPlayer(), creature)) {
            return;
         }

         creature.damage(this.getValue("damage", level), event.getPlayer());
      }
   }
}
