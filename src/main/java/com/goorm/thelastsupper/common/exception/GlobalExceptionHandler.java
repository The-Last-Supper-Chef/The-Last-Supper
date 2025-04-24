package com.goorm.thelastsupper.common.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.ArrayList;
import java.util.List;

import com.goorm.thelastsupper.reservation.exception.ReservationErrorCode;
import com.goorm.thelastsupper.reservation.exception.ReservationException;
import com.goorm.thelastsupper.reservation.exception.SlotAlreadyExistsException;
import com.goorm.thelastsupper.restaurant.exception.ApiResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * 1. BindException Handler
	 * 폼 데이터(또는 쿼리 파라미터) 바인딩 과정에서 유효성 검증에 실패한 경우 발생하는 예외를 처리합니다.
	 * HTTP 상태 코드: 400 (BAD_REQUEST)
	 * 에러 코드: C-002 (INVALID_PARAMETER)
	 * 에러 요약 메시지: ErrorCode.INVALID_PARAMETER.getMessage()
	 * 에러 상세 메시지 목록: BindException 내부의 FieldError 정보를 기반으로 생성
	 *
	 * @param e BindException 객체
	 * @return 오류 응답(ResponseEntity<ApiResponse<?>>)
	 */
	@ExceptionHandler(BindException.class)
	protected ResponseEntity<ApiResponse<?>> handleBindException(BindException e) {
		log.error("[handleBindException] 발생", e);
		String summaryMessage = ErrorCode.INVALID_PARAMETER.getMessage();
		List<String> detailList = new ArrayList<>();
		for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
			StringBuilder errorMsg = new StringBuilder();
			errorMsg.append("필드 [").append(fieldError.getField()).append("]: ");
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
			errorMsg.append(fieldError.getDefaultMessage());
			detailList.add(errorMsg.toString());
		}
		ApiResponse<?> errorResponse = ApiResponse.error(
			ErrorCode.INVALID_PARAMETER.getErrorCode(),
			summaryMessage,
			detailList,
			ErrorCode.INVALID_PARAMETER.getHttpStatus().value()
		);
		return ResponseEntity.status(ErrorCode.INVALID_PARAMETER.getHttpStatus()).body(errorResponse);
	}

	/**
	 * 2. MethodArgumentNotValidException Handler (RequestBody)
	 * JSON RequestBody로 들어오는 DTO의 유효성 검증 실패 시 발생하는 예외를 처리합니다.
	 * HTTP 상태 코드: 400 (BAD_REQUEST)
	 * 에러 코드: C-002 (INVALID_PARAMETER)
	 * 에러 요약 메시지: ErrorCode.INVALID_PARAMETER.getMessage()
	 * 에러 상세 메시지 목록: MethodArgumentNotValidException 내부의 FieldError 정보를 기반으로 생성
	 *
	 * @param e MethodArgumentNotValidException 객체
	 * @return 오류 응답(ResponseEntity<ApiResponse<?>>)
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<ApiResponse<?>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		log.error("[handleMethodArgumentNotValidException] 발생", e);
		String summaryMessage = ErrorCode.INVALID_PARAMETER.getMessage();
		List<String> detailList = new ArrayList<>();
		for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
			StringBuilder errorMsg = new StringBuilder();
			errorMsg.append("필드 [").append(fieldError.getField()).append("]: ");
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
			errorMsg.append(fieldError.getDefaultMessage());
			detailList.add(errorMsg.toString());
		}
		ApiResponse<?> errorResponse = ApiResponse.error(
			ErrorCode.INVALID_PARAMETER.getErrorCode(),
			summaryMessage,
			detailList,
			ErrorCode.INVALID_PARAMETER.getHttpStatus().value()
		);
		return ResponseEntity.status(ErrorCode.INVALID_PARAMETER.getHttpStatus()).body(errorResponse);
	}

	/**
	 * 3. MethodArgumentTypeMismatchException Handler (예: enum 바인딩 실패)
	 * 파라미터 타입이 불일치할 때(예: enum 바인딩 실패) 발생하는 예외를 처리합니다.
	 * HTTP 상태 코드: 400 (BAD_REQUEST)
	 * 에러 코드: C-002 (INVALID_PARAMETER)
	 * 에러 요약 메시지: ErrorCode.INVALID_PARAMETER.getMessage()
	 * 에러 상세 메시지 목록: 파라미터 이름, 필요 타입, 예외 메시지 등
	 *
	 * @param e MethodArgumentTypeMismatchException 객체
	 * @return 오류 응답(ResponseEntity<ApiResponse<?>>)
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<ApiResponse<?>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
		log.error("[handleMethodArgumentTypeMismatchException] 발생", e);
		String summaryMessage = ErrorCode.INVALID_PARAMETER.getMessage();
		String detailMessage = String.format(
			"파라미터 '%s'의 값이 적절하지 않습니다. (필요한 타입: %s). 상세: %s",
			e.getName(),
			(e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "알 수 없음"),
			e.getMessage()
		);
		List<String> detailList = new ArrayList<>();
		detailList.add(detailMessage);
		ApiResponse<?> errorResponse = ApiResponse.error(
			ErrorCode.INVALID_PARAMETER.getErrorCode(),
			summaryMessage,
			detailList,
			ErrorCode.INVALID_PARAMETER.getHttpStatus().value()
		);
		return ResponseEntity.status(ErrorCode.INVALID_PARAMETER.getHttpStatus()).body(errorResponse);
	}

	/**
	 * 4. HttpRequestMethodNotSupportedException Handler
	 * 지원하지 않는 HTTP 메서드를 호출했을 때 발생하는 예외를 처리합니다.
	 * HTTP 상태 코드: 405 (METHOD_NOT_ALLOWED)
	 * 에러 코드: E-405 (METHOD_NOT_ALLOWED)
	 * 에러 요약 메시지: ErrorCode.METHOD_NOT_ALLOWED.getMessage()
	 * 에러 상세 메시지 목록: 요청 메서드, 지원 메서드 목록 등
	 *
	 * @param e HttpRequestMethodNotSupportedException 객체
	 * @return 오류 응답(ResponseEntity<ApiResponse<?>>)
	 */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	protected ResponseEntity<ApiResponse<?>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
		log.error("[handleHttpRequestMethodNotSupportedException] 발생", e);
		String summaryMessage = ErrorCode.METHOD_NOT_ALLOWED.getMessage();
		String detailMessage = String.format(
			"요청한 메서드: [%s], 사용 가능한 메서드: [%s].",
			e.getMethod(),
			(e.getSupportedMethods() != null ? String.join(", ", e.getSupportedMethods()) : "없음")
		);
		List<String> detailList = new ArrayList<>();
		detailList.add(detailMessage);
		ApiResponse<?> errorResponse = ApiResponse.error(
			ErrorCode.METHOD_NOT_ALLOWED.getErrorCode(),
			summaryMessage,
			detailList,
			ErrorCode.METHOD_NOT_ALLOWED.getHttpStatus().value()
		);
		return ResponseEntity.status(ErrorCode.METHOD_NOT_ALLOWED.getHttpStatus()).body(errorResponse);
	}

	/**
	 * 5. HttpMessageNotReadableException Handler
	 * 요청 본문이 누락되었거나(JSON 미제공 등) JSON 파싱에 실패했을 때 발생하는 예외를 처리합니다.
	 * HTTP 상태 코드: 400 (BAD_REQUEST)
	 * 에러 코드: C-003 (BODY_NOT_READABLE)
	 * 에러 요약 메시지: ErrorCode.BODY_NOT_READABLE.getMessage()
	 * 에러 상세 메시지 목록: 본문 누락 안내, JSON 파싱 오류 메시지 등
	 *
	 * @param e HttpMessageNotReadableException 객체
	 * @return 오류 응답(ResponseEntity<ApiResponse<?>>)
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	protected ResponseEntity<ApiResponse<?>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
		log.error("[handleHttpMessageNotReadableException] 발생", e);
		String summaryMessage = ErrorCode.BODY_NOT_READABLE.getMessage();
		List<String> detailList = new ArrayList<>();
		if(e.getMessage().contains("Required request body is missing")) {
			detailList.add("요청 본문이 전혀 전달되지 않았습니다. JSON 형식의 요청 본문을 포함하여 필수 입력 데이터를 제공해 주십시오.");
		} else {
			detailList.add(String.format("요청 바디 형식 오류. 원인: %s", e.getMessage()));
		}
		ApiResponse<?> errorResponse = ApiResponse.error(
			ErrorCode.BODY_NOT_READABLE.getErrorCode(),
			summaryMessage,
			detailList,
			ErrorCode.BODY_NOT_READABLE.getHttpStatus().value()
		);
		return ResponseEntity.status(ErrorCode.BODY_NOT_READABLE.getHttpStatus()).body(errorResponse);
	}

	/**
	 * 6. MissingServletRequestParameterException Handler
	 * 요청 파라미터가 누락되었을 때 발생하는 예외를 처리합니다.
	 * HTTP 상태 코드: 400 (BAD_REQUEST)
	 * 에러 코드: C-002 (INVALID_PARAMETER)
	 * 에러 요약 메시지: ErrorCode.INVALID_PARAMETER.getMessage()
	 * 에러 상세 메시지 목록: 누락된 파라미터 이름 등
	 *
	 * @param e MissingServletRequestParameterException 객체
	 * @return 오류 응답(ResponseEntity<ApiResponse<?>>)
	 */
	@ExceptionHandler(org.springframework.web.bind.MissingServletRequestParameterException.class)
	protected ResponseEntity<ApiResponse<?>> handleMissingServletRequestParameterException(org.springframework.web.bind.MissingServletRequestParameterException e) {
		log.error("[handleMissingServletRequestParameterException] 발생", e);
		String summaryMessage = ErrorCode.INVALID_PARAMETER.getMessage();
		String detailMessage = String.format("요청 파라미터 '%s'가 누락되었습니다.", e.getParameterName());
		List<String> detailList = new ArrayList<>();
		detailList.add(detailMessage);
		ApiResponse<?> errorResponse = ApiResponse.error(
			ErrorCode.INVALID_PARAMETER.getErrorCode(),
			summaryMessage,
			detailList,
			ErrorCode.INVALID_PARAMETER.getHttpStatus().value()
		);
		return ResponseEntity.status(ErrorCode.INVALID_PARAMETER.getHttpStatus()).body(errorResponse);
	}

	/**
	 * 7. NoHandlerFoundException Handler
	 * 존재하지 않는 엔드포인트를 호출했을 때 발생하는 예외를 처리합니다.
	 * HTTP 상태 코드: 500 (INTERNAL_SERVER_ERROR)
	 * 에러 코드: E-500 (INTERNAL_SERVER_ERROR)
	 * 에러 요약 메시지: "요청한 엔드포인트를 찾을 수 없습니다." 등
	 * 에러 상세 메시지 목록: 잘못된 URL, 메서드 정보 등
	 *
	 * @param e NoHandlerFoundException 객체
	 * @return 오류 응답(ResponseEntity<ApiResponse<?>>)
	 */
	@ExceptionHandler(NoHandlerFoundException.class)
	protected ResponseEntity<ApiResponse<?>> handleNoHandlerFoundException(NoHandlerFoundException e) {
		log.error("[handleNoHandlerFoundException] 발생", e);
		String summaryMessage = "요청한 엔드포인트를 찾을 수 없습니다.";
		List<String> detailList = new ArrayList<>();
		detailList.add(String.format("No endpoint %s %s.", e.getHttpMethod(), e.getRequestURL()));
		ApiResponse<?> errorResponse = ApiResponse.error(
			ErrorCode.INTERNAL_SERVER_ERROR.getErrorCode(),
			summaryMessage,
			detailList,
			ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus().value()
		);
		return ResponseEntity.status(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus()).body(errorResponse);
	}

	/**
	 * 8. ConstraintViolationException Handler
	 * 메서드 파라미터 검증(예: @Validated)에서 발생하는 ConstraintViolationException을 처리합니다.
	 * HTTP 상태 코드: 400 (BAD_REQUEST)
	 * 에러 코드: C-002 (INVALID_PARAMETER)
	 * 에러 요약 메시지: ErrorCode.INVALID_PARAMETER.getMessage()
	 * 에러 상세 메시지 목록: 어떤 값이 어떻게 잘못되었는지
	 *
	 * @param e ConstraintViolationException 객체
	 * @return 오류 응답(ResponseEntity<ApiResponse<?>>)
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	protected ResponseEntity<ApiResponse<?>> handleConstraintViolationException(ConstraintViolationException e) {
		log.error("[handleConstraintViolationException] 발생", e);
		String summaryMessage = ErrorCode.INVALID_PARAMETER.getMessage();
		List<String> detailList = new ArrayList<>();
		e.getConstraintViolations().forEach(violation -> {
			detailList.add(String.format("값 [%s]: %s", violation.getInvalidValue(), violation.getMessage()));
		});
		ApiResponse<?> errorResponse = ApiResponse.error(
			ErrorCode.INVALID_PARAMETER.getErrorCode(),
			summaryMessage,
			detailList,
			ErrorCode.INVALID_PARAMETER.getHttpStatus().value()
		);
		return ResponseEntity.status(ErrorCode.INVALID_PARAMETER.getHttpStatus()).body(errorResponse);
	}

	/**
	 * 1. ReservationException Handler
	 * Reservation 정의 비즈니스 로직 오류가 발생했을 때(ReservationException) 처리합니다.
	 * HTTP 상태 코드: ErrorCode에 정의된 값
	 * 에러 코드: ErrorCode에 정의된 값
	 * 에러 요약 메시지: ErrorCode에 정의된 message
	 * 에러 상세 메시지 목록: HTTP 상태, 로그 레벨 등 추가 정보
	 *
	 * @param e ReservationException 객체
	 * @return 오류 응답(ResponseEntity<ApiResponse<?>>)
	 */
	@ExceptionHandler(ReservationException.class)
	protected ResponseEntity<ApiResponse<?>> handlReservationException(ReservationException e) {
		log.error("[ReservationException] 발생", e);
		String summaryMessage = e.getErrorCode().getMessage();
		String detailMessage = String.format("HTTP 상태: %d, 로그 레벨: %s",
			e.getErrorCode().getHttpStatus().value());
		List<String> detailList = new ArrayList<>();
		detailList.add(detailMessage);
		ApiResponse<?> errorResponse = ApiResponse.error(
			"",
			summaryMessage,
			detailList,
			e.getErrorCode().getHttpStatus().value()
		);
		return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(errorResponse);
	}

	@ExceptionHandler(SlotAlreadyExistsException.class)
	public ResponseEntity<ApiResponse<?>> handleSlotAlreadyExistsException(SlotAlreadyExistsException e) {
		// 예외 메시지와 에러 코드 처리
		String message = e.getMessage();  // 예외에서 메시지를 추출
		ReservationErrorCode errorCode = e.getErrorCode();  // 에러 코드 추출

		// ApiResponse 객체를 생성하고, 메시지 및 코드 전달
		ApiResponse<?> response = ApiResponse.error(
			errorCode.getMessage(),
			message,
			List.of(message),
			HttpStatus.BAD_REQUEST.value()
		);

		// 적절한 HTTP 상태 코드와 함께 응답 반환
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}


	/**
	 * 10. ServletRequestBindingException Handler
	 * 요청 파라미터/헤더 등이 누락되어 발생하는 바인딩 예외를 처리합니다.
	 * HTTP 상태 코드: 400 (BAD_REQUEST)
	 * 에러 코드: C-002 (INVALID_PARAMETER)
	 * 에러 요약 메시지: ErrorCode.INVALID_PARAMETER.getMessage()
	 * 에러 상세 메시지 목록: e.getMessage() 등
	 */
	@ExceptionHandler(org.springframework.web.bind.ServletRequestBindingException.class)
	protected ResponseEntity<ApiResponse<?>> handleServletRequestBindingException(ServletRequestBindingException e) {
		log.error("[handleServletRequestBindingException] 발생", e);
		String summaryMessage = ErrorCode.INVALID_PARAMETER.getMessage();
		List<String> detailList = new ArrayList<>();
		detailList.add(e.getMessage());

		ApiResponse<?> errorResponse = ApiResponse.error(
			ErrorCode.INVALID_PARAMETER.getErrorCode(),
			summaryMessage,
			detailList,
			ErrorCode.INVALID_PARAMETER.getHttpStatus().value()
		);
		return ResponseEntity.status(ErrorCode.INVALID_PARAMETER.getHttpStatus()).body(errorResponse);
	}

	/**
	 * 11. MissingServletRequestPartException Handler
	 * 멀티파트(FormData)에서 특정 파트가 누락되었을 때 발생하는 예외를 처리합니다.
	 * HTTP 상태 코드: 400 (BAD_REQUEST)
	 * 에러 코드: C-002 (INVALID_PARAMETER)
	 */
	@ExceptionHandler(MissingServletRequestPartException.class)
	protected ResponseEntity<ApiResponse<?>> handleMissingServletRequestPartException(MissingServletRequestPartException e) {
		log.error("[handleMissingServletRequestPartException] 발생", e);
		String summaryMessage = ErrorCode.INVALID_PARAMETER.getMessage();
		List<String> detailList = new ArrayList<>();
		detailList.add(String.format("요청 파트 '%s'가 누락되었습니다.", e.getRequestPartName()));

		ApiResponse<?> errorResponse = ApiResponse.error(
			ErrorCode.INVALID_PARAMETER.getErrorCode(),
			summaryMessage,
			detailList,
			ErrorCode.INVALID_PARAMETER.getHttpStatus().value()
		);
		return ResponseEntity.status(ErrorCode.INVALID_PARAMETER.getHttpStatus()).body(errorResponse);
	}

	/**
	 * 12. Exception Handler
	 * 그 외 처리되지 않은 모든 예외(Catch-all)를 처리합니다.
	 * HTTP 상태 코드: 500 (INTERNAL_SERVER_ERROR)
	 * 에러 코드: E-500 (INTERNAL_SERVER_ERROR)
	 * 에러 요약 메시지: ErrorCode.INTERNAL_SERVER_ERROR.getMessage()
	 * 에러 상세 메시지 목록: 예외 원인 등
	 *
	 * @param e Exception 객체
	 * @return 오류 응답(ResponseEntity<ApiResponse<?>>)
	 */
	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ApiResponse<?>> handleException(Exception e) {
		log.error("[Exception] 발생", e);
		String summaryMessage = ErrorCode.INTERNAL_SERVER_ERROR.getMessage();
		String detailMessage = String.format("원인: %s", e.getMessage());
		List<String> detailList = new ArrayList<>();
		detailList.add(detailMessage);
		ApiResponse<?> errorResponse = ApiResponse.error(
			ErrorCode.INTERNAL_SERVER_ERROR.getErrorCode(),
			summaryMessage,
			detailList,
			ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus().value()
		);
		return ResponseEntity.status(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus()).body(errorResponse);
	}

	/**
	 * IllegalArgumentException을 처리하는 핸들러
	 * @param e IllegalArgumentException 예외 객체
	 * @return 오류 응답(ResponseEntity<ApiResponse<?>>)
	 */
	@ExceptionHandler(IllegalArgumentException.class)
	protected ResponseEntity<ApiResponse<?>> handleIllegalArgumentException(IllegalArgumentException e) {
		log.error("[handleIllegalArgumentException] 발생", e);

		String summaryMessage = "잘못된 요청입니다."; // 예외 메시지 설정
		List<String> detailList = new ArrayList<>();
		detailList.add(e.getMessage()); // 예외 메시지를 상세 메시지로 추가

		ApiResponse<?> errorResponse = ApiResponse.error(
			"INVALID_REQUEST",  // 오류 코드 설정
			summaryMessage,     // 요약 메시지
			detailList,         // 상세 메시지
			HttpStatus.BAD_REQUEST.value()  // HTTP 상태 코드 설정
		);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}
}
