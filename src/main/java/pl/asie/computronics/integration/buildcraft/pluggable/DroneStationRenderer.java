package pl.asie.computronics.integration.buildcraft.pluggable;

import buildcraft.api.core.render.ITextureStates;
import buildcraft.api.transport.IPipe;
import buildcraft.api.transport.pluggable.IPipePluggableRenderer;
import buildcraft.api.transport.pluggable.PipePluggable;
import buildcraft.core.utils.MatrixTranformations;
import buildcraft.transport.render.TextureStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;
import pl.asie.computronics.integration.buildcraft.pluggable.IntegrationBuildCraft.Textures;

/**
 * @author Vexatos
 */
public class DroneStationRenderer implements IPipePluggableRenderer {

	private float zFightOffset = 1 / 4096.0F;

	private void droneStationPartRender(RenderBlocks renderblocks,
		ForgeDirection side, ITextureStates blockStateMachine, int x, int y, int z,
		float xStart, float xEnd, float yStart, float yEnd, float zStart,
		float zEnd) {

		float[][] zeroState = new float[3][2];
		// X START - END
		zeroState[0][0] = xStart + zFightOffset;
		zeroState[0][1] = xEnd - zFightOffset;
		// Y START - END
		zeroState[1][0] = yStart;
		zeroState[1][1] = yEnd;
		// Z START - END
		zeroState[2][0] = zStart + zFightOffset;
		zeroState[2][1] = zEnd - zFightOffset;

		IIcon[] icons = ((TextureStateManager) blockStateMachine.getTextureState()).popArray();
		icons[0] = Textures.DRONE_STATION_BOTTOM.getIcon();
		icons[1] = Textures.DRONE_STATION_BOTTOM.getIcon();
		for(int i = 2; i < icons.length; i++) {
			icons[i] = Textures.DRONE_STATION_SIDE.getIcon();
		}
		((TextureStateManager) blockStateMachine.getTextureState()).popArray();

		//blockStateMachine.getTextureState().set(Textures.DRONE_STATION_TOP.getIcon());

		float[][] rotated = MatrixTranformations.deepClone(zeroState);
		MatrixTranformations.transform(rotated, side);

		renderblocks.setRenderBounds(rotated[0][0], rotated[1][0],
			rotated[2][0], rotated[0][1], rotated[1][1],
			rotated[2][1]);
		renderblocks.renderStandardBlock(blockStateMachine.getBlock(), x, y, z);
		((TextureStateManager) blockStateMachine.getTextureState()).pushArray();
	}

	@Override
	public void renderPluggable(RenderBlocks renderblocks, IPipe pipe, ForgeDirection side, PipePluggable pipePluggable, ITextureStates blockStateMachine, int renderPass, int x, int y, int z) {
		if(renderPass != 0) {
			return;
		}

		droneStationPartRender(renderblocks, side, blockStateMachine, x, y, z,
			0.56F, 0.625F,
			0.09F, 0.224F,
			0.375F, 0.44F);

		droneStationPartRender(renderblocks, side, blockStateMachine, x, y, z,
			0.56F, 0.625F,
			0.09F, 0.224F,
			0.56F, 0.625F);

		droneStationPartRender(renderblocks, side, blockStateMachine, x, y, z,
			0.375F, 0.44F,
			0.09F, 0.224F,
			0.375F, 0.44F);

		droneStationPartRender(renderblocks, side, blockStateMachine, x, y, z,
			0.375F, 0.44F,
			0.09F, 0.224F,
			0.56F, 0.625F);

		float[][] zeroState = new float[3][2];

		// X START - END
		zeroState[0][0] = 0.25F + zFightOffset;
		zeroState[0][1] = 0.75F - zFightOffset;
		// Y START - END
		zeroState[1][0] = 0.225F;
		zeroState[1][1] = 0.251F;
		// Z START - END
		zeroState[2][0] = 0.25F + zFightOffset;
		zeroState[2][1] = 0.75F - zFightOffset;

		IIcon[] icons = ((TextureStateManager) blockStateMachine.getTextureState()).popArray();
		icons[0] = Textures.DRONE_STATION_SIDE.getIcon();
		icons[1] = Textures.DRONE_STATION_TOP.getIcon();
		for(int i = 2; i < icons.length; i++) {
			icons[i] = Textures.DRONE_STATION_SIDE.getIcon();
		}
		((TextureStateManager) blockStateMachine.getTextureState()).popArray();

		//blockStateMachine.getTextureState().set(Textures.DRONE_STATION_TOP.getIcon());

		float[][] rotated = MatrixTranformations.deepClone(zeroState);
		MatrixTranformations.transform(rotated, side);
		renderblocks.setRenderBounds(rotated[0][0], rotated[1][0],
			rotated[2][0], rotated[0][1], rotated[1][1],
			rotated[2][1]);
		renderblocks.renderStandardBlock(blockStateMachine.getBlock(), x, y, z);

		// X START - END
		zeroState[0][0] = 0.25F + 0.125F / 2 + zFightOffset;
		zeroState[0][1] = 0.75F - 0.125F / 2 + zFightOffset;
		// Y START - END
		zeroState[1][0] = 0.25F;
		zeroState[1][1] = 0.25F + 0.125F;
		// Z START - END
		zeroState[2][0] = 0.25F + 0.125F / 2;
		zeroState[2][1] = 0.75F - 0.125F / 2;

		//((TextureStateManager) blockStateMachine.getTextureState()).pushArray();

		icons[0] = Textures.DRONE_STATION_BOTTOM.getIcon();
		icons[1] = Textures.DRONE_STATION_SIDE.getIcon();
		for(int i = 2; i < icons.length; i++) {
			icons[i] = Textures.DRONE_STATION_BOTTOM.getIcon();
		}

		//blockStateMachine.getTextureState().set(Textures.DRONE_STATION_BOTTOM.getIcon());
		rotated = MatrixTranformations.deepClone(zeroState);
		MatrixTranformations.transform(rotated, side);

		renderblocks.setRenderBounds(rotated[0][0], rotated[1][0], rotated[2][0], rotated[0][1], rotated[1][1], rotated[2][1]);
		renderblocks.renderStandardBlock(blockStateMachine.getBlock(), x, y, z);
		((TextureStateManager) blockStateMachine.getTextureState()).pushArray();
	}

