package hamsteryds.nereusopus.enchants.skill;

import taboolib.library.xseries.XPotion;
import hamsteryds.nereusopus.enchants.internal.entries.SkillEnchantment;
import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SelfRealization extends SkillEnchantment {
   public static List<PotionEffectType> debuffs = Stream.of(
         XPotion.BLINDNESS,
         XPotion.SLOWNESS,
         XPotion.POISON,
         XPotion.MINING_FATIGUE,
         XPotion.NAUSEA,
         XPotion.HUNGER,
         XPotion.LEVITATION,
         XPotion.WEAKNESS,
         XPotion.WITHER,
         XPotion.BAD_OMEN,
         XPotion.INSTANT_DAMAGE,
         XPotion.UNLUCK
      )
      .<PotionEffectType>map(XPotion::getPotionEffectType)
      .filter(Objects::nonNull)
      .toList();

   public SelfRealization(File file) {
      super(file);
   }

   @Override
   public void run(Player player, int level) {
      for (PotionEffect effect : player.getActivePotionEffects()) {
         if (debuffs.contains(effect.getType())) {
            player.removePotionEffect(effect.getType());
         }
      }
   }
}
