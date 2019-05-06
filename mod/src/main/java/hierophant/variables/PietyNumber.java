package hierophant.variables;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import hierophant.HierophantMod;
import hierophant.cards.AbstractHierophantCard;

import static hierophant.HierophantMod.makeID;

public class PietyNumber extends DynamicVariable {
    @Override
    public String key() {
        return makeID("P");
    }

    @Override
    public boolean isModified(AbstractCard card) {
        return ((AbstractHierophantCard) card).isPietyModified;
    }

    @Override
    public int value(AbstractCard card) {
        return ((AbstractHierophantCard) card).piety;
    }

    @Override
    public int baseValue(AbstractCard card) {
        return ((AbstractHierophantCard) card).basePiety;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        return ((AbstractHierophantCard) card).upgradedPiety;
    }
}
