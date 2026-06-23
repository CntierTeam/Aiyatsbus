package hamsteryds.nereusopus.enchants.skill;

import hamsteryds.nereusopus.enchants.internal.entries.SkillEnchantment;
import hamsteryds.nereusopus.utils.FoliaUtils;
import hamsteryds.nereusopus.utils.PermissionUtils;
import java.io.File;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class Quake extends SkillEnchantment {
   public Quake(File file) {
      super(file);
   }

   @Override
   public void run(Player player, int level) {
      double range = this.getValue("range", level);
      double damage = this.getValue("damage", level);
      FoliaUtils.runTask(player, task -> {
         for (Entity entity : player.getNearbyEntities(range, range, range)) {
            if (entity instanceof LivingEntity creature) {
               if (!PermissionUtils.hasDamagePermission(player, creature)) {
                  return;
               }

               creature.damage(damage, player);
            }
         }
      });
   }
}
