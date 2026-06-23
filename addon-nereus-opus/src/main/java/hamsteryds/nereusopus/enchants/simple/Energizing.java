package hamsteryds.nereusopus.enchants.simple;

import taboolib.library.xseries.XPotion;
import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.listeners.executors.FoliaNeeded;
import java.io.File;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

public class Energizing extends EventExecutor implements FoliaNeeded {
   public Energizing(File file) {
      super(file);
   }

   @Override
   public void blockBreak(int level, BlockBreakEvent event) {
      int duration = (int)this.getValue("duration", level);
      int amplifier = (int)this.getValue("amplifier", level);
      Player player = event.getPlayer();
      player.addPotionEffect(XPotion.HASTE.buildPotionEffect(duration, amplifier - 1));
   }
}
