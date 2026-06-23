package hamsteryds.nereusopus.enchants.internal.entries;

import taboolib.library.configuration.ConfigurationSection;
import taboolib.library.xseries.particles.XParticle;
import taboolib.module.configuration.Configuration;
import taboolib.module.configuration.Type;
import hamsteryds.nereusopus.NereusOpus;
import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.WorldUtils;
import hamsteryds.nereusopus.utils.stats.Pair;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class ArtifactEnchantment extends EventExecutor {
   public static List<Double> range;
   public static List<Double> height;
   public static List<String> triggers;
   public static HashMap<EquipmentSlot, Pair<Double, Double>> customParticleData = new HashMap<>();
   public static HashMap<EquipmentSlot, String> customParticleType = new HashMap<>();

   public ArtifactEnchantment(File file) {
      super(file);
   }

   @Override
   public void tickTask(int level, EquipmentSlot slot, Player player, int stamp) {
      if (this.params().getOrDefault("custom", "false").equals("true")) {
         this.spawnCustomParticle(player.getLocation(), slot);
      } else {
         if (player.isGliding() && slot == EquipmentSlot.CHEST) {
            this.spawnSimpleParticle(player.getLocation());
         }

         if (slot == EquipmentSlot.FEET) {
            this.spawnCircleParticle(player.getLocation().add(0.0, 0.2, 0.0), 2);
         }
      }
   }

   @Override
   public void blockBreak(int level, BlockBreakEvent event) {
      Block block = event.getBlock();
      if (triggers.contains(block.getType().getKey().getKey())) {
         this.spawnSimpleParticle(block.getLocation().add(0.5, 0.5, 0.5));
      }
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      if (event.getDamager() instanceof Player) {
         Player player = (Player)event.getDamager();
         if (player.getAttackCooldown() < 0.9F) {
            return;
         }
      }

      if (event.getEntity() instanceof LivingEntity) {
         LivingEntity creature = (LivingEntity)event.getEntity();
         int size = creature instanceof Slime ? Math.min(((Slime)creature).getSize(), 3) : 2;
         size = creature instanceof Ageable ? (((Ageable)creature).isAdult() ? 2 : 1) : size;
         size = creature instanceof Ghast ? 3 : size;
         this.spawnDNAParticles(event.getEntity().getLocation().add(0.0, 1.0, 0.0), size);
      }
   }

   public void spawnDNAParticles(Location loc, int size) {
      WorldUtils.spawnRNAParticles(this.getParticle(), loc, (int)this.getValue("amount"), this.getOptions(), height.get(size - 1), range.get(size - 1));
   }

   public void spawnSimpleParticle(Location loc) {
      WorldUtils.spawnSimpleParticle(this.getParticle(), loc, (int)this.getValue("amount"), this.getOptions());
   }

   public void spawnCircleParticle(Location loc, int size) {
      WorldUtils.spawnCircleParticles(this.getParticle(), loc, (int)this.getValue("amount"), this.getOptions(), range.get(size - 1));
   }

   public void spawnCustomParticle(Location loc, EquipmentSlot slot) {
      String type = customParticleType.get(slot);
      Pair<Double, Double> data = customParticleData.get(slot);

      double height = switch (slot) {
         case HAND, OFF_HAND -> 1.2;
         case FEET -> 0.1;
         case LEGS -> 0.6;
         case CHEST -> 1.15;
         case HEAD -> 1.8;
         default -> 1.825;
      };
      loc = loc.clone().add(0.0, height, 0.0);
      switch (type) {
         case "RNA":
            WorldUtils.spawnRNAParticles(this.getParticle(), loc, (int)this.getValue("amount"), this.getOptions(), data.getSecond(), data.getFirst(), 10, 2);
            break;
         case "CIRCLE":
            WorldUtils.spawnCircleParticles(
               this.getParticle(), loc.add(0.0, data.getSecond(), 0.0), (int)this.getValue("amount"), this.getOptions(), data.getFirst()
            );
            break;
         case "SIMPLE":
            WorldUtils.spawnSimpleParticle(this.getParticle(), loc, (int)this.getValue("amount"), this.getOptions());
      }
   }

   public Particle getParticle() {
      return XParticle.ASH.get();
   }

   public Object getOptions() {
      return null;
   }

   static {
      File folder = new File(((NereusOpus)JavaPlugin.getPlugin(NereusOpus.class)).getDataFolder(), "enchantments");
      File file = new File(folder, "artifacts.yml");
      if (!file.exists()) {
         ((NereusOpus)JavaPlugin.getPlugin(NereusOpus.class)).saveResource("enchantments/artifacts.yml", true);
      }

      Configuration config = Configuration.Companion.loadFromFile(file, Type.YAML, true);
      ArtifactEnchantment.range = config.getDoubleList("range");
      ArtifactEnchantment.height = config.getDoubleList("height");
      triggers = config.getStringList("triggers");
      ConfigurationSection section = config.getConfigurationSection("custom");

      for (String path : section.getKeys(false)) {
         String type = section.getString(path + ".type");
         double range = section.getDouble(path + ".range", 0.0);
         double height = section.getDouble(path + ".height", 0.0);
         customParticleType.put(EquipmentSlot.valueOf(path), type);
         customParticleData.put(EquipmentSlot.valueOf(path), new Pair<>(range, height));
      }
   }
}
