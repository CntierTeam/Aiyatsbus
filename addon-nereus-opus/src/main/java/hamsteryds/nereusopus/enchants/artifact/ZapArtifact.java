package hamsteryds.nereusopus.enchants.artifact;

import taboolib.library.xseries.particles.XParticle;
import hamsteryds.nereusopus.enchants.internal.entries.ArtifactEnchantment;
import java.io.File;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;

public class ZapArtifact extends ArtifactEnchantment {
   public ZapArtifact(File file) {
      super(file);
   }

   @Override
   public Particle getParticle() {
      return XParticle.DUST.get();
   }

   public DustOptions getOptions() {
      return new DustOptions(Color.YELLOW, 1.0F);
   }
}
