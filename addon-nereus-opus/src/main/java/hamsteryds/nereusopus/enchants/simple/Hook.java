package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.listeners.executors.FoliaNeeded;
import hamsteryds.nereusopus.utils.WorldUtils;
import java.io.File;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.util.Vector;

public class Hook extends EventExecutor implements FoliaNeeded {
   public Hook(File file) {
      super(file);
   }

   @Override
   public void fish(int level, PlayerFishEvent event) {
      FishHook hook = event.getHook();
      if (hook.getHookedEntity() instanceof LivingEntity) {
         LivingEntity creature = (LivingEntity)hook.getHookedEntity();
         Vector direction = event.getPlayer().getLocation().subtract(creature.getLocation()).toVector().normalize();
         direction = direction.multiply(this.getValue("velocity", level));
         WorldUtils.addVelocity(creature, direction, true);
      }
   }
}
