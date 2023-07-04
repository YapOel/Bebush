package org.sgx.bebush;


import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import org.sgx.bebush.block.PlantBush;
import org.sgx.bebush.item.Bush;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;


public class Bebush implements ModInitializer {
	public static final String MODID = "bebush";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	public static final Item MY_NEW_ITEM = new Bush();

	public static final Block MY_PLANT_BLOCK = new PlantBush(FabricBlockSettings.copyOf(Blocks.SUGAR_CANE).nonOpaque());
	public static final Item MY_PLANT_ITEM = new BlockItem(MY_PLANT_BLOCK, new Item.Settings().group(ItemGroup.MISC));
	public static final Item IGAR = new Item(new Item.Settings().group(ItemGroup.MISC)) {
		private static final int EFFECT_DURATION = 15 * 20; // Длительность эффекта в тиках
		private static final int PARTICLE_COUNT = 2500; // Количество партиклов для генерации
		private static final int PARTICLE_INTERVAL = 1; // Интервал между генерацией партиклов в тиках
		private static final String CHAT_MESSAGE = "Вы делаете тягу"; // Сообщение для отправки в чат

		@Override
		public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
			if (!world.isClient) {
				player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, EFFECT_DURATION));
				player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 4 * 60 * 20));
				player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 6 * 60 * 20));
				player.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 2 * 60 * 20));
				player.getStackInHand(hand).decrement(1);

				// Генерируем партиклы различных цветов вокруг игрока
				generateColorfulParticles(player);

				// Отправляем сообщение в чат игрока
				player.sendMessage(new LiteralText(CHAT_MESSAGE), false);

			}
			return TypedActionResult.success(player.getStackInHand(hand));
		}

		private void generateColorfulParticles(PlayerEntity player) {
			World world = player.getEntityWorld();
			if (world instanceof ServerWorld) {
				ServerWorld serverWorld = (ServerWorld) world;
				int taskCount = 0;

				for (int i = 0; i < PARTICLE_COUNT; i++) {
					serverWorld.getServer().submit(() -> {
						double x = player.getX() + player.getRandom().nextGaussian() * 2.0;
						double y = player.getY() + player.getRandom().nextGaussian() * 2.0;
						double z = player.getZ() + player.getRandom().nextGaussian() * 2.0;
						serverWorld.spawnParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, 1, 0, 0, 0, 0);
					});

					taskCount++;
					if (taskCount >= 2500) {
						break; // Прекратить генерацию после <value> партиклов
					}

					try {
						Thread.sleep(PARTICLE_INTERVAL);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	};


	public static final Item BUNCH = new Item(new Item.Settings().group(ItemGroup.MISC));


	@Override
	public void onInitialize() {
		Registry.register(Registry.ITEM, new Identifier("bebush", "222"), MY_NEW_ITEM);
		Registry.register(Registry.BLOCK, new Identifier("bebush", "my_plant"), MY_PLANT_BLOCK);
		Registry.register(Registry.ITEM, new Identifier("bebush", "my_plant_item"), MY_PLANT_ITEM);
		Registry.register(Registry.ITEM, new Identifier("bebush", "bunch"), BUNCH);
		Registry.register(Registry.ITEM, new Identifier("bebush", "igar"), IGAR);

	}

}

