package com.dragonchang.domain.dto.eastmoney;

import lombok.Data;

@Data
public class StockFreeHolderRecordDTO {
    String SECUCODE;
    String SECURITY_CODE;
    String END_DATE;
    String HOLDER_RANK;
    String HOLDER_NEW;
    String HOLDER_NAME;
    String HOLDER_TYPE;
    String SHARES_TYPE;
    String HOLD_NUM;
    String FREE_HOLDNUM_RATIO;
    String HOLD_NUM_CHANGE;
    String CHANGE_RATIO;
}
