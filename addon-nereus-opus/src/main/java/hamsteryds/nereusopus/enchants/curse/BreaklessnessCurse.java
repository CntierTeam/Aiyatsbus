package hamsteryds.nereusopus.enchants.curse;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import java.io.File;
import org.bukkit.event.block.BlockDamageEvent;

public class BreaklessnessCurse extends EventExecutor {
   public BreaklessnessCurse(File file) {
      super(file);
   }

   @Override
   public void blockDamage(int level, BlockDamageEvent event) {
      event.setCancelled(true);
   }
}
