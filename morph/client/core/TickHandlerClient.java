package morph.client.core;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map.Entry;

import morph.client.model.ModelMorph;
import morph.client.morph.MorphInfoClient;
import morph.client.render.RenderMorph;
import morph.common.morph.MorphInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class TickHandlerClient 
	implements ITickHandler
{
	
	public TickHandlerClient()
	{
		renderMorphInstance = new RenderMorph(new ModelMorph(), 0.5F);
	}
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) 
	{
		if (type.equals(EnumSet.of(TickType.RENDER)))
		{
			if(Minecraft.getMinecraft().theWorld != null)
			{
				preRenderTick(Minecraft.getMinecraft(), Minecraft.getMinecraft().theWorld, (Float)tickData[0]); //only ingame
			}
		}
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) 
	{
		if (type.equals(EnumSet.of(TickType.CLIENT)))
		{
			if(Minecraft.getMinecraft().theWorld != null)
			{      		
				worldTick(Minecraft.getMinecraft(), Minecraft.getMinecraft().theWorld);
			}
		}
		else if (type.equals(EnumSet.of(TickType.PLAYER)))
		{
			playerTick((World)((EntityPlayer)tickData[0]).worldObj, (EntityPlayer)tickData[0]);
		}
		else if (type.equals(EnumSet.of(TickType.RENDER)))
		{
			if(Minecraft.getMinecraft().theWorld != null)
			{
				renderTick(Minecraft.getMinecraft(), Minecraft.getMinecraft().theWorld, (Float)tickData[0]); //only ingame
			}
		}
	}

	@Override
	public EnumSet<TickType> ticks() 
	{
		return EnumSet.of(TickType.CLIENT, TickType.PLAYER, TickType.RENDER);
	}

	@Override
	public String getLabel() 
	{
		return "TickHandlerClientMorph";
	}

	public void worldTick(Minecraft mc, WorldClient world)
	{
		if(clock != world.getWorldTime())
		{
			clock = world.getWorldTime();
			
			if(clock != world.getWorldTime())
			{
				clock = world.getWorldTime();
				
				for(Entry<String, MorphInfoClient> e : playerMorphInfo.entrySet())
				{
					MorphInfo info = e.getValue();
					
					if(info.getMorphing())
					{
						info.morphProgress++;
						if(info.morphProgress > 80)
						{
							info.morphProgress = 80;
							info.setMorphing(false);
						}
					}
				}
			}
		}
	}

	public void playerTick(World world, EntityPlayer player)
	{
	}

	public void preRenderTick(Minecraft mc, World world, float renderTick)
	{
		this.renderTick = renderTick;
	}

	public void renderTick(Minecraft mc, World world, float renderTick)
	{
	}
	
	public long clock;
	
	public RenderMorph renderMorphInstance;
	
	public HashMap<String, MorphInfoClient> playerMorphInfo = new HashMap<String, MorphInfoClient>();

	public float renderTick;
}