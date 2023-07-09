package com.sensegarden.sensegardenplaydev.ui.genericflow;

import com.sensegarden.sensegardenplaydev.models.genericflow.Category;

public interface GenericFlowInterface {
    void onFolderClicked(Category folder);

    void onFolderToMovePicked(Category folder);
    void onSelection(boolean isSomeSelected);
}
