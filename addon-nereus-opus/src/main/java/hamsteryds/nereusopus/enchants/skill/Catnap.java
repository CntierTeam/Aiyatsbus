package hamsteryds.nereusopus.enchants.skill;

import hamsteryds.nereusopus.enchants.internal.entries.SkillEnchantment;
import hamsteryds.nereusopus.utils.FoliaUtils;
import java.io.File;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class Catnap extends SkillEnchantment {
   public Catnap(File file) {
      super(file);
   }

   public static void confuse(Player player) {
      for (Entity var2 : player.getNearbyEntities(20.0, 20.0, 20.0)) {
         ;
      }
   }

   @Override
   public void run(Player player, int level) {
      int range = (int)this.getValue("range", level);
      int duration = (int)this.getValue("duration", level);
      FoliaUtils.runTask(player, task -> {
         for (Entity entity : player.getNearbyEntities(range, range, range)) {
            if (entity instanceof LivingEntity creature) {
               creature.addPotionEffect(PotionEffectType.BLINDNESS.createEffect(duration, 4));
            }
         }
      });
   }
}
