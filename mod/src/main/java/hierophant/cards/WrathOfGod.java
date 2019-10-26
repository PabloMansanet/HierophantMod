package hierophant.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;
import hierophant.HierophantMod;
import hierophant.characters.Hierophant;

import static hierophant.HierophantMod.makeCardPath;

public class WrathOfGod extends AbstractTitheCard {

    public static final String ID = HierophantMod.makeID(WrathOfGod.class.getSimpleName());
    public static final String IMG = makeCardPath("WrathOfGod.png");

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Hierophant.Enums.COLOR_GOLD;

    private static final int COST = 6;

    private static final int DAMAGE = 40;
    private static final int UPGRADE_PLUS_DMG = 20;
    private static final int UPGRADE_PLUS_MAGIC = 4;

    public WrathOfGod() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = DAMAGE/5;
        this.isMultiDamage = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        payTithe();
        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            AbstractDungeon.actionManager.addToBottom(new VFXAction(new WeightyImpactEffect(mo.hb.cX, mo.hb.cY)));
        }
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new WeightyImpactEffect(p.hb.cX, p.hb.cY)));
        AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.NONE));
        AbstractDungeon.actionManager.addToBottom( new DamageAction(p, new DamageInfo(p, magicNumber, damageTypeForTurn), AbstractGameAction.AttackEffect.NONE));
    }

    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        this.magicNumber = damage / 4;
        this.isDamageModified = this.damage != this.baseDamage;
        this.isMagicNumberModified = this.isDamageModified;
    }

    public void applyPowers() {
        super.applyPowers();
        this.magicNumber = damage / 4;
        this.isDamageModified = this.damage != this.baseDamage;
        this.isMagicNumberModified = this.isDamageModified;
        this.isDamageModified = this.damage != this.baseDamage;
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

