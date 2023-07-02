package org.sgx.bebush.mixin;

import net.minecraft.loot.LootPool;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.util.Identifier;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import org.sgx.bebush.Bebush;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LootManager.class)
public class ShipwreckLootMixin {
    @Inject(method = "getTable", at = @At("RETURN"), cancellable = true)
    private void modifyLootTable(Identifier id, CallbackInfoReturnable<LootTable> info) {
        if (id.equals(new Identifier("minecraft", "chests/shipwreck_supply"))) {
            LootPool pool = LootPool.builder()
                    .rolls(ConstantLootNumberProvider.create(1))
                    .with(ItemEntry.builder(Bebush.MY_PLANT_ITEM))
                    .build();

            LootTable modifiedTable = LootTable.builder()
                    .pool(pool)
                    .build();

            info.setReturnValue(modifiedTable);
        }
    }
}
