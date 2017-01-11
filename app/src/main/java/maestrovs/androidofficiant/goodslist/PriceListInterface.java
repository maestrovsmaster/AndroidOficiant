package maestrovs.androidofficiant.goodslist;

import maestrovs.androidofficiant.essences.DicTable;
import maestrovs.androidofficiant.essences.Good;
import maestrovs.androidofficiant.essences.GoodGRP;

/**
 * Created by Vasily on 29.11.2016.
 */

public interface PriceListInterface {

    void clickOnPriceGroup(DicTable goodGRP);

    void clickOnPricePosition(Good good);


}
