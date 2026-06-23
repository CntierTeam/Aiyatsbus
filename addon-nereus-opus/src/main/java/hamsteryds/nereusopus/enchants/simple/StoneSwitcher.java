package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.FoliaUtils;
import hamsteryds.nereusopus.utils.items.ItemUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class StoneSwitcher extends EventExecutor {
   public List<Material> drops = new ArrayList<>();
   public HashSet<Material> targets = new HashSet<>();

   public StoneSwitcher(File file) {
      super(file);

      for (String name : this.getText("drops").split(",")) {
         this.drops.add(ItemUtils.getMaterial(name));
      }

      for (String name : this.getText("targets").split(",")) {
         this.targets.add(ItemUtils.getMaterial(name));
      }
   }

   @Override
   public void blockBreak(int level, BlockBreakEvent event) {
      if (!event.isCancelled()) {
         if (this.targets.contains(event.getBlock().getType())) {
            event.setDropItems(false);
            FoliaUtils.runTask(
               event.getBlock(),
               t -> event.getBlock()
                  .getWorld()
                  .dropItem(event.getBlock().getLocation(), new ItemStack(this.drops.get((int)(this.drops.size() * Math.random()))))
            );
         }
      }
   }
}
