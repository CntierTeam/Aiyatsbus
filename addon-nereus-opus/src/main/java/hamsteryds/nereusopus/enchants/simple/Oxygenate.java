package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.listeners.executors.FoliaNeeded;
import java.io.File;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

public class Oxygenate extends EventExecutor implements FoliaNeeded {
   public Oxygenate(File file) {
      super(file);
   }

   @Override
   public void blockBreak(int level, BlockBreakEvent event) {
      Player player = event.getPlayer();
      if (player.isInWater()) {
         int added = (int)(player.getRemainingAir() + this.getValue("oxygen", level) * 20.0);
         player.setRemainingAir(Math.min(added, player.getMaximumAir()));
      }
   }
}
