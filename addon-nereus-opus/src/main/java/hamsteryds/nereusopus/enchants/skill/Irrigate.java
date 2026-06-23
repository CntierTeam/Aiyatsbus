package hamsteryds.nereusopus.enchants.skill;

import hamsteryds.nereusopus.enchants.internal.entries.SkillEnchantment;
import java.io.File;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

public class Irrigate extends SkillEnchantment {
   public Irrigate(File file) {
      super(file);
   }

   @Override
   public void run(Player player, int level) {
      RayTraceResult result = player.rayTraceBlocks(30.0);
      if (result == null) {
         player.sendTitle("\u00a7c\u2716", "\u00a7c\u4f60\u6ca1\u6709\u6307\u5411\u4efb\u4f55\u65b9\u5757\uff01");
      } else {
         Block block = result.getHitBlock();
         if (block == null) {
            player.sendTitle("\u00a7c\u2716", "\u00a7c\u4f60\u6ca1\u6709\u6307\u5411\u4efb\u4f55\u65b9\u5757\uff01");
            return;
         }

         if (block.getBlockData() instanceof Ageable) {
            Ageable ageable = (Ageable)block.getBlockData();
            ageable.setAge(ageable.getMaximumAge());
            block.setBlockData(ageable);
         }
      }
   }
}
