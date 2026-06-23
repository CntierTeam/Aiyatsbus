package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import java.io.File;
import org.bukkit.event.entity.EntityDamageEvent;

public class Reinforcement extends EventExecutor {
   public Reinforcement(File file) {
      super(file);
   }

   @Override
   public void damaged(int level, EntityDamageEvent event) {
      event.setDamage(event.getDamage() * this.getValue("damage-multiplier", level));
   }
}
