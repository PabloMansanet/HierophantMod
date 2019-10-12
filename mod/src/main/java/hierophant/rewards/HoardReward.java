package hierophant.rewards;

import basemod.abstracts.CustomReward;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;

public class HoardReward extends CustomReward {
    private static final Texture ICON = new Texture(Gdx.files.internal("hierophantResources/images/ui/gold.png"));

    public int amount;
    public HoardReward(int amount) {
        super(ICON, amount + " Hoarded Gold", hierophant.patches.HoardRewardTypePatch.HIEROPHANT_HOARD_REWARD);
        goldAmt = amount;
    }

    @Override
    public boolean claimReward() {
        AbstractDungeon.player.gainGold(this.goldAmt);
        return true;
    }

    public static void addHoardedGoldToRewards(int gold) {
      for (RewardItem i : AbstractDungeon.getCurrRoom().rewards) {
        if (i.type == hierophant.patches.HoardRewardTypePatch.HIEROPHANT_HOARD_REWARD) {
          i.goldAmt += gold;
          i.text = i.goldAmt + " Hoarded Gold";
          return;
        }
      }
      AbstractDungeon.getCurrRoom().rewards.add(new HoardReward(gold));
    }

}
