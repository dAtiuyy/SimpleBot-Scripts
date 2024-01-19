package LogBonfire;

import simple.api.coords.WorldArea;
import simple.api.coords.WorldPoint;
import simple.api.events.ChatMessageEvent;
import simple.api.filters.SimplePrayers;
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
import java.awt.event.KeyEvent;
import java.util.regex.Pattern;

@ScriptManifest(
        author = "alex",
        category = Category.FIREMAKING,
        description = "<html>"
                + "<p><strong>Log To Bonfire</strong>.</p>"
                + "<ul>"
                + "</ul>"
                + "</html>",
        discord = "",
        name = "LogBonfire",
        servers = {"RuneX"},
        version = "1.0"
)

public class LogBonfireMain extends Script implements SimpleMessageListener, SimplePaintable {
    public long startTime;
    public String status;
    public int startExperience;
    private LogBonfireGUI gui;
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
                gui = new LogBonfireGUI();
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
        int SPACE_BUTTON = KeyEvent.VK_SPACE;
        String selectedItem = gui.getSelectedItem();
        SimpleSceneObject bank = ctx.objects.populate().filter("Bank Chest", "Bank Booth").nextNearest();
        SimpleSceneObject bonfire = ctx.objects.populate().filter(5249).nextNearest();
        SimpleItem log = ctx.inventory.populate().filter("Logs", "Oak logs", "Willow logs", "Maple logs", "Yew logs", "Magic logs", "Redwood logs").next();
        if (gui != null && gui.isScriptStarted()) {
            if (log == null) {
                bank.interact(900);
                status("restock");
            }
            if (log != null && bonfire != null){
                log.interact(447);
                bonfire.interact(62);
                status("interact");
                //ctx.onCondition(() -> !ctx.dialogue.dialogueOpen(), 250, 12);
            }
            if (ctx.dialogue.dialogueOpen() && !ctx.players.getLocal().isAnimating()) {
                ctx.keyboard.clickKey(SPACE_BUTTON);
                //ctx.onCondition(() -> !ctx.players.getLocal().isAnimating(), 250, 12);
                status("bruh");
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
            int totalExp = ctx.skills.getExperience(SimpleSkills.Skill.FIREMAKING) - startExperience;
            Font font2 = new Font("Karla", Font.BOLD, 12);
            g.setFont(font2);
            g.setColor(Color.WHITE);
            g.drawString("LogBonfire  v. " + "1.0", 380, 274);
        }
    }
}
