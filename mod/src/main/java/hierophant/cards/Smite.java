package hierophant.cards;

import static hierophant.HierophantMod.makeCardPath;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import hierophant.HierophantMod;
import hierophant.characters.Hierophant;
import hierophant.effects.SmiteEffect;
import hierophant.powers.PietyPower;

public class Smite extends AbstractDynamicCard {
    public static final String POWER_ID = HierophantMod.makeID("PietyPower");
    public static final String ID = HierophantMod.makeID(Smite.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("Smite.png");
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Hierophant.Enums.COLOR_GOLD;

    private static final int COST = 1;
    private static final int PIETY = 6;
    private static final int UPGRADE_PLUS_PIETY = 3;

    public Smite() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        piety = basePiety = PIETY;
        baseDamage = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int combinedDamage = piety + HierophantMod.pietyGainedThisTurn;
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p,
                new PietyPower(p, p, piety), piety));

        this.baseDamage = combinedDamage;
        calculateCardDamage(m);
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.NONE));
        initializeDescription();
        AbstractDungeon.actionManager.addToTop(new VFXAction(
                    new SmiteEffect(p.drawX - 400.0F, p.drawY + 800.0F * Settings.scale), 0.2f));
        AbstractDungeon.actionManager.addToTop(new SFXAction("ORB_LIGHTNING_EVOKE"));
    }

    @Override
    public void applyPowers()
    {
        int combinedDamage = piety + HierophantMod.pietyGainedThisTurn;
        this.baseDamage = combinedDamage;
        super.applyPowers();

        this.rawDescription = DESCRIPTION;
        this.rawDescription += UPGRADE_DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void onMoveToDiscard()
    {
        this.rawDescription = DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo)
    {
        super.calculateCardDamage(mo);
        this.rawDescription = DESCRIPTION;
        this.rawDescription += UPGRADE_DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradePiety(UPGRADE_PLUS_PIETY);
        }
    }
}


