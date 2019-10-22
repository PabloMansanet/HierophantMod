package hierophant.cards;

import static hierophant.HierophantMod.makeCardPath;

import java.util.Iterator;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.AttackDamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import hierophant.HierophantMod;
import hierophant.characters.Hierophant;

public class LocustPlague extends AbstractDynamicCard {
    public static final String ID = HierophantMod.makeID(LocustPlague.class.getSimpleName());
    public static final String IMG = makeCardPath("LocustPlague.png");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Hierophant.Enums.COLOR_GOLD;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;

    private static final int COST = 3;

    private static final int DAMAGE = 4;
    private static final int UPGRADE_PLUS_DMG = 2;

    public LocustPlague() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = 0;
    }

    public int countCost() {
        int count = 0;
        Iterator iter = AbstractDungeon.player.hand.group.iterator();
        AbstractCard c;
        while(iter.hasNext()) {
            c = (AbstractCard)iter.next();
            if (c == this || c.costForTurn < 0) {
                continue;
            }
            count += c.costForTurn;
        }
        return count;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.magicNumber = baseMagicNumber = countCost();
        for (int i = 0; i < magicNumber; i++) {
            AbstractDungeon.actionManager.addToBottom(new AttackDamageRandomEnemyAction(this, AttackEffect.POISON));
        }
        this.rawDescription = DESCRIPTION;
        this.initializeDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        this.magicNumber = this.baseMagicNumber = countCost();
        this.rawDescription = DESCRIPTION +
            ((this.magicNumber == 1) ? EXTENDED_DESCRIPTION[0] : EXTENDED_DESCRIPTION[1]);
        initializeDescription();
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        this.magicNumber = this.baseMagicNumber = countCost();
        this.rawDescription = DESCRIPTION +
            ((this.magicNumber == 1) ? EXTENDED_DESCRIPTION[0] : EXTENDED_DESCRIPTION[1]);
        initializeDescription();
    }

    @Override
    public void onMoveToDiscard()
    {
        this.rawDescription = DESCRIPTION;
        initializeDescription();
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }
}
