package hierophant.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import hierophant.HierophantMod;
import hierophant.characters.Hierophant;
import hierophant.effects.PunishLightningEffect;
import hierophant.powers.PietyPower;

import static hierophant.HierophantMod.makeCardPath;

public class Punish extends AbstractDynamicCard {

    public static final String ID = HierophantMod.makeID(Punish.class.getSimpleName());
    public static final String IMG = makeCardPath("Punish.png");

    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Hierophant.Enums.COLOR_GOLD;

    private static final int COST = 2;

    private static final int DAMAGE = 8;
    private static final int UPGRADE_PLUS_DMG = 4;

    private static final int PIETY = 8;
    private static final int UPGRADE_PLUS_PIETY = 4;

    public Punish() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        piety = basePiety = PIETY;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.NONE));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p,
                new PietyPower(p, p, piety), piety));
        AbstractDungeon.actionManager.addToTop(new VFXAction(
                    new PunishLightningEffect(m.drawX, m.drawY), 0.2f));
        AbstractDungeon.actionManager.addToTop(new SFXAction("ORB_LIGHTNING_EVOKE"));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradePiety(UPGRADE_PLUS_PIETY);
        }
    }
}

