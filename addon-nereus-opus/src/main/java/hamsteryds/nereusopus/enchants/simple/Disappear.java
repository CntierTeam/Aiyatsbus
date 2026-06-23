package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.FoliaUtils;
import java.io.File;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.potion.PotionEffectType;

public class Disappear extends EventExecutor {
   public Disappear(File file) {
      super(file);
   }

   @Override
   public void tickTask(int level, EquipmentSlot slot, Player player, int stamp) {
      if (player.getHealth() / player.getMaxHealth() <= this.getValue("min-health-percent", level)) {
         FoliaUtils.runTask(player, task -> player.addPotionEffect(PotionEffectType.INVISIBILITY.createEffect(100, 0)));
      }
   }
}
