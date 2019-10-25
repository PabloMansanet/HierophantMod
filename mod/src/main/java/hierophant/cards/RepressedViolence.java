package hierophant.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import hierophant.HierophantMod;
import java.util.Iterator;
import hierophant.characters.Hierophant;

import static hierophant.HierophantMod.makeCardPath;

public class RepressedViolence extends AbstractDynamicCard {

    public static final String ID = HierophantMod.makeID(RepressedViolence.class.getSimpleName());
    public static final String IMG = makeCardPath("RepressedViolence.png");

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Hierophant.Enums.COLOR_GOLD;

    private static final int COST = 0;

    private static final int DAMAGE = 3;

    private static final int MAGIC = 2;
    private static final int UPGRADE_PLUS_MAGIC = 1;

    public RepressedViolence() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        isEthereal = true;
        baseDamage = misc = DAMAGE;
        magicNumber = baseMagicNumber = MAGIC;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
    }

    @Override
    public void applyPowers() {
        this.baseDamage = this.misc;
        this.isDamageModified = false;
        super.applyPowers();
        this.initializeDescription();
    }

    @Override
    public void triggerOnExhaust() {
        Iterator iter = AbstractDungeon.player.masterDeck.group.iterator();
        AbstractCard c;
        while(iter.hasNext()) {
            c = (AbstractCard)iter.next();
            if (c.uuid.equals(this.uuid)) {
                c.misc += this.magicNumber;
                c.applyPowers();
                c.baseDamage = c.misc;
                c.isDamageModified = false;
            }
        }

        for(iter = GetAllInBattleInstances.get(this.uuid).iterator(); iter.hasNext(); c.baseDamage = c.misc) {
            c = (AbstractCard)iter.next();
            c.misc += this.magicNumber;
            c.applyPowers();
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
        }
    }
}

