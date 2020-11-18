package kr.hs.dgsw.dbook.dbook.Service;

import kr.hs.dgsw.dbook.dbook.Json.MypageResponse;
import kr.hs.dgsw.dbook.dbook.Json.TokenResponse;
import kr.hs.dgsw.dbook.dbook.VO.ProfileImg;

public interface MyPageService {
    MypageResponse getMyPageInfo(TokenResponse objToken);

    ProfileImg getProfileImageInfo(Integer imageNo);

    boolean updatePassword(TokenResponse objToken, String password);
}
