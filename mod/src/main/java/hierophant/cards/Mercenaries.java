package hierophant.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hierophant.HierophantMod;
import hierophant.characters.Hierophant;

import static hierophant.HierophantMod.makeCardPath;

public class Mercenaries extends AbstractDynamicCard {

    public static final String ID = HierophantMod.makeID(Mercenaries.class.getSimpleName());
    public static final String IMG = makeCardPath("Attack.png");

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Hierophant.Enums.COLOR_GOLD;
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    private static final int COST = 2;
    private static final int UPGRADED_COST = 2;

    private static final int DAMAGE = 10;
    private static final int UPGRADE_PLUS_DMG = 4;

    private static final int MAGIC = 4;
    private static final int UPGRADE_PLUS_MAGIC = -1;



    public Mercenaries() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = MAGIC;

    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.baseDamage = DAMAGE + HierophantMod.goldLostThisTurn / magicNumber;
        if (this.upgraded) {
            this.baseDamage += UPGRADE_PLUS_DMG;
        }
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
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
    public void applyPowers()
    {
        this.baseDamage = DAMAGE + HierophantMod.goldLostThisTurn / magicNumber;
        if (this.upgraded) {
            this.baseDamage += UPGRADE_PLUS_DMG;
        }
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
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            this.upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
        }
    }
}

