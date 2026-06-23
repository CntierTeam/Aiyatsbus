package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import java.io.File;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;

public class LuckyCatch extends EventExecutor {
   public LuckyCatch(File file) {
      super(file);
   }

   @Override
   public void fish(int level, PlayerFishEvent event) {
      Entity caught = event.getCaught();
      if (caught instanceof Item caughtItem) {
         if (event.getState() == State.CAUGHT_FISH) {
            if (((Item)caught).getItemStack().getType().getMaxStackSize() >= 2) {
               caughtItem.getItemStack().setAmount(caughtItem.getItemStack().getAmount() * 2);
            }
         }
      }
   }
}
