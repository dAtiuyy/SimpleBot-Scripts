package DZoneMiner;

import simple.api.events.ChatMessageEvent;
import simple.api.filters.SimpleSkills;
import simple.api.listeners.SimpleMessageListener;
import simple.api.script.Category;
import simple.api.script.Script;
import simple.api.script.ScriptManifest;
import simple.api.script.interfaces.SimplePaintable;
import simple.api.wrappers.*;
import javax.swing.*;
import java.awt.*;
import java.awt.Graphics2D;

@ScriptManifest(
        author = "alex",
        category = Category.MINING,
        description = "<html>"
                + "<p><strong>DZone Miner</strong>.</p>"
                + "<ul>"
                + "<li><strong>A simple af DZone Miner for you to use</strong>.</li>"
                + "<li><strong>It supports all the ores available at ::dzone and AMETHYST at Max Guild, please have your last withdrawn loudout to have the required tool, a pickaxe</strong>.</li>"
                + "<li>Start this at ::dzone and have your last withdrawn loudout to whatever gear you want to be in when you fish.</li>"
                + "<li>This way the bot can use XP tokens and not have any problems with them.</li>"
                + "<li>This script features a cute, <strong>USELESS</strong>, gui which allows you to stop and start the script (the gui disappears after you stop the script).</li>"
                + "</ul>"
                + "</html>",
        discord = "",
        name = "DZone Miner",
        servers = {"RuneX"},
        version = "1.0"
)

public class DZoneMinerMain extends Script implements SimpleMessageListener, SimplePaintable {
    public long startTime;
    public String status;
    public int startExperience;
    private DZoneMinerGUI gui;
    private static boolean hidePaint = false;
    @Override
    public void onChatMessage(ChatMessageEvent chatMessageEvent) {
    }
    @Override
    public boolean onExecute() {
        this.startTime = System.currentTimeMillis();
        this.startExperience = ctx.skills.getExperience(SimpleSkills.Skill.MINING);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                gui = new DZoneMinerGUI();
            }
        });
        return true;
    }
    @Override
    public void onTerminate() {
        if (gui != null) {
            gui.onCloseGUI();
        }
    }
    @Override
    public void onProcess() {
        String selectedItem = gui.getSelectedItem();
        SimpleSceneObject coal = ctx.objects.populate().filter(2097).nextNearest();
        SimpleSceneObject gold = ctx.objects.populate().filter(2099).nextNearest();
        SimpleSceneObject mith = ctx.objects.populate().filter(2102).nextNearest();
        SimpleSceneObject addy = ctx.objects.populate().filter(2105).nextNearest();
        SimpleSceneObject rune = ctx.objects.populate().filter(2107).nextNearest();
        SimpleSceneObject amethyst = ctx.objects.populate().filter(3042).nextNearest();
        SimpleSceneObject bankchest = ctx.objects.populate().filter("Bank chest").nextNearest();
        SimpleNpc banker = ctx.npcs.populate().filter("Banker").nextNearest();
        if (gui != null && gui.isScriptStarted()) {
            if (ctx.inventory.inventoryFull()) {
                if (bankchest != null) {
                    bankchest.interact(900);
                } else if (banker != null) {
                    banker.interact(225);
                }
                status("Deposit");
            }
            if (!ctx.inventory.inventoryFull() && ctx.players.getLocal().getAnimation() == -1) {
                if (coal != null && "Coal".equals(selectedItem)) {
                    coal.interact(502);
                    ctx.onCondition(() -> ctx.players.getLocal().getInteracting() != null, 250, 6);
                    status("Mining Coal");
                }
                if (gold != null && "Gold".equals(selectedItem)) {
                    gold.interact(502);
                    ctx.onCondition(() -> ctx.players.getLocal().getInteracting() != null, 250, 6);
                    status("Mining Gold");
                }
                if (mith != null && "Mithril".equals(selectedItem)) {
                    mith.interact(502);
                    ctx.onCondition(() -> ctx.players.getLocal().getInteracting() != null, 250, 6);
                    status("Mining Mithril");
                }
                if (addy != null && "Adamantite".equals(selectedItem)) {
                    addy.interact(502);
                    ctx.onCondition(() -> ctx.players.getLocal().getInteracting() != null, 250, 6);
                    status("Mining Adamantite");
                }
                if (rune != null && "Runite".equals(selectedItem)) {
                    rune.interact(502);
                    ctx.onCondition(() -> ctx.players.getLocal().getInteracting() != null, 250, 6);
                    status("Mining Runite");
                }
                if (amethyst != null && "Amethyst".equals(selectedItem)) {
                    amethyst.interact(502);
                    ctx.onCondition(() -> ctx.players.getLocal().getInteracting() != null, 250, 6);
                    status("Mining Amethyst");
                }
            }
        }
    }
    public void status(final String status) {
        this.status = status;
    }
    @Override
    public void onPaint(Graphics2D g1) {
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

            Font font1 = new Font("Karla", Font.BOLD, 10);
            g.setFont(font1);
            g.setColor(Color.GRAY);
            g.drawString("Time: " + ctx.paint.formatTime(System.currentTimeMillis() - startTime), 380, 286);
            g.drawString("Status: " + status, 380, 298);
            int totalExp = ctx.skills.getExperience(SimpleSkills.Skill.MINING) - startExperience;
            g.drawString("XP Earned: " + ctx.paint.formatValue(totalExp), 380, 308);
            Font font2 = new Font("Karla", Font.BOLD, 12);
            g.setFont(font2);
            g.setColor(Color.WHITE);
            g.drawString("DZone Miner  v. " + "1.0", 380, 274);
        }
    }
}
