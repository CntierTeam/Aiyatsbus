package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import java.io.File;
import org.bukkit.event.entity.EntityRegainHealthEvent;

public class Rejuvenation extends EventExecutor {
   public Rejuvenation(File file) {
      super(file);
   }

   @Override
   public void regainHealth(int level, EntityRegainHealthEvent event) {
      event.setAmount(event.getAmount() * this.getValue("heal-multiplier", level));
   }
}
