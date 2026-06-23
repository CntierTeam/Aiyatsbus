package hamsteryds.nereusopus.enchants.internal.data;

public enum AttainSource {
   VILLAGER("\u6751\u6c11\u4ea4\u6613"),
   ENCHANTING_TABLE("\u9644\u9b54\u53f0"),
   LOOT_CHEST("\u6218\u5229\u54c1\u7bb1");

   public final String name;

   private AttainSource(String name) {
      this.name = name;
   }
}
