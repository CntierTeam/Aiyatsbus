package hamsteryds.nereusopus.enchants.artifact;

import taboolib.library.xseries.particles.XParticle;
import hamsteryds.nereusopus.enchants.internal.entries.ArtifactEnchantment;
import java.io.File;
import org.bukkit.Particle;

public class AshArtifact extends ArtifactEnchantment {
   public AshArtifact(File file) {
      super(file);
   }

   @Override
   public Particle getParticle() {
      return XParticle.ASH.get();
   }
}
