package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.FoliaUtils;
import hamsteryds.nereusopus.utils.items.ItemUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

public class Replenish extends EventExecutor {
   public List<Material> blacklist = new ArrayList<>();

   public Replenish(File file) {
      super(file);

      for (String name : this.getText("blacklist").split(",")) {
         this.blacklist.add(ItemUtils.getMaterial(name));
      }
   }

   @Override
   public void blockBreak(int level, BlockBreakEvent event) {
      Player player = event.getPlayer();
      if (!player.isSneaking() || !this.getBool("disable-on-sneak")) {
         Block block = event.getBlock();
         BlockData data = block.getBlockData();
         Material type = block.getType();
         if (!this.blacklist.contains(type)) {
            if (data instanceof Ageable ageable) {
               if (type.toString().contains("BERR") || type == Material.SUGAR_CANE) {
                  if (type == Material.GLOW_BERRIES) {
                     event.setCancelled(true);
                  }

                  return;
               }

               if (ageable.getAge() != ageable.getMaximumAge()) {
                  event.setDropItems(false);
                  event.setExpToDrop(0);
                  ageable.setAge(0);
                  FoliaUtils.runDelayed(block, task -> {
                     block.setType(type);
                     block.setBlockData(ageable);
                  }, 1L);
               }

               ageable.setAge(0);
               FoliaUtils.runDelayed(block, task -> {
                  block.setType(type);
                  block.setBlockData(ageable);
               }, 1L);
            }
         }
      }
   }
}
