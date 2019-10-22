package hierophant.cards;

import hierophant.actions.CoinFlingAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hierophant.HierophantMod;
import hierophant.tags.HierophantTags;
import hierophant.characters.Hierophant;

import static hierophant.HierophantMod.makeCardPath;

public class CoinFling extends AbstractDynamicCard {

    public static final String ID = HierophantMod.makeID(CoinFling.class.getSimpleName());
    public static final String IMG = makeCardPath("CoinFling.png");

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Hierophant.Enums.COLOR_GOLD;

    private static final int COST = 0;

    private static final int DAMAGE = 6;
    private static final int UPGRADE_PLUS_DMG = 3;


    public CoinFling() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        tags.add(HierophantTags.HIEROPHANT_GILDED);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        calculateCardDamage(m);
        AbstractDungeon.actionManager.addToBottom(new CoinFlingAction(m, damage));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }
}

