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
	private final String errorCode;         // 실패 시 에러 코드
	private final List<String> errorDetail; // 실패 시 상세 에러 메시지 목록
	private final T data;                   // 성공 시 반환할 데이터

	// 수정된 생성자
	private ApiResponse(ResultType result, int httpStatus, String errorCode, String message, List<String> errorDetail, T data) {
		this.result = result;
		this.httpStatus = httpStatus;
		this.errorCode = errorCode;
		this.message = message;
		this.errorDetail = errorDetail;
		this.data = data;
	}

	// successWithMessage를 지원하는 생성자 추가
	public ApiResponse(ResultType resultType, int value, String errorCode, String message, List<String> errorDetail, String successMessage, T data) {
		this.result = resultType;
		this.httpStatus = value;
		this.errorCode = errorCode;
		this.message = successMessage;
		this.errorDetail = errorDetail;
		this.data = data;
	}

	/**
	 * 성공 응답 (데이터 없이) - HTTP 상태 200
	 */
	public static <T> ApiResponse<T> success() {
		return new ApiResponse<>(ResultType.SUCCESS, HttpStatus.OK.value(), null, "요청이 성공적으로 처리되었습니다.", null, null);
	}

	/**
	 * 성공 응답 (데이터 포함) - HTTP 상태 200
	 */
	public static <T> ApiResponse<T> success(T data) {
		return new ApiResponse<>(ResultType.SUCCESS, HttpStatus.OK.value(), null, "요청이 성공적으로 처리되었습니다.", null, data);
	}

	/**
	 * 성공 응답 (성공 메시지 포함) - HTTP 상태 201
	 */
	public static <T> ApiResponse<T> successWithMessage(T data, String successMessage) {
		return new ApiResponse<>(ResultType.SUCCESS, HttpStatus.CREATED.value(), null, successMessage, null, data);
	}

	/**
	 * 에러 응답 (요약 메시지와 상세 메시지 모두 전달)
	 */
	public static <T> ApiResponse<T> error(String errorCode, String errorMessage, List<String> errorDetail, int httpStatus) {
		return new ApiResponse<>(ResultType.ERROR, httpStatus, errorCode, errorMessage, errorDetail, null);
	}

	/**
	 * 에러 응답 (BindingResult로부터 상세 에러 메시지 구성)
	 */
	public static <T> ApiResponse<T> error(String errorCode, BindingResult bindingResult, int httpStatus) {
		String summaryMessage = "입력값이 올바르지 않습니다.";
		List<String> detailMessages = createErrorDetail(bindingResult);
		return new ApiResponse<>(ResultType.ERROR, httpStatus, errorCode, summaryMessage, detailMessages, null);
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
}
