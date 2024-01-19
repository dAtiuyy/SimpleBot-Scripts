package daPrayers;

import net.runelite.api.ChatMessageType;
import simple.hooks.filters.SimpleBank;
import simple.hooks.filters.SimpleSkills;
import simple.hooks.scripts.Category;
import simple.hooks.scripts.LoopingScript;
import simple.hooks.scripts.ScriptManifest;
import simple.hooks.scripts.task.Task;
import simple.hooks.scripts.task.TaskScript;
import simple.hooks.simplebot.ChatMessage;
import simple.hooks.wrappers.SimpleItem;
import simple.hooks.wrappers.SimpleObject;
import simple.hooks.wrappers.SimplePlayer;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@ScriptManifest(
        author = "alex && Esmaabi",
        category = Category.HERBLORE,
        description = "<html>"
                + "<p>The most effective anvil herblore bot on Zenyte!</p>"
                + "<p><strong>Features & recommendations:</strong></p>"
                + "<ul>"
                + "<li>Start with a <strong>empty inventory</strong>.</li>"
                + "<li>Start <strong>near bank booth or bank chest</strong>.</li>"
                + "<li>Have unfinished potions and secondaries visible in bank</li>"
                + "<li>Zoom in to <strong>see bank close</strong> for better performance.</li>"
                + "<li>At the moment only <strong>making potions</strong> supported.</li>"
                + "</ul>"
                + "</html>",
        discord = "Esmaabi#5752",
        name = "eHerbloreBotZenyte",
        servers = {"Battlescape"},
        version = "0.3"
)

public class daMain extends TaskScript implements LoopingScript {

    private static final String[] BANK_NAME = {"Bank booth", "Bank chest"};

    // Variables
    private long startTime = 0L;
    private long startingSkillLevel;
    private long startingSkillExp;
    private long currentExp;
    private int count;
    private int unfinishedPotionID;
    public static String status = null;
    public static int returnItem;
    private static String nameOfItem = null;
    public int secondIngrediente;
    private long lastAnimation = -1;
    public static boolean botStarted;
    private static boolean hidePaint = false;
    private boolean makingSuperCombat = false;
    private boolean makingStamingPotions = false;
    private static String playerGameName;

    // Gui
    private static daGui gui;
    private void initializeGUI() {
        gui = new daGui();
        gui.setVisible(true);
        gui.setLocale(ctx.getClient().getCanvas().getLocale());
    }

    public enum PotionItems {
        PRAYER_POTIONS("prayer potions", 99, 231);
        private final String nameOfItem;
        private final int unfinishedPotionID;
        private final int secondIngrediente;

        PotionItems(String nameOfItem, int unfinishedPotionID, int secondIngrediente) {
            this.nameOfItem = nameOfItem;
            this.unfinishedPotionID = unfinishedPotionID;
            this.secondIngrediente = secondIngrediente;
        }

        public String getNameOfItem() {
            return nameOfItem;
        }

        public int getUnfinishedPotionID() {
            return unfinishedPotionID;
        }

        public int getSecondIngrediente() {
            return secondIngrediente;
        }
    }

    // Tasks
    private final List<Task> tasks = new ArrayList<>();

    @Override
    public boolean prioritizeTasks() {
        return true;
    }

    @Override
    public List<Task> tasks() {
        return tasks;
    }

    @Override
    public void onExecute() {
        initializeGUI();

        // Other vars
        System.out.println("Started eHerbloreBot!");
        this.ctx.updateStatus("--------------- " + currentTime() + " ---------------");
        this.ctx.updateStatus("-------------------------------");
        this.ctx.updateStatus("           eHerbloreBot        ");
        this.ctx.updateStatus("-------------------------------");

        // Vars
        updateStatus("Setting up bot");
        this.startTime = System.currentTimeMillis();
        this.startingSkillLevel = this.ctx.skills.realLevel(SimpleSkills.Skills.HERBLORE);
        this.startingSkillExp = this.ctx.skills.experience(SimpleSkills.Skills.HERBLORE);
        currentExp = this.ctx.skills.experience(SimpleSkills.Skills.HERBLORE);
        lastAnimation = System.currentTimeMillis();
        botStarted = false;
        unfinishedPotionID = 0;
        secondIngrediente = 0;
        makingSuperCombat = false;
        makingStamingPotions = false;
        count = 0;
        ctx.viewport.angle(270);
        ctx.viewport.pitch(true);
        returnItem = PotionItems.PRAYER_POTIONS.ordinal();
    }

