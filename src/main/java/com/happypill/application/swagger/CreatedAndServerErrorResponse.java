package com.happypill.application.swagger;

import com.happypill.application.exception.global.ErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses({
        @ApiResponse(responseCode = "201", description = "요청이 정상적으로 처리되어 리소스가 생성됐을 때"),
        @ApiResponse(responseCode = "500", description = "서버 내부에서 오류 발생 했을 때", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
})
public @interface CreatedAndServerErrorResponse {
}
