package hamsteryds.nereusopus.enchants.skill;

import hamsteryds.nereusopus.enchants.internal.entries.SkillEnchantment;
import hamsteryds.nereusopus.utils.WorldUtils;
import java.io.File;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Ascend extends SkillEnchantment {
   public Ascend(File file) {
      super(file);
   }

   @Override
   public void run(Player player, int level) {
      double force = Math.pow(Math.log(this.getValue("power")), 1.0 / (Math.max(this.getBasicData().getMaxLevel() - level, 0) + 2));
      Vector direction = new Vector(0.0, force, 0.0);
      direction = direction.add(player.getEyeLocation().getDirection().multiply(0.5));
      WorldUtils.addVelocity(player, direction, false);
   }
}