    @Override
    public void onProcess() {
        super.onProcess();

        if (!botStarted) {
            getTaskItem();
            return;
        }

        if (currentExp != this.ctx.skills.experience(SimpleSkills.Skills.HERBLORE)) {
            count++;
            currentExp = this.ctx.skills.experience(SimpleSkills.Skills.HERBLORE);
        }

        if (ctx.dialogue.dialogueOpen() && !makingStamingPotions) {
            int SPACE_BUTTON = KeyEvent.VK_SPACE;
            ctx.keyboard.clickKey(SPACE_BUTTON);
        }

        SimplePlayer localPlayer = ctx.players.getLocal();
        boolean playerIsAnimating = localPlayer.isAnimating();
        if (makingStamingPotions) {
            boolean unfPotionInv = !ctx.inventory.populate().filter(unfinishedPotionID).isEmpty();
            boolean secondIngredienteInv = !ctx.inventory.populate().filter(secondIngrediente).isEmpty();

            if (unfPotionInv && secondIngredienteInv) {
                if (ctx.bank.bankOpen()) {
                    updateStatus("Closing bank task");
                    ctx.bank.closeBank();
                }
                herbStaminaTask();
            } else {
                openingBank();
            }
        } else {

            boolean unfPotionInv = !ctx.inventory.populate().filter(unfinishedPotionID).isEmpty();
            boolean secondIngredienteInv = !ctx.inventory.populate().filter(secondIngrediente).isEmpty();

            if (unfPotionInv && secondIngredienteInv) {
                if (ctx.bank.bankOpen()) {
                    updateStatus("Closing bank task");
                    ctx.bank.closeBank();
                }

                if (!playerIsAnimating && (System.currentTimeMillis() > (lastAnimation + 3000))) {
                    herbTask();
                } else if (playerIsAnimating) {
                    lastAnimation = System.currentTimeMillis();
                }
            } else {
                openingBank();
            }
        }
    }

    // Banking

    private void openingBank() {
        if (ctx.bank.bankOpen()) {
            updateStatus("Depositing items");
            ctx.bank.depositAllExcept(12640); // Amylase
            updateStatus("Withdrawing herblore supplies");
            if (makingStamingPotions) {
                ctx.bank.withdraw(unfinishedPotionID, 27);
                ctx.bank.withdraw(secondIngrediente, SimpleBank.Amount.ALL);
            } else if (makingSuperCombat) {
                ctx.bank.withdraw(unfinishedPotionID, 7);
                ctx.bank.withdraw(2440, 7);
                ctx.bank.withdraw(2436, 7);
                ctx.bank.withdraw(2442, 7);
            } else {
                ctx.bank.withdraw(unfinishedPotionID, 14);
                ctx.bank.withdraw(secondIngrediente, 14);
            }
            ctx.onCondition(() -> ctx.inventory.populate().population() > 14, 3000);
            updateStatus("Closing bank");
            ctx.bank.closeBank();
            return;
        }

        if (!ctx.bank.bankOpen() && !ctx.players.getLocal().isAnimating()) {
            SimpleObject bankChest = getBankChest();
            if (bankChest != null) {
                updateStatus("Refilling supplies");
                bankChest.click(1);
                ctx.onCondition(() -> ctx.bank.bankOpen(), 5000);
            }
        }
    }

    private SimpleObject getBankChest() {
        SimpleObject bankChest = ctx.objects.populate().filter(BANK_NAME).nearest().next();
        if (bankChest != null && bankChest.distanceTo(ctx.players.getLocal()) <= 10 && bankChest.validateInteractable()) {
            return bankChest;
        }
        return null;
    }


    // Herblore

    private void herbTask() {
        SimpleItem unfPotionInv = ctx.inventory.populate().filter(unfinishedPotionID).next();
        SimpleItem secondIngredienteInv = ctx.inventory.populate().filter(secondIngrediente).next();
        boolean itemsNotNull = unfPotionInv != null || secondIngredienteInv != null;

        if (!makingStamingPotions) {
            if (ctx.players.getLocal().isAnimating()) {
                return;
            }
        }

        if (!itemsNotNull) {
            openingBank();
        } else {
            updateStatus("Making " + nameOfItem);
            ctx.inventory.itemOnItem(unfPotionInv, secondIngredienteInv);
            lastAnimation = System.currentTimeMillis();
        }
    }

    private void herbStaminaTask() {
        SimpleItem unfPotionInv = ctx.inventory.populate().filter(unfinishedPotionID).reverse().next();
        SimpleItem secondIngredienteInv = ctx.inventory.populate().filter(secondIngrediente).next();
        boolean itemsNotNull = unfPotionInv != null || secondIngredienteInv != null;

        if (!itemsNotNull) {
            openingBank();
        } else {
            if (!ctx.dialogue.dialogueOpen()) {
                updateStatus("Making " + nameOfItem);
                ctx.inventory.itemOnItem(unfPotionInv, secondIngredienteInv);
            } else {
                int SPACE_BUTTON = KeyEvent.VK_SPACE;
                ctx.keyboard.clickKey(SPACE_BUTTON);
                ctx.sleepCondition(() -> unfPotionInv == null, 30000);
            }
        }
    }

