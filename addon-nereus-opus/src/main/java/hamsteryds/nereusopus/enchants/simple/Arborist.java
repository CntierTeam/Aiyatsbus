package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.ConfigUtils;
import hamsteryds.nereusopus.utils.FoliaUtils;
import java.io.File;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class Arborist extends EventExecutor {
   public List<Material> items = ConfigUtils.getEnumList(this.getConfig().getConfigurationSection("params"), "arborist.yml", "items", Material.class);

   public Arborist(File file) {
      super(file);
   }

   @Override
   public void blockDropItem(int level, BlockDropItemEvent event) {
      Block block = event.getBlock();
      if (block.getType().toString().contains("LEAVES")) {
         FoliaUtils.runTask(
            block, t -> block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(this.items.get((int)(this.items.size() * Math.random()))))
         );
      }
   }
}
