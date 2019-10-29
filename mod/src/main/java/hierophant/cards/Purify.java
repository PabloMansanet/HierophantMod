package hierophant.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import static java.lang.Integer.min;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hierophant.HierophantMod;
import hierophant.characters.Hierophant;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import hierophant.powers.FervorPower;

import static hierophant.HierophantMod.makeCardPath;

public class Purify extends AbstractTitheCard {

    public static final String ID = HierophantMod.makeID(Purify.class.getSimpleName());
    public static final String IMG = makeCardPath("Purify.png");

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Hierophant.Enums.COLOR_GOLD;

    private static final int COST = 2;

    private static final int DAMAGE = 20;
    private static final int UPGRADE_PLUS_DMG = 10;

    private static final int MAGIC = 10;
    private static final int UPGRADE_PLUS_MAGIC = 5;

    public Purify() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = MAGIC;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        payTithe();
        int healAmount = min(magicNumber, m.maxHealth - m.currentHealth);
        AbstractDungeon.actionManager.addToBottom(new HealAction(m, p, healAmount));
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
        }
    }
}

