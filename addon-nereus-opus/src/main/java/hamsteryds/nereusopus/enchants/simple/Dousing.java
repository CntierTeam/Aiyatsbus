package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.FoliaUtils;
import java.io.File;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.event.entity.ProjectileHitEvent;

public class Dousing extends EventExecutor {
   public Dousing(File file) {
      super(file);
   }

   @Override
   public void projectileHitBlock(int level, ProjectileHitEvent event) {
      if (event.getEntity() instanceof AbstractArrow) {
         Block block = event.getHitBlock();
         Location loc = block.getLocation().add(event.getHitBlockFace().getDirection());
         Block fire = loc.getBlock();
         if (fire.getType() == Material.FIRE || fire.getType() == Material.SOUL_FIRE) {
            FoliaUtils.runTask(fire, t -> fire.setType(Material.AIR));
         }
      }
   }
}
