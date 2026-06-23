package hamsteryds.nereusopus.enchants.skill;

import hamsteryds.nereusopus.enchants.internal.entries.SkillEnchantment;
import java.io.File;
import org.bukkit.entity.Player;

public class Vitalize extends SkillEnchantment {
   public Vitalize(File file) {
      super(file);
   }

   @Override
   public void run(Player player, int level) {
      player.setHealth(player.getMaxHealth());
   }
}
