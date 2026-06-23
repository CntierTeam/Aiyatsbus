package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.FoliaUtils;
import java.io.File;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class HealingCloseCombat extends EventExecutor {
   public HealingCloseCombat(File file) {
      super(file);
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      double heal = this.getValue("heal", level);
      if (event.getEntity() instanceof LivingEntity creature && creature.getType() == EntityType.PLAYER) {
         event.setDamage(0.0);
         FoliaUtils.runTask(creature, t -> {
            creature.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 3, 1));
            creature.setHealth(Math.min(creature.getHealth() + heal, creature.getMaxHealth()));
         });
      }
   }
}
