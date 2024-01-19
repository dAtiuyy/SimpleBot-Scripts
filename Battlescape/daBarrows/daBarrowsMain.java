package daBarrows;

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
import simple.robot.utils.WorldArea;

@ScriptManifest(author = "alex", category = Category.MONEYMAKING, description = "", discord = "",
        name = "daBarrows", servers = { "Battlescape" }, version = "2.0")

public class daBarrowsMain extends Script {
    public String status;
    public long startTime;
    private boolean presetClicked = false; // 20973 is the chest @ barrows
    private boolean killedVerac = false;

    public static final WorldArea HOME = new WorldArea(
            new WorldPoint(3085, 3493, 0),
            new WorldPoint(3105, 3513, 0));

    public static final WorldArea BARROWS_AREA = new WorldArea(
            new WorldPoint(3545, 3320, 0),
            new WorldPoint(3585, 3267, 0));

    public static final WorldArea VERAC_AREA = new WorldArea(
            new WorldPoint(3588, 9696, 3),
            new WorldPoint(3568, 9716, 3));

    @Override
    public void onExecute() {
        System.out.println("Started daBarrows");
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void onProcess() {
        status("in da first");
        SimpleItem seedPod = ctx.inventory.populate().filter(19564).next();
        SimplePrayers prayers = new SimplePrayers(ctx);
        SimpleItem potion = ctx.inventory.populate().filter(Pattern.compile("Prayer potion\\(\\d+\\)")).next();
        SimpleItem spade = ctx.inventory.populate().filter(952).next();


        if (!presetClicked && HOME.containsPoint(ctx.players.getLocal())) {
            clickPreset();
            presetClicked = true;
        }

        if (HOME.containsPoint(ctx.players.getLocal())){
            ctx.teleporter.open();
            ctx.teleporter.teleportStringPath("Minigames","Barrows");
            status("go barrows moment");
        }

        if (BARROWS_AREA.containsPoint(ctx.players.getLocal()) && seedPod != null && potion == null  && prayers.points() <= 20){
            seedPod.click(0);
            status("go home moment");
            presetClicked = false;
        }

        if (BARROWS_AREA.containsPoint(ctx.players.getLocal()) && !killedVerac) {
            status("trying to skip verac 1st");
            skipVerac();
            presetClicked = false;
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
        SimpleWidget offtaskBarrows = ctx.widgets.getWidget(702, 87);
        SimpleWidget questTab = ctx.widgets.getWidget(548, 50);
        SimpleWidget loadOuts = ctx.widgets.getWidget(702, 5);
        if (questTab != null && !questTab.isHidden()){
            questTab.click(0);
            if (loadOuts != null && !loadOuts.isHidden()){
                loadOuts.click(0);
                if (offtaskBarrows != null && !offtaskBarrows.isHidden()) {
                    offtaskBarrows.click(0);
                }
            }
        }
    }

    private void skipVerac() {
        status("skipping verac");
        ctx.pathing.step(new WorldPoint(3557, 3300, 0));
        SimpleItem spade = ctx.inventory.populate().filter(952).next();
        SimpleItem skipItem = ctx.inventory.populate().filter(60038).next();
        WorldPoint currentLocation = ctx.players.getLocal().getLocation();
        SimpleObject veracCoffin = ctx.objects.populate().filter(20772).nearest().next();
        SimpleObject sStairs = ctx.objects.populate().filter(20672).nearest().next();

        if (currentLocation.equals(new WorldPoint(3557, 3300, 0))) {
            spade.click(0);
            status("move skip verac");
        }
        if (currentLocation.equals(new WorldPoint(3578, 9706, 3)) && skipItem != null){
            skipItem.click(0);
            veracCoffin.click(0);
            ctx.sleepCondition(() -> ctx.pathing.inMotion() == true, 2000);
            status("skip varoc click");
        }
        if (currentLocation.equals(new WorldPoint(3578, 9706, 3)) ){
            sStairs.click(0);
            status("leaving verac crypt");
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