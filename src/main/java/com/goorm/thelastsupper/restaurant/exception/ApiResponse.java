package com.goorm.thelastsupper.restaurant.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ApiResponse<T> {
	private final ResultType result;        // 성공/실패 여부
	private final int httpStatus;           // HTTP 상태 코드
	private final String message;           // 요약 메시지
	private final List<String> errorDetail; // 실패 시 상세 에러 메시지 목록
	private final T data;                   // 성공 시 반환할 데이터

	public ApiResponse(ResultType result, int httpStatus, String message, List<String> errorDetail, T data) {
		this.result = result;
		this.httpStatus = httpStatus;
		this.message = message;
		this.errorDetail = errorDetail;
		this.data = data;
	}

	/**
	 * 에러 응답 (요약 메시지와 상세 메시지 모두 전달)
	 */
	/** 커스텀 메시지 + 상세 리스트 직접 전달 */
	public static <T> ApiResponse<T> error(String summary,
		List<String> detail,
		HttpStatus status) {
		return new ApiResponse<>(ResultType.ERROR, status.value(), summary, detail, null);
	}

	/**
	 * BindingResult에서 상세 에러 메시지 목록을 생성
	 * - 각 FieldError의 코드를 확인하여, 누락된 값인지 혹은 형식 오류인지를 추가로 안내합니다.
	 */
	private static List<String> createErrorDetail(BindingResult bindingResult) {
		List<String> errorList = new ArrayList<>();
		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			StringBuilder errorMsg = new StringBuilder();
			errorMsg.append("필드 [").append(fieldError.getField()).append("]: ");

			// FieldError의 코드 배열을 통해 누락 또는 형식 오류인지 판단
			String[] codes = fieldError.getCodes();
			boolean isMissing = false;
			boolean isTypeMismatch = false;
			if (codes != null) {
				for (String code : codes) {
					if (code != null) {
						if (code.contains("NotNull") || code.contains("NotBlank") || code.contains("NotEmpty")) {
							isMissing = true;
						}
						if (code.contains("typeMismatch")) {
							isTypeMismatch = true;
						}
					}
				}
			}
			if (isMissing) {
				errorMsg.append("필수 입력값이 누락되었습니다. ");
			}
			if (isTypeMismatch) {
				errorMsg.append("형식이 올바르지 않습니다. ");
			}
			// 기본 에러 메시지 추가
			errorMsg.append(fieldError.getDefaultMessage());
			errorList.add(errorMsg.toString());
		}
		return errorList;
	}

	public static <T> ApiResponse<T> success(T data, String message) {
		return new ApiResponse<>(
			ResultType.SUCCESS,
			HttpStatus.OK.value(),
			message,
			null,
			data
		);
	}


}
