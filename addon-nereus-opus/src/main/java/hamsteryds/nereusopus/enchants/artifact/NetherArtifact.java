package hamsteryds.nereusopus.enchants.artifact;

import taboolib.library.xseries.particles.XParticle;
import hamsteryds.nereusopus.enchants.internal.entries.ArtifactEnchantment;
import java.io.File;
import org.bukkit.Particle;

public class NetherArtifact extends ArtifactEnchantment {
   public NetherArtifact(File file) {
      super(file);
   }

   @Override
   public Particle getParticle() {
      return XParticle.PORTAL.get();
   }
}
