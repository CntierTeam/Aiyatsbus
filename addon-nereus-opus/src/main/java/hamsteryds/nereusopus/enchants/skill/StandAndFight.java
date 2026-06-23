package hamsteryds.nereusopus.enchants.skill;

import taboolib.library.xseries.XPotion;
import hamsteryds.nereusopus.enchants.internal.entries.SkillEnchantment;
import java.io.File;
import org.bukkit.entity.Player;

public class StandAndFight extends SkillEnchantment {
   public StandAndFight(File file) {
      super(file);
   }

   @Override
   public void run(Player player, int level) {
      int duration = (int)this.getValue("duration", level);
      int amplifier = (int)(this.getValue("amplifier", level) - 1.0);
      player.damage(0.1);
      player.setHealth(player.getHealth() * 0.5);
      player.addPotionEffect(XPotion.STRENGTH.buildPotionEffect(duration, amplifier));
      player.addPotionEffect(XPotion.SPEED.buildPotionEffect(duration, amplifier));
   }
}
