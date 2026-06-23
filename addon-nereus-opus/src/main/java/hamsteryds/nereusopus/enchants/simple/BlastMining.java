package hamsteryds.nereusopus.enchants.simple;

import cc.polarastrum.aiyatsbus.core.compat.AntiGriefChecker;
import taboolib.library.xseries.particles.XParticle;
import hamsteryds.nereusopus.NereusOpus;
import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.WorldUtils;
import hamsteryds.nereusopus.utils.items.ItemUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

public class BlastMining extends EventExecutor {
   public List<Material> blacklist = new ArrayList<>();

   public BlastMining(File file) {
      super(file);

      for (String name : this.getText("blacklist").split(",")) {
         this.blacklist.add(ItemUtils.getMaterial(name));
      }
   }

   @Override
   public void blockBreak(int level, BlockBreakEvent event) {
      if (!NereusOpus.getInstance().isFolia()) {
         Player player = event.getPlayer();
         if (!player.isSneaking() || !this.getBool("disable-on-sneak")) {
            if (this.getBool("enable-sound")) {
               player.spawnParticle(XParticle.EXPLOSION_EMITTER.get(), player.getLocation(), 1);
            }

            double rangeX = this.getValue("range_x", level);
            double rangeY = this.getValue("range_y", level);
            double rangeZ = this.getValue("range_z", level);
            List<Location> breaks = new ArrayList<>();

            for (double x = -rangeX + 1.0; x < rangeX; x++) {
               for (double y = -rangeY + 1.0; y < rangeY; y++) {
                  for (double z = -rangeZ + 1.0; z < rangeZ; z++) {
                     Location loc = event.getBlock().getLocation().add(x, y, z);
                     Block block = loc.getBlock();
                     Material material = block.getType();
                     if (!this.blacklist.contains(material)
                        && material != Material.AIR
                        && !this.getText("hardness-check").contains(material.getKey().getKey())
                        && AntiGriefChecker.INSTANCE.canBreak(player, loc)) {
                        breaks.add(loc);
                     }
                  }
               }
            }

            WorldUtils.fastMutilBreak(player, breaks, this.getInt("per-tick", 12), this);
         }
      }
   }
}
