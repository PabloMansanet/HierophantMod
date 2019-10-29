package hierophant.cards;

import static hierophant.HierophantMod.makeCardPath;

import java.util.Iterator;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;

import hierophant.HierophantMod;
import hierophant.characters.Hierophant;
import hierophant.tags.HierophantTags;

public class GoldFever extends AbstractDynamicCard {
    public static final String ID = HierophantMod.makeID(GoldFever.class.getSimpleName());
    public static final String IMG = makeCardPath("GoldFever.png");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Hierophant.Enums.COLOR_GOLD;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;

    private static final int COST = 2;

    private static final int MAGIC = 6;
    private static final int UPGRADE_PLUS_MAGIC = 4;

    public GoldFever() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        tags.add(HierophantTags.HIEROPHANT_GILDED);
        baseDamage = 0;
        magicNumber = baseMagicNumber = MAGIC;
    }

    public static int countDoubloons() {
        int count = 0;
        Iterator var1 = AbstractDungeon.player.discardPile.group.iterator();

        while(var1.hasNext()) {
            AbstractCard c = (AbstractCard)var1.next();
            if (c.cardID == Doubloon.ID) {
                ++count;
            }
        }

        return count;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.baseDamage = this.magicNumber * countDoubloons();
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AttackEffect.BLUNT_HEAVY));
        this.rawDescription = (this.upgraded ? UPGRADE_DESCRIPTION : DESCRIPTION);
        this.initializeDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        this.baseDamage = this.magicNumber * countDoubloons();
        super.calculateCardDamage(mo);
        this.isDamageModified = this.damage > 0;
        this.rawDescription = (this.upgraded ? UPGRADE_DESCRIPTION : DESCRIPTION) + EXTENDED_DESCRIPTION[0];
        initializeDescription();
    }

    @Override
    public void applyPowers() {
        this.baseDamage = this.magicNumber * countDoubloons();
        super.applyPowers();
        this.rawDescription = (this.upgraded ? UPGRADE_DESCRIPTION : DESCRIPTION) + EXTENDED_DESCRIPTION[0];
        initializeDescription();
    }

    @Override
    public void onMoveToDiscard()
    {
        this.rawDescription = (this.upgraded ? UPGRADE_DESCRIPTION : DESCRIPTION);
        initializeDescription();
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}
