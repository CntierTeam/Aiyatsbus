package hamsteryds.nereusopus.enchants.curse;

import hamsteryds.nereusopus.enchants.PublicTasks;
import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.ItemUtils;
import java.io.File;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class DecayCurse extends EventExecutor {
   public int tick;

   public DecayCurse(File file) {
      super(file);
      PublicTasks.registerTaskEnchant(this, this.getClass());
   }

   public void run(Player player, int slot, int level) {
      PlayerInventory inv = player.getInventory();
      if (inv.getHeldItemSlot() != slot) {
         ItemStack item = inv.getItem(slot);

         assert item != null;

         inv.setItem(slot, ItemUtils.addDurability(item, (int)this.getValue("durability-decrease", level)));
      }
   }
}
