
package hierophant.cards;

import static hierophant.HierophantMod.makeCardPath;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import hierophant.HierophantMod;
import hierophant.characters.Hierophant;
import hierophant.powers.FervorPower;
import hierophant.powers.PietyPower;

public class Exaltation extends AbstractDynamicCard {
    public static final String ID = HierophantMod.makeID(Exaltation.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("Exaltation.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Hierophant.Enums.COLOR_GOLD;

    private static final int COST = 1;
    private static final int UPGRADED_COST = 0;

    public Exaltation() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.piety = this.basePiety = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p.hasPower(FervorPower.POWER_ID)) {
            this.basePiety = p.getPower(FervorPower.POWER_ID).amount;
        }
        int fervorToGain = 0;
        if (p.hasPower(PietyPower.POWER_ID)) {
            fervorToGain = p.getPower(PietyPower.POWER_ID).amount;
        }
        applyPowers();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p,
                new FervorPower(p, p, fervorToGain), fervorToGain));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p,
                new PietyPower(p, p, this.piety), this.piety));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeBaseCost(UPGRADED_COST);
            this.upgradeName();
            this.initializeDescription();
        }
    }
}
