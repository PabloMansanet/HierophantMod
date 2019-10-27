package hierophant.powers;

import static hierophant.HierophantMod.makePowerPath;
import static java.lang.Math.ceil;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.FlameParticleEffect;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import basemod.interfaces.CloneablePowerInterface;
import hierophant.HierophantMod;
import hierophant.cards.FieryStrike;
import hierophant.cards.FlamingChariot;
import hierophant.cards.ThreeLashes;
import hierophant.effects.FervorFlameEffect;
import hierophant.util.TextureLoader;

public class FervorPower extends AbstractPower implements CloneablePowerInterface {
    public static final Logger logger = LogManager.getLogger(HierophantMod.class.getName());
    public AbstractCreature source;

    public static final String POWER_ID = HierophantMod.makeID("FervorPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("fervor_big.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("fervor_small.png"));

    public FervorPower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        this.source = source;

        type = PowerType.BUFF;
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

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type)
    {
        if (type == DamageInfo.DamageType.NORMAL) {
            return (float) ceil(damage * (this.amount * 10 + 100)) / 100;
        }
        return damage;
    }

    @Override
    public void onAfterCardPlayed(AbstractCard card) {
        if (card.type == AbstractCard.CardType.ATTACK && card.cardID != FlamingChariot.ID) {
            AbstractPlayer p = AbstractDungeon.player;
            if (p.hasPower(ArchangelShieldPower.POWER_ID)) {
                int shieldAmount = p.getPower(ArchangelShieldPower.POWER_ID).amount;
                this.flash();
                AbstractDungeon.actionManager.addToBottom(
                        new GainBlockAction(p, p, this.amount * shieldAmount));
            }

            AbstractDungeon.actionManager.addToBottom(
                    new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
            if (card.cardID == FieryStrike.ID || card.cardID == ThreeLashes.ID) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p,
                        new FervorPower(p, p, card.magicNumber), card.magicNumber));
            }
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new FervorPower(owner, source, amount);
    }

    @Override
    public void update(int slot) {
        super.update(slot);
        for(int i = 0; i < java.lang.Math.min(this.amount/4, 15); ++i) {
            AbstractDungeon.effectsQueue.add(new FervorFlameEffect(this.owner.drawX, this.owner.drawY + 140F, this.amount / 20.0F ));
        }
    }


}

