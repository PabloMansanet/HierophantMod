
package hierophant.cards;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import hierophant.HierophantMod;
import hierophant.characters.Hierophant;

import static hierophant.HierophantMod.makeCardPath;

public class ChurchCoffers extends AbstractTitheCard {

    public static final String ID = HierophantMod.makeID(ChurchCoffers.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("ChurchCoffers.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Hierophant.Enums.COLOR_GOLD;

    private static final int COST = 2;
    private static final int UPGRADED_COST = 1;
    private static final int MAGIC = 2;

    // /STAT DECLARATION/

    public ChurchCoffers() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MAGIC;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        payTithe();
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, this.magicNumber));
        AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(this.magicNumber));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(UPGRADED_COST);
        }
    }
}
