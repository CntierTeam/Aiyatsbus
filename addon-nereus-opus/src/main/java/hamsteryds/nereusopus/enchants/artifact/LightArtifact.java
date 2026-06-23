package hamsteryds.nereusopus.enchants.artifact;

import taboolib.library.xseries.particles.XParticle;
import hamsteryds.nereusopus.enchants.internal.entries.ArtifactEnchantment;
import hamsteryds.nereusopus.utils.WorldUtils;
import java.io.File;
import org.bukkit.Material;
import org.bukkit.Particle;

public class LightArtifact extends ArtifactEnchantment {
   private static boolean LIGHT_SUPPORTED;

   public LightArtifact(File file) {
      super(file);
   }

   @Override
   public Particle getParticle() {
      return LIGHT_SUPPORTED ? WorldUtils.getParticle("LIGHT") : XParticle.BLOCK_MARKER.get();
   }

   @Override
   public Object getOptions() {
      return LIGHT_SUPPORTED ? super.getOptions() : Material.LIGHT.createBlockData();
   }

   static {
      try {
         Particle.valueOf("LIGHT");
         LIGHT_SUPPORTED = true;
      } catch (Throwable var1) {
         LIGHT_SUPPORTED = false;
      }
   }
}
