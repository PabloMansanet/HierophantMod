package hierophant.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import hierophant.HierophantMod;
import hierophant.characters.Hierophant;

import static hierophant.HierophantMod.makeCardPath;

public class Mercenaries extends AbstractTitheCard {

    public static final String ID = HierophantMod.makeID(Mercenaries.class.getSimpleName());
    public static final String IMG = makeCardPath("Mercenaries.png");

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Hierophant.Enums.COLOR_GOLD;
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;

    private static final int COST = 2;
    private static final int MAGIC = 2;
    private static final int UPGRADE_PLUS_MAGIC = 1;

    public Mercenaries() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = 0;
        magicNumber = baseMagicNumber = MAGIC;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        payTithe();
        this.baseDamage = HierophantMod.goldLostThisTurn * magicNumber / 2;
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        this.rawDescription = (this.upgraded ? UPGRADE_DESCRIPTION : DESCRIPTION);
        initializeDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo)
    {
        super.calculateCardDamage(mo);
        this.rawDescription = (this.upgraded ? UPGRADE_DESCRIPTION : DESCRIPTION);
        this.rawDescription += EXTENDED_DESCRIPTION[0];
        initializeDescription();
    }

    @Override
    public void applyPowers()
    {
        int tithe = GOLD_PER_ENERGY * (this.costForTurn - EnergyPanel.totalCount);
        this.baseDamage = (tithe + HierophantMod.goldLostThisTurn) * magicNumber / 2;
        super.applyPowers();
        this.rawDescription = (this.upgraded ? UPGRADE_DESCRIPTION : DESCRIPTION);
        this.rawDescription += EXTENDED_DESCRIPTION[0];
        initializeDescription();
    }

    @Override
    public void onMoveToDiscard()
    {
        this.rawDescription = (this.upgraded ? UPGRADE_DESCRIPTION : DESCRIPTION);
        initializeDescription();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
            this.upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
        }
    }
}

