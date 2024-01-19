package daGemCutter;

import java.awt.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import simple.hooks.scripts.Category;
import simple.hooks.scripts.ScriptManifest;
import simple.hooks.simplebot.ChatMessage;
import simple.hooks.wrappers.SimpleItem;
import simple.hooks.wrappers.SimpleWidget;
import simple.robot.script.Script;

@ScriptManifest(
        author = "alex",
        category = Category.CRAFTING,
        description = "<html>"
                + "<p>Dumb gem cutter for Battlescape</p>"
                + "<p><strong>cuts gems</strong></p>"
                + "<ul>"
                + "<li>Start <strong>on a preset ok tile</strong>.</li>"
                + "<li><strong>Have your first preset set to have 27 gems and a chisel</strong></li>"
                + "</ul>"
                + "</html>",
        discord = "",
        name = "daGemCutter",
        servers = {"Battlescape"},
        version = "2.0"
)

public class daGemCutterMain extends Script {
    public String status;
    public long startTime;
    private static boolean hidePaint = false;

    @Override
    public void onExecute() {
        System.out.println("Started daGemCutter");
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void onProcess() {
        status("Cutting");
        SimpleItem chisel = ctx.inventory.populate().filter(1755).next();
        SimpleItem gem = ctx.inventory.populate().filter(1617).next();


        if (chisel == null || gem == null) {
            clickPreset();
        }

        if (chisel != null && gem != null && !ctx.players.getLocal().isAnimating()) {
            chisel.click(0);
            gem.click(0);
            if (ctx.dialogue.dialogueOpen()) {
                int SPACE_BUTTON = KeyEvent.VK_SPACE;
                ctx.keyboard.clickKey(SPACE_BUTTON);
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
            Font font2 = new Font("Karla", Font.BOLD, 12); // Adjust the font family, style, and size as desired
            g.setFont(font2);
            g.setColor(Color.WHITE);
            g.drawString("daGemCutter  v. " + "2.0", 380, 274);
        }
    }
}