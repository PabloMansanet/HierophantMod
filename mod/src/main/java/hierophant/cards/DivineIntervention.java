
package hierophant.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import static java.lang.Integer.min;
import hierophant.HierophantMod;
import hierophant.characters.Hierophant;
import hierophant.powers.PietyPower;
import hierophant.powers.FervorPower;

import static hierophant.HierophantMod.makeCardPath;

public class DivineIntervention extends AbstractDynamicCard {

    public static final String ID = HierophantMod.makeID(DivineIntervention.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("DivineIntervention.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Hierophant.Enums.COLOR_GOLD;

    private static final int COST = 1;
    private static final int MAGIC = 15;
    private static final int UPGRADE_PLUS_MAGIC = 15;

    public DivineIntervention() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MAGIC;
        basePiety = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int healAmount = min(magicNumber, m.maxHealth - m.currentHealth);
        if (healAmount == 0) {
            return;
        }
        basePiety = healAmount * 2;
        applyPowers();
        AbstractDungeon.actionManager.addToBottom(new HealAction(m, p, healAmount));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p,
                new PietyPower(p, p, piety), piety));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
        }
    }
}
