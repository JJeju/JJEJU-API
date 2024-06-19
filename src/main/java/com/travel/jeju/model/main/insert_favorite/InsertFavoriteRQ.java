package com.travel.jeju.model.main.insert_favorite;

import lombok.Data;

@Data
public class InsertFavoriteRQ {

    private String fa_fk_id; // 회원 아이디

    private String fa_fk_cnum; // 사업장 번호
    
}
