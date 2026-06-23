package hamsteryds.nereusopus.enchants.simple;

import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent;
import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.FoliaUtils;
import java.io.File;
import org.bukkit.entity.ExperienceOrb;

public class Wisdom extends EventExecutor {
   public Wisdom(File file) {
      super(file);
   }

   @Override
   public void pickUpExperience(int level, PlayerPickupExperienceEvent event) {
      ExperienceOrb entity = event.getExperienceOrb();
      int experience = entity.getExperience();
      double multiplier = this.getValue("exp-multiplier", level);
      FoliaUtils.runTask(entity, t -> entity.setExperience((int)(experience * multiplier)));
   }
}
