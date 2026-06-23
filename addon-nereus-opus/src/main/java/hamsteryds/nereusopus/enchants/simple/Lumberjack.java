package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.listeners.executors.FoliaNeeded;
import hamsteryds.nereusopus.utils.MechanismUtils;
import hamsteryds.nereusopus.utils.WorldUtils;
import hamsteryds.nereusopus.utils.items.ItemUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

public class Lumberjack extends EventExecutor implements FoliaNeeded {
   public HashSet<Material> targets = new HashSet<>();

   public Lumberjack(File file) {
      super(file);

      for (String name : this.getText("targets").split(",")) {
         this.targets.add(ItemUtils.getMaterial(name));
      }
   }

   @Override
   public void blockBreak(int level, BlockBreakEvent event) {
      Player player = event.getPlayer();
      if (!player.isSneaking() || !this.getBool("disable-on-sneak")) {
         if (this.targets.contains(event.getBlock().getType())) {
            int amount = (int)this.getValue("amount", level);
            Location start = event.getBlock().getLocation();
            List<Location> mines = new ArrayList<>();
            Queue<Location> queue = new ConcurrentLinkedQueue<>();
            queue.add(start);

            while (!queue.isEmpty() && mines.size() < amount) {
               Location current = queue.element();
               queue.remove();
               List<Location> locs = new ArrayList<>();

               for (int dx = -1; dx <= 1; dx++) {
                  for (int dy = -1; dy <= 1; dy++) {
                     for (int dz = -1; dz <= 1; dz++) {
                        locs.add(current.clone().add(dx, dy, dz));
                     }
                  }
               }

               for (Location loc : locs) {
                  if (loc.getBlock().getType() == current.getBlock().getType() && !mines.contains(loc)) {
                     queue.add(loc);
                     mines.add(loc);
                  }
               }
            }

            for (Location locx : mines) {
               Block block = locx.getBlock();
               if (MechanismUtils.checkPermission(this, block, player)) {
                  WorldUtils.breakExtraBlock(player, block);
               }
            }
         }
      }
   }
}
