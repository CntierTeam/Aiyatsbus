package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.listeners.executors.FoliaNeeded;
import hamsteryds.nereusopus.utils.stats.MathUtils;
import java.io.File;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Extract extends EventExecutor implements FoliaNeeded {
   public Extract(File file) {
      super(file);
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      if (event.getDamager() instanceof LivingEntity) {
         LivingEntity creature = (LivingEntity)event.getDamager();
         double damage = event.getDamage();
         double heal = MathUtils.calculate(this.getText("heal"), "level", level, "damage", damage);
         creature.setHealth(Math.min(creature.getHealth() + heal, creature.getMaxHealth()));
      }

      if (event.getDamager() instanceof Projectile projectile && projectile.getShooter() instanceof LivingEntity creature) {
         double damage = event.getDamage();
         double heal = MathUtils.calculate(this.getText("heal"), "level", level + "", "damage", damage + "");
         creature.setHealth(Math.min(creature.getHealth() + heal, creature.getMaxHealth()));
      }
   }
}
