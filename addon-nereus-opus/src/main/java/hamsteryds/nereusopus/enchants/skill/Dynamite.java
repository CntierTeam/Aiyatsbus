package hamsteryds.nereusopus.enchants.skill;

import cc.polarastrum.aiyatsbus.core.compat.AntiGriefChecker;
import taboolib.library.xseries.particles.XParticle;
import hamsteryds.nereusopus.enchants.internal.entries.SkillEnchantment;
import hamsteryds.nereusopus.utils.WorldUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Dynamite extends SkillEnchantment {
   private static Class<?> blockTileEntity = null;

   public Dynamite(File file) {
      super(file);
   }

   @Override
   public void run(Player player, int level) {
      player.spawnParticle(XParticle.EXPLOSION_EMITTER.get(), player.getLocation(), 1);
      double range = this.getValue("range", level);
      List<Location> locations = new ArrayList<>();

      for (double x = -range; x <= range; x++) {
         for (double y = -range; y <= range; y++) {
            for (double z = -range; z <= range; z++) {
               Location loc = player.getLocation().add(x, y, z);
               Block block = loc.getBlock();
               if (!this.getText("hardness-check").contains(block.getType().getKey().getKey())
                  && AntiGriefChecker.INSTANCE.canBreak(player, loc)
                  && (blockTileEntity == null || !blockTileEntity.isAssignableFrom(block.getClass()))) {
                  locations.add(loc);
               }
            }
         }
      }

      WorldUtils.fastMutilBreak(player, locations, this.getInt("tick-per", 16), this);
   }

   static {
      try {
         blockTileEntity = Class.forName("net.minecraft.world.level.block.BlockTileEntity");
      } catch (Throwable var1) {
      }
   }
}
