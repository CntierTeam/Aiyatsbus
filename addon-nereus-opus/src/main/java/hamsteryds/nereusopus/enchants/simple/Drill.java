package hamsteryds.nereusopus.enchants.simple;

import taboolib.library.xseries.particles.XParticle;
import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.listeners.executors.FoliaNeeded;
import hamsteryds.nereusopus.utils.MechanismUtils;
import hamsteryds.nereusopus.utils.WorldUtils;
import java.io.File;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.util.Vector;

public class Drill extends EventExecutor implements FoliaNeeded {
   public Drill(File file) {
      super(file);
   }

   @Override
   public void blockBreak(int level, BlockBreakEvent event) {
      Player player = event.getPlayer();
      if (!player.isSneaking() || !this.getBool("disable-on-sneak")) {
         if (this.getBool("enable-sound")) {
            player.spawnParticle(XParticle.EXPLOSION_EMITTER.get(), player.getLocation(), 1);
         }

         double range = this.getValue("range", level);
         Vector direction = player.getEyeLocation().getDirection().normalize();
         direction.setX((float)Math.round(direction.getX()));
         direction.setY((float)Math.round(direction.getY()));
         direction.setZ((float)Math.round(direction.getZ()));
         Location loc = event.getBlock().getLocation().add(0.5, 0.5, 0.5);

         for (int i = 1; i <= range; i++) {
            Location current = loc.clone().add(direction.multiply(i));
            Block block = current.getBlock();
            if (MechanismUtils.checkPermission(this, block, player) && !this.getText("hardness-check").contains(block.getType().getKey().getKey())) {
               WorldUtils.breakExtraBlock(player, block);
            }
         }
      }
   }
}
