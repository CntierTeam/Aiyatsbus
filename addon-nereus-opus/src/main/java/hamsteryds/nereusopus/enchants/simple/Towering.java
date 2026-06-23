package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.stats.MathUtils;
import java.io.File;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Towering extends EventExecutor {
   public Towering(File file) {
      super(file);
   }

   @Override
   public void damagedByEntity(int level, EntityDamageByEntityEvent event) {
      if (event.getEntity() instanceof LivingEntity) {
         LivingEntity creature = (LivingEntity)event.getEntity();
         String text = this.getText("health-judging-line", "maxBlood", String.format("%.2f", creature.getMaxHealth()));
         double judgeHealth = MathUtils.calculate(text, "level", level);
         double highMultiplier = this.getValue("high-damage-multiplier", level);
         double lowMultiplier = this.getValue("low-damage-multiplier", level);
         if (creature.getHealth() > judgeHealth) {
            event.setDamage(event.getDamage() * highMultiplier);
         } else {
            event.setDamage(event.getDamage() * lowMultiplier);
         }
      }
   }
}
