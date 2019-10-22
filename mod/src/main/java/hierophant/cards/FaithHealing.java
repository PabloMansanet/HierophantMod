
package hierophant.cards;

import static hierophant.HierophantMod.makeCardPath;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import hierophant.HierophantMod;
import hierophant.actions.FaithHealingAction;
import hierophant.characters.Hierophant;

public class FaithHealing extends AbstractDynamicCard {

    public static final String ID = HierophantMod.makeID(FaithHealing.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("FaithHealing.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Hierophant.Enums.COLOR_GOLD;

    private static final int COST = 1;
    private static final int PIETY = 8;
    private static final int PIETY_PLUS_UPGRADE = 4;

    public FaithHealing() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        piety = basePiety = PIETY;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new FaithHealingAction(p, piety));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradePiety(PIETY_PLUS_UPGRADE);
        }
    }
}
