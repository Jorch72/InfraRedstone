package com.elytradev.infraredstone.util;

import com.elytradev.infraredstone.InRedLog;
import com.elytradev.infraredstone.block.ModBlocks;
import com.elytradev.infraredstone.item.ModItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;

public class InRedRecipes {

    @SubscribeEvent
    public static void onRegisterRecipes(RegistryEvent.Register<IRecipe> event) {

        IForgeRegistry<IRecipe> r = event.getRegistry();

//         Crafting bench recipes

//        Gates
        recipe(r, new ShapedOreRecipe(new ResourceLocation("infraredstone:blocks"), new ItemStack(ModBlocks.GATE_AND, 1),
                " t ", "trt", "ptp",
                'r', new ItemStack(Items.REDSTONE),
                't', new ItemStack(Blocks.REDSTONE_TORCH),
                'p', new ItemStack(ModItems.PCB)
        ));

//        recipe(r, new ShapedOreRecipe(new ResourceLocation("infraredstone:blocks"), new ItemStack(ModBlocks.NAND, 1),
//                " t ", "ttt", "ptp",
//                'r', new ItemStack(Items.REDSTONE),
//                't', new ItemStack(Blocks.REDSTONE_TORCH),
//                'p', new ItemStack(ModItems.PCB)
//        ));

//        recipe(r, new ShapedOreRecipe(new ResourceLocation("infraredstone:blocks"), new ItemStack(ModBlocks.OR, 1),
//                " r ", "rrr", "prp",
//                'r', new ItemStack(Items.REDSTONE),
//                'p', new ItemStack(ModItems.PCB)
//        ));

//        recipe(r, new ShapedOreRecipe(new ResourceLocation("infraredstone:blocks"), new ItemStack(ModBlocks.XOR, 1),
//                "tr ", "prp", " rt",
//                'r', new ItemStack(Items.REDSTONE),
//                't', new ItemStack(Blocks.REDSTONE_TORCH),
//                'p', new ItemStack(ModItems.PCB)
//        ));

//        recipe(r, new ShapedOreRecipe(new ResourceLocation("infraredstone:blocks"), new ItemStack(ModBlocks.XOR, 1),
//                " rt", "prp", "tr ",
//                'r', new ItemStack(Items.REDSTONE),
//                't', new ItemStack(Blocks.REDSTONE_TORCH),
//                'p', new ItemStack(ModItems.PCB)
//        ));

//        recipe(r, new ShapedOreRecipe(new ResourceLocation("infraredstone:blocks"), new ItemStack(ModBlocks.NOT, 1),
//                "t", "p",
//                't', new ItemStack(Blocks.REDSTONE_TORCH),
//                'p', new ItemStack(ModItems.PCB)
//        ));

//        Modules
        recipe(r, new ShapedOreRecipe(new ResourceLocation("infraredstone:blocks"), new ItemStack(ModBlocks.FINE_LEVER, 1),
                "r", "l", "p",
                'r', new ItemStack(Items.REDSTONE),
                'l', new ItemStack(Blocks.LEVER),
                'p', new ItemStack(ModItems.PCB)
        ));

        recipe(r, new ShapedOreRecipe(new ResourceLocation("infraredstone:blocks"), new ItemStack(ModBlocks.DIODE, 1),
                "t t", "rpr",
                'r', new ItemStack(Items.REDSTONE),
                't', new ItemStack(Blocks.REDSTONE_TORCH),
                'p', new ItemStack(ModItems.PCB)
        ));

//        recipe(r, new ShapedOreRecipe(new ResourceLocation("infraredstone:blocks"), new ItemStack(ModBlocks.TRANSISTOR, 1),
//                " r ", "rpr", " r ",
//                'r', new ItemStack(Items.REDSTONE),
//                'p', new ItemStack(ModItems.PCB)
//        ));

//        recipe(r, new ShapedOreRecipe(new ResourceLocation("infraredstone:blocks"), new ItemStack(ModBlocks.OSCILLATOR, 1),
//                " q ", "rpr",
//                'r', new ItemStack(Items.REDSTONE),
//                'q', new ItemStack(Items.QUARTZ),
//                'p', new ItemStack(ModItems.PCB)
//        ));

//        Other
        recipe(r, new ShapedOreRecipe(new ResourceLocation("infraredstone:blocks"), new ItemStack(ModBlocks.INFRA_REDSTONE, 3),
                "rrr", "ggg",
                'r', new ItemStack(Items.REDSTONE),
                'g', new ItemStack(Blocks.GLASS)
        ));

//        Items
        recipe(r, new ShapedOreRecipe(new ResourceLocation("infraredstone:items"), new ItemStack(ModItems.PCB, 1),
                "c", "r", "s",
                'c', "dyeCyan",
                'r', new ItemStack(ModBlocks.INFRA_REDSTONE),
                's', new ItemStack(Blocks.STONE_SLAB)
        ));
        recipe(r, new ShapedOreRecipe(new ResourceLocation("infraredstone:items"), new ItemStack(ModItems.PCB, 2),
                "cc", "rr", "ss",
                'c', "dyeCyan",
                'r', new ItemStack(ModBlocks.INFRA_REDSTONE),
                's', new ItemStack(Blocks.STONE_SLAB)
        ));
        recipe(r, new ShapedOreRecipe(new ResourceLocation("infraredstone:items"), new ItemStack(ModItems.PCB, 3),
                "ccc", "rrr", "sss",
                'c', "dyeCyan",
                'r', new ItemStack(ModBlocks.INFRA_REDSTONE),
                's', new ItemStack(Blocks.STONE_SLAB)
        ));

    }

    public static <T extends IRecipe> T recipe(IForgeRegistry<IRecipe> registry, T t) {
        t.setRegistryName(new ResourceLocation(t.getRecipeOutput().getItem().getRegistryName()+"_"+t.getRecipeOutput().getItemDamage()));
        registry.register(t);
        return t;
    }

}
