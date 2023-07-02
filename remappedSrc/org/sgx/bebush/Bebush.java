package org.sgx.bebush;

import net.fabricmc.api.ModInitializer;
import org.sgx.bebush.item.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bebush implements ModInitializer {
	public static final String MODID = "bebush";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	@Override
	public void onInitialize() {
		ModItems.register();
	}
}
