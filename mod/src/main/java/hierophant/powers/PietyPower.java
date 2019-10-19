package hierophant.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.actions.utility.HideHealthBarAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.random.Random;

import hierophant.HierophantMod;
import hierophant.patches.EscapeFieldPatch;
import hierophant.util.TextureLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static hierophant.HierophantMod.makePowerPath;
import static java.lang.Math.ceil;

import java.util.HashMap;
import java.util.Map;

// Defeat any enemies with lower or equal HP than your Piety at end of turn.
public class PietyPower extends AbstractPower implements CloneablePowerInterface {

    public static final Logger logger = LogManager.getLogger(HierophantMod.class.getName());
    public AbstractCreature source;

    public static final String POWER_ID = HierophantMod.makeID("PietyPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("piety_big.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("piety_small.png"));
    private static final Map<String, String[]> dialogs = new HashMap<String, String[]>();

    public static final int REDUCTION_FACTOR = 3;

    public PietyPower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
        dialogs.put("Cultist", new String[]{"CA-CAW! HAIL THE NEW GODS! CA-CAW!", "CA-CAW! THE LIGHT SHINES!"});
        dialogs.put("SlaverBlue", new String[]{"Slavery isn't my thing anyway..."});
        dialogs.put("SlaverRed", new String[]{"I was the slave all along!"});
        dialogs.put("Champ", new String[]{"ALL THIS VIOLENCE! ALL FOR NOTHING!", "SKY IS THE LIMIT!"});
        dialogs.put("GremlinLeader", new String[]{"My children, what have I done?"});
        dialogs.put("Chosen", new String[]{"Will I ever atone for my sins?"});
        dialogs.put("Mystic", new String[]{"Your Gods and mine are all but the same."});
        dialogs.put("Centurion", new String[]{"I should've listened to her..."});
        dialogs.put("GiantHead", new String[]{"Hello... Theism..."});
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        this.source = source;

        type = PowerType.BUFF;
        isTurnBased = true;

        // We load those txtures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        this.description = DESCRIPTIONS[0];

        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount)
    {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        if (this.amount == 0) {
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
    }

    private void determinationHeal(int healthRemaining) {
        AbstractPlayer p = AbstractDungeon.player;
        if (p.hasPower(DeterminationPower.POWER_ID)) {
            AbstractPower determination = p.getPower(DeterminationPower.POWER_ID);
            determination.flash();
            AbstractDungeon.actionManager.addToBottom(
                    new HealAction(p, p, (determination.amount * healthRemaining) / 3));
        }
    }

    public void pietyDialog(AbstractMonster mo) {
        Random rand = new Random();
        int chance = 30;

        if (rand.random(100) > chance)  {
            return;
        }

        String[] dialogStrings = dialogs.get(mo.id);
        if (dialogStrings != null) {
            String dialog = dialogStrings[rand.random(0, dialogStrings.length)];
            AbstractDungeon.actionManager.addToBottom(new TalkAction(mo, dialog, 1.0F, 2.0F));
        }
    }

    @Override
    public void atEndOfTurn(final boolean isPlayer) {
        boolean allMonstersPacified = true;
        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if ((mo.currentHealth <= this.amount) && (mo.currentHealth > 0)) {
                this.flash();
                determinationHeal(mo.currentHealth);
                pietyDialog(mo);
                AbstractDungeon.actionManager.addToTop(new HideHealthBarAction(mo));
                AbstractDungeon.actionManager.addToBottom(new SuicideAction(mo));
                EscapeFieldPatch.escapingPiety.set(mo, true);
            } else if (mo.currentHealth > 0) {
                allMonstersPacified = false;
            }
        }

        if (allMonstersPacified) {
            // Prevent darkling softlock
            AbstractDungeon.getCurrRoom().cannotLose = false;
        }

        int toReduce = (int)ceil(amount / (float)REDUCTION_FACTOR);
        AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, POWER_ID, toReduce));
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new PietyPower(owner, source, amount);
    }
}
