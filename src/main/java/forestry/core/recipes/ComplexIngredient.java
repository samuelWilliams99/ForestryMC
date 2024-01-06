package forestry.core.recipes;

import net.minecraft.world.item.ItemStack;

import net.minecraftforge.common.crafting.StrictNBTIngredient;

/**
 * Only used to bypass the 'protected' constructor of NBTIngredient.
 */
public class ComplexIngredient extends StrictNBTIngredient {
	public ComplexIngredient(ItemStack stack) {
		super(stack);
	}
}
