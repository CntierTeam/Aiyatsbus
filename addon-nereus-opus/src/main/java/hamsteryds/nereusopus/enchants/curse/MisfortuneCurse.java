package hamsteryds.nereusopus.enchants.curse;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import java.io.File;
import org.bukkit.event.block.BlockBreakEvent;

public class MisfortuneCurse extends EventExecutor {
   public MisfortuneCurse(File file) {
      super(file);
   }

   @Override
   public void blockBreak(int level, BlockBreakEvent event) {
      event.setDropItems(false);
      event.setExpToDrop(0);
   }
}
