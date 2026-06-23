package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.listeners.executors.FoliaNeeded;
import hamsteryds.nereusopus.utils.items.ItemUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Trident;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;

public class Spearfishing extends EventExecutor implements FoliaNeeded {
   public List<Material> drops = new ArrayList<>();

   public Spearfishing(File file) {
      super(file);

      for (String name : this.getText("drops").split(",")) {
         this.drops.add(ItemUtils.getMaterial(name));
      }
   }

   @Override
   public void projectileHitBlock(int level, ProjectileHitEvent event) {
      if (event.getEntity() instanceof Trident) {
         Trident trident = (Trident)event.getEntity();
         Block last = event.getHitBlock().getLocation().add(event.getHitBlockFace().getDirection()).getBlock();
         if (last.isLiquid()) {
            trident.getWorld().dropItem(trident.getLocation(), new ItemStack(this.drops.get((int)(this.drops.size() * Math.random()))));
         }
      }
   }
}