    private void herbSuperCombatsTask() {
        SimpleItem unfPotionInv = ctx.inventory.populate().filter(unfinishedPotionID).next();
        SimpleItem superStrenght = ctx.inventory.populate().filter(2440).next();
        SimpleItem superAttack = ctx.inventory.populate().filter(2436).next();
        SimpleItem superDefence = ctx.inventory.populate().filter(2442).next();
        boolean itemsNotNull = unfPotionInv != null && superStrenght != null && superAttack != null && superDefence != null;

        if (ctx.players.getLocal().isAnimating()) {
            return;
        }

        if (!itemsNotNull) {
            openingBank();
        } else {
            updateStatus("Making " + nameOfItem);
            ctx.inventory.itemOnItem(unfPotionInv, superStrenght);
            lastAnimation = System.currentTimeMillis();
        }
    }

    private void getTaskItem() {
        PotionItems item = PotionItems.values()[returnItem];
        nameOfItem = item.getNameOfItem();
        unfinishedPotionID = item.getUnfinishedPotionID();
        secondIngrediente = item.getSecondIngrediente();
    }

    //Utility
    public static String currentTime() {
        return LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    private void updateStatus(String newStatus) {
        status = newStatus;
        ctx.updateStatus(status);
        System.out.println(status);
    }

    public String getPlayerName() {
        if (playerGameName == null) {
            playerGameName = ctx.players.getLocal().getName();
        }
        return playerGameName;
    }

    @Override
    public void onTerminate() {

        // Other vars
        this.startingSkillLevel = 0L;
        this.startingSkillExp = 0L;
        this.count = 0;
        secondIngrediente = 0;
        unfinishedPotionID = 0;
        makingSuperCombat = false;
        makingStamingPotions = false;
        gui.setVisible(false);

        this.ctx.updateStatus("-------------- " + currentTime() + " --------------");
        this.ctx.updateStatus("----------------------");
        this.ctx.updateStatus("Thank You & Good Luck!");
        this.ctx.updateStatus("----------------------");
    }

    @Override
    public void onChatMessage(ChatMessage m) {
        ChatMessageType getType = m.getType();
        net.runelite.api.events.ChatMessage getEvent = m.getChatEvent();
        playerGameName = getPlayerName();

        if (m.getMessage() == null) {
            return;
        }

        if (getType == ChatMessageType.GAME) {
            String senderName = getEvent.getName();

            // Remove any text within angle brackets and trim
            senderName = senderName.replaceAll("<[^>]+>", "").trim();

            if (senderName.contains(playerGameName)) {
                ctx.updateStatus(currentTime() + " Someone asked for you");
                ctx.updateStatus(currentTime() + " Stopping script");
                ctx.stopScript();
            }

        }
    }

    @Override
    public int loopDuration() {
        return 150;
    }

    @Override
    public void paint(Graphics g) {
        // Check if mouse is hovering over the paint
        Point mousePos = ctx.mouse.getPoint();
        if (mousePos != null) {
            Rectangle paintRect = new Rectangle(5, 120, 200, 110);
            hidePaint = paintRect.contains(mousePos.getLocation());
        }

        // Get runtime and skill information
        long runTime = System.currentTimeMillis() - this.startTime;
        long currentSkillLevel = this.ctx.skills.realLevel(SimpleSkills.Skills.HERBLORE);
        long currentSkillExp = this.ctx.skills.experience(SimpleSkills.Skills.HERBLORE);
        long skillLevelsGained = currentSkillLevel - this.startingSkillLevel;
        long skillExpGained = currentSkillExp - this.startingSkillExp;

        // Calculate experience and actions per hour
        long skillExpPerHour = skillExpGained * 3600000L / runTime;
        long actionsPerHour = count * 3600000L / (System.currentTimeMillis() - this.startTime);

        // Set up colors
        Color philippineRed = new Color(196, 18, 48);
        Color raisinBlack = new Color(35, 31, 32, 127);

        // Draw paint if not hidden
        if (!hidePaint) {
            g.setColor(raisinBlack);
            g.fillRoundRect(5, 120, 200, 110, 20, 20);

            g.setColor(philippineRed);
            g.drawRoundRect(5, 120, 200, 110, 20, 20);

            g.setColor(philippineRed);
            g.drawString("eHerbloreBot by Esmaabi", 15, 135);
            g.setColor(Color.WHITE);
            g.drawString("Runtime: " + formatTime(runTime), 15, 150);
            g.drawString("Skill Level: " + this.startingSkillLevel + " (+" + skillLevelsGained + "), started at " + currentSkillLevel, 15, 165);
            g.drawString("Current Exp: " + currentSkillExp, 15, 180);
            g.drawString("Exp gained: " + skillExpGained + " (" + (skillExpPerHour / 1000L) + "k xp/h)", 15, 195);
            g.drawString("Potions made: " + count + " (" + actionsPerHour + " per/h)", 15, 210);
            g.drawString("Status: " + status, 15, 225);

        }
    }

    private String formatTime(long ms) {
        long s = ms / 1000L;
        long m = s / 60L;
        long h = m / 60L;
        s %= 60L;
        m %= 60L;
        h %= 24L;
        return String.format("%02d:%02d:%02d", h, m, s);
    }

}