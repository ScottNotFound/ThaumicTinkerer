package com.nekokittygames.thaumictinkerer.client.gui;

import com.nekokittygames.thaumictinkerer.ThaumicTinkerer;
import com.nekokittygames.thaumictinkerer.client.gui.button.GuiTexturedButton;
import com.nekokittygames.thaumictinkerer.client.gui.button.GuiTexturedRadioButton;
import com.nekokittygames.thaumictinkerer.client.gui.button.IRadioButton;
import com.nekokittygames.thaumictinkerer.client.libs.LibClientResources;
import com.nekokittygames.thaumictinkerer.common.blocks.BlockMagnet;
import com.nekokittygames.thaumictinkerer.common.containers.MagnetContainer;
import com.nekokittygames.thaumictinkerer.common.packets.PacketHandler;
import com.nekokittygames.thaumictinkerer.common.packets.PacketMagnetMode;
import com.nekokittygames.thaumictinkerer.common.packets.PacketMobMagnet;
import com.nekokittygames.thaumictinkerer.common.tileentity.TileEntityMagnet;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class GuiMagnet extends GuiContainer {

    private static final int WIDTH = 176;
    private static final int HEIGHT = 166;
    private int x, y;
    protected List<GuiTexturedButton> buttonListMM = new ArrayList<>();
    protected List<IRadioButton> radioButtons = new ArrayList<>();

    private TileEntityMagnet magnet;

    /**
     * Constructor
     *
     * @param tileEntity mob magnet to display GUI for
     * @param container  mo magnet's container
     */
    public GuiMagnet(TileEntityMagnet tileEntity, MagnetContainer container) {
        super(container);
        xSize = WIDTH;
        ySize = HEIGHT;
        this.magnet = tileEntity;
    }

    /**
     * Initializes the GUI
     */
    @Override
    public void initGui() {
        super.initGui();
        x = (width - xSize) / 2;
        y = (height - ySize) / 2;
        buttonListMM.clear();
        getButtons();
        buttonList.addAll(buttonListMM);

    }


    protected void getButtons() {
        addButton(new GuiTexturedRadioButton(0, x + 100, y - 13, LibClientResources.GUI_MOBMAGNET, magnet.GetMode() == BlockMagnet.MagnetPull.PUSH,"mode", radioButtons));
        addButton(new GuiTexturedRadioButton(1, x + 100, y + 8, LibClientResources.GUI_MOBMAGNET, magnet.GetMode() == BlockMagnet.MagnetPull.PULL, "mode",radioButtons));;
    }
    /**
     * Adds button to GUI
     *
     * @param button button to add
     * @param <T>    type of button to add
     * @return added button
     */
    @Nonnull
    @Override
    protected <T extends GuiButton> T addButton(@Nonnull T button) {
        if (button instanceof GuiTexturedButton) {
            buttonListMM.add((GuiTexturedButton) button);

            if (button instanceof IRadioButton)
                radioButtons.add((IRadioButton) button);
            return button;
        } else
            return super.addButton(button);
    }

    /**
     * Callback for button pressed
     * @param button button that was pressed
     */
    @Override
    protected void actionPerformed(GuiButton button) {
        if (button instanceof IRadioButton)
            ((IRadioButton) button).enableFromClick();
        else buttonListMM.get(1).setButtonEnabled(!buttonListMM.get(1).isButtonEnabled());

        magnet.setMode(buttonListMM.get(0).isButtonEnabled()?BlockMagnet.MagnetPull.PUSH:BlockMagnet.MagnetPull.PULL);

        PacketHandler.INSTANCE.sendToServer(new PacketMagnetMode(magnet, magnet.GetMode()));
        //mobMagnet.adult = buttonListMM.get(0).enabled;

    }

    /**
     * Draws the background layer
     * @param partialTicks update ticks
     * @param mouseX xPos of mouse
     * @param mouseY yPos of mouse
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(LibClientResources.GUI_MOBMAGNET);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        String push = ThaumicTinkerer.proxy.localize("ttmisc.mobmagnet.push");
        String pull = ThaumicTinkerer.proxy.localize("ttmisc.mobmagnet.pull");
        //ItemStack stack = mobMagnet.getInventory().getStackInSlot(0);
        String filter;
        //if (stack != ItemStack.EMPTY) {
        //    String name = ItemSoulMould.getEntityName(stack);
        //    if (name != null) {
        //        filter = ThaumicTinkerer.proxy.localize("entity." + name + ".name");
        //    } else
        //        filter = ThaumicTinkerer.proxy.localize("ttmisc.mobmagnet.none");
//
//        } else
//            filter = ThaumicTinkerer.proxy.localize("ttmisc.mobmagnet.all");
  //      fontRenderer.drawString(filter, x + xSize / 2 - fontRenderer.getStringWidth(filter) / 2 - 26, y + 16, 0x999999);
        fontRenderer.drawString(push, x + 120, y -11, 0x999999);
        fontRenderer.drawString(pull, x + 120, y + 10, 0x999999);
        GL11.glColor3f(1F, 1F, 1F);
    }
}
