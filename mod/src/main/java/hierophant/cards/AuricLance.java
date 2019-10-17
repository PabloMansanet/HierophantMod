package hierophant.cards;

import static hierophant.HierophantMod.makeCardPath;

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
public class AuricLance extends AbstractTitheCard {

    public static final String ID = HierophantMod.makeID(AuricLance.class.getSimpleName());
    public static final String IMG = makeCardPath("Attack.png");

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Hierophant.Enums.COLOR_GOLD;
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    private static final int COST = 3;
    private static final int MAGIC = 15;
    private static final int UPGRADE_PLUS_MAGIC = 5;

    public AuricLance() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MAGIC;
        baseDamage = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        baseDamage = (p.gold * magicNumber) / 100;
        calculateCardDamage(m);
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));
        initializeDescription();
    }

    @Override
    public void applyPowers()
    {
        AbstractPlayer p = AbstractDungeon.player;
        baseDamage = (p.gold * magicNumber) / 100;
        super.applyPowers();
        this.rawDescription = DESCRIPTION;
        this.rawDescription += UPGRADE_DESCRIPTION;
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
    public void onMoveToDiscard()
    {
        this.rawDescription = DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
            initializeDescription();
        }
    }
}
