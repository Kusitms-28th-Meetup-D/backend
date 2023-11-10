package com.kusithm.meetupd.domain.review.service;

import com.kusithm.meetupd.common.error.EntityNotFoundException;
import com.kusithm.meetupd.domain.review.aws.AWSFeignClient;
import com.kusithm.meetupd.domain.review.aws.dto.request.SendEmailByAWSFeignRequestDto;
import com.kusithm.meetupd.domain.review.dto.request.UploadReviewRequestDto;
import com.kusithm.meetupd.domain.review.dto.response.GetIsUserReviewTeamResponseDto;
import com.kusithm.meetupd.domain.review.dto.response.GetUserReviewResponseDto;
import com.kusithm.meetupd.domain.review.dto.response.UploadReviewResponseDto;
import com.kusithm.meetupd.domain.review.entity.Review;
import com.kusithm.meetupd.domain.review.entity.UserReviewedTeam;
import com.kusithm.meetupd.domain.review.entity.WaitReview;
import com.kusithm.meetupd.domain.review.entity.inner.ReviewComment;
import com.kusithm.meetupd.domain.review.entity.inner.SelectKeyword;
import com.kusithm.meetupd.domain.review.entity.inner.SelectTeamCulture;
import com.kusithm.meetupd.domain.review.entity.inner.SelectWorkMethod;
import com.kusithm.meetupd.domain.review.mongo.ReviewRepository;
import com.kusithm.meetupd.domain.review.mongo.WaitReviewRepository;
import com.kusithm.meetupd.domain.review.mysql.UserReviewedTeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.kusithm.meetupd.common.error.ErrorCode.USER_RECOMMENDATION_NOT_FOUND;
import static com.kusithm.meetupd.domain.review.entity.inner.ReviewComment.createRecommendationComment;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final WaitReviewRepository waitReviewRepository;
    private final UserReviewedTeamRepository userReviewedTeamRepository;
    private final MongoTemplate mongoTemplate;
    private final AWSFeignClient awsFeignClient;

    public void createUserEmptyReview(Long userId){
        Review recommendation = Review.creatEmptyReview(userId);
        reviewRepository.save(recommendation);
    }

    public GetUserReviewResponseDto getUserReviewByUserId(Long userId) {
        Review review = getReviewByUserId(userId);
        return GetUserReviewResponseDto.of(review);
    }

    public UploadReviewResponseDto uploadReviews(Long sendUserId, UploadReviewRequestDto request) {
        // TODO 개발 테스트 편의를 위해 이미 유저가 팀에 리뷰를 작성했는지 여부는 주석 처리 했습니다.
//        if(checkUserReviewThisTeam(sendUserId, getTeamId(request)))
//            throw new ConflictException(DUPLICATE_USER_REVIEW_TEAM);
        List<WaitReview> waitReviews = makeWaitReviewListFromUploadRequest(request);
        waitReviews.forEach(this::uploadOrWaitReview);
        String uploadResultString = updateSendUserReviews(sendUserId, getTeamId(request));
        return UploadReviewResponseDto.of(uploadResultString);
    }

    public GetIsUserReviewTeamResponseDto isUserReviewThisTeam(Long userId, Long teamId) {
        return GetIsUserReviewTeamResponseDto.of(checkUserReviewThisTeam(userId, teamId));
    }

    private Long getTeamId(UploadReviewRequestDto request) {
        return request.getUploadReviews().get(0).getTeamId();
    }

    private Review getReviewByUserId(Long userId) {
        return reviewRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER_RECOMMENDATION_NOT_FOUND));
    }

    private static List<WaitReview> makeWaitReviewListFromUploadRequest(UploadReviewRequestDto request) {
        return request.getUploadReviews().stream()
                .map(WaitReview::of)
                .toList();
    }


    private void uploadOrWaitReview(WaitReview waitReview) {
        // 리뷰 받는 유저가 팀에 추천사를 남겼으면 바로 반영
        if(checkUserReviewThisTeam(waitReview.getUserId(), waitReview.getTeamId())) {
            uploadReview(waitReview);
        }
        else {
            // 아직 리뷰받는 유저가 팀에 추천사를 남기지 않았다면 대기 리뷰 document에 저장
            uploadWaitReview(waitReview);
        }

        // TODO 이메일로 인증 ~~님이 회원님에게 추천사를 작성했어요 어서 확인해보세요! 보내야함.
        // SendEmailByAWSFeignRequestDto.of(보낼 유저 이메일 , 팀 이름)
//        SendEmailByAWSFeignRequestDto request = SendEmailByAWSFeignRequestDto.of("ojy09291@naver.com", "내팀팀팀");
//        awsFeignClient.sendReviewEmail(request);
    }

    private String updateSendUserReviews(Long userId, Long teamId) {
        saveUserReviewedTeam(userId, teamId);
        List<WaitReview> userWaitReviews = getWaitReviewsByUserAndTeam(userId, teamId);
        userWaitReviews.forEach(this::uploadReview);
        userWaitReviews.forEach(this::deleteWaitReview);

        return userWaitReviews.isEmpty() ? "추천사를 성공적으로 등록했습니다.": "추천사를 성공적으로 등록했습니다.\n회원님에게 새로운 추천사가 등록되었습니다.";
    }

    private void saveUserReviewedTeam(Long userId, Long teamId) {
        userReviewedTeamRepository.save(UserReviewedTeam.of(userId, teamId));
    }

    private List<WaitReview> getWaitReviewsByUserAndTeam(Long userId, Long teamId) {
        return waitReviewRepository.findAllByUserIdAndTeamId(userId, teamId);
    }

    private Boolean checkUserReviewThisTeam(Long userId, Long teamId) {
        return userReviewedTeamRepository.existsByUserIdAndTeamId(userId, teamId);
    }

    private void uploadReview(WaitReview waitReview) {
        Query query = createFindByUserIdQuery(waitReview.getUserId());
        Update update = new Update();

        increaseChoiceCountInUpdate(waitReview.getSelectedKeywords(), update);
        increaseTeamCultureCountInUpdate(waitReview.getSelectedTeamCultures(), update);
        increaseWorkMethodCountInUpdate(waitReview.getSelectedWorkMethods(), update);

        // TODO 팀 repository에서 teamId를 통해 공모전의 이름을 가져와서 여기서 넣어야할듯
        if(isReviewHaveComment(waitReview)) {
            ReviewComment createComment = createRecommendationComment(waitReview.getTeamId(), "testContest", waitReview.getRecommendationComment());
            addCommentInUpdate(update, createComment);
        }

        mongoTemplate.updateMulti(query, update, Review.class);
    }

    private void deleteWaitReview(WaitReview waitReview) {
        mongoTemplate.remove(waitReview);
    }
    private void uploadWaitReview(WaitReview waitReview) {
        waitReviewRepository.save(waitReview);
    }

    private Query createFindByUserIdQuery(Long userId) {
        return new Query(Criteria.where("userId").is(userId));
    }

    private void increaseChoiceCountInUpdate(List<SelectKeyword> choiceKeywordCodes, Update update) {
        choiceKeywordCodes.forEach(keywordCode -> update.inc("multiple_chocies." + keywordCode.getSelectKeyword() + ".count", 1));
    }

    private void increaseTeamCultureCountInUpdate(SelectTeamCulture selectTeamCultureDto, Update update) {

        // 0의 경우 왼쪽 선택지를 count, 그외 경우(1)일 경우 오른쪽 선택지를 count
        update.inc("team_cultures." + 0 + (selectTeamCultureDto.getFeedbackStyle() == 0 ? ".left_count" : ".right_count"), 1);
        update.inc("team_cultures." + 1 + (selectTeamCultureDto.getTeamStyle() == 0 ? ".left_count" : ".right_count"), 1);
        update.inc("team_cultures." + 2 + (selectTeamCultureDto.getPersonalityStyle() == 0 ? ".left_count" : ".right_count"), 1);
    }
    private void increaseWorkMethodCountInUpdate(SelectWorkMethod workMethodSelects, Update update) {

        // 0의 경우 왼쪽 선택지를 count, 그외 경우(1)일 경우 오른쪽 선택지를 count
        update.inc("work_methods." + 0 + (workMethodSelects.getWorkStyle() == 0 ? ".left_count" : ".right_count"), 1);
        update.inc("work_methods." + 1 + (workMethodSelects.getResultProcess() == 0 ? ".left_count" : ".right_count"), 1);
        update.inc("work_methods." + 2 + (workMethodSelects.getWorkLifeBalance() == 0 ? ".left_count" : ".right_count"), 1);
    }

    private boolean isReviewHaveComment(WaitReview waitReview) {
        return waitReview.getRecommendationComment() != null;
    }

    private void addCommentInUpdate(Update update, ReviewComment createComment) {
        update.push("essays", createComment);
    }

}
