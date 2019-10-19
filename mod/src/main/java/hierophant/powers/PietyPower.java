package hierophant.powers;

import static hierophant.HierophantMod.makePowerPath;
import static java.lang.Math.ceil;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import basemod.interfaces.CloneablePowerInterface;
import hierophant.HierophantMod;
import hierophant.util.TextureLoader;

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

    public static final int REDUCTION_FACTOR = 3;

    public PietyPower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
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

    @Override
    public void atEndOfTurn(final boolean isPlayer) {
        boolean allMonstersPacified = true;
        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if ((mo.currentHealth <= this.amount) && (mo.currentHealth > 0)) {
                this.flash();
                determinationHeal(mo.currentHealth);
                AbstractDungeon.actionManager.addToTop(new HideHealthBarAction(mo));
                AbstractDungeon.actionManager.addToBottom(new SuicideAction(mo));
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
