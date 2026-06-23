package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.stats.MathUtils;
import java.io.File;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class Invigoration extends EventExecutor {
   public Invigoration(File file) {
      super(file);
   }

   @Override
   public void damaged(int level, EntityDamageEvent event) {
      if (event.getEntity() instanceof LivingEntity) {
         LivingEntity creature = (LivingEntity)event.getEntity();
         double heal = MathUtils.calculate(this.getText("min-health"), "level", level + "", "maxBlood", creature.getMaxHealth());
         if (creature.getHealth() <= heal) {
            event.setDamage(event.getDamage() * this.getValue("del-damage-multiplier", level, "1-0.2*level"));
         }
      }
   }

   @Override
   public void damagedByEntity(int level, EntityDamageByEntityEvent event) {
      if (event.getDamager() instanceof Player && event.getEntity() instanceof LivingEntity) {
         Player player = (Player)event.getDamager();
         double heal = MathUtils.calculate(this.getText("min-health"), "level", level + "", "maxBlood", player.getMaxHealth());
         if (player.getHealth() <= heal) {
            event.setDamage(event.getDamage() * this.getValue("add-damage-multiplier", level, "1+0.2*level"));
         }
      }
   }
}
