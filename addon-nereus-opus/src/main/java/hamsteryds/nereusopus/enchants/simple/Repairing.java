package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.enchants.PublicTasks;
import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.listeners.executors.FoliaNeeded;
import hamsteryds.nereusopus.utils.ItemUtils;
import java.io.File;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Repairing extends EventExecutor implements FoliaNeeded {
   public Repairing(File file) {
      super(file);
      PublicTasks.registerTaskEnchant(this, this.getClass());
   }

   public void run(Player player, int slot, int level) {
      PlayerInventory inv = player.getInventory();
      if (inv.getHeldItemSlot() != slot) {
         ItemStack item = inv.getItem(slot);

         assert item != null;

         inv.setItem(slot, ItemUtils.addDurability(item, -this.getValue("durability-increase", level)));
      }
   }
}
