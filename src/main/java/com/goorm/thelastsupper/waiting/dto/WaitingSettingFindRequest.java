package com.goorm.thelastsupper.waiting.dto;

import com.goorm.thelastsupper.restaurant.entity.Restaurant;
import com.goorm.thelastsupper.waiting.entity.WaitingSetting;


import java.util.Optional;

//@Getter, @AllArgsConstructor Record파일은 이미 지원을 해줘서 작성을 하지 않아도 됨
public record WaitingSettingFindRequest(Restaurant restaurant, Optional<WaitingSetting> latestWaitingSetting) {
}
