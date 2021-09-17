package ganymedes01.etfuturum.mixins;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import ganymedes01.etfuturum.EtFuturum;
import ganymedes01.etfuturum.ModBlocks;
import ganymedes01.etfuturum.configuration.ConfigBase;
import ganymedes01.etfuturum.world.generate.BlockAndMetadataMapping;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMinable;

@Mixin(value = WorldGenMinable.class)
public class MixinWorldGenMinable {

	@Shadow
	/**
	 * Block to generate
	 */
    private Block field_150519_a;
	@Shadow
	/**
	 * Generate meta
	 */
    private int mineableBlockMeta;
	@Shadow
	/**
	 * Block to replace
	 */
    private Block field_150518_c;
	
	private boolean doesDeepslateDenyHere(World world, int x, int y, int z) {
		return world.getBlock(x, y, z).isReplaceableOreGen(world, x, y, z, ModBlocks.deepslate) && ((ConfigBase.deepslateReplacesDirt && field_150519_a == Blocks.dirt) || (ConfigBase.deepslateReplacesStones && (ConfigBase.deepslateReplacesDirt && field_150519_a == ModBlocks.stone)));
	}
	
    @Inject(method = "generate", at = @At(value = "HEAD"), cancellable = true)
	private void canGenerateHere(World world, Random rand, int x, int y, int z, CallbackInfoReturnable<Boolean> info) {
    	if(doesDeepslateDenyHere(world, x, y, z)) {
    		info.setReturnValue(false);
    	}
	}
    
    @Redirect(method = "generate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlock(IIILnet/minecraft/block/Block;II)Z", ordinal = 0))
	private boolean ignoreVanillaSetblock(World world, int x, int y, int z, Block block, int meta, int flag) {
    	if (world.getBlock(x, y, z).isReplaceableOreGen(world, x, y, z, ModBlocks.deepslate) && EtFuturum.deepslateOres.get(new BlockAndMetadataMapping(field_150519_a, mineableBlockMeta)) != null) {
    		BlockAndMetadataMapping replacement = EtFuturum.deepslateOres.get(new BlockAndMetadataMapping(field_150519_a, mineableBlockMeta));
    		world.setBlock(x, y, z, replacement.getOre(), replacement.getMeta(), 2);
    		return true;
    	} else if (!doesDeepslateDenyHere(world, x, y, z)) {
    		world.setBlock(x, y, z, this.field_150519_a, mineableBlockMeta, 2);
    		return true;
    	}
    	return false;
    }
}