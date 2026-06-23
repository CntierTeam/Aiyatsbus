package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.listeners.executors.FoliaNeeded;
import hamsteryds.nereusopus.utils.DebugUtils;
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

public class Vein extends EventExecutor implements FoliaNeeded {
   public HashSet<Material> targets = new HashSet<>();

   public Vein(File file) {
      super(file);

      for (String name : this.getText("targets").split(",")) {
         this.targets.add(ItemUtils.getMaterial(name));
      }
   }

   @Override
   public void blockBreak(int level, BlockBreakEvent event) {
      Player player = event.getPlayer();
      DebugUtils.debug("\u5224\u5b9a\u5230\u73a9\u5bb6\u7834\u574f\u65b9\u5757\uff01", player);
      if (!player.isSneaking() || !this.getBool("disable-on-sneak")) {
         DebugUtils.debug("\u5224\u5b9a\u5230\u73a9\u5bb6\u672a\u8e72\u4e0b\uff01", player);
         if (this.targets.contains(event.getBlock().getType())) {
            DebugUtils.debug("\u5224\u5b9a\u5230\u65b9\u5757\u662f\u77ff\u7269", player);
            int amount = (int)this.getValue("amount", level);
            Location start = event.getBlock().getLocation();
            List<Location> mines = new ArrayList<>();
            Queue<Location> queue = new ConcurrentLinkedQueue<>();
            queue.add(start);

            while (!queue.isEmpty() && mines.size() < amount) {
               Location current = queue.element();
               queue.remove();
               List<Location> locs = new ArrayList<>();
               locs.add(current.clone().add(0.0, 1.0, 0.0));
               locs.add(current.clone().add(0.0, -1.0, 0.0));
               locs.add(current.clone().add(1.0, 0.0, 0.0));
               locs.add(current.clone().add(-1.0, 0.0, 0.0));
               locs.add(current.clone().add(0.0, 0.0, -1.0));
               locs.add(current.clone().add(0.0, 0.0, 1.0));
               locs.add(current.clone().add(1.0, 1.0, 0.0));
               locs.add(current.clone().add(-1.0, -1.0, 0.0));
               locs.add(current.clone().add(1.0, -1.0, 0.0));
               locs.add(current.clone().add(-1.0, 1.0, 0.0));
               locs.add(current.clone().add(0.0, 1.0, -1.0));
               locs.add(current.clone().add(0.0, -1.0, 1.0));
               locs.add(current.clone().add(0.0, -1.0, -1.0));
               locs.add(current.clone().add(0.0, 1.0, 1.0));
               locs.add(current.clone().add(1.0, 0.0, -1.0));
               locs.add(current.clone().add(-1.0, 0.0, 1.0));
               locs.add(current.clone().add(-1.0, 0.0, -1.0));
               locs.add(current.clone().add(1.0, 0.0, 1.0));

               for (Location loc : locs) {
                  if (loc.getBlock().getType() == current.getBlock().getType() && !mines.contains(loc)) {
                     queue.add(loc);
                     mines.add(loc);
                  }
               }
            }

            for (Location locx : mines) {
               if (!locx.equals(event.getBlock().getLocation())) {
                  Block block = locx.getBlock();
                  DebugUtils.debug("\u8fde\u9501\u65b9\u5757:" + locx, player);
                  if (!MechanismUtils.checkPermission(this, block, player)) {
                     DebugUtils.debug("\u6743\u9650\u68c0\u67e5\u5931\u8d25!", player);
                  } else {
                     WorldUtils.breakExtraBlock(player, block);
                  }
               }
            }
         }
      }
   }
}
