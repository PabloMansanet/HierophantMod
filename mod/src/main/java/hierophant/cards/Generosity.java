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
import hierophant.powers.GenerosityPower;
import hierophant.tags.HierophantTags;

public class Generosity extends AbstractDynamicCard {

    public static final String ID = HierophantMod.makeID(Generosity.class.getSimpleName());
    public static final String IMG = makeCardPath("Generosity.png");

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = Hierophant.Enums.COLOR_GOLD;

    private static final int COST = 3;
    private static final int UPGRADED_COST = 2;

    public Generosity() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        tags.add(HierophantTags.HIEROPHANT_GILDED);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p,
                new GenerosityPower(p, p, 1), 1));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADED_COST);
            initializeDescription();
        }
    }
}
