package daZobbers;

import simple.hooks.filters.SimpleSkills;
import simple.hooks.scripts.Category;
import simple.hooks.scripts.LoopingScript;
import simple.hooks.scripts.ScriptManifest;
import simple.hooks.simplebot.ChatMessage;
import simple.hooks.wrappers.SimpleNpc;
import simple.hooks.wrappers.SimpleObject;
import simple.hooks.wrappers.SimpleWidget;
import simple.robot.script.Script;

import java.awt.*;

@ScriptManifest(
        author = "alex",
        category = Category.THIEVING,
        description = "<html>"
                + "<p>Dumb master farmer thief for Zaros</p>"
                + "<p><strong>Performs pickpockets</strong></p>"
                + "<ul>"
                + "<li><strong>Start @ the master farmer at Farming Guild</strong>.</li>"
                + "<li><strong>Drops the useless shit and keeps the good shit</strong></li>"
                + "</ul>"
                + "</html>",
        discord = "",
        name = "daZobber",
        servers = {"Zaros"},
        version = "4.0"
)

public class daZobberMain extends Script implements LoopingScript {
    public String status;
    public long startTime;
    public int startExperience, times_stole;
    private static final int DA_VICTIM_ID = 5730;
    public int[] itemIds = {1,2};
    private static boolean hidePaint = false;

    @Override
    public void onExecute() {
        System.out.println("Started daZobber");
        this.startExperience = ctx.skills.experience(SimpleSkills.Skills.THIEVING);
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void onProcess() {
        SimpleWidget seedVWidget = ctx.widgets.getWidget(631, 3);
        SimpleWidget depositInvButton = ctx.widgets.getWidget(631, 25);
        SimpleObject seedVObject = ctx.objects.populate().filter("Seed vault").nearest().next();
        SimpleNpc pickpocket_ore = ctx.npcs.populate().filter(DA_VICTIM_ID).nearest().next();

        if (pickpocket_ore != null && !ctx.inventory.inventoryFull()) {
            pickpocket_ore.click(0);
            status("Zobbing");
        }

        if (ctx.inventory.inventoryFull() && seedVObject != null) {
            seedVObject.click(0);
            if (!seedVWidget.isHidden() && seedVWidget != null) {
                status("Banking 1");
                ctx.sleepCondition(() -> seedVWidget.isHidden(), 2500);
                if (!depositInvButton.isHidden() && depositInvButton != null) {
                    depositInvButton.click(0);
                    ctx.sleepCondition(() -> !ctx.inventory.inventoryFull(), 2500);
                }
            }
        }

        if (seedVWidget != null) {
            if (!seedVWidget.isHidden() && !ctx.inventory.inventoryFull()) {
                ctx.pathing.step(pickpocket_ore.getLocation());
            }
        }
    }

    /*
    private void clickPreset() {
        status("preset moment");
        SimpleWidget preset = ctx.widgets.getWidget(702, 38);
        SimpleWidget questTab = ctx.widgets.getWidget(548, 50);
        SimpleWidget loadOuts = ctx.widgets.getWidget(702, 5);
        if (questTab != null && !questTab.isHidden()) {
            questTab.click(0);
            if (loadOuts != null && !loadOuts.isHidden()) {
                loadOuts.click(0);
                if (preset != null && !preset.isHidden()) {
                    preset.click(0);
                }
            }
        }
    }
    if invFull && seedObj!=null {
    clilc.obj
    if !widget.isHidden && !null

    }

     */

    @Override
    public void onChatMessage(ChatMessage m) {
        if (m.getMessage() != null) {
            String message = m.getMessage().toLowerCase();
            if (message.contains("ou pick the")) {
                times_stole++;
            }
        }
    }

    @Override
    public void onTerminate() {
        System.out.println("You stole this many times:");
        System.out.println(times_stole);
    }

    public void status(final String status) {
        this.status = status;
    }

    public void paint(Graphics g1) {
        // Check if mouse is hovering over the paint
        Point mousePos = ctx.mouse.getPoint();
        if (mousePos != null) {
            Rectangle paintRect = new Rectangle(368, 260, 150, 75);
            hidePaint = paintRect.contains(mousePos.getLocation());
        }

        Graphics2D g = (Graphics2D) g1;

        if (!hidePaint) {
            g.setColor(Color.DARK_GRAY);
            g.fillRect(368, 260, 150, 75);
            g.setColor(Color.BLACK);
            g.drawRect(368, 260, 150, 75);

            Font font1 = new Font("Karla", Font.BOLD, 10); // Adjust the font family, style, and size as desired
            g.setFont(font1);
            g.setColor(Color.GRAY);
            g.drawString("Time: " + ctx.paint.formatTime(System.currentTimeMillis() - startTime), 380, 286);
            g.drawString("Status: " + status, 380, 298);
            int totalExp = ctx.skills.experience(SimpleSkills.Skills.THIEVING) - startExperience;
            g.drawString("XP: " + ctx.paint.formatValue(totalExp) + " (" + ctx.paint.valuePerHour(totalExp / 1000, startTime) + "k)", 380, 308);
            g.drawString("Pickpockets: " + ctx.paint.formatValue(times_stole) + " (" + ctx.paint.valuePerHour(times_stole, startTime) + ")", 380, 320);
            Font font2 = new Font("Karla", Font.BOLD, 12); // Adjust the font family, style, and size as desired
            g.setFont(font2);
            g.setColor(Color.WHITE);
            g.drawString("daZobber  v. " + "4.0", 380, 274);
        }
    }

    @Override
    public int loopDuration() {
        return 300;
    }
}