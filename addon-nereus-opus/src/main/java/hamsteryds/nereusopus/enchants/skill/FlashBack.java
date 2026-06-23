package hamsteryds.nereusopus.enchants.skill;

import taboolib.library.xseries.particles.XParticle;
import hamsteryds.nereusopus.enchants.internal.entries.SkillEnchantment;
import hamsteryds.nereusopus.utils.FoliaUtils;
import java.io.File;
import java.util.Collection;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class FlashBack extends SkillEnchantment {
   public FlashBack(File file) {
      super(file);
   }

   @Override
   public void run(Player player, int level) {
      Location loc = player.getLocation();
      double health = player.getHealth();
      int foodLevel = player.getFoodLevel();
      Collection<PotionEffect> buffs = player.getActivePotionEffects();
      int duration = (int)this.getValue("duration", level);
      player.getWorld().spawnParticle(XParticle.EXPLOSION_EMITTER.get(), loc, 1);
      FoliaUtils.runDelayed(player, task -> {
         if (player.isOnline()) {
            FoliaUtils.teleport(player, loc);
            player.setHealth(health);
            player.setFoodLevel(foodLevel);
            player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
            player.addPotionEffects(buffs);
            player.sendTitle("\u00a77", "\u00a7a\u56de\u6eaf\u6210\u529f\uff01");
         }
      }, (long)duration);
   }
}
