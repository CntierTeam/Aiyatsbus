package hamsteryds.nereusopus.enchants.skill;

import hamsteryds.nereusopus.enchants.internal.entries.SkillEnchantment;
import hamsteryds.nereusopus.utils.WorldUtils;
import java.io.File;
import org.bukkit.entity.Player;

public class Charge extends SkillEnchantment {
   public Charge(File file) {
      super(file);
   }

   @Override
   public void run(Player player, int level) {
      double force = this.getValue("power", level);
      WorldUtils.addVelocity(player, player.getEyeLocation().getDirection().multiply(force), false);
   }
}
