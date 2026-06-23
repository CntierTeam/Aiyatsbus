package hamsteryds.nereusopus.enchants.curse;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import java.io.File;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;

public class HungerCurse extends EventExecutor {
   public HungerCurse(File file) {
      super(file);
   }

   @Override
   public void tickTask(int level, EquipmentSlot slot, Player player, int stamp) {
      if (stamp % (int)this.getValue("repeat-ticks") == 0) {
         player.setFoodLevel(Math.max(player.getFoodLevel() - 1, 0));
      }
   }
}
