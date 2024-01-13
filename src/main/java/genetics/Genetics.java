package genetics;

import com.mojang.datafixers.kinds.Const;
import forestry.core.config.Constants;
import io.netty.util.Constant;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import genetics.api.GeneticsAPI;
import genetics.api.IGeneTemplate;
import genetics.api.organism.IOrganism;
import genetics.api.root.IRootDefinition;
import genetics.api.root.components.DefaultStage;
import genetics.commands.CommandListAlleles;
import genetics.plugins.PluginManager;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

@Mod(Genetics.MOD_ID)
// @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Constants.MOD_ID)
public class Genetics {
	public static final String MOD_ID = "geneticsapi";

	/**
	 * Capability for {@link IOrganism}.
	 */
	public static Capability<IOrganism> ORGANISM = CapabilityManager.get(new CapabilityToken<>() {
	});
	public static Capability<IGeneTemplate> GENE_TEMPLATE = CapabilityManager.get(new CapabilityToken<>() {
	});

	public Genetics() {
		GeneticsAPI.apiInstance = ApiInstance.INSTANCE;
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		modBus.addListener(this::setupCommon);
		modBus.addListener(this::loadComplete);
		modBus.register(this);
		MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
	}

	@SubscribeEvent
	public void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.register(IOrganism.class);
		event.register(IGeneTemplate.class);
	}

	// TODO[SW] This is erroring because Forestry.createFeatures (priority HIGH) is running before this, even with a lower prio
	// I think its because this is a separate mod_id, so all the hooks of this are running after all the hooks of Forestry
	// I'll try another hook on priority LOWEST in forestry and see if it runs before this, then maybe ask around about event buses
	// Perhaps explicitly rendering the listener will be smarter?
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void registerBlocks(RegisterEvent event) {
		System.out.println("Genetics registerBlocks" + event.getRegistryKey());
		if (event.getRegistryKey().equals(ForgeRegistries.Keys.BLOCKS)) {
			System.out.println("Genetics registerBlocks BLOCKS!");
			PluginManager.create();
			PluginManager.initPlugins();
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void registerFinished(RegisterEvent event) {
		if (!event.getRegistryKey().equals(ForgeRegistries.Keys.ITEMS)) return;
		for (IRootDefinition definition : GeneticsAPI.apiInstance.getRoots().values()) {
			if (!definition.isPresent()) {
				continue;
			}
			definition.get().getComponentContainer().onStage(DefaultStage.REGISTRATION);
		}
	}

	private void setupCommon(FMLCommonSetupEvent event) {
		for (IRootDefinition definition : GeneticsAPI.apiInstance.getRoots().values()) {
			if (!definition.isPresent()) {
				continue;
			}
			definition.get().getComponentContainer().onStage(DefaultStage.SETUP);
		}
	}

	private void loadComplete(FMLLoadCompleteEvent event) {
		for (IRootDefinition definition : GeneticsAPI.apiInstance.getRoots().values()) {
			if (!definition.isPresent()) {
				continue;
			}
			definition.get().getComponentContainer().onStage(DefaultStage.COMPLETION);
		}
	}

	public void registerCommands(RegisterCommandsEvent event) {
		CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
		LiteralArgumentBuilder<CommandSourceStack> rootCommand = LiteralArgumentBuilder.literal("genetics");
		rootCommand.then(CommandListAlleles.register());
		dispatcher.register(rootCommand);
	}
}
