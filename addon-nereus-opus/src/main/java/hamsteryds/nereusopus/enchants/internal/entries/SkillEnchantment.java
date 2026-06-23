package hamsteryds.nereusopus.enchants.internal.entries;

import taboolib.library.xseries.XSound;
import taboolib.module.configuration.Configuration;
import taboolib.module.configuration.Type;
import hamsteryds.nereusopus.NereusOpus;
import hamsteryds.nereusopus.enchants.simple.Silence;
import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.DebugUtils;
import hamsteryds.nereusopus.utils.MechanismUtils;
import hamsteryds.nereusopus.utils.WorldUtils;
import java.io.File;
import java.util.HashMap;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class SkillEnchantment extends EventExecutor {
   public static boolean cdInfoEnabled;
   public static String cdForm;
   public static String cdMessage;
   public static String action;
   public static boolean shiftNeeded;
   public static boolean shiftIgnored;
   public static HashMap<String, Double> rates = new HashMap<>();

   public SkillEnchantment(File file) {
      super(file);
   }

   @Override
   public void interactRight(int level, PlayerInteractEvent event) {
      this.trigger(level, event.getPlayer(), "RIGHT_CLICK");
   }

   @Override
   public void interactLeft(int level, PlayerInteractEvent event) {
      this.trigger(level, event.getPlayer(), "LEFT_CLICK");
   }

   @Override
   public void swapHandItems(int level, PlayerSwapHandItemsEvent event) {
      this.trigger(level, event.getPlayer(), "SWAP");
   }

   public void trigger(int level, Player player, String type) {
      if (type.contains(action)) {
         if (player.isSneaking() || !shiftNeeded) {
            if (!player.isSneaking() || !shiftIgnored) {
               if (Silence.stamps.getOrDefault(player.getUniqueId(), 0L) > System.currentTimeMillis()) {
                  player.sendTitle(
                     "\u00a77",
                     "\u00a7c\u65e0\u6cd5\u91ca\u653e\u4e3b\u52a8\u6280\u80fd\u9644\u9b54\uff0c\u60a8\u88ab\u77ed\u65f6\u95f4\u6c89\u9ed8\u4e86\uff01"
                  );
               } else {
                  DebugUtils.debug("\u68c0\u67e5\u51b7\u5374", player);
                  if (MechanismUtils.checkCooldown(player, this.getBasicData().getName(), this.getValue("cooldown", level), true)) {
                     DebugUtils.debug("\u901a\u8fc7\u51b7\u5374", player);
                     MechanismUtils.addCooldown(player, this.getBasicData().getName());
                     XSound.of(this.getText("sound")).ifPresent(sound -> sound.play(player.getLocation(), 100.0F, 10.0F));
                     player.spawnParticle(
                        WorldUtils.getParticle(this.getText("particle.type")),
                        player.getLocation().add(0.0, 2.0, 0.0),
                        (int)this.getValue("particle.amount", level)
                     );
                     this.run(player, level);
                  }
               }
            }
         }
      }
   }

   public void run(Player player, int level) {
   }

   static {
      File folder = new File(((NereusOpus)JavaPlugin.getPlugin(NereusOpus.class)).getDataFolder(), "enchantments");
      File file = new File(folder, "skills.yml");
      if (!file.exists()) {
         ((NereusOpus)JavaPlugin.getPlugin(NereusOpus.class)).saveResource("enchantments/skills.yml", true);
      }

      Configuration config = Configuration.Companion.loadFromFile(file, Type.YAML, true);
      cdInfoEnabled = config.getBoolean("cd-info.enable");
      cdForm = config.getString("cd-info.form");
      cdMessage = config.getString("cd-info.message");
      action = config.getString("trigger.action");
      shiftNeeded = config.getBoolean("trigger.shift-needed");
      shiftIgnored = config.getBoolean("trigger.shift-ignored");

      for (String line : config.getStringList("rate")) {
         rates.put(line.split(":")[0], Double.parseDouble(line.split(":")[1]));
      }
   }
}