	public static class ItemRenderer extends ModelBase implements IItemRenderer {

		private ResourceLocation texture = new ResourceLocation("computronics", "textures/blocks/buildcraft/pluggable/drone_station.png");

		@Override
		public boolean handleRenderType(ItemStack item, ItemRenderType type) {
			switch(type){
				case ENTITY:
					return true;
				case EQUIPPED:
					return true;
				case EQUIPPED_FIRST_PERSON:
					return true;
				case INVENTORY:
					return true;
				default:
					return false;
			}
		}

		@Override
		public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
			return helper != ItemRendererHelper.BLOCK_3D;
		}

		@Override
		public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
			GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
			GL11.glPushMatrix();

			Minecraft.getMinecraft().renderEngine.bindTexture(texture);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glDepthFunc(GL11.GL_LEQUAL);
			GL11.glDisable(GL11.GL_CULL_FACE);
			switch(type){
				case ENTITY:
					GL11.glRotatef(-180, 1, 0, 0);
					this.Base.render(1 / 16f);
					break;
				case EQUIPPED_FIRST_PERSON:
					GL11.glRotatef(20, 1F, 0F, 1F);
					GL11.glRotatef(50, 1F, 1F, 0F);
					GL11.glRotatef(-20, 0F, 1F, 0F);
					GL11.glTranslatef(0.6F, 0F, -0.3F);
				case EQUIPPED:
					GL11.glTranslatef(0.6F, 1F, 0.7F);
					GL11.glRotatef(-140, 0, 0, 1F);
					GL11.glRotatef(140, 0, 1F, 0);
					GL11.glRotatef(-35, 1F, 0, 0);
					GL11.glScalef(2F, 2F, 2F);
					this.Base.render(1 / 16f);
					break;
				case INVENTORY:
					GL11.glScalef(1.1F, 1.1F, 1.1F);
					GL11.glRotatef(-180, 1, 0, 0);
					GL11.glRotatef(60, 0, 1, 0);
					this.Base.render(1 / 16f);
					break;
				default:
					break;
			}
			GL11.glPopMatrix();
			GL11.glPopAttrib();
		}

		public ModelRenderer Base;
		public ModelRenderer Nook1;
		public ModelRenderer Nook2;
		public ModelRenderer Nook3;
		public ModelRenderer Nook4;

		public ItemRenderer() {
			this.textureWidth = 64;
			this.textureHeight = 32;
			this.Nook3 = new ModelRenderer(this, 8, 12);
			this.Nook3.setRotationPoint(1.0F, -2.0F, -2.0F);
			this.Nook3.addBox(0.0F, 0.0F, 0.0F, 1, 2, 1, 0.0F);
			this.Nook4 = new ModelRenderer(this, 12, 12);
			this.Nook4.setRotationPoint(-2.0F, -2.0F, -2.0F);
			this.Nook4.addBox(0.0F, 0.0F, 0.0F, 1, 2, 1, 0.0F);
			this.Base = new ModelRenderer(this, 0, 0);
			this.Base.setRotationPoint(0.0F, 0.0F, 0.0F);
			this.Base.addBox(-4.0F, 0.0F, -4.0F, 8, 2, 8, 0.0F);
			this.Nook1 = new ModelRenderer(this, 0, 12);
			this.Nook1.setRotationPoint(1.0F, -2.0F, 1.0F);
			this.Nook1.addBox(0.0F, 0.0F, 0.0F, 1, 2, 1, 0.0F);
			this.Nook2 = new ModelRenderer(this, 4, 12);
			this.Nook2.setRotationPoint(-2.0F, -2.0F, 1.0F);
			this.Nook2.addBox(0.0F, 0.0F, 0.0F, 1, 2, 1, 0.0F);
			this.Base.addChild(this.Nook3);
			this.Base.addChild(this.Nook4);
			this.Base.addChild(this.Nook1);
			this.Base.addChild(this.Nook2);
		}
	}
}