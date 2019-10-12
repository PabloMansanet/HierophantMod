package hierophant.rewards;

import basemod.abstracts.CustomReward;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class DonationReward extends CustomReward {
    private static final Texture ICON = new Texture(Gdx.files.internal("hierophantResources/images/ui/gold.png"));

    public int amount;
    public DonationReward(int amount) {
        super(ICON, amount + " Donation Gold", hierophant.patches.DonationRewardTypePatch.HIEROPHANT_DONATION_REWARD);
        this.amount = amount;
    }

    @Override
    public boolean claimReward() {
        AbstractDungeon.player.gainGold(this.amount);
        return true;
    }
}
