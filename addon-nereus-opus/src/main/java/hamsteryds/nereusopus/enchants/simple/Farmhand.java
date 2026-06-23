package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.FoliaUtils;
import hamsteryds.nereusopus.utils.MechanismUtils;
import java.io.File;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public class Farmhand extends EventExecutor {
   public Farmhand(File file) {
      super(file);
   }

   @Override
   public void interactRightBlock(int level, PlayerInteractEvent event) {
      Player player = event.getPlayer();
      if (!player.isSneaking() || !this.getBool("disable-on-sneak")) {
         double range = this.getValue("range", level);

         for (double x = -range + 1.0; x < range; x++) {
            for (double z = -range + 1.0; z < range; z++) {
               Location loc = event.getClickedBlock().getLocation().add(x, 0.0, z);
               Block block = loc.getBlock();
               if ((block.getType() == Material.DIRT || block.getType() == Material.GRASS_BLOCK) && MechanismUtils.checkPermission(this, block, player)) {
                  FoliaUtils.runTask(block, t -> block.setType(Material.FARMLAND));
               }
            }
         }
      }
   }
}
