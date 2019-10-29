package hierophant.cards;

import static hierophant.HierophantMod.makeCardPath;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import hierophant.HierophantMod;
import hierophant.characters.Hierophant;
import hierophant.powers.RefugePower;
import hierophant.tags.HierophantTags;

public class Refuge extends AbstractDynamicCard {

    public static final String ID = HierophantMod.makeID(Refuge.class.getSimpleName());
    public static final String IMG = makeCardPath("Refuge.png");

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = Hierophant.Enums.COLOR_GOLD;

    private static final int COST = 1;
    private static final int MAGIC = 4;
    private static final int UPGRADE_PLUS_MAGIC = 2;

    public Refuge() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        tags.add(HierophantTags.HIEROPHANT_GILDED);
        magicNumber = baseMagicNumber = MAGIC;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p,
                new RefugePower(p, p, magicNumber), magicNumber));
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
