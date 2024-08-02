package com.ssafy.bookkoo.memberservice.dto.response;

import com.ssafy.bookkoo.memberservice.enums.Gender;
import java.util.List;
import lombok.Builder;

@Builder
public record ResponseMemberInfoDto(
    String nickName,
    Gender gender,
    List<Integer> categories,
    int age,
    String introduction,
    String profileImgUrl
) {

}
