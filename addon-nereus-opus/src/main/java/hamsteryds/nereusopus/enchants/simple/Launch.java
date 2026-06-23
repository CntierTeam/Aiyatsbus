package hamsteryds.nereusopus.enchants.simple;

import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent;
import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.FoliaUtils;
import hamsteryds.nereusopus.utils.WorldUtils;
import java.io.File;
import org.bukkit.entity.Player;

public class Launch extends EventExecutor {
   public Launch(File file) {
      super(file);
   }

   @Override
   public void elytraBoost(int level, PlayerElytraBoostEvent event) {
      double velocity = this.getValue("velocity", level);
      Player player = event.getPlayer();
      FoliaUtils.runDelayed(player, task -> WorldUtils.addVelocity(player, player.getVelocity().multiply(velocity), false), 1L);
   }
}
