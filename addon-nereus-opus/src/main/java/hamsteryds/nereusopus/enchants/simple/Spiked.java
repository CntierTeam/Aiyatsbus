package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.listeners.executors.FoliaNeeded;
import hamsteryds.nereusopus.utils.PermissionUtils;
import java.io.File;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.PlayerFishEvent;

public class Spiked extends EventExecutor implements FoliaNeeded {
   public Spiked(File file) {
      super(file);
   }

   @Override
   public void fish(int level, PlayerFishEvent event) {
      FishHook hook = event.getHook();
      if (hook.getHookedEntity() instanceof LivingEntity) {
         LivingEntity creature = (LivingEntity)hook.getHookedEntity();
         if (!PermissionUtils.hasDamagePermission(event.getPlayer(), creature)) {
            return;
         }

         creature.damage(this.getValue("damage", level));
      }
   }
}
