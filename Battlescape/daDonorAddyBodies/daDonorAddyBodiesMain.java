package daDonorAddyBodies;

import java.awt.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.regex.Pattern;

import net.runelite.api.coords.WorldPoint;
import simple.hooks.filters.SimplePrayers;
import simple.hooks.scripts.Category;
import simple.hooks.scripts.ScriptManifest;
import simple.hooks.simplebot.ChatMessage;
import simple.hooks.wrappers.SimpleItem;
import simple.hooks.wrappers.SimpleObject;
import simple.hooks.wrappers.SimpleWidget;
import simple.robot.script.Script;

@ScriptManifest(author = "alex", category = Category.SMITHING, description = "", discord = "",
        name = "daDonorAddyBodies", servers = { "Battlescape" }, version = "2.0")

public class daDonorAddyBodiesMain extends Script {
    public String status;
    public long startTime;
    private boolean presetClicked = false; // 20973 is the chest @ barrows

    @Override
    public void onExecute() {
        System.out.println("Started daDonorAddyBodies");
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void onProcess() {
        status("in da first");
        SimpleItem addyBar = ctx.inventory.populate().filter(2361).next();
        SimpleObject anvil = ctx.objects.populate().filter(2031).nearest().next();

        if (addyBar != null || addyBar.getQuantity() < 5) {
            ctx.pathing.step(new WorldPoint(3044, 3502, 0));
            if (ctx.players.getLocal().getLocation().equals(new WorldPoint(3044, 3502, 0))){
                clickPreset();
            }
        }

        if (addyBar.getQuantity() >= 5) {
            status("Click anvil");
            anvil.click(0);
            if (anvil.validateInteractable()) {
                makeBody();
            }
        }

    }

    @Override
    public void onChatMessage(ChatMessage m) {
    }

    @Override
    public void onTerminate() {
    }

    public void status(final String status) {
        this.status = status;
    }

    private void clickPreset() {
        status("preset moment");
        SimpleWidget preset = ctx.widgets.getWidget(702, 89);
        SimpleWidget questTab = ctx.widgets.getWidget(548, 50);
        SimpleWidget loadOuts = ctx.widgets.getWidget(702, 5);
        if (questTab != null && !questTab.isHidden()){
            questTab.click(0);
            if (loadOuts != null && !loadOuts.isHidden()){
                loadOuts.click(0);
                if (preset != null && !preset.isHidden()) {
                    preset.click(0);
                    ctx.sleep(15000);
                }
            }
        }
    }

    private void makeBody() {
        status("making bodies & shit");
        SimpleWidget smithingWindow = ctx.widgets.getWidget(312, 0);
        SimpleWidget bodyWindow = ctx.widgets.getWidget(312, 15).getDynamicChildren()[2];
        if (smithingWindow != null && !smithingWindow.isHidden()) {
            if (bodyWindow != null && !bodyWindow.isHidden()){
                if (ctx.getClient().getLocalPlayer().getAnimation() == -1) {
                    bodyWindow.click(5);
                    ctx.sleep(2500);
                }
            }
        }
        }

    public void paint(Graphics g1) {
        Graphics2D g = (Graphics2D) g1;

        g.setColor(Color.BLACK);
        g.fillRect(5, 2, 192, 58);
        g.setColor(Color.decode("#ea411c"));
        g.drawRect(5, 2, 192, 58);
        g.drawLine(8, 24, 194, 24);

        g.setColor(Color.decode("#e0ad01"));
        g.drawString("RBarrows                             v. " + "0.1", 12, 20);
        g.drawString("Time: " + ctx.paint.formatTime(System.currentTimeMillis() - startTime), 14, 42);
        g.drawString("Status: " + status, 14, 56);
    }
}