package ganymedes01.etfuturum.configuration.configs;

import java.io.File;

import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.MixinEnvironment.Side;

import ganymedes01.etfuturum.EtFuturumMixinPlugin;
import ganymedes01.etfuturum.configuration.ConfigBase;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.common.config.Configuration;

public class ConfigMixins extends ConfigBase {
	
	public static boolean endPortalFix;
	public static boolean fenceWallConnectFix;
	public static boolean furnaceCrackle;
	public static boolean stepHeightFix;

	static final String catBackport = "backported features";
	static final String catOptimization = "optimizations";
	static final String catFixes = "fixes";
	static final String catMisc = "misc";
	
	public static final String PATH = ConfigBase.configDir + File.separator + "mixins.cfg";
	public static final ConfigMixins configInstance = new ConfigMixins(new File(Launch.minecraftHome, PATH));
	public static boolean avoidDroppingItemsWhenClosing;
	public static boolean enableSpectatorMode;
	public static boolean enableElytra;
	public static boolean enableNewElytraTakeoffLogic;
	public static boolean enableDoWeatherCycle;
	
	public ConfigMixins(File file) {
		super(file);
		setCategoryComment(catBackport, "Backports that can typically only have a clean implementation with mixins.");
		setCategoryComment(catOptimization, "Better implementations of existing features.\nThis is generally used when doing something through the Forge API would be slower or less practical than using a Mixin.");
		setCategoryComment(catFixes, "Fixes to vanilla issues which are necessary for backports.");
		setCategoryComment(catMisc, "Mixins that don't fit in any other category.");
		
		configCats.add(getCategory(catBackport));
		configCats.add(getCategory(catOptimization));
		configCats.add(getCategory(catFixes));
		configCats.add(getCategory(catMisc));
	}

	@Override
	protected void syncConfigOptions() {
		Configuration cfg = configInstance;
		if(EtFuturumMixinPlugin.side == MixinEnvironment.Side.CLIENT) {
			furnaceCrackle = cfg.getBoolean("furnaceCrackle", catBackport, true, "(Client only) Allows vanilla furnaces to have crackling sounds.\nModified Client Classes: net.minecraft.block.BlockFurnace"); 
		}
		
		endPortalFix = cfg.getBoolean("endPortalFix", catBackport, true, "Makes the End Portal block (the actual portal, not the frame) have an item icon, proper hitbox and will not instantly destroy itself in other dimensions.\nModified classes: net.minecraft.block.BlockEndPortal");
		fenceWallConnectFix = cfg.getBoolean("fenceWallConnectFix", catBackport, true, "Makes vanilla fences connect to modded ones of the same material. Might have connection issue with mods that don't reference BlockFence super code.\nModified classes: net.minecraft.block.BlockFence net.minecraft.block.BlockWall");
		avoidDroppingItemsWhenClosing = cfg.getBoolean("avoidDroppingItemsWhenClosing", catBackport, false, "Experimental: avoid dropping items when closing an inventory, like in modern versions.\nModified Classes: net.minecraft.entity.player.EntityPlayerMP");
		enableSpectatorMode = cfg.getBoolean("enableSpectatorMode", catBackport, true, "VERY EXPERIMENTAL!\nModified Classes: net.minecraft.world.WorldSettings.GameType net.minecraft.entity.Entity net.minecraft.world.World net.minecraft.entity.player.EntityPlayer net.minecraft.network.NetHandlerPlayServer\nModified Client Classes: net.minecraft.client.renderer.EntityRenderer net.minecraft.entity.player.EntityPlayer net.minecraft.client.renderer.WorldRenderer");
		enableElytra = cfg.getBoolean("enableElytra", catBackport, true, "Enables the elytra item.\nModified Classes: net.minecraft.entity.EntityLivingBase net.minecraft.entity.player.EntityPlayer net.minecraft.entity.EntityTrackerEntry net.minecraft.network.NetHandlerPlayServer net.minecraft.client.entity.AbstractClientPlayer net.minecraft.client.entity.EntityPlayerSP net.minecraft.client.model.ModelBiped net.minecraft.client.renderer.entity.RenderPlayer");
		enableNewElytraTakeoffLogic = cfg.getBoolean("enableNewElytraTakeoffLogic", catBackport, true, "When enabled, the 1.15+ elytra takeoff logic is used, when disabled, the 1.9-1.14 elytra takeoff logic is used.");
		enableDoWeatherCycle = cfg.getBoolean("enableDoWeatherCycle", catBackport, true, "Add the doWeatherCycle game rule from 1.11+");


		stepHeightFix = cfg.getBoolean("stepHeightFix", catFixes, true, "Makes the player able to step up even if a block would be above their head at the destination.\nModified classes: net.minecraft.entity.Entity");
	}
	
}