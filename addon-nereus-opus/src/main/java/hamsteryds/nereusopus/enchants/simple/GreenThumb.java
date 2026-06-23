package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.FoliaUtils;
import hamsteryds.nereusopus.utils.MechanismUtils;
import java.io.File;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerInteractEvent;

public class GreenThumb extends EventExecutor {
   public GreenThumb(File file) {
      super(file);
   }

   @Override
   public void interactLeftBlock(int level, PlayerInteractEvent event) {
      Block block = event.getClickedBlock();
      if (MechanismUtils.checkPermission(this, block, event.getPlayer())) {
         if (block.getType() == Material.DIRT) {
            FoliaUtils.runTask(block, t -> block.setType(Material.GRASS_BLOCK));
            event.setCancelled(true);
         }
      }
   }
}
