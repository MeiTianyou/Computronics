package pl.asie.computronics.integration.buildcraft;

import buildcraft.api.transport.PipeManager;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.registry.GameRegistry;
import li.cil.oc.api.Driver;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import pl.asie.computronics.Computronics;
import pl.asie.computronics.integration.buildcraft.pluggable.DroneStationPluggable;
import pl.asie.computronics.integration.buildcraft.pluggable.DroneStationRenderer;
import pl.asie.computronics.integration.buildcraft.pluggable.ItemDockingUpgrade;
import pl.asie.computronics.integration.buildcraft.pluggable.ItemDroneStation;
import pl.asie.computronics.reference.Mods;

/**
 * @author Vexatos
 */
public class IntegrationBuildCraft {

	public ItemDroneStation droneStationItem;
	public ItemDockingUpgrade dockingUpgrade;

	@Optional.Method(modid = Mods.OpenComputers)
	public void preInitOC() {
		Computronics.log.info("Registering Drone Docking Station for OpenComputers");
		droneStationItem = new ItemDroneStation();
		GameRegistry.registerItem(droneStationItem, "computronics.droneStation");
		dockingUpgrade = new ItemDockingUpgrade();
		GameRegistry.registerItem(dockingUpgrade, "computronics.dockingUpgrade");
		Driver.add(dockingUpgrade);
		if(Computronics.proxy.isClient()) {
			MinecraftForge.EVENT_BUS.register(new DroneStationRenderer.TextureHandler());
		}
	}

	@Optional.Method(modid = Mods.OpenComputers)
	public void initOC() {
		if(Computronics.proxy.isClient()) {
			MinecraftForgeClient.registerItemRenderer(this.droneStationItem, new DroneStationRenderer.ItemRenderer());
		}
	}

	@Optional.Method(modid = Mods.OpenComputers)
	public void postInitOC() {
		PipeManager.registerPipePluggable(DroneStationPluggable.class, "computronics.droneStation");

		ItemStack robotStation = GameRegistry.findItemStack(Mods.BuildCraftTransport, "robotStation", 1);
		if(robotStation == null || robotStation.getItem() == null) {
			robotStation = new ItemStack(Items.ender_pearl, 1, 0);
		}
		GameRegistry.addShapedRecipe(new ItemStack(droneStationItem, 1, 0),
			" a ", "tst", " c ", 's', robotStation, 'a', li.cil.oc.api.Items.get("chip1").createItemStack(1),
			'c', li.cil.oc.api.Items.get("cable").createItemStack(1), 't', li.cil.oc.api.Items.get("transistor").createItemStack(1)
		);
		ItemStack pipe = GameRegistry.findItemStack(Mods.BuildCraftTransport, "item.buildcraftPipe.pipeitemsquartz", 1);
		if(pipe == null || pipe.getItem() == null) {
			pipe = li.cil.oc.api.Items.get("cable").createItemStack(1);
		}
		GameRegistry.addShapedRecipe(new ItemStack(dockingUpgrade, 1, 0),
			" a ", "tst", " c ", 's', new ItemStack(droneStationItem, 1, 0), 'a', li.cil.oc.api.Items.get("chip1").createItemStack(1),
			'c', pipe, 't', li.cil.oc.api.Items.get("transistor").createItemStack(1)
		);
	}
}