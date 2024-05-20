package com.dragonchang.domain.dto.eastmoney;

import lombok.Data;

@Data
public class StockNewHolderRecordDTO {
    String SECUCODE;
    String SECURITY_CODE;
    String SHARES_TYPE;
    String HOLD_NUM_RATIO;
    String HOLD_NUM_CHANGE;
    String HOLD_NUM;
    String HOLDER_RANK;
    String HOLDER_NEW;
    String HOLDER_NAME;
    String END_DATE;
    String CHANGE_RATIO;
}
